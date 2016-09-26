package org.brandao.brcache.server.command;

import java.io.InputStream;

import org.brandao.brcache.BasicCache;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalConstants;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;
import org.brandao.brcache.server.util.ArraysUtil;

/**
 * Representa o comando <code>put</code>.
 * Sua sintaxe é:
 * <pre>
 * put &lt;key&gt; &lt;timeToLive&gt; &lt;timeToIdle&gt; &lt;size&gt; &lt;reserved&gt;\r\n
 * &lt;data&gt;\r\n
 * </pre> 
 * @author Brandao
 *
 */
public class PutCommand extends AbstractCommand{

	private static final byte[] REPLACE_SUCCESS_DTA = TerminalConstants.REPLACE_SUCCESS_DTA;

	private static final byte[] STORED_DTA = TerminalConstants.STORED_DTA;
	
	public void executeCommand(Terminal terminal, BasicCache cache, TerminalReader reader,
			TerminalWriter writer, byte[][] parameters)
			throws Throwable {
		
        int timeToLive;
        int timeToIdle;
        int size;
		String key;

		try{
			key        = ArraysUtil.toString(parameters[1]);
        	timeToLive = ArraysUtil.toInt(parameters[2]);
        	timeToIdle = ArraysUtil.toInt(parameters[3]);
            size       = ArraysUtil.toInt(parameters[4]);
			
			if(key == null){
		        throw new NullPointerException();
			}
			
        	if(timeToLive < 0){
        		throw new IllegalStateException();
        	}
        	
        	if(timeToIdle < 0){
        		throw new IllegalStateException();
        	}
        	
        	if(size <= 0){
        		throw new IllegalStateException();
        	}
        	
	    }
	    catch(Throwable e){
	        throw new ServerErrorException(ServerErrors.ERROR_1004);
	    }

        InputStream stream = reader.getStream(size);
        boolean result     = false;
        Throwable error    = null;
        
        try{
            result = cache.putStream(
                key, 
                stream,
                timeToLive,
                timeToIdle);
        }
        catch(Throwable e){
        	//capturado erro no processamento do fluxo de bytes do item
        	error = e;
        }
        finally{
        	try{
            	//tenta fechar o fluxo
        		stream.close();
        	}
        	catch(Throwable e){
        		//se error for null, a falha a ser considerada é do 
        		//fechamento do fluxo.
        		if(error == null){
        			throw new ServerErrorException(e, ServerErrors.ERROR_1004);
        		}
        	}
            
            //Lança o erro encontrado no processamento da stream.
            if(error != null)
    			throw new ServerErrorException(error, ServerErrors.ERROR_1004);
        }
        
    	writer.sendMessage(result? REPLACE_SUCCESS_DTA : STORED_DTA);
        writer.flush();
	}

}
