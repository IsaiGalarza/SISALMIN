package bo.com.qbit.webapp.service;

import javax.ejb.Stateless;

import bo.com.qbit.webapp.model.TipoCuenta;

//The @Stateless annotation eliminates the need for manual transaction demarcation

@Stateless
public class TipoCuentaRegistration extends DataAccessService<TipoCuenta>{
	public TipoCuentaRegistration(){
		super(TipoCuenta.class);
	}

}

