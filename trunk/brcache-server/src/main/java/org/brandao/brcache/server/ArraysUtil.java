package org.brandao.brcache.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Provê métodos auxiliares de manipulação de arranjo de bytes.
 * @author Brandao
 *
 */
public class ArraysUtil {

	private static final int NEGATIVE_INT = -1;
	
	private static final byte ZERO = '0';

	private static final byte NEGATIVE = '-';

	private static final byte POSITIVE = '+';

	private static final byte TRUE = 1;

	private static final byte FALSE = 0;
	
	/**
	 * Verifica se um arranjo de bytes inicia com os mesmos bytes de outro arranjo de bytes.
	 * @param array arranjo de bytes que será verificado.
	 * @param value arranjo de bytes usado na comparação
	 * @return <code>true</code> se array iniciar com value. Caso contrário <code>false</code>
	 */
	public static boolean startsWith(byte[] array, byte[] value){
		
		if(array.length < value.length)
			return false;
		
		for(int i=0;i<value.length;i++){
			
			if(array[i] != value[i]){
				return false;
			}
				
		}
		
		return true;
	}

	/**
	 * Fragmento um arranjo usando um byte como delimitador.
	 * @param array arranjo
	 * @param value delimitador.
	 * @param index índice inicial.
	 * @return fragmentos.
	 */
	public static byte[][] split(byte[] array, int index, byte value){
		int start = index;
		int end   = 0;
		List<byte[]> result = new ArrayList<byte[]>();
		int limit = array.length -1;
		
		for(int i=index;i<array.length;i++){
			
			if(array[i] == value){
				end = i;
				byte[] item = copy(array, start, end);
				result.add(item);
				start = end + 1;
				end = start;
			}
			
		}
		
		if(start != limit || (start == limit && array[limit] != 32) ){
			byte[] item = copy(array, start, limit + 1);
			result.add(item);
		}
		
		return result.toArray(new byte[0][]);
	}
	
	public static int toInt(byte[] value){
		//byte[] chars   = new byte[value.length];
		int limit      = value.length - 1;
		byte signal    = value[0] == NEGATIVE? FALSE : TRUE;
		byte hasSignal = value[0] == NEGATIVE || value[0] == POSITIVE? TRUE : FALSE;
		
		int start  = hasSignal == TRUE? 1 : 0;
		int result = 0;
		int mult   = 1;
		
		for(int i=limit;i>=start;i--){
			int tmp = value[i] - ZERO;
			result += tmp*mult;
			mult   *= 10;
		}
		
		if(signal == FALSE){
			result = result | NEGATIVE_INT;
		}
		
		return result;
	}
	
	public static void main(String[] a){
		byte[] data = "-100".getBytes();
		int r = toInt(data);
	}
	
	public static String toString(byte[] value){
		char[] chars = new char[value.length];
		int len = value.length;
		
		for(int i=0;i<len;i++){
			chars[i] = (char)value[i];
		}
		
		return new String(chars);
	}
	
	private static byte[] copy(byte[] origin, int start, int end){
		int len = end-start;
		byte[] item = new byte[end-start];
		System.arraycopy(origin, start, item, 0, len);
		return item;
	}
	
}
