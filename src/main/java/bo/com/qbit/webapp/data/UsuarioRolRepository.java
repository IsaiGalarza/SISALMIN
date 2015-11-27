package bo.com.qbit.webapp.data;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.model.security.UsuarioRol;

import java.util.List;

@Stateless
public class UsuarioRolRepository {
 
    @Inject
    private EntityManager em;

    public UsuarioRol findById(Long id) {
        return em.find(UsuarioRol.class, id);
    }    
    
	@SuppressWarnings("unchecked")
	public List<UsuarioRol> findById(Integer id) {
		try {
			System.out.println(">>>> Parametro: "+id);
	        String query = "select ur from UsuarioRol ur where ur.user.id='"+id+"'";
	        return em.createQuery(query).getResultList();
		} catch (Exception e) {
			System.out.println("Error in findByEmail: "+e.getMessage());
			e.printStackTrace();
			return null;
		}
    }
	
	public UsuarioRol findByUsuario(Usuario usuario){
		String query = "select ur from UsuarioRol ur where ur.usuario.id="+usuario.getId();
        return (UsuarioRol) em.createQuery(query).getSingleResult();
	}
	
//	public UsuarioRol findByUsuarioV1(Usuario usuario){
//		String query = "select ur from UsuarioRol ur where ur.estado ='AC' and ur.usuario.id="+usuario.getId();
//        return (UsuarioRol) em.createQuery(query).getSingleResult();
//	}
//
//    public List<UsuarioRol> findAllOrderedByNombre() {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<UsuarioRol> criteria = cb.createQuery(UsuarioRol.class);
//        Root<UsuarioRol> UsuarioRol = criteria.from(UsuarioRol.class);
//        criteria.select(UsuarioRol).orderBy(cb.asc(UsuarioRol.get("rol.name")));
//        return em.createQuery(criteria).getResultList();
//    }
}
