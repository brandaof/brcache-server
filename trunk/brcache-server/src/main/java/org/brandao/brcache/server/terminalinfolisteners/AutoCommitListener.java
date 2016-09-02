package org.brandao.brcache.server.terminalinfolisteners;

import org.brandao.brcache.BasicCache;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalInfo;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;
import org.brandao.brcache.server.TerminalInfo.TerminalInfoListener;
import org.brandao.brcache.server.command.BeginTransactionCommand;
import org.brandao.brcache.server.command.CommitTransactionCommand;
import org.brandao.brcache.server.command.RollbackTransactionCommand;

public class AutoCommitListener implements TerminalInfoListener{

	private Terminal terminal;
	
	private BasicCache cache;
	
	private TerminalReader reader;
	
	private TerminalWriter writer;
	
	private BeginTransactionCommand begin; 
	
	private CommitTransactionCommand commit;
	
	private RollbackTransactionCommand rollback;

	private String[] emptyParams = new String[0];
	
	public AutoCommitListener(Terminal terminal, BasicCache cache,
			TerminalReader reader, TerminalWriter writer,
			BeginTransactionCommand begin, CommitTransactionCommand commit,
			RollbackTransactionCommand rollback) {
		this.terminal = terminal;
		this.cache = cache;
		this.reader = reader;
		this.writer = writer;
		this.begin = begin;
		this.commit = commit;
		this.rollback = rollback;
	}

	public void actionPerformed(String key, Object oldValue, Object newValue,
			TerminalInfo terminalInfo) {

		boolean value = (Boolean)newValue;
		
		try{
			if(value){
				this.commit.execute(terminal, cache, reader, writer, emptyParams);
			}
			else{
				this.begin.execute(terminal, cache, reader, writer, emptyParams);
			}
		}
		catch(RuntimeException e){
			terminalInfo.put(key, oldValue);
		}
	}

}
