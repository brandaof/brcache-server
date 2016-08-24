package org.brandao.brcache.server.command;

import org.brandao.brcache.Cache;
import org.brandao.brcache.TXCache;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;
import org.brandao.brcache.tx.CacheTransaction;

public class RollbackTransactionCommand 
	extends AbstractCommand{

	public void executeCommand(Terminal terminal, Cache cache, TerminalReader reader,
			TerminalWriter writer, String[] parameters)
			throws Throwable {

		if(!(cache instanceof TXCache)){
			throw new ServerErrorException(ServerErrors.ERROR_1009);
		}
		
		TXCache txCahe = (TXCache)cache;
		CacheTransaction tx = txCahe.getTransactionManager().getCurrrent();
		tx.rollback();
		
	}

}