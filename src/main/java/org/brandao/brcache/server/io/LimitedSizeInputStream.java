package org.brandao.brcache.server.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class LimitedSizeInputStream 
	extends InputStream{

	protected BufferedInputStream buffer;
	
	protected int size;
	
	protected int read;
	
	public LimitedSizeInputStream(BufferedInputStream buffer, int size){
		this.buffer     = buffer;
		this.size       = size;
		this.read       = 0;
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
    	int toRead = size - read;
    	while(toRead > 0){
    	    byte[] closeBuffer = new byte[1024];
    		int r = this.buffer.read(closeBuffer, 0, closeBuffer.length);
    		if(r < 0){
            	throw new EOFException("premature end of data");
    		}
    		read   += r;
    		toRead -= r;
    	}
    }
    
}
