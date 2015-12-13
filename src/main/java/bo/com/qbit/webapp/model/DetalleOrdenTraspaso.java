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

@Entity
@Table(name = "detalle_orden_traspaso", schema = "public")
public class DetalleOrdenTraspaso implements Serializable{

	private static final long serialVersionUID = 6101168071340551453L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="cantidad_solicitada", nullable = true)
	private double cantidadSolicitada;

	@Column(name="cantidad_entregada", nullable = true)
	private double cantidadEntregada;

	private String estado;
	
	private String observacion;
	
	@Column(name="precio_unitario", nullable = true)
	private double precioUnitario;
	
	private double total;

	@Column(name="fecha_registro")
	private Date fechaRegistro;

	@Column(name="usuario_registro")
	private String usuarioRegistro;

	// bi-directional many-to-one association to PedidoMov
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "id_orden_traspaso")
	private OrdenTraspaso ordenTraspaso;

	// bi-directional many-to-one association to Producto
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "id_producto", nullable = true)
	private Producto producto;
	
	public DetalleOrdenTraspaso() {
		super();
		this.id = 0 ;
		this.cantidadSolicitada = 1;
		this.cantidadEntregada = 0;
		this.setEstado("AC");
		this.total = 0;
		this.precioUnitario = 0;
		this.observacion = "ninguna";
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
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public OrdenTraspaso getOrdenTraspaso() {
		return ordenTraspaso;
	}
	
	public void setOrdenTraspaso(OrdenTraspaso ordenTraspaso) {
		this.ordenTraspaso = ordenTraspaso;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
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

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

}
