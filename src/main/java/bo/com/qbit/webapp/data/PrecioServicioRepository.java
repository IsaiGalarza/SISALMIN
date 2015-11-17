package bo.com.qbit.webapp.data;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.PrecioServicio;
import bo.com.qbit.webapp.model.Servicio;

@Stateless
public class PrecioServicioRepository {

	@Inject
	private EntityManager em;
	
	@Inject
	private Logger log;

	public PrecioServicio findById(int id) {
		return em.find(PrecioServicio.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<PrecioServicio> findAllOrderedByID() {
		String query = "select em from PrecioServicio em ";// where em.estado='AC' or em.estado='IN' order by em.id desc";
		log.info("Query PrecioServicio: "+query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<PrecioServicio> findAllByServicio(Servicio servicio){
		String query = "select em from PrecioServicio em  where em.servicio.id="+servicio.getId()+" order by em.id asc";
		log.info("Query PrecioServicio: "+query);
		return em.createQuery(query).getResultList();
	}
	
	
}
