package bo.com.qbit.webapp.model;
// Generated Jul 30, 2014 2:47:16 PM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Class ParametroEmpresa
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@Table(name = "parametro_empresa", schema = "public")
@SuppressWarnings("serial")
public class ParametroEmpresa implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="centro_costo",nullable=false )
	private boolean centroCosto;
	
	@Column(name="facturacion_computarizada",nullable=false )
	private boolean facturacionComputarizada;
	
	@Column(name="nivel_maximo",nullable=false )
	private Integer nivelMaximo;
	
	@Column(name="codificacion_etandar",nullable=false )
	private String codificacionEtandar;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_empresa", nullable=false)
	private Empresa empresa;
	
	//------
	@Size(max = 2) //AC , IN
	@Column(name="estado",nullable=true )
	private String estado;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	@Column(name="fecha_modificacion",nullable=true )
	private Date fechaModificacion;
	
	@Column(name="usuario_registro",nullable=false )
	private String usuarioRegistro;

	public ParametroEmpresa() {
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
			if(!(obj instanceof ParametroEmpresa)){
				return false;
			}else{
				if(((ParametroEmpresa)obj).id==this.id){
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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public boolean isCentroCosto() {
		return centroCosto;
	}

	public void setCentroCosto(boolean centroCosto) {
		this.centroCosto = centroCosto;
	}

	public Integer getNivelMaximo() {
		return nivelMaximo;
	}

	public void setNivelMaximo(Integer nivelMaximo) {
		this.nivelMaximo = nivelMaximo;
	}

	public String getCodificacionEtandar() {
		return codificacionEtandar;
	}

	public void setCodificacionEtandar(String codificacionEtandar) {
		this.codificacionEtandar = codificacionEtandar;
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

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public boolean isFacturacionComputarizada() {
		return facturacionComputarizada;
	}

	public void setFacturacionComputarizada(boolean facturacionComputarizada) {
		this.facturacionComputarizada = facturacionComputarizada;
	}

	
}


