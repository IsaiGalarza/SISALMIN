package bo.com.qbit.webapp.model;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;

import bo.com.qbit.webapp.data.AlmacenProductoRepository;
import bo.com.qbit.webapp.data.KardexProductoRepository;
import bo.com.qbit.webapp.service.AlmacenProductoRegistration;
import bo.com.qbit.webapp.service.DetalleProductoRegistration;
import bo.com.qbit.webapp.service.KardexProductoRegistration;

/**
 * @Pattern Fachada
 * @author mauriciobejaranorivera
 *
 */
@Stateless
public class FachadaOrdenIngreso implements Serializable{

	private static final long serialVersionUID = 4222532906084076924L;

	private @Inject AlmacenProductoRepository almacenProductoRepository;
	private @Inject KardexProductoRepository kardexProductoRepository;

	private @Inject DetalleProductoRegistration detalleProductoRegistration;
	private @Inject AlmacenProductoRegistration almacenProductoRegistration;
	private @Inject KardexProductoRegistration kardexProductoRegistration;
	
	public void actualizarStockExistente(Almacen almacen,Producto prod ,double newStock)  {
		try{
			//0 . verificar si existe el producto en el almacen
			AlmacenProducto almProd =  almacenProductoRepository.findByAlmacenProducto(almacen,prod);
			System.out.println("aqui ");
			if(almProd != null){
				// 1 .  si existe el producto
				double oldStock = almProd.getStock();
				almProd.setStock(oldStock + newStock);
				almacenProductoRegistration.updated(almProd);
			}
			
		}catch(Exception e){

			System.out.println("ERROR actualizarStock() "+e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * registro en la tabla almacen_producto, actualiza el stock y el precio(promedio agrupando los productos)
	 * @param almacen
	 * @param proveedor
	 * @param prod
	 * @param newStock
	 * @param date
	 * @param precioUnitario
	 * @param usuarioSession
	 * @throws Exception
	 */
	public void actualizarStock(Almacen almacen,Proveedor proveedor,Producto prod ,double newStock,Date date,double precioUnitario,String usuarioSession)  {
		try{
			//0 . verificar si existe el producto en el almacen
			System.out.println("actualizarStock() "+almacen.getId());
			System.out.println("actualizarStock() "+proveedor.getId());
			System.out.println("actualizarStock() "+prod.getId());

			AlmacenProducto almProd =  almacenProductoRepository.findByAlmacenProducto(almacen,prod);
			System.out.println("aqui ");
			if(almProd != null){
				// 1 .  si existe el producto
				double oldStock = almProd.getStock();
				double oldPrecioUnitario = almProd.getPrecioUnitario();
				almProd.setStock(oldStock + newStock);
				almProd.setPrecioUnitario((( oldPrecioUnitario + precioUnitario)/2));//precio ponderado del producto
				almacenProductoRegistration.updated(almProd);
				return ;
			}
			// 2 . no existe el producto
			almProd = new AlmacenProducto();
			almProd.setAlmacen(almacen);
			almProd.setProducto(prod);
			almProd.setProveedor(proveedor);
			almProd.setStock(newStock);
			almProd.setPrecioUnitario(precioUnitario);
			almProd.setEstado("AC");
			almProd.setFechaRegistro(date);
			almProd.setUsuarioRegistro(usuarioSession);

			almacenProductoRegistration.register(almProd);
		}catch(Exception e){

			System.out.println("ERROR actualizarStock() "+e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * registro en la tabla kardex_producto
	 * @param gestionSesion
	 * @param newOrdenIngreso
	 * @param prod
	 * @param fechaActual
	 * @param cantidad
	 * @param precioUnitario
	 * @param usuarioSession
	 * @throws Exception
	 */
	public void actualizarKardexProducto(String correlativo,Almacen almacen,Gestion gestionSesion,Producto prod,Date fechaActual,double cantidad,Double precioUnitario,String usuarioSession) {
		try{
			//registrar Kardex
			KardexProducto kardexProductoAnt = kardexProductoRepository.findKardexStockAnteriorByProducto(prod);
			double stockAnterior = 0;
			if(kardexProductoAnt != null){
				//se obtiene el saldo anterior del producto
				stockAnterior = kardexProductoAnt.getStockAnterior();
			}
			double entrada = cantidad;
			double salida = 0;
			double saldo = stockAnterior + cantidad;

			KardexProducto kardexProducto = new KardexProducto();
			kardexProducto.setUnidadSolicitante("ORDEN INGRESO");
			kardexProducto.setFecha(fechaActual);
			kardexProducto.setAlmacen(almacen);
			kardexProducto.setCantidad(cantidad);
			kardexProducto.setEstado("AC");
			kardexProducto.setFechaRegistro(fechaActual);
			kardexProducto.setGestion(gestionSesion);
			kardexProducto.setNumeroTransaccion(correlativo);


			//BOLIVIANOS
			kardexProducto.setPrecioUnitario(precioUnitario);
			kardexProducto.setTotalEntrada(precioUnitario * entrada);
			kardexProducto.setTotalSalida(precioUnitario * salida);
			kardexProducto.setTotalSaldo(precioUnitario * saldo);

			//CANTIDADES
			kardexProducto.setStock(entrada);//ENTRADA
			kardexProducto.setStockActual(salida);//SALIDA
			kardexProducto.setStockAnterior(saldo);//SALDO

			kardexProducto.setProducto(prod);

			kardexProducto.setTipoMovimiento("ORDEN INGRESO");
			kardexProducto.setUsuarioRegistro(usuarioSession);
			kardexProductoRegistration.register(kardexProducto);
		}catch(Exception e){
			System.out.println("ERROR actualizarKardexProducto() "+e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * cargar en la ttabla detalle_producto, reegistros de productos, para luego utilizar el metodo PEPS
	 * @param fechaActual
	 * @param almacen
	 * @param producto
	 * @param cantidad
	 * @param precio
	 * @param fecha
	 * @param correlativoTransaccion
	 * @param usuarioSession
	 * @throws Exception
	 */
	public void cargarDetalleProducto(Date fechaActual,Almacen almacen,Producto producto,double cantidad, double precio, Date fecha, String correlativoTransaccion,String usuarioSession ) {
		try{
		DetalleProducto detalleProducto = new DetalleProducto();
		detalleProducto.setCodigo("OI"+correlativoTransaccion+fecha.toString());
		detalleProducto.setAlmacen(almacen);
		detalleProducto.setEstado("AC");
		detalleProducto.setPrecio(precio);
		detalleProducto.setStockActual(cantidad);
		detalleProducto.setStockInicial(cantidad);
		detalleProducto.setCorrelativoTransaccion(correlativoTransaccion);
		detalleProducto.setFecha(fecha);
		detalleProducto.setFechaRegistro(fechaActual);
		detalleProducto.setProducto(producto);
		detalleProducto.setUsuarioRegistro(usuarioSession);
		detalleProductoRegistration.register(detalleProducto);
		}catch(Exception e){
			System.out.println("ERROR cargarDetalleProducto() "+e.getMessage());
			e.printStackTrace();
		}
	}

}
