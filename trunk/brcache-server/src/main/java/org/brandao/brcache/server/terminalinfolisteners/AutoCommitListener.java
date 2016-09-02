package org.brandao.brcache.server.terminalinfolisteners;

import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalInfo;
import org.brandao.brcache.server.TerminalInfo.TerminalInfoListener;

public class AutoCommitListener implements TerminalInfoListener{

	private Terminal terminal;
	
	private String[] emptyParams = new String[0];
	
	public AutoCommitListener(Terminal terminal) {
		this.terminal = terminal;
	}

	public void actionPerformed(String key, Object oldValue, Object newValue,
			TerminalInfo terminalInfo) {

		boolean value = (Boolean)newValue;
		
		try{
			if(value){
				Terminal.COMMIT_TX.execute(terminal, terminal.getCache(), terminal.getReader(), terminal.getWriter(), emptyParams);
			}
			else{
				Terminal.BEGIN_TX.execute(terminal, terminal.getCache(), terminal.getReader(), terminal.getWriter(), emptyParams);
			}
		}
		catch(RuntimeException e){
			terminalInfo.put(key, oldValue);
		}
	}

}
