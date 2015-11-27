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
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.EmpresaRepository;
import bo.com.qbit.webapp.data.GestionRepository;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.service.EmpresaRegistration;
import bo.com.qbit.webapp.service.GestionRegistration;
import bo.com.qbit.webapp.util.FacesUtil;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "empresaController")
@ConversationScoped
public class EmpresaController implements Serializable {

	private static final long serialVersionUID = 5399619661135190257L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	@Inject
	private EmpresaRegistration empresaRegistration;

	@Inject
	private GestionRegistration gestionRegistration;

	@Inject
	private EmpresaRepository empresaRepository;

	@Inject
	private GestionRepository gesionRepository;

	private Logger log = Logger.getLogger(this.getClass());

	private Usuario usuarioSession;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	private boolean modificar = false;
	private Integer gestion;

	//login
	private @Inject SessionMain sessionMain; //variable del login
	private String nombreUsuario;

	@Produces
	@Named
	private Empresa newEmpresa;
	private Gestion newGestion;

	private List<Gestion> listGestion = new ArrayList<Gestion>();

	@PostConstruct
	public void initNewEmpresa() {

		log.info(" init new initNewEmpresa");
		beginConversation();
		usuarioSession = sessionMain.getUsuarioLogin();
		nombreUsuario = usuarioSession.getLogin();

		loadValuesDefault();
	}

	private void loadValuesDefault(){
		listGestion = gesionRepository.findAll();
		newGestion = new Gestion();
		this.nuevaGestion = String.valueOf(obtenerGestionActiva().getGestion());
		newEmpresa = empresaRepository.findAll().size()>0?empresaRepository.findAll().get(0):null;

		modificar = false;
	}

	private Gestion obtenerGestionActiva(){
		for(Gestion g: listGestion){
			if(g.getEstadoCierre().equals("AC")){
				return g;
			}
		}
		return null;
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
			Date fechaActual = new Date();
			newEmpresa.setFechaModificacion(fechaActual);
			empresaRegistration.update(newEmpresa);

			for(Gestion g: listGestion ){
				if(g.getId() == 0){//nueva gestion
					g.setEstado("AC");
					g.setUsuarioRegistro(nombreUsuario);
					g.setEmpresa(newEmpresa);
					g.setFechaRegistro(fechaActual);
					gestionRegistration.create(g);
				}else{
					g.setFechaModificacion(fechaActual);
					gestionRegistration.update(g);
				}
			}
			FacesUtil.infoMessage("Empresa Modificada", "Empresa "+newEmpresa.getRazonSocial());
			modificar = false;
			loadValuesDefault();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al modificar");
		}
	}
	
	private String nuevaGestion;

	public void registrarGestion(){
		for(Gestion g: listGestion){
			g.setEstadoCierre("CE");
		}
		newGestion.setId(0);
		newGestion.setEstadoCierre("AC");
		listGestion.add( newGestion);
	}

	public String urlServletLogoEmpresa(){
		String url = "";//FacesUtil.getUrlPath()+"ServletLogoEmpresa?idFormatoEmpresa="+formatoEmpresa.getId();
		log.info("url = "+url);
		return url;
	}

	public void modificarForm(){
		modificar = true;
	}

	public void actualizarForm(){
		modificar = false;
	}

	// ----------------   get and set  ----------------------

	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	public Gestion getNewGestion() {
		return newGestion;
	}

	public void setNewGestion(Gestion newGestion) {
		this.newGestion = newGestion;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public List<Gestion> getListGestion() {
		return listGestion;
	}

	public void setListGestion(List<Gestion> listGestion) {
		this.listGestion = listGestion;
	}

	public Integer getGestion() {
		return gestion;
	}

	public void setGestion(Integer gestion) {
		this.gestion = gestion;
	}

	public String getNuevaGestion() {
		return nuevaGestion;
	}

	public void setNuevaGestion(String nuevaGestion) {
		this.nuevaGestion = nuevaGestion;
		for(Gestion g: listGestion){
			g.setEstadoCierre("CE");
			if(g.getGestion() == Integer.parseInt(nuevaGestion)){
				g.setEstadoCierre("AC");
			}
		}
	}
}
