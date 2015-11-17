package bo.com.qbit.webapp.service;

import javax.ejb.Stateless;

import bo.com.qbit.webapp.model.MonedaEmpresa;

//The @Stateless annotation eliminates the need for manual transaction demarcation

@Stateless
public class MonedaEmpresaRegistration extends DataAccessService<MonedaEmpresa>{
	public MonedaEmpresaRegistration(){
		super(MonedaEmpresa.class);
	}

}

