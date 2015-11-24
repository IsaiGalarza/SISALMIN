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
@Table(name = "orden_traspaso", schema = "public")
public class OrdenTraspaso implements Serializable{

	private static final long serialVersionUID = -5245389763015492273L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String correlativo;

	private String observacion;

	private String cargo;

	@Column(name = "fecha_documento", nullable = true)
	private Date fechaDocumento;

	@Column(name = "detalle_proyecto")
	private String detalleProyecto;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_funcionario", nullable = true)
	private Funcionario funcionario;
	

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "id_proyecto", nullable = true)
	private Proyecto proyecto;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_gestion", nullable = true)
	private Gestion gestion;

	private String estado;

	@Column(name = "fecha_registro")
	private Date fechaRegistro;

	@Column(name = "total_importe")
	private double totalImporte;

	@Column(name = "usuario_registro")
	private String usuarioRegistro;

	@Column(name = "fecha_aprobacion")
	private Date fechaAprobacion;

	// bi-directional many-to-one association to DetallePedidoMov
	@OneToMany(mappedBy = "ordenTraspaso")
	private List<DetalleOrdenTraspaso> detalleOrdenTraspaso;

	@ManyToOne
	@JoinColumn(name = "id_usuario_aprobacion")
	private Usuario usuarioAprobacion;	

	@ManyToOne
	@JoinColumn(name = "id_almacen_origen")
	private Almacen almacenOrigen;

	@ManyToOne
	@JoinColumn(name = "id_almacen_destino")
	private Almacen almacenDestino;

	public OrdenTraspaso() {
		this.id = 0 ;
		this.almacenOrigen = new Almacen();		
		this.almacenDestino = new Almacen();	
		this.proyecto =new Proyecto();
		this.setGestion(new Gestion());
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

	public Usuario getUsuarioAprobacion() {
		return usuarioAprobacion;
	}

	public void setUsuarioAprobacion(Usuario usuarioAprobacion) {
		this.usuarioAprobacion = usuarioAprobacion;
	}

	public Date getFechaAprobacion() {
		return fechaAprobacion;
	}

	public void setFechaAprobacion(Date fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}

	public Proyecto getProyecto() {
		return proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}
	
	public double getTotalImporte() {
		return totalImporte;
	}

	public void setTotalImporte(double totalImporte) {
		this.totalImporte = totalImporte;
	}

	public List<DetalleOrdenTraspaso> getDetalleOrdenTraspaso() {
		return detalleOrdenTraspaso;
	}

	public void setDetalleOrdenTraspaso(List<DetalleOrdenTraspaso> detalleOrdenTraspaso) {
		this.detalleOrdenTraspaso = detalleOrdenTraspaso;
	}

	public Date getFechaDocumento() {
		return fechaDocumento;
	}

	public void setFechaDocumento(Date fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}

	public Gestion getGestion() {
		return gestion;
	}

	public void setGestion(Gestion gestion) {
		this.gestion = gestion;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public Almacen getAlmacenOrigen() {
		return almacenOrigen;
	}

	public void setAlmacenOrigen(Almacen almacenOrigen) {
		this.almacenOrigen = almacenOrigen;
	}

	public Almacen getAlmacenDestino() {
		return almacenDestino;
	}

	public void setAlmacenDestino(Almacen almacenDestino) {
		this.almacenDestino = almacenDestino;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public String getDetalleProyecto() {
		return detalleProyecto;
	}

	public void setDetalleProyecto(String detalleProyecto) {
		this.detalleProyecto = detalleProyecto;
	}

}
