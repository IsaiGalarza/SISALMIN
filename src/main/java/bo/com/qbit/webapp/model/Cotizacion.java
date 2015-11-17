package bo.com.qbit.webapp.model;
// Generated Jul 30, 2014 2:47:16 PM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Class Cotizacion
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "cotizacion", schema = "public")
public class Cotizacion implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private Integer numero;
	private Date fecha;
	
	@Column(name="fecha_vencimiento",nullable=true )
	private Date fechaVencimiento;
	
	private double total;
	private String observacion;
	private String correlativo;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_moneda_empresa",nullable=true )
	private MonedaEmpresa monedaEmpresa;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_empresa",nullable=true )
	private Empresa empresa; //empresa que realizo la cotizacion
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="id_cliente", nullable=false)
	private Cliente cliente;
	
	@Size(max = 2) //AC , IN
	private String estado;
	
	@Column(name="usuario_registro",nullable=false )
	private String usuarioRegistro;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	@Column(name="fecha_modificacion",nullable=true )
	private Date fechaModificacion;

	public Cotizacion() {
		super();
		this.id = 0;
	}
	
	@Override
	public String toString() {
		return String.valueOf(numero );
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
			if(!(obj instanceof Cotizacion)){
				return false;
			}else{
				if(((Cotizacion)obj).id==this.id){
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

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public MonedaEmpresa getMonedaEmpresa() {
		return monedaEmpresa;
	}

	public void setMonedaEmpresa(MonedaEmpresa monedaEmpresa) {
		this.monedaEmpresa = monedaEmpresa;
	}

	public String getCorrelativo() {
		return correlativo;
	}

	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
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


