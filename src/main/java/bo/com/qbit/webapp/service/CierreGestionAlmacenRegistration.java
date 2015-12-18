package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.CierreGestionAlmacen;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class CierreGestionAlmacenRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<CierreGestionAlmacen> almacenEventSrc;
    
    public CierreGestionAlmacen register(CierreGestionAlmacen almacen) throws Exception {
        log.info("Registering CierreGestionAlmacen: ");
        em.persist(almacen);
        this.em.flush();
        this.em.refresh(almacen);
        almacenEventSrc.fire(almacen);
        return almacen;
    }
    
    public void updated(CierreGestionAlmacen almacen) throws Exception {
    	log.info("Updated CierreGestionAlmacen: " );
        em.merge(almacen);
        almacenEventSrc.fire(almacen);
    }
    
    public void remover(CierreGestionAlmacen almacen){
    	log.info("Remover CierreGestionAlmacen: ");
    	almacen.setEstado("RM");
        em.merge(almacen);
        almacenEventSrc.fire(almacen);
    }
	
}
