package org.brandao.brcache.server.util;

import java.lang.reflect.InvocationTargetException;

public class ClassUtil {

    public static Class<?> getClasse( String name ) throws ClassNotFoundException{
        return Class.forName( name, true, Thread.currentThread().getContextClassLoader() );
    }
	
	public static Object toObject(Class<?> type, String value) 
			throws IllegalAccessException, IllegalArgumentException, 
			InvocationTargetException, NoSuchMethodException, SecurityException{
		return type.getMethod("valueOf", String.class).invoke(type, value);
	}
}
