package bo.com.qbit.webapp.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.AlmacenProducto;
import bo.com.qbit.webapp.model.Producto;
import bo.com.qbit.webapp.model.Usuario;

@ApplicationScoped
public class AlmacenProductoRepository {

	@Inject
	private EntityManager em;

	public AlmacenProducto findById(int id) {
		return em.find(AlmacenProducto.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findAllOrderedByID() {
		String query = "select ser from AlmacenProducto ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		System.out.println("Query AlmacenProducto: " + query);
		return em.createQuery(query).getResultList();
	}

	public AlmacenProducto findByProducto(Producto producto) {
		try{
			String query = "select em from AlmacenProducto em where ( em.estado='AC' or em.estado='IN' ) and em.producto.id="
					+ producto.getId() ;
			System.out.println("Query AlmacenProducto: " + query);
			return (AlmacenProducto) em.createQuery(query).getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

}
