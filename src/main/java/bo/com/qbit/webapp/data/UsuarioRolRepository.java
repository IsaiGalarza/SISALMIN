package bo.com.qbit.webapp.data;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import bo.com.qbit.webapp.model.Roles;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.model.UsuarioRol;
import bo.com.qbit.webapp.model.security.UsuarioRolV1;

import java.util.ArrayList;
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
	
	public UsuarioRolV1 findByUsuarioV1(Usuario usuario){
		String query = "select ur from UsuarioRolV1 ur where ur.usuario.id="+usuario.getId();
        return (UsuarioRolV1) em.createQuery(query).getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Roles> findByIdRoles(Integer id){
		try {
			System.out.println(">>>> Parametro: "+id);
	        String query = "select ur from UsuarioRol ur where ur.usuario.id='"+id+"'";
	        List<UsuarioRol> ur=em.createQuery(query).getResultList();
	        List<Roles> r = new ArrayList<Roles>();
	        if(ur!=null&&!ur.isEmpty()){
	        	for (UsuarioRol usuarioRol : ur) {
					r.add(usuarioRol.getRoles());
				}
	        }
	        return r;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

    public List<UsuarioRol> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UsuarioRol> criteria = cb.createQuery(UsuarioRol.class);
        Root<UsuarioRol> UsuarioRol = criteria.from(UsuarioRol.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
        criteria.select(UsuarioRol).orderBy(cb.asc(UsuarioRol.get("roles.name")));
        return em.createQuery(criteria).getResultList();
    }
}
