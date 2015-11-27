package bo.com.qbit.webapp.service;

import javax.ejb.Stateless;

import bo.com.qbit.webapp.model.security.UsuarioRol;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class UsuarioRolRegistration extends DataAccessService<UsuarioRol>{

	public UsuarioRolRegistration(){
		super(UsuarioRol.class);
	}

	
}

