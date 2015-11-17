package bo.com.qbit.webapp.controller;

import java.io.IOException;
import java.io.Serializable;

import org.apache.log4j.Logger;

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

import bo.com.qbit.webapp.data.UsuarioRolRepository;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.model.security.UsuarioRolV1;
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

	Logger log = Logger.getLogger(this.getClass());

	@PostConstruct
	public void initNewLogin() {
		username = "";
		password = "";

	}

	public void login() {
		log.info(" ------- login() ----user="+username+"  |  pass="+password);
		if(username.isEmpty() || password.isEmpty()){
			log.info("login() -> Usuario o Password sin datos.");
			FacesUtil.errorMessage("Ingrear Usuario y Contraseña.");
			return; 
		}
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		Usuario usuarioSession = sessionMain.validarUsuarioV2(username, password);
		if(usuarioSession!=null){
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
				log.error("login() -> "+ e.toString());
				context.addMessage(null, new FacesMessage("Error!", "Usuario o contraseña incorrecta"));
			}
		} else{
			log.info("login() -> No existe Usuario");
			FacesUtil.errorMessage("Revisar Usuario o Contraseña."); 
		}
	}

	private void load(Usuario usuario){
		UsuarioRolV1 usuarioRolV1 = usuarioRolRepository.findByUsuarioV1(usuario);
		sessionMain.setUsuarioLoggin(usuario);
		sessionMain.cargarPermisos(usuarioRolV1.getRol());
		sessionMain.setImageUserSession();
	}
	
	public void logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession session = request.getSession(false);
		log.info( "User ({0}) Cerrando sesion #" + DateUtility.getCurrentDateTime()+" user"+ request.getUserPrincipal().getName());
		if (session != null) {
			session.invalidate();
			try {
				context.getExternalContext().redirect(request.getContextPath() + "/login.xhtml");
			} catch (IOException e) {
				log.error("logout() -> "+e.toString());
			}
		}
	}

	public void verificarTipoCambio(){
		log.info("verificarTipoCambio()");
		RequestContext.getCurrentInstance().execute("stickyTipoCambio()");
		int test = 0;
		if( 0 == test){
			//RequestContext.getCurrentInstance().execute("stickyTipoCambio()");
		}
	}
	
	// ----------- Getters and Setters ------------

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		log.info("username = "+username);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		log.info("password = "+password);
	}
}
