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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.EmpresaRepository;
import bo.com.qbit.webapp.data.MonedaEmpresaRepository;
import bo.com.qbit.webapp.data.ParametroEmpresaRepository;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.Moneda;
import bo.com.qbit.webapp.model.MonedaEmpresa;
import bo.com.qbit.webapp.model.ParametroEmpresa;
import bo.com.qbit.webapp.service.EstadoUsuarioLogin;
import bo.com.qbit.webapp.service.MonedaEmpresaRegistration;

@Named(value = "parametroEmpresaController")
@SuppressWarnings("serial")
@ConversationScoped
public class ParamaetroEmpresaController implements Serializable {

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	private FacesContext facesContext;

	@Inject
	Conversation conversation;

	@Inject
	private EmpresaRepository empresaRepository;

	@Inject
	private ParametroEmpresaRepository parametroEmpresaRepository;

	@Inject
	private MonedaEmpresaRepository monedaEmpresaRepository;

	@Inject
	private MonedaEmpresaRegistration monedaEmpresaRegistration;

	Logger log = Logger.getLogger(ParamaetroEmpresaController.class);

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//estados
	private boolean crear = true;
	private boolean registrar = false;
	private boolean modificar = false;

	private String tituloPanel = "Registrar Empresa";
	private String nombreEstado="ACTIVO";
	private String tipoColumnTable = "col-md-12"; //8
	//private String simboloMonedaNacional;
	//private String simboloMonedaExtranjera;

	private String[] listEstado = {"ACTIVO","INACTIVO"};
	private List<MonedaEmpresa> listMonedaEmpresa = new ArrayList<MonedaEmpresa>();

	//login
	private String nombreUsuario;	
	private EstadoUsuarioLogin estadoUsuarioLogin;
	private Empresa empresaLogin;

	private ParametroEmpresa selectedParametroEmpresa;
	private MonedaEmpresa monedaEmpresaNacional;
	private MonedaEmpresa monedaEmpresaExtranjera;
	private Moneda selectedMonedaNacional;
	private Moneda selectedMonedaExtranjera;

	@PostConstruct
	public void initNewSucursal() {
		log.info(" init new initNewSucursal controller");
		beginConversation();		
		estadoUsuarioLogin = new EstadoUsuarioLogin(facesContext);
		nombreUsuario =  estadoUsuarioLogin.getNombreUsuarioSession();
		empresaLogin = estadoUsuarioLogin.getEmpresaSession(empresaRepository);

		// tituloPanel
		tituloPanel = "Parametro Empresa";
		listMonedaEmpresa = monedaEmpresaRepository.findAllByEmpresa(empresaLogin);
		loadDefault();
	}

	private void loadDefault(){
		monedaEmpresaNacional = new MonedaEmpresa();
		selectedParametroEmpresa = parametroEmpresaRepository.findByEmpresa(empresaLogin);
		if(listMonedaEmpresa.size()>0){
			selectedMonedaNacional = obtenerMonedaByTipo("NACIONAL").getMoneda();//listMoneda.get(0);
			//simboloMonedaNacional = selectedMonedaNacional.getSimboloReferencial();
			selectedMonedaExtranjera = obtenerMonedaByTipo("EXTRANJERA").getMoneda();//listMoneda.get(1);
			//simboloMonedaExtranjera = selectedMonedaExtranjera.getSimboloReferencial();
		}else{
			//cargar por default una moneda nacional y una moneda extranjera
		}
	}

