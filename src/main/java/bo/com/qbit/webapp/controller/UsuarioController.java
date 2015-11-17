package bo.com.qbit.webapp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;
import bo.com.qbit.webapp.data.RolesRepository;
import bo.com.qbit.webapp.data.UsuarioRepository;
import bo.com.qbit.webapp.data.UsuarioRolRepository;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.Roles;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.model.UsuarioRol;
import bo.com.qbit.webapp.service.UserRegistration;
import bo.com.qbit.webapp.service.UsuarioRolesRegistration;
import bo.com.qbit.webapp.util.FacesUtil;
import bo.com.qbit.webapp.util.SessionMain;

@SuppressWarnings("serial")
@Named(value = "usuarioController")
@ConversationScoped
public class UsuarioController implements Serializable {

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	private FacesContext facesContext;

	@Inject
	Conversation conversation;

	@Inject
	private UserRegistration usuarioRegistration;

	@Inject
	private UsuarioRepository usuarioRepository;

	@Inject
	private UsuarioRolesRegistration usuarioRolesRegistration;

	@Inject
	private RolesRepository rolesRepository;
	
	@Inject
	private UsuarioRolRepository usuarioRolesRepository;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	private @Inject SessionMain sessionMain; //variable del login
	private String nombreUsuario;	
	private Empresa empresaLogin;


	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	private String tituloPanel ;
	private String nombreRol;
	private String nombreEstado="ACTIVO";

	@Produces
	@Named
	private Usuario newUsuario;
	private Usuario selectedUsuario;
	private Roles rol;

	private List<Usuario> listUsuario = new ArrayList<Usuario>();
	private List<Usuario> listFilterUsuario = new ArrayList<Usuario>();

	private List<Roles> listaRoles = new ArrayList<Roles>();
	private String[] listEstado = {"ACTIVO","INACTIVO"};	

	//estados
	private boolean crear = true;
	private boolean registrar = false;
	private boolean modificar = false;
	private boolean stateInicial = true;
	
	//columnas
	private String tipoColumnRegistro= "col-md-4"; //4
	private String tipoColumnTable = "col-md-12"; //8

	@PostConstruct
	public void initNewUsuario() {

		log.info(" init new initNewUsuario");
		beginConversation();
		nombreUsuario = sessionMain.getUsuarioLoggin().getLogin();
		empresaLogin = sessionMain.getEmpresaLoggin();
	

		loadDefault();
		
	}
	
	private void loadDefault(){
		newUsuario = new Usuario();
		selectedUsuario = new Usuario();
		rol = obtenerRolUserLogin();
		listUsuario = usuarioRepository.findAllOrderedByID();
		
		listaRoles = rolesRepository.findAll();
		nombreRol = listaRoles.size()>0?listaRoles.get(0).getName():"";

		// tituloPanel
		tituloPanel = "Registrar Usuario";
		modificar = false;
	}

	public Roles obtenerRolUserLogin(){
		List<Roles> list = rolesRepository.findAll();
		String nameRoles = "";
		HttpServletRequest request = (HttpServletRequest) facesContext
				.getExternalContext().getRequest();
		for(Roles r: list){
			if(request.isUserInRole(r.getName())){
				nameRoles = r.getName();
			}
		}
		return rolesRepository.findByName(nameRoles);
	}

