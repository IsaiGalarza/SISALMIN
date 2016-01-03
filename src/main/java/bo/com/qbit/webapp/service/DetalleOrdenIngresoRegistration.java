package bo.com.qbit.webapp.service;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.DetalleOrdenIngreso;
//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class DetalleOrdenIngresoRegistration {

    @Inject
    private EntityManager em;

    @Inject
    private Event<DetalleOrdenIngreso> PartidaEventSrc;
    
    public DetalleOrdenIngreso register(DetalleOrdenIngreso detalleOrdenIngreso) throws Exception {
        System.out.println("Registering DetalleOrdenIngreso " );
        em.persist(detalleOrdenIngreso);
        this.em.flush();
        this.em.refresh(detalleOrdenIngreso);
        PartidaEventSrc.fire(detalleOrdenIngreso);
        return detalleOrdenIngreso;
    }
    
    public void updated(DetalleOrdenIngreso detalleOrdenIngreso) throws Exception {
    	System.out.println("Updated detalleOrdenIngreso ");
        em.merge(detalleOrdenIngreso);
        PartidaEventSrc.fire(detalleOrdenIngreso);
    }
    
    public void remover(DetalleOrdenIngreso detalleOrdenIngreso){
    	System.out.println("Remover detalleOrdenIngreso ");
    	detalleOrdenIngreso.setEstado("RM");
        em.merge(detalleOrdenIngreso);
        PartidaEventSrc.fire(detalleOrdenIngreso);
    }
	
}
