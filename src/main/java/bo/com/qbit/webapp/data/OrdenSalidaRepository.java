package bo.com.qbit.webapp.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import bo.com.qbit.webapp.model.OrdenSalida;

@ApplicationScoped
public class OrdenSalidaRepository {

	@Inject
	private EntityManager em;

	public OrdenSalida findById(int id) {
		return em.find(OrdenSalida.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<OrdenSalida> findAllOrderedByID() {
		String query = "select ser from OrdenSalida ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		System.out.println("Query findAllOrderedByID: " + query);
		return em.createQuery(query).getResultList();
	}

}
