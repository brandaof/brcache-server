package org.brandao.brcache.server.command;

import org.brandao.brcache.Cache;
import org.brandao.brcache.server.ServerConstants;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalConstants;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;
import org.brandao.brcache.tx.TXCache;

/**
 * Representa o comando <code>begin</code>.
 * Sua sintaxe é:
 * <pre>
 * begin
 * </pre> 
 * @author Brandao
 *
 */
public class BeginTransactionCommand 
	extends AbstractCommand{

	public void executeCommand(Terminal terminal, Cache cache, TerminalReader reader,
			TerminalWriter writer, byte[][] parameters)
			throws Throwable {

		if(!(cache instanceof TXCache)){
			throw new ServerErrorException(ServerErrors.ERROR_1009);
		}
		
		TXCache txCahe = (TXCache)cache;
		
		txCahe.beginTransaction();
		
		terminal.getTerminalVars().set(ServerConstants.AUTO_COMMIT, false);
		
        writer.sendMessage(TerminalConstants.SUCCESS_DTA);
        writer.flush();
		
	}

}
