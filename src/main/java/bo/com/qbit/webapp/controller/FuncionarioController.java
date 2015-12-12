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

import bo.com.qbit.webapp.data.DetalleUnidadRepository;
import bo.com.qbit.webapp.data.FuncionarioRepository;
import bo.com.qbit.webapp.model.DetalleUnidad;
import bo.com.qbit.webapp.model.Funcionario;
import bo.com.qbit.webapp.service.FuncionarioRegistration;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "funcionarioController")
@ConversationScoped
public class FuncionarioController implements Serializable {
//test
	private static final long serialVersionUID = 1L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	private FacesContext facesContext;

	@Inject
	private EntityManager em;

	@Inject
	Conversation conversation;

	@Inject
	private FuncionarioRegistration FuncionarioRegistration;

	@Inject
	private FuncionarioRepository FuncionarioRepository;
	
	@Inject
	private DetalleUnidadRepository detalleUnidadRepository;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	private boolean modificar = false;
	private boolean registrar = false;
	private boolean crear = true;
	
	private String tituloPanel = "Registrar Funcionario";
	
	private Funcionario selectedFuncionario;
	private Funcionario newFuncionario= new Funcionario();
	private DetalleUnidad selectedDetalleUnidad = new DetalleUnidad();
	
	private List<DetalleUnidad> listDetalleUnidad = new ArrayList<DetalleUnidad>();
	private List<Funcionario> listaFuncionario;

	
	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;
	
	private boolean atencionCliente=false;

	// @Named provides access the return value via the EL variable name
	// "servicios" in the UI (e.g.
	// Facelets or JSP view)
	@Produces
	@Named
	public List<Funcionario> getListaFuncionario() {
		return listaFuncionario;
	}
	
	@PostConstruct
	public void initNewFuncionario() {

		beginConversation();

		usuarioSession = sessionMain.getUsuarioLogin().getLogin();

		selectedFuncionario = null;
		newFuncionario = new Funcionario();
		newFuncionario.setEstado("AC");
		newFuncionario.setFechaRegistro(new Date());
		newFuncionario.setUsuarioRegistro(usuarioSession);
		

		// tituloPanel
		tituloPanel = "Registrar Funcionario";

		// traer todos las Funcionarioes ordenados por ID Desc
		listaFuncionario = FuncionarioRepository.traerFuncionarioActivas();
		listDetalleUnidad = detalleUnidadRepository.traerDetalleUnidadActivas();
		
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
		 * if(!newFuncionario.isShowCantidad()){ newFuncionario.setCantidadCaja(0); }
		 */
	}

	// SELECT PRESENTACION CLICK
	public void onRowSelectFuncionarioClick(SelectEvent event) {
		try {
			Funcionario Funcionario = (Funcionario) event.getObject();
			System.out.println("onRowSelectFuncionarioClick  " + Funcionario.getId());
			selectedFuncionario = Funcionario;
			newFuncionario = em.find(Funcionario.class, Funcionario.getId());
			newFuncionario.setFechaRegistro(new Date());
			newFuncionario.setUsuarioRegistro(usuarioSession);
			selectedDetalleUnidad = newFuncionario.getDetalleUnidad();

			tituloPanel = "Modificar Funcionario";
			modificar = false;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in onRowSelectFuncionarioClick: "
					+ e.getMessage());
		}
	}

	public void registrarFuncionario() {
		try {
			System.out.println("Ingreso a registrarFuncionario: ");
			newFuncionario.setDetalleUnidad(selectedDetalleUnidad);
			FuncionarioRegistration.register(newFuncionario);
			
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Funcionario Registrado!", newFuncionario.getNombre()+"!");
			facesContext.addMessage(null, m);
			
			initNewFuncionario();
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Registro Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}

	public void modificarFuncionario() {
		try {
			System.out.println("Ingreso a modificarFuncionario: "
					+ newFuncionario.getId());
			newFuncionario.setDetalleUnidad(selectedDetalleUnidad);
			FuncionarioRegistration.updated(newFuncionario);
			
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Funcionario Modificado!", newFuncionario.getNombre()+"!");
			facesContext.addMessage(null, m);
			initNewFuncionario();

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

	public void eliminarFuncionario() {
		try {
			System.out.println("Ingreso a eliminarFuncionario: "
					+ newFuncionario.getId());
			FuncionarioRegistration.remover(newFuncionario);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Funcionario Borrado!", newFuncionario.getNombre()+"!");
			facesContext.addMessage(null, m);
			initNewFuncionario();

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
	
	//Detalle Unidad
	
	public List<DetalleUnidad> completeDetalleUnidad(String query) {
		String upperQuery = query.toUpperCase();
		List<DetalleUnidad> results = new ArrayList<DetalleUnidad>();
		for(DetalleUnidad i : listDetalleUnidad) {
			if(i.getNombre().toUpperCase().startsWith(upperQuery)){
				results.add(i);
			}
		}         
		return results;
	}
	
	public void onRowSelectDetalleUnidadClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(DetalleUnidad i : listDetalleUnidad){
			if(i.getNombre().equals(nombre)){
				selectedDetalleUnidad = i;
				return;
			}
		}
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

	public Funcionario getSelectedFuncionario() {
		return selectedFuncionario;
	}

	public void setSelectedFuncionario(Funcionario selectedFuncionario) {
		this.selectedFuncionario = selectedFuncionario;
	}

	public Funcionario getNewFuncionario() {
		return newFuncionario;
	}

	public void setNewFuncionario(Funcionario newFuncionario) {
		this.newFuncionario = newFuncionario;
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

	public List<DetalleUnidad> getListDetalleUnidad() {
		return listDetalleUnidad;
	}

	public void setListDetalleUnidad(List<DetalleUnidad> listDetalleUnidad) {
		this.listDetalleUnidad = listDetalleUnidad;
	}

	public DetalleUnidad getSelectedDetalleUnidad() {
		return selectedDetalleUnidad;
	}

	public void setSelectedDetalleUnidad(DetalleUnidad selecteDetalleUnidad) {
		this.selectedDetalleUnidad = selecteDetalleUnidad;
	}

}
