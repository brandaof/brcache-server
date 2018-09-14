package org.brandao.brcache.server.command;

import org.brandao.brcache.Cache;
import org.brandao.brcache.CacheException;
import org.brandao.brcache.server.Command;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;

public abstract class AbstractCommand 
	implements Command{

	public void execute(Terminal terminal, Cache cache, TerminalReader reader,
			TerminalWriter writer, byte[][] parameters)
			throws ServerErrorException {

		try{
			this.executeCommand(terminal, cache, reader, writer, parameters);
		}
        catch(ServerErrorException ex){
            throw ex;
        }
        catch(CacheException ex){
            throw new ServerErrorException(ex, ex.getError(), ex.getParams());
        }
        catch (Throwable ex) {
            throw new ServerErrorException(ex, ServerErrors.ERROR_1005, ServerErrors.ERROR_1005.getString());
        }
		
	}
	
	protected abstract void executeCommand(Terminal terminal, Cache cache, TerminalReader reader,
			TerminalWriter writer, byte[][] parameters)
			throws Throwable;
}
