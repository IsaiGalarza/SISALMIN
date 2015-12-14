package bo.com.qbit.webapp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Class Gestion
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@Table(name = "gestion", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {"gestion","id_empresa"}))
public class Gestion implements Serializable {

	private static final long serialVersionUID = 1017112418637684740L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private Integer gestion;
	
	@Column(name="iniciada",nullable=true )
	private boolean iniciada;//al crear una toma de incentario inicial se pone en true
	
	@Column(name="estado_cierre" )
	private String estadoCierre;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_empresa", nullable=false)
	private Empresa empresa;
	
	@Size(max = 2) //AC , IN
	@Column(name="estado", nullable=true )
	private String estado;
	
	@Column(name="fecha_registro",nullable=true )
	private Date fechaRegistro;
	
	@Column(name="fecha_modificacion",nullable=true )
	private Date fechaModificacion;
	
	@Column(name="usuario_registro",nullable=true )
	private String usuarioRegistro;

	public Gestion() {
		super();
		this.id = 0;
		this.iniciada = false;
	}
	
	@Override
	public String toString() {
		return String.valueOf(gestion);
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
			if(!(obj instanceof Gestion)){
				return false;
			}else{
				if(((Gestion)obj).id==this.id){
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
	
	public Integer getGestion() {
		return gestion;
	}

	public void setGestion(Integer gestion) {
		this.gestion = gestion;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstadoCierre() {
		return estadoCierre;
	}

	public void setEstadoCierre(String estadoCierre) {
		this.estadoCierre = estadoCierre;
	}

	public boolean isIniciada() {
		return iniciada;
	}

	public void setIniciada(boolean iniciada) {
		this.iniciada = iniciada;
	}	

}


