package bo.com.qbit.webapp.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.KardexProducto;
import bo.com.qbit.webapp.model.Producto;

@ApplicationScoped
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
		String query = "select ser from KardexProducto ser where (ser.estado='AC' or ser.estado='IN') and ser.producto.id="+producto.getId()+" and ser.gestion.id="+gestion.getId()+" order by ser.id desc";
		System.out.println("Query KardexProducto: " + query);
		return em.createQuery(query).getResultList();
	}

	public KardexProducto findKardexStockAnteriorByProducto(Producto producto) {
		String query = "select ser from KardexProducto ser Producto prod,Gestion ges where ser.producto.id = prod.id = and ser.gestion.id = ges.id and (ser.estado='AC' or ser.estado='IN') and ser.producto.id="+producto.getId()+" order by ser.id desc";
		System.out.println("Query KardexProducto: " + query);
		List<KardexProducto> list =  em.createQuery(query).getResultList();
		return list.size()>0?list.get(0):null;
	}

}
