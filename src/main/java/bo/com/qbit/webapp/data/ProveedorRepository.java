package bo.com.qbit.webapp.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Proveedor;

@Stateless
public class ProveedorRepository {

	@Inject
	private EntityManager em;

	public Proveedor findById(int id) {
		return em.find(Proveedor.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Proveedor> findAllOrderedByID() {
		String query = "select em from Proveedor em ";// where em.estado='AC' or em.estado='IN' order by em.id desc";
		System.out.println("Query Proveedor: "+query);
		return em.createQuery(query).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Proveedor> findAllActivoOrderedByID() {
		String query = "select em from Proveedor em where em.estado='AC' order by em.id desc";
		System.out.println("Query Proveedor: "+query);
		return em.createQuery(query).getResultList();
	}

	
	@SuppressWarnings("unchecked")
	public List<Proveedor> findAllByEmpresa() {
		String query = "select em from Proveedor em where (em.estado='AC' or em.estado='IN')  order by em.id desc";
		System.out.println("Query Proveedor: "+query);
		return em.createQuery(query).getResultList();
	}

}
