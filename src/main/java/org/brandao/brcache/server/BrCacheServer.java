/*
 * BRCache http://brcache.brandao.org/
 * Copyright (C) 2015 Afonso Brandao. (afonso.rbn@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.brandao.brcache.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.brandao.brcache.BRCacheConfig;
import org.brandao.brcache.BasicCache;
import org.brandao.brcache.BasicCacheHandler;
import org.brandao.brcache.Cache;
import org.brandao.brcache.CacheConstants;
import org.brandao.brcache.CacheHandler;
import org.brandao.brcache.Configuration;
import org.brandao.brcache.PropertiesBRCacheConfig;
import org.brandao.brcache.server.io.DefaultStreamFactory;
import org.brandao.brcache.server.io.StreamFactory;
import org.brandao.brcache.tx.CacheTransactionManager;
import org.brandao.brcache.tx.CacheTransactionManagerImp;
import org.brandao.brcache.tx.TXCache;

/**
 * Representa o servidor do cache.
 * 
 * @author Brandao
 */
public class BrCacheServer {
    
    private ServerSocket serverSocket;
    
    private int port;
    
    private int maxConnections;
    
    private int timeout;
    
    private boolean reuseAddress;
    
    volatile int countConnections;
    
    private Cache cache;
    
    private int readBufferSize;
    
    private int writeBufferSize;
    
    private TerminalFactory terminalFactory;
    
    private ExecutorService executorService;
    
    private Configuration config;
    
    private MonitorThread monitorThread;
    
    private boolean run;
    
    private StreamFactory streamFactory;
    
    private TerminalVars globalVars;
    
    private String dataPath;
    
    private boolean txSupport;
    
    private long txTimeout;
    
    private CacheTransactionManager txManager; 
    
    private InetAddress address;
    
    private int backlog;
    
    /**
     * Cria uma nova instância do cache.
     * 
     * @param config Configuração.
     * @throws UnknownHostException 
     */
    public BrCacheServer(Configuration config) throws UnknownHostException{
        this.loadConfiguration(config);
    }
    
    /**
     * Cria uma nova instância do cache.
     * 
     * @param port Porta que o servidor irá escutar.
     * @param maxConnections Número máximo de conexões permitidas.
     * @param timeout Define o timeout da conexão em milesegundos.
     * @param reuseAddress Liga ou desliga a opção do socket SO_REUSEADDR.
     * @param cache Cache.
     */
    public BrCacheServer(
    		InetAddress address,
            int port, 
            int maxConnections, 
            int timeout, 
            boolean reuseAddress,
            int backlog,
            Cache cache){
        this.run            = false;
        this.timeout        = timeout;
        this.reuseAddress   = reuseAddress;
        this.maxConnections = maxConnections;
        this.port           = port;
        this.cache          = cache;
    }
    
    /**
     * Inicia o servidor.
     * 
     * @throws IOException Lançada se ocorrer alguma falha ao tentar iniciar 
     * o servidor.
     */
    public void start() throws IOException{
        this.terminalFactory = new TerminalFactory(1, maxConnections);
        this.serverSocket    = new ServerSocket(port, backlog, address);
        this.executorService = Executors.newFixedThreadPool(maxConnections);
        this.streamFactory   = createStreamFactory();
        
        this.serverSocket.setSoTimeout(timeout);
        this.serverSocket.setReuseAddress(reuseAddress);
        
        this.run = true;
        while(this.run){
            Socket socket = null;
            try{
                socket = serverSocket.accept();
                //socket.setTcpNoDelay(true);

                Terminal terminal = terminalFactory.getInstance();
                TerminalTask task = 
                    new TerminalTask(
                            terminal, 
                            cache, 
                            socket, 
                            streamFactory,
                            readBufferSize,
                            writeBufferSize,
                            terminalFactory,
                            globalVars);
                
                executorService.execute(task);
            }
            catch(Exception e){
                //e.printStackTrace();
            }
        }
    }
    
    /**
     * Para o servidor.
     * 
     * @throws IOException Lançada se ocorrer alguma falha ao tentar parar 
     * o servidor.
     */
    public void stop() throws IOException{
        this.run = false;
        try{
            this.executorService.shutdownNow();
        }
        finally{
            this.serverSocket.close();
        }
    }
    
    private StreamFactory createStreamFactory(){
        StreamFactory factory = new DefaultStreamFactory();
        factory.setConfiguration(this.config);
        return factory;
    }
    
    private void loadConfiguration(Configuration config) throws UnknownHostException{
    	this.initProperties(config);
    	this.initCache(config);
    	this.initMonitorThread();
    	this.initGlobalTerminalInfo();
    }

