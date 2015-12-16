package bo.com.qbit.webapp.model;

import java.io.Serializable;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.*;
import javax.validation.constraints.Size;

import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.validator.Validator;

/**
 * Class Usuario
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "usuario", catalog = "public", uniqueConstraints = @UniqueConstraint(columnNames="login"))
public class Usuario extends Validator implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String nombre;
	private String email;
	private String login;
	private String password;

	@Size(max = 2) //AC , IN
	private String state;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	@Column(name="fecha_modificacion",nullable=true )
	private Date fechaModificacion;

	@Column(name = "foto_perfil", nullable = true)
	private byte[] fotoPerfil;
	
	@Column(name = "peso_foto", nullable = true)
	private int pesoFoto;

	@Column(name="usuario_registro",nullable=false )
	private String usuarioRegistro;

	public Usuario() {
		super();
		this.id = 0 ;
		this.nombre = "";
		this.email = " ";
		this.login = "";
		this.password = "";
		this.state = "AC";
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
			if(!(obj instanceof Usuario)){
				return false;
			}else{
				if(((Usuario)obj).id==this.id){
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

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public byte[] getFotoPerfil() {
		return fotoPerfil;
	}

	public void setFotoPerfil(byte[] fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public int getPesoFoto() {
		return pesoFoto;
	}

	public void setPesoFoto(int pesoFoto) {
		this.pesoFoto = pesoFoto;
	}

	public boolean validate(FacesContext facesContext,Empresa empresa , Gestion gestion){
		if(isEmppty(this.nombre)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"El campo nombre no puede ser vacío!",""+nombre);
			facesContext.addMessage(null, m);
			return false;
		}
		if(isEmppty(this.email)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"El campo email no puede ser vacío!","");
			facesContext.addMessage(null, m);
			return false;
		}
		if(isEmppty(this.login)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"El campo login no puede ser vacío!","");
			facesContext.addMessage(null, m);
			return false;
		}
		if(isEmppty(this.password)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"El campo Contraseña no puede ser vacío!","");
			facesContext.addMessage(null, m);
			return false;
		}
//		if(existUsuario( this.login)){
//			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
//					"Usuario login ya existe!", nombre);
//			facesContext.addMessage(null, m);
//			return false;
//		}
//		if(isValidateEmail(email)){
//			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
//					"email no válido!", "Ej: email@dominio.com");
//			facesContext.addMessage(null, m);
//			return false;
//		}
		return true;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
}


