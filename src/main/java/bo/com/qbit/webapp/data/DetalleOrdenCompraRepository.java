package bo.com.qbit.webapp.data;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.DetalleOrdenCompra;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.OrdenCompra;

@Stateless
public class DetalleOrdenCompraRepository {

	@Inject
	private EntityManager em;

	@Inject
	private Logger log;

	public DetalleOrdenCompra findById(int id) {
		return em.find(DetalleOrdenCompra.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<DetalleOrdenCompra> findAllActivosByEmpresa(Empresa empresa) {
		String query = "select em from DetalleOrdenCompra em, OrdenCompra oc where em.estado='AC' and oc.id=em.ordenCompra.id and oc.empresa.id="+empresa.getId()+" order by em.id desc";
		log.info("Query DetalleOrdenCompra: "+query);
		return em.createQuery(query).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<DetalleOrdenCompra> findAllByOrdenCompra(OrdenCompra ordenCompra) {
		String query = "select em from DetalleOrdenCompra em, OrdenCompra oc where em.estado='AC' and em.ordenCompra.id="+ordenCompra.getId()+"  order by em.id desc";
		log.info("Query DetalleOrdenCompra: "+query);
		return em.createQuery(query).getResultList();
	}
}
