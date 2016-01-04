package bo.com.qbit.webapp.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import bo.com.qbit.webapp.model.Partida;

@ApplicationScoped
public class PartidaRepository {

	@Inject
	private EntityManager em;

	public Partida findById(int id) {
		return em.find(Partida.class, id);
	}

	public List<Partida> findAllOrderedByDescripcion() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Partida> criteria = cb.createQuery(Partida.class);
		Root<Partida> presentacion = criteria.from(Partida.class);
		criteria.select(presentacion).orderBy(
				cb.desc(presentacion.get("descripcion")));
		return em.createQuery(criteria).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Partida> findAllPartidaByID() {
		String query = "select ser from Partida ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		System.out.println("Query Partida: " + query);
		return em.createQuery(query).getResultList();
	}

	public Partida findByCodigo(String codigo) {
		try{
			String query = "select ser from Partida ser where (ser.estado='AC' or ser.estado='IN') and upper(ser.codigo)='"+ codigo+"'";
			System.out.println("Query Partida: " + query);
			return (Partida) em.createQuery(query).getSingleResult();
		}catch(Exception e){
			System.out.println("Error: " + e.getMessage());
			return null;
		}
	}


	public List<Partida> findAllPartidaByFechaRegistro() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Partida> criteria = cb.createQuery(Partida.class);
		Root<Partida> presentacion = criteria.from(Partida.class);
		criteria.select(presentacion).orderBy(
				cb.desc(presentacion.get("fechaRegistro")));
		return em.createQuery(criteria).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Partida> findAllPartidaForDescription(String criterio) {
		try {
			String query = "select ser from Partida ser where ser.nombre like '%"
					+ criterio + "%' and ser.estado='AC' order by ser.nombre asc";
			System.out.println("Consulta: " + query);
			List<Partida> listaPartida = em.createQuery(query).getResultList();
			return listaPartida;
		} catch (Exception e) {
			System.out.println("Error en findAllPartidaForDescription: "
					+ e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Partida> traerPartidaActivas() {
		try {
			String query = "select ser from Partida ser where ser.estado='AC' order by ser.nombre asc";
			System.out.println("Consulta traerPartidaActivas: " + query);
			List<Partida> listaPartida = em.createQuery(query).getResultList();
			return listaPartida;
		} catch (Exception e) {
			System.out.println("Error en traerPartidaActivas: "
					+ e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Partida> findAll100UltimosPartida() {
		try {
			String query = "select ser from Partida ser order by ser.fechaRegistro desc";
			System.out.println("Consulta: " + query);
			List<Partida> listaPartida = em.createQuery(query)
					.setMaxResults(100).getResultList();
			return listaPartida;
		} catch (Exception e) {
			System.out.println("Error en findAll100UltimosPartida: "
					+ e.getMessage());
			return null;
		}
	}

}
