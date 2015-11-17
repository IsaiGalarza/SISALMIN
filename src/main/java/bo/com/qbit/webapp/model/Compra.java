package bo.com.qbit.webapp.model;
// Generated Jul 30, 2014 2:47:16 PM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Class Compra
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@Table(name = "compra", schema = "public")
@SuppressWarnings("serial")
public class Compra implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	//campos obligatorios
	private int correlativo;
	
	@Column(name="fecha_factura",nullable=false )
	private Date fechaFactura;
	
	@Column(name="nit_proveedor",nullable=false )
	private String nitProveedor;
	
	@Column(name="razon_social",nullable=false )
	private String razonSocial;
	
	@Column(name="numero_factura",nullable=false )
	private String numeroFactura;
	
	@Column(name="numero_dui",nullable=false )
	private String numeroDUI;
	
	@Column(name="numero_autorizacion",nullable=false )
	private String numeroAutorizacion;
	
	@Column(name="importe_total",nullable=false )
	private double importeTotal;

	//ANTIGUA NORMA 
	@Column(name="importe_ice",nullable=false )
	private double importeICE = 0;
	
	@Column(name="importe_excentos",nullable=false )
	private double importeExcentos = 0;

	//NUEVA NORMA
	@Column(name="importe_no_sujeto_credito_fiscal",nullable=false )
	private double importeNoSujetoCreditoFiscal;
	
	@Column(name="importe_subtotal",nullable=false )
	private double importeSubTotal;
	
	@Column(name="descuentos_bonos_rebajas",nullable=true )
	private double descuentosBonosRebajas;
	
	@Column(name="importe_base_credito_fiscal",nullable=false )
	private double importeBaseCreditoFiscal;
	
	@Column(name="credito_fiscal",nullable=false )
	private double creditoFiscal;
	
	@Column(name="codigo_control",nullable=false )
	private String codigoControl;
	
	@Column(name="tipo_compra",nullable=false )
	private String tipoCompra;

	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_empresa", nullable=false)
	private Empresa empresa;
	
	
	@Size(max = 2) //AC , IN
	private String estado;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	@Column(name="fecha_modificacion",nullable=false )
	private Date fechaModificacion;

	public Compra() {
		super();
		this.id = 0 ;
		this.nitProveedor = "";
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
			if(!(obj instanceof Compra)){
				return false;
			}else{
				if(((Compra)obj).id==this.id){
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

	public String getNitProveedor() {
		return nitProveedor;
	}

	public void setNitProveedor(String nitProveedor) {
		this.nitProveedor = nitProveedor;
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

	public String getNumeroDUI() {
		return numeroDUI;
	}

	public void setNumeroDUI(String numeroDUI) {
		this.numeroDUI = numeroDUI;
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

	public double getImporteNoSujetoCreditoFiscal() {
		return importeNoSujetoCreditoFiscal;
	}

	public void setImporteNoSujetoCreditoFiscal(double importeNoSujetoCreditoFiscal) {
		this.importeNoSujetoCreditoFiscal = importeNoSujetoCreditoFiscal;
	}

	public double getImporteSubTotal() {
		return importeSubTotal;
	}

	public void setImporteSubTotal(double importeSubTotal) {
		this.importeSubTotal = importeSubTotal;
	}

	public double getDescuentosBonosRebajas() {
		return descuentosBonosRebajas;
	}

	public void setDescuentosBonosRebajas(double descuentosBonosRebajas) {
		this.descuentosBonosRebajas = descuentosBonosRebajas;
	}

	public double getImporteBaseCreditoFiscal() {
		return importeBaseCreditoFiscal;
	}

	public void setImporteBaseCreditoFiscal(double importeBaseCreditoFiscal) {
		this.importeBaseCreditoFiscal = importeBaseCreditoFiscal;
	}

	public double getCreditoFiscal() {
		return creditoFiscal;
	}

	public void setCreditoFiscal(double creditoFiscal) {
		this.creditoFiscal = creditoFiscal;
	}

	public String getCodigoControl() {
		return codigoControl;
	}

	public void setCodigoControl(String codigoControl) {
		this.codigoControl = codigoControl;
	}

	public String getTipoCompra() {
		return tipoCompra;
	}

	public void setTipoCompra(String tipoCompra) {
		this.tipoCompra = tipoCompra;
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

}


