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

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.RolesRepository;
import bo.com.qbit.webapp.data.UsuarioRepository;
import bo.com.qbit.webapp.data.UsuarioRolRepository;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.model.security.Roles;
import bo.com.qbit.webapp.model.security.UsuarioRol;
import bo.com.qbit.webapp.service.UsuarioRegistration;
import bo.com.qbit.webapp.service.UsuarioRolRegistration;
import bo.com.qbit.webapp.util.FacesUtil;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "usuarioController")
@ConversationScoped
public class UsuarioController implements Serializable {

	private static final long serialVersionUID = 6211210765749674269L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	private FacesContext facesContext;

	@Inject
	Conversation conversation;

	@Inject
	private UsuarioRegistration usuarioRegistration;

	@Inject
	private UsuarioRepository usuarioRepository;

	@Inject
	private UsuarioRolRegistration usuarioRolRegistration;

	@Inject
	private RolesRepository rolesRepository;

	@Inject
	private UsuarioRolRepository usuarioRolRepository;

	@Inject
	private UsuarioRolRepository usuarioRolesRepository;

	private Logger log = Logger.getLogger(this.getClass());

	private @Inject SessionMain sessionMain; //variable del login
	private String nombreUsuario;	
	private Empresa empresaLogin;
	private Gestion gestionLogin;

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
	private UsuarioRol selectedUsuarioRol;
	private Roles selectedRol;

	private List<Usuario> listUsuario = new ArrayList<Usuario>();
	private List<Usuario> listFilterUsuario = new ArrayList<Usuario>();
	private List<Roles> listRol = new ArrayList<Roles>();
	private String[] listEstado = {"ACTIVO","INACTIVO"};
	
	private List<UsuarioRol> listUsuarioRol = new ArrayList<UsuarioRol>();
	private List<UsuarioRol> listFilterUsuarioRol = new ArrayList<UsuarioRol>();

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
		nombreUsuario = sessionMain.getUsuarioLogin().getLogin();
		empresaLogin = sessionMain.getEmpresaLogin();
		gestionLogin = sessionMain.getGestionLogin();

		listRol = rolesRepository.findAllOrderByAsc();

		loadDefault();

	}

	private void loadDefault(){
		
		tipoColumnRegistro= "col-md-4";
		tipoColumnTable = "col-md-12";
		
		crear = true;
		registrar = false;
		modificar = false;
		stateInicial = true;
		newUsuario = new Usuario();
		selectedUsuario = new Usuario();
		listUsuario = usuarioRepository.findAllOrderedByID();
		
		listUsuarioRol = usuarioRolRepository.findAllOrderedByNombre();

		nombreRol = listRol.get(0).getNombre();
		selectedRol = listRol.get(0);

		// tituloPanel
		tituloPanel = "Registrar Usuario";
		modificar = false;
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
			Date fechaActual = new Date();
			newUsuario.setFechaRegistro(new Date());
			newUsuario.setUsuarioRegistro(nombreUsuario);
			newUsuario.setState(nombreEstado.equals("ACTIVO")?"AC":"IN");
			if(!newUsuario.validate(facesContext, empresaLogin, gestionLogin)){
				log.info("registrarUsuario - > false ");
				resetearFitrosTabla("formTableUsuario:dataTableUser");
				return;
			}

			newUsuario = usuarioRegistration.create(newUsuario);
			UsuarioRol usuarioRol = new UsuarioRol();
			usuarioRol.setRol(selectedRol);
			usuarioRol.setUsuario(newUsuario);
			usuarioRol.setEstado("AC");
			usuarioRol.setFechaRegistro(fechaActual);
			usuarioRol.setUsuarioRegistro(nombreUsuario);
			usuarioRolRegistration.create(usuarioRol);

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
			Date fechaActual = new Date();
			newUsuario.setFechaModificacion(new Date());
			newUsuario.setState(nombreEstado.equals("ACTIVO")?"AC":"IN");
			if(!newUsuario.validate(facesContext, empresaLogin, gestionLogin)){
				log.info("registrarUsuario - > false ");
				resetearFitrosTabla("formTableUsuario:dataTableUser");
				return;
			}
			usuarioRegistration.update(newUsuario);
			UsuarioRol usuarioRol = usuarioRolRepository.findByUsuario(newUsuario);
			usuarioRol.setRol(selectedRol);
			usuarioRol.setFechaModificacion(fechaActual);
			usuarioRolRegistration.update(usuarioRol);
			
			FacesUtil.infoMessage("Usuario Modificado", ""+newUsuario.getLogin());
			resetearFitrosTabla("formTableUsuario:dataTableUser");			
			loadDefault();
		} catch (Exception e) {
			log.error("Error al Modificar. Usuario error: "+e.getMessage());
		}
	}

	public void eliminarUsuario() {
		try {
			Date fechaActual = new Date();
			log.info("Ingreso a eliminarUsuario "
					+ newUsuario.getId());
			newUsuario.setState("RM");
			newUsuario.setFechaModificacion(fechaActual);
			usuarioRegistration.update(newUsuario);
			
			//eliminar usuariorol
			selectedUsuarioRol.setEstado("RM");
			selectedUsuarioRol.setFechaModificacion(fechaActual);
			usuarioRolRegistration.update(selectedUsuarioRol);

			FacesUtil.infoMessage("Usuario Eliminado", ""+newUsuario.getLogin());
			resetearFitrosTabla("formTableUsuario:dataTableUser");
			loadDefault();
		} catch (Exception e) {
			log.error("Error al Eliminar. Usuario error: "+e.getMessage());
		}
	}

	public void onRowSelect(SelectEvent event) {
		//selectedRol = usuarioRolesRepository.findByUsuario(selectedUsuario).getRol();
		selectedRol = selectedUsuarioRol.getRol();
		nombreRol = selectedRol.getNombre();
		//newUsuario = selectedUsuario;
		newUsuario = selectedUsuarioRol.getUsuario();
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

	private Roles obtenerRolByLocal(String nombreRol){
		for(Roles r: listRol){
			if(r.getNombre().equals(nombreRol)){
				return r;
			}
		}
		return null;
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
		selectedRol = obtenerRolByLocal( nombreRol);
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

	public Roles getSelectedRol() {
		return selectedRol;
	}

	public void setSelectedRol(Roles selectedRol) {
		this.selectedRol = selectedRol;
	}

	public List<Roles> getListRol() {
		return listRol;
	}

	public void setListRol(List<Roles> listRol) {
		this.listRol = listRol;
	}

	public List<UsuarioRol> getListUsuarioRol() {
		return listUsuarioRol;
	}

	public void setListUsuarioRol(List<UsuarioRol> listUsuarioRol) {
		this.listUsuarioRol = listUsuarioRol;
	}

	public UsuarioRol getSelectedUsuarioRol() {
		return selectedUsuarioRol;
	}

	public void setSelectedUsuarioRol(UsuarioRol selectedUsuarioRol) {
		this.selectedUsuarioRol = selectedUsuarioRol;
	}

	public List<UsuarioRol> getListFilterUsuarioRol() {
		return listFilterUsuarioRol;
	}

	public void setListFilterUsuarioRol(List<UsuarioRol> listFilterUsuarioRol) {
		this.listFilterUsuarioRol = listFilterUsuarioRol;
	}

}
