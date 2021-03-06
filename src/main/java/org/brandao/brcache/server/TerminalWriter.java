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

import java.io.OutputStream;

/**
 *
 * @author Brandao
 */
public interface TerminalWriter {
    
    void sendMessage(String message) throws WriteDataException;

    void sendMessage(byte[] message) throws WriteDataException;

    void write(byte[] b, int off, int len) throws WriteDataException;

    void directWrite(byte[] b, int off, int len) throws WriteDataException;
    
    void write(byte[] b) throws WriteDataException;
    
    void sendCRLF() throws WriteDataException;
    
    void flush() throws WriteDataException;
    
    OutputStream getStream();
    
}
