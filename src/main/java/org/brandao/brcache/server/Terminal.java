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

import org.brandao.brcache.Cache;
import org.brandao.brcache.server.command.ExitCommand;
import org.brandao.brcache.server.command.GetCommand;
import org.brandao.brcache.server.command.GetForUpdateCommand;
import org.brandao.brcache.server.command.PutCommand;
import org.brandao.brcache.server.command.RemoveCommand;
import org.brandao.brcache.server.command.StatsCommand;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;

/**
 *
 * @author Brandao
 */
public class Terminal {
    
	private static final Command PUT    		= new PutCommand();

	private static final Command GET    		= new GetCommand();

	private static final Command GET_FOR_UPDATE	= new GetForUpdateCommand();
	
	private static final Command REMOVE 		= new RemoveCommand();

	private static final Command STATS  		= new StatsCommand();
	
	private static final Command EXIT   		= new ExitCommand();
	
    private Cache cache;
    
    private Socket socket;
    
    private boolean run;
    
    private TerminalReader reader;
    
    private TerminalWriter writer;
    
    private Configuration config;
    
    private int readBufferSize;
    
    private int writeBufferSize;
    
    public Terminal(Configuration config){
        this.run = false;
        this.config = config;
    }

    protected void init(Socket socket, Cache cache, 
            StreamFactory streamFactory,
            int readBufferSize, int writeBufferSize) throws IOException{
        try{
            this.socket = socket;
            this.cache  = cache;
            this.readBufferSize  = readBufferSize;
            this.writeBufferSize = writeBufferSize;
            this.reader = new TextTerminalReader(this.socket, streamFactory, readBufferSize);
            this.writer = new TextTerminalWriter(this.socket, streamFactory, writeBufferSize);
            this.run = true;
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
    
    public Configuration getConfiguration() {
		return config;
	}

	public void destroy() throws IOException{
        try{
            if(this.socket != null)
                this.socket.close();
        }
        finally{
            this.run    = false;
            this.cache  = null;
            this.reader = null;
            this.writer = null;
        }
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
               	if("get_for_update".equals(command[0])){
        			GET_FOR_UPDATE.execute(this, cache, reader, writer, command);
               	}
               	else 
               	if("remove".equals(command[0])){
        			REMOVE.execute(this, cache, reader, writer, command);
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
    
}
