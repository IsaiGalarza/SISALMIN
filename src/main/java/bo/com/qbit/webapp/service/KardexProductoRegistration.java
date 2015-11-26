package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.KardexProducto;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class KardexProductoRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<KardexProducto> FuncionarioEventSrc;
    
    public void register(KardexProducto kardexProducto) throws Exception {
        log.info("Registering KardexProducto ");
        em.persist(kardexProducto);
        FuncionarioEventSrc.fire(kardexProducto);
    }
    
    public void updated(KardexProducto kardexProducto) throws Exception {
    	log.info("Updated Funcionario ");
        em.merge(kardexProducto);
        FuncionarioEventSrc.fire(kardexProducto);
    }
    
    public void remover(KardexProducto kardexProducto){
    	log.info("Remover KardexProducto " );
    	kardexProducto.setEstado("RM");
        em.merge(kardexProducto);
        FuncionarioEventSrc.fire(kardexProducto);
    }
	
}
