package bo.com.qbit.webapp.service;

import javax.ejb.Stateless;

import bo.com.qbit.webapp.model.Nivel;

//The @Stateless annotation eliminates the need for manual transaction demarcation

@Stateless
public class NivelRegistration extends DataAccessService<Nivel>{
	public NivelRegistration(){
		super(Nivel.class);
	}

}

