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
			String query = "select em from UsuarioRol em where em.user.id='"+id+"'";
			return em.createQuery(query).getResultList();
		} catch (Exception e) {
			System.out.println("Error in findByEmail: "+e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public UsuarioRol findByUsuario(Usuario usuario){
		String query = "select em from UsuarioRol em where em.usuario.id="+usuario.getId();
		System.out.println("query findByUsuario : "+query);
		return (UsuarioRol) em.createQuery(query).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<UsuarioRol> findAllOrderedByNombre() {
		String query = "select em from UsuarioRol em where (em.estado ='AC' or em.estado='IN') order by em.id desc";
		return  em.createQuery(query).getResultList();
	}
}
