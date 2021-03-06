package bo.com.qbit.webapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import bo.com.qbit.webapp.data.AlmacenProductoRepository;
import bo.com.qbit.webapp.data.DetalleProductoRepository;
import bo.com.qbit.webapp.data.KardexProductoRepository;
import bo.com.qbit.webapp.service.AlmacenProductoRegistration;
import bo.com.qbit.webapp.service.DetalleOrdenSalidaRegistration;
import bo.com.qbit.webapp.service.DetalleProductoRegistration;
import bo.com.qbit.webapp.service.KardexProductoRegistration;

/**
 * @Pattern Facade
 * @author mauriciobejaranorivera
 *
 */
@Stateless
public class FachadaOrdenSalida implements Serializable {

	private static final long serialVersionUID = 3930915805505213300L;

	private @Inject DetalleProductoRepository detalleProductoRepository;
	private @Inject DetalleOrdenSalidaRegistration detalleOrdenSalidaRegistration;
	private @Inject AlmacenProductoRepository almacenProductoRepository;
	private @Inject KardexProductoRepository kardexProductoRepository;
	private @Inject DetalleProductoRegistration detalleProductoRegistration;
	private @Inject AlmacenProductoRegistration almacenProductoRegistration;
	private @Inject KardexProductoRegistration kardexProductoRegistration;

