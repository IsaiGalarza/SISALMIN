package bo.com.qbit.webapp.service;

import javax.ejb.Stateless;

import bo.com.qbit.webapp.model.ParametroEmpresa;

//The @Stateless annotation eliminates the need for manual transaction demarcation

@Stateless
public class ParametroEmpresaRegistration extends DataAccessService<ParametroEmpresa>{
	public ParametroEmpresaRegistration(){
		super(ParametroEmpresa.class);
	}
}

