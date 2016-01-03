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
@Table(name = "detalle_ti_oi", schema = "public")
public class DetalleTomaInventarioOrdenIngreso implements Serializable{

	private static final long serialVersionUID = 6101168071340551453L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String estado;

	@Column(name="fecha_registro")
	private Date fechaRegistro;

	@Column(name="usuario_registro")
	private String usuarioRegistro;

	// bi-directional many-to-one association to PedidoMov
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_orden_ingreso")
	private OrdenIngreso ordenIngreso;

	// bi-directional many-to-one association to Producto
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_toma_inventario", nullable = true)
	private TomaInventario tomaInventario;
	
	public DetalleTomaInventarioOrdenIngreso() {
		super();
		this.id = 0 ;
		this.setEstado("AC");
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
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public OrdenIngreso getOrdenIngreso() {
		return ordenIngreso;
	}
	
	public void setOrdenIngreso(OrdenIngreso ordenIngreso) {
		this.ordenIngreso = ordenIngreso;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public TomaInventario getTomaInventario() {
		return tomaInventario;
	}

	public void setTomaInventario(TomaInventario tomaInventario) {
		this.tomaInventario = tomaInventario;
	}

}
