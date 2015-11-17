package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Funcionario;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class FuncionarioRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Funcionario> FuncionarioEventSrc;
    
    public void register(Funcionario Funcionario) throws Exception {
        log.info("Registering Funcionario: " + Funcionario.getNombre());
        em.persist(Funcionario);
        FuncionarioEventSrc.fire(Funcionario);
    }
    
    public void updated(Funcionario Funcionario) throws Exception {
    	log.info("Updated Funcionario: " + Funcionario.getNombre());
        em.merge(Funcionario);
        FuncionarioEventSrc.fire(Funcionario);
    }
    
    public void remover(Funcionario Funcionario){
    	log.info("Remover Funcionario: " + Funcionario.getNombre());
    	Funcionario.setEstado("RM");
        em.merge(Funcionario);
        FuncionarioEventSrc.fire(Funcionario);
    }
	
}
