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
	//Test
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String correlativo;
	
	@Column(name = "numero_pedido_materiales", nullable = false)
	private String numeroPedidoMateriales;
	
	@Column(name = "fecha_pedido", nullable = false)
	private Date fechaPedido;

	@Column(name = "fecha_aprobacion", nullable = true)
	private Date fechaAprobacion;
	
	@Column(name = "total_importe")
	private double totalImporte;
	
	private String estado;
	
	@Column(name = "fecha_registro")
	private Date fechaRegistro;
	
	@Column(name = "usuario_registro")
	private String usuarioRegistro;
	//length=225
	@Column(name = "observacion", nullable = true)
	private String observacion;

	// bi-directional many-to-one association 
	@OneToMany(mappedBy = "ordenSalida")
	private List<DetalleOrdenSalida> detalleOrdenSalida;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_gestion", nullable = true)
	private Gestion gestion;

	@ManyToOne
	@JoinColumn(name = "id_almacen")
	private Almacen almacen;
	
	@ManyToOne
	@JoinColumn(name = "id_detalle_unidad") //insert="false" update="false"
	private DetalleUnidad unidadSolicitante;
	
	@ManyToOne
	@JoinColumn(name = "id_funcionario")
	private Funcionario funcionario;

	@ManyToOne
	@JoinColumn(name = "id_proyecto")
	private Proyecto proyecto;

	public OrdenSalida() {
		this.id = 0 ;
		this.unidadSolicitante = new DetalleUnidad();
		this.funcionario = new Funcionario();
		this.setProyecto(new Proyecto());
		this.almacen = new Almacen();
		this.numeroPedidoMateriales = "";
		this.observacion = "Ninguna";
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCorrelativo() {
		return correlativo;
	}

	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}
	
	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public Gestion getGestion() {
		return gestion;
	}

	public void setGestion(Gestion gestion) {
		this.gestion = gestion;
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
	
	public List<DetalleOrdenSalida> getDetalleOrdenSalida() {
		return detalleOrdenSalida;
	}

	public void setDetalleOrdenSalida(List<DetalleOrdenSalida> detalleOrdenSalida) {
		this.detalleOrdenSalida = detalleOrdenSalida;
	}
	
	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Date getFechaPedido() {
		return fechaPedido;
	}

	public void setFechaPedido(Date fechaPedido) {
		this.fechaPedido = fechaPedido;
	}

	public Proyecto getProyecto() {
		return proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

	public String getNumeroPedidoMateriales() {
		return numeroPedidoMateriales;
	}

	public void setNumeroPedidoMateriales(String numeroPedidoMateriales) {
		this.numeroPedidoMateriales = numeroPedidoMateriales;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

}
