package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.AlmacenProducto;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class AlmacenProductoRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<AlmacenProducto> almacenEventSrc;
    
    public void register(AlmacenProducto almacen) throws Exception {
        log.info("Registering AlmacenProducto ");
        em.persist(almacen);
        almacenEventSrc.fire(almacen);
    }
    
    public void updated(AlmacenProducto almacen) throws Exception {
    	log.info("Updated AlmacenProducto ");
        em.merge(almacen);
        almacenEventSrc.fire(almacen);
    }
    
    public void remover(AlmacenProducto almacen){
    	log.info("Remover AlmacenProducto ");
    	almacen.setEstado("RM");
        em.merge(almacen);
        almacenEventSrc.fire(almacen);
    }
	
}
