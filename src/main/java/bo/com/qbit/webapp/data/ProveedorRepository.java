package bo.com.qbit.webapp.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.Proveedor;

@Stateless
public class ProveedorRepository {

	@Inject
	private EntityManager em;

	public Proveedor findById(int id) {
		return em.find(Proveedor.class, id);
	}

	public Proveedor findByNombreAndEmpresa(String nombre, Empresa empresa) {
		String query = "select em from Proveedor em  where em.nombre='"+nombre+"' and em.empresa.id="+empresa.getId();
		System.out.println("Query Proveedor: "+query);
		return (Proveedor) em.createQuery(query).getSingleResult();
	}


	@SuppressWarnings("unchecked")
	public List<Proveedor> findAllOrderedByID() {
		String query = "select em from Proveedor em ";// where em.estado='AC' or em.estado='IN' order by em.id desc";
		System.out.println("Query Proveedor: "+query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Proveedor> findAllByEmpresa(Empresa empresa) {
		String query = "select em from Proveedor em where (em.estado='AC' or em.estado='IN') and em.empresa.id="+empresa.getId()+" order by em.id desc";
		System.out.println("Query Proveedor: "+query);
		return em.createQuery(query).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Proveedor> findAllByEmpresa() {
		String query = "select em from Proveedor em where (em.estado='AC' or em.estado='IN')  order by em.id desc";
		System.out.println("Query Proveedor: "+query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Proveedor> findAllActivasByEmpresa(Empresa empresa) {
		String query = "select em from Proveedor em where em.estado='AC' and em.empresa.id="+empresa.getId()+" order by em.id desc";
		System.out.println("Query Proveedor: "+query);
		return em.createQuery(query).getResultList();
	}




}
