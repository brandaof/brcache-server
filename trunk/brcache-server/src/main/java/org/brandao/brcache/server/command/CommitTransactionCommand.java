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

public class CommitTransactionCommand 
	extends AbstractCommand{

	public void executeCommand(Terminal terminal, Cache cache, TerminalReader reader,
			TerminalWriter writer, String[] parameters)
			throws Throwable {

		if(!(cache instanceof TXCache)){
			throw new ServerErrorException(ServerErrors.ERROR_1009);
		}
		
		TXCache txCahe = (TXCache)cache;
		CacheTransaction tx = txCahe.getTransactionManager().getCurrrent();
		tx.commit();
		
        writer.sendMessage(TerminalConstants.SUCCESS);
        writer.flush();
		
	}

}
