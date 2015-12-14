package bo.com.qbit.webapp.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Producto;
import bo.com.qbit.webapp.model.Proveedor;

@Stateless
public class ProveedorRepository {

	@Inject
	private EntityManager em;

	public Proveedor findById(int id) {
		return em.find(Proveedor.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Proveedor> findAllOrderedByID() {
		String query = "select em from Proveedor em ";// where em.estado='AC' or em.estado='IN' order by em.id desc";
		System.out.println("Query Proveedor: "+query);
		return em.createQuery(query).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Proveedor> findAllActivoOrderedByID() {
		String query = "select em from Proveedor em where em.estado='AC' order by em.id desc";
		System.out.println("Query Proveedor: "+query);
		return em.createQuery(query).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Proveedor> findAllProveedorForQueryNombre(String criterio) {
		try {
			String query = "select ser from Proveedor ser where ser.nombre like '%"
					+ criterio + "%' and ser.estado='AC' order by ser.nombre asc";
			System.out.println("Consulta: " + query);
			List<Proveedor> listaProveedor= em.createQuery(query).getResultList();
			return listaProveedor;
		} catch (Exception e) {
			System.out.println("Error en findAllProveedorForQueryNombre: "
					+ e.getMessage());
			return null;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<Proveedor> findAllByEmpresa() {
		String query = "select em from Proveedor em where (em.estado='AC' or em.estado='IN')  order by em.id desc";
		System.out.println("Query Proveedor: "+query);
		return em.createQuery(query).getResultList();
	}

}
