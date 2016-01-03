package bo.com.qbit.webapp.service;


import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.DetalleTomaInventario;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class DetalleTomaInventarioRegistration {
	
    @Inject
    private EntityManager em;

    @Inject
    private Event<DetalleTomaInventario> tomaInventarioEventSrc;
    
    public DetalleTomaInventario register(DetalleTomaInventario detalleTomaInventario) throws Exception {
        System.out.println("Registering detalleTomaInventario " );
        this.em.persist(detalleTomaInventario);
        this.em.flush();
        this.em.refresh(detalleTomaInventario);
        tomaInventarioEventSrc.fire(detalleTomaInventario);
        return detalleTomaInventario;
    }
    
    public void updated(DetalleTomaInventario detalleTomaInventario) throws Exception {
    	System.out.println("Updated detalleTomaInventario ");
        em.merge(detalleTomaInventario);
        tomaInventarioEventSrc.fire(detalleTomaInventario);
    }
    
    public void remover(DetalleTomaInventario detalleTomaInventario){
    	System.out.println("Remover detalleTomaInventario ");
    	detalleTomaInventario.setEstado("RM");
        em.merge(detalleTomaInventario);
        tomaInventarioEventSrc.fire(detalleTomaInventario);
    }
	
}
