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
@Table(name = "detalle_orden_ingreso", schema = "public")
public class DetalleOrdenIngreso implements Serializable{

	private static final long serialVersionUID = 6101168071340551453L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private double cantidad;

	private String estado;
	
	private String observacion;
	
	private double total;
	
	@Column(name="precio_unitario", nullable = true)
	private double precioUnitario;

	@Column(name="fecha_registro")
	private Date fechaRegistro;

	@Column(name="usuario_registro")
	private String usuarioRegistro;

	// bi-directional many-to-one association to PedidoMov
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "id_orden_ingreso")
	private OrdenIngreso ordenIngreso;

	// bi-directional many-to-one association to Producto
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "id_producto", nullable = true)
	private Producto producto;
	
	public DetalleOrdenIngreso() {
		super();
		this.id = 0 ;
		this.cantidad = 1;
		this.setEstado("AC");
		this.precioUnitario = 0;
		this.observacion = "ninguna";
	}
	
	public double getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
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
	
	public OrdenIngreso getOrdenIngreso() {
		return ordenIngreso;
	}
	
	public void setOrdenIngreso(OrdenIngreso ordenIngreso) {
		this.ordenIngreso = ordenIngreso;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

}
