package bo.com.qbit.webapp.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.Proyecto;
import bo.com.qbit.webapp.model.Usuario;

@ApplicationScoped
public class ProyectoRepository {

	@Inject
	private EntityManager em;

	public Proyecto findById(int id) {
		return em.find(Proyecto.class, id);
	}

	public List<Proyecto> findAllOrderedByDescripcion() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Proyecto> criteria = cb.createQuery(Proyecto.class);
		Root<Proyecto> presentacion = criteria.from(Proyecto.class);
		criteria.select(presentacion).orderBy(
				cb.desc(presentacion.get("descripcion")));
		return em.createQuery(criteria).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Proyecto> findAllOrderedByID() {
		String query = "select ser from Proyecto ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		System.out.println("Query Proyecto: " + query);
		return em.createQuery(query).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Proyecto> findAllActivosOrderedByID() {
		String query = "select ser from Proyecto ser where ser.estado='AC' order by ser.id desc";
		System.out.println("Query Proyecto: " + query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public Proyecto findProyectoForUser(Usuario user) {
		String query = "select ser from Proyecto ser where (ser.estado='AC' or ser.estado='IN') and ser.encargado.id="
				+ user.getId() + " order by ser.id desc";
		System.out.println("Query Proyecto: " + query);
		List<Proyecto> listProyecto = em.createQuery(query).getResultList();
		if (listProyecto.size() > 0) {
			return listProyecto.get(0);
		} else {
			Proyecto Proyecto = new Proyecto();
			Proyecto.setId(-1);
			return Proyecto;
		}
	}


	public List<Proyecto> findAllOrderedByFechaRegistro() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Proyecto> criteria = cb.createQuery(Proyecto.class);
		Root<Proyecto> presentacion = criteria.from(Proyecto.class);
		criteria.select(presentacion).orderBy(
				cb.desc(presentacion.get("fechaRegistro")));
		return em.createQuery(criteria).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Proyecto> findAllProyectoForDescription(String criterio) {
		try {
			String query = "select ser from Proyecto ser where ser.nombre like '%"
					+ criterio + "%'";
			System.out.println("Consulta: " + query);
			List<Proyecto> listaProyecto = em.createQuery(query).getResultList();
			return listaProyecto;
		} catch (Exception e) {
			System.out.println("Error en findAllProyectoForDescription: "
					+ e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Proyecto> traerProyectoActivas(Gestion gestion) {
		try {
			String query = "select ser from Proyecto ser where ser.estado='AC'  and ser.gestion.id="+gestion.getId()+"  order by ser.nombre asc";
			System.out.println("Consulta traerProyectoActivas: " + query);
			List<Proyecto> listaProyecto = em.createQuery(query).getResultList();
			return listaProyecto;
		} catch (Exception e) {
			System.out.println("Error en traerProyectoActivas: "
					+ e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Proyecto> findAll100UltimosProyecto(Gestion gestion) {
		try {
			String query = "select ser from Proyecto ser where  ser.gestion.id="+gestion.getId()+"  order by ser.fechaRegistro desc";
			System.out.println("Consulta: " + query);
			List<Proyecto> listaProyecto = em.createQuery(query)
					.setMaxResults(100).getResultList();
			return listaProyecto;
		} catch (Exception e) {
			System.out.println("Error en findAll100UltimosProyecto: "
					+ e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Proyecto> findAllProyectoForQueryNombre(String criterio,Gestion gestion) {
		try {
			String query = "select ser from Proyecto ser where ser.nombre like '%"
					+ criterio + "%' and ser.estado='AC'  and ser.gestion.id="+gestion.getId()+"  order by ser.nombre asc";
			System.out.println("Consulta: " + query);
			List<Proyecto> listaProyecto = em.createQuery(query).getResultList();
			return listaProyecto;
		} catch (Exception e) {
			System.out.println("Error en findAllProyectoForDescription: "
					+ e.getMessage());
			return null;
		}
	}
	
	@SuppressWarnings("unused")
	public List<Proyecto> findAllProyectoForQueryCodigo(String codigo,Gestion gestion){
		String query = "select ser from Proyecto ser where ser.codigo like '%" + codigo + "%' and ser.estado='AC'  and ser.gestion.id="+gestion.getId()+"  order by ser.nombre asc";
		List<Proyecto> listaProyecto = em.createQuery(query).getResultList();
		return listaProyecto;
	}

}
