package bo.com.qbit.webapp.data;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.Nivel;

@Stateless
public class NivelRepository {
	 
	@Inject
    private EntityManager em;

    public Nivel findById(int id) {
        return em.find(Nivel.class, id);
    }
    
    public Nivel findByNivelEmpresa(int nivel,Empresa empresa) {
    	String query = "select em from Nivel em  where em.nivel="+nivel+" and em.empresa.id="+empresa.getId();
    	System.out.println("Query Nivel: "+query);
    	return (Nivel) em.createQuery(query).getSingleResult();
    }
    
	
}
