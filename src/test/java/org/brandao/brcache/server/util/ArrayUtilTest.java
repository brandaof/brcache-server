package org.brandao.brcache.server.util;

import java.util.Arrays;

import junit.framework.TestCase;

public class ArrayUtilTest extends TestCase{

	
	public void testAlgoritmEquals1(){
		byte[] a   = new byte[1024];
		byte[] b   = new byte[1024];
		long total = 0;
		int ops    = 1000000;
		
		for(int i=0;i<ops;i++){
			long nanoStart = System.nanoTime();
			equals1(a,b);
			long nanoEnd = System.nanoTime();
			total += nanoEnd - nanoStart;
		}
		
		double timeOp = total / ops;
		double opsSec = 1000000000 / timeOp;
		
		System.out.println("equals1 operations: " + ops + ", time: " + total + " nano, ops/Sec: " + + opsSec );
	}

	public void testAlgoritmEquals2(){
		byte[] a   = new byte[1024];
		byte[] b   = new byte[1024];
		long total = 0;
		int ops    = 1000000;
		
		for(int i=0;i<ops;i++){
			long nanoStart = System.nanoTime();
			equals2(a,b);
			long nanoEnd = System.nanoTime();
			total += nanoEnd - nanoStart;
		}
		
		double timeOp = total / ops;
		double opsSec = 1000000000 / timeOp;
		
		System.out.println("equals2 operations: " + ops + ", time: " + total + " nano, ops/Sec: " + + opsSec );
	}
	
	public void testAlgoritmEquals3(){
		byte[] a   = new byte[1024];
		byte[] b   = new byte[1024];
		long total = 0;
		int ops    = 1000000;
		
		for(int i=0;i<ops;i++){
			long nanoStart = System.nanoTime();
			Arrays.equals(a,b);
			long nanoEnd = System.nanoTime();
			total += nanoEnd - nanoStart;
		}
		
		double timeOp = total / ops;
		double opsSec = 1000000000 / timeOp;
		
		System.out.println("equals3 operations: " + ops + ", time: " + total + " nano, ops/Sec: " + + opsSec );
	}
	
	private boolean equals1(byte[] a, byte[] b){
		int alen = a.length;
		
		if(alen != b.length){
			return false;
		}
		
		for(int i=0;i<alen;i++)
			if( a[i] != b[i])
				return false;
		
		return true;
	}

	private boolean equals2(byte[] a, byte[] b){
		int alen = a.length;
		
		if(alen != b.length){
			return false;
		}
		
		int r = 0;
		for(int i=0;i<alen;i++){
			r = a[i] - b[i];
		}
		
		return r == 0;
	}

}
