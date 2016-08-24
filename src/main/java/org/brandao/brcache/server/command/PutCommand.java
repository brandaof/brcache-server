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
 * Representa o comando PUT.
 * Sua sintaxe é:
 * PUT <name> <time> <size> <reserved>\r\n
 * <data>\r\n
 * END\r\n 
 * @author Brandao
 *
 */
public class PutCommand extends AbstractCommand{

	public void executeCommand(Terminal terminal, Cache cache, TerminalReader reader,
			TerminalWriter writer, String[] parameters)
			throws Throwable {
		
		String name;
        int time;
        int size;

        try{
            name = parameters[1];
        }
        catch(Throwable e){
            throw new ServerErrorException(ServerErrors.ERROR_1003, "name");
        }
        
        try{
            time = Integer.parseInt(parameters[3]);
        }
        catch(Throwable e){
            throw new ServerErrorException(ServerErrors.ERROR_1003, "time");
        }

        try{
            size = Integer.parseInt(parameters[4]);
        }
        catch(Throwable e){
            throw new ServerErrorException(ServerErrors.ERROR_1003, "size");
        }
        
        InputStream stream = null;
        try{
        	stream = reader.getStream(size);
            cache.putStream(
                name, 
                time, 
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
