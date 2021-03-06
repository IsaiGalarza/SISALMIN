package bo.com.qbit.webapp.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the alm_producto database table.
 * 
 */
@Entity
@Table(name="almacen_producto" ,schema="public")
@NamedQuery(name="AlmacenProducto.findAll", query="SELECT a FROM AlmacenProducto a")
public class AlmacenProducto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="precio_unitario", nullable = true)
	private double precioUnitario;

	private double stock;

	@Column(name="stock_min")
	private double stock_min;

	@Column(name="stock_max")
	private double stock_max;

	private String estado;

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
	@JoinColumn(name="id_producto", nullable=false)
	private Producto producto;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_proveedor", nullable=true)
	private Proveedor proveedor;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_gestion", nullable=true)
	private Gestion gestion;

	public AlmacenProducto() {
		this.producto = new Producto();
		this.proveedor= new Proveedor();
		this.almacen= new Almacen();
		this.precioUnitario = 0;
		this.stock = 0;
		this.stock_max = 500;
		this.stock_min = 1;
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

	public double getStock() {
		return this.stock;
	}

	public void setStock(double stock) {
		this.stock = stock;
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


	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}


	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}


	public double getStock_max() {
		return stock_max;
	}

	public void setStock_max(double stock_max) {
		this.stock_max = stock_max;
	}

	public double getStock_min() {
		return stock_min;
	}

	public void setStock_min(double stock_min) {
		this.stock_min = stock_min;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public Gestion getGestion() {
		return gestion;
	}

	public void setGestion(Gestion gestion) {
		this.gestion = gestion;
	}

}