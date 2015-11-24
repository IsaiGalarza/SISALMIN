package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.DetalleOrdenSalida;
import bo.com.qbit.webapp.model.OrdenSalida;


//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class OrdenSalidaRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<OrdenSalida> OrdenSalidaEventSrc;
    
    public OrdenSalida register(OrdenSalida OrdenSalida) throws Exception {
        log.info("Registering OrdenSalida " );
        this.em.persist(OrdenSalida);
        this.em.flush();
        this.em.refresh(OrdenSalida);
        OrdenSalidaEventSrc.fire(OrdenSalida);
        return OrdenSalida;
    }
    
    public void updated(OrdenSalida OrdenSalida) throws Exception {
    	log.info("Updated OrdenSalida ");
        em.merge(OrdenSalida);
        OrdenSalidaEventSrc.fire(OrdenSalida);
    }
    
    public void remover(OrdenSalida OrdenSalida){
    	log.info("Remover OrdenSalida ");
    	OrdenSalida.setEstado("RM");
        em.merge(OrdenSalida);
        OrdenSalidaEventSrc.fire(OrdenSalida);
    }
	
}
