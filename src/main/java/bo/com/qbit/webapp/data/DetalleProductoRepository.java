package bo.com.qbit.webapp.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.DetalleProducto;
import bo.com.qbit.webapp.model.Producto;

@ApplicationScoped
public class DetalleProductoRepository {

	@Inject
	private EntityManager em;

	public DetalleProducto findById(int id) {
		return em.find(DetalleProducto.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<DetalleProducto> findAllOrderedByID() {
		String query = "select em from DetalleProducto em where em.estado='AC' or em.estado='IN' order by em.id desc";
		System.out.println("Query AlmacenProducto: " + query);
		return em.createQuery(query).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<DetalleProducto> findAllActivoOrderedByID() {
		String query = "select em from DetalleProducto em where em.estado='AC' order by em.fecha asc";
		System.out.println("Query AlmacenProducto: " + query);
		return em.createQuery(query).getResultList();
	}

	/**
	 * PEPS
	 * Obtener detalleProducto por fechas 
	 * @param producto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DetalleProducto> findAllByProductoOrderByFecha(Producto producto) {
		try{
			String query = "select em from DetalleProducto em where em.estado='AC' and em.producto.id="
					+ producto.getId() +" order by em.fecha desc";
			System.out.println("Query DetalleProducto: " + query);
			return em.createQuery(query).getResultList();
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * PEPS
	 * Obtener detalleProducto por fechas
	 *  @param almacen 
	 * @param producto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DetalleProducto> findAllByProductoAndAlmacenOrderByFecha(Almacen almacen,Producto producto) {
		try{
			String query = "select em from DetalleProducto em where em.estado='AC' and em.producto.id="
					+ producto.getId() +" and em.almacen.id="+almacen.getId()+" order by em.fecha desc";
			System.out.println("Query DetalleProducto: " + query);
			return em.createQuery(query).getResultList();
		}catch(Exception e){
			return null;
		}
	}
}
