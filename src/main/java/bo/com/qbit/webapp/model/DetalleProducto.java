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
import javax.validation.constraints.Size;

/**
 * 
 * @author mauriciobejaranorivera
 * 
 * @Descripcion: Al registrar una orden ingreso,el detalle de los productos con sus cantidades , respectivas fechas y 
 * el correlativo de la orden de ingreso , se registran aqui, ya que luego es utilizada para saber que productos van a salir primero
 * cuando se proceda una orden de salida o traspaso, es decir para aplicar el metodo PEPS
 *
 */

@Entity
@Table(name = "detalle_producto", schema = "public")
public class DetalleProducto {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_producto", nullable=false)
	private Producto producto;
	
	private double stockInicial;// se registra la primera vez
	
	private double stockActual;//este campo se actualiza
	
	private double precio;
	
	private Date fecha;
	
	@Column(name="correlativo_transaccion")
	private String correlativoTransaccion;
	
	@Column(name="codigo", nullable=false)
	private String codigo;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_almacen", nullable=false)
	private Almacen almacen;
	
	@Size(max = 2) //AC , IN
	private String estado;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	@Column(name="fecha_modificacion",nullable=true )
	private Date fechaModificacion;
	
	@Column(name="usuario_registro",nullable=false )
	private String usuarioRegistro;
	
	public DetalleProducto(){
		super();
		this.id = 0;
		almacen = new Almacen();
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
			if(!(obj instanceof DetalleProducto)){
				return false;
			}else{
				if(((DetalleProducto)obj).id==this.id){
					return true;
				}else{
					return false;
				}
			}
		}
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}


	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getCorrelativoTransaccion() {
		return correlativoTransaccion;
	}

	public void setCorrelativoTransaccion(String correlativoTransaccion) {
		this.correlativoTransaccion = correlativoTransaccion;
	}

	public double getStockInicial() {
		return stockInicial;
	}

	public void setStockInicial(double stockInicial) {
		this.stockInicial = stockInicial;
	}

	public double getStockActual() {
		return stockActual;
	}

	public void setStockActual(double stockActual) {
		this.stockActual = stockActual;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Almacen getAlmacen() {
		return almacen;
	}

	public void setAlmacen(Almacen almacen) {
		this.almacen = almacen;
	}

}
