package org.brandao.brcache.server.command;

import java.io.InputStream;

import org.brandao.brcache.BasicCache;
import org.brandao.brcache.CacheErrors;
import org.brandao.brcache.CacheException;
import org.brandao.brcache.CacheInputStream;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalConstants;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;
import org.brandao.brcache.server.error.ServerErrorException;
import org.brandao.brcache.server.error.ServerErrors;

/**
 * Representa o comando <code>replace</code>.
 * Sua sintaxe é:
 * <pre>
 * set &lt;key&gt; &lt;timeToLive&gt; &lt;timeToIdle&gt; &lt;size&gt; &lt;reserved&gt;\r\n
 * &lt;data&gt;\r\n
 * end\r\n 
 * </pre> 
 * @author Brandao
 *
 */
public class SetCommand extends AbstractCommand{

	public void executeCommand(Terminal terminal, BasicCache cache, TerminalReader reader,
			TerminalWriter writer, String[] parameters)
			throws Throwable {
		
        int timeToLive;
        int timeToIdle;
        int size;
		String key;

		try{
			key = parameters[1];
			
			if(key == null){
		        throw new NullPointerException();
			}
	    }
	    catch(Throwable e){
	        throw new ServerErrorException(ServerErrors.ERROR_1003, "key");
	    }
		
        try{
        	timeToLive = Integer.parseInt(parameters[2]);
        	if(timeToLive < 0){
        		throw new IllegalStateException();
        	}
        }
        catch(Throwable e){
            throw new ServerErrorException(ServerErrors.ERROR_1003, "timeToLive");
        }

        try{
        	timeToIdle = Integer.parseInt(parameters[3]);
        	if(timeToIdle < 0){
        		throw new IllegalStateException();
        	}
        }
        catch(Throwable e){
            throw new ServerErrorException(ServerErrors.ERROR_1003, "timeToIdle");
        }

        try{
            size = Integer.parseInt(parameters[4]);
        	if(size <= 0){
        		throw new IllegalStateException();
        	}
        }
        catch(Throwable e){
            throw new ServerErrorException(ServerErrors.ERROR_1003, "size");
        }
        
        InputStream stream = reader.getStream(size);
        CacheInputStream result = null;
        Throwable error    = null;
        
        try{
            result = (CacheInputStream)cache.putIfAbsentStream(
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
            if(error != null){
            	//Se for lançada a exceção CacheException com o erro ERROR_1030, significa que
            	//já existe um item e o mesmo expirou na execução do método.
            	if(!(error instanceof CacheException) || ((CacheException)error).getError() != CacheErrors.ERROR_1030){
            	//	throw new ServerErrorException(error, ServerErrors.ERROR_1030);
            	//}
            	//else{
            		throw new ServerErrorException(error, ServerErrors.ERROR_1004);
            	}
            }
        }
        

        writer.sendMessage(result != null || error != null? TerminalConstants.NOT_STORED : TerminalConstants.STORED);
        writer.flush();        
	}

}
