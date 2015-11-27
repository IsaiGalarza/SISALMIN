package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import bo.com.qbit.webapp.model.DetalleOrdenSalida;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class DetalleOrdenSalidaRegistration {
	
	//Test
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<DetalleOrdenSalida> PartidaEventSrc;
    
    public void register(DetalleOrdenSalida ordenSalida) throws Exception {
        log.info("Registering ordenSalida " );
        em.persist(ordenSalida);
        PartidaEventSrc.fire(ordenSalida);
    }
    
    public void updated(DetalleOrdenSalida ordenSalida) throws Exception {
    	log.info("Updated ordenSalida ");
        em.merge(ordenSalida);
        PartidaEventSrc.fire(ordenSalida);
    }
    
    public void remover(DetalleOrdenSalida ordenSalida){
    	log.info("Remover Partida ");
    	ordenSalida.setEstado("RM");
        em.merge(ordenSalida);
        PartidaEventSrc.fire(ordenSalida);
    }
	
}
