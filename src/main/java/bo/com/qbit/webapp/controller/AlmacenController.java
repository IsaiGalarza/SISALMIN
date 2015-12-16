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

import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.AlmacenRepository;
import bo.com.qbit.webapp.data.UsuarioRepository;
import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.service.AlmacenRegistration;
import bo.com.qbit.webapp.util.FacesUtil;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "almacenController")
@ConversationScoped
public class AlmacenController implements Serializable {

	private static final long serialVersionUID = -4296662022834297942L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	private FacesContext facesContext;

	@Inject
	private EntityManager em;

	@Inject
	Conversation conversation;

	private @Inject AlmacenRepository almacenRepository;
	private @Inject UsuarioRepository usuarioRepository;

	private @Inject AlmacenRegistration almacenRegistration;	

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	private boolean modificar = false;
	private boolean registrar = false;
	private boolean crear = true;

	private String tituloPanel = "Registrar Almacen";
	private Almacen selectedAlmacen;
	private Almacen newAlmacen= new Almacen();
	private List<Usuario> listUsuario = new ArrayList<Usuario>();
	private List<Almacen> listaAlmacen;


	private boolean atencionCliente=false;

	// @Named provides access the return value via the EL variable name
	// "servicios" in the UI (e.g.
	// Facelets or JSP view)
	@Produces
	@Named
	public List<Almacen> getListaAlmacen() {
		return listaAlmacen;
	}

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;

	@PostConstruct
	public void initNewAlmacen() {

		// initConversation();
		beginConversation();

		usuarioSession = sessionMain.getUsuarioLogin().getLogin();
		//jalar los usuarios(ENCARGADOS LIBRES=SIN ALMACEN ASIGNADOS)
		listUsuario = usuarioRepository.findAllOrderedByID();

		newAlmacen = new Almacen();
		newAlmacen.setEstado("AC");
		newAlmacen.setFechaRegistro(new Date());
		newAlmacen.setUsuarioRegistro(usuarioSession);

		selectedAlmacen = null;

		// tituloPanel
		tituloPanel = "Registrar Almacen";

		// traer todos las almacenes ordenados por ID Desc
		listaAlmacen = almacenRepository.traerAlmacenActivas();

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
		 * if(!newAlmacen.isShowCantidad()){ newAlmacen.setCantidadCaja(0); }
		 */
	}

	// SELECT PRESENTACION CLICK
	public void onRowSelectAlmacenClick(SelectEvent event) {
		try {
			Almacen almacen = (Almacen) event.getObject();
			System.out.println("onRowSelectAlmacenClick  " + almacen.getId());
			selectedAlmacen = almacen;
			newAlmacen = em.find(Almacen.class, almacen.getId());
			newAlmacen.setFechaRegistro(new Date());
			newAlmacen.setUsuarioRegistro(usuarioSession);

			tituloPanel = "Modificar Almacen";
			modificar = false;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in onRowSelectAlmacenClick: "
					+ e.getMessage());
		}
	}

	public void registrarAlmacen() {
		if(newAlmacen.getNombre().isEmpty() || newAlmacen.getCodigo().isEmpty() || newAlmacen.getDireccion().isEmpty() || newAlmacen.getTelefono().isEmpty() || newAlmacen.getEncargado().getId()==0){
			FacesUtil.infoMessage("VALIDACION", "No pueden haber campos vacios.");
			return;
		}
		try {
			System.out.println("Ingreso a registrarAlmacen: ");
			almacenRegistration.register(newAlmacen);

			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Almacen Registrado!", newAlmacen.getNombre()+"!");
			facesContext.addMessage(null, m);

			initNewAlmacen();
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Registro Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}

	public void modificarAlmacen() {
		try {
			System.out.println("Ingreso a modificarAlmacen: "
					+ newAlmacen.getId());
			almacenRegistration.updated(newAlmacen);

			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Almacen Modificado!", newAlmacen.getNombre()+"!");
			facesContext.addMessage(null, m);
			initNewAlmacen();

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

	public void eliminarAlmacen() {
		try {
			System.out.println("Ingreso a eliminarAlmacen: " + newAlmacen.getId());
			almacenRegistration.remover(newAlmacen);
			FacesUtil.infoMessage("Almacen Borrado!", newAlmacen.getNombre()+"!");
			initNewAlmacen();
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

	public Almacen getSelectedAlmacen() {
		return selectedAlmacen;
	}

	public void setSelectedAlmacen(Almacen selectedAlmacen) {
		this.selectedAlmacen = selectedAlmacen;
	}

	public Almacen getNewAlmacen() {
		return newAlmacen;
	}

	public void setNewAlmacen(Almacen newAlmacen) {
		this.newAlmacen = newAlmacen;
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

	public List<Usuario> getListUsuario() {
		return listUsuario;
	}

	public void setListUsuario(List<Usuario> listUsuario) {
		this.listUsuario = listUsuario;
	}

}
