package org.brandao.brcache.server.util;

import java.util.Arrays;

public class ArrayUtilEqualsTestHelper {

	public static boolean equals1(byte[] a, byte[] b){
		int alen = a.length;
		
		if(alen != b.length){
			return false;
		}
		
		for(int i=0;i<alen;i++)
			if( a[i] != b[i])
				return false;
		
		return true;
	}

	public static boolean equals2(byte[] a, byte[] b){
		int alen = a.length;
		
		if(alen != b.length){
			return false;
		}
		
		int r = 0;
		for(int i=0;i<alen;i++){
			r += a[i] - b[i];
		}
		
		return r == 0;
	}

	public static boolean equals3(byte[] a, byte[] b){
		return Arrays.equals(a, b);
	}
	
}
