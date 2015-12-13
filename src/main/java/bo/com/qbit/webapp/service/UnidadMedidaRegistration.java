package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.UnidadMedida;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class UnidadMedidaRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<UnidadMedida> FuncionarioEventSrc;
    
    public void register(UnidadMedida unidadMedida) throws Exception {
        log.info("Registering UnidadMedida ");
        em.persist(unidadMedida);
        FuncionarioEventSrc.fire(unidadMedida);
    }
    
    public void updated(UnidadMedida unidadMedida) throws Exception {
    	log.info("Updated UnidadMedida ");
        em.merge(unidadMedida);
        FuncionarioEventSrc.fire(unidadMedida);
    }
    
    public void remover(UnidadMedida unidadMedida){
    	log.info("Remover UnidadMedida " );
    	unidadMedida.setEstado("RM");
        em.merge(unidadMedida);
        FuncionarioEventSrc.fire(unidadMedida);
    }
	
}
