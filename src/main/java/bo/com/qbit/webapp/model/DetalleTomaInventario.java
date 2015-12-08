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
@Table(name = "detalle_toma_inventario", schema = "public")
public class DetalleTomaInventario implements Serializable{

	private static final long serialVersionUID = 6101168071340551453L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private int cantidad;

	private String estado;
	
	@Column(name="observacion",nullable=true)
	private String observacion;
	
	@Column(name="cantidad_registrada")
	private double cantidadRegistrada;
	
	@Column(name="cantidad_verificada",nullable=true)
	private double cantidadVerificada;
	
	@Column(name="diferencia",nullable=true)
	private double diferencia;

	@Column(name="fecha_registro")
	private Date fechaRegistro;

	@Column(name="usuario_registro")
	private String usuarioRegistro;

	// bi-directional many-to-one association to PedidoMov
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "id_toma_inventario")
	private TomaInventario tomaInventario;

	// bi-directional many-to-one association to Producto
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "id_producto", nullable = true)
	private Producto producto;
	
	public DetalleTomaInventario() {
		super();
		this.id = 0 ;
		this.cantidad = 0;
		this.setEstado("AC");
		
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
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

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public double getCantidadRegistrada() {
		return cantidadRegistrada;
	}

	public void setCantidadRegistrada(double cantidadRegistrada) {
		this.cantidadRegistrada = cantidadRegistrada;
	}

	public double getCantidadVerificada() {
		return cantidadVerificada;
	}

	public void setCantidadVerificada(double cantidadVerificada) {
		this.cantidadVerificada = cantidadVerificada;
	}

	public double getDiferencia() {
		return diferencia;
	}

	public void setDiferencia(double diferencia) {
		this.diferencia = diferencia;
	}

	public TomaInventario getTomaInventario() {
		return tomaInventario;
	}

	public void setTomaInventario(TomaInventario tomaInventario) {
		this.tomaInventario = tomaInventario;
	}

}
