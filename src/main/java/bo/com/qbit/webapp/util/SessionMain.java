package bo.com.qbit.webapp.util;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.model.UploadedFile;

import bo.com.qbit.webapp.data.EmpresaRepository;
import bo.com.qbit.webapp.data.GestionRepository;
import bo.com.qbit.webapp.data.PermisoRepository;
import bo.com.qbit.webapp.data.UsuarioRepository;
import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.model.security.Accion;
import bo.com.qbit.webapp.model.security.Pagina;
import bo.com.qbit.webapp.model.security.Permiso;
import bo.com.qbit.webapp.model.security.Roles;
import bo.com.qbit.webapp.service.UsuarioRegistration;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class SessionMain, datos persistente durante la session del usuario
 * @author mauricio.bejarano.rivera
 *
 */

//sessionMain.usuarioLogin
@Named
@SessionScoped
public class SessionMain implements Serializable {

	private static final long serialVersionUID = 9114105051707972603L;
	
	private @Inject FacesContext facesContext;

	//Repository
	private @Inject PermisoRepository permmisoRepository;
	private @Inject UsuarioRepository usuarioRepository;
	private @Inject EmpresaRepository empresaRepository;
	private @Inject GestionRepository gestionRepository;

	private @Inject UsuarioRegistration usuarioRegistration;

	//Object
	private Usuario usuarioLogin;
	private Empresa empresaLogin;
	private Gestion gestionLogin;
	private Almacen almacenLogin;
	private UploadedFile file;

	private boolean modificar = false;

	//list Permisos del usuario
	private List<Permiso> listPermiso;


	@PostConstruct
	public void initSessionMain(){
		System.out.println("----- initSessionMain() --------");
		listPermiso = new ArrayList<Permiso>();
		usuarioLogin = null;
		empresaLogin = null;
		gestionLogin = null;
		almacenLogin = null;
	}

	public Usuario validarUsuario_(String username,String password){
		//if(usuarioLoggin == null){
		return	usuarioRepository.findByLogin(username, password);
		//}
		//return getUsuarioLoggin();
	}

	/**
	 * Verifica si la pagina tiene permiso de acceso
	 * @param pagina
	 * @return boolean
	 */
	public boolean tienePermisoPagina(String pagina){
		if( pagina.equals("profile.xhtml")  || pagina.equals("dashboard.xhtml") || pagina.equals("manual_usuario.xhtml")){
			return true;
		}
		for(Permiso p: listPermiso){
			if(p.getDetallePagina().getPagina().getPath().equals(pagina)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Verifica si la pagina tiene permiso de acceso
	 * @param pagina
	 * @return boolean
	 */
	public boolean tienePermisoPaginaAccion(String pagina,String accion){
		for(Permiso p: listPermiso){
			Accion accionAux = p.getDetallePagina().getAccion();
			Pagina paginaAux = p.getDetallePagina().getPagina();
			if(paginaAux.getNombre().equals(pagina) && accionAux.getNombre().equals(accion)){
				return true;
			}
		}
		return false;
	}

	public String getParameterRequest(String name){
		HttpServletRequest request = (HttpServletRequest) facesContext
				.getExternalContext().getRequest();
		return request.getParameter(name);
	}

	public void setAttributeSession(String key,String value){
		try{
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			session.setAttribute(key, value);
		}catch(Exception e){
			System.out.println("setAttributeSession() ERROR: "+e.getMessage());
		}		
	}

	public String getAttributeSession(String key){
		try {
			HttpSession request = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			return request.getAttribute(key)!=null ? (String) request.getAttribute(key):null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean removeAttributeSession(String key){
		try {
			HttpSession request = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			request.removeAttribute(key);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//----------------------------------------

	public void cargarPermisos(Roles rol){
		listPermiso = permmisoRepository.findByRol(rol);
	}

	public List<Permiso> getListPermiso() {
		return listPermiso;
	}

	public void setListPermiso(List<Permiso> listPermiso) {
		this.listPermiso = listPermiso;
	}

	public Usuario getUsuarioLogin() {
		return usuarioLogin;
	}

	public void setUsuarioLogin(Usuario usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}

	public void actualizarrUsuario(){
		usuarioRegistration.update(usuarioLogin);
	}

	public Empresa getEmpresaLogin() {
		if(empresaLogin == null){
			try{
				empresaLogin = empresaRepository.findById(1);
			}catch(Exception e){
				empresaLogin =  null;
				System.out.println("getEmpresaLoggin() ERROR: "+e.getMessage());
			}
		}
		return empresaLogin;
	}

	public void setEmpresaLogin(Empresa empresaLogin) {
		this.empresaLogin = empresaLogin;
	}

	public Gestion getGestionLogin() {
		if(gestionLogin == null){
			this.gestionLogin = gestionRepository.findByGestionCierreActivo();
		}
		return gestionLogin;
	}
	
	public void setGestionLogin(Gestion gestionLogin){
		this.gestionLogin = gestionLogin;
	}

	public Almacen getAlmacenLogin() {
		if(almacenLogin == null){

		}
		return almacenLogin;
	}

	public void setAlmacenLogin(Almacen almacenLogin) {
		this.almacenLogin = almacenLogin;
	}

	public UploadedFile getFile() {
		System.out.println("getFile "+file);
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;

		System.out.println("setFile "+file);
	}

	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}


}
