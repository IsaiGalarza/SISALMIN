package bo.com.qbit.webapp.service;

import javax.ejb.Stateless;

import bo.com.qbit.webapp.model.security.Rol;

@Stateless
public class RolRegistration extends DataAccessService<Rol>{
	public RolRegistration(){
		super(Rol.class);
	}
}
