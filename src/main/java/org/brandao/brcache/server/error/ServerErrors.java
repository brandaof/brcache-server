package org.brandao.brcache.server.error;

import org.brandao.brcache.CacheError;
import org.brandao.brcache.CacheErrors;

public class ServerErrors extends CacheErrors {

	public static final CacheError ERROR_1001 = new CacheError(1001, "The command %s not recognized!");
	
	public static final CacheError ERROR_1002 = new CacheError(1002, "Command not informed!");

	public static final CacheError ERROR_1003 = new CacheError(1003, "%s is invalid!");

	public static final CacheError ERROR_1004 = new CacheError(1004, "Bad command syntax error!");

	public static final CacheError ERROR_1005 = new CacheError(1005, "internal error!");

	public static final CacheError ERROR_1006 = new CacheError(1006, "Unknow error!");

	public static final CacheError ERROR_1009 = new CacheError(1009, "Transaction not supported!");
	
}
