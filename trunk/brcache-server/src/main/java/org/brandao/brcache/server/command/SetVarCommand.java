package org.brandao.brcache.server.command;

import org.brandao.brcache.BasicCache;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;

/**
 * Representa o comando <code>begin</code>.
 * Sua sintaxe é:
 * <pre>
 * begin
 * </pre> 
 * @author Brandao
 *
 */
public class SetVarCommand 
	extends AbstractCommand{

	public void executeCommand(Terminal terminal, BasicCache cache, TerminalReader reader,
			TerminalWriter writer, String[] parameters)
			throws Throwable {

		String key;
		String value;
		
		try{
			key = parameters[1];
			
			if(key == null){
		        throw new NullPointerException();
			}
	    }
	    catch(Throwable e){
	        throw new ServerErrorException(ServerErrors.ERROR_1003, "var_name");
	    }

		
		try{
			value = parameters[2];
			
			if(value == null){
		        throw new NullPointerException();
			}
	    }
	    catch(Throwable e){
	        throw new ServerErrorException(ServerErrors.ERROR_1003, "new_value");
	    }
		
		Object obj = terminal.getTerminalInfo().get(key);
        writer.sendMessage(key + "=" + (obj == null? "empty" : String.valueOf(obj)) );
        writer.flush();
		
	}

}
