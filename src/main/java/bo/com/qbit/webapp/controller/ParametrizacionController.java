package bo.com.qbit.webapp.controller;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.EmpresaRepository;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.service.EstadoUsuarioLogin;


@Named(value = "parametrizacionController")
@ConversationScoped
public class ParametrizacionController implements Serializable {

	
	private static final long serialVersionUID = -899586283065172878L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	private FacesContext facesContext;

	@Inject
	Conversation conversation;

	@Inject
	private EmpresaRepository empresaRepository;
	
	@Inject
	private Logger log;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//estados
	private boolean crear = true;
	private boolean registrar = false;
	private boolean modificar = false;
	private boolean permitirCredito = true;

	private String nombreEstado="ACTIVO";
	private String nombreUsuario; 
	private String tituloPanel = "Prametrizacion";
	private String tipoColumnTable = "col-md-12"; //8

	private EstadoUsuarioLogin estadoUsuarioLogin;
	private Empresa empresaLogin;

	@PostConstruct
	public void initNewParametrizacion() {
		log.info(" init new initNewParametrizacion");
		beginConversation();
		estadoUsuarioLogin = new EstadoUsuarioLogin(facesContext);
		nombreUsuario =  estadoUsuarioLogin.getNombreUsuarioSession();
		empresaLogin = estadoUsuarioLogin.getEmpresaSession(empresaRepository);
		
		tituloPanel = "Parametrizacion";
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
			initNewParametrizacion();
		}catch(Exception e){
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Registro Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}

	public void actualizarForm(){
		crear = true;
		registrar = false;
		modificar = false;
		tipoColumnTable = "col-md-12";
	}
	
	public void modificar(){
		try{
			
			initNewParametrizacion();
		}catch(Exception e){
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Registro Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}

	public void eliminar(){
		try{
			
			initNewParametrizacion();
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

	public void resetearFitrosTabla(String id) {
		DataTable table = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(id);
		table.setSelection(null);
		table.reset();
	}

	public void actualizarFormReg(){
		crear = true;
		registrar = false;
		modificar = false;
		setTipoColumnTable("col-md-12");
		resetearFitrosTabla("formTableGrupoImpuesto:dataTableGrupoImpuesto");
	}
	
	public void cambiarAspecto(){
		crear = false;
		registrar = true;
		modificar = false;
		tipoColumnTable = "col-md-8";
		resetearFitrosTabla("formTableGrupoImpuesto:dataTableGrupoImpuesto");
	}

	public void onRowSelect(SelectEvent event) {
		crear = false;
		registrar = false;
		modificar = true;
		tipoColumnTable = "col-md-8";
		resetearFitrosTabla("formTableGrupoImpuesto:dataTableGrupoImpuesto");
	}
	
	// --------------   get and set  ---------------
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

	public String getNombreEstado() {
		return nombreEstado;
	}

	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}

	public boolean isPermitirCredito() {
		return permitirCredito;
	}

	public void setPermitirCredito(boolean permitirCredito) {
		this.permitirCredito = permitirCredito;
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

	public String getTipoColumnTable() {
		return tipoColumnTable;
	}

	public void setTipoColumnTable(String tipoColumnTable) {
		this.tipoColumnTable = tipoColumnTable;
	}

}
