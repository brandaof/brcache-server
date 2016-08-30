package org.brandao.brcache.server.command;

import java.io.InputStream;

import org.brandao.brcache.Cache;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalConstants;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;

/**
 * Representa o comando <code>replace</code>.
 * Sua sintaxe é:
 * <pre>
 * replace &lt;key&gt; &lt;timeToLive&gt; &lt;timeToIdle&gt; &lt;size&gt; &lt;reserved&gt;\r\n
 * &lt;data&gt;\r\n
 * end\r\n 
 * </pre> 
 * @author Brandao
 *
 */
public class ReplaceCommand extends AbstractCommand{

	public void executeCommand(Terminal terminal, Cache cache, TerminalReader reader,
			TerminalWriter writer, String[] parameters)
			throws Throwable {
		
        int timeToLive;
        int timeToIdle;
        int size;
		String key;

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
        	timeToLive = Integer.parseInt(parameters[2]);
        }
        catch(Throwable e){
            throw new ServerErrorException(ServerErrors.ERROR_1003, "timeToLive");
        }

        try{
        	timeToIdle = Integer.parseInt(parameters[3]);
        }
        catch(Throwable e){
            throw new ServerErrorException(ServerErrors.ERROR_1003, "timeToIdle");
        }

        try{
            size = Integer.parseInt(parameters[5]);
        }
        catch(Throwable e){
            throw new ServerErrorException(ServerErrors.ERROR_1003, "size");
        }
        
        InputStream stream = null;
        boolean result;
        try{
        	stream = reader.getStream(size);
        	result = cache.replaceStream(        	
                key, 
                stream,
                timeToLive,
                timeToIdle);
        }
        finally{
            if(stream != null)
                stream.close();
        }
        
        String end = reader.getMessage();
        
        if(!TerminalConstants.BOUNDARY_MESSAGE.equals(end)){
            throw new ServerErrorException(ServerErrors.ERROR_1004);
        }
        
    	writer.sendMessage(result? TerminalConstants.REPLACE_SUCCESS : TerminalConstants.NOT_STORED);
        writer.flush();
        
	}

}
