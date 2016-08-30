package org.brandao.brcache.server.command;

import java.io.OutputStream;

import org.brandao.brcache.Cache;
import org.brandao.brcache.CacheInputStream;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalConstants;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;
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

	public void executeCommand(Terminal terminal, Cache cache, TerminalReader reader,
			TerminalWriter writer, String[] parameters)
			throws Throwable {

		String key;
		boolean forUpdate;
		
		try{
			key = parameters[1];
			
			if(key == null){
		        throw new NullPointerException();
			}
	    }
	    catch(Throwable e){
	        throw new ServerErrorException(ServerErrors.ERROR_1003, "key");
	    }
		
        try{
            forUpdate = !parameters[2].equals("0");
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
        			in = (CacheInputStream)txCache.get(key, forUpdate);
        		}
        	}
        	else{
        		in = (CacheInputStream) cache.getStream(key);
        	}
        	
            if(in != null){
                String responseMessage = 
            		"value " +
            		key +
            		TerminalConstants.SEPARATOR_COMMAND +
            		in.getSize() +
            		" 0";
                writer.sendMessage(responseMessage);
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
                String responseMessage =
            		"value " +
    				key +
    				" 0 0";
                writer.sendMessage(responseMessage);
            }
        }
        finally{
            if(in != null)
                in.close();
        }

        writer.sendMessage(TerminalConstants.BOUNDARY_MESSAGE);
        writer.flush();
        
	}

}