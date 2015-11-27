package bo.com.qbit.webapp.service;

import javax.ejb.Stateless;

import bo.com.qbit.webapp.model.security.Roles;

@Stateless
public class RolRegistration extends DataAccessService<Roles>{
	public RolRegistration(){
		super(Roles.class);
	}
}
