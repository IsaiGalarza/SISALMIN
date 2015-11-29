package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.TomaInventario;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class TomaInventarioRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<TomaInventario> tomaInventarioEventSrc;
    
    public TomaInventario register(TomaInventario tomaInventario) throws Exception {
        log.info("Registering ordenIngreso " );
        this.em.persist(tomaInventario);
        this.em.flush();
        this.em.refresh(tomaInventario);
        tomaInventarioEventSrc.fire(tomaInventario);
        return tomaInventario;
    }
    
    public void updated(TomaInventario almacen) throws Exception {
    	log.info("Updated TomaInventario ");
        em.merge(almacen);
        tomaInventarioEventSrc.fire(almacen);
    }
    
    public void remover(TomaInventario almacen){
    	log.info("Remover TomaInventario ");
    	almacen.setEstado("RM");
        em.merge(almacen);
        tomaInventarioEventSrc.fire(almacen);
    }
	
}
