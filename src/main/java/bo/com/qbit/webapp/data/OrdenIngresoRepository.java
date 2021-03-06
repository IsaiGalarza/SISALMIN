package bo.com.qbit.webapp.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.OrdenIngreso;

@ApplicationScoped
public class OrdenIngresoRepository {

	@Inject
	private EntityManager em;

	public OrdenIngreso findById(int id) {
		return em.find(OrdenIngreso.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<OrdenIngreso> findAllOrderedByID() {
		String query = "select ser from OrdenIngreso ser where ser.estado='AC' or ser.estado='IN' or ser.estado='PR' order by ser.id desc";
		System.out.println("Query OrdenIngreso: " + query);
		return em.createQuery(query).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<OrdenIngreso> findAllOrderedByIDGestion(Gestion gestion) {
		String query = "select ser from OrdenIngreso ser where ( ser.estado='AC' or ser.estado='PR' or ser.estado='AN') and ser.gestion.id="+gestion.getId()+" order by ser.id desc";
		System.out.println("Query OrdenIngreso: " + query);
		return em.createQuery(query).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public int obtenerNumeroOrdenIngreso( Gestion gestion){
		//Integer year = Integer.parseInt( new SimpleDateFormat("yyyy").format(date));
		String query = "select em from OrdenIngreso em where (em.estado='AC' or em.estado='IN' or em.estado='PR') and em.gestion.id="+gestion.getId()+"  order by em.id asc";
		System.out.println("Query OrdenIngreso: "+query);
		List<OrdenIngreso> list = em.createQuery(query).getResultList();
		OrdenIngreso orden = list.size()>0?list.get(list.size()-1):null;
		return orden==null? 1 :Integer.valueOf(orden.getCorrelativo())+ 1;
	}

	public double contarOrdenesActivas(Gestion gestion){
		String query = "select count(em) from OrdenIngreso em where em.estado='AC' and em.gestion.id="+gestion.getId();
		System.out.println("Query contarOrdenesActivas: "+query);
		return (Long)em.createQuery(query).getSingleResult();
	}
	
	public double contarOrdenesProcesadas(Gestion gestion){
		String query = "select count(em) from OrdenIngreso em where em.estado='PR' and em.gestion.id="+gestion.getId();
		System.out.println("Query contarOrdenesProcesadas: "+query);
		return (Long)em.createQuery(query).getSingleResult();
	}

	public double contarOrdenesTotales(Gestion gestion){
		String query = "select count(em) from OrdenIngreso em where (em.estado='AC' or em.estado='PR') and em.gestion.id="+gestion.getId();
		System.out.println("Query contarOrdenesTotales: "+query);
		return (Long)em.createQuery(query).getSingleResult();
	}

}
