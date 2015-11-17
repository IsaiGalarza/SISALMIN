package bo.com.qbit.webapp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.DetalleUnidadRepository;
import bo.com.qbit.webapp.data.UsuarioRepository;
import bo.com.qbit.webapp.model.DetalleUnidad;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.service.DetalleUnidadRegistration;
import bo.com.qbit.webapp.service.EstadoUsuarioLogin;

@Named(value = "detalleUnidadController")
@ConversationScoped
public class DetalleUnidadController implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	private FacesContext facesContext;

	@Inject
	private EntityManager em;

	@Inject
	Conversation conversation;

	@Inject
	private DetalleUnidadRegistration detalleUnidadRegistration;

	@Inject
	private DetalleUnidadRepository detalleUnidadRepository;

	private @Inject UsuarioRepository usuarioRepository;
//	private Usuario usuarioSession;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	private boolean modificar = false;
	private boolean registrar = false;
	private boolean crear = true;
	
	private String tituloPanel = "Registrar Unidad";
	private DetalleUnidad selectedDetalleUnidad;
	private DetalleUnidad newDetalleUnidad= new DetalleUnidad();
	private List<Usuario> listUsuario = new ArrayList<Usuario>();

	private List<DetalleUnidad> listaDetalleUnidad;
	private EstadoUsuarioLogin estadoUsuarioLogin;

	
	private boolean atencionCliente=false;

	// @Named provides access the return value via the EL variable name
	// "servicios" in the UI (e.g.
	// Facelets or JSP view)
	@Produces
	@Named
	public List<DetalleUnidad> getListaDetalleUnidad() {
		return listaDetalleUnidad;
	}
	
	private String usuarioSession;
	
	@PostConstruct
	public void initNewDetalleUnidad() {

		// initConversation();
		beginConversation();

		HttpServletRequest request = (HttpServletRequest) facesContext
				.getExternalContext().getRequest();
		System.out
				.println("init Tipo Producto*********************************");
		System.out.println("request.getClass().getName():"
				+ request.getClass().getName());
		System.out.println("isVentas:" + request.isUserInRole("ventas"));
		System.out.println("remoteUser:" + request.getRemoteUser());
		System.out.println("userPrincipalName:"
				+ (request.getUserPrincipal() == null ? "null" : request
						.getUserPrincipal().getName()));
		
		estadoUsuarioLogin = new EstadoUsuarioLogin(facesContext);
		usuarioSession =  estadoUsuarioLogin.getNombreUsuarioSession();
		listUsuario = usuarioRepository.findAllOrderedByID();

		newDetalleUnidad = new DetalleUnidad();
		newDetalleUnidad.setEstado("AC");
		newDetalleUnidad.setFechaRegistro(new Date());
		newDetalleUnidad.setUsuarioRegistro(usuarioSession);
		

		// tituloPanel
		tituloPanel = "Registrar Unidad";

		// traer todos las DetalleUnidades ordenados por ID Desc
		listaDetalleUnidad = detalleUnidadRepository.traerDetalleUnidadActivas();
		
		modificar = false;
		registrar = false;
		crear = true;
		atencionCliente=false;
	}
	
	public void cambiarAspecto(){
		modificar = false;
		registrar = true;
		crear = false;
		
	}
	
	public void cambiarAspectoModificar(){
		modificar = true;
		registrar = false;
		crear = false;
		
	}
	
	public void beginConversation() {

		if (conversation.isTransient()) {
			System.out.println("beginning conversation : " + this.conversation);
			conversation.begin();
			System.out.println("---> Init Conversation");
		}
	}

	public void endConversation() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
	}

	public void updatedCantidad() {
		/*
		 * if(!newDetalleUnidad.isShowCantidad()){ newDetalleUnidad.setCantidadCaja(0); }
		 */
	}

	// SELECT PRESENTACION CLICK
	public void onRowSelectDetalleUnidadClick(SelectEvent event) {
		try {
			DetalleUnidad DetalleUnidad = (DetalleUnidad) event.getObject();
			System.out.println("onRowSelectDetalleUnidadClick  " + DetalleUnidad.getId());
			selectedDetalleUnidad = DetalleUnidad;
			newDetalleUnidad = em.find(DetalleUnidad.class, DetalleUnidad.getId());
			newDetalleUnidad.setFechaRegistro(new Date());
			newDetalleUnidad.setUsuarioRegistro(usuarioSession);

			tituloPanel = "Modificar Unidad";
			modificar = false;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Error in onRowSelectDetalleUnidadClick: "
					+ e.getMessage());
		}
	}

	public void registrarDetalleUnidad() {
		try {
			System.out.println("Ingreso a registrarDetalleUnidad: ");
			detalleUnidadRegistration.register(newDetalleUnidad);
			
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"DetalleUnidad Registrado!", newDetalleUnidad.getNombre()+"!");
			facesContext.addMessage(null, m);
			
			initNewDetalleUnidad();
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Registro Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}

	public void modificarDetalleUnidad() {
		try {
			System.out.println("Ingreso a modificarDetalleUnidad: "
					+ newDetalleUnidad.getId());
			detalleUnidadRegistration.updated(newDetalleUnidad);
			
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"DetalleUnidad Modificado!", newDetalleUnidad.getNombre()+"!");
			facesContext.addMessage(null, m);
			initNewDetalleUnidad();

		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Modificado Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}
	
	public void modificar(){
		System.out.println("Ingreso a modificar");
	}

	public void eliminarDetalleUnidad() {
		try {
			System.out.println("Ingreso a eliminarDetalleUnidad: "
					+ newDetalleUnidad.getId());
			detalleUnidadRegistration.remover(newDetalleUnidad);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"DetalleUnidad Borrado!", newDetalleUnidad.getNombre()+"!");
			facesContext.addMessage(null, m);
			initNewDetalleUnidad();

		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Borrado Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}

	private String getRootErrorMessage(Exception e) {
		// Default to general error message that registration failed.
		String errorMessage = "Registration failed. See server log for more information";
		if (e == null) {
			// This shouldn't happen, but return the default messages
			return errorMessage;
		}

		// Start with the exception and recurse to find the root cause
		Throwable t = e;
		while (t != null) {
			// Get the message from the Throwable class instance
			errorMessage = t.getLocalizedMessage();
			t = t.getCause();
		}
		// This is the root cause message
		return errorMessage;
	}

	// get and set
	public String getTituloPanel() {
		return tituloPanel;
	}

	public void setTituloPanel(String tituloPanel) {
		this.tituloPanel = tituloPanel;
	}

	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	public DetalleUnidad getSelectedDetalleUnidad() {
		return selectedDetalleUnidad;
	}

	public void setSelectedDetalleUnidad(DetalleUnidad selectedDetalleUnidad) {
		this.selectedDetalleUnidad = selectedDetalleUnidad;
	}

	public DetalleUnidad getNewDetalleUnidad() {
		return newDetalleUnidad;
	}

	public void setNewDetalleUnidad(DetalleUnidad newDetalleUnidad) {
		this.newDetalleUnidad = newDetalleUnidad;
	}

	public List<Usuario> getListUsuario() {
		return listUsuario;
	}

	public void setListUsuario(List<Usuario> listUsuario) {
		this.listUsuario = listUsuario;
	}

	public boolean isAtencionCliente() {
		return atencionCliente;
	}

	public void setAtencionCliente(boolean atencionCliente) {
		this.atencionCliente = atencionCliente;
	}

	public boolean isCrear() {
		return crear;
	}

	public void setCrear(boolean crear) {
		this.crear = crear;
	}

	public boolean isRegistrar() {
		return registrar;
	}

	public void setRegistrar(boolean registrar) {
		this.registrar = registrar;
	}

}
