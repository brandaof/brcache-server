package org.brandao.brcache.server.command;

import org.brandao.brcache.BasicCache;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalConstants;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;

/**
 * Representa o comando <code>exit</code>.
 * Sua sintaxe é:
 * <pre>
 * exit
 * </pre> 
 * @author Brandao
 *
 */
public class ExitCommand 
	extends AbstractCommand{

	public void executeCommand(Terminal terminal, BasicCache cache, TerminalReader reader,
			TerminalWriter writer, byte[][] parameters)
			throws Throwable {
		 
        writer.sendMessage(TerminalConstants.DISCONNECT_DTA);
        writer.flush();
        terminal.destroy();
	}

}
