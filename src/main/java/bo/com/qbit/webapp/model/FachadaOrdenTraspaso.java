package bo.com.qbit.webapp.model;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import bo.com.qbit.webapp.data.AlmacenProductoRepository;
import bo.com.qbit.webapp.service.AlmacenProductoRegistration;

/**
 * @Pattern Facade
 * @author mauriciobejaranorivera
 *
 */
@Stateless
public class FachadaOrdenTraspaso implements Serializable {

	private static final long serialVersionUID = -3921522485942842166L;
	private @Inject AlmacenProductoRepository almacenProductoRepository;
	private @Inject AlmacenProductoRegistration almacenProductoRegistration;


	/**
	 * actualizar en la tabla almacen_producto, actualiza el stock y el precio(promedio agrupando los productos)
	 * @param Almacen
	 * @param DetalleOrdenSalida
	 * @throws Exception
	 */
	public void actualizarStock(Gestion gestionSesion,Almacen almacen,DetalleTomaInventario detalle) throws Exception {
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
			Producto producto = detalle.getProducto();
			double cantidadSolicitada = detalle.getDiferencia();// 15
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
