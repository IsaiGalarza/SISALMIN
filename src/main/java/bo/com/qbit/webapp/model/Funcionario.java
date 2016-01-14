package bo.com.qbit.webapp.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the Funcionario database table.
 * 
 */
@Entity 
@Table(name="funcionario" ,schema="public")
@NamedQuery(name="Funcionario.findAll", query="SELECT p FROM Funcionario p")
public class Funcionario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String nombre;
	
	private String cargo;
	
	private String apellidos;
	
	private String direccion;
	
	private String telefono;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_detalle_unidad", nullable = true)
	private DetalleUnidad detalleUnidad;

	private String estado;
	
	@Column(name="fecha_registro")
	private Date fechaRegistro;

	
	@Column(name="usuario_registro")
	private String usuarioRegistro;


	public Funcionario() {
		this.id  = 0 ;
		this.detalleUnidad = new DetalleUnidad();
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

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
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

	public DetalleUnidad getDetalleUnidad() {
		return detalleUnidad;
	}

	public void setDetalleUnidad(DetalleUnidad detalleUnidad) {
		this.detalleUnidad = detalleUnidad;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	@Override
	public String toString() {
		return  nombre + " " + apellidos;
	}

	
}