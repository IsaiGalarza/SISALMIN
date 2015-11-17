package bo.com.qbit.webapp.util;

import java.util.Date;

public class EDTipoCambio {
	
	private Date fecha;
	private double tipoCambio;
	private double ufv;
	
	public EDTipoCambio(Date fecha, double tipoCambio, double ufv) {
		super();
		this.setFecha(fecha);
		this.setTipoCambio(tipoCambio);
		this.setUfv(ufv);
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

	public double getUfv() {
		return ufv;
	}

	public void setUfv(double ufv) {
		this.ufv = ufv;
	}	

}
