package bo.com.qbit.webapp.data;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.DetalleOrdenTraspaso;
import bo.com.qbit.webapp.model.OrdenTraspaso;

@Stateless
public class DetalleOrdenTraspasoRepository {

	@Inject
	private EntityManager em;

	@Inject
	private Logger log;

	public DetalleOrdenTraspaso findById(int id) {
		return em.find(DetalleOrdenTraspaso.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<DetalleOrdenTraspaso> findAllOrderByDesc() {
		String query = "select em from DetalleOrdenTraspaso em, OrdenTraspaso oc where em.estado='AC' and em.estado='IN'and oc.id=em.ordenTraspaso.id  order by em.id desc";
		log.info("Query DetalleOrdenIngreso: "+query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<DetalleOrdenTraspaso> findAllByOrdenTraspaso(OrdenTraspaso ordenTraspaso) {
		String query = "select em from DetalleOrdenTraspaso em where em.estado='AC' and em.ordenTraspaso.id="+ordenTraspaso.getId()+"  order by em.id desc";
		log.info("Query DetalleOrdenIngreso: "+query);
		return em.createQuery(query).getResultList();
	}
}
