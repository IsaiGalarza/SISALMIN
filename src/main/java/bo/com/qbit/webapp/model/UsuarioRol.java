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

import bo.com.qbit.webapp.model.security.Rol;

/**
 * Class UsuarioRol
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@Table(name="usuario_rol", catalog="public")
@SuppressWarnings("serial")
@NamedQuery(name = UsuarioRol.ALL, query = "SELECT ur FROM UsuarioRol ur")
public class UsuarioRol  implements java.io.Serializable {

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name="id", unique=true, nullable=false)
	private Integer id;

	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_roles", nullable=false)
	private Roles roles;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_usuario", nullable=false)
	private Usuario usuario;

	public final static String ALL = "UsuarioRol.findAll";

	public UsuarioRol() {
		super();
		this.id = 0;
	}

	public UsuarioRol(int id, Roles roles, Usuario usuario) {
		this.id = id;
		this.roles = roles;
		this.usuario = usuario;
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
			if(!(obj instanceof UsuarioRol)){
				return false;
			}else{
				if(((UsuarioRol)obj).id==this.id){
					return true;
				}else{
					return false;
				}
			}
		}
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Roles getRoles() {
		return this.roles;
	}

	public void setRoles(Roles roles) {
		this.roles = roles;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
