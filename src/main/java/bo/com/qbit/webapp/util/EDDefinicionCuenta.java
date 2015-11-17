package bo.com.qbit.webapp.util;

public class EDDefinicionCuenta {
	
	private int digito;
	private String nombreCuenta;
	
	public EDDefinicionCuenta(int digito,String nombreCuenta){
		this.digito = digito;
		this.nombreCuenta = nombreCuenta;
	}
	
	public int getDigito() {
		return digito;
	}
	public void setDigito(int digito) {
		this.digito = digito;
	}
	public String getNombreCuenta() {
		return nombreCuenta;
	}
	public void setNombreCuenta(String nombreCuenta) {
		this.nombreCuenta = nombreCuenta;
	}
	
	

}
