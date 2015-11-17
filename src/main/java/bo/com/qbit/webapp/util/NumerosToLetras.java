package bo.com.qbit.webapp.util;


public class NumerosToLetras {
	
	
	public static void main(String[] args) {
		String country = System.getProperty("user.country"); 
		String lan = System.getProperty("user.language");
		System.out.println("country: "+country);
		System.out.println("lan: "+lan);
		System.out.println(" = "+ convertNumberToLetter(5130.50));
	   }
	
	public static String convertNumberToLetter(String number)
			throws NumberFormatException {
		
		String country = System.getProperty("user.country"); 
		String lan = "es";// System.getProperty("user.language");
		System.out.println("country: "+country);
		System.out.println("lan: "+lan);
		
		String converted = new String();
		
		String splitNumber[] = null;
		//INGLES
		if(lan.equals("en")){
			System.out.println(">>>>>> Idioma Ingles....");
			splitNumber = number.replace('.', '#').split("#"); //INGLES
		}
		//ESPANOL
		if(lan.equals("es")){
			System.out.println(">>>>>> Idioma Espaniol....");
			splitNumber = number.replace(',', '#').split("#"); //ESPANIOL
		}

		// Descompone el trio de millones
		int millon = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0],
				8))
				+ String.valueOf(getDigitAt(splitNumber[0], 7))
				+ String.valueOf(getDigitAt(splitNumber[0], 6)));
		if (millon == 1)
			converted = "UN MILLON ";
		if (millon > 1)
			converted = convertNumber(String.valueOf(millon)) + "MILLONES ";

		// Descompone el trio de miles
		int miles = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0],
				5))
				+ String.valueOf(getDigitAt(splitNumber[0], 4))
				+ String.valueOf(getDigitAt(splitNumber[0], 3)));
		if (miles == 1)
			converted += "UN MIL ";
		if (miles > 1)
			converted += convertNumber(String.valueOf(miles)) + "MIL ";

		// Descompone el ultimo trio de unidades
		int cientos = Integer.parseInt(String.valueOf(getDigitAt(
				splitNumber[0], 2))
				+ String.valueOf(getDigitAt(splitNumber[0], 1))
				+ String.valueOf(getDigitAt(splitNumber[0], 0)));
		if (cientos == 1)
			converted += "UN";

		if (millon + miles + cientos == 0)
			converted += "CERO";
		if (cientos > 1)
			converted += convertNumber(String.valueOf(cientos));

		int centavos = Integer.parseInt(String.valueOf(getDigitAt(
				splitNumber[1], 2))
				+ String.valueOf(getDigitAt(splitNumber[1], 1))
				+ String.valueOf(getDigitAt(splitNumber[1], 0)));
		
		converted += String.valueOf(centavos)+"/100 Bolivianos";

		return converted;
	}
	
	private static final String[] UNIDADES = { "", "UN ", "DOS ", "TRES ",
			"CUATRO ", "CINCO ", "SEIS ", "SIETE ", "OCHO ", "NUEVE ", "DIEZ ",
			"ONCE ", "DOCE ", "TRECE ", "CATORCE ", "QUINCE ", "DIECISEIS ",
			"DIECISIETE ", "DIECIOCHO ", "DIECINUEVE ", "VEINTE " };

	private static final String[] DECENAS = { "VEINTI", "TREINTA ", "CUARENTA ",
			"CINCUENTA ", "SESENTA ", "SETENTA ", "OCHENTA ", "NOVENTA ",
			"CIEN " };

	private static final String[] CENTENAS = { "CIENTO ", "DOSCIENTOS ",
			"TRESCIENTOS ", "CUATROCIENTOS ", "QUINIENTOS ", "SEISCIENTOS ",
			"SETECIENTOS ", "OCHOCIENTOS ", "NOVECIENTOS " };

	public static String convertNumberToLetter(double number) {
		try{
			return convertNumberToLetter(doubleToString(number));
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error en convertNumberToLetter..."+e);
			return null;
		}
	}

	private static String doubleToString(double numero) {
		return String.format("%.2f", numero);
	}

	private static int getDigitAt(String origin, int position) {
		if (origin.length() > position && position >= 0)
			return origin.charAt(origin.length() - position - 1) - 48;
		return 0;
	}
	
	private static String convertNumber(String number) {
		if (number.length() > 3)
			throw new NumberFormatException("La longitud maxima debe ser 3 digitos");

		String output = new String();
		if (getDigitAt(number, 2) != 0)
			output = CENTENAS[getDigitAt(number, 2) - 1];

		int k = Integer.parseInt(String.valueOf(getDigitAt(number, 1))
				+ String.valueOf(getDigitAt(number, 0)));

		if (k <= 20)
			output += UNIDADES[k];
		else {
			if (k > 30 && getDigitAt(number, 0) != 0)
				output += DECENAS[getDigitAt(number, 1) - 2] + "Y "
						+ UNIDADES[getDigitAt(number, 0)];
			else
				output += DECENAS[getDigitAt(number, 1) - 2]
						+ UNIDADES[getDigitAt(number, 0)];
		}

		if (getDigitAt(number, 2) == 1 && k == 0)
			output = "CIEN";

		return output;
	}

	
}