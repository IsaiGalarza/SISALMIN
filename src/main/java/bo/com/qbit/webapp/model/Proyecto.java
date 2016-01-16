package bo.com.qbit.webapp.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the Proyecto database table.
 * 
 */
@Entity 
@Table(name="proyecto" ,schema="public")
public class Proyecto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String codigo;
	
	private String nombre;
	
	private String descripcion;

	private String estado;
	
	@Column(name="fecha_registro")
	private Date fechaRegistro;

	@Column(name="usuario_registro")
	private String usuarioRegistro;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_gestion", nullable=true)
	private Gestion gestion;

	public Proyecto() {
		super();
		this.id = 0;
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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Gestion getGestion() {
		return gestion;
	}

	public void setGestion(Gestion gestion) {
		this.gestion = gestion;
	}

}