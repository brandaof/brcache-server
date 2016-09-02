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

import java.net.Socket;

import org.brandao.brcache.BasicCache;

/**
 *
 * @author Brandao
 */
class TerminalTask implements Runnable{

    private final Terminal terminal;
    
    private final TerminalFactory factory;
    
    private final TerminalInfo terminalInfo;
    
    private BasicCache cache;
    
    private Socket socket;
    
    private int readBufferSize;
    
    private int writeBufferSize;
    
    private StreamFactory streamFactory;
    
    public TerminalTask(Terminal terminal, BasicCache cache, 
            Socket socket,
            StreamFactory streamFactory,
            int readBufferSize, int writeBufferSize, 
            TerminalFactory factory,
            TerminalInfo terminalInfo){
        this.terminal            = terminal;
        this.factory             = factory;
        this.cache               = cache;
        this.socket              = socket;
        this.readBufferSize      = readBufferSize;
        this.writeBufferSize     = writeBufferSize;
        this.terminalInfo        = terminalInfo;
        this.streamFactory       = streamFactory;
    }
    
    public void run() {
        try{
            updateInfo();
            this.terminal.init(this.socket, this.cache, 
                    this.streamFactory,
                    this.readBufferSize, this.writeBufferSize, this.createLocalTerminalInfo());
            this.terminal.execute();
        }
        catch(Throwable e){
            e.printStackTrace();
        }
        finally{
            try{
                terminal.destroy();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            this.factory.release(this.terminal);
            updateInfo();
        }
    }
    
    private TerminalInfo createLocalTerminalInfo(){
    	TerminalInfo lti = new TerminalInfo(this.terminalInfo);
    	
    	return lti;
    }
    
    private void updateInfo(){
        synchronized(TerminalTask.class){
            this.terminalInfo.put("curr_connections",  this.factory.getCurrentInstances());
            this.terminalInfo.put("total_connections", this.factory.getCountInstances());
        }
    }
}
