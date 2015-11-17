package bo.com.qbit.webapp.service;

import javax.ejb.Stateless;

import bo.com.qbit.webapp.model.Venta;

//The @Stateless annotation eliminates the need for manual transaction demarcation

@Stateless
public class VentaRegistration extends DataAccessService<Venta>{
	public VentaRegistration(){
		super(Venta.class);
	}

}

