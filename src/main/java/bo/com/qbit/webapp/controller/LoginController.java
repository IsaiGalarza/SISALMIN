package bo.com.qbit.webapp.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import bo.com.qbit.webapp.data.UsuarioRolRepository;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.model.security.UsuarioRol;
import bo.com.qbit.webapp.util.DateUtility;
import bo.com.qbit.webapp.util.FacesUtil;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "loginController")
@SuppressWarnings("serial")
@SessionScoped
public class LoginController implements Serializable {

	private @Inject SessionMain sessionMain; //variable del login
	private @Inject UsuarioRolRepository usuarioRolRepository;

	private String username;
	private String password;


	//temporal
	private StreamedContent fotoPerfilTemp;

	private UploadedFile file;

	private boolean modificar = false;

	@PostConstruct
	public void initNewLogin() {
		username = "";
		password = "";
	}

	public void login() {
		System.out.println(" ------- login() ----user="+username+"  |  pass="+password);
		if(username.isEmpty() || password.isEmpty()){
			System.out.println("login() -> Usuario o Password sin datos.");
			FacesUtil.errorMessage("Ingrear Usuario y Contraseña.");
			return; 
		}

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		Usuario usuarioSession = sessionMain.validarUsuario_(username, password);
		if(usuarioSession!=null){
			//validacion usuario eliminado
			if(usuarioSession.getState().equals("RM")){
				FacesUtil.errorMessage("Usuario no registrado");
				return;
			}
			//validacion usuario inactivo
			if(usuarioSession.getState().equals("IN")){
				FacesUtil.errorMessage("Usuario Inactivo");
				return;
			}
			try {
				if (request.getUserPrincipal() != null) {
					logout();
				}
				request.login(username, password);
				load(usuarioSession);
				try {
					context.getExternalContext().redirect(request.getContextPath() + "/pages/dashboard.xhtml");
				} catch (IOException ex) {
					context.addMessage(null, new FacesMessage("Error!", "Ocurrio un Error!"));
				}
			} catch (ServletException e) {
				System.out.println("login() -> "+ e.toString());
				context.addMessage(null, new FacesMessage("Error!", "Usuario o contraseña incorrecta"));
			}
		} else{
			System.out.println("login() -> No existe Usuario");
			FacesUtil.errorMessage("Revisar Usuario o Contraseña."); 
		}
	}

	private void load(Usuario usuario){
		UsuarioRol usuarioRolV1 = usuarioRolRepository.findByUsuario(usuario);
		sessionMain.setUsuarioLogin(usuario);
		sessionMain.cargarPermisos(usuarioRolV1.getRol());
		sessionMain.setImageUserSession();
	}

	public void logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession session = request.getSession(false);
		System.out.println( "User ({0}) Cerrando sesion #" + DateUtility.getCurrentDateTime()+" user"+ request.getUserPrincipal().getName());
		if (session != null) {
			session.invalidate();
			try {
				context.getExternalContext().redirect(request.getContextPath() + "/login.xhtml");
			} catch (IOException e) {
				System.out.println("logout() -> "+e.toString());
			}
		}
	}

	public void verificarTipoCambio(){
		System.out.println("verificarTipoCambio()");
		RequestContext.getCurrentInstance().execute("stickyTipoCambio()");
		int test = 0;
		if( 0 == test){
			//RequestContext.getCurrentInstance().execute("stickyTipoCambio()");
		}
	}

	public void upload() {
		setModificar(false);
		System.out.println("upload()  file:" + file);
		if ( file != null ) {
			InputStream is = null;
			String mimeType = "image/jpg";
			try{
				is = new ByteArrayInputStream(file.getContents());//file.getContents()
				fotoPerfilTemp = null;
				fotoPerfilTemp = new DefaultStreamedContent(new ByteArrayInputStream(toByteArrayUsingJava(is)), mimeType);
				sessionMain.setFotoPerfil(fotoPerfilTemp);
				Usuario user = sessionMain.getUsuarioLogin();
				user.setFotoPerfil(file.getContents());
				user.setPesoFoto(file.getContents().length);
				user.setFechaRegistro(new Date());
				sessionMain.setUsuarioLogin(user);
				sessionMain.actualizarrUsuario();
			}catch(Exception e){
				System.out.println("upload() -> error : "+e.getMessage());
			}
			FacesUtil.infoMessage("Foto perfil Cargada", "");
		}
	}

	private static byte[] toByteArrayUsingJava(InputStream is) throws IOException{ 
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = is.read();
		while(reads != -1){
			baos.write(reads); reads = is.read(); 
		}
		return baos.toByteArray();
	}

	// ----------- Getters and Setters ------------

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		System.out.println("username = "+username);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		System.out.println("password = "+password);
	}

	public StreamedContent getFotoPerfilTemp() {
		return fotoPerfilTemp;
	}

	public void setFotoPerfilTemp(StreamedContent fotoPerfilTemp) {
		this.fotoPerfilTemp = fotoPerfilTemp;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}
}