	private MonedaEmpresa obtenerMonedaByTipo(String tipo){
		for(MonedaEmpresa m : listMonedaEmpresa){
			if(m.getTipo().equals(tipo)){
				return m;
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

	public void registrar() {
		try {
			Date fechaActual = new Date();
			monedaEmpresaNacional.setEmpresa(empresaLogin);
			monedaEmpresaNacional.setMoneda(selectedMonedaNacional);
			//monedaEmpresaNacional.setSimbolo(simboloMonedaNacional);
			monedaEmpresaNacional.setTipo("NACIONAL");
			monedaEmpresaNacional.setEstado("AC");
			monedaEmpresaNacional.setFechaRegistro(fechaActual);
			monedaEmpresaExtranjera.setEmpresa(empresaLogin);
			monedaEmpresaExtranjera.setMoneda(selectedMonedaExtranjera);
			//monedaEmpresaExtranjera.setSimbolo(simboloMonedaExtranjera);
			monedaEmpresaExtranjera.setTipo("EXTRANJERA");
			monedaEmpresaExtranjera.setEstado("AC");
			monedaEmpresaExtranjera.setFechaRegistro(fechaActual);
			//register
			monedaEmpresaNacional = monedaEmpresaRegistration.create(monedaEmpresaNacional);
			monedaEmpresaExtranjera = monedaEmpresaRegistration.create(monedaEmpresaExtranjera);

			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Parametros Registrados!","");
			facesContext.addMessage(null, m);

			initNewSucursal();
		} catch (Exception e) {
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					e.getMessage(), "Registro Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}

	public void modificar() {
		try {
			Date fechaActual = new Date();
			monedaEmpresaNacional.setMoneda(selectedMonedaNacional);
			//monedaEmpresaNacional.setSimbolo(simboloMonedaNacional);
			//fecha modificacion nacional
			monedaEmpresaExtranjera.setMoneda(selectedMonedaExtranjera);
			//monedaEmpresaExtranjera.setSimbolo(simboloMonedaExtranjera);
			// fecha modificacion extranjera
			monedaEmpresaNacional = monedaEmpresaRegistration.update(monedaEmpresaNacional);
			monedaEmpresaExtranjera = monedaEmpresaRegistration.update(monedaEmpresaExtranjera);
			selectedParametroEmpresa.setFechaModificacion(fechaActual);

			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Parametros Modificados!","");
			facesContext.addMessage(null, m);
			crear = false;
			registrar = true;
			modificar = false;
			tipoColumnTable = "col-md-8";
			resetearFitrosTabla("formTableSucursal:dataTableSucursal");
			initNewSucursal();
		} catch (Exception e) {
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					e.getMessage(), "Modificado Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}

	public void eliminar() {
		try {
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Sucusarl Eliminada!", "!");
			facesContext.addMessage(null, m);
			crear = false;
			registrar = true;
			modificar = false;
			tipoColumnTable = "col-md-8";
			resetearFitrosTabla("formTableSucursal:dataTableSucursal");
			initNewSucursal();
		} catch (Exception e) {
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					e.getMessage(), "Borrado Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}

	public void actualizarForm(){
		crear = true;
		registrar = false;
		modificar = false;
		tipoColumnTable = "col-md-12";
		//resetearFitrosTabla("formTableSucursal:dataTableSucursal");
	}

	public void onRowSelect(SelectEvent event) {
		crear = false;
		registrar = false;
		modificar = true;
		tipoColumnTable = "col-md-8";
		//resetearFitrosTabla("formTableSucursal:dataTableSucursal");
		//resetearFitrosTabla("formTableDosificacion:dataTableDosificacion");
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
		//resetearFitrosTabla("formTableSucursal:dataTableSucursal");
	}

	// -------------get and set---------
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

	public ParametroEmpresa getSelectedParametroEmpresa() {
		return selectedParametroEmpresa;
	}

	public void setSelectedParametroEmpresa(ParametroEmpresa selectedParametroEmpresa) {
		this.selectedParametroEmpresa = selectedParametroEmpresa;
	}

	public List<MonedaEmpresa> getListMonedaEmpresa() {
		return listMonedaEmpresa;
	}

	public void setListMonedaEmpresa(List<MonedaEmpresa> listMonedaEmpresa) {
		this.listMonedaEmpresa = listMonedaEmpresa;
	}

	public Moneda getSelectedMonedaNacional() {
		return selectedMonedaNacional;
	}

	public void setSelectedMonedaNacional(Moneda selectedMonedaNacional) {
		this.selectedMonedaNacional = selectedMonedaNacional;
	}

	public Moneda getSelectedMonedaExtranjera() {
		return selectedMonedaExtranjera;
	}

	public void setSelectedMonedaExtranjera(Moneda selectedMonedaExtranjera) {
		this.selectedMonedaExtranjera = selectedMonedaExtranjera;
	}

	public MonedaEmpresa getMonedaEmpresaNacional() {
		return monedaEmpresaNacional;
	}

	public void setMonedaEmpresaNacional(MonedaEmpresa monedaEmpresaNacional) {
		this.monedaEmpresaNacional = monedaEmpresaNacional;
	}

	public MonedaEmpresa getMonedaEmpresaExtranjera() {
		return monedaEmpresaExtranjera;
	}

	public void setMonedaEmpresaExtranjera(MonedaEmpresa monedaEmpresaExtranjera) {
		this.monedaEmpresaExtranjera = monedaEmpresaExtranjera;
	}

}
