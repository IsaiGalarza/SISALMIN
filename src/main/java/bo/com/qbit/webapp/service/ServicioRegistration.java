package bo.com.qbit.webapp.service;


import javax.ejb.Stateless;

import bo.com.qbit.webapp.model.Servicio;

//The @Stateless annotation eliminates the need for manual transaction demarcation

@Stateless
public class ServicioRegistration extends DataAccessService<Servicio>{
	public ServicioRegistration(){
		super(Servicio.class);
	}

}
