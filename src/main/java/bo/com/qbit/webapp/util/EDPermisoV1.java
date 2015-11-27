package bo.com.qbit.webapp.util;

public class EDPermisoV1 {

	private String nombre;
	private String icon;
	private String tipo;
	
	private Object object;

	// Constructor
	public EDPermisoV1() {
		super();		
	}

	public EDPermisoV1(String nombre, String icon, String tipo,Object object) {
		super();
		this.nombre = nombre;
		this.icon = icon;
		this.tipo = tipo;
		this.object = object;
	}



	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
	


}
