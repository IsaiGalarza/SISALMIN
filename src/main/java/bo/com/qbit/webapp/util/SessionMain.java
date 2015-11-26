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

import bo.com.qbit.webapp.data.EmpresaRepository;
import bo.com.qbit.webapp.data.GestionRepository;
import bo.com.qbit.webapp.data.PermisoRepository;
import bo.com.qbit.webapp.data.UsuarioRepository;
import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.model.security.PermisoV1;
import bo.com.qbit.webapp.model.security.Rol;

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

	//Object
	private Usuario usuarioLoggin;
	private Empresa empresaLoggin;
	private Gestion gestionLogin;
	private Almacen almacenLogin;

	private StreamedContent fotoPerfil;

	//list Permisos del usuario
	private List<PermisoV1> listPermiso;


	@PostConstruct
	public void initSessionMain(){
		log.info("----- initSessionMain() --------");
		listPermiso = new ArrayList<PermisoV1>();
		usuarioLoggin = null;
		empresaLoggin = null;
		gestionLogin = null;
		fotoPerfil = null;
		almacenLogin = null;
	}

	public Usuario validarUsuario(String username,String password){
		if(usuarioLoggin == null){
			setUsuarioLoggin(usuarioRepository.findByLogin(username, password));
		}
		return getUsuarioLoggin();
	}
	
	public Usuario validarUsuarioV2(String username,String password){
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
		if(pagina.equals("index.xhtml") || pagina.equals("index_.xhtml") || pagina.equals("profile.xhtml")  || pagina.equals("dashboard.xhtml") || pagina.equals("comprobante.xhtml")|| pagina.equals("permiso2.xhtml") || pagina.equals("certificacion.xhtml")){
			return true;
		}
		for(PermisoV1 p: listPermiso){
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
			log.info("----- setImageUserSession() --------");
			if(getUsuarioLoggin().getPesoFoto() == 0){
				this.usuarioLoggin.setFotoPerfil(toByteArrayUsingJava(getImageDefaul().getStream()));
				this.usuarioLoggin.setPesoFoto(32);
			}
		}catch(Exception e){
			log.info("setImageUserSession() - Error: "+e.getMessage());
		}
	}

	private StreamedContent getImageDefaul() {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream stream = classLoader
				.getResourceAsStream("avatar.jpg");
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

	public void cargarPermisos(Rol rol){
		listPermiso = permmisoRepository.findByRol(rol);
	}

	public List<PermisoV1> getListPermiso() {
		return listPermiso;
	}

	public void setListPermiso(List<PermisoV1> listPermiso) {
		this.listPermiso = listPermiso;
	}

	public Usuario getUsuarioLoggin() {
		return usuarioLoggin;
	}

	public void setUsuarioLoggin(Usuario usuarioLoggin) {
		this.usuarioLoggin = usuarioLoggin;
	}

	public Empresa getEmpresaLoggin() {
		if(empresaLoggin == null){
			String empresa= "";
			try{
				HttpSession request = (HttpSession) facesContext.getExternalContext().getSession(false);
				empresa = request.getAttribute("empresa")!=null?request.getAttribute("empresa").toString():"";
				if(! empresa.isEmpty()){
					empresaLoggin =  empresaRepository.findByRazonSocial(empresa);
					log.info("getEmpresaLoggin() -> empresaLoggin : "+empresaLoggin.getRazonSocial());
				}
			}catch(Exception e){
				empresaLoggin =  null;
				log.error("getEmpresaLoggin() ERROR: "+e.getMessage());
			}
		}
		return empresaLoggin;
	}

	public void setEmpresaLoggin(Empresa empresaLoggin) {
		this.empresaLoggin = empresaLoggin;
	}

	public StreamedContent getFotoPerfil() {
		log.info("----- getFotoPerfil() --------");
		if(fotoPerfil == null){
			log.info("----- fotoPerfil = null --------");
			String mimeType = "image/jpg";
			InputStream is = null;
			try{
				is= new ByteArrayInputStream(getUsuarioLoggin().getFotoPerfil());
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

}
