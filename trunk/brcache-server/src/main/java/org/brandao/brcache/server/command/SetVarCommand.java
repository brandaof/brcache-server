package org.brandao.brcache.server.command;

import java.util.HashMap;
import java.util.Map;

import org.brandao.brcache.BasicCache;
import org.brandao.brcache.server.ServerConstants;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalConstants;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;
import org.brandao.brcache.server.util.ClassUtil;

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

	@SuppressWarnings("serial")
	private Map<String, Class<?>> types = new HashMap<String, Class<?>>(){{
		put(ServerConstants.AUTO_COMMIT, Boolean.class);
	}};
	
	public void executeCommand(Terminal terminal, BasicCache cache, TerminalReader reader,
			TerminalWriter writer, String[] parameters)
			throws Throwable {

		String key;
		Object value;
		
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

			Class<?> type = this.types.get(key);
			if(type != null){
				value = ClassUtil.toObject(type, (String)value);
			}
			
	    }
	    catch(Throwable e){
	        throw new ServerErrorException(ServerErrors.ERROR_1003, "new_value");
	    }
		
		
		terminal.getTerminalVars().put(key, value);
        writer.sendMessage(TerminalConstants.SUCCESS);
        writer.flush();
		
	}

}
