package bo.com.qbit.webapp.model;

import java.io.Serializable;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.*;
import javax.validation.constraints.Size;

import bo.com.qbit.webapp.validator.Validator;

/**
 * Class Servicio
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "servicio", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {"nombre","id_empresa"}))
public class Servicio extends Validator implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String nombre;
	
	@Column(name="precio_referencial",nullable=true )
	private double precioReferencial;
	
	@Size(max = 2) //AC , IN
	private String estado;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_empresa", nullable=false)
	private Empresa empresa;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	@Column(name="usuario_registro",nullable=false )
	private String usuarioRegistro;

	public Servicio() {
		this.nombre = "";
		this.precioReferencial = 0;
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
		if(obj==null){
			return false;
		}else{
			if(!(obj instanceof Servicio)){
				return false;
			}else{
				if(((Servicio)obj).id==this.id){
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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public double getPrecioReferencial() {
		return precioReferencial;
	}

	public void setPrecioReferencial(double precioReferencial) {
		this.precioReferencial = precioReferencial;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
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


}


