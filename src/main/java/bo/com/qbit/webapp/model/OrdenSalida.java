package bo.com.qbit.webapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "orden_salida", schema = "public")
public class OrdenSalida implements Serializable{

	private static final long serialVersionUID = -5245389763015492273L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Integer correlativo;
	
	@Column(name = "servidor_publico_solicitante", nullable = false)
	private String servidorPublicoSolicitantes;
	
	@Column(name = "cargo", nullable = false)
	private String cargo;
	
	@Column(name = "numero_pedido_materiales", nullable = false)
	private String numeroPedidoMateriales;
	
	@Column(name = "fecha_salida", nullable = false)
	private Date fechaSalida;
	
	@Column(name = "fecha_entrega", nullable = false)
	private Date fechaEntrega;

	private String estado;
	
	@Column(name = "fecha_registro")
	private Date fechaRegistro;
	
	@Column(name = "total_importe")
	private double totalImporte;
	
	@Column(name = "usuario_registro")
	private String usuarioRegistro;

	@Column(name = "fecha_aprobacion")
	private Date fechaAprobacion;

	// bi-directional many-to-one association 
	@OneToMany(mappedBy = "ordenSalida")
	private List<DetalleOrdenSalida> detalleOrdenSalida;

	@ManyToOne
	@JoinColumn(name = "id_almacen")
	private Almacen almacen;
	
	@ManyToOne
	@JoinColumn(name = "id_unidad")
	private DetalleUnidad unidadSolicitante;
	
	@ManyToOne
	@JoinColumn(name = "id_unidad")
	private Proyecto proyecto;
	
	@Column(name = "detalle_proyecto_actividad")
	private String detalleProyectoActividad;

	public OrdenSalida() {
		this.id = 0 ;
		this.almacen = new Almacen();		
		this.proyecto = new Proyecto();
	}

	public Integer getId() {
		return this.id;
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

	public Date getFechaAprobacion() {
		return fechaAprobacion;
	}

	public void setFechaAprobacion(Date fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}
	
	public double getTotalImporte() {
		return totalImporte;
	}

	public void setTotalImporte(double totalImporte) {
		this.totalImporte = totalImporte;
	}

	public Almacen getAlmacen() {
		return almacen;
	}

	public void setAlmacen(Almacen almacen) {
		this.almacen = almacen;
	}

	public DetalleUnidad getUnidadSolicitante() {
		return unidadSolicitante;
	}

	public void setUnidadSolicitante(DetalleUnidad unidadSolicitante) {
		this.unidadSolicitante = unidadSolicitante;
	}

	public String getServidorPublicoSolicitantes() {
		return servidorPublicoSolicitantes;
	}

	public void setServidorPublicoSolicitantes(
			String servidorPublicoSolicitantes) {
		this.servidorPublicoSolicitantes = servidorPublicoSolicitantes;
	}

	public String getDetalleProyectoActividad() {
		return detalleProyectoActividad;
	}

	public void setDetalleProyectoActividad(String detalleProyectoActividad) {
		this.detalleProyectoActividad = detalleProyectoActividad;
	}
	
	public List<DetalleOrdenSalida> getDetalleOrdenSalida() {
		return detalleOrdenSalida;
	}

	public void setDetalleOrdenSalida(List<DetalleOrdenSalida> detalleOrdenSalida) {
		this.detalleOrdenSalida = detalleOrdenSalida;
	}

}
