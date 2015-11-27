package bo.com.qbit.webapp.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.model.security.Roles;

@Stateless
public class UsuarioRepository {

	@Inject
	private EntityManager em;

	public Usuario findById(Long id) {
		try {

		} catch (Exception e) {
		}
		return em.find(Usuario.class, id);
	}

	public Usuario findById(Integer id){
		return em.find(Usuario.class, id);
	}

	public Usuario findByLogin(String name){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteria = cb.createQuery(Usuario.class);
		Root<Usuario> user = criteria.from(Usuario.class);
		criteria.select(user).where(cb.equal(user.get("login"), name));
		return em.createQuery(criteria).getSingleResult();
	}

	public Usuario findByLogin(String login, String password,Roles rol){
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Usuario> criteria = cb.createQuery(Usuario.class);
			Root<Usuario> user = criteria.from(Usuario.class);
			criteria.select(user).where(cb.equal(user.get("login"), login),cb.equal(user.get("password"), password));
			return em.createQuery(criteria).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> findAllOrderedByID() {
		String query = "select em from Usuario em where em.state='AC' or em.state='IN' or em.state='SU' order by em.id desc";
		System.out.println("Query Usuario: "+query);
		return em.createQuery(query).getResultList();
	}

	public Usuario findByLogin(String login, String password) {
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Usuario> criteria = cb.createQuery(Usuario.class);
			Root<Usuario> user = criteria.from(Usuario.class);
			criteria.select(user).where(cb.equal(user.get("login"), login),cb.equal(user.get("password"), password));
			return em.createQuery(criteria).getSingleResult();
		}catch(Exception e){
			System.out.println("usuario no valido error: "+e.getMessage());
			return null;
		}
	}

	public Usuario findByEmail(String email) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteria = cb.createQuery(Usuario.class);
		Root<Usuario> user = criteria.from(Usuario.class);
		criteria.select(user).where(cb.equal(user.get("email"), email));
		return em.createQuery(criteria).getSingleResult();
	}

	public List<Usuario> findAllOrderedByLogin() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteria = cb.createQuery(Usuario.class);
		Root<Usuario> user = criteria.from(Usuario.class);
		criteria.select(user).orderBy(cb.asc(user.get("login")));
		return em.createQuery(criteria).getResultList();
	}

	public List<Roles> findAllRol(){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Roles> criteria = cb.createQuery(Roles.class);
		Root<Roles> user = criteria.from(Roles.class);
		criteria.select(user).orderBy(cb.asc(user.get("login")));
		return em.createQuery(criteria).getResultList();
	}
}
