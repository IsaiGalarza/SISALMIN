package bo.com.qbit.webapp.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the almacen database table.
 * 
 */
@Entity 
@Table(name="partida" ,schema="public", uniqueConstraints = @UniqueConstraint(columnNames = "nombre"))
@NamedQuery(name="Partida.findAll", query="SELECT p FROM Partida p")
public class Partida implements Serializable {

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


	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="id_partida_padre", nullable= true)
	private Partida partida;

	public Partida() {
		super();
		this.id = 0;
		this.nombre="";
		this.codigo="";
		this.descripcion="Ninguna";
	}
	
	@Override
	public String toString() {
		return nombre;
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

	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}