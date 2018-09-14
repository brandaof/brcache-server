package org.brandao.brcache.server.command;

import org.brandao.brcache.Cache;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalConstants;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;
import org.brandao.brcache.tx.CacheTransaction;
import org.brandao.brcache.tx.TXCache;

/**
 * Representa o comando <code>rollback</code>.
 * Sua sintaxe é:
 * <pre>
 * rollback
 * </pre> 
 * @author Brandao
 *
 */
public class RollbackTransactionCommand 
	extends AbstractCommand{

	public void executeCommand(Terminal terminal, Cache cache, TerminalReader reader,
			TerminalWriter writer, byte[][] parameters)
			throws Throwable {

		if(!(cache instanceof TXCache)){
			throw new ServerErrorException(ServerErrors.ERROR_1009);
		}
		
		TXCache txCahe = (TXCache)cache;
		CacheTransaction tx = txCahe.getTransactionManager().getCurrrent();
		tx.rollback();
		
        writer.sendMessage(TerminalConstants.SUCCESS_DTA);
        writer.flush();
		
	}

}
