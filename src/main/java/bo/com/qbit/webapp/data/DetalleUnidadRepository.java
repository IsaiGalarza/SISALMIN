package bo.com.qbit.webapp.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.DetalleUnidad;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.Usuario;

@ApplicationScoped
public class DetalleUnidadRepository {

	@Inject
	private EntityManager em;

	public DetalleUnidad findById(int id) {
		return em.find(DetalleUnidad.class, id);
	}

//	public List<DetalleUnidad> findAllOrderedByDescripcion() {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<DetalleUnidad> criteria = cb.createQuery(DetalleUnidad.class);
//		Root<DetalleUnidad> presentacion = criteria.from(DetalleUnidad.class);
//		criteria.select(presentacion).orderBy(
//				cb.desc(presentacion.get("descripcion")));
//		return em.createQuery(criteria).getResultList();
//	}

	@SuppressWarnings("unchecked")
	public List<DetalleUnidad> findAllOrderedByID(Gestion gestion) {
		String query = "select ser from DetalleUnidad ser where ser.gestion.id="+gestion.getId()+" and ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		System.out.println("Query DetalleUnidad: " + query);
		return em.createQuery(query).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<DetalleUnidad> findAllActivosOrderedByID(Gestion gestion) {
		String query = "select ser from DetalleUnidad ser where ser.gestion.id="+gestion.getId()+" and ser.estado='AC' order by ser.id desc";
		System.out.println("Query DetalleUnidad: " + query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public DetalleUnidad findDetalleUnidadForUser(Usuario user,Gestion gestion) {
		String query = "select ser from DetalleUnidad ser where (ser.estado='AC' or ser.estado='IN') ser.gestion.id="+gestion.getId()+" and ser.encargado.id="
				+ user.getId() + " order by ser.id desc";
		System.out.println("Query DetalleUnidad: " + query);
		List<DetalleUnidad> listDetalleUnidad = em.createQuery(query).getResultList();
		if (listDetalleUnidad.size() > 0) {
			return listDetalleUnidad.get(0);
		} else {
			DetalleUnidad DetalleUnidad = new DetalleUnidad();
			DetalleUnidad.setId(-1);
			return DetalleUnidad;
		}
	}
	

//	public List<DetalleUnidad> findAllOrderedByFechaRegistro() {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<DetalleUnidad> criteria = cb.createQuery(DetalleUnidad.class);
//		Root<DetalleUnidad> presentacion = criteria.from(DetalleUnidad.class);
//		criteria.select(presentacion).orderBy(
//				cb.desc(presentacion.get("fechaRegistro")));
//		return em.createQuery(criteria).getResultList();
//	}

	@SuppressWarnings("unchecked")
	public List<DetalleUnidad> findAllDetalleUnidadForDescription(String criterio,Gestion gestion) {
		try {
			String query = "select ser from DetalleUnidad ser where ser.gestion.id="+gestion.getId()+" and ser.nombre like '%"
					+ criterio + "%'";
			System.out.println("Consulta: " + query);
			List<DetalleUnidad> listaDetalleUnidad = em.createQuery(query).getResultList();
			return listaDetalleUnidad;
		} catch (Exception e) {
			System.out.println("Error en findAllDetalleUnidadForDescription: "
					+ e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<DetalleUnidad> traerDetalleUnidadActivas(Gestion gestion) {
		try {
			String query = "select ser from DetalleUnidad ser where ser.gestion.id="+gestion.getId()+" and ser.estado='AC' order by ser.nombre asc";
			System.out.println("Consulta traerDetalleUnidadActivas: " + query);
			List<DetalleUnidad> listaDetalleUnidad = em.createQuery(query).getResultList();
			return listaDetalleUnidad;
		} catch (Exception e) {
			System.out.println("Error en traerDetalleUnidadActivas: "
					+ e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<DetalleUnidad> findAll100UltimosDetalleUnidad(Gestion gestion) {
		try {
			String query = "select ser from DetalleUnidad ser where ser.gestion.id="+gestion.getId()+" order by ser.fechaRegistro desc";
			System.out.println("Consulta: " + query);
			List<DetalleUnidad> listaDetalleUnidad = em.createQuery(query)
					.setMaxResults(100).getResultList();
			return listaDetalleUnidad;
		} catch (Exception e) {
			System.out.println("Error en findAll100UltimosDetalleUnidad: "
					+ e.getMessage());
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<DetalleUnidad> findAllDetalleUnidadForQueryNombre(String criterio,Gestion gestion) {
		try {//translate (ser.nombre, 'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜñ', 'aeiouAEIOUaeiouAEIOUÑ') 
			String query = "select ser from DetalleUnidad ser where ser.gestion.id="+gestion.getId()+" and upper(translate (ser.nombre, 'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜñ', 'aeiouAEIOUaeiouAEIOUÑ')) like '%"
					+ criterio + "%' and ser.estado='AC' order by ser.nombre asc";
			System.out.println("Consulta: " + query);
			List<DetalleUnidad> listaDetalleUnidad = em.createQuery(query).getResultList();
			return listaDetalleUnidad;
		} catch (Exception e) {
			System.out.println("Error en findAllPartidaForDescription: "
					+ e.getMessage());
			return null;
		}
	}

}
