package bo.com.qbit.webapp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.AlmacenProductoRepository;
import bo.com.qbit.webapp.data.AlmacenRepository;
import bo.com.qbit.webapp.data.DetalleTomaInventarioRepository;
import bo.com.qbit.webapp.data.TomaInventarioRepository;
import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.AlmacenProducto;
import bo.com.qbit.webapp.model.DetalleTomaInventario;
import bo.com.qbit.webapp.model.Proveedor;
import bo.com.qbit.webapp.model.TomaInventario;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.service.DetalleTomaInventarioRegistration;
import bo.com.qbit.webapp.service.TomaInventarioRegistration;
import bo.com.qbit.webapp.util.FacesUtil;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "tomaInventarioController")
@ConversationScoped
public class TomaInventarioController implements Serializable {

	private static final long serialVersionUID = 749163787421586877L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	private @Inject AlmacenRepository almacenRepository;
	private @Inject AlmacenProductoRepository almacenProductoRepository;
	private @Inject DetalleTomaInventarioRepository detalleTomaInventarioRepository;
	private @Inject TomaInventarioRepository tomaInventarioRepository;

	private @Inject TomaInventarioRegistration tomaInventarioRegistration;
	private @Inject DetalleTomaInventarioRegistration detalleTomaInventarioRegistration;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	@Inject
	private FacesContext facesContext;

	//ESTADOS

	private boolean crear = true;
	private boolean verProcesar = true;
	private boolean verReport = false;
	private boolean verButtonReport = false;
	private boolean revisarReport = false;
	private boolean verGuardar = false;

	private boolean verLista = true;//mostrar lista de tomas de inventario
	private boolean modificar = false;//verificar
	private boolean registrar = false;//mostrar maestro detalle

	private String tituloPanel = "Registrar Almacen";
	private String urlTomaInventario = "";

	//OBJECT
	private Almacen selectedAlmacen;
	private TomaInventario newTomaInventario;
	private TomaInventario selectedTomaInventario;

	//LIST
	private List<Usuario> listUsuario = new ArrayList<Usuario>();
	private List<Almacen> listaAlmacen = new ArrayList<Almacen>();
	private List<Proveedor> listaProveedor = new ArrayList<Proveedor>();
	private List<DetalleTomaInventario> listDetalleTomaInventario = new ArrayList<DetalleTomaInventario>();
	private List<TomaInventario> listTomaInventario = new ArrayList<TomaInventario>();
	private List<AlmacenProducto> listAlmacenProducto = new ArrayList<AlmacenProducto>();

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;
	private boolean atencionCliente=false;

	@PostConstruct
	public void initNewTomaInventario() {

		System.out.println(" ... initNewTomaInventario ...");

		usuarioSession = sessionMain.getUsuarioLogin().getLogin();

		selectedAlmacen = new Almacen();

		// tituloPanel
		tituloPanel = "Registrar Toma Inventario";

		crear = true;
		verProcesar = true;
		verReport = false;
		revisarReport = false;
		verGuardar = false;
		verButtonReport = false;

		//---
		verLista = true;
		modificar = false;
		registrar = false;

		listTomaInventario = tomaInventarioRepository.findAllOrderedByID();

		listDetalleTomaInventario = new ArrayList<DetalleTomaInventario>();
		listaAlmacen = almacenRepository.findAllOrderedByID();

		selectedTomaInventario = null;

		newTomaInventario = new TomaInventario();
		//newTomaInventario.setCorrelativo(cargarCorrelativo(listaOrdenIngreso.size()+1));
		newTomaInventario.setEstado("AC");
		newTomaInventario.setFechaRegistro(new Date());
		newTomaInventario.setUsuarioRegistro(usuarioSession);

	}

	public void cambiarAspecto(){
		verLista = false;
		modificar = false;
		registrar = true;
		crear = false;
	}

	public void cambiarAspectoModificar(){
		modificar = true;
		registrar = false;
		crear = false;
	}

	public void initConversation() {
		if (!FacesContext.getCurrentInstance().isPostback() && conversation.isTransient()) {
			conversation.begin();
			System.out.println(">>>>>>>>>> CONVERSACION INICIADA...");
		}
	}

	public String endConversation() {
		if (!conversation.isTransient()) {
			conversation.end();
			System.out.println(">>>>>>>>>> CONVERSACION TERMINADA...");
		}
		return "orden_ingreso.xhtml?faces-redirect=true";
	}

