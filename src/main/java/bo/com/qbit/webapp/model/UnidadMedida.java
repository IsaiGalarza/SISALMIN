package bo.com.qbit.webapp.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the UnidadMedida database table.
 * 
 */
@Entity 
@Table(name="unidad_medida" ,schema="public", uniqueConstraints = @UniqueConstraint(columnNames="nombre"))
public class UnidadMedida implements Serializable {
 
	private static final long serialVersionUID = 5502501064662404770L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String nombre;

	@Column(name="descripcion")
	private String descripcion;
	
	private String estado;
	
	@Column(name="usuario_registro")
	private String usuarioRegistro;
	
	@Column(name="fecha_registro")
	private Date fechaRegistro;

	public UnidadMedida() {
		this.id = 0 ;
		this.nombre = "";
		this.estado= "AC";
		this.descripcion = "";
	}

	@Override
	public String toString() {
		return nombre ;
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


}