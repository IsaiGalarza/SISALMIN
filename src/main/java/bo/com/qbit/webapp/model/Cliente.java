package bo.com.qbit.webapp.model;

import java.io.Serializable;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.*;
import javax.validation.constraints.Size;

import bo.com.qbit.webapp.validator.Validator;

/**
 * Class Cliente
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "cliente", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {"nombre","id_empresa"}))
public class Cliente extends Validator implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	
	private String codigo;
	private String telefono;
		
	private String direccion;
	private String correo;
	
	//tipo de cliente
	@Column(name="tipo",nullable=true )//false
	private String tipo;//NATURAL o JURIDICO
	
	//cliente normal
	@Column(name="ci",nullable=true )
	private String ci;
	private String nombre;
	
	//cliente juridico
	@Column(name="nit",nullable=true )
	private String nit;
	@Column(name="razon_social",nullable=true )
	private String razonSocial;
	
	@Column(name="permitir_credito",nullable=false )
	private String permitirCredito;
	
	@Column(name="dias_permitidos",nullable=true )
	private int diasPermitidosCredito;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_empresa", nullable=false)
	private Empresa empresa;
	
	@Size(max = 2) //AC , IN
	private String estado;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	@Column(name="fecha_modificacion",nullable=true )
	private Date fechaModificacion;
	
	@Column(name="usuario_registro",nullable=false )
	private String usuarioRegistro;

	public Cliente() {
		super();
		this.id= 0 ;
		this.nombre= "";
		this.codigo= "";
		this.telefono= "";
		this.tipo = "NATURAL";
		this.nit = "";
		this.razonSocial ="";
		this.direccion= "";
		this.correo= "";
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
			if(!(obj instanceof Cliente)){
				return false;
			}else{
				if(((Cliente)obj).id==this.id){
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
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getPermitirCredito() {
		return permitirCredito;
	}

	public void setPermitirCredito(String permitirCredito) {
		this.permitirCredito = permitirCredito;
	}

	public int getDiasPermitidosCredito() {
		return diasPermitidosCredito;
	}

	public void setDiasPermitidosCredito(int diasPermitidosCredito) {
		this.diasPermitidosCredito = diasPermitidosCredito;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}
	
	public boolean validate(FacesContext facesContext,Empresa empresa){
		if(isEmppty(this.nombre)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"El campo nombre no puede ser vacío!",""+nombre);
			facesContext.addMessage(null, m);
			return false;
		}
		if(isEmppty(this.correo)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"El campo correo no puede ser vacío!","");
			facesContext.addMessage(null, m);
			return false;
		}
		if(isEmppty(this.telefono)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"El campo telefono no puede ser vacío!","");
			facesContext.addMessage(null, m);
			return false;
		}
		if(isEmppty(this.direccion)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"El campo direccion no puede ser vacío!","");
			facesContext.addMessage(null, m);
			return false;
		}
		if(existByEmpresa("Cliente", "nombre", this.nombre,empresa)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"nombre Cliente ya existe!", nombre);
			facesContext.addMessage(null, m);
			return false;
		}
		if(isValidateEmail(correo)){
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"email no válido!", "Ej: email@dominio.com");
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

	public String getCi() {
		return ci;
	}

	public void setCi(String ci) {
		this.ci = ci;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}


