package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Proyecto;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class ProyectoRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Proyecto> ProyectoEventSrc;
    
    public void register(Proyecto Proyecto) throws Exception {
        log.info("Registering Proyecto: " + Proyecto.getNombre());
        em.persist(Proyecto);
        ProyectoEventSrc.fire(Proyecto);
    }
    
    public void updated(Proyecto Proyecto) throws Exception {
    	log.info("Updated Proyecto: " + Proyecto.getNombre());
        em.merge(Proyecto);
        ProyectoEventSrc.fire(Proyecto);
    }
    
    public void remover(Proyecto Proyecto){
    	log.info("Remover Proyecto: " + Proyecto.getNombre());
    	Proyecto.setEstado("RM");
        em.merge(Proyecto);
        ProyectoEventSrc.fire(Proyecto);
    }
	
}
