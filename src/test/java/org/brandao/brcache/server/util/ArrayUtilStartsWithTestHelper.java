package org.brandao.brcache.server.util;

public class ArrayUtilStartsWithTestHelper {

	public static boolean startsWith1(byte[] array, byte[] value){
		
		if(array.length < value.length)
			return false;
		
		for(int i=0;i<value.length;i++){
			
			if(array[i] != value[i]){
				return false;
			}
				
		}
		
		return true;
	}

	public static boolean startsWith2(byte[] array, byte[] value){
		try{
			int len = value.length;
			int r   = 0;
			for(int i=0;i<len;i++){
				r += array[i] - value[i];
			}
			return r == 0;
		}
		catch(Throwable e){
			return false;
		}
	}
	
}
