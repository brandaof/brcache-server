package org.brandao.brcache.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.brandao.brcache.Configuration;

public class Bootstrap {

	private static BrCacheServer server;
	
	public static void start(String[] args) throws FileNotFoundException, IOException{
    	StartParamsParser paramsParser = new StartParamsParser(args);
    	
        File f = new File(paramsParser.getConfigFile());
        
        if(!f.exists() || !f.canRead()){
            System.out.println("configuration not found!");
            System.exit(2);
        }
            
        Configuration config = new Configuration();
        config.load(new FileInputStream(f));
     
        try{
            server = new BrCacheServer(config);
            server.start();
        }
        catch(Throwable e){
            e.printStackTrace();
            System.exit(2);
        }
    	
	}
	
	public static void stop(String[] args) throws IOException{
		server.stop();
        System.exit(0);
	}
	
}
