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
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.ProveedorRepository;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.Proveedor;
import bo.com.qbit.webapp.service.EstadoUsuarioLogin;
import bo.com.qbit.webapp.service.ProveedorRegistration;

@Named(value = "proveedorController")
@SuppressWarnings("serial")
@ConversationScoped
public class ProveedorController implements Serializable {

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	private FacesContext facesContext;

	@Inject
	Conversation conversation;

	@Inject
	private ProveedorRegistration proveedorRegistration;

	@Inject
	private ProveedorRepository proveedorRepository;

	Logger log = Logger.getLogger(ProveedorController.class);

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//estados
	private boolean crear = true;
	private boolean registrar = false;
	private boolean modificar = false;

	private String tituloPanel = "Registrar Proveedor";
	private String nombreEstado="ACTIVO";
	private String textoAutoCompleteCuenta;
	private String textoAutoCompleteCuentaServicio;
	private String textoAutoCompleteCuentaAnticipo;
	private String tipoColumnTable = "col-md-12"; //8

	private List<Proveedor> listProveedor  = new ArrayList<Proveedor>();
	private List<Proveedor> listFilterProveedor  = new ArrayList<Proveedor>();

	private String[] listEstado = {"ACTIVO","INACTIVO"};
	private String[] listTipoProveedor = {"NACIONAL","EXTRANJERA"};

	@Produces
	@Named
	private Proveedor newProveedor;
	private Proveedor selectedProveedor;

	//login
	private String nombreUsuario;	
	private EstadoUsuarioLogin estadoUsuarioLogin;
	private Empresa empresaLogin;

	@Produces
	@Named
	public List<Proveedor> getListProveedor() {
		return listProveedor;
	}

	@PostConstruct
	public void initNewProveedor() {
		log.info(" init new initNewSucursal");
		beginConversation();		
		estadoUsuarioLogin = new EstadoUsuarioLogin(facesContext);
		nombreUsuario =  estadoUsuarioLogin.getNombreUsuarioSession();
		loadValuesDefaul();
	}

