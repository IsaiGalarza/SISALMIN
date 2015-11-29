package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.DetalleTomaInventario;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class DetalleTomaInventarioRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<DetalleTomaInventario> tomaInventarioEventSrc;
    
    public DetalleTomaInventario register(DetalleTomaInventario tomaInventario) throws Exception {
        log.info("Registering DetalleTomaInventario " );
        this.em.persist(tomaInventario);
        this.em.flush();
        this.em.refresh(tomaInventario);
        tomaInventarioEventSrc.fire(tomaInventario);
        return tomaInventario;
    }
    
    public void updated(DetalleTomaInventario almacen) throws Exception {
    	log.info("Updated DetalleTomaInventario ");
        em.merge(almacen);
        tomaInventarioEventSrc.fire(almacen);
    }
    
    public void remover(DetalleTomaInventario almacen){
    	log.info("Remover DetalleTomaInventario ");
    	almacen.setEstado("RM");
        em.merge(almacen);
        tomaInventarioEventSrc.fire(almacen);
    }
	
}
