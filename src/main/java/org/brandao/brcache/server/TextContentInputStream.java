package org.brandao.brcache.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class TextContentInputStream 
	extends InputStream{

    private static final byte[] BOUNDARY = 
             (TerminalConstants.CRLFText + 
             TerminalConstants.BOUNDARY_MESSAGE + 
             TerminalConstants.CRLFText).getBytes();
    
	private TextBufferReader buffer;
	
	private int size;
	
	private int read;
	
	public TextContentInputStream(TextBufferReader buffer, int size){
		this.buffer = buffer;
		this.size   = size;
		this.read   = 0;
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
        	byte[] tmp = new byte[1024];
        	int toRead = size - read;
        	while(toRead > 0){
        		read += this.buffer.read(tmp, 0, tmp.length);
        		toRead = size - read;
        	}
        }
    	
        byte[] limit = new byte[7];
        this.buffer.read(limit, 0, limit.length);
        
        if(!Arrays.equals(limit, BOUNDARY)){
        	throw new EOFException("premature end of data");
        }
        
    }
    
}
