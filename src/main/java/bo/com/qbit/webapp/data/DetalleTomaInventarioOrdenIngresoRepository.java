package bo.com.qbit.webapp.data;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.DetalleTomaInventarioOrdenIngreso;
import bo.com.qbit.webapp.model.OrdenIngreso;

@Stateless
public class DetalleTomaInventarioOrdenIngresoRepository {

	@Inject
	private EntityManager em;

	@Inject
	private Logger log;

	public DetalleTomaInventarioOrdenIngreso findById(int id) {
		return em.find(DetalleTomaInventarioOrdenIngreso.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<DetalleTomaInventarioOrdenIngreso> findAllOrderByDesc() {
		String query = "select em from DetalleTomaInventarioOrdenIngreso em, OrdenIngreso oc where em.estado='AC' and em.estado='IN'and oc.id=em.ordenIngreso.id  order by em.id desc";
		log.info("Query DetalleOrdenIngreso: "+query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<DetalleTomaInventarioOrdenIngreso> findAllByOrdenIngreso(OrdenIngreso ordenIngreso) {
		String query = "select em from DetalleOrdenIngreso em where em.estado='AC' and em.ordenIngreso.id="+ordenIngreso.getId()+"  order by em.id desc";
		log.info("Query DetalleOrdenIngreso: "+query);
		return em.createQuery(query).getResultList();
	}
}
