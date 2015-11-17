package bo.com.qbit.webapp.util;

public class EDTemplatePlanCuenta {
	private String codigo;
	private String cuenta;
	private String tipo; // DEBE - HABER
	private String clase;
	
	
	public EDTemplatePlanCuenta() {
		super();
	}
	
	public EDTemplatePlanCuenta(String codigo, String cuenta,String tipo ,String clase) {
		super();
		this.codigo = codigo;
		this.cuenta = cuenta;
		this.clase = clase;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	@Override
	public String toString() {
		return codigo ;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	
}
