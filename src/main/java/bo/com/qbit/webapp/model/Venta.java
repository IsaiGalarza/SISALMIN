package bo.com.qbit.webapp.model;
// Generated Jul 30, 2014 2:47:16 PM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Class Venta
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@Table(name = "venta", schema = "public")
@SuppressWarnings("serial")
public class Venta implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	//campos obligatorios
	private int correlativo;
	
	@Column(name="nit_ci",nullable=false )
	private String nitCi;//
	
	@Column(name="razon_social",nullable=false )
	private String razonSocial;//
	
	@Column(name="numero_factura",nullable=false )
	private String numeroFactura;//
	
	@Column(name="numero_autorizacion",nullable=false )
	private String numeroAutorizacion;//
	
	@Column(name="fecha_factura",nullable=false )
	private Date fechaFactura;
	
	@Column(name="importe_total",nullable=false )
	private double importeTotal;

	//ANTIGUA NORMA 
	@Column(name="importe_ice",nullable=false )
	private double importeICE = 0;
	
	@Column(name="importe_excentos",nullable=false )
	private double importeExcentos = 0;

	//NUEVA NORMA
	@Column(name="importe_sujeto_a_debito_fiscal",nullable=false )
	private double importeSujetoADebitoFiscal;

	@Column(name="debito_fiscal",nullable=false )
	private double debitoFiscal;
	
	@Size(max = 1) //V, A N, E
	@Column(name="estado_factura",nullable=false )
	private String estadoFactura;
	
	@Column(name="codigo_control",nullable=false )
	private String codigoControl;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="id_empresa", nullable=false)
	private Empresa empresa;
		
	@Size(max = 2) //AC , IN
	private String estado;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	@Column(name="fecha_modificacion",nullable=true )
	private Date fechaModificacion;
	
	@Column(name="usuario_registro",nullable=false )
	private String usuarioRegistro;

	public Venta() {
		super();
		this.id = 0 ;
	}

	@Override
	public String toString() {
		return String.valueOf(id);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}else{
			if(!(obj instanceof Venta)){
				return false;
			}else{
				if(((Venta)obj).id==this.id){
					return true;
				}else{
					return false;
				}
			}
		}
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public int getCorrelativo() {
		return correlativo;
	}

	public void setCorrelativo(int correlativo) {
		this.correlativo = correlativo;
	}

	public Date getFechaFactura() {
		return fechaFactura;
	}

	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	
	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getNumeroFactura() {
		return numeroFactura;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}
	
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

	public double getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(double importeTotal) {
		this.importeTotal = importeTotal;
	}

	public double getImporteICE() {
		return importeICE;
	}

	public void setImporteICE(double importeICE) {
		this.importeICE = importeICE;
	}

	public double getImporteExcentos() {
		return importeExcentos;
	}

	public void setImporteExcentos(double importeExcentos) {
		this.importeExcentos = importeExcentos;
	}

	public String getCodigoControl() {
		return codigoControl;
	}

	public void setCodigoControl(String codigoControl) {
		this.codigoControl = codigoControl;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getNitCi() {
		return nitCi;
	}

	public void setNitCi(String nitCi) {
		this.nitCi = nitCi;
	}

	public double getImporteSujetoADebitoFiscal() {
		return importeSujetoADebitoFiscal;
	}

	public void setImporteSujetoADebitoFiscal(double importeSujetoADebitoFiscal) {
		this.importeSujetoADebitoFiscal = importeSujetoADebitoFiscal;
	}

	public double getDebitoFiscal() {
		return debitoFiscal;
	}

	public void setDebitoFiscal(double debitoFiscal) {
		this.debitoFiscal = debitoFiscal;
	}

	public String getEstadoFactura() {
		return estadoFactura;
	}

	public void setEstadoFactura(String estadoFactura) {
		this.estadoFactura = estadoFactura;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}
	
}


