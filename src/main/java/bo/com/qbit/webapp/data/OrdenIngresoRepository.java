package bo.com.qbit.webapp.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.OrdenIngreso;

@ApplicationScoped
public class OrdenIngresoRepository {

	@Inject
	private EntityManager em;

	public OrdenIngreso findById(int id) {
		return em.find(OrdenIngreso.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<OrdenIngreso> findAllOrderedByID() {
		String query = "select ser from OrdenIngreso ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		System.out.println("Query OrdenIngreso: " + query);
		return em.createQuery(query).getResultList();
	}

}
