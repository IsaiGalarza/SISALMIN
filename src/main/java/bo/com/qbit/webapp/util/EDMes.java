package bo.com.qbit.webapp.util;

public class EDMes {

	public static final int ENERO		= 0;
	public static final int FEBRERO		= 1;
	public static final int MARZO		= 2;
	public static final int ABRIL		= 3;
	public static final int MAYO		= 4;
	public static final int JUNIO		= 5;
	public static final int JULIO		= 6;
	public static final int AGOSTO		= 7;
	public static final int SEPTIEMBRE	= 8;
	public static final int OCTUBRE		= 9;
	public static final int NOVIEMBRE	= 10;
	public static final int DICIEMBRE	= 11;
	public static final int TODO		= 12;

	
	public static int getMesNumeral(String mes){
		System.out.println("getMesNumeral("+mes+")");
		int mesOut = 0;
		switch (mes) {
		case "ENERO": mesOut = ENERO; System.out.println("ENERO"); break;
		case "FEBRERO": mesOut = FEBRERO; break;
		case "MARZO": mesOut = MARZO; break;
		case "ABRIL": mesOut = ABRIL; break;
		case "MAYO": mesOut = MAYO; break;
		case "JUNIO": mesOut = JUNIO; break;
		case "JULIO": mesOut = JULIO; break;
		case "AGOSTO": mesOut = AGOSTO; break;
		case "SEPTIEMBRE": mesOut = SEPTIEMBRE; break;
		case "OCTUBRE": mesOut = OCTUBRE; break;
		case "NOVIEMBRE": mesOut = NOVIEMBRE; break;
		case "DICIEMBRE": mesOut = DICIEMBRE; break;
		case "TODO": mesOut = TODO; break;
		default:
			break;
		}
		return mesOut;
	}

}
