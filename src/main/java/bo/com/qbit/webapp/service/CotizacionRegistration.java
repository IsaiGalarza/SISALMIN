package bo.com.qbit.webapp.service;

import javax.ejb.Stateless;
import bo.com.qbit.webapp.model.Cotizacion;

//The @Stateless annotation eliminates the need for manual transaction demarcation

@Stateless
public class CotizacionRegistration extends DataAccessService<Cotizacion>{
	public CotizacionRegistration(){
		super(Cotizacion.class);
	}
}
//
//
//@Stateless
//public class CotizacionRegistration {
//	
//	@Inject
//    private Logger log;
//
//    @Inject
//    private EntityManager em;
//
//    @Inject
//    private Event<Cotizacion> sucursalEventSrc;
//    
//    public Cotizacion register(Cotizacion cotizacion) {
//    	try{
//        log.info("Registering cotizacion ");
//        em.persist(cotizacion);
//        em.flush();
//        em.refresh(cotizacion);
//        sucursalEventSrc.fire(cotizacion);
//        return cotizacion;
//    	}catch(Exception e){
//    		log.severe("register(cotizacion) error: "+e.getMessage());
//    		return null;
//    		
//    	}
//    }
//    
//    public void updated(Cotizacion cotizacion) throws Exception {
//    	log.info("Updated Sucursal " );
//        em.merge(cotizacion);
//        sucursalEventSrc.fire(cotizacion);
//    }
//    
//    public void remove(Cotizacion cotizacion){
//    	log.info("Remover Sucursal ");
//        em.merge(cotizacion);
//        sucursalEventSrc.fire(cotizacion);
//    }
//	
//}