	private void loadValuesDefaul(){
		crear = true;
		registrar = false;
		modificar = false;

		newProveedor = new Proveedor();
		selectedProveedor = new Proveedor();
		textoAutoCompleteCuenta = "";
		textoAutoCompleteCuentaAnticipo = "";
		// tituloPanel
		tituloPanel = "Registrar Sucursal";
		// traer todos por Empresa ordenados por ID Desc
		listProveedor = proveedorRepository.findAllByEmpresa();
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

	public void registrar() {
		try {
			//proveedor
			String estado = nombreEstado.equals("ACTIVO")?"AC":"IN";
			newProveedor.setEstado(estado);
			newProveedor.setUsuarioRegistro(nombreUsuario);
			newProveedor.setFechaRegistro(new Date());
			newProveedor = proveedorRegistration.create(newProveedor);
			
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Proveedor Registrado!", newProveedor.getNombre()+"!");
			facesContext.addMessage(null, m);

			loadValuesDefaul();
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Registro Incorrecto.");
			facesContext.addMessage(null, m);
			log.info("registrar() ERROR: "+errorMessage); 
		}
	}

	public void modificar() {
		try {
			//proveedor
			String estado = nombreEstado.equals("ACTIVO")?"AC":"IN";
			newProveedor.setEstado(estado);
			proveedorRegistration.update(newProveedor);
			newProveedor.setUsuarioRegistro(nombreUsuario);
			newProveedor.setFechaRegistro(new Date());
			

			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Proveedor Modificado!", newProveedor.getNombre()+"!");
			facesContext.addMessage(null, m);
			crear = true;
			registrar = false;
			modificar = false;
			tipoColumnTable = "col-md-8";
			resetearFitrosTabla("formTableProveedor:dataTableProveedor");
			loadValuesDefaul();
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Modificado Incorrecto.");
			facesContext.addMessage(null, m);
			log.info("modificar() ERROR: "+errorMessage); 
		}
	}

	public void eliminar() {
		try {
			//proveedor
			newProveedor.setEstado("RM");
			proveedorRegistration.update(newProveedor);			

			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Proveedor Eliminado!", newProveedor.getNombre()+"!");
			facesContext.addMessage(null, m);
			crear = false;
			registrar = true;
			modificar = false;
			tipoColumnTable = "col-md-8";
			resetearFitrosTabla("formTableProveedor:dataTableProveedor");
		
			loadValuesDefaul();
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Borrado Incorrecto.");
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

	public void actualizarForm(){
		crear = true;
		registrar = false;
		modificar = false;
		tipoColumnTable = "col-md-12";
		newProveedor = new Proveedor();
		resetearFitrosTabla("formTableProveedor:dataTableProveedor");
		selectedProveedor = new Proveedor();
	}

	public void onRowSelect(SelectEvent event) {
		newProveedor = new Proveedor();
		newProveedor = selectedProveedor;
		nombreEstado = newProveedor.getEstado().equals("AC")?"ACTIVO":"INACTIVO";
	
		crear = false;
		registrar = false;
		modificar = true;
		tipoColumnTable = "col-md-8";
		resetearFitrosTabla("formTableProveedor:dataTableProveedor");
		
	}


	public void resetearFitrosTabla(String id) {
		DataTable table = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(id);
		table.setSelection(null);
		table.reset();
	}

	public void cambiarAspecto(){
		crear = false;
		registrar = true;
		modificar = false;
		tipoColumnTable = "col-md-8";
		resetearFitrosTabla("formTableProveedor:dataTableProveedor");
		
	}

	public void actualizarFormReg(){
		crear = true;
		registrar = false;
		modificar = false;
		setTipoColumnTable("col-md-12");
		resetearFitrosTabla("formTableProveedor:dataTableProveedor");
		newProveedor = new Proveedor();
		
		textoAutoCompleteCuenta = "";
		textoAutoCompleteCuentaServicio = "";
		textoAutoCompleteCuentaAnticipo = "";
		selectedProveedor = new Proveedor();
	}

	//  ---- get and set -----
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

	public Proveedor getSelectedProveedor() {
		return selectedProveedor;
	}

	public void setSelectedProveedor(Proveedor selectedProveedor) {
		this.selectedProveedor = selectedProveedor;
	}

	public String getTest(){
		return "test";
	}

	public Empresa getEmpresaLogin() {
		return empresaLogin;
	}

	public void setEmpresaLogin(Empresa empresaLogin) {
		this.empresaLogin = empresaLogin;
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

	public List<Proveedor> getListFilterProveedor() {
		return listFilterProveedor;
	}

	public void setListFilterProveedor(List<Proveedor> listFilterProveedor) {
		this.listFilterProveedor = listFilterProveedor;
	}

	public String getTextoAutoCompleteCuenta() {
		return textoAutoCompleteCuenta;
	}

	public void setTextoAutoCompleteCuenta(String textoAutoCompleteCuenta) {
		this.textoAutoCompleteCuenta = textoAutoCompleteCuenta;
	}

	

	public String[] getListTipoProveedor() {
		return listTipoProveedor;
	}

	public void setListTipoProveedor(String[] listTipoProveedor) {
		this.listTipoProveedor = listTipoProveedor;
	}

	public String getTextoAutoCompleteCuentaAnticipo() {
		return textoAutoCompleteCuentaAnticipo;
	}

	public void setTextoAutoCompleteCuentaAnticipo(
			String textoAutoCompleteCuentaAnticipo) {
		this.textoAutoCompleteCuentaAnticipo = textoAutoCompleteCuentaAnticipo;
	}

	

	public String getTextoAutoCompleteCuentaServicio() {
		return textoAutoCompleteCuentaServicio;
	}

	public void setTextoAutoCompleteCuentaServicio(
			String textoAutoCompleteCuentaServicio) {
		this.textoAutoCompleteCuentaServicio = textoAutoCompleteCuentaServicio;
	}


}
