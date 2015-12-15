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
@Table(name = "baja_producto", schema = "public")
public class BajaProducto implements Serializable{

	private static final long serialVersionUID = -5245389763015492273L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String observacion;

	@Column(name = "stock_anterior")
	private double stockAnterior;

	@Column(name = "stock_actual")
	private double stockActual;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_detalle_toma_inventario", nullable = true)
	private DetalleTomaInventario detalleTomaInventario;

	private String estado;
	
	@Column(name = "fecha_registro")
	private Date fechaRegistro;
	
	@Column(name = "usuario_registro")
	private String usuarioRegistro;

	public BajaProducto() {
		this.id = 0 ;	
		this.observacion = "Ninguna";
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

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public double getStockAnterior() {
		return stockAnterior;
	}

	public void setStockAnterior(double stockAnterior) {
		this.stockAnterior = stockAnterior;
	}

	public double getStockActual() {
		return stockActual;
	}

	public void setStockActual(double stockActual) {
		this.stockActual = stockActual;
	}

	public DetalleTomaInventario getDetalleTomaInventario() {
		return detalleTomaInventario;
	}

	public void setDetalleTomaInventario(DetalleTomaInventario detalleTomaInventario) {
		this.detalleTomaInventario = detalleTomaInventario;
	}

}
