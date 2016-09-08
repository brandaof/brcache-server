package org.brandao.brcache.server.command;

import org.brandao.brcache.BasicCache;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalConstants;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;
import org.brandao.brcache.tx.CacheTransaction;
import org.brandao.brcache.tx.TXCache;

/**
 * Representa o comando <code>commit</code>.
 * Sua sintaxe é:
 * <pre>
 * commit
 * </pre> 
 * @author Brandao
 *
 */
public class CommitTransactionCommand 
	extends AbstractCommand{

	public void executeCommand(Terminal terminal, BasicCache cache, TerminalReader reader,
			TerminalWriter writer, byte[][] parameters)
			throws Throwable {

		if(!(cache instanceof TXCache)){
			throw new ServerErrorException(ServerErrors.ERROR_1009);
		}
		
		TXCache txCahe = (TXCache)cache;
		CacheTransaction tx = txCahe.getTransactionManager().getCurrrent();
		tx.commit();
		
        writer.sendMessage(TerminalConstants.SUCCESS_DTA);
        writer.flush();
		
	}

}
