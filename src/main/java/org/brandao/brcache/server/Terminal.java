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

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

import org.brandao.brcache.BasicCache;
import org.brandao.brcache.server.command.BeginTransactionCommand;
import org.brandao.brcache.server.command.CommitTransactionCommand;
import org.brandao.brcache.server.command.ExitCommand;
import org.brandao.brcache.server.command.GetCommand;
import org.brandao.brcache.server.command.PutCommand;
import org.brandao.brcache.server.command.RemoveCommand;
import org.brandao.brcache.server.command.ReplaceCommand;
import org.brandao.brcache.server.command.RollbackTransactionCommand;
import org.brandao.brcache.server.command.StatsCommand;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;
import org.brandao.brcache.tx.CacheTransaction;
import org.brandao.brcache.tx.CacheTransactionManager;
import org.brandao.brcache.tx.TXCache;

/**
 *
 * @author Brandao
 */
public class Terminal {
    
	public static final Command PUT    		= new PutCommand();

	public static final Command REPLACE   	= new ReplaceCommand();
	
	public static final Command GET    		= new GetCommand();

	public static final Command BEGIN_TX  	= new BeginTransactionCommand();
	
	public static final Command COMMIT_TX	= new CommitTransactionCommand();

	public static final Command ROLLBACK_TX	= new RollbackTransactionCommand();
	
	public static final Command REMOVE 		= new RemoveCommand();

	public static final Command STATS  		= new StatsCommand();
	
	public static final Command EXIT   		= new ExitCommand();
	
    private BasicCache cache;
    
    private Socket socket;
    
    private boolean run;
    
    private TerminalReader reader;
    
    private TerminalWriter writer;
    
    private TerminalInfo terminalInfo;
    
    private int readBufferSize;
    
    private int writeBufferSize;
    
    public Terminal(){
        this.run    = false;
    }

    protected void init(Socket socket, BasicCache cache, 
            StreamFactory streamFactory,
            int readBufferSize, int writeBufferSize, TerminalInfo terminalInfo) throws IOException{
        try{
            this.socket          = socket;
            this.cache           = cache;
            this.readBufferSize  = readBufferSize;
            this.writeBufferSize = writeBufferSize;
            this.reader          = new TextTerminalReader(this.socket, streamFactory, readBufferSize);
            this.writer          = new TextTerminalWriter(this.socket, streamFactory, writeBufferSize);
            this.run             = true;
            this.terminalInfo    = terminalInfo;
        }
        catch(Throwable e){
            if(this.socket != null)
                this.socket.close();
        }
        finally{
            if(this.socket == null || this.socket.isClosed())
                this.run = false;
        }
    }
    
    public TerminalInfo getConfiguration() {
		return this.terminalInfo;
	}

    private void closeTransaction(){
		try{
			if(this.cache instanceof TXCache){
				TXCache txCache = (TXCache)this.cache;
				CacheTransactionManager txManager = txCache.getTransactionManager();
				CacheTransaction currentTx = txManager.getCurrrent(false);
				if(currentTx != null){
					currentTx.rollback();
				}
			}
		}
		catch(Throwable e){
			e.printStackTrace();
		}
    }
    
    private void closeConnection(){
        try{
            if(this.socket != null)
                this.socket.close();
		}
		catch(Throwable e){
			e.printStackTrace();
		}
        finally{
            this.run    = false;
            this.cache  = null;
            this.reader = null;
            this.writer = null;
        }
    }
    
	public void destroy() throws IOException{
		this.closeTransaction();
		this.closeConnection();
    }
    
    public void execute() throws Throwable{
    	int index;
    	int start;
    	int end;
        String[] command = new String[6];
        
        while(this.run){
        	
            try{
                String message = reader.getMessage();
                
                index = 0;
                start = 0;
                
                while((end = message.indexOf(' ', start)) != -1){
                	String part = message.substring(start, end);
                	command[index++] = part;
                	start = end + 1;
                }
                
                command[index] = message.substring(start, message.length());
                
            	if("get".equals(command[0])){
        			GET.execute(this, cache, reader, writer, command);
            	}
            	else
               	if("put".equals(command[0])){
            		PUT.execute(this, cache, reader, writer, command);
               	}
               	else 
               	if("replace".equals(command[0])){
        			REPLACE.execute(this, cache, reader, writer, command);
               	}
               	else
               	if("remove".equals(command[0])){
        			REMOVE.execute(this, cache, reader, writer, command);
               	}
               	else
               	if("begin".equals(command[0])){
        			BEGIN_TX.execute(this, cache, reader, writer, command);
               	}
               	else
               	if("commit".equals(command[0])){
        			COMMIT_TX.execute(this, cache, reader, writer, command);
               	}
               	else
               	if("rollback".equals(command[0])){
        			ROLLBACK_TX.execute(this, cache, reader, writer, command);
               	}
               	else 
               	if("stats".equals(command[0])){
        			STATS.execute(this, cache, reader, writer, command);
               	}
               	else 
               	if("exit".equals(command[0])){
        			EXIT.execute(this, cache, reader, writer, command);
               	}
                else{
                    this.writer.sendMessage(ServerErrors.ERROR_1001.getString(command[0]));
                    this.writer.flush();
                }
            }
            catch (StringIndexOutOfBoundsException ex) {
            	ex.printStackTrace();
                this.writer.sendMessage(ServerErrors.ERROR_1002.getString());
                this.writer.flush();
            }
            catch (ServerErrorException ex) {
            	ex.printStackTrace();
            	if(ex.getCause() instanceof EOFException && !"premature end of data".equals(ex.getCause().getMessage()))
        			throw ex;
            	
                this.writer.sendMessage(ex.getMessage());
                this.writer.flush();
            }
            catch(Throwable ex){
            	ex.printStackTrace();
                throw ex;
            }
        }
    }

	public TerminalReader getReader() {
		return reader;
	}

	public TerminalWriter getWriter() {
		return writer;
	}

	public BasicCache getCache() {
		return cache;
	}

	public TerminalInfo getTerminalInfo() {
		return terminalInfo;
	}

}
