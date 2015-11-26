package bo.com.qbit.webapp.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import bo.com.qbit.webapp.model.Partida;
import bo.com.qbit.webapp.model.Producto;

@ApplicationScoped
public class ProductoRepository {
//test
	@Inject
	private EntityManager em;

	public Producto findById(int id) {
		return em.find(Producto.class, id);
	}

	public List<Producto> findAllOrderedByDescripcion() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Producto> criteria = cb.createQuery(Producto.class);
		Root<Producto> presentacion = criteria.from(Producto.class);
		criteria.select(presentacion).orderBy(
				cb.desc(presentacion.get("descripcion")));
		return em.createQuery(criteria).getResultList();
	}

	public List<Producto> findAllProductoActivosByID() {
		String query = "select ser from Producto ser where ser.estado='AC' order by ser.id desc";
		System.out.println("Query Producto: " + query);
		return em.createQuery(query).getResultList();
	}
	
	public List<Producto> findAllProductoForQueryNombre(String criterio) {
		try {
			String query = "select ser from Producto ser where ser.nombre like '%"
					+ criterio + "%' and ser.estado='AC' order by ser.nombre asc";
			System.out.println("Consulta: " + query);
			List<Producto> listaProducto = em.createQuery(query).getResultList();
			return listaProducto;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error en findAllPartidaForDescription: "
					+ e.getMessage());
			return null;
		}
	}
	
	public List<Producto> findAllProductoByID() {
		String query = "select ser from Producto ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		System.out.println("Query Producto: " + query);
		return em.createQuery(query).getResultList();
	}
	
	public List<Producto> findAllProductoForPartidaID(int partidaID) {
		try {
			String query = "select pro from Producto pro where pro.estado='AC' and pro.partida.id="+partidaID+" order by pro.id desc";
			System.out.println("Query findAllProductoForPartidaID: " + query);
			return em.createQuery(query).getResultList();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}


	public List<Producto> findAllProductoByFechaRegistro() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Producto> criteria = cb.createQuery(Producto.class);
		Root<Producto> presentacion = criteria.from(Producto.class);
		criteria.select(presentacion).orderBy(
				cb.desc(presentacion.get("fechaRegistro")));
		return em.createQuery(criteria).getResultList();
	}

	public List<Producto> findAllProductoForDescription(String criterio) {
		try {
			String query = "select ser from Producto ser where ser.nombre like '%"
					+ criterio + "%'";
			System.out.println("Consulta: " + query);
			List<Producto> listaProducto = em.createQuery(query).getResultList();
			return listaProducto;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error en findAllProductoForDescription: "
					+ e.getMessage());
			return null;
		}
	}

	public List<Producto> traerProductoActivas() {
		try {
			String query = "select ser from Producto ser where ser.estado='AC' order by ser.id desc";
			System.out.println("Consulta traerProductoActivas: " + query);
			List<Producto> listaProducto = em.createQuery(query).getResultList();
			return listaProducto;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error en traerProductoActivas: "
					+ e.getMessage());
			return null;
		}
	}

	public List<Producto> findAll100UltimosProducto() {
		try {
			String query = "select ser from Producto ser order by ser.fechaRegistro desc";
			System.out.println("Consulta: " + query);
			List<Producto> listaProducto = em.createQuery(query)
					.setMaxResults(100).getResultList();
			return listaProducto;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error en findAll100UltimosProducto: "
					+ e.getMessage());
			return null;
		}
	}

}
