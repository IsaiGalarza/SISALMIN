package bo.com.qbit.webapp.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.DetalleTomaInventario;
import bo.com.qbit.webapp.model.TomaInventario;

@ApplicationScoped
public class DetalleTomaInventarioRepository {

	@Inject
	private EntityManager em;

	public DetalleTomaInventario findById(int id) {
		return em.find(DetalleTomaInventario.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<DetalleTomaInventario> findAllOrderedByID() {
		String query = "select ser from DetalleTomaInventario ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		System.out.println("Query DetalleTomaInventario: " + query);
		return em.createQuery(query).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<DetalleTomaInventario> findAllByTomaInventario(TomaInventario tomaInventario) {
		String query = "select ser from DetalleTomaInventario ser where ( ser.estado='AC' or ser.estado='IN') and ser.tomaInventario.id="+tomaInventario.getId()+" order by ser.id desc";
		System.out.println("Query DetalleTomaInventario: " + query);
		return  em.createQuery(query).getResultList();
	}
	
	public DetalleTomaInventario findByTomaInventario(TomaInventario tomaInventario) {
		String query = "select ser from DetalleTomaInventario ser where ( ser.estado='AC' or ser.estado='IN') and ser.tomaInventario.id="+tomaInventario.getId();
		System.out.println("Query DetalleTomaInventario: " + query);
		return (DetalleTomaInventario) em.createQuery(query).getSingleResult();
	}
	
	

}
