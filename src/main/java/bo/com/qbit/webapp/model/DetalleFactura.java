package bo.com.qbit.webapp.model;


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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 * Class DetalleFactura
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="detalle_factura",schema="public")
public class DetalleFactura  implements java.io.Serializable {

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", unique=true, nullable=false)
	private Integer id;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_factura", nullable=false)
	private Factura factura;
	
	@Column(name="codigo_producto", length=20)
	private String codigoProducto;
	
	@Column(name="concepto", nullable=false, length=200)
	private String concepto;
	
	@Column(name="cantidad", nullable=false)
	private double cantidad;
	
	@Column(name="precio_unitario", nullable=false, precision=8, scale=8)
	private double precioUnitario;
	
	@Column(name="precio_total", nullable=false, precision=8, scale=8)
	private double precioTotal;
	
	@Column(name="descuentos", precision=8, scale=8)
	private double descuentos;
	
	@Size(max = 2) //AC , IN
	private String estado;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_registro", length=29)
	private Date fechaRegistro;
	
	@Column(name="usuario_registro", length=30)
	private String usuarioRegistro;
	
	private String origen;

	public DetalleFactura() {
		super();
		this.id = 0;
	}

	public DetalleFactura(Integer id, Factura factura, String concepto, double cantidad, double precioUnitario, double precioTotal, String origen) {
		this.id = id;
		this.factura = factura;
		this.concepto = concepto;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
		this.precioTotal = precioTotal;
		this.origen = origen;
	}

	public DetalleFactura(Integer id, Factura factura, String codigoProducto, String concepto, double cantidad, double precioUnitario, double precioTotal, double descuentos, String estado, Date fechaRegistro, String usuarioRegistro, String origen) {
		this.id = id;
		this.factura = factura;
		this.codigoProducto = codigoProducto;
		this.concepto = concepto;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
		this.precioTotal = precioTotal;
		this.descuentos = descuentos;
		this.estado = estado;
		this.fechaRegistro = fechaRegistro;
		this.usuarioRegistro = usuarioRegistro;
		this.origen = origen;
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
			if(!(obj instanceof DetalleFactura)){
				return false;
			}else{
				if(((DetalleFactura)obj).id==this.id){
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

	public Factura getFactura() {
		return this.factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public String getCodigoProducto() {
		return this.codigoProducto;
	}

	public void setCodigoProducto(String codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	public String getConcepto() {
		return this.concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public double getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecioUnitario() {
		precioUnitario = round(precioUnitario, 2);
		return this.precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public double getPrecioTotal() {
		precioTotal = round(precioTotal, 2);
		return this.precioTotal;
	}

	public void setPrecioTotal(double precioTotal) {
		this.precioTotal = precioTotal;
	}

	public double getDescuentos() {
		return this.descuentos;
	}

	public void setDescuentos(double descuentos) {
		this.descuentos = descuentos;
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

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

}


