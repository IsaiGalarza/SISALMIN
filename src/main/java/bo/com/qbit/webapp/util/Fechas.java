package bo.com.qbit.webapp.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Fechas {

	//Metodo usado para obtener la fecha actual
	//@return Retorna un <b>STRING</b> con la fecha actual formato "dd-MM-yyyy"
	public static String getFechaActual() {
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
		return formateador.format(ahora);
	}
	
	public static String deDateAString(Date date){
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
		return formateador.format(date);
	}

	//Metodo usado para obtener la hora actual del sistema
	//@return Retorna un <b>STRING</b> con la hora actual formato "hh:mm:ss"
	public static String getHoraActual() {
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("hh:mm:ss");
		return formateador.format(ahora);
	}

	//Sumarle dias a una fecha determinada
	//@param fch La fecha para sumarle los dias
	//@param dias Numero de dias a agregar
	//@return La fecha agregando los dias
	public static java.sql.Date sumarFechasDias(java.sql.Date fch, int dias) {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(fch.getTime());
		cal.add(Calendar.DATE, dias);
		return new java.sql.Date(cal.getTimeInMillis());
	}

	//Restarle dias a una fecha determinada
	//@param fch La fecha
	//@param dias Dias a restar
	//@return La fecha restando los dias
	public static synchronized java.sql.Date restarFechasDias(java.sql.Date fch, int dias) {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(fch.getTime());
		cal.add(Calendar.DATE, -dias);
		return new java.sql.Date(cal.getTimeInMillis());
	}

	//Diferencias entre dos fechas
	//@param fechaInicial La fecha de inicio
	//@param fechaFinal  La fecha de fin
	//@return Retorna el numero de dias entre dos fechas
	public static synchronized int diferenciasDeFechas(Date fechaInicial, Date fechaFinal) {

		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		String fechaInicioString = df.format(fechaInicial);
		try {
			fechaInicial = df.parse(fechaInicioString);
		} catch (ParseException ex) {
		}

		String fechaFinalString = df.format(fechaFinal);
		try {
			fechaFinal = df.parse(fechaFinalString);
		} catch (ParseException ex) {
		}

		long fechaInicialMs = fechaInicial.getTime();
		long fechaFinalMs = fechaFinal.getTime();
		long diferencia = fechaFinalMs - fechaInicialMs;
		double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
		return ((int) dias);
	}

	//Devuele un java.util.Date desde un String en formato dd-MM-yyyy
	//@param La fecha a convertir a formato date
	//@return Retorna la fecha en formato Date
	public static synchronized java.util.Date deStringToDate(String fecha) {
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
		Date fechaEnviar = null;
		try {
			fechaEnviar = formatoDelTexto.parse(fecha);
			return fechaEnviar;
		} catch (ParseException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static java.util.Date cambiarYearDate(int year){
		java.util.Date date = new Date();
		GregorianCalendar otroCalendario = new GregorianCalendar();
		otroCalendario.setTime(date);
		otroCalendario.set(Calendar.YEAR, year);
		return otroCalendario.getTime();

	} 

	public static Date restarDiasFecha(Date fecha, int dias){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha); // Configuramos la fecha que se recibe
		calendar.add(Calendar.DAY_OF_YEAR, -dias);  // numero de días a añadir, o restar en caso de días<0
		return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
	}

	public static Date sumarFechaDia(Date fch, int dias) {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(fch.getTime());
		cal.add(Calendar.DATE, dias);
		return new Date(cal.getTimeInMillis());
	}

	public static synchronized Date restarFechasDias(Date fch, int dias) {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(fch.getTime());
		cal.add(Calendar.DATE, -dias);
		return new java.sql.Date(cal.getTimeInMillis());
	}

	public static Date getUltimoDiaDelMesActual() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH),
				cal.getActualMaximum(Calendar.DAY_OF_MONTH),
				cal.getMaximum(Calendar.HOUR_OF_DAY),
				cal.getMaximum(Calendar.MINUTE),
				cal.getMaximum(Calendar.SECOND));
		return cal.getTime();
	}

	public static int getUltimoDiaMes(int anio, int mes) {
		Calendar calendario=Calendar.getInstance();
		calendario.set(anio, mes-1, 1);
		return calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static void main(String[] args){
		System.out.println("hora actual Java.util "+new Date());
		System.out.println("hora actual restarFechasDias"+ restarDiasFecha(new Date(),1));
		System.out.println("hora actual "+ sumarFechaDia(new Date(), 1));

	}
	
	 public static String obtenerFormatoYYYYMMDD(Date date){
	    	try {
	    		String DATE_FORMAT = "yyyyMMdd";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	    		return sdf.format(date);
	    		
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error en obtenerFormatoYYYYMMDD: "+e.getMessage());
				return null;
			}
	 }




}
