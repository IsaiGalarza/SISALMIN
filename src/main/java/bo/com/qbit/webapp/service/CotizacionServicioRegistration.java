package bo.com.qbit.webapp.service;


import javax.ejb.Stateless;
import bo.com.qbit.webapp.model.CotizacionServicio;

//The @Stateless annotation eliminates the need for manual transaction demarcation

@Stateless
public class CotizacionServicioRegistration extends DataAccessService<CotizacionServicio>{
	public CotizacionServicioRegistration(){
		super(CotizacionServicio.class);
	}
}
