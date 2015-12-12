package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.DetalleProducto;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class DetalleProductoRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<DetalleProducto> almacenEventSrc;
    
    public void register(DetalleProducto detalleProducto) throws Exception {
        log.info("Registering DetalleProducto ");
        em.persist(detalleProducto);
        almacenEventSrc.fire(detalleProducto);
    }
    
    public void updated(DetalleProducto detalleProducto) throws Exception {
    	log.info("Updated DetalleProducto ");
        em.merge(detalleProducto);
        almacenEventSrc.fire(detalleProducto);
    }
    
    public void remover(DetalleProducto detalleProducto){
    	log.info("Remover DetalleProducto ");
    	detalleProducto.setEstado("RM");
        em.merge(detalleProducto);
        almacenEventSrc.fire(detalleProducto);
    }
	
}
