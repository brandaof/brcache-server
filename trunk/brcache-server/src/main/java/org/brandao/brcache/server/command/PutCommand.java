package org.brandao.brcache.server.command;

import java.io.InputStream;

import org.brandao.brcache.BasicCache;
import org.brandao.brcache.server.ArraysUtil;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalConstants;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;

/**
 * Representa o comando <code>put</code>.
 * Sua sintaxe é:
 * <pre>
 * put &lt;key&gt; &lt;timeToLive&gt; &lt;timeToIdle&gt; &lt;size&gt; &lt;reserved&gt;\r\n
 * &lt;data&gt;\r\n
 * end\r\n 
 * </pre> 
 * @author Brandao
 *
 */
public class PutCommand extends AbstractCommand{

	private byte[] b = new byte[9024];

	public void executeCommand(Terminal terminal, BasicCache cache, TerminalReader reader,
			TerminalWriter writer, byte[][] parameters)
			throws Throwable {
		
        int timeToLive;
        int timeToIdle;
        int size;
		String key;

		try{
			key = ArraysUtil.toString(parameters[1]);
			
			if(key == null){
		        throw new NullPointerException();
			}
	    }
	    catch(Throwable e){
	        throw new ServerErrorException(ServerErrors.ERROR_1003, "key");
	    }
		
        try{
        	timeToLive = Integer.parseInt(ArraysUtil.toString(parameters[2]));
        	if(timeToLive < 0){
        		throw new IllegalStateException();
        	}
        }
        catch(Throwable e){
            throw new ServerErrorException(ServerErrors.ERROR_1003, "timeToLive");
        }

        try{
        	timeToIdle = Integer.parseInt(ArraysUtil.toString(parameters[3]));
        	if(timeToIdle < 0){
        		throw new IllegalStateException();
        	}
        }
        catch(Throwable e){
            throw new ServerErrorException(ServerErrors.ERROR_1003, "timeToIdle");
        }

        try{
            size = Integer.parseInt(ArraysUtil.toString(parameters[4]));
        	if(size <= 0){
        		throw new IllegalStateException();
        	}
        }
        catch(Throwable e){
            throw new ServerErrorException(ServerErrors.ERROR_1003, "size");
        }
        
        InputStream stream = reader.getStream(size);
        boolean result     = false;
        Throwable error    = null;
        
        try{
        	int l = 0;
        	while((l = stream.read(b, 0, b.length)) != -1);
        	
        	/*
            result = cache.putStream(
                key, 
                stream,
                timeToLive,
                timeToIdle);
                */
        }
        catch(Throwable e){
        	//capturado erro no processamento do fluxo de bytes do item
        	error = e;
        }
        finally{
            if(stream != null){
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
            }
            
            //Lança o erro encontrado no processamento da stream.
            if(error != null)
    			throw new ServerErrorException(error, ServerErrors.ERROR_1004);
        }
        

    	writer.sendMessage(result? TerminalConstants.REPLACE_SUCCESS : TerminalConstants.STORED);
        writer.flush();
        
	}

}
