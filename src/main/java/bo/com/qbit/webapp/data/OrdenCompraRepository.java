package bo.com.qbit.webapp.data;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.OrdenCompra;

@Stateless
public class OrdenCompraRepository {

	@Inject
	private EntityManager em;

	@Inject
	private Logger log;

	public OrdenCompra findById(int id) {
		return em.find(OrdenCompra.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<OrdenCompra> findAllOrderedByID() {
		String query = "select em from OrdenCompra em where em.estado='AC' or em.estado='IN' order by em.id desc";
		log.info("Query OrdenCompra: "+query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<OrdenCompra> findAllAcivosOrderedByID() {
		String query = "select em from OrdenCompra em where em.estado='AC' order by em.id desc";
		log.info("Query OrdenCompra: "+query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<OrdenCompra> findAllByEmpresa(Empresa empresa) {
		String query = "select em from OrdenCompra em where (em.estado='AC' or em.estado='PR') and em.empresa.id="+empresa.getId()+" order by em.id desc";
		log.info("Query OrdenCompra: "+query);
		return em.createQuery(query).getResultList();
	}
}
