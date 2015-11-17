package bo.com.qbit.webapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Class UsuarioEmpresa
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@Table(name="usuario_empresa", catalog="public")
@SuppressWarnings("serial")
@NamedQuery(name = UsuarioEmpresa.ALL, query = "SELECT ue FROM UsuarioEmpresa ue")
public class UsuarioEmpresa implements java.io.Serializable { 

	public final static String ALL = "UsuarioEmpresa.findAll";	

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", unique=true, nullable=false)
	private Integer id;

	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_usuario", nullable=false)
	private Usuario usuario;

	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_empresa", nullable=false)
	private Empresa empresa;

	public UsuarioEmpresa() {
		super();
		this.id = 0;
	}	

	public UsuarioEmpresa(Integer id, Usuario usuario, Empresa empresa) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.empresa = empresa;
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
			if(((UsuarioEmpresa)obj).id==this.id){
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

}


