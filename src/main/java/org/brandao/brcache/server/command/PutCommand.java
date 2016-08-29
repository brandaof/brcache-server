package org.brandao.brcache.server.command;

import java.io.InputStream;

import org.brandao.brcache.Cache;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalConstants;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;
import org.brandao.brcache.tx.TXCache;

/**
 * Representa o comando PUT.
 * Sua sintaxe é:
 * PUT <key> <timeToLive> <timeToIdle> <update> <size> <reserved>\r\n
 * <data>\r\n
 * END\r\n 
 * @author Brandao
 *
 */
public class PutCommand extends AbstractCommand{

	public void executeCommand(Terminal terminal, Cache cache, TerminalReader reader,
			TerminalWriter writer, String[] parameters)
			throws Throwable {
		
        int timeToLive;
        int timeToIdle;
        boolean forUpdate;
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
            forUpdate = !parameters[4].equals("0");
        }
        catch(Throwable e){
            throw new ServerErrorException(ServerErrors.ERROR_1003, "update");
        }
        
        try{
            size = Integer.parseInt(parameters[5]);
        }
        catch(Throwable e){
            throw new ServerErrorException(ServerErrors.ERROR_1003, "size");
        }
        
        InputStream stream = null;
        try{
        	stream = reader.getStream(size);
        	if(forUpdate){
        		if(!(cache instanceof TXCache)){
        			throw new ServerErrorException(ServerErrors.ERROR_1009);
        		}
        		else{
        			((TXCache)cache).putStream(key, timeToLive, timeToIdle, inputData)
        		}
        	}
            cache.putStream(
                key, 
                timeToLive,
                timeToIdle,
                stream);
        }
        finally{
            if(stream != null)
                stream.close();
        }
        
        String end = reader.getMessage();
        
        if(!TerminalConstants.BOUNDARY_MESSAGE.equals(end)){
            throw new ServerErrorException(ServerErrors.ERROR_1004);
        }
        	
        writer.sendMessage(TerminalConstants.SUCCESS);
        writer.flush();
        
	}

}
