package bo.com.qbit.webapp.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.security.Permiso;
import bo.com.qbit.webapp.model.security.Roles;

@Stateless
public class PermisoRepository {
	
	@Inject
    private EntityManager em;

    public Permiso findById(int id) {
        return em.find(Permiso.class, id);
    }
    
    @SuppressWarnings("unchecked")
	public List<Permiso> findAllOrderedByID() {
    	String query = "select em from Permiso em  where em.estado='AC' order by em.id asc";
    	System.out.println("Query Permiso: "+query);
    	return em.createQuery(query).getResultList();
    }
    
    public Permiso findByNombre(String nombre) {
    	String query = "select em from Permiso em where em.name='"+nombre+"'";
    	System.out.println("Query Permiso: "+query);
    	return (Permiso) em.createQuery(query).getSingleResult();
    }
    
    @SuppressWarnings("unchecked")
	public List<Permiso> findByRol(Roles roles){
    	String query = "select em from Permiso em where em.estado = 'AC' and em.roles.id="+roles.getId();
    	System.out.println("Query Permiso: "+query);
    	return em.createQuery(query).getResultList();
    }
	
}
