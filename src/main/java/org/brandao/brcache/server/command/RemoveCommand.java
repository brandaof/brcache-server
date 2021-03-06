package org.brandao.brcache.server.command;

import org.brandao.brcache.BasicCache;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalConstants;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;
import org.brandao.brcache.server.util.ArraysUtil;

/**
 * Representa o comando <code>remove</code>.
 * Sua sintaxe é:
 * <pre>
 * delete &lt;name&gt; &lt;reserved&gt;\r\n
 * </pre> 
 * @author Brandao
 *
 */
public class RemoveCommand extends AbstractCommand{

	public void executeCommand(Terminal terminal, BasicCache cache, TerminalReader reader,
			TerminalWriter writer, byte[][] parameters)
			throws Throwable {

		boolean result;
		
		String name = ArraysUtil.toString(parameters[1]);
		
        if(name == null){
        	throw new ServerErrorException(ServerErrors.ERROR_1003, "name");        	
        }
		
        result = cache.remove(name);
    	
	    if(result){
	    	writer.sendMessage(TerminalConstants.SUCCESS_DTA);
	    }
	    else{
	    	writer.sendMessage(TerminalConstants.NOT_FOUND_DTA);
	    }
	    writer.flush();

    }

}
