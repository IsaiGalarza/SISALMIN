package bo.com.qbit.webapp.data;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import bo.com.qbit.webapp.model.security.Accion;
import bo.com.qbit.webapp.model.security.DetallePagina;
import bo.com.qbit.webapp.model.security.Pagina;

@Stateless
public class DetallePaginaRepository {

	@Inject
	private EntityManager em;

	private Logger log = Logger.getLogger(this.getClass());

	public DetallePagina findById(int id) {
		return em.find(DetallePagina.class, id);
	}


	@SuppressWarnings("unchecked")
	public List<DetallePagina> findAll() {
		try{
			String query = "select em from DetallePagina em ";
			log.info("Query DetallePagina: "+query);
			return em.createQuery(query).getResultList();
		}catch(Exception e){
			return new ArrayList<DetallePagina>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Accion> findAccionByPagina(Pagina pagina) {
		try{
			String query = "select ac from DetallePagina em ,Accion ac where em.accion.id = ac.id and em.pagina.id = "+pagina.getId();
			log.info("Query DetallePagina: "+query);
			return em.createQuery(query).getResultList();
		}catch(Exception e){
			return new ArrayList<Accion>();
		}
	}

	public DetallePagina findAccionByPaginaAndAccion(Pagina pagina,Accion accion) {
		try{
			String query = "select em from DetallePagina em where em.accion.id = "+accion.getId()+" and em.pagina.id = "+pagina.getId();
			log.info("Query DetallePagina: "+query);
			return (DetallePagina) em.createQuery(query).getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

}
