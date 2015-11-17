package bo.com.qbit.webapp.model;
// Generated Jul 30, 2014 2:47:16 PM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;

import javax.persistence.*;

/**
 * Class CotizacionServicio
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "cotizacion_servicio", schema = "public")
public class CotizacionServicio implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)	private Integer id;
	private Integer cantidad;
	
	@Column(name="descuento",nullable=true )
	private double descuento;
	
	@Column(name="sub_total",nullable=true )
	private double subTotal; 
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_cotizacion", nullable=false)
	private Cotizacion cotizacion;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_servicio", nullable=false)
	private Servicio servicio;

	public CotizacionServicio() {
		super();
		this.id = 0;
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
			if(!(obj instanceof CotizacionServicio)){
				return false;
			}else{
				if(((CotizacionServicio)obj).id==this.id){
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
	
	public Cotizacion getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(Cotizacion cotizacion) {
		this.cotizacion = cotizacion;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public double getDescuento() {
		return descuento;
	}

	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}

	public double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	
}


