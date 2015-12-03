package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.DetalleOrdenIngreso;
import bo.com.qbit.webapp.model.OrdenIngreso;


//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class OrdenIngresoRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<OrdenIngreso> PartidaEventSrc;
    
    public OrdenIngreso register(OrdenIngreso ordenIngreso) throws Exception {
        log.info("Registering ordenIngreso " );
        this.em.persist(ordenIngreso);
        this.em.flush();
        this.em.refresh(ordenIngreso);
        PartidaEventSrc.fire(ordenIngreso);
        return ordenIngreso;
    }
    
    public void updated(OrdenIngreso ordenIngreso) throws Exception {
    	log.info("Updated ordenIngreso ");
        em.merge(ordenIngreso);
        PartidaEventSrc.fire(ordenIngreso);
    }
    
    public void remover(OrdenIngreso ordenIngreso) throws Exception{
    	log.info("Remover OrdenIngreso ");
    	ordenIngreso.setEstado("RM");
        em.merge(ordenIngreso);
        PartidaEventSrc.fire(ordenIngreso);
    }
	
}
