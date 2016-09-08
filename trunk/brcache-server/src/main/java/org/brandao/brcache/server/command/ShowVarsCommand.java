package org.brandao.brcache.server.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.brandao.brcache.BasicCache;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalConstants;
import org.brandao.brcache.server.TerminalVars;
import org.brandao.brcache.server.TerminalReader;
import org.brandao.brcache.server.TerminalWriter;

/**
 * Representa o comando <code>show_vars</code>.
 * Sua sintaxe é:
 * <pre>
 * show_vars
 * </pre> 
 * @author Brandao
 *
 */
public class ShowVarsCommand 
	extends AbstractCommand{

    private Runtime runtime;

    public ShowVarsCommand(){
    	this.runtime = Runtime.getRuntime();
    }
    
	public void executeCommand(Terminal terminal, BasicCache cache, TerminalReader reader,
			TerminalWriter writer, byte[][] parameters)
			throws Throwable {
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		TerminalVars vars = terminal.getTerminalVars();

        for(String prop: vars.keySet()){
        	map.put(prop, vars.get(prop));
        }

        map.put("read_entry",   cache.getCountRead());
        map.put("read_data",    cache.getCountReadData());
        map.put("write_entry",  cache.getCountWrite());
        map.put("removed_data", cache.getCountRemoved());
        map.put("total_memory", runtime.totalMemory());
        map.put("free_memory",  runtime.freeMemory());
        map.put("used_memory",  runtime.totalMemory() - runtime.freeMemory());
        
		StringBuilder result = new StringBuilder();
        
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);
        for(String prop: keys){
            result
            	.append(prop).append(": ")
            	.append(map.get(prop)).append(TerminalConstants.CRLF);
        }
        
        result.append(TerminalConstants.BOUNDARY);
        writer.sendMessage(result.toString());
        writer.flush();
		
	}

}
