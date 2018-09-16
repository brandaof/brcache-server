package org.brandao.brcache.server.util;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

@SuppressWarnings("restriction")
public class ArrayUtilCopyTestHelper {

	private static final Unsafe UNSAFE;
	
    private static final long BYTE_ARRAY_OFFSET;
    
    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe) theUnsafe.get(null);
    		BYTE_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(byte[].class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	public static void copy1(byte[] src, int srcPos,
			byte[] dest, int destPos, int length){
		UNSAFE.copyMemory(src, BYTE_ARRAY_OFFSET + srcPos, dest, BYTE_ARRAY_OFFSET + destPos, length);		
	}

	public static void copy2(byte[] src, int srcPos,
			byte[] dest, int destPos, int length){
		System.arraycopy(src, srcPos, dest, destPos, length);		
	}
	
}
