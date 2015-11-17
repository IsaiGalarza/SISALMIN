package bo.com.qbit.webapp.service;

import javax.ejb.Stateless;

import bo.com.qbit.webapp.model.ContactoProveedor;

//The @Stateless annotation eliminates the need for manual transaction demarcation

@Stateless
public class ContactoProveedorRegistration extends DataAccessService<ContactoProveedor>{
	public ContactoProveedorRegistration(){
		super(ContactoProveedor.class);
	}

}

