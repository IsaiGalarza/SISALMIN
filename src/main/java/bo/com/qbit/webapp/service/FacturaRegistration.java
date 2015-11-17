package bo.com.qbit.webapp.service;

import javax.ejb.Stateless;

import bo.com.qbit.webapp.model.Factura;

//The @Stateless annotation eliminates the need for manual transaction demarcation

@Stateless
public class FacturaRegistration extends DataAccessService<Factura>{
	public FacturaRegistration(){
		super(Factura.class);
	}

}

