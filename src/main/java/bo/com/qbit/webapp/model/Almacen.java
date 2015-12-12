package bo.com.qbit.webapp.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the almacen database table.
 * 
 */
@Entity 
@Table(name="almacen" ,schema="public", uniqueConstraints = @UniqueConstraint(columnNames="codigo"))
@NamedQuery(name="Almacen.findAll", query="SELECT a FROM Almacen a")
public class Almacen implements Serializable {

	private static final long serialVersionUID = 347357319180148125L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String direccion;

	@Column(name="codigo",nullable=true)
	private String codigo;
	
	@Column(name="online")
	private boolean online;

	private String nombre;

	private String telefono;

	@Column(name="tipo_almacen")
	private String tipoAlmacen;
	
	private String estado;
	
	@Column(name="usuario_registro")
	private String usuarioRegistro;
	
	@Column(name="fecha_registro")
	private Date fechaRegistro;

	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_encargado",nullable= true)
	private Usuario encargado;

	public Almacen() {
		this.id = 0 ;
		encargado= new Usuario();
		estado= "AC";
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

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
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

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	@Column(name="tipo_almacen")
	public String getTipoAlmacen() {
		return tipoAlmacen;
	}

	public void setTipoAlmacen(String tipoAlmacen) {
		this.tipoAlmacen = tipoAlmacen;
	}

	public Usuario getEncargado() {
		return encargado;
	}

	public void setEncargado(Usuario encargado) {
		this.encargado = encargado;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}