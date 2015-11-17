package bo.com.qbit.webapp.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import bo.com.qbit.webapp.model.Funcionario;

@ApplicationScoped
public class FuncionarioRepository {

	@Inject
	private EntityManager em;

	public Funcionario findById(int id) {
		return em.find(Funcionario.class, id);
	}

	public List<Funcionario> findAllOrderedByDescripcion() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Funcionario> criteria = cb.createQuery(Funcionario.class);
		Root<Funcionario> presentacion = criteria.from(Funcionario.class);
		criteria.select(presentacion).orderBy(
				cb.desc(presentacion.get("descripcion")));
		return em.createQuery(criteria).getResultList();
	}

	public List<Funcionario> findAllOrderedByID() {
		String query = "select ser from Funcionario ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		System.out.println("Query Funcionario: " + query);
		return em.createQuery(query).getResultList();
	}
	

	public List<Funcionario> findAllOrderedByFechaRegistro() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Funcionario> criteria = cb.createQuery(Funcionario.class);
		Root<Funcionario> presentacion = criteria.from(Funcionario.class);
		criteria.select(presentacion).orderBy(
				cb.desc(presentacion.get("fechaRegistro")));
		return em.createQuery(criteria).getResultList();
	}

	public List<Funcionario> findAllFuncionarioForNombre(String criterio) {
		try {
			String query = "select ser from Funcionario ser where ser.nombre like '%"
					+ criterio + "%' or ser.apellidos like '%"+ criterio + "%'";
			System.out.println("Consulta: " + query);
			List<Funcionario> listaFuncionario = em.createQuery(query).getResultList();
			return listaFuncionario;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error en findAllFuncionarioForNombre: "
					+ e.getMessage());
			return null;
		}
	}

	public List<Funcionario> traerFuncionarioActivas() {
		try {
			String query = "select ser from Funcionario ser where ser.estado='AC' order by ser.id desc";
			System.out.println("Consulta traerFuncionarioActivas: " + query);
			List<Funcionario> listaFuncionario = em.createQuery(query).getResultList();
			return listaFuncionario;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error en traerFuncionarioActivas: "
					+ e.getMessage());
			return null;
		}
	}

	public List<Funcionario> findAll100UltimosFuncionario() {
		try {
			String query = "select ser from Funcionario ser order by ser.fechaRegistro desc";
			System.out.println("Consulta: " + query);
			List<Funcionario> listaFuncionario = em.createQuery(query)
					.setMaxResults(100).getResultList();
			return listaFuncionario;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error en findAll100UltimosFuncionario: "
					+ e.getMessage());
			return null;
		}
	}

}
