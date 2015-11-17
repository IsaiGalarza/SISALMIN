package bo.com.qbit.webapp.util;

public class EDCentroCosto {
	
	private int id;
	private String grupo;
	private String nombre;
		
	public EDCentroCosto(int id, String grupo, String nombre) {
		super();
		this.id = id;
		this.grupo = grupo;
		this.nombre = nombre;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getGrupo() {
		return grupo;
	}
	
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Override
	public String toString() {
		return "EDCentroCosto [id=" + id + ", grupo=" + grupo + ", nombre="
				+ nombre + "]";
	}
	

	
}
