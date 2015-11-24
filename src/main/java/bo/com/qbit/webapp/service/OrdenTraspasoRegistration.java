package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.OrdenTraspaso;


//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class OrdenTraspasoRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<OrdenTraspaso> PartidaEventSrc;
    
    public OrdenTraspaso register(OrdenTraspaso ordenTraspaso) throws Exception {
        log.info("Registering ordenTraspaso " );
        this.em.persist(ordenTraspaso);
        this.em.flush();
        this.em.refresh(ordenTraspaso);
        PartidaEventSrc.fire(ordenTraspaso);
        return ordenTraspaso;
    }
    
    public void updated(OrdenTraspaso ordenTraspaso) throws Exception {
    	log.info("Updated ordenTraspaso ");
        em.merge(ordenTraspaso);
        PartidaEventSrc.fire(ordenTraspaso);
    }
    
    public void remover(OrdenTraspaso ordenTraspaso){
    	log.info("Remover ordenTraspaso ");
    	ordenTraspaso.setEstado("RM");
        em.merge(ordenTraspaso);
        PartidaEventSrc.fire(ordenTraspaso);
    }
	
}
