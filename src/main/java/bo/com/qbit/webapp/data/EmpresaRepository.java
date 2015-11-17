package bo.com.qbit.webapp.data;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.Usuario;
 
@Stateless
public class EmpresaRepository {
	
	@Inject
    private EntityManager em;
	
	@Inject
	private Logger log;

    public Empresa findById(int id) {
        return em.find(Empresa.class, id);
    }
    
    @SuppressWarnings("unchecked")
	public List<Empresa> findAllOrderedByID() {
    	String query = "select em from Empresa em where em.estado='AC' or ser.estado='IN' order by em.id desc";
    	log.info("Query Empresa: "+query);
    	return em.createQuery(query).getResultList();
    }
    
    public Empresa findByRazonSocial(String razonSocial) {
    	String query = "select em from Empresa em where em.razonSocial='"+razonSocial+"'";
    	log.info("Query Empresa: "+query);
    	return (Empresa) em.createQuery(query).getSingleResult();
    }
    
    public Empresa findByNIT(String NIT) {
    	String query = "select em from Empresa em where em.NIT='"+NIT+"'";
    	log.info("Query Empresa: "+query);
    	return (Empresa) em.createQuery(query).getSingleResult();
    }

    public List<Empresa> findAll(){
    	CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Empresa> criteria = cb.createQuery(Empresa.class);
		Root<Empresa> company = criteria.from(Empresa.class);
		criteria.select(company);
		return em.createQuery(criteria).getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public List<Empresa> findAllByUsuario(Usuario u) {
    	String query = "select em from Empresa em ,UsuarioEmpresa ue where (em.estado='AC' or em.estado='IN') and ue.usuario.id="+u.getId()
    			+ " and em.id=ue.empresa.id  order by em.id desc";
    	log.info("Query Empresa: "+query);
    	return em.createQuery(query).getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public List<Empresa> findAllActivasByUsuario(Usuario u) {
    	String query = "select em from Empresa em ,UsuarioEmpresa ue where em.estado='AC' and ue.usuario.id="+u.getId()
    			+ " and em.id=ue.empresa.id  order by em.id desc";
    	log.info("Query Empresa: "+query);
    	return em.createQuery(query).getResultList();
    }
    
    public Empresa findByUsuarioEmpresa(Usuario u, String nombreEmpresa) {
    	String query = "select em from Empresa em ,UsuarioEmpresa ue where ue.usuario.id="+u.getId()
    			+ " and em.id=ue.empresa.id and em.razonSocial='"+nombreEmpresa+"' order by em.id desc";
    	log.info("Query Empresa: "+query);
    	return (Empresa) em.createQuery(query).getSingleResult();
    }
    
	
}
