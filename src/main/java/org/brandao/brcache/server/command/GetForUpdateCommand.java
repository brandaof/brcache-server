package org.brandao.brcache.server.command;

import java.io.OutputStream;

import org.brandao.brcache.Cache;
import org.brandao.brcache.CacheInputStream;
import org.brandao.brcache.TXCache;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalConstants;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;

/**
 * Representa o comando GET.
 * Sua sintaxe é:
 * GET_FOR_UPDATE <nome> <reserved> lock\r\n
 * 
 * @author Brandao
 *
 */
public class GetForUpdateCommand extends AbstractCommand{

	public void executeCommand(Terminal terminal, Cache cache, TerminalReader reader,
			TerminalWriter writer, String[] parameters)
			throws Throwable {

		
		if(!(cache instanceof TXCache)){
			throw new ServerErrorException(ServerErrors.ERROR_1009);
		}
		
		String name;

        try{
            name = parameters[1];
        }
        catch(Throwable e){
            throw new ServerErrorException(ServerErrors.ERROR_1003, "name");
        }
		
		TXCache txCahe = (TXCache)cache;
		
        CacheInputStream in = null;
        try{
            in = (CacheInputStream) txCahe.get(name, true);
            if(in != null){
                String responseMessage = 
            		"VALUE " +
    				name +
            		TerminalConstants.SEPARATOR_COMMAND +
            		in.getSize() +
            		" 0";
                writer.sendMessage(responseMessage);
                OutputStream out = null;
                try{
                    out = writer.getStream();
                    in.writeTo(out);
                }
                finally{
                    if(out != null){
                        try{
                            out.close();
                        }
                        catch(Throwable e){
                        }
                    }
                    writer.sendCRLF();
                }
            }
            else{
                String responseMessage =
            		"VALUE " +
    				name +
    				" 0 0";
                writer.sendMessage(responseMessage);
            }
        }
        finally{
            if(in != null)
                in.close();
        }

        writer.sendMessage(TerminalConstants.BOUNDARY_MESSAGE);
        writer.flush();
	}

}
