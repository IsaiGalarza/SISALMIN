package bo.com.qbit.webapp.controller;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.CompraRepository;
import bo.com.qbit.webapp.data.EmpresaRepository;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.service.CompraRegistration;
import bo.com.qbit.webapp.service.EstadoUsuarioLogin;

@Named(value = "compraController")
@SuppressWarnings("serial")
@ConversationScoped
public class compraController implements Serializable {

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	private FacesContext facesContext;

	@Inject
	Conversation conversation;

	@Inject
	private EmpresaRepository empresaCostoRepository;

	@Inject
	private CompraRepository compraRepository;

	@Inject
	private CompraRegistration compraRegistration;
	
	Logger log = Logger.getLogger(compraController.class);

	private ScheduleModel eventModel;
	private ScheduleEvent event = new DefaultScheduleEvent();

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//estados
	private boolean modificar = false;

	private String tituloPanel = "Registrar Compra";

	private Empresa empresaLoggin;
	private EstadoUsuarioLogin estadoUsuarioLogin;
	

	@PostConstruct
	public void initNewCompra() {

		beginConversation();
		estadoUsuarioLogin = new EstadoUsuarioLogin(facesContext);

		// tituloPanel
		tituloPanel = "Centro Costo";
		empresaLoggin = estadoUsuarioLogin.getEmpresaSession(empresaCostoRepository);
		setModificar(false);
	}

	public void beginConversation() {

		if (conversation.isTransient()) {
			log.info("beginning conversation : " + this.conversation);
			conversation.begin();
			log.info("---> Init Conversation");
		}
	}

	public void endConversation() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
	}

	public void registrar(){
		try{
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					" Registrado!", ""+"!");
			facesContext.addMessage(null, m);
			initNewCompra();
		}catch(Exception e){
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Registro Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}

	public void modificar(){
		try{
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					" Registrado!", ""+"!");
			facesContext.addMessage(null, m);
			initNewCompra();
		}catch(Exception e){
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Registro Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}

	public void eliminar(){
		try{
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registrado!", ""+"!");
			facesContext.addMessage(null, m);
			initNewCompra();
		}catch(Exception e){
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Registro Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}

	private String getRootErrorMessage(Exception e) {
		String errorMessage = "Registration failed. See server log for more information";
		if (e == null) {
			return errorMessage;
		}
		Throwable t = e;
		while (t != null) {
			errorMessage = t.getLocalizedMessage();
			t = t.getCause();
		}
		return errorMessage;
	}

	public void actualizarFormReg(){
		this.setModificar(false);
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public void onEventSelect(SelectEvent selectEvent) {
		event = (ScheduleEvent) selectEvent.getObject();
	}

	public void onDateSelect(SelectEvent selectEvent) {
		event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
	}

	public void onEventMove(ScheduleEntryMoveEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

		addMessage(message);
	}

	public void onEventResize(ScheduleEntryResizeEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
		addMessage(message);
	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	// ---------    get and set  -------

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

}
