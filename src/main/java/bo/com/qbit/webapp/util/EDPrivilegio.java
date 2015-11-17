package bo.com.qbit.webapp.util;

public class EDPrivilegio {
	
	private String nombre;
	private String nivel;
	private String icon;
	private String padre;
	
	public EDPrivilegio() {
		super();
	}
	
	public EDPrivilegio(String nombre, String nivel, String icon,String padre) {
		super();
		this.nombre = nombre;
		this.nivel = nivel;
		this.icon = icon;
		this.padre = padre;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPadre() {
		return padre;
	}

	public void setPadre(String padre) {
		this.padre = padre;
	}
	
//	@Override
//	public String toString(){
//		return this.nombre;
//	}

}
