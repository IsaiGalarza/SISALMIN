package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.BajaProducto;
//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class BajaProductoRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<BajaProducto> FuncionarioEventSrc;
    
    public void register(BajaProducto bajaProducto) throws Exception {
        log.info("Registering BajaProducto ");
        em.persist(bajaProducto);
        FuncionarioEventSrc.fire(bajaProducto);
    }
    
    public void updated(BajaProducto bajaProducto) throws Exception {
    	log.info("Updated BajaProducto ");
        em.merge(bajaProducto);
        FuncionarioEventSrc.fire(bajaProducto);
    }
    
    public void remover(BajaProducto bajaProducto){
    	log.info("Remover BajaProducto " );
    	bajaProducto.setEstado("RM");
        em.merge(bajaProducto);
        FuncionarioEventSrc.fire(bajaProducto);
    }
	
}
