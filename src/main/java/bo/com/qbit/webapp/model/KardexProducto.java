package bo.com.qbit.webapp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the KardexProducto database table.
 * 
 */
@Entity
@Table(name = "kardex_producto", schema = "public")
public class KardexProducto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String transaccion;// VENTA,COMPRA,TRASPASO,BAJAS

	private String estado;
	
	private Date fecha;

	private double cantidad;

	@Column(name = "precio_compra")
	private double precioCompra;

	@Column(name = "precio_venta")
	private double precioVenta;
	
	@Column(name = "stock")//cantidad entrante
	private double stock;

	@Column(name = "stock_anterior")//
	private double stockAnterior;

	@Column(name = "stock_actual")//Saldo
	private double stockActual;

	@Column(name = "tipo_movimiento")
	private String tipoMovimiento;
	
	@Column(name = "unidad_solicitante")
	private String unidadSolicitante;

	@Column(name = "numero_transaccion")
	private String numeroTransaccion;

	@Column(name = "usuario_registro")
	private String usuarioRegistro;
	
	@Column(name = "fecha_registro")
	private Date fechaRegistro;

	// bi-directional many-to-one association to Usuario
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "id_producto", nullable = true)
	private Producto producto;

	// bi-directional many-to-one association to Usuario
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "id_almacen", nullable = true)
	private Almacen almacen;
	
	// bi-directional many-to-one association to Usuario
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "id_gestion", nullable = true)
	private Gestion gestion;
	
	public KardexProducto(){
		
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

	public double getStockActual() {
		return stockActual;
	}

	public void setStockActual(double precioActual) {
		this.stockActual = precioActual;
	}

	public double getStockAnterior() {
		return stockAnterior;
	}

	public void setStockAnterior(double precioAnterior) {
		this.stockAnterior = precioAnterior;
	}

	public String getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public double getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(double precioVenta) {
		this.precioVenta = precioVenta;
	}

	public double getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(double precioCompra) {
		this.precioCompra = precioCompra;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Almacen getAlmacen() {
		return almacen;
	}

	public void setAlmacen(Almacen almacen) {
		this.almacen = almacen;
	}

	public String getNumeroTransaccion() {
		return numeroTransaccion;
	}

	public void setNumeroTransaccion(String numeroTransaccion) {
		this.numeroTransaccion = numeroTransaccion;
	}

	public String getTransaccion() {
		return transaccion;
	}

	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public Gestion getGestion() {
		return gestion;
	}

	public void setGestion(Gestion gestion) {
		this.gestion = gestion;
	}

	public double getStock() {
		return stock;
	}

	public void setStock(double stock) {
		this.stock = stock;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getUnidadSolicitante() {
		return unidadSolicitante;
	}

	public void setUnidadSolicitante(String unidadSolicitante) {
		this.unidadSolicitante = unidadSolicitante;
	}

}