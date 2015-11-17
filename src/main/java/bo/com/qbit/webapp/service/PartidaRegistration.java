package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Partida;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class PartidaRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Partida> PartidaEventSrc;
    
    public void register(Partida partida) throws Exception {
        log.info("Registering Partida: " + partida.getNombre());
        em.persist(partida);
        PartidaEventSrc.fire(partida);
    }
    
    public void updated(Partida partida) throws Exception {
    	log.info("Updated Partida: " + partida.getNombre());
        em.merge(partida);
        PartidaEventSrc.fire(partida);
    }
    
    public void remover(Partida partida){
    	log.info("Remover Partida: " + partida.getNombre());
    	partida.setEstado("RM");
        em.merge(partida);
        PartidaEventSrc.fire(partida);
    }
	
}
