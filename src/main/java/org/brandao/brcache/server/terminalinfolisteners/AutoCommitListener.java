package org.brandao.brcache.server.terminalinfolisteners;

import org.brandao.brcache.Cache;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalVars;
import org.brandao.brcache.server.TerminalVars.TerminalInfoListener;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;
import org.brandao.brcache.tx.CacheTransaction;
import org.brandao.brcache.tx.TXCache;

public class AutoCommitListener implements TerminalInfoListener{

	private Terminal terminal;
	
	//private String[] emptyParams = new String[0];
	
	public AutoCommitListener(Terminal terminal) {
		this.terminal = terminal;
	}

	public void actionPerformed(String key, Object oldValue, Object newValue,
			TerminalVars terminalSession) {

		boolean value = (Boolean)newValue;
		
		try{
			Cache cache = terminal.getCache();
			if(value){
				if(!(cache instanceof TXCache)){
					throw new ServerErrorException(ServerErrors.ERROR_1009);
				}
				
				TXCache txCahe = (TXCache)cache;
				CacheTransaction tx = txCahe.getTransactionManager().getCurrrent();
				tx.commit();
			}
			else{
				
				if(!(cache instanceof TXCache)){
					throw new ServerErrorException(ServerErrors.ERROR_1009);
				}
				
				TXCache txCahe = (TXCache)cache;
				
				txCahe.beginTransaction();
			}
		}
		catch(RuntimeException e){
			terminalSession.set(key, oldValue);
			throw e;
		}
	}

}
