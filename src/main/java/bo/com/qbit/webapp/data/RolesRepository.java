package bo.com.qbit.webapp.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import bo.com.qbit.webapp.model.security.Roles;

@Stateless
public class RolesRepository {
	 
	@Inject
    private EntityManager em;

    public Roles findById(int id) {
        return em.find(Roles.class, id);
    }
    
    @SuppressWarnings("unchecked")
	public List<Roles> findAllOrderByAsc(){
    	String query = "select em from Roles em where em.estado='AC' or em.estado='IN' or em.estado='SU' order by em.id asc";
    	System.out.println("Query Roles: "+query);
    	return em.createQuery(query).getResultList();
    }
    
    public Roles findRolByNombre(String name) {
    	String query = "select em from Roles em  where em.nombre='"+name+"'";
    	System.out.println("Query Roles: "+query);
    	return (Roles) em.createQuery(query).getSingleResult();
    }
	
}
