package bo.com.qbit.webapp.controller;

import java.io.Serializable;
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

import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.UnidadMedidaRepository;
import bo.com.qbit.webapp.model.UnidadMedida;
import bo.com.qbit.webapp.service.UnidadMedidaRegistration;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "unidadMedidaController")
@ConversationScoped
public class UnidadMedidaController implements Serializable {
//test
	private static final long serialVersionUID = 1L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	private FacesContext facesContext;

	@Inject
	Conversation conversation;

	@Inject
	private UnidadMedidaRegistration unidadMedidaRegistration;

	@Inject
	private UnidadMedidaRepository unidadMedidaRepository;
	

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	private boolean modificar = false;
	private boolean registrar = false;
	private boolean crear = true;
	
	private String tituloPanel = "Registrar UnidadMedida";
	
	private UnidadMedida selectedUnidadMedida;
	private UnidadMedida newUnidadMedida= new UnidadMedida();
	
	private List<UnidadMedida> listaUnidadMedida;

	
	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;

	// @Named provides access the return value via the EL variable name
	// "servicios" in the UI (e.g.
	// Facelets or JSP view)
	@Produces
	@Named
	public List<UnidadMedida> getListaUnidadMedida() {
		return listaUnidadMedida;
	}
	
	@PostConstruct
	public void initNewUnidadMedida() {

		beginConversation();

		usuarioSession = sessionMain.getUsuarioLogin().getLogin();

		setSelectedUnidadMedida(null);
		newUnidadMedida = new UnidadMedida();
		newUnidadMedida.setEstado("AC");
		newUnidadMedida.setFechaRegistro(new Date());
		newUnidadMedida.setUsuarioRegistro(usuarioSession);
		

		// tituloPanel
		tituloPanel = "Registrar Funcionario";

		// traer todos las UnidadMedida ordenados por ID Desc
		listaUnidadMedida = unidadMedidaRepository.findAllActivosOrderedByID();
		
		modificar = false;
		registrar = false;
		crear = true;
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
		 * if(!newFuncionario.isShowCantidad()){ newFuncionario.setCantidadCaja(0); }
		 */
	}

	// SELECT PRESENTACION CLICK
	public void onRowSelectUnidadMedidaClick(SelectEvent event) {
		try {
			System.out.println("onRowSelectFuncionarioClick  " + selectedUnidadMedida.getId());
			newUnidadMedida = selectedUnidadMedida;
			newUnidadMedida.setFechaRegistro(new Date());
			newUnidadMedida.setUsuarioRegistro(usuarioSession);

			tituloPanel = "Modificar UnidadMedida";
			modificar = false;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in onRowSelectFuncionarioClick: "
					+ e.getMessage());
		}
	}

	public void registrarUnidadMedida() {
		try {
			System.out.println("Ingreso a registrarUnidadMedida: ");
			unidadMedidaRegistration.register(newUnidadMedida);
			
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Unidad Medida Registrada!", newUnidadMedida.getNombre()+"!");
			facesContext.addMessage(null, m);
			
			initNewUnidadMedida();
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Registro Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}

	public void modificarUnidadMedida() {
		try {
			System.out.println("Ingreso a modificarUnidadMedida: "
					+ newUnidadMedida.getId());
			unidadMedidaRegistration.updated(newUnidadMedida);
			
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Unidad Medida Modificado!", newUnidadMedida.getNombre()+"!");
			facesContext.addMessage(null, m);
			initNewUnidadMedida();

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

	public void eliminarUnidadMedida() {
		try {
			System.out.println("Ingreso a eliminarUnidadMedida: "
					+ newUnidadMedida.getId());
			unidadMedidaRegistration.remover(newUnidadMedida);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Unidad Medida Eliminada!", newUnidadMedida.getNombre()+"!");
			facesContext.addMessage(null, m);
			initNewUnidadMedida();

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
	
	public UnidadMedida getNewUnidadMedida() {
		return newUnidadMedida;
	}

	public void setNewUnidadMedida(UnidadMedida newUnidadMedida) {
		this.newUnidadMedida = newUnidadMedida;
	}

	public UnidadMedida getSelectedUnidadMedida() {
		return selectedUnidadMedida;
	}

	public void setSelectedUnidadMedida(UnidadMedida selectedUnidadMedida) {
		this.selectedUnidadMedida = selectedUnidadMedida;
	}

}
