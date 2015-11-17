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
 * Class DetallePagina
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */

@Entity
@SuppressWarnings("serial")
@Table(name="detalle_pagina", catalog="public")
public class DetallePagina implements Serializable {

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", unique=true, nullable=false)
	private Integer id;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=true)
    @JoinColumn(name="id_pagina", nullable=false)
	private Pagina pagina;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=true)
    @JoinColumn(name="id_accion", nullable=false)
	private Accion accion;

	public DetallePagina() {
		super();
		this.id = 0;
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
			if(((DetallePagina)obj).id==this.id){
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

	public Pagina getPagina() {
		return pagina;
	}

	public void setPagina(Pagina pagina) {
		this.pagina = pagina;
	}

	public Accion getAccion() {
		return accion;
	}

	public void setAccion(Accion accion) {
		this.accion = accion;
	}
}


