package bo.com.qbit.webapp.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.AlmacenProducto;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.Producto;

@Stateless
public class AlmacenProductoRepository {

	@Inject
	private EntityManager em;

	public AlmacenProducto findById(int id) {
		return em.find(AlmacenProducto.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findAllOrderedByID(Gestion gestion) {
		String query = "select ser from AlmacenProducto ser where ser.estado='AC' or ser.estado='IN' and ser.gestion.id="+gestion.getId()+" order by ser.id desc";
		System.out.println("Query AlmacenProducto: " + query);
		return em.createQuery(query).getResultList();
	}

	public AlmacenProducto findByAlmacenProducto(Gestion gestion,Almacen almacen,Producto producto) {
		System.out.println("findByAlmacenProducto() ");
		try{
			String query = "select em from AlmacenProducto em where ( em.estado='AC' or em.estado='IN' ) and em.gestion.id="+gestion.getId()+" and em.almacen.id="
					+ almacen.getId() + " and em.producto.id="+producto.getId();
			System.out.println("Query AlmacenProducto: " + query);
			return (AlmacenProducto) em.createQuery(query).getSingleResult();
		}catch(Exception e){
			System.out.println("findByAlmacenProducto() "+e.getMessage());
			return null;
		}
	}	

	public AlmacenProducto findByProducto(Gestion gestion,Producto producto) {
		try{
			String query = "select em from AlmacenProducto em where ( em.estado='AC' or em.estado='IN' ) and em.gestion.id="+gestion.getId()+" and em.producto.id="
					+ producto.getId() ;
			System.out.println("Query AlmacenProducto: " + query);
			return (AlmacenProducto) em.createQuery(query).getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * PEPS
	 * Obtener List<AlmacenProducto> por fechas
	 * @param almacen 
	 * @param producto
	 * @return List<AlmacenProducto>
	 */
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findAllByProductoAndAlmacenOrderByFecha(Gestion gestion, Almacen almacen,Producto producto) {
		try{
			String query = "select em from AlmacenProducto em where em.estado='AC' and em.producto.id="
					+ producto.getId() +" and em.almacen.id="+almacen.getId()+" and em.gestion.id="+gestion.getId()+" order by em.fechaRegistro asc";
			System.out.println("Query DetalleProducto: " + query);
			return em.createQuery(query).getResultList();
		}catch(Exception e){
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findByAlmacen(Gestion gestion,Almacen almacen) {
		try{
			String query = "select em from AlmacenProducto em where ( em.estado='AC' or em.estado='IN' ) and em.stock > 0 and em.gestion.id="+gestion.getId()+" and em.almacen.id="
					+ almacen.getId() ;
			System.out.println("Query AlmacenProducto: " + query);
			return em.createQuery(query).getResultList();
		}catch(Exception e){
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findAllByProducto(Gestion gestion,Producto producto) {
		try{
			String query = "select em from AlmacenProducto em where ( em.estado='AC' or em.estado='IN' ) and em.gestion.id="+gestion.getId()+" and em.producto.id="
					+ producto.getId() ;
			System.out.println("Query AlmacenProducto: " + query);
			return em.createQuery(query).getResultList();
		}catch(Exception e){
			return null;
		}
	}
	

	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findAllByProductoAndGestion(Producto producto,Gestion gestion) {
		try{
			String query = "select em from AlmacenProducto em where ( em.estado='AC' or em.estado='IN' ) and em.gestion.id="+gestion.getId()+" and em.producto.id="
					+ producto.getId() ;
			System.out.println("Query AlmacenProducto: " + query);
			return em.createQuery(query).getResultList();
		}catch(Exception e){
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findProductoConStockOrderedByID(Gestion gestion) {
		String query = "select ser from AlmacenProducto ser where ser.estado='AC' and ser.stock > 0 and ser.gestion.id="+gestion.getId()+" order by ser.id asc";
		System.out.println("Query AlmacenProducto: " + query);
		return em.createQuery(query).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findProductoConStockOrderedByIDAndGestion( Gestion gestion) {
		String query = "select ser from AlmacenProducto ser where ser.estado='AC' and ser.stock > 0 and ser.gestion.id="+gestion.getId()+" order by ser.id asc";
		System.out.println("Query AlmacenProducto: " + query);
		return em.createQuery(query).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public double findPrecioPromedioByProducto(Gestion gestion,Producto producto) {
		double promedio = 0;
		try{
			String query = "select em from AlmacenProducto em where ( em.estado='AC' or em.estado='IN' ) and em.gestion.id="+gestion.getId()+" and em.producto.id="
					+ producto.getId() ;
			System.out.println("Query AlmacenProducto: " + query);
			List<AlmacenProducto> list = em.createQuery(query).getResultList();
			for(AlmacenProducto a:list){
				promedio = promedio + a.getPrecioUnitario();
			}
			return list.size()>0?promedio/list.size():promedio;
		}catch(Exception e){
			return promedio;
		}
	}

}
