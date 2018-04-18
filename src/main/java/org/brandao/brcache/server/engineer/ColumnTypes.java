package org.brandao.brcache.server.engineer;

import org.brandao.brcache.CacheException;

public enum ColumnTypes implements ColumnType{

	  CHAR{},
	  VARCHAR,
	  BYTE,
	  SHORT,
	  INTEGER,
	  LONG,
	  FLOAT,
	  DOUBLE,
	  NUMERIC,
	  DATE,
	  DATE_TIME,
	  TIME,
	  BIN;

	public String getString(Object o) throws CacheException {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] getBytes(Object o, int length, int decimal, int scale)
			throws CacheException {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] getStringUTF_8(Object o) throws CacheException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object toObject(String o) throws CacheException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(byte[] o, int length, int decimal, int scale)
			throws CacheException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getValueStringUTF8(byte[] o) throws CacheException {
		// TODO Auto-generated method stub
		return null;
	}

}
