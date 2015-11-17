package bo.com.qbit.webapp.data;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Cotizacion;
import bo.com.qbit.webapp.model.CotizacionServicio;

@Stateless
public class CotizacionServicioRepository {
	 
	@Inject
    private EntityManager em;

    public CotizacionServicio findById(int id) {
        return em.find(CotizacionServicio.class, id);
    }
    
    @SuppressWarnings("unchecked")
	public List<CotizacionServicio> findAllOrderedByID() {
    	String query = "select em from CotizacionServicio em ";// where em.estado='AC' or ser.estado='IN' order by em.id desc";
    	System.out.println("Query CotizacionServicio: "+query);
    	return em.createQuery(query).getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public List<CotizacionServicio> findAllByCotizacion(Cotizacion cotizacion) {
    	String query = "select em from CotizacionServicio em where em.cotizacion.id="+cotizacion.getId();
    	System.out.println("Query CotizacionServicio: "+query);
    	return em.createQuery(query).getResultList();
    }    
    
	
}
