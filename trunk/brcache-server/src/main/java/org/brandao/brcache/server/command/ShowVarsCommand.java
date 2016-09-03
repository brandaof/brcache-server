package org.brandao.brcache.server.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.brandao.brcache.BasicCache;
import org.brandao.brcache.server.Terminal;
import org.brandao.brcache.server.TerminalConstants;
import org.brandao.brcache.server.TerminalSession;
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

	public void executeCommand(Terminal terminal, BasicCache cache, TerminalReader reader,
			TerminalWriter writer, String[] parameters)
			throws Throwable {
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		TerminalSession config = terminal.getConfiguration();

        for(String prop: config.keySet()){
        	map.put(prop,config.get(prop));
        }

        map.put("read_entry",   cache.getCountRead());
        map.put("read_data",    cache.getCountReadData());
        map.put("write_entry",  cache.getCountWrite());
        map.put("removed_data", cache.getCountRemoved());
        
		StringBuilder result = new StringBuilder();
        
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);
        for(String prop: keys){
            result
            	.append(prop).append(": ")
            	.append(map.get(prop)).append(TerminalConstants.CRLFText);
        }
        
        result.append(TerminalConstants.BOUNDARY_MESSAGE);
        writer.sendMessage(result.toString());
        writer.flush();
		
	}

}
