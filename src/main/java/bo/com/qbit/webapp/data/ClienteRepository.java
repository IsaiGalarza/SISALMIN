package bo.com.qbit.webapp.data;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Cliente;
import bo.com.qbit.webapp.model.Empresa;

@Stateless
public class ClienteRepository {

	@Inject
	private EntityManager em;
	
	@Inject
    private Logger log;
	//log.info

	public Cliente findById(int id) {
		return em.find(Cliente.class, id);
	}

	public Cliente findByNombre(String nombre) {
		String query = "select em from Cliente em where em.estado='AC' and em.nombre='"+nombre+"' ";
		log.info("Query Cliente: "+query);
		return (Cliente) em.createQuery(query).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Cliente> findByEmpresa(Empresa empresa) {
		try{
			String query = "select em from Cliente em  where (em.estado='AC' or em.estado='IN') and em.empresa.id="+empresa.getId()+ " order by em.id desc";
			log.info("Query Cliente: "+query);
			return em.createQuery(query).getResultList();
		}catch(Exception e){
			return new ArrayList<>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Cliente> findActivosByEmpresa(Empresa empresa) {
		try{
			String query = "select em from Cliente em  where em.estado='AC' and em.empresa.id="+empresa.getId()+ " order by em.id desc";
			log.info("Query Cliente: "+query);
			return em.createQuery(query).getResultList();
		}catch(Exception e){
			return new ArrayList<>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Cliente> findAllOrderedByID() {
		String query = "select em from Cliente em  where em.estado='AC' or ser.estado='IN' order by em.id desc";
		log.info("Query Cliente: "+query);
		return em.createQuery(query).getResultList();
	}



}
