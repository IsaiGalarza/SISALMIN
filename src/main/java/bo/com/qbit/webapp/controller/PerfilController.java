package bo.com.qbit.webapp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.UploadedFile;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.UsuarioRepository;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.service.UsuarioRegistration;


@Named(value = "perfilController")
@ConversationScoped
public class PerfilController implements Serializable {

	
	private static final long serialVersionUID = -2989737706810995315L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	private FacesContext facesContext;

	@Inject
	Conversation conversation;

	@Inject
	private UsuarioRepository usuarioRepository;

	@Inject
	private UsuarioRegistration usuarioRegistration;

	private Usuario usuarioSession;
	
	@Inject
	private Logger log;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	private boolean modificar = false;
	private String tituloPanel = "Registrar Empresa";
	private List<Usuario> listUsuario = new ArrayList<Usuario>();
	private UploadedFile file;

	@PostConstruct
	public void initNewPerfil() {

		beginConversation();

		HttpServletRequest request = (HttpServletRequest) facesContext
				.getExternalContext().getRequest();
		log.info("init new perfil*********************************");
		log.info("request.getClass().getName():"
				+ request.getClass().getName());
		log.info("remoteUser:" + request.getRemoteUser());
		log.info("userPrincipalName:"
				+ (request.getUserPrincipal() == null ? "null" : request
						.getUserPrincipal().getName()));

		usuarioSession = usuarioRepository.findByLogin(request
				.getUserPrincipal().getName());
		// tituloPanel
		tituloPanel = "Perfil";
		modificar = false;
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

	public void modificarEmpresa() {
		try {
			usuarioRegistration.update(usuarioSession);

			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Empresa Modificada!", usuarioSession.getLogin()+"!");
			facesContext.addMessage(null, m);
			initNewPerfil();

		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Modificado Incorrecto.");
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

	public String getURLServletImage(){
		HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();  
		String urlPath = request.getRequestURL().toString();
		urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
		log.info("urlPath >> "+urlPath);
		return urlPath;
	}

	public void upload() {
		setModificar(true);
		log.info("upload()  file:"+file);
		if(file != null) {
			try{
				usuarioSession.setFotoPerfil(file.getContents());
				usuarioRegistration.update(usuarioSession);
				FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Modificado!", "Modificado successful");
				facesContext.addMessage(null, m);
				pushEventSucursal.fire(String.format("Usuario Modificado: %s (id: %d)", usuarioSession.getLogin(), usuarioSession.getId()));
			}catch(Exception e){

			}
		}
	}

	// ------------   get and set   -----------------------
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
	
	public void cambiarModificar(){
		setModificar(false);
	}

	public List<Usuario> getListUsuario() {
		return listUsuario;
	}

	public void setListUsuario(List<Usuario> listUsuario) {
		this.listUsuario = listUsuario;
	}

	public String getTest(){
		return "test";
	} 

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}
	
	public Usuario getUsuario() {
		return usuarioSession;
	}

	public void setUsuario(Usuario usuarioSession) {
		this.usuarioSession = usuarioSession;
	}
}
