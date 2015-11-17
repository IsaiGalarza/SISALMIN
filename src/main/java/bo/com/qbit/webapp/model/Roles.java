package bo.com.qbit.webapp.model;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import bo.com.qbit.webapp.validator.Validator;

/**
 * Class Roles
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="roles", catalog="public")
@NamedQuery(name = Roles.ALL, query = "SELECT r FROM Roles r")
public class Roles extends Validator implements java.io.Serializable { 

	public final static String ALL = "Roles.findAll";	

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", unique=true, nullable=false)
	private Integer id;
	
	@Column(name="name", unique=true, nullable=false, length=25)
	private String name;
	
	@Size(max = 2) //AC , IN
	private String state;
	
	@Column(name="usuario_registro",nullable=false )
	private String usuarioRegistro;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	public Roles() {
		super();
		this.id = 0;
		this.name = "";
	}

	public Roles(Integer id, String name,String state) {
		this.id = id;
		this.name = name;
		this.state = state;
	}

	@Override
	public String toString() {
		return name ;
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
			if(((Roles)obj).id==this.id){
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	public boolean validate(FacesContext facesContext,Empresa empresa){
		if(isEmppty(this.name)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"El campo nombre no puede ser vacío!","");
			facesContext.addMessage(null, m);
			return false;
		}
		if(exist("Roles", "name", this.name)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"nombre Rol ya existe!", name);
			facesContext.addMessage(null, m);
			return false;
		}		
		return true;
	}
}


