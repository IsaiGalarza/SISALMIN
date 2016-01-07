package bo.com.qbit.webapp.model;

import java.io.Serializable;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.*;
import javax.validation.constraints.Size;

import bo.com.qbit.webapp.validator.Validator;

/**
 * Class Empresa
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */

@Entity
@Table(name = "empresa", catalog = "public", uniqueConstraints = @UniqueConstraint(columnNames="razonSocial"))
public class Empresa extends Validator implements Serializable {

	private static final long serialVersionUID = 8780729092507900621L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String razonSocial;
	private String direccion;
	private String telefono;
	private String NIT;
	private String ciudad;
	
	@Column(name = "foto_perfil", nullable = true)
	private byte[] fotoPerfil;
	
	@Column(name = "peso_foto", nullable = true)
	private int pesoFoto;
	
	@Size(max = 2) //AC , IN
	private String estado;
	
	@Column(name="usuario_registro",nullable=false )
	private String usuarioRegistro;
	
	private Date fecha_registro;
	
	@Column(name="fecha_modificacion",nullable=true )
	private Date fechaModificacion;

	public Empresa() {
		super();
		this.id = 0;
		this.razonSocial = "";
		this.direccion = "";
		this.telefono = "";
		this.NIT = "";
		this.ciudad = "";
		this.pesoFoto = 0;
	}
	
	@Override
	public String toString() {
		return razonSocial;
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
			if(!(obj instanceof Empresa)){
				return false;
			}else{
				if(((Empresa)obj).id==this.id){
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

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getNIT() {
		return NIT;
	}

	public void setNIT(String nIT) {
		NIT = nIT;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public Date getFecha_registro() {
		return fecha_registro;
	}

	public void setFecha_registro(Date fecha_registro) {
		this.fecha_registro = fecha_registro;
	}
	
	public boolean validate(FacesContext facesContext,Empresa empresa){
		if(isEmppty(this.razonSocial)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"El campo razon social no puede ser vacío!","");
			facesContext.addMessage(null, m);
			return false;
		}
		if(isEmppty(this.direccion)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"El campo dirección no puede ser vacío!","");
			facesContext.addMessage(null, m);
			return false;
		}
		if(isEmppty(this.telefono)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"El campo telefono no puede ser vacío!","");
			facesContext.addMessage(null, m);
			return false;
		}
		if(isEmppty(this.NIT)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"El campo NIT no puede ser vacío!","");
			facesContext.addMessage(null, m);
			return false;
		}
		if(isEmppty(this.ciudad)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"El campo ciudad no puede ser vacío!","");
			facesContext.addMessage(null, m);
			return false;
		}
		if(! isNumeric(this.telefono)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"El campo telefono no es válido!","");
			facesContext.addMessage(null, m);
			return false;
		}
		if(exist("Empresa", "razonSocial", this.razonSocial)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"nombre Empresa ya existe!", razonSocial);
			facesContext.addMessage(null, m);
			return false;
		}
		return true;
	}
	
	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public byte[] getFotoPerfil() {
		return fotoPerfil;
	}

	public void setFotoPerfil(byte[] fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}

	public int getPesoFoto() {
		return pesoFoto;
	}

	public void setPesoFoto(int pesoFoto) {
		this.pesoFoto = pesoFoto;
	}

}


