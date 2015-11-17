package bo.com.qbit.webapp.service;

import javax.ejb.Stateless;

import bo.com.qbit.webapp.model.Compra;

//The @Stateless annotation eliminates the need for manual transaction demarcation

@Stateless
public class CompraRegistration extends DataAccessService<Compra>{
	public CompraRegistration(){
		super(Compra.class);
	}

}

