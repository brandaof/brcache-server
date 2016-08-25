package org.brandao.brcache.server.command;

import org.brandao.brcache.Cache;
import org.brandao.brcache.StorageException;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalConstants;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;

/**
 * Representa o comando REMOVE.
 * Sua sintaxe é:
 * DELETE <name> <reserved>\r\n
 * 
 * @author Brandao
 *
 */
public class RemoveCommand extends AbstractCommand{

	public void executeCommand(Terminal terminal, Cache cache, TerminalReader reader,
			TerminalWriter writer, String[] parameters)
			throws Throwable {

		boolean result;
		
		String name = parameters[1];
		
        if(name == null){
        	throw new ServerErrorException(ServerErrors.ERROR_1003, "name");        	
        }
		
        result = cache.remove(name);
    	
	    if(result){
	    	writer.sendMessage(TerminalConstants.SUCCESS);
	    }
	    else{
	    	writer.sendMessage(TerminalConstants.NOT_FOUND);
	    }
	    writer.flush();

    }

}