	public void actualizarDetalleProducto(Gestion gestion, Almacen almacen,Producto producto,double cantidadSolicitada){
		try{
			double precioPonderado = 0;
			int cantidadPrecios = 0;
			//obtener todos los detalles del producto, para poder descontar stock de acuerdo a la cantidad solicitada
			List<DetalleProducto> listDetalleProducto = detalleProductoRepository.findAllByProductoAndAlmacenOrderByFecha(almacen,producto,gestion);
			if(listDetalleProducto.size()>0){
				for(DetalleProducto d : listDetalleProducto){
					double stockActual = d.getStockActual();
					if(cantidadSolicitada > 0){// si la  cantidad Solicitada lo obtiene
						cantidadPrecios = cantidadPrecios + 1;//1
						precioPonderado = precioPonderado + d.getPrecio();
						double stockFinal = stockActual- cantidadSolicitada; 
						double cantidadRestada = stockFinal < 0 ? cantidadSolicitada - stockActual : cantidadSolicitada;
						d.setStockActual( stockFinal <= 0 ? 0 : stockFinal);
						d.setEstado(stockFinal<=0?"IN":"AC");
						detalleProductoRegistration.updated(d);
						cantidadSolicitada = cantidadSolicitada - cantidadRestada  ;//actualizar cantidad solicitada
					}
				}

			}
		}catch(Exception e){
			System.out.println("actualizarDetalleProducto() ERROR: "+e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Actualiza el stock, verifica existencias de acuerdo al metodo PEPS
	 * @param almacen De que almacen se sacara los productos
	 * @param detalle
	 * @return true si hay stock, false si no hay existencias
	 */
	public boolean actualizarDetalleProductoByOrdenSalida(Gestion gestion,Almacen almacen,DetalleOrdenSalida detalle){
		try{
			Producto producto = detalle.getProducto();
			double cantidadAux = detalle.getCantidadSolicitada();
			double cantidadSolicitada = detalle.getCantidadSolicitada();//6
			int cantidad = 1;
			//obtener todos los detalles del producto, para poder descontar stock de acuerdo a la cantidad solicitada
			List<DetalleProducto> listDetalleProducto = detalleProductoRepository.findAllByProductoAndAlmacenOrderByFecha(almacen,producto,gestion);
			//5 | 10
			if(listDetalleProducto.size()>0){
				for(DetalleProducto d : listDetalleProducto){
					double stockActual = d.getStockActual();//5 
					double precio = d.getPrecio(); // 50 
					if(cantidadSolicitada > 0){// 6
						double stockFinal = stockActual- cantidadSolicitada; // 5-6=-1 | 10-5=5 |
						double cantidadRestada = stockFinal < 0 ? cantidadSolicitada -(cantidadSolicitada - stockActual) : cantidadSolicitada; //6-(6-5)=5 
						d.setStockActual( stockFinal <= 0 ? 0 : stockFinal); // 0 | 5
						d.setEstado(stockFinal<=0?"IN":"AC"); // IN | AC
						detalleProductoRegistration.updated(d);
						cantidadSolicitada = cantidadSolicitada - cantidadRestada  ;//actualizar cantidad solicitada // 6-5=1
						if(cantidad == 1){
							detalle.setCantidadEntregada(cantidadRestada);
							detalle.setCantidadSolicitada(cantidadAux);
							detalle.setPrecioUnitario(precio);
							detalle.setTotal(precio*cantidadRestada);
							detalleOrdenSalidaRegistration.updated(detalle);
						}else{//nuevo DetalleOrdenSalida con otro precio
							detalle.setId(0);
							detalle.setCantidadEntregada(cantidadRestada );
							detalle.setCantidadSolicitada(cantidadAux);
							detalle.setPrecioUnitario(precio);
							detalle.setTotal(precio*cantidadRestada);
							detalleOrdenSalidaRegistration.register(detalle);
						}
						cantidad = cantidad + 1;
					}
				}
				return true;
			}
			return false;
		}catch(Exception e){
			System.out.println("actualizarDetalleProductoByOrdenSalida() ERROR: "+e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * registro en la tabla kardex_producto
	 * @param gestionSesion
	 * @param selectedOrdenSalida
	 * @param prod
	 * @param fechaActual
	 * @param cantidad
	 * @param precioUnitario
	 * @param usuarioSession
	 * @throws Exception
	 */
	public void actualizarKardexProducto(String unidadSolicitante,Gestion gestionSesion,OrdenSalida selectedOrdenSalida,Producto prod,Date fechaActual,double cantidad,double precioUnitario,String usuarioSession) throws Exception{
		try{
			System.out.println("actualizarKardexProducto()");
			//registrar Kardex
			KardexProducto kardexProductoAnt = kardexProductoRepository.findKardexStockAnteriorByProductoAlmacen(gestionSesion,prod,selectedOrdenSalida.getAlmacen());
			double stockAnterior = 0;
			if(kardexProductoAnt != null){
				stockAnterior = kardexProductoAnt.getStockAnterior();
			}
			double entrada = 0;
			double salida = cantidad;
			double saldo = stockAnterior - cantidad;

			KardexProducto kardexProducto = new KardexProducto();
			kardexProducto.setUnidadSolicitante(unidadSolicitante);
			kardexProducto.setFecha(fechaActual);
			kardexProducto.setAlmacen(selectedOrdenSalida.getAlmacen());
			kardexProducto.setCantidad(cantidad);
			kardexProducto.setEstado("AC");
			kardexProducto.setFechaRegistro(fechaActual);
			kardexProducto.setGestion(gestionSesion);
			kardexProducto.setNumeroTransaccion("S-"+selectedOrdenSalida.getCorrelativo());

			//EN BOLIVIANOS
			kardexProducto.setPrecioUnitario(precioUnitario);
			kardexProducto.setTotalEntrada(precioUnitario * entrada);
			kardexProducto.setTotalSalida(precioUnitario * salida);
			kardexProducto.setTotalSaldo(precioUnitario * saldo);

			//CANTIDADES
			kardexProducto.setStock(entrada);//ENTRADA
			kardexProducto.setStockActual(salida);//SALIDA
			kardexProducto.setStockAnterior(saldo);//SALDO

			kardexProducto.setProducto(prod);
			kardexProducto.setTipoMovimiento("ORDEN SALIDA");
			kardexProducto.setUsuarioRegistro(usuarioSession);
			kardexProductoRegistration.register(kardexProducto);
		}catch(Exception e){
			System.out.println("actualizarKardexProducto Error : "+e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * actualizar en la tabla almacen_producto, actualiza el stock y el precio(promedio agrupando los productos)
	 * @param Almacen
	 * @param DetalleOrdenSalida
	 * @throws Exception
	 */
	public void actualizarStock(Gestion gestionSesion,Almacen almacen,Producto producto,double cantidadSolicitada) throws Exception {
		System.out.println("actualizarStock()");
		try{/*
			//0 . verificar si existe el producto en el almacen

			//AlmacenProducto almProd =  new AlmacenProducto();
			if(almProd != null){
				// 1 .  si existe el producto
				double oldStock = almProd.getStock();
				double oldPrecioUnitario = almProd.getPrecioUnitario();
				almProd.setStock(oldStock - newStock); //quitar (-)
				almProd.setPrecioUnitario(precioUnitario==-1?oldPrecioUnitario:((oldPrecioUnitario+precioUnitario)/2));//precioPonderado
				almacenProductoRegistration.updated(almProd);
				return ;
			}
		 */
			//Producto producto = detalle.getProducto();
			//double cantidadSolicitada = detalle.getCantidadSolicitada();// 15
			//obtener listAlmacenProducto ordenado por fecha segun metodo PEPS
			List<AlmacenProducto> listAlmacenProducto =  almacenProductoRepository.findAllByProductoAndAlmacenOrderByFecha(gestionSesion,almacen,producto);
			
			if(listAlmacenProducto.size()>0){
				for(AlmacenProducto d : listAlmacenProducto){
					double stockActual = d.getStock();//10 
					if(cantidadSolicitada > 0){// 15 
						double stockFinal = stockActual- cantidadSolicitada; // 10-15=-5 | 10-5=5 |
						double cantidadRestada = stockFinal < 0 ? cantidadSolicitada -(cantidadSolicitada - stockActual) : cantidadSolicitada; //15-(15-10)=10 
						d.setStock( stockFinal <= 0 ? 0 : stockFinal); // 0 | 5
						d.setEstado(stockFinal<=0?"IN":"AC"); // IN | AC
						almacenProductoRegistration.updated(d);
						cantidadSolicitada = cantidadSolicitada - cantidadRestada  ;//actualizar cantidad solicitada // 15-10=5
						
					}
				}
			}
		}catch(Exception e){
			System.out.println("actualizarStock Error : "+e.getMessage());
		}
	}


}
