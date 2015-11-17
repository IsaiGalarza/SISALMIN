package bo.com.qbit.webapp.model.security;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.validator.Validator;

/**
 * Class Rol
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@Table(name="rol", catalog="public")
public class Rol extends Validator implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", unique=true, nullable=false)
	private Integer id;
	
	@Column(name="nombre", unique=true, nullable=false, length=255)
	private String nombre;
	
	@Column(name="descripcion",  nullable=true, length=255)
	private String descripcion;
	
	@Size(max = 2) //AC , IN
	private String estado;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	@Column(name="fecha_modificacion",nullable=true )
	private Date fechaModificacion;
	
	@Column(name="usuario_registro",nullable=false )
	private String usuarioRegistro;
	
	public Rol() {
		super();
		this.id = 0;
		this.nombre = "";
		this.descripcion = "";
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
			if(((Rol)obj).id==this.id){
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
		if(isEmppty(this.nombre)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"El campo nombre no puede ser vacío!","");
			facesContext.addMessage(null, m);
			return false;
		}
		if(exist("Roles", "name", this.nombre)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"nombre Rol ya existe!", nombre);
			facesContext.addMessage(null, m);
			return false;
		}		
		return true;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}


