package bo.com.qbit.webapp.model;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Class Moneda
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "moneda", schema = "public")
public class Moneda implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String nombre;
	
	@Column(name="simbolo_referencial",nullable=true )
	private String simboloReferencial;

	public Moneda() {
		super();
		this.id = 0;
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
			if(!(obj instanceof Moneda)){
				return false;
			}else{
				if(((Moneda)obj).id==this.id){
					return true;
				}else{
					return false;
				}
			}
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

	public String getSimboloReferencial() {
		return simboloReferencial;
	}

	public void setSimboloReferencial(String simboloReferencial) {
		this.simboloReferencial = simboloReferencial;
	}

}


