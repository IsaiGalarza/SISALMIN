package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.DetalleUnidad;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class DetalleUnidadRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<DetalleUnidad> DetalleUnidadEventSrc;
    
    public void register(DetalleUnidad DetalleUnidad) throws Exception {
        log.info("Registering DetalleUnidad: " + DetalleUnidad.getNombre());
        em.persist(DetalleUnidad);
        DetalleUnidadEventSrc.fire(DetalleUnidad);
    }
    
    public void updated(DetalleUnidad DetalleUnidad) throws Exception {
    	log.info("Updated DetalleUnidad: " + DetalleUnidad.getNombre());
        em.merge(DetalleUnidad);
        DetalleUnidadEventSrc.fire(DetalleUnidad);
    }
    
    public void remover(DetalleUnidad DetalleUnidad){
    	log.info("Remover DetalleUnidad: " + DetalleUnidad.getNombre());
    	DetalleUnidad.setEstado("RM");
        em.merge(DetalleUnidad);
        DetalleUnidadEventSrc.fire(DetalleUnidad);
    }
	
}
