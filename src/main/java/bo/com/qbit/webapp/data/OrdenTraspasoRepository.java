package bo.com.qbit.webapp.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.OrdenTraspaso;

@ApplicationScoped
public class OrdenTraspasoRepository {

	@Inject
	private EntityManager em;

	public OrdenTraspaso findById(int id) {
		return em.find(OrdenTraspaso.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<OrdenTraspaso> findAllOrderedByID() {
		String query = "select ser from OrdenTraspaso ser where ser.estado='AC' or ser.estado='IN' or ser.estado='PR' order by ser.id desc";
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

}
