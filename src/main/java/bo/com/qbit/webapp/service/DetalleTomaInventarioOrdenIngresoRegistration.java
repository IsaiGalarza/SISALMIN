package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.DetalleTomaInventarioOrdenIngreso;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class DetalleTomaInventarioOrdenIngresoRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<DetalleTomaInventarioOrdenIngreso> PartidaEventSrc;
    
    public void register(DetalleTomaInventarioOrdenIngreso ordenIngreso) throws Exception {
        log.info("Registering DetalleTomaInventarioOrdenIngreso " );
        em.persist(ordenIngreso);
        PartidaEventSrc.fire(ordenIngreso);
    }
    
    public void updated(DetalleTomaInventarioOrdenIngreso ordenIngreso) throws Exception {
    	log.info("Updated DetalleTomaInventarioOrdenIngreso ");
        em.merge(ordenIngreso);
        PartidaEventSrc.fire(ordenIngreso);
    }
    
    public void remover(DetalleTomaInventarioOrdenIngreso ordenIngreso){
    	log.info("Remover DetalleTomaInventarioOrdenIngreso ");
    	ordenIngreso.setEstado("RM");
        em.merge(ordenIngreso);
        PartidaEventSrc.fire(ordenIngreso);
    }
	
}