	//correlativo incremental por gestion
	private String cargarCorrelativo(int nroOrdenIngreso){
		//pather = "000001";
		//Date fecha = new Date(); 
		//String year = new SimpleDateFormat("yy").format(fecha);
		//String mes = new SimpleDateFormat("MM").format(fecha);
		return String.format("%06d", nroOrdenIngreso);
	}

	public void registrarTomaInventario() {
		try {
			Date fechaActual = new Date();
			newTomaInventario.setAlmacen(selectedAlmacen);
			newTomaInventario.setFechaRegistro(fechaActual);
			newTomaInventario = tomaInventarioRegistration.register(newTomaInventario);
			for(DetalleTomaInventario detalle : listDetalleTomaInventario){
				detalle.setTomaInventario(newTomaInventario);
				detalle.setFechaRegistro(fechaActual);
				detalle.setUsuarioRegistro(usuarioSession);
				detalleTomaInventarioRegistration.register(detalle);
			}

			FacesUtil.infoMessage("Toma Inventario Registrada!", "");
			initNewTomaInventario();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Registrar.");
		}
	}

	public void modificarTomaInvenario() {
		try {
			System.out.println("Ingreso a modificarOrdenIngreso: ");

			FacesUtil.infoMessage("Orden de Ingreso Modificada!", "");
			initNewTomaInventario();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Modificar.");
		}
	}

	public void eliminarTomaInventario() {
		try {
			System.out.println("Ingreso a eliminarOrdenIngreso: ");

			FacesUtil.infoMessage("Orden de Ingreso Eliminada!", "");
			initNewTomaInventario();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Eliminar.");
		}
	}
	
	public void procesarConsulta(){
		try {
			listAlmacenProducto = almacenProductoRepository.findByAlmacen(selectedAlmacen);
			listDetalleTomaInventario = new ArrayList<DetalleTomaInventario>();
			for(AlmacenProducto ap : listAlmacenProducto){
				DetalleTomaInventario detalle = new DetalleTomaInventario();
				detalle.setProducto(ap.getProducto());
				detalle.setCantidadRegistrada(ap.getStock());
				listDetalleTomaInventario.add(detalle);
			}
			verGuardar = listDetalleTomaInventario.size()>0?true:false;
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Procesar!");
		}
	}

	public void buttonRevisar(){
		try {
			newTomaInventario = selectedTomaInventario;
			selectedAlmacen = selectedTomaInventario.getAlmacen();
			listDetalleTomaInventario = new ArrayList<DetalleTomaInventario>();
			listDetalleTomaInventario = detalleTomaInventarioRepository.findByTomaInventario(selectedTomaInventario);

			verLista = false;
			modificar = true;
			registrar = false;
			verButtonReport = false;
		} catch (Exception e) {
			System.out.println("ERROR "+e.getMessage());
		}
	}

	public void verificacionTomaInventario(){
		try{
			newTomaInventario.setEstadoRevision("SI");
			newTomaInventario.setEstado("RE");
			newTomaInventario.setFechaRevision(new Date());
			tomaInventarioRegistration.updated(newTomaInventario);
			for(DetalleTomaInventario detalle : listDetalleTomaInventario){
				detalleTomaInventarioRegistration.updated(detalle);
			}
			FacesUtil.infoMessage("Toma Inventario Revisada", "");
			initNewTomaInventario();			
		}catch(Exception e){
			System.out.println("ERROR "+e.getMessage());
		}
	}

	public void cargarReporte(){
		try {
			urlTomaInventario = loadURL();
			verReport = true;
			verLista = false;
			modificar = false;
			registrar = false;
			revisarReport = false;
		} catch (Exception e) {
			FacesUtil.errorMessage("Proceso Incorrecto.");
		}
	}

	public String loadURL(){
		try{
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();  
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
			String urlPDFreporte = urlPath+"ReporteTomaInventario?pIdTomaInventario="+selectedTomaInventario.getId()+"&pIdEmpresa=1&pUsuario="+usuarioSession;
			return urlPDFreporte;
		}catch(Exception e){
			return "error";
		}
	}

	public void onRowSelectTomaInventarioClick(SelectEvent event){
		verButtonReport = true;
		crear = false;
		if(selectedTomaInventario.getEstadoRevision().equals("NO")){
			revisarReport = true;
		}else{
			revisarReport = false;
		}
	}

