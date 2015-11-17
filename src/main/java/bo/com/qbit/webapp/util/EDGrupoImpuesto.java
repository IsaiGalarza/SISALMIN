package bo.com.qbit.webapp.util;

public class EDGrupoImpuesto {
	private String id;
	private String nombre;
	private String tipo;
	private double porcentaje;
	private String padre;
	
	
	public EDGrupoImpuesto() {
		super();
	}
	
	public EDGrupoImpuesto(String id, String nombre, String tipo,double porcentaje,String padre) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.tipo = tipo;
		this.porcentaje = porcentaje;
		this.padre = padre;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}

	public String getPadre() {
		return padre;
	}

	public void setPadre(String padre) {
		this.padre = padre;
	}




	
}
