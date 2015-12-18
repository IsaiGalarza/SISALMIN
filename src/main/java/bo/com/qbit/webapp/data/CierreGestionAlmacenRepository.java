package bo.com.qbit.webapp.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.CierreGestionAlmacen;
import bo.com.qbit.webapp.model.Gestion;

@Stateless
public class CierreGestionAlmacenRepository {

	@Inject
	private EntityManager em;

	public CierreGestionAlmacen findById(int id) {
		return em.find(CierreGestionAlmacen.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<CierreGestionAlmacen> findAllOrderedByID() {
		String query = "select ser from CierreGestionAlmacen ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		System.out.println("Query CierreGestionAlmacen: " + query);
		return em.createQuery(query).getResultList();
	}

	//veerifica si el almacen fue cerrado
	public CierreGestionAlmacen finAlmacenGestionCerrado(Almacen almacen ,Gestion  gestion){
		try{
			String query = "select ser from CierreGestionAlmacen ser where ser.almacen.id="+almacen.getId()+" and ser.gestion.id="+gestion.getId();
			System.out.println("Query isAlmacenGestionCerrado: " + query);
			return (CierreGestionAlmacen) em.createQuery(query).getSingleResult();
		}catch(Exception e){
			System.out.println(" isAlmacenGestionCerrado ERROR: " + e.getMessage());
			return null;
		}
	}


}
