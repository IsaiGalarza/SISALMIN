package bo.com.qbit.webapp.model;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Class Nivel
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "nivel", schema = "public")
public class Nivel implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private Integer nivel;
	
	private Integer nroDigito;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="id_empresa", nullable=true)
	private Empresa empresa;

	public Nivel() {
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
		if(obj==null){
			return false;
		}else{
			if(!(obj instanceof Nivel)){
				return false;
			}else{
				if(((Nivel)obj).id==this.id){
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

	public Integer getNroDigito() {
		return nroDigito;
	}

	public void setNroDigito(Integer nroDigito) {
		this.nroDigito = nroDigito;
	}

	public Integer getNivel() {
		return nivel;
	}

	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
}


