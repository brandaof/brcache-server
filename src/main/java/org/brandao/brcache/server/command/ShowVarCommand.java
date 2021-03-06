package org.brandao.brcache.server.command;

import org.brandao.brcache.BasicCache;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;
import org.brandao.brcache.server.util.ArraysUtil;

/**
 * Representa o comando <code>show_var</code>.
 * Sua sintaxe é:
 * <pre>
 * show_var
 * </pre> 
 * @author Brandao
 *
 */
public class ShowVarCommand 
	extends AbstractCommand{

	public void executeCommand(Terminal terminal, BasicCache cache, TerminalReader reader,
			TerminalWriter writer, byte[][] parameters)
			throws Throwable {

		String key;
		
		try{
			key = ArraysUtil.toString(parameters[1]);
			
			if(key == null){
		        throw new NullPointerException();
			}
	    }
	    catch(Throwable e){
	        throw new ServerErrorException(ServerErrors.ERROR_1003, "var_name");
	    }
		
		Object obj = terminal.getTerminalVars().get(key);
        writer.sendMessage(key + ": " + (obj == null? "empty" : String.valueOf(obj)) );
        writer.flush();
		
	}

}