	public void resetearFitrosTabla(String id) {
        DataTable table = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(id);
        table.setSelection(null);
        table.reset();
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

	public void registrarUsuario() {
		try {
			log.info("Ingreso a registrarUsuario: ");
			
			Roles roles = rolesRepository.findByName(nombreRol);
			newUsuario.setFechaRegistro(new Date());
			newUsuario.setUsuarioRegistro(nombreUsuario);
			newUsuario.setState(nombreEstado.equals("ACTIVO")?"AC":"IN");
			
			
			newUsuario = usuarioRegistration.create(newUsuario);
			UsuarioRol usuarioRol = new UsuarioRol();
			usuarioRol.setRoles(roles);
			usuarioRol.setUsuario(newUsuario);
			usuarioRolesRegistration.register(usuarioRol);
			
			resetearFitrosTabla("formTableUsuario:dataTableUser");
			FacesUtil.infoMessage("Registro", "Usuario Registrado! "+newUsuario.getLogin());
			loadDefault();
		} catch (Exception e) {
			log.error("Error al registrar Usuario error: "+e.getMessage());
			FacesUtil.errorMessage("Error al registrar Usuario");
		}
	}

	public void modificarUsuario() {
		try {
			log.info("Ingreso a modificarUsuario: "
					+ newUsuario.getId());
			newUsuario.setFechaModificacion(new Date());
			newUsuario.setState(nombreEstado.equals("ACTIVO")?"AC":"IN");
			
			usuarioRegistration.update(newUsuario);
			Roles roles = rolesRepository.findByName(nombreRol);
			UsuarioRol usuarioRol = new UsuarioRol();
			usuarioRol.setRoles(roles);
			usuarioRol.setUsuario(newUsuario);
			usuarioRolesRegistration.update(usuarioRol);
			
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Usuario Modificado", newUsuario.getLogin());
			facesContext.addMessage(null, m);
			crear = false;
			registrar = true;
			modificar = false;
			tipoColumnTable = "col-md-8";
			resetearFitrosTabla("formTableUsuario:dataTableUser");			
			loadDefault();
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Registro Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}

	public void eliminarUsuario() {
		try {
			log.info("Ingreso a eliminarUsuario "
					+ newUsuario.getId());
			newUsuario.setState("RM");
			newUsuario.setFechaModificacion(new Date());
			usuarioRegistration.update(newUsuario);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Usuario Eliminado!", newUsuario.getLogin()+"!");
			facesContext.addMessage(null, m);
			crear = false;
			registrar = true;
			modificar = false;
			tipoColumnTable = "col-md-8";
			resetearFitrosTabla("formTableUsuario:dataTableUser");
			loadDefault();
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Error al Eliminar.");
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

	public boolean isRole(String permiso){
		
		return false;
	}

	public void verificarPermisoPagina(String permiso){
		if(!isRole(permiso)){
			try {
				FacesContext.getCurrentInstance().getExternalContext()
				.redirect("/webapp/error403.xhtml");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void onRowSelect(SelectEvent event) {
		nombreRol = usuarioRolesRepository.findByUsuario(selectedUsuario).getRoles().getName();
		newUsuario = selectedUsuario;
		nombreEstado = newUsuario.getState().equals("AC")?"ACTIVO":"INACTIVO";
		crear = false;
		registrar = false;
		modificar = true;
		tipoColumnTable = "col-md-8";
		resetearFitrosTabla("formTableUsuario:dataTableUser");
	}

	public void actualizarFormReg(){
		crear = true;
		registrar = false;
		modificar = false;
		tipoColumnTable = "col-md-12";
		newUsuario = new Usuario();
		selectedUsuario = new Usuario();
		resetearFitrosTabla("formTableUsuario:dataTableUser");
	}
	
	public void cambiarAspecto(){
		crear = false;
		registrar = true;
		modificar = false;
		tipoColumnTable = "col-md-8";
		selectedUsuario = new Usuario();
		newUsuario = new Usuario();
	}

	//validaciones
	
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2)
	         throws ValidatorException {
	      if (((String)arg2).length()<1) {
	         throw new ValidatorException(new FacesMessage("Al menos 1 caracteres "));
	      }
	   }
	
	
	// ----------  get and set -------------
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

	public Usuario getSelectedUsuario() {
		return selectedUsuario;
	}

	public void setSelectedUsuario(Usuario selectedUsuario) {
		this.selectedUsuario = selectedUsuario;
	}

	public List<Usuario> getListUsuario() {
		return listUsuario;
	}

	public void setListUsuario(List<Usuario> listUsuario) {
		this.listUsuario = listUsuario;
	}

	public String getNombreRol() {
		return nombreRol;
	}

	public void setNombreRol(String nombreRol) {
		this.nombreRol = nombreRol;
	}

	public List<Usuario> getListFilterUsuario() {
		return listFilterUsuario;
	}

	public void setListFilterUsuario(List<Usuario> listFilterUsuario) {
		this.listFilterUsuario = listFilterUsuario;
	}

	public String getNombreEstado() {
		return nombreEstado;
	}

	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}

	public String[] getListEstado() {
		return listEstado;
	}

	public void setListEstado(String[] listEstado) {
		this.listEstado = listEstado;
	}

	public boolean isStateInicial() {
		return stateInicial;
	}

	public void setStateInicial(boolean stateInicial) {
		this.stateInicial = stateInicial;
	}

	public String getTipoColumnRegistro() {
		return tipoColumnRegistro;
	}

	public void setTipoColumnRegistro(String tipoColumnRegistro) {
		this.tipoColumnRegistro = tipoColumnRegistro;
	}

	public String getTipoColumnTable() {
		return tipoColumnTable;
	}

	public void setTipoColumnTable(String tipoColumnTable) {
		this.tipoColumnTable = tipoColumnTable;
	}

	public List<Roles> getListaRoles() {
		return listaRoles;
	}

	public void setListaRoles(List<Roles> listaRoles) {
		this.listaRoles = listaRoles;
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
