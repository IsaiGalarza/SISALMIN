package bo.com.qbit.webapp.data;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Compra;
import bo.com.qbit.webapp.model.Empresa;

@Stateless
public class CompraRepository {

	@Inject
	private EntityManager em;
	
	@Inject
	private Logger log;

	public Compra findById(int id) {
		return em.find(Compra.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Compra> findAllOrderedByID() {
		String query = "select em from Compra em ";// where em.estado='AC' or ser.estado='IN' order by em.id desc";
		log.info("Query Compra: "+query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Compra> findAllByEmpresa(Empresa empresa){
		String query = "select em from Compra em  where em.empresa.id="+empresa.getId()+" order by em.id asc";
		log.info("Query Compra: "+query);
		return em.createQuery(query).getResultList();
	}
}
