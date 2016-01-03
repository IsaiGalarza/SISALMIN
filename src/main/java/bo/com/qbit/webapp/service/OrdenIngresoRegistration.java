package bo.com.qbit.webapp.service;


import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.OrdenIngreso;


//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class OrdenIngresoRegistration {
	
    @Inject
    private EntityManager em;

    @Inject
    private Event<OrdenIngreso> PartidaEventSrc;
    
    public OrdenIngreso register(OrdenIngreso ordenIngreso) throws Exception {
        System.out.println("Registering ordenIngreso " );
        this.em.persist(ordenIngreso);
        this.em.flush();
        this.em.refresh(ordenIngreso);
        PartidaEventSrc.fire(ordenIngreso);
        return ordenIngreso;
    }
    
    public void updated(OrdenIngreso ordenIngreso) throws Exception {
    	System.out.println("Updated ordenIngreso ");
        em.merge(ordenIngreso);
        PartidaEventSrc.fire(ordenIngreso);
    }
    
    public void remover(OrdenIngreso ordenIngreso) throws Exception{
    	System.out.println("Remover OrdenIngreso ");
    	ordenIngreso.setEstado("RM");
        em.merge(ordenIngreso);
        PartidaEventSrc.fire(ordenIngreso);
    }
	
}
