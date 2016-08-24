package org.brandao.brcache.server.error;

import org.brandao.brcache.CacheError;
import org.brandao.brcache.CacheException;

public class ServerErrorException 
	extends CacheException{

	private static final long serialVersionUID = 8545856451576836405L;

	public ServerErrorException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ServerErrorException(CacheError error, Object... params) {
		super(error, params);
		// TODO Auto-generated constructor stub
	}

	public ServerErrorException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ServerErrorException(Throwable thrwbl, CacheError error,
			Object... params) {
		super(thrwbl, error, params);
		// TODO Auto-generated constructor stub
	}


}
