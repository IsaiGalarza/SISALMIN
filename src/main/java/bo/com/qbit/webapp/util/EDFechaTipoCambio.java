package bo.com.qbit.webapp.util;

import java.util.Date;

public class EDFechaTipoCambio {
	private Date fecha;
	private double tipoCambio;
	private double tipoCambioUFV;
	
	public EDFechaTipoCambio(Date fecha, double tipoCambio,
			double tipoCambioUFV) {
		super();
		this.fecha = fecha;
		this.tipoCambio = tipoCambio;
		this.tipoCambioUFV = tipoCambioUFV;
	}
	
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public double getTipoCambio() {
		return tipoCambio;
	}
	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	public double getTipoCambioUFV() {
		return tipoCambioUFV;
	}
	public void setTipoCambioUFV(double tipoCambioUFV) {
		this.tipoCambioUFV = tipoCambioUFV;
	}
	
	

}
