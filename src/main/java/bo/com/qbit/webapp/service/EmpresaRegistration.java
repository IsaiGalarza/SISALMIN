package bo.com.qbit.webapp.service;

import javax.ejb.Stateless;
import bo.com.qbit.webapp.model.Empresa;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class EmpresaRegistration extends DataAccessService<Empresa>{
	
	public EmpresaRegistration(){
		super(Empresa.class);
	}

}