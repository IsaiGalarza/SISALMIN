package bo.com.qbit.webapp.util;

public class EDCotizacion {
	
	private int id;
	private String codigo;
	private String descripcion;
	private int cantidad;
	private double precioUnitario;
	private double importe;
	private String estado;
	private double descuento;

	public EDCotizacion(){
		super();	
	}

	public EDCotizacion(int id, String codigo, String descripcion,
			int cantidad, double precioUnitario, double importe, String estado,double descuento) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
		this.importe = importe;
		this.estado = estado;
		this.descuento = descuento;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public double getDescuento() {
		return descuento;
	}

	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}

	@Override
	public String toString() {
		return "EDCotizacion [id=" + id + ", codigo=" + codigo
				+ ", descripcion=" + descripcion + ", cantidad=" + cantidad
				+ ", precioUnitario=" + precioUnitario + ", importe=" + importe
				+ ", estado=" + estado + ", descuento=" + descuento + "]";
	}

		

}
