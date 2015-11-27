package bo.com.qbit.webapp.data;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.DetalleOrdenIngreso;
import bo.com.qbit.webapp.model.DetalleOrdenSalida;
import bo.com.qbit.webapp.model.OrdenIngreso;
import bo.com.qbit.webapp.model.OrdenSalida;

@Stateless
public class DetalleOrdenSalidaRepository {
	
	//Test
	
	@Inject
	private EntityManager em;

	@Inject
	private Logger log;

	public DetalleOrdenSalida findById(int id) {
		return em.find(DetalleOrdenSalida.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<DetalleOrdenSalida> findAllOrderByDesc() {
		String query = "select em from DetalleOrdenSalida em, OrdenSalida oc where em.estado='AC' and em.estado='IN' and oc.id=em.ordenSalida.id  order by em.id desc";
		log.info("Query DetalleOrdenSalida: "+query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<DetalleOrdenSalida> findAllByOrdenSalida(OrdenSalida ordenSalida) {
		String query = "select em from DetalleOrdenSalida em where em.estado='AC' and em.ordenSalida.id="+ordenSalida.getId()+"  order by em.id desc";
		log.info("Query DetalleOrdenSalida: "+query);
		return em.createQuery(query).getResultList();
	}
	
}
