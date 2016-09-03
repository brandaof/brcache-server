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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.brandao.brcache.BRCacheConfig;
import org.brandao.brcache.BasicCache;
import org.brandao.brcache.Cache;
import org.brandao.brcache.Configuration;
import org.brandao.brcache.collections.Collections;
import org.brandao.brcache.tx.CacheTransactionManager;
import org.brandao.brcache.tx.CacheTransactionManagerImp;

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
    
    private BasicCache cache;
    
    private int readBufferSize;
    
    private int writeBufferSize;
    
    private TerminalFactory terminalFactory;
    
    private ExecutorService executorService;
    
    private Configuration config;
    
    private MonitorThread monitorThread;
    
    private boolean run;
    
    private boolean compress;
    
    private StreamFactory streamFactory;
    
    private TerminalSession globalTerminalInfo;
    
    private String dataPath;
    /**
     * Cria uma nova instância do cache.
     * 
     * @param config Configuração.
     */
    public BrCacheServer(Configuration config){
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
            int port, 
            int maxConnections, 
            int timeout, 
            boolean reuseAddress,
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
        this.terminalFactory = new TerminalFactory(1, this.maxConnections);
        this.serverSocket = new ServerSocket(this.port);
        this.serverSocket.setSoTimeout(this.timeout);
        this.serverSocket.setReuseAddress(this.reuseAddress);
        this.executorService = Executors.newFixedThreadPool(this.maxConnections);
        this.streamFactory = this.createStreamFactory();
        
        this.run = true;
        while(this.run){
            Socket socket = null;
            try{
                socket = this.serverSocket.accept();
                Terminal terminal = this.terminalFactory.getInstance();
                TerminalTask task = 
                    new TerminalTask(
                            terminal, 
                            this.cache, 
                            socket, 
                            this.streamFactory,
                            this.readBufferSize,
                            this.writeBufferSize,
                            this.terminalFactory,
                            this.globalTerminalInfo);
                
                this.executorService.execute(task);
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
        StreamFactory factory;
        if(this.compress)
            factory = new CompressStreamFactory();
        else
            factory = new DefaultStreamFactory();
        
        factory.setConfiguration(this.config);
        return factory;
    }
    
    private void loadConfiguration(Configuration config){
    	this.initProperties(config);
    	this.initCache(config);
    	this.initMonitorThread();
    	this.initGlobalTerminalInfo();
    }

    private void initProperties(Configuration config){
        long portNumber            = config.getLong("port","8084");
        long max_connections       = config.getLong("max_connections","1024");
        long timeout_connection    = config.getLong("timeout_connection","0");
        boolean reuse_address      = config.getBoolean("reuse_address", "false");
        String data_path           = config.getString("data_path","/var/brcache");
        long write_buffer_size     = config.getLong("write_buffer_size","16k");
        long read_buffer_size      = config.getLong("read_buffer_size","16k");
        boolean compressState      = config.getBoolean("compress_stream","false");
        
        this.run             = false;
        this.config          = config;
        this.timeout         = (int)timeout_connection;
        this.reuseAddress    = reuse_address;
        this.maxConnections  = (int)max_connections;
        this.port            = (int)portNumber;
        this.readBufferSize  = (int)read_buffer_size;
        this.writeBufferSize = (int)write_buffer_size;
        this.compress        = compressState;
        this.dataPath        = data_path;
        
        Collections.setPath(data_path);
        
    }
    
    private void initCache(Configuration config){
        boolean transactionSupport = config.getBoolean("transaction_support","false");
        long txTimeout             = config.getLong("transaction_time_out","300000");
        
        CacheTransactionManager txManager = 
        		(CacheTransactionManager)config.getObject(
        				"transaction_manager", 
        				CacheTransactionManagerImp.class.getName());

        BRCacheConfig brcacheConfig = new BRCacheConfig();
        brcacheConfig.setConfiguration(config);
        this.cache = new BasicCache(brcacheConfig);
        
        if(transactionSupport){
        	this.cache = this.cache.getTXCache(txManager, txTimeout);
        }
    }
    
    private void initMonitorThread(){
        this.monitorThread = new MonitorThread(this.cache, this.config);
        this.monitorThread.start();
    }
    
    private void initGlobalTerminalInfo(){
    	this.globalTerminalInfo     = new TerminalSession();
        BRCacheConfig brcacheConfig = this.cache.getConfig();
    	
        this.globalTerminalInfo.put("port", 				this.port);
        this.globalTerminalInfo.put("max_connections", 		this.maxConnections);
        this.globalTerminalInfo.put("timeout_connection",	this.timeout);
        this.globalTerminalInfo.put("reuse_address", 		this.reuseAddress);
        this.globalTerminalInfo.put("data_path",			this.dataPath);
        this.globalTerminalInfo.put("write_buffer_size",	this.writeBufferSize);
        this.globalTerminalInfo.put("read_buffer_size",		this.readBufferSize);
        this.globalTerminalInfo.put("compress_stream",		this.compress);
        
        this.globalTerminalInfo.put("nodes_buffer_size",	brcacheConfig.getNodesBufferSize());
        this.globalTerminalInfo.put("nodes_page_size",		brcacheConfig.getNodesPageSize());
        this.globalTerminalInfo.put("nodes_swap_factor",	brcacheConfig.getNodesSwapFactor());
        this.globalTerminalInfo.put("index_buffer_size",	brcacheConfig.getIndexBufferSize());
        this.globalTerminalInfo.put("index_page_size",		brcacheConfig.getIndexPageSize());
        this.globalTerminalInfo.put("index_swap_factor",	brcacheConfig.getIndexSwapFactor());
        this.globalTerminalInfo.put("data_buffer_size",		brcacheConfig.getDataBufferSize());
        this.globalTerminalInfo.put("data_block_size",		brcacheConfig.getDataBlockSize());
        this.globalTerminalInfo.put("data_page_size",		brcacheConfig.getDataPageSize());
        this.globalTerminalInfo.put("data_swap_factor",		brcacheConfig.getDataSwapFactor());
        this.globalTerminalInfo.put("max_size_entry",		brcacheConfig.getMaxSizeEntry());
        this.globalTerminalInfo.put("max_size_key",			brcacheConfig.getMaxSizeKey());
        this.globalTerminalInfo.put("swapper_thread",		brcacheConfig.getSwapperThread());
        this.globalTerminalInfo.put("data_path",			brcacheConfig.getDataPath());
        this.globalTerminalInfo.put("swapper_type",			brcacheConfig.getSwapper());
        
    }
    
    public StreamFactory getStreamFactory() {
        return streamFactory;
    }
    
}
