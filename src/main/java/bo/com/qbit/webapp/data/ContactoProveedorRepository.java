package bo.com.qbit.webapp.data;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.ContactoProveedor;
import bo.com.qbit.webapp.model.Proveedor;

@Stateless
public class ContactoProveedorRepository {

	@Inject
	private EntityManager em;

	@Inject
	private Logger log;

	public ContactoProveedor findById(int id) {
		return em.find(ContactoProveedor.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<ContactoProveedor> findAllOrderedByID() {
		String query = "select em from ContactoProveedor em ";// where em.estado='AC' or em.estado='IN' order by em.id desc";
		log.info("Query ContactoProveedor: "+query);
		return em.createQuery(query).getResultList();
	}

	public ContactoProveedor findByProveedor(Proveedor proveedor){
		try{
			String query = "select em from ContactoProveedor em  where em.proveedor.id="+proveedor.getId();
			log.info("Query ContactoProveedor: "+query);
			return (ContactoProveedor) em.createQuery(query).getSingleResult();
		}catch(javax.persistence.NoResultException e){
			log.info("findByProveedor() NOT FOUND ERROR: "+e.getMessage());
			return new ContactoProveedor();
		}
	}


}
