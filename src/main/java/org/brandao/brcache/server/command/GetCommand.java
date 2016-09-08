package org.brandao.brcache.server.command;

import java.io.OutputStream;
import java.util.Arrays;

import org.brandao.brcache.BasicCache;
import org.brandao.brcache.CacheInputStream;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalConstants;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;
import org.brandao.brcache.server.util.ArraysUtil;
import org.brandao.brcache.tx.TXCache;

/**
 * Representa o comando <code>get</code>.
 * Sua sintaxe é:
 * <pre>
 * get &lt;key&gt; &lt;update&gt; &lt;reserved&gt;\r\n
 * </pre> 
 * @author Brandao
 *
 */
public class GetCommand extends AbstractCommand{

	private static final byte[] FALSE        = new byte[]{'0'};
	
	private static final byte[] PREFIX       = "value ".getBytes();

	private static final byte[] SUFFIX       = " 0".getBytes();
	
	private static final byte[] EMPTY_SUFFIX = " 0 0".getBytes();

	public void executeCommand(Terminal terminal, BasicCache cache, TerminalReader reader,
			TerminalWriter writer, byte[][] parameters)
			throws Throwable {

		String key;
		boolean forUpdate;
		
		try{
			key = ArraysUtil.toString(parameters[1]);
			
			if(key == null){
		        throw new NullPointerException();
			}
	    }
	    catch(Throwable e){
	        throw new ServerErrorException(ServerErrors.ERROR_1003, "key");
	    }
		
        try{
            forUpdate = !Arrays.equals(parameters[2], FALSE);
        }
        catch(Throwable e){
            throw new ServerErrorException(ServerErrors.ERROR_1003, "update");
        }
		
        CacheInputStream in = null;
        try{
        	if(forUpdate){
        		if(!(cache instanceof TXCache)){
        			throw new ServerErrorException(ServerErrors.ERROR_1009);
        		}
        		else{
        			TXCache txCache = (TXCache)cache;
        			in = (CacheInputStream)txCache.getStream(key, forUpdate);
        		}
        	}
        	else{
        		in = (CacheInputStream) cache.getStream(key);
        	}
        	
            if(in != null){
            	writer.write(PREFIX, 0, PREFIX.length);
            	writer.write(ArraysUtil.toBytes(key));
            	writer.write(TerminalConstants.SEPARATOR_COMMAND_DTA);
            	writer.write(ArraysUtil.toBytes(in.getSize()));
            	writer.write(SUFFIX, 0, SUFFIX.length);
            	
                OutputStream out = null;
                try{
                    out = writer.getStream();
                    in.writeTo(out);
                }
                finally{
                    if(out != null){
                        try{
                            out.close();
                        }
                        catch(Throwable e){
                        }
                    }
                    writer.sendCRLF();
                }
            }
            else{
            	writer.write(PREFIX, 0, PREFIX.length);
            	writer.write(ArraysUtil.toBytes(key));
            	writer.write(EMPTY_SUFFIX, 0, EMPTY_SUFFIX.length);
            }
        }
        finally{
            if(in != null)
                in.close();
        }

        writer.sendMessage(TerminalConstants.BOUNDARY_DTA);
        writer.flush();
        
	}

}
