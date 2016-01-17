package bo.com.qbit.webapp.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.KardexProducto;
import bo.com.qbit.webapp.model.Producto;

@Stateless
public class KardexProductoRepository {

	@Inject
	private EntityManager em;

	public KardexProducto findById(int id) {
		return em.find(KardexProducto.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<KardexProducto> findAllOrderedByID() {
		String query = "select ser from KardexProducto ser where (ser.estado='AC' or ser.estado='IN') order by ser.id desc";
		System.out.println("Query KardexProducto: " + query);
		return em.createQuery(query).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<KardexProducto> findByProductoAndGestion(Producto producto, Gestion gestion) {
		String query = "select ser from KardexProducto ser where ser.estado='AC' and ser.producto.id="+producto.getId()+" and ser.gestion.id="+gestion.getId()+" order by ser.id asc";
		System.out.println("Query KardexProducto: " + query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public KardexProducto findKardexStockAnteriorByProducto(Gestion gestion, Producto producto) {
		String query = "select ser from KardexProducto ser, Producto prod,Gestion ges where ser.producto.id = prod.id  and ser.gestion.id = ges.id and (ser.estado='AC' or ser.estado='IN') and ser.producto.id="+producto.getId()+" and ser.gestion.id="+gestion.getId()+"  order by ser.id desc";
		System.out.println("Query KardexProducto: " + query);
		List<KardexProducto> list =  em.createQuery(query).getResultList();
		return list.size()>0?list.get(0):null;
	}
	
	@SuppressWarnings("unchecked")
	public KardexProducto findKardexStockAnteriorByProductoAlmacen(Gestion gestion, Producto producto,Almacen almacen) {
		String query = "select ser from KardexProducto ser, Producto prod,Gestion ges where ser.producto.id = prod.id  and ser.gestion.id = ges.id and (ser.estado='AC' or ser.estado='IN') and ser.producto.id="+producto.getId()+" and ser.almacen.id="+almacen.getId()+" and ser.gestion.id="+gestion.getId()+" order by ser.id desc";
		System.out.println("Query KardexProducto: " + query);
		List<KardexProducto> list =  em.createQuery(query).getResultList();
		return list.size()>0?list.get(0):null;
	}
	
	@SuppressWarnings("unchecked")
	public List<KardexProducto> findByProductoAlmacenAndGestion(Producto producto,Almacen almacen, Gestion gestion) {
		String query = "select ser from KardexProducto ser where ser.estado='AC' and ser.producto.id="+producto.getId()+" and ser.almacen.id="+almacen.getId()+" and ser.gestion.id="+gestion.getId()+" order by ser.id asc";
		System.out.println("Query KardexProducto: " + query);
		return em.createQuery(query).getResultList();
	}
	
	

}
