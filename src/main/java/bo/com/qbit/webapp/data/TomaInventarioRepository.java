package bo.com.qbit.webapp.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.TomaInventario;

@ApplicationScoped
public class TomaInventarioRepository {

	@Inject
	private EntityManager em;

	public TomaInventario findById(int id) {
		return em.find(TomaInventario.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<TomaInventario> findAllOrderedByID() {
		String query = "select ser from TomaInventario ser where (ser.estado='AC' or ser.estado='IN' or ser.estado='RE' or ser.estado='PR' or ser.estado='CN' or ser.estado='CE') order by ser.id desc";
		System.out.println("Query TomaInventario: " + query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<TomaInventario> findAllOrderedByIDGestion(Gestion gestion) {
		String query = "select ser from TomaInventario ser where (ser.estado='AC' or ser.estado='IN' or ser.estado='RE' or ser.estado='PR' or ser.estado='CN' or ser.estado='CE') and ser.gestion.id="+gestion.getId()+" order by ser.id desc";
		System.out.println("Query TomaInventario: " + query);
		return em.createQuery(query).getResultList();
	}

	public boolean findTIInicialByGestion(Gestion gestion) {
		try{
			String query = "select ser from TomaInventario ser where ser.estado='AC' and ser.tipo='INICIAL' and ser.gestion.id="+gestion.getId();
			System.out.println("Query TomaInventario: " + query);
			TomaInventario object =  (TomaInventario) em.createQuery(query).getSingleResult();
			return object!=null?true:false;
		}catch(Exception e){
			System.out.println("findTIInicialByGestion() Error "+e.getMessage());
			return false;
		}
	}

}
