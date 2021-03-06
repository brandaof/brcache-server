/*
 * BRCache http://brcache.brandao.org/
 * Copyright (C) 2015 Afonso Brandao. (afonso.rbn@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.brandao.brcache.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.brandao.brcache.Configuration;

/**
 * 
 * @author Brandao
 */
public class Main {
    
    public static void main(String[] params) throws IOException{
    	StartParamsParser paramsParser = new StartParamsParser(params);
    	start(paramsParser.getConfigFile());
    }
    
    private static void start(String configFile) throws FileNotFoundException, IOException{
        File f = new File(configFile);
        
        if(!f.exists() || !f.canRead()){
            System.out.println("configuration not found!");
            System.exit(2);
        }
            
        Configuration config = new Configuration();
        config.load(new FileInputStream(f));
     
        try{
            BrCacheServer server = new BrCacheServer(config);
            server.start();
        }
        catch(Throwable e){
            e.printStackTrace();
            System.exit(2);
        }
    }
    
}
