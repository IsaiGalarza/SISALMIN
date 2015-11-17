package bo.com.qbit.webapp.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.MonedaEmpresa;

@Stateless
public class MonedaEmpresaRepository {
	 
	@Inject
    private EntityManager em;

    public MonedaEmpresa findById(int id) {
        return em.find(MonedaEmpresa.class, id);
    }
    
    @SuppressWarnings("unchecked")
	public List<MonedaEmpresa> findAllByEmpresa(Empresa empresa) {
    	String query = "select em from MonedaEmpresa em  where em.empresa.id="+empresa.getId();
    	System.out.println("Query Moneda: "+query);
    	return (List<MonedaEmpresa>) em.createQuery(query).getResultList();
    }    
	
}
