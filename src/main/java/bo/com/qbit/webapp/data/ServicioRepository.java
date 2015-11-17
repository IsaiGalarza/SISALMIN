package bo.com.qbit.webapp.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.Servicio;

@Stateless
public class ServicioRepository {
	 
	@Inject
    private EntityManager em;

    public Servicio findById(int id) {
        return em.find(Servicio.class, id);
    }
    
    @SuppressWarnings("unchecked")
	public List<Servicio> findAllOrderedByID() {
    	String query = "select em from Servicio em where em.estado='AC' or em.estado='IN' order by em.id desc";
    	System.out.println("Query Servicio: "+query);
    	return em.createQuery(query).getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public List<Servicio> findAllByEmpresa(Empresa empresa) {
    	String query = "select em from Servicio em where (em.estado='AC' or em.estado='IN') and em.empresa.id="+empresa.getId()+" order by em.id desc";
    	System.out.println("Query Servicio: "+query);
    	return em.createQuery(query).getResultList();
    }    
    
    @SuppressWarnings("unchecked")
	public List<Servicio> findAllActivosByEmpresa(Empresa empresa) {
    	String query = "select em from Servicio em where em.estado='AC' and em.empresa.id="+empresa.getId()+" order by em.id desc";
    	System.out.println("Query Servicio: "+query);
    	return em.createQuery(query).getResultList();
    } 
	
}
