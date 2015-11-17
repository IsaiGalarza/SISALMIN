package bo.com.qbit.webapp.service;

public class EDNivel {
	
	private String id;
	private String nombre;
	private Integer tamanio;
	
	public EDNivel(String id, String nombre, Integer tamanio) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.tamanio = tamanio;
	}

	public EDNivel() {
		super();
		this.id= "0";
	}
	
	@Override
	public String toString() {
		return nombre;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}else{
			if(!(obj instanceof EDNivel)){
				return false;
			}else{
				if(((EDNivel)obj).id==this.id){
					return true;
				}else{
					return false;
				}
			}
		}
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

	public Integer getTamanio() {
		return tamanio;
	}

	public void setTamanio(Integer tamanio) {
		this.tamanio = tamanio;
	}
	
	
	

}
