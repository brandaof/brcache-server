package org.brandao.brcache.server.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.brandao.brcache.server.TerminalConstants;
import org.brandao.brcache.server.util.ArraysUtil;

public class TextContentInputStream 
	extends InputStream{

    private static final byte[] BOUNDARY = TerminalConstants.CRLF_DTA;
    
	private TextBufferReader buffer;
	
	private int size;
	
	private int read;
	
    private byte[] closeBuffer;
	
	public TextContentInputStream(TextBufferReader buffer, int size){
		this.buffer     = buffer;
		this.size       = size;
		this.read       = 0;
		this.closeBuffer = new byte[1024];
	}
	
    public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
		this.read = 0;
	}

	public int read() throws IOException{
    	
    	if(this.size == this.read ){
    		return -1;
    	}
    	
    	int val = this.buffer.read();
    	this.read++;
    	return val;
    }
	
    public int read(byte[] b, int off, int len) throws IOException{
    	
    	if(this.size == this.read ){
    		return -1;
    	}
    	
    	int maxRead = this.size - this.read; 
    	int toRead  = len > maxRead? maxRead : len;
    	int l       = this.buffer.read(b, off, toRead);
    	this.read  += l;
    	return l;
    }
    
    public void close() throws IOException{
    	
        if(size != read){
        	int toRead = size - read;
        	while(toRead > 0){
        		
        		int r = this.buffer.read(this.closeBuffer, 0, this.closeBuffer.length);
        		
        		if(r < 0){
                	throw new EOFException("premature end of data");
        		}
        		read   += r;
        		toRead -= r;
        	}
        }
    	
        this.buffer.read(this.closeBuffer, 0, 2);
        
        if(!ArraysUtil.startsWith(this.closeBuffer, BOUNDARY)){
        	throw new EOFException("premature end of data");
        }
        
    }
    
}
