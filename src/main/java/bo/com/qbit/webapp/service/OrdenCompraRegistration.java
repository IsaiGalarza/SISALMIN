package bo.com.qbit.webapp.service;

import javax.ejb.Stateless;

import bo.com.qbit.webapp.model.OrdenCompra;

//The @Stateless annotation eliminates the need for manual transaction demarcation

@Stateless
public class OrdenCompraRegistration extends DataAccessService<OrdenCompra>{
	public OrdenCompraRegistration(){
		super(OrdenCompra.class);
	}

}

