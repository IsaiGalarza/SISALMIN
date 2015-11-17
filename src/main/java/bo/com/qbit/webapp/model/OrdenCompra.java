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
import javax.validation.constraints.Size;

@Entity
@SuppressWarnings("serial")
@Table(name = "orden_compra", catalog = "public")
public class OrdenCompra implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String concepto;
	
	private Integer cuotas;
	
	@Column(name="dias_credito",nullable=true )
	private  Integer diasCredito ;
	
	@Column(name="interes",nullable=false )
	private  double interes ;
	
	@Column(name="permitir_credito",nullable=true )
	private  String permitirCredito;
	
	@Column(name="tipo_cambio",nullable=true )
	private  double tipoCambio ;
	
	private  double total;
	
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_empresa", nullable=false)
	private  Empresa empresa;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_proveedor", nullable=false)
	private  Proveedor proveedor;
	private  String tipo ;
	
	@Size(max = 2) //AC , IN
	private  String estado;
	
	@Column(name="fecha_registro",nullable=false )
	private  Date fechaRegistro;
	
	@Column(name="usuario_registro",nullable=false )
	private  String usuarioRegistro;
	
	
	public OrdenCompra(){
		super();
		this.id = 0;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public Integer getCuotas() {
		return cuotas;
	}
	public void setCuotas(Integer cuotas) {
		this.cuotas = cuotas;
	}
	public Integer getDiasCredito() {
		return diasCredito;
	}
	public void setDiasCredito(Integer diasCredito) {
		this.diasCredito = diasCredito;
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
	public double getInteres() {
		return interes;
	}
	public void setInteres(double interes) {
		this.interes = interes;
	}
	public String getPermitirCredito() {
		return permitirCredito;
	}
	public void setPermitirCredito(String permitirCredito) {
		this.permitirCredito = permitirCredito;
	}
	public double getTipoCambio() {
		return tipoCambio;
	}
	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
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
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public Proveedor getProveedor() {
		return proveedor;
	}
	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}