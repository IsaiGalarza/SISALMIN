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
@Table(name = "orden_ingreso", schema = "public")
public class OrdenIngreso implements Serializable{

	private static final long serialVersionUID = -5245389763015492273L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String correlativo;
	
	private String observacion;
	
	private String motivoIngreso;
	
	@Column(name = "fecha_documento", nullable = true)
	private Date fechaDocumento;

	@Column(name = "tipo_documento")
	private String tipoDocumento;

	@Column(name = "numero_documento")
	private String numeroDocumento;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "id_proveedor", nullable = true)
	private Proveedor proveedor;
	
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
	@OneToMany(mappedBy = "ordenIngreso")
	private List<DetalleOrdenIngreso> detalleOrdenIngreso;

	@ManyToOne
	@JoinColumn(name = "id_usuario_aprobacion")
	private Usuario usuarioAprobacion;	

	@ManyToOne
	@JoinColumn(name = "id_almacen")
	private Almacen almacen;

	public OrdenIngreso() {
		this.id = 0 ;
		this.tipoDocumento = "FACTURA";
		this.motivoIngreso = "COMPRA";
		this.almacen = new Almacen();		
		this.proveedor=new Proveedor();
		this.gestion = new Gestion();
		this.observacion = "Ninguna";
		this.numeroDocumento = "";
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
	
	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor idProveedor) {
		this.proveedor = idProveedor;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
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

	public List<DetalleOrdenIngreso> getDetalleOrdenIngreso() {
		return detalleOrdenIngreso;
	}

	public void setDetalleOrdenIngreso(List<DetalleOrdenIngreso> detalleOrdenIngreso) {
		this.detalleOrdenIngreso = detalleOrdenIngreso;
	}

	public String getMotivoIngreso() {
		return motivoIngreso;
	}

	public void setMotivoIngreso(String motivoIngreso) {
		this.motivoIngreso = motivoIngreso;
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

}