	public void modificarTomaInventario(){
		System.out.println("modificarDetalleOrdenIngreso ");

		FacesUtil.resetDataTable("formTableOrdenIngreso:itemsTable1");

	}

	// ONCOMPLETETEXT ALMACEN
	public List<Almacen> completeAlmacen(String query) {
		String upperQuery = query.toUpperCase();
		listaAlmacen = almacenRepository.findAllAlmacenForQueryNombre(upperQuery);
		return listaAlmacen;
	}

	public void onRowSelectAlmacenClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Almacen i : listaAlmacen){
			if(i.getNombre().equals(nombre)){
				selectedAlmacen = i;
				return;
			}
		}
	}

	// -------- get and set -------
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

	public Almacen getSelectedAlmacen() {
		return selectedAlmacen;
	}

	public void setSelectedAlmacen(Almacen selectedAlmacen) {
		this.selectedAlmacen = selectedAlmacen;
	}

	public List<Usuario> getListUsuario() {
		return listUsuario;
	}

	public void setListUsuario(List<Usuario> listUsuario) {
		this.listUsuario = listUsuario;
	}

	public boolean isAtencionCliente() {
		return atencionCliente;
	}

	public void setAtencionCliente(boolean atencionCliente) {
		this.atencionCliente = atencionCliente;
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

	public List<Almacen> getListaAlmacen() {
		return listaAlmacen;
	}

	public void setListaAlmacen(List<Almacen> listaAlmacen) {
		this.listaAlmacen = listaAlmacen;
	}

	public List<Proveedor> getListaProveedor() {
		return listaProveedor;
	}

	public void setListaProveedor(List<Proveedor> listaProveedor) {
		this.listaProveedor = listaProveedor;
	}

	public boolean isVerProcesar() {
		return verProcesar;
	}

	public void setVerProcesar(boolean verProcesar) {
		this.verProcesar = verProcesar;
	}

	public boolean isVerReport() {
		return verReport;
	}

	public void setVerReport(boolean verReport) {
		this.verReport = verReport;
	}

	public TomaInventario getNewTomaInventario() {
		return newTomaInventario;
	}

	public void setNewTomaInventario(TomaInventario newTomaInventario) {
		this.newTomaInventario = newTomaInventario;
	}

	public TomaInventario getSelectedTomaInventario() {
		return selectedTomaInventario;
	}

	public void setSelectedTomaInventario(TomaInventario selectedTomaInventario) {
		this.selectedTomaInventario = selectedTomaInventario;
	}

	public List<DetalleTomaInventario> getListDetalleTomaInventario() {
		return listDetalleTomaInventario;
	}

	public void setListDetalleTomaInventario(
			List<DetalleTomaInventario> listDetalleTomaInventario) {
		this.listDetalleTomaInventario = listDetalleTomaInventario;
	}

	public List<TomaInventario> getListTomaInventario() {
		return listTomaInventario;
	}

	public void setListTomaInventario(List<TomaInventario> listTomaInventario) {
		this.listTomaInventario = listTomaInventario;
	}

	public List<AlmacenProducto> getListAlmacenProducto() {
		return listAlmacenProducto;
	}

	public void setListAlmacenProducto(List<AlmacenProducto> listAlmacenProducto) {
		this.listAlmacenProducto = listAlmacenProducto;
	}

	public String getUrlTomaInventario() {
		return urlTomaInventario;
	}

	public void setUrlTomaInventario(String urlTomaInventario) {
		this.urlTomaInventario = urlTomaInventario;
	}

	public boolean isRevisarReport() {
		return revisarReport;
	}

	public void setRevisarReport(boolean revisarReport) {
		this.revisarReport = revisarReport;
	}

	public boolean isVerGuardar() {
		return verGuardar;
	}

	public void setVerGuardar(boolean verGuardar) {
		this.verGuardar = verGuardar;
	}

	public boolean isVerButtonReport() {
		return verButtonReport;
	}

	public void setVerButtonReport(boolean verButtonReport) {
		this.verButtonReport = verButtonReport;
	}

	public boolean isVerLista() {
		return verLista;
	}

	public void setVerLista(boolean verLista) {
		this.verLista = verLista;
	}

}
