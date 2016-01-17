package bo.com.qbit.webapp.util;

public class LetrasUtil {

	private static final String ORIGINAL
	= "ÁáÉéÍíÓóÚúÜü";
	private static final String REPLACEMENT
	= "AaEeIiOoUuUu";
	
	public static void main(String arg[]){
		//LetrasUtil.stripAccents
		System.out.println(" Output: " + stripAccents("Aló ñato Ñ"));
	}
	
	
	public static String stripAccents(String str) {
		if (str == null) {
			return null;
		}
		char[] array = str.toCharArray();
		for (int index = 0; index < array.length; index++) {
			int pos = ORIGINAL.indexOf(array[index]);
			if (pos > -1) {
				array[index] = REPLACEMENT.charAt(pos);
			}
		}
		return new String(array);
	}

}
