package bo.com.qbit.webapp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Class DetalleOrdenSalida
 * @author Isai.Galarza
 * @version v1.0
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "detalle_orden_salida", catalog = "public")
public class DetalleOrdenSalida implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private Integer correlativo;

	@Column(name="cantidad_solicitada", nullable = false)
	private double cantidadSolicitada;
	
	@Column(name="cantidad_entregada")
	private double cantidadEntregada;

	private String estado;
	
	private String obervacion;
	
	private double total;

	@Column(name="fecha_registro")
	private Date fechaRegistro;

	@Column(name="usuario_registro")
	private String usuarioRegistro;

	// bi-directional many-to-one association to PedidoMov
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "id_orden_salida")
	private OrdenSalida ordenSalida;

	// bi-directional many-to-one association to Producto
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "id_producto", nullable = true)
	private Producto producto;

	@Column(name="precio_unitario")
	private double precioUnitario;
	
	public DetalleOrdenSalida() {
		this.cantidadSolicitada = 1;
		this.cantidadEntregada = 1;
		this.setEstado("AC");
		this.fechaRegistro = new Date();
	}
	

	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	public double getTotal() {
		return total;
	}
	
	public void setTotal(double total) {
		this.total = total;
	}
	
	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}
	
	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}
	
	public Producto getProducto() {
		return producto;
	}
	
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getCorrelativo() {
		return correlativo;
	}
	
	public void setCorrelativo(Integer correlativo) {
		this.correlativo = correlativo;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public double getPrecioUnitario() {
		return precioUnitario;
	}
	
	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}


	public String getObervacion() {
		return obervacion;
	}

	public void setObervacion(String obervacion) {
		this.obervacion = obervacion;
	}
	
	public OrdenSalida getOrdenSalida() {
		return ordenSalida;
	}

	public void setOrdenSalida(OrdenSalida ordenSalida) {
		this.ordenSalida = ordenSalida;
	}

	public double getCantidadSolicitada() {
		return cantidadSolicitada;
	}

	public void setCantidadSolicitada(double cantidadSolicitada) {
		this.cantidadSolicitada = cantidadSolicitada;
	}

	public double getCantidadEntregada() {
		return cantidadEntregada;
	}

	public void setCantidadEntregada(double cantidadEntregada) {
		this.cantidadEntregada = cantidadEntregada;
	}
	
}


