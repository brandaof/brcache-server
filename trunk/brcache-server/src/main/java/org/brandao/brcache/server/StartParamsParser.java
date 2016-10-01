package org.brandao.brcache.server;

public class StartParamsParser {

	private String configFile;
	
	private String[] params;
	
	public StartParamsParser(String[] params){
		this.params             = params;
		this.configFile         = "./brcache.conf";
		this.parser();
	}
	
	private void parser(){

		for(String param: params){
			if(param.startsWith("--default-file")){
				String[] parts = param.split("\\=");
				if(parts.length != 2 || parts[1].trim().isEmpty()){
					throw new IllegalStateException("expected --default-file=<path>");
				}
				this.configFile = parts[1].trim();
			}
		}
	}

	public String getConfigFile() {
		return configFile;
	}

}
