package org.brandao.brcache.server.io;

import java.io.EOFException;
import java.io.IOException;

import org.brandao.brcache.server.TerminalConstants;
import org.brandao.brcache.server.util.ArraysUtil;

public class TextInputStream extends LimitedSizeInputStream{

    private static final byte[] BOUNDARY = TerminalConstants.CRLF_DTA;
	
    private byte[] closeBuffer;
    
	public TextInputStream(BufferedInputStream buffer, int size) {
		super(buffer, size);
		this.closeBuffer = new byte[2];
	}

    public void close() throws IOException{
    	super.close();
    	
        this.buffer.read(this.closeBuffer, 0, 2);

        if(!ArraysUtil.equals(BOUNDARY, this.closeBuffer)){
        	throw new EOFException("premature end of data");
        }
    }
	
}
