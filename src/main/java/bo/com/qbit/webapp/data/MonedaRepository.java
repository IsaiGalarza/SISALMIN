package bo.com.qbit.webapp.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.Moneda;
import bo.com.qbit.webapp.model.MonedaEmpresa;

@Stateless
public class MonedaRepository {
	 
	@Inject
    private EntityManager em;

    public Moneda findById(int id) {
        return em.find(Moneda.class, id);
    }
    
    public Moneda findByNombre(String nombre) {
    	String query = "select em from Moneda em  where em.nombre='"+nombre+"' ";
    	System.out.println("Query Moneda: "+query);
    	return (Moneda) em.createQuery(query).getSingleResult();
    }
    
    @SuppressWarnings("unchecked")
	public List<Moneda> findAllByEmpresa(Empresa empresa) {
    	String query = "select em.moneda from MonedaEmpresa em  where em.empresa.id="+empresa.getId();
    	System.out.println("Query Moneda: "+query);
    	return (List<Moneda>) em.createQuery(query).getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public List<Moneda> findAllOrderedByID() {
    	String query = "select em from Moneda em ";// where em.estado='AC' or em.estado='IN' order by em.id desc";
    	System.out.println("Query Moneda: "+query);
    	return em.createQuery(query).getResultList();
    }

    @SuppressWarnings("unchecked")
	public List<Moneda> findAll() {
    	String query = "select em from Moneda em ";// where em.estado='AC' or em.estado='IN' order by em.id desc";
    	System.out.println("Query Moneda: "+query);
    	return em.createQuery(query).getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public List<MonedaEmpresa> findMonedaEmpresaAllByEmpresa(Empresa empresa){
    	String query = "select em from MonedaEmpresa em  where ( em.estado='AC' or em.estado='IN') and em.empresa.id="+empresa.getId()+" order by em.id asc";
    	System.out.println("Query Moneda: "+query);
    	return em.createQuery(query).getResultList();
    }
    
    @SuppressWarnings("unchecked")
   	public List<MonedaEmpresa> findMonedaEmpresaAllActivasByEmpresa(Empresa empresa){
       	String query = "select em from MonedaEmpresa em  where em.estado='AC' and em.empresa.id="+empresa.getId()+" order by em.id asc";
       	System.out.println("Query Moneda: "+query);
       	return em.createQuery(query).getResultList();
       }
    
    public Moneda findMonedaByEmpresaAndTipo(Empresa empresa,String tipoMoneda){
    	String query = "select em from MonedaEmpresa em  where ( em.estado='AC' or em.estado='IN') and em.empresa.id="+empresa.getId()+" and em.tipo='"+tipoMoneda+"' order by em.id asc";
    	System.out.println("Query Moneda: "+query);
    	return ((MonedaEmpresa) em.createQuery(query).getSingleResult()).getMoneda();
    }
    
	
}
