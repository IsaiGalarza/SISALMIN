package bo.com.qbit.webapp.service;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import bo.com.qbit.webapp.data.EmpresaRepository;
import bo.com.qbit.webapp.data.UsuarioRepository;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.Usuario;

public class EstadoUsuarioLogin implements Serializable{

	private static final long serialVersionUID = 5793623941703126286L;
	private FacesContext facesContext;

	public EstadoUsuarioLogin(FacesContext facesContext){
		this.facesContext = facesContext;
	}

	public Empresa getEmpresaSession(EmpresaRepository empresaRepository){
		String empresa= "";
		try{
			HttpSession request = (HttpSession) facesContext.getExternalContext().getSession(false);
			empresa = request.getAttribute("empresa")!=null?request.getAttribute("empresa").toString():"";
			if(! empresa.isEmpty()){
				return empresaRepository.findByRazonSocial(empresa);
			}
			return null;
		}catch(Exception e){
			Logger.getLogger(getClass().getName()).log(
					Level.SEVERE, "getEmpresaSession()...", e);
			return null;
		}
	}

	public String getNombreUsuarioSession(){
		HttpServletRequest request1 = (HttpServletRequest) facesContext
				.getExternalContext().getRequest();
		return request1.getUserPrincipal() == null ? "null" : request1.getUserPrincipal().getName();
	}

	public Usuario getUsuarioSession(UsuarioRepository usuarioRepository){
		try{
			return usuarioRepository.findByLogin(getNombreUsuarioSession());
		}catch(Exception e){
			Logger.getLogger(getClass().getName()).log(
					Level.SEVERE, "getUsuarioSession()...", e);
			return null;
		}
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
			Logger.getLogger(getClass().getName()).log(
					Level.SEVERE, "setAttributeSession()...", e);
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

}
