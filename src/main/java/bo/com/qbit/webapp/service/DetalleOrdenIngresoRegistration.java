package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.DetalleOrdenIngreso;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class DetalleOrdenIngresoRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<DetalleOrdenIngreso> PartidaEventSrc;
    
    public void register(DetalleOrdenIngreso ordenIngreso) throws Exception {
        log.info("Registering ordenIngreso " );
        em.persist(ordenIngreso);
        PartidaEventSrc.fire(ordenIngreso);
    }
    
    public void updated(DetalleOrdenIngreso ordenIngreso) throws Exception {
    	log.info("Updated ordenIngreso ");
        em.merge(ordenIngreso);
        PartidaEventSrc.fire(ordenIngreso);
    }
    
    public void remover(DetalleOrdenIngreso ordenIngreso){
    	log.info("Remover Partida ");
    	ordenIngreso.setEstado("RM");
        em.merge(ordenIngreso);
        PartidaEventSrc.fire(ordenIngreso);
    }
	
}
