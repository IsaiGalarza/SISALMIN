package bo.com.qbit.webapp.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.Usuario;

@ApplicationScoped
public class AlmacenRepository {

	@Inject
	private EntityManager em;

	public Almacen findById(int id) {
		return em.find(Almacen.class, id);
	}

	public List<Almacen> findAllOrderedByDescripcion() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Almacen> criteria = cb.createQuery(Almacen.class);
		Root<Almacen> presentacion = criteria.from(Almacen.class);
		criteria.select(presentacion).orderBy(
				cb.desc(presentacion.get("descripcion")));
		return em.createQuery(criteria).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Almacen> findAllOrderedByID() {
		String query = "select ser from Almacen ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		System.out.println("Query Almacen: " + query);
		return em.createQuery(query).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Almacen> findAllActivosOrderedByID() {
		String query = "select ser from Almacen ser where ser.estado='AC' order by ser.id desc";
		System.out.println("Query Almacen: " + query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public Almacen findAlmacenForUser(Usuario user) {
		String query = "select ser from Almacen ser where (ser.estado='AC' or ser.estado='IN') and ser.encargado.id="
				+ user.getId() + " order by ser.id desc";
		System.out.println("Query Almacen: " + query);
		List<Almacen> listAlmacen = em.createQuery(query).getResultList();
		if (listAlmacen.size() > 0) {
			return listAlmacen.get(0);
		} else {
			Almacen almacen = new Almacen();
			almacen.setId(-1);
			return almacen;
		}
	}

	public Almacen findByCodigo(String codigo) {
		try{
			String query = "select ser from Almacen ser where (ser.estado='AC' or ser.estado='IN') and upper(ser.codigo)='"
					+ codigo + "'";
			System.out.println("Query Almacen: " + query);
			return (Almacen) em.createQuery(query).getSingleResult();
		}catch(Exception e){
			System.out.println("Error : " + e.getMessage());
			return null;
		}
	}


	public List<Almacen> findAllOrderedByFechaRegistro() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Almacen> criteria = cb.createQuery(Almacen.class);
		Root<Almacen> presentacion = criteria.from(Almacen.class);
		criteria.select(presentacion).orderBy(
				cb.desc(presentacion.get("fechaRegistro")));
		return em.createQuery(criteria).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Almacen> findAllAlmacenForDescription(String criterio) {
		try {
			String query = "select ser from Almacen ser where ser.nombre like '%"
					+ criterio + "%'";
			System.out.println("Consulta: " + query);
			List<Almacen> listaAlmacen = em.createQuery(query).getResultList();
			return listaAlmacen;
		} catch (Exception e) {
			System.out.println("Error en findAllAlmacenForDescription: "
					+ e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Almacen> traerAlmacenActivas() {
		try {
			String query = "select ser from Almacen ser where ser.estado='AC' order by ser.nombre asc";
			System.out.println("Consulta traerAlmacenActivas: " + query);
			List<Almacen> listaAlmacen = em.createQuery(query).getResultList();
			return listaAlmacen;
		} catch (Exception e) {
			System.out.println("Error en traerAlmacenActivas: "
					+ e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Almacen> findAll100UltimosAlmacen() {
		try {
			String query = "select ser from Almacen ser order by ser.fechaRegistro desc";
			System.out.println("Consulta: " + query);
			List<Almacen> listaAlmacen = em.createQuery(query)
					.setMaxResults(100).getResultList();
			return listaAlmacen;
		} catch (Exception e) {
			System.out.println("Error en findAll100UltimosAlmacen: "
					+ e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Almacen> findAllAlmacenForQueryNombre(String criterio) {
		try {
			String query = "select ser from Almacen ser where upper(ser.nombre) like '%"
					+ criterio + "%' and ser.estado='AC' order by ser.nombre asc";
			System.out.println("Consulta: " + query);
			List<Almacen> listaAlmacen = em.createQuery(query).getResultList();
			return listaAlmacen;
		} catch (Exception e) {
			System.out.println("Error en findAllAlmacenForDescription: "
					+ e.getMessage());
			return null;
		}
	}

}