    private void initProperties(Configuration config) throws UnknownHostException{
        int backlog  			= config.getInt(ServerConstants.BACKLOG,					"10");
        String addressName		= config.getString(ServerConstants.ADDRESS,					null);
        int portNumber			= config.getInt(ServerConstants.PORT,						"1044");
        int max_connections		= config.getInt(ServerConstants.MAX_CONNECTIONS,			"1024");
        int timeout_connection	= config.getInt(ServerConstants.TIMEOUT_CONNECTION,			"1000");
        boolean reuse_address	= config.getBoolean(ServerConstants.REUSE_ADDRESS,			"false");
        String data_path		= config.getString(CacheConstants.DATA_PATH,				"/var/brcache");
        int write_buffer_size	= config.getInt(ServerConstants.WRITE_BUFFER_SIZE,			"8k");
        int read_buffer_size	= config.getInt(ServerConstants.READ_BUFFER_SIZE,			"8k");
        boolean txSupport		= config.getBoolean(ServerConstants.TRANSACTION_SUPPORT,	"false");
        long txTimeout			= config.getLong(ServerConstants.TRANSACTION_TIMEOUT,		"300000");
        Object txManager		= config.getObject(ServerConstants.TRANSACTION_MANAGER,		CacheTransactionManagerImp.class.getName());
        
        this.run                = false;
        this.config             = config;
        this.timeout            = timeout_connection;
        this.reuseAddress       = reuse_address;
        this.maxConnections     = max_connections;
        this.port               = portNumber;
        this.readBufferSize     = read_buffer_size;
        this.writeBufferSize    = write_buffer_size;
        this.dataPath           = data_path;
        this.txSupport          = txSupport;
        this.txTimeout          = txTimeout;
        this.address            = addressName == null? null : InetAddress.getByName(addressName);
        this.backlog            = backlog;
        this.txManager          = (CacheTransactionManager)txManager;
        
        //Collections.setPath(data_path);
    }
    
    private void initCache(Configuration c) {
        BRCacheConfig config      = new PropertiesBRCacheConfig(c);
        CacheHandler cacheHandler = new BasicCacheHandler("default", config);
        
        if(txSupport){
            txManager.setPath(config.getDataPath() + "/tx");
            txManager.setTimeout(txTimeout);
        	cache = new TXCache(cacheHandler, txManager);
        }
        else{
        	cache = new BasicCache(cacheHandler);
        }
    }
    
    private void initMonitorThread(){
        this.monitorThread = new MonitorThread(this.cache, this.config);
        this.monitorThread.start();
    }
    
    private void initGlobalTerminalInfo(){
    	this.globalVars             = new TerminalVars();
        BRCacheConfig brcacheConfig = this.cache.getConfig();
    	
        this.globalVars.put("port", 				this.port);
        this.globalVars.put("max_connections", 		this.maxConnections);
        this.globalVars.put("timeout_connection",	this.timeout);
        this.globalVars.put("reuse_address", 		this.reuseAddress);
        this.globalVars.put("data_path",			this.dataPath);
        this.globalVars.put("write_buffer_size",	this.writeBufferSize);
        this.globalVars.put("read_buffer_size",		this.readBufferSize);
        this.globalVars.put("transaction_support",	this.txSupport);
        this.globalVars.put("transaction_timeout",	this.txTimeout);
        
        this.globalVars.put("nodes_buffer_size",	brcacheConfig.getNodesBufferSize());
        this.globalVars.put("nodes_page_size",		brcacheConfig.getNodesPageSize());
        this.globalVars.put("nodes_swap_factor",	brcacheConfig.getNodesSwapFactor());
        this.globalVars.put("index_buffer_size",	brcacheConfig.getIndexBufferSize());
        this.globalVars.put("index_page_size",		brcacheConfig.getIndexPageSize());
        this.globalVars.put("index_swap_factor",	brcacheConfig.getIndexSwapFactor());
        this.globalVars.put("data_buffer_size",		brcacheConfig.getDataBufferSize());
        this.globalVars.put("data_block_size",		brcacheConfig.getDataBlockSize());
        this.globalVars.put("data_page_size",		brcacheConfig.getDataPageSize());
        this.globalVars.put("data_swap_factor",		brcacheConfig.getDataSwapFactor());
        this.globalVars.put("max_size_entry",		brcacheConfig.getMaxSizeEntry());
        this.globalVars.put("max_size_key",			brcacheConfig.getMaxSizeKey());
        this.globalVars.put("swapper_thread",		brcacheConfig.getSwapperThread());
        this.globalVars.put("data_path",			brcacheConfig.getDataPath());
        
    }
    
    public StreamFactory getStreamFactory() {
        return this.streamFactory;
    }
    
}
