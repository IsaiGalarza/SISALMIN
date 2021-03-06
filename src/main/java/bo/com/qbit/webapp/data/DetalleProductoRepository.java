package bo.com.qbit.webapp.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.DetalleProducto;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.Producto;

@ApplicationScoped
public class DetalleProductoRepository {

	@Inject
	private EntityManager em;

	public DetalleProducto findById(int id) {
		return em.find(DetalleProducto.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<DetalleProducto> findByAlmacenProductoAndFecha(Gestion gestion,Almacen almacen,Producto producto,Date fecha) {
		System.out.println("findByAlmacenProductoAndFecha() ");
		try{
			String query = "select em from DetalleProducto em where ( em.estado='AC' or em.estado='IN' ) and em.gestion.id="+gestion.getId()+" and em.almacen.id="
					+ almacen.getId() + " and em.producto.id="+producto.getId()+" and em.fechaRegistro='"+fecha+"'";
			System.out.println("Query DetalleProducto: " + query);
			return em.createQuery(query).getResultList();
		}catch(Exception e){
			System.out.println("findByAlmacenProducto() "+e.getMessage());
			return new ArrayList<DetalleProducto>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<DetalleProducto> findAllOrderedByID(Gestion gestion) {
		String query = "select em from DetalleProducto em where em.estado='AC' or em.estado='IN' and em.gestion.id="+gestion.getId()+" order by em.id desc";
		System.out.println("Query DetalleProducto: " + query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<DetalleProducto> findAllActivoOrderedByID(Gestion gestion) {
		String query = "select em from DetalleProducto em where em.estado='AC' and em.gestion.id="+gestion.getId()+" order by em.fecha asc";
		System.out.println("Query DetalleProducto: " + query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<DetalleProducto> findAllActivoAndGestionOrderedByID(Gestion gestion) {
		String query = "select em from DetalleProducto em where em.estado='AC' and em.gestion.id="+gestion.getId()+" order by em.fecha asc";
		System.out.println("Query DetalleProducto: " + query);
		return em.createQuery(query).getResultList();
	}

	/**
	 * PEPS
	 * Obtener detalleProducto por fechas 
	 * @param producto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DetalleProducto> findAllByProductoOrderByFecha(Producto producto,Gestion gestion) {
		try{
			String query = "select em from DetalleProducto em where em.estado='AC' and em.gestion.id="+gestion.getId()+" and em.producto.id="
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
	public List<DetalleProducto> findAllByProductoAndAlmacenOrderByFecha(Almacen almacen,Producto producto,Gestion gestion) {
		try{
			String query = "select em from DetalleProducto em where em.estado='AC' and em.gestion.id="+gestion.getId()+" and em.producto.id="
					+ producto.getId() +" and em.almacen.id="+almacen.getId()+" order by em.fecha asc";
			System.out.println("Query DetalleProducto: " + query);
			return em.createQuery(query).getResultList();
		}catch(Exception e){
			return null;
		}
	}
	//select em from DetalleProducto em where em.estado='AC' and em.producto.id=21 and em.almacen.id=3 order by em.fecha desc
}
