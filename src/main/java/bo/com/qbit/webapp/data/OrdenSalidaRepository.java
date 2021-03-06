package bo.com.qbit.webapp.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.DetalleOrdenSalida;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.OrdenIngreso;
import bo.com.qbit.webapp.model.OrdenSalida;

@ApplicationScoped
public class OrdenSalidaRepository {

	@Inject
	private EntityManager em;

	public OrdenSalida findById(int id) {
		return em.find(OrdenSalida.class, id);
	}

	@SuppressWarnings("unchecked")
	public int obtenerNumeroOrdenSalida( Gestion gestion){
		String query = "select em from OrdenSalida em where (em.estado='AC' or em.estado='IN' or em.estado='PR') and em.gestion.id="+gestion.getId()+" order by em.id asc";
		System.out.println("Query OrdenSalida: "+query);
		List<OrdenSalida> list = em.createQuery(query).getResultList();
		OrdenSalida orden = list.size()>0?list.get(list.size()-1):null;
		return orden==null? 1 :Integer.valueOf(orden.getCorrelativo())+ 1;
	}

	@SuppressWarnings("unchecked")
	public List<OrdenSalida> findAllOrderedByID() {
		String query = "select ser from OrdenSalida ser where ser.estado='AC' or ser.estado='PR' order by ser.id desc";
		System.out.println("Query findAllOrderedByID: " + query);
		return em.createQuery(query).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<OrdenSalida> findAllOrderedByIDGestion(Gestion gestion) {
		String query = "select ser from OrdenSalida ser where ( ser.estado='AC' or ser.estado='PR' or ser.estado='AN') and ser.gestion.id="+gestion.getId()+" order by ser.id desc";
		System.out.println("Query findAllOrderedByID: " + query);
		return em.createQuery(query).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<OrdenSalida> findByFechas(Date fechaInicial,Date fechaFinal) {
		String query = "select os from DetalleOrdenSalida em,OrdenSalida os where em.ordenSalida.id=os.id and os.estado='PR' and os.fechaAprobacion>=:stDate and os.fechaAprobacion<=:edDate order by os.id desc";
		System.out.println("Query findByFechas: " + query);
		return em.createQuery(query).setParameter("stDate", fechaInicial).setParameter("edDate", fechaFinal).getResultList();
	}
	
	public double contarOrdenesActivas(Gestion gestion){
		String query = "select count(em) from OrdenSalida em where em.estado='AC' and em.gestion.id="+gestion.getId();
		System.out.println("Query contarOrdenesActivas: "+query);
		return (Long)em.createQuery(query).getSingleResult();
	}

	public double contarOrdenesProcesadas(Gestion gestion){
		String query = "select count(em) from OrdenSalida em where em.estado='PR' and em.gestion.id="+gestion.getId();
		System.out.println("Query contarOrdenesProcesadas: "+query);
		return (Long)em.createQuery(query).getSingleResult();
	}
	
	public double contarOrdenesTotales(Gestion gestion){
		String query = "select count(em) from OrdenSalida em where (em.estado='AC' or em.estado='PR') and em.gestion.id="+gestion.getId();
		System.out.println("Query contarOrdenesTotales: "+query);
		return (Long)em.createQuery(query).getSingleResult();
	}

}
