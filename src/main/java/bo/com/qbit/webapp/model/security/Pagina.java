package bo.com.qbit.webapp.model.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * Class Pagina
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="pagina", catalog="public")
public class Pagina implements Serializable {

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", unique=true, nullable=false)
	private Integer id;
	
	@Column(name="nombre", unique=true, nullable=false, length=25)
	private String nombre;
	
	@Column(name="path", nullable=false, length=25)
	private String path;
	
	@Column(name="path2", nullable=true, length=25)
	private String path2;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=true)
    @JoinColumn(name="id_modulo", nullable=false)
	private Modulo modulo;

	public Pagina() {
		super();
		this.id = 0;
		this.nombre = "";
	}

	@Override
	public String toString() {
		return nombre ;
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
			if(((Pagina)obj).id==this.id){
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath2() {
		return path2;
	}

	public void setPath2(String path2) {
		this.path2 = path2;
	}
}


