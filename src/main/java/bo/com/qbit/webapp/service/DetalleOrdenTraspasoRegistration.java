package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.DetalleOrdenTraspaso;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class DetalleOrdenTraspasoRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<DetalleOrdenTraspaso> PartidaEventSrc;
    
    public DetalleOrdenTraspaso register(DetalleOrdenTraspaso detalleOrdenTraspaso) throws Exception {
        log.info("Registering detalleOrdenTraspaso " );
        em.persist(detalleOrdenTraspaso);
        this.em.flush();
        this.em.refresh(detalleOrdenTraspaso);
        PartidaEventSrc.fire(detalleOrdenTraspaso);
        return detalleOrdenTraspaso;
    }
    
    public void updated(DetalleOrdenTraspaso detalleOrdenTraspaso) throws Exception {
    	log.info("Updated detalleOrdenTraspaso ");
        em.merge(detalleOrdenTraspaso);
        PartidaEventSrc.fire(detalleOrdenTraspaso);
    }
    
    public void remover(DetalleOrdenTraspaso detalleOrdenTraspaso){
    	log.info("Remover detalleOrdenTraspaso ");
    	detalleOrdenTraspaso.setEstado("RM");
        em.merge(detalleOrdenTraspaso);
        PartidaEventSrc.fire(detalleOrdenTraspaso);
    }
	
}
