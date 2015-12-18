package bo.com.qbit.webapp.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the alm_producto database table.
 * 
 */
@Entity
@Table(name="cierre_gestion_almacen" ,schema="public")
public class CierreGestionAlmacen implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String estado;

	private String descripcion;

	@Column(name="fecha_registro")
	private Date fechaRegistro;

	@Column(name="usuario_registro")
	private String usuarioRegistro;

	//bi-directional many-to-one association to Almacen

	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_almacen", nullable=false)
	private Almacen almacen;

	//bi-directional many-to-one association to Producto
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_gestion", nullable=false)
	private Gestion gestion;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_toma_inventario", nullable=false)
	private TomaInventario tomaInventario;

	public CierreGestionAlmacen() {
		this.id = 0;
		this.almacen= new Almacen();
		this.setGestion(new Gestion());
		this.tomaInventario = new TomaInventario();
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

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public Almacen getAlmacen() {
		return this.almacen;
	}

	public void setAlmacen(Almacen almacen) {
		this.almacen = almacen;
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

	public TomaInventario getTomaInventario() {
		return tomaInventario;
	}

	public void setTomaInventario(TomaInventario tomaInventario) {
		this.tomaInventario = tomaInventario;
	}

}