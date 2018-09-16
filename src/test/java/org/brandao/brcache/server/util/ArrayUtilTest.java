package org.brandao.brcache.server.util;

import static org.brandao.brcache.server.util.ArrayUtilCopyTestHelper.copy1;
import static org.brandao.brcache.server.util.ArrayUtilCopyTestHelper.copy2;
import static org.brandao.brcache.server.util.ArrayUtilEqualsTestHelper.equals1;
import static org.brandao.brcache.server.util.ArrayUtilEqualsTestHelper.equals2;
import static org.brandao.brcache.server.util.ArrayUtilEqualsTestHelper.equals3;
import static org.brandao.brcache.server.util.ArrayUtilStartsWithTestHelper.startsWith1;
import static org.brandao.brcache.server.util.ArrayUtilStartsWithTestHelper.startsWith2;

import java.text.DecimalFormat;

import junit.framework.TestCase;

public class ArrayUtilTest extends TestCase{
	
	private static DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###.000");
	
	public void testEquals1(){
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
		
		System.out.println("equals1 operations: " + ops + ", time: " + total + " nano, ops/Sec: " + df.format(opsSec) );
	}

	public void testEquals2(){
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
		
		System.out.println("equals2 operations: " + ops + ", time: " + total + " nano, ops/Sec: " + df.format(opsSec) );
	}
	
	public void testEquals3(){
		byte[] a   = new byte[1024];
		byte[] b   = new byte[1024];
		long total = 0;
		int ops    = 1000000;
		
		for(int i=0;i<ops;i++){
			long nanoStart = System.nanoTime();
			equals3(a,b);
			long nanoEnd = System.nanoTime();
			total += nanoEnd - nanoStart;
		}
		
		double timeOp = total / ops;
		double opsSec = 1000000000 / timeOp;
		
		System.out.println("equals3 operations: " + ops + ", time: " + total + " nano, ops/Sec: " + df.format(opsSec) );
	}

	public void testArrayCopy1(){
		byte[] a   = new byte[1024];
		byte[] b   = new byte[1024];
		long total = 0;
		int ops    = 1000000;
		
		for(int i=0;i<ops;i++){
			long nanoStart = System.nanoTime();
			copy1(a, 0, b, 0, a.length);
			long nanoEnd = System.nanoTime();
			total += nanoEnd - nanoStart;
		}
		
		double timeOp = total / ops;
		double opsSec = 1000000000 / timeOp;
		
		System.out.println("copy1 operations: " + ops + ", time: " + total + " nano, ops/Sec: " + df.format(opsSec) );
	}

	public void testArrayCopy2(){
		byte[] a   = new byte[1024];
		byte[] b   = new byte[1024];
		long total = 0;
		int ops    = 1000000;
		
		for(int i=0;i<ops;i++){
			long nanoStart = System.nanoTime();
			copy2(a, 0, b, 0, a.length);
			long nanoEnd = System.nanoTime();
			total += nanoEnd - nanoStart;
		}
		
		double timeOp = total / ops;
		double opsSec = 1000000000 / timeOp;
		
		System.out.println("copy2 operations: " + ops + ", time: " + total + " nano, ops/Sec: " + df.format(opsSec) );
	}

	public void testStartsWith1(){
		byte[] a   = new byte[1024];
		byte[] b   = new byte[1023];
		long total = 0;
		int ops    = 1000000;
		
		for(int i=0;i<ops;i++){
			long nanoStart = System.nanoTime();
			startsWith1(a, b);
			long nanoEnd = System.nanoTime();
			total += nanoEnd - nanoStart;
		}
		
		double timeOp = total / ops;
		double opsSec = 1000000000 / timeOp;
		
		System.out.println("StartsWith1 operations: " + ops + ", time: " + total + " nano, ops/Sec: " + df.format(opsSec) );
	}

	public void testStartsWith2(){
		byte[] a   = new byte[1024];
		byte[] b   = new byte[1023];
		long total = 0;
		int ops    = 1000000;
		
		for(int i=0;i<ops;i++){
			long nanoStart = System.nanoTime();
			startsWith2(a, b);
			long nanoEnd = System.nanoTime();
			total += nanoEnd - nanoStart;
		}
		
		double timeOp = total / ops;
		double opsSec = 1000000000 / timeOp;
		
		System.out.println("StartsWith2 operations: " + ops + ", time: " + total + " nano, ops/Sec: " + df.format(opsSec) );
	}
	
}
