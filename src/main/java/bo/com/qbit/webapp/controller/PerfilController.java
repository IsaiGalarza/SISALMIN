package bo.com.qbit.webapp.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.util.FacesUtil;
import bo.com.qbit.webapp.util.SessionMain;


@ManagedBean
public class PerfilController implements Serializable {

	
	private static final long serialVersionUID = -2989737706810995315L;

	private Usuario usuarioSession;

	private boolean modificar = false;
	private String tituloPanel = "Registrar Empresa";
	private List<Usuario> listUsuario = new ArrayList<Usuario>();
	private UploadedFile file;
	
	private String nombreUsuario; 
	private @Inject SessionMain sessionMain; //variable del login
	private Empresa empresaLogin;
	
	//temporal
	private StreamedContent fotoPerfilTemp;

	@PostConstruct
	public void initNewPerfil() {
		System.out.println("initNewPerfil()");
		setNombreUsuario(sessionMain.getUsuarioLogin().getLogin());
		setEmpresaLogin(sessionMain.getEmpresaLogin());
		// tituloPanel
		tituloPanel = "Perfil";
		modificar = false;
		fotoPerfilTemp = null;
	}

	public void upload() {
		setModificar(false);
		System.out.println("upload()  file:" + file);
		if ( file != null ) {
			//usuarioLogin.setFotoPerfil(file.getContents());
			//usuarioLogin.setPesoFoto(file.getContents().length);
			//usuarioLogin.setFechaRegistro(new Date());
			//usuarioRegistration.update(usuarioLogin);
			InputStream is = null;
			String mimeType = "image/jpg";
			try{
				is = new ByteArrayInputStream(file.getContents());//file.getContents()
				fotoPerfilTemp = new DefaultStreamedContent(new ByteArrayInputStream(toByteArrayUsingJava(is)), mimeType);
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
		InputStream is = null;
		String mimeType = "image/jpg";
		try{
			is = new ByteArrayInputStream(file.getContents());//file.getContents()
			fotoPerfilTemp = new DefaultStreamedContent(new ByteArrayInputStream(toByteArrayUsingJava(is)), mimeType);
		}catch(Exception e){
			System.out.println("setImageUserSession() -> error : "+e.getMessage());
		}
	}
	
	public Usuario getUsuario() {
		return usuarioSession;
	}

	public void setUsuario(Usuario usuarioSession) {
		this.usuarioSession = usuarioSession;
	}


	public StreamedContent getFotoPerfilTemp() {
		return fotoPerfilTemp;
	}


	public void setFotoPerfilTemp(StreamedContent fotoPerfilTemp) {
		this.fotoPerfilTemp = fotoPerfilTemp;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public Empresa getEmpresaLogin() {
		return empresaLogin;
	}

	public void setEmpresaLogin(Empresa empresaLogin) {
		this.empresaLogin = empresaLogin;
	}
}
