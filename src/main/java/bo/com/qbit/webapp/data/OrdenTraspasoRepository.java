package bo.com.qbit.webapp.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.OrdenIngreso;
import bo.com.qbit.webapp.model.OrdenTraspaso;

@ApplicationScoped
public class OrdenTraspasoRepository {

	@Inject
	private EntityManager em;

	public OrdenTraspaso findById(int id) {
		return em.find(OrdenTraspaso.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public int obtenerNumeroOrdenTraspaso(Gestion gestion){
		String query = "select em from OrdenTraspaso em where (em.estado='AC' or em.estado='IN' or em.estado='PR') and em.gestion.id="+gestion.getId()+" order by em.id asc";
		System.out.println("Query OrdenTraspaso: "+query);
		List<OrdenTraspaso> list = em.createQuery(query).getResultList();
		OrdenTraspaso orden = list.size()>0?list.get(list.size()-1):null;
		return orden==null? 1 :Integer.valueOf(orden.getCorrelativo())+ 1;
	}


	@SuppressWarnings("unchecked")
	public List<OrdenTraspaso> findAllOrderedByID() {
		String query = "select ser from OrdenTraspaso ser where ser.estado='AC' or ser.estado='IN' or ser.estado='PR' order by ser.id desc";
		System.out.println("Query OrdenIngreso: " + query);
		return em.createQuery(query).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<OrdenTraspaso> findAllOrderedByIDGestion(Gestion gestion) {
		String query = "select ser from OrdenTraspaso ser where ( ser.estado='AC' or ser.estado='IN' or ser.estado='PR') and ser.gestion.id="+gestion.getId()+" order by ser.id desc";
		System.out.println("Query OrdenIngreso: " + query);
		return em.createQuery(query).getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	public int obtenerNumeroOrdenIngreso(Date date, Gestion gestion){
		Integer year = Integer.parseInt( new SimpleDateFormat("yyyy").format(date));
		String query = "select em from OrdenTraspaso em where (em.estado='AC' or em.estado='IN' or em.estado='PR') and em.gestion.id="+gestion.getId()+" and date_part('year', em.fechaDocumento) ="+year;
		System.out.println("Query Comprobante: "+query);
		return (( List<OrdenTraspaso>)em.createQuery(query).getResultList()).size() + 1;
	}
	
	public double contarOrdenesActivas(Gestion gestion){
		String query = "select count(em) from OrdenTraspaso em where em.estado='AC' and em.gestion.id="+gestion.getId();
		System.out.println("Query contarOrdenesActivas: "+query);
		return (Long)em.createQuery(query).getSingleResult();
	}

	public double contarOrdenesProcesadas(Gestion gestion){
		String query = "select count(em) from OrdenTraspaso em where em.estado='PR' and em.gestion.id="+gestion.getId();
		System.out.println("Query contarOrdenesActivas: "+query);
		return (Long)em.createQuery(query).getSingleResult();
	}
	
	public double contarOrdenesTotales(Gestion gestion){
		String query = "select count(em) from OrdenTraspaso em where (em.estado='AC' or em.estado='PR') and em.gestion.id="+gestion.getId();
		System.out.println("Query contarOrdenesTotales: "+query);
		return (Long)em.createQuery(query).getSingleResult();
	}

}
