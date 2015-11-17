package bo.com.qbit.webapp.service;

import javax.ejb.Stateless;

import bo.com.qbit.webapp.model.ClienteProveedor;

//The @Stateless annotation eliminates the need for manual transaction demarcation

@Stateless
public class ClienteProveedorRegistration extends DataAccessService<ClienteProveedor>{
	public ClienteProveedorRegistration(){
		super(ClienteProveedor.class);
	}

}

