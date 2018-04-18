package org.brandao.brcache.server.engineer;

import org.brandao.brcache.CacheException;

public interface ColumnType {

    String getString(Object o) throws CacheException;
    
    byte[] getBytes(Object o, int length, int decimal, int scale) throws CacheException;

    byte[] getStringUTF_8(Object o) throws CacheException;
    
    Object toObject(String o) throws CacheException;
    
    Object getValue(byte[] o, int length, int decimal, int scale) throws CacheException;

    String getValueStringUTF8(byte[] o) throws CacheException;
    
}
