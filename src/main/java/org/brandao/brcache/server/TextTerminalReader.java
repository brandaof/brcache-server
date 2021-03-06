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
import java.io.InputStream;
import java.net.Socket;

import org.brandao.brcache.server.io.StreamFactory;
import org.brandao.brcache.server.io.BufferedInputStream;
import org.brandao.brcache.server.io.LimitedSizeInputStream;
import org.brandao.brcache.server.io.TextInputStream;
import org.brandao.brcache.server.io.TextInputStreamReader;

/**
 *
 * @author Brandao
 */
public class TextTerminalReader implements TerminalReader{

    @SuppressWarnings("unused")
	private Socket socket;
    
    private InputStream stream;
    
    private BufferedInputStream buffer;
    
    private LimitedSizeInputStream textContentInputStream;
    
    private int offset;
    
    public TextTerminalReader(Socket socket, StreamFactory streamFactory, int readBufferSize) throws IOException{
        this.socket = socket;
        this.stream = streamFactory.createInpuStream(socket);
        this.buffer = new BufferedInputStream(readBufferSize, this.stream);
        this.textContentInputStream = new TextInputStream(buffer, 0);
        this.offset = 0;
    }
    
    public InputStream getStream() {
        return new TextInputStreamReader(this.buffer, this.offset);
    }

    public InputStream getStream(int size) {
    	textContentInputStream.setSize(size);
    	return textContentInputStream;
    }
    
    public int getOffset() {
        return offset;
    }

    public int readMessage(byte[] b, int off, int len) throws ReadDataException{
    	try{
    		return this.buffer.readFullLine(b, off, len);
		}
		catch(IOException e){
			throw new ReadDataException(e);
		}
    }
    
	public String getMessage() throws ReadDataException {
		try{
			return this.buffer.readLine();
		}
		catch(IOException e){
			throw new ReadDataException(e);
		}
	}

	public byte[] getMessageBytes() throws ReadDataException {
		try{
			return this.buffer.readLineInBytes();
		}
		catch(IOException e){
			throw new ReadDataException(e);
		}
	}
	
}
