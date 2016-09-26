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

package org.brandao.brcache.server.io;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.brandao.brcache.server.util.ArraysUtil;

/**
 *
 * @author Brandao
 */
public class BufferedInputStream extends InputStream{
    
    private int offset;
    
    private int limit;
    
    private byte[] buffer;
    
    private int capacity;
    
    private InputStream stream;

    private boolean hasLineFeed;
    
    private int offsetResult;
    
    public BufferedInputStream(int capacity, InputStream stream){
        this.offset   = 0;
        this.limit    = 0;
        this.buffer   = new byte[capacity];
        this.capacity = capacity;
        this.stream   = stream;
    }

    public String readLine() throws IOException{
        byte[] data = readLineInBytes();
        return data == null? null : new String(data);
    }

    public int read() throws IOException{
    	
        if(this.offset == this.limit && this.checkBuffer() < 0){
        	return -1;
        }
    	
        return this.buffer[this.offset++];
    }
    
    public int read(byte[] b, int off, int len) throws IOException{
    	
    	int read  = 0;
    	
    	while(len > 0){
    		
            int maxRead = this.limit - this.offset;
            
            if(len > maxRead){
            	
                if(this.offset == this.limit && this.checkBuffer() < 0){
                	return read;
                }
                
            	ArraysUtil.arraycopy(this.buffer, this.offset, b, off, maxRead);
            	this.offset += maxRead;
            	off         += maxRead;
            	read        += maxRead;
            	len         -= maxRead;
            }
            else{
            	ArraysUtil.arraycopy(this.buffer, this.offset, b, off, len);
            	this.offset += len;
            	read        += len;
            	return read; 
            }
            
    	}
    	
    	return read;
    }
    
    private int checkBuffer() throws IOException{
        if(this.limit == this.capacity){
            this.offset = 0;
            this.limit  = 0;
        }
        
        int len = stream.read(this.buffer, this.limit, this.buffer.length - limit);
        
        if(len == -1){
        	return -1;
        }
        
        this.limit += len;
        return len;
    }
    
    public int readFullLineInBytes(byte[] b, int off, int len) throws IOException{
    	
    	int startOff = this.offset;
    	int read     = 0;

		int maxRead;
		
    	for(;;){
    		
    		if(len == 0){
    			throw new IOException("out of memory");
    		}
    		
            if(this.offset == this.limit){
        		maxRead  = this.offset - startOff;
        		
        		if(maxRead > len){
        			throw new IOException("out of memory");
        		}
        		
        		ArraysUtil.arraycopy(this.buffer, startOff, b, off, maxRead);
            	
            	len -= maxRead;
            	off += maxRead;
            	read+= maxRead;
            	
            	if(this.offset == this.limit && this.checkBuffer() < 0)
            		return read > 0? read : -1;
            	
            	startOff = this.offset;
            }
            
            if(this.buffer[this.offset++] == '\n'){
        		maxRead  = this.offset - startOff;
        		
        		if(maxRead > len){
        			throw new IOException("out of memory");
        		}
        		
        		if(this.offset < 2){
        			if(read == 0 || b[read -1] != '\r'){
            			throw new IOException("expected \\r");
        			}
        			else{
                		ArraysUtil.arraycopy(this.buffer, startOff, b, off - 1, maxRead - 1);
                    	read+= maxRead - 2;
        			}
        		}
        		else{
        			if(this.buffer[this.offset - 2] != '\r'){
            			throw new IOException("expected \\r");
        			}
        			else{
                		ArraysUtil.arraycopy(this.buffer, startOff, b, off, maxRead - 2);
                    	read+= maxRead - 2;
        			}
        		}
        		
            	return read;
            }
            
    	}
    	
    }
    
    public byte[] readLineInBytes() throws IOException{
    	
    	ByteArrayOutputStream bout = new ByteArrayOutputStream(12);
    	int startOff  = this.offset;
    	
    	for(;;){
            if(this.offset == this.limit){
            	bout.write(this.buffer, startOff, this.offset - startOff);
            	if(this.offset == this.limit && this.checkBuffer() < 0)
            		return bout.toByteArray();
            	startOff = this.offset;
            }
            
            if(this.buffer[this.offset++] == '\n'){
            	bout.write(this.buffer, startOff, this.offset - startOff);
            	byte[] array = bout.toByteArray();
            	
            	if(array.length > 0){ 
            		if(array[array.length-1] == '\r'){
                    	return Arrays.copyOf(array, array.length - 1);
            		}
            		else{
                        throw new IOException("expected \\r");
            		}
            	}
            	else{
            		return array;
            	}
            }
            
    	}
    	
    }
    
    public byte[] readLineInBytes(int totalRead) throws IOException{
    	
        byte[] result = new byte[totalRead];
        this.offsetResult = 0;
        
        int remainingToMaxRead = totalRead;
        int read;
        
        while(remainingToMaxRead > 0){

            if(this.offset == this.limit){
                
                if(this.limit == this.capacity){
                    this.offset = 0;
                    this.limit  = 0;
                }
                
                int len = stream.read(this.buffer, this.limit, this.buffer.length - limit);
                
                if(len == -1)
                    throw new EOFException("premature end of data");
                
                this.limit += len;
            }
            
            int maxRead = this.limit - this.offset;
            if(remainingToMaxRead > maxRead)
            	read = maxRead;
            else
            	read = remainingToMaxRead;
            
            ArraysUtil.arraycopy(this.buffer, this.offset, result, this.offsetResult, read);
            this.offsetResult += read;
        	//this.updateResult(this.buffer, this.offset, read);
            this.offset += read;
            remainingToMaxRead -= read;
        }
        
        return result;
    }
    
    public void clear(){
        this.offset = 0;
        this.limit  = 0;
    }

    public boolean isHasLineFeed() {
        return hasLineFeed;
    }
    
}
