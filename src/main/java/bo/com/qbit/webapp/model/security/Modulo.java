package bo.com.qbit.webapp.model.security;

import groovy.lang.Lazy;

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
 * Class Modulo
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="modulo", catalog="public")
public class Modulo implements Serializable {

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", unique=true, nullable=false)
	private Integer id;
	
	@Column(name="nombre", unique=true, nullable=false, length=25)
	private String nombre;
	
	@ManyToOne(fetch=FetchType.LAZY,optional=true)
    @JoinColumn(name="id_modulo_padre", nullable=true)
	@Lazy
	private Modulo moduloPadre;
	
	public Modulo() {
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
			if(((Modulo)obj).id==this.id){
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

	public Modulo getModuloPadre() {
		return moduloPadre;
	}

	public void setModuloPadre(Modulo moduloPadre) {
		this.moduloPadre = moduloPadre;
	}

}


