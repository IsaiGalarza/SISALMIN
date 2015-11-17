package bo.com.qbit.webapp.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.TipoComprobante;

@Stateless
public class TipoComprobanteRepository {
	 
	@Inject
    private EntityManager em;

    public TipoComprobante findById(int id) {
        return em.find(TipoComprobante.class, id);
    }
    
    @SuppressWarnings("unchecked")
	public List<TipoComprobante> findAllOrderedByID() {
    	String query = "select em from TipoComprobante em ";// where em.estado='AC' or ser.estado='IN' order by em.id desc";
    	System.out.println("Query Sucursal: "+query);
    	return em.createQuery(query).getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public List<TipoComprobante> findAllByEmpresa(Empresa empresa) {
    	String query = "select em from TipoComprobante em  where em.empresa.id="+empresa.getId() +" and em.estado='AC' order by em.id asc";
    	System.out.println("Query TipoComprobante: "+query);
    	return em.createQuery(query).getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public List<TipoComprobante> findAllOrderByIdDescEmpresa(Empresa empresa) {
    	String query = "select em from TipoComprobante em  where em.empresa.id="+empresa.getId() +" and em.estado='AC' order by em.id desc";
    	System.out.println("Query TipoComprobante: "+query);
    	return em.createQuery(query).getResultList();
    }
    
    public TipoComprobante findByNombreAndEmpresa(String nombreTipoComprobante, Empresa empresa) {
    	String query = "select em from TipoComprobante em  where em.nombre='"+nombreTipoComprobante+"' and em.empresa.id="+empresa.getId();
    	System.out.println("Query TipoComprobante: "+query);
    	return (TipoComprobante) em.createQuery(query).getSingleResult();
    }

    public List<TipoComprobante> findAll(){
    	CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TipoComprobante> criteria = cb.createQuery(TipoComprobante.class);
		Root<TipoComprobante> company = criteria.from(TipoComprobante.class);
		criteria.select(company);
		return em.createQuery(criteria).getResultList();
    }
    
    
	
}
