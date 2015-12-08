package bo.com.qbit.webapp.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.OrdenIngreso;
import bo.com.qbit.webapp.model.Producto;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class ProductoRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Producto> ProductoEventSrc;
    
    public Producto register(Producto producto) throws Exception {
        log.info("Registering Producto: " + producto.getNombre());
        this.em.persist(producto);
        this.em.flush();
        this.em.refresh(producto);
        ProductoEventSrc.fire(producto);
        return producto;
    }
    
    public void updated(Producto Producto) throws Exception {
    	log.info("Updated Producto: " + Producto.getNombre());
        em.merge(Producto);
        ProductoEventSrc.fire(Producto);
    }
    
    public void remover(Producto Producto){
    	log.info("Remover Producto: " + Producto.getNombre());
    	Producto.setEstado("RM");
        em.merge(Producto);
        ProductoEventSrc.fire(Producto);
    }
	
}
