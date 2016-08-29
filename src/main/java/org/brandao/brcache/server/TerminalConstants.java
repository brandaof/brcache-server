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

/**
 *
 * @author Brandao
 */
public class TerminalConstants {

	public static final String SEPARATOR_COMMAND = " ";
	
	public static final String[] EMPTY_STR_ARRAY = new String[]{};
	
    public static final String CRLFText = "\r\n";
    
    public static final byte[] CRLF = "\r\n".getBytes();
    
    public static final String BOUNDARY_MESSAGE = "end";

    public static final byte[] BOUNDARY = BOUNDARY_MESSAGE.getBytes();
    
    public static final String UNKNOW_COMMAND = "unknow commmand: %s";
    
    public static final String INVALID_NUMBER_OF_PARAMETERS = "invalid number of parameters";
    
    public static final String INVALID_TIME = "invalid exptime";

    public static final String INVALID_SIZE = "invalid size";
    
    public static final String SUCCESS = "ok";

    public static final String PUT_SUCCESS = "stored";

    public static final String REPLACE_SUCCESS = "replaced";
    
    public static final String NOT_FOUND = "not_found";
    
    public static final String INSERT_ENTRY_FAIL = "insert entry fail";
    
    public static final String READ_ENTRY_FAIL = "read entry fail";
    
    public static final String SEND_MESSAGE_FAIL  = "send message fail";
    
    public static final String STREAM_CLOSED = "stream closed";
    
    public static final String CANT_READ_COMMAND = "cant read command: %s";
    
    public static final String EMPTY_PARAMETER = "empty parameter: %s";

    public static final String CANT_READ_PARAMETER = "cant read parameter: %s";
    
    public static final String DISCONNECT = "goodbye!";
    
}
