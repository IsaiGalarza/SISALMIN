package bo.com.qbit.webapp.util;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import bo.com.qbit.webapp.data.EmpresaRepository;
import bo.com.qbit.webapp.data.GestionRepository;
import bo.com.qbit.webapp.data.PermisoRepository;
import bo.com.qbit.webapp.data.UsuarioRepository;
import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.model.security.Permiso;
import bo.com.qbit.webapp.model.security.Roles;
import bo.com.qbit.webapp.service.UsuarioRegistration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
@SuppressWarnings("serial")
@SessionScoped
public class SessionMain implements Serializable {

	private @Inject FacesContext facesContext;
	private Logger log = Logger.getLogger(this.getClass());

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

	private StreamedContent fotoPerfil;

	//list Permisos del usuario
	private List<Permiso> listPermiso;


	@PostConstruct
	public void initSessionMain(){
		System.out.println("----- initSessionMain() --------");
		listPermiso = new ArrayList<Permiso>();
		usuarioLogin = null;
		empresaLogin = null;
		gestionLogin = null;
		fotoPerfil = null;
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
		if( pagina.equals("profile.xhtml")  || pagina.equals("dashboard.xhtml") ){
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
	 * cargar foto del usuario
	 */
	public void setImageUserSession() {
		try{
			System.out.println("----- setImageUserSession() --------");
			if(getUsuarioLogin().getPesoFoto() == 0){
				this.usuarioLogin.setFotoPerfil(toByteArrayUsingJava(getImageDefault().getStream()));
				this.usuarioLogin.setPesoFoto(32);
			}
		}catch(Exception e){
			System.out.println("setImageUserSession() - Error: "+e.getMessage());
		}
	}

	public StreamedContent getImagen(){
		return getImageDefault();
	}

	private StreamedContent getImageDefault() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream stream = classLoader.getResourceAsStream("avatar.jpg");
		return new DefaultStreamedContent(stream, "image/jpeg");
	}

	private static byte[] toByteArrayUsingJava(InputStream is) throws IOException{ 
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = is.read();
		while(reads != -1){
			baos.write(reads); reads = is.read(); 
		}
		return baos.toByteArray();
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
			log.error("setAttributeSession() ERROR: "+e.getMessage());
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
			String empresa= "";
			try{
				HttpSession request = (HttpSession) facesContext.getExternalContext().getSession(false);
				empresa = request.getAttribute("empresa")!=null?request.getAttribute("empresa").toString():"";
				if(! empresa.isEmpty()){
					empresaLogin =  empresaRepository.findByRazonSocial(empresa);
					System.out.println("getEmpresaLoggin() -> empresaLoggin : "+empresaLogin.getRazonSocial());
				}
			}catch(Exception e){
				empresaLogin =  null;
				log.error("getEmpresaLoggin() ERROR: "+e.getMessage());
			}
		}
		return empresaLogin;
	}

	public void setEmpresaLoggin(Empresa empresaLoggin) {
		this.empresaLogin = empresaLoggin;
	}

	public StreamedContent getFotoPerfil() {
		System.out.println("----- getFotoPerfil() fotoPerfil="+fotoPerfil+" --------");
		if(fotoPerfil == null){
			System.out.println("----- fotoPerfil = null --------");
			String mimeType = "image/jpg";
			InputStream is = null;
			try{
				is= new ByteArrayInputStream(getUsuarioLogin().getFotoPerfil());
				fotoPerfil = new DefaultStreamedContent(new ByteArrayInputStream(toByteArrayUsingJava(is)), mimeType);
			}catch(Exception e){
				log.error("getImageUserSession() -> error : "+e.getMessage());
				return null;
			}
		}
		return fotoPerfil;
	}

	public void setFotoPerfil(StreamedContent fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}

	public Gestion getGestionLogin() {
		if(gestionLogin == null){
			this.gestionLogin = gestionRepository.findByGestionCierreActivo();
		}
		return gestionLogin;
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
