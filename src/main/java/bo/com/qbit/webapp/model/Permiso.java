package bo.com.qbit.webapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;


/**
 * Class Permiso
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="permiso", catalog="public")
@NamedQuery(name = Permiso.ALL, query = "SELECT r FROM Permiso r")
public class Permiso implements java.io.Serializable {

	public final static String ALL = "Permiso.findAll";	

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", unique=true, nullable=false)
	private Integer id;
	
	@Column(name="nombre", unique=true, nullable=false, length=50)
	private String name;
	
	@Column(name="tipo",nullable=true )
	private String tipo;
	
	@Column(name="padre",nullable=true )
	private String padre;
	
	@Size(max = 2) //AC , IN
	private String estado;

	public Permiso() {
		super();
		this.id = 0;
	}

	public Permiso(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return String.valueOf(id);
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		try {
			if(((Permiso)obj).id==this.id){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getPadre() {
		return padre;
	}

	public void setPadre(String padre) {
		this.padre = padre;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

}


