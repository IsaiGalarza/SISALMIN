package bo.com.qbit.webapp.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.DetalleOrdenCompraRepository;
import bo.com.qbit.webapp.data.EmpresaRepository;
import bo.com.qbit.webapp.data.OrdenCompraRepository;
import bo.com.qbit.webapp.data.ProveedorRepository;
import bo.com.qbit.webapp.data.TipoComprobanteRepository;
import bo.com.qbit.webapp.model.Compra;
import bo.com.qbit.webapp.model.DetalleOrdenCompra;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.OrdenCompra;
import bo.com.qbit.webapp.model.Proveedor;
import bo.com.qbit.webapp.model.Servicio;
import bo.com.qbit.webapp.model.TipoComprobante;
import bo.com.qbit.webapp.service.CompraRegistration;
import bo.com.qbit.webapp.service.DetalleOrdenCompraRegistration;
import bo.com.qbit.webapp.service.EstadoUsuarioLogin;
import bo.com.qbit.webapp.service.OrdenCompraRegistration;
import bo.com.qbit.webapp.util.Fechas;
import bo.com.qbit.webapp.util.NumerosToLetras;

@Named(value = "ordenCompraController")
@ConversationScoped
@SuppressWarnings("serial")
public class OrdenCompraController implements Serializable {

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	private FacesContext facesContext;

	@Inject
	private Conversation conversation;

	@Inject
	private EmpresaRepository empresaRepository;

	@Inject
	private OrdenCompraRepository ordenCompraRepository;

	@Inject
	private ProveedorRepository proveedorRepository;

	@Inject
	private OrdenCompraRegistration ordenCompraRegistration;

	@Inject
	private TipoComprobanteRepository tipoComprobanteRepository;

	@Inject
	private DetalleOrdenCompraRepository detalleOrdenCompraRepository;

	@Inject
	private DetalleOrdenCompraRegistration detalleOrdenCompraRegistration;
	
	@Inject
	private CompraRegistration compraRegistration;

	Logger log = Logger.getLogger(OrdenCompraController.class);

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	private Event<String> pushEventSucursal;

	//estados
	private boolean crear = true;
	private boolean registrar = false;
	private boolean modificar = false;

	//estado orden compra ya registradas
	private boolean ordenCompraSeleccionada ;

	private String tituloPanel = "Registrar Compra";
	private String nombreProveedor;
	private String tipoColumnTable = "col-md-12"; //8
	private String nombreUsuario;
	private String nombreTipoCompra;

	//BienServicio
	private String textoServicio;
	private Integer cantidad = 1;
	private double totalImportePorServicio;
	private double descuento = 0;
	private double totalImporte = 0;

	//Login
	private Empresa empresaLogin;
	private EstadoUsuarioLogin estadoUsuarioLogin;

	//Object Entity
	private OrdenCompra newOrdenCompra;
	private OrdenCompra selectedOrdenCompra;
	private Proveedor selectedProveedor;
	private Servicio servicioXOrdenCompra;
	
	//libro de compra
	private Compra libroCompra;
	private List<Proveedor> listProveedor = new ArrayList<Proveedor>();
	private List<OrdenCompra> listOrdenCompra = new ArrayList<OrdenCompra>();
	private List<OrdenCompra> listFilterOrdenCompra = new ArrayList<OrdenCompra>();
	private List<Servicio> listServicioXOrdenCompra = new ArrayList<Servicio>();
	private String[] listEstado = {"ACTIVO","INACTIVO"};
	

	@PostConstruct
	public void initNewOrdenCompra() {

		log.info(" init new OrdenCompraController");
		beginConversation();
		estadoUsuarioLogin = new EstadoUsuarioLogin(facesContext);
		nombreUsuario =  estadoUsuarioLogin.getNombreUsuarioSession();

		// tituloPanel
		tituloPanel = "Orden de Compra";

		modificar = false;
		loadDefault();
	}

	private void loadDefault(){
		crear = true;
		registrar = false;
		modificar = false;

		ordenCompraSeleccionada = false;

		libroCompra = new Compra();
		newOrdenCompra = new OrdenCompra();
		selectedOrdenCompra = new OrdenCompra();
		selectedProveedor = new Proveedor();
		servicioXOrdenCompra = new Servicio();
		
		listOrdenCompra = ordenCompraRepository.findAllByEmpresa(empresaLogin);
		listProveedor = proveedorRepository.findAllActivasByEmpresa(empresaLogin);
		selectedProveedor = listProveedor.size()>0?listProveedor.get(0):new Proveedor();
		if(selectedProveedor.getId()!=0){
			nombreProveedor = selectedProveedor.getDescripcion();
		}
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

	/**
	 * @method registrar() , registra una nueva orden de compra, con sus
	 * respectivos bieServicios cargados
	 */
	public void registrar(){
		try{
			newOrdenCompra.setEmpresa(empresaLogin);
			newOrdenCompra.setEstado("AC");
			newOrdenCompra.setFechaRegistro(new Date());
			newOrdenCompra.setProveedor(selectedProveedor);
			newOrdenCompra.setPermitirCredito("NO");
			newOrdenCompra.setDiasCredito(0);
			newOrdenCompra.setUsuarioRegistro(nombreUsuario);
			newOrdenCompra.setTotal(totalImporte);
			newOrdenCompra = ordenCompraRegistration.create(newOrdenCompra);
			
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Orden Compra Registrada", selectedProveedor.getNombre());
			facesContext.addMessage(null, m);
			resetearFitrosTabla("formTableOrdenCompra:dataTableOrdenCompra");
			loadDefault();
		}catch(Exception e){
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Registro Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}

	public void modificarVista(){

	}

	public void modificar(){
		try{
			newOrdenCompra.setProveedor(selectedProveedor);
			newOrdenCompra.setUsuarioRegistro(nombreUsuario);

			ordenCompraRegistration.update(newOrdenCompra);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Orden Compra Modificada!", selectedProveedor.getNombre());
			facesContext.addMessage(null, m);
			loadDefault();
		}catch(Exception e){
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Registro Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}

	public void eliminar(){
		try{
			newOrdenCompra.setEstado("RM");
			ordenCompraRegistration.update(newOrdenCompra);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Orden Compra Eliminada!", selectedProveedor.getNombre());
			facesContext.addMessage(null, m);
			loadDefault();
		}catch(Exception e){
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Registro Incorrecto.");
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

	public void actualizarFormReg(){
		log.info("actualizarFormReg");
		ordenCompraSeleccionada = false;
		crear = true;
		registrar = false;
		modificar = false;
		setTipoColumnTable("col-md-12");
		resetearFitrosTabla("formTableOrdenCompra:dataTableOrdenCompra");
		resetearFitrosTabla("formTableBienServicio:dataTableBienServicio");

		newOrdenCompra = new OrdenCompra();	
		selectedOrdenCompra = new OrdenCompra();
	}

	private Proveedor obtenerProveedorByLocal(String nombre){
		for(Proveedor pro : listProveedor){
			if(pro.getNombre().equals(nombre)){
				return pro;
			}
		}
		return null;
	}

	public void cambiarAspecto(){
		crear = false;
		registrar = true;
		modificar = false;
		setTipoColumnTable("col-md-8");
	}


	public void resetearFitrosTabla(String id) {
		DataTable table = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(id);
		table.setSelection(null);
		table.reset();
	}

	public void onRowSelect(SelectEvent event) {
		ordenCompraSeleccionada = true;
		//newOrdenCompra = new OrdenCompra();
		//newOrdenCompra = selectedOrdenCompra;
		crear = false;
		//registrar = false;
		//modificar = true;
		//tipoColumnTable = "col-md-8";
		//resetearFitrosTabla("formTableOrdenCompra:dataTableOrdenCompra");
		if(selectedOrdenCompra.getEstado().equals("AC")){
			nombreTipoCompra = selectedOrdenCompra.getTipo();//INTENA - BOLETO
			libroCompra.setFechaFactura(new Date());
			libroCompra.setRazonSocial(selectedOrdenCompra.getProveedor().getNombre());
			libroCompra.setNitProveedor(selectedOrdenCompra.getProveedor().getNit());
			libroCompra.setImporteTotal(selectedOrdenCompra.getTotal());
			double exento = 0;
			double ice = 0;
			double creditoFiscal = 0.13;

			libroCompra.setImporteExcentos(exento);
			libroCompra.setImporteICE(ice);
			libroCompra.setImporteNoSujetoCreditoFiscal(selectedOrdenCompra.getTotal()-ice-exento);
			libroCompra.setCreditoFiscal(libroCompra.getImporteNoSujetoCreditoFiscal()*creditoFiscal);
		}
	}

	public void onRowSelectServicio(SelectEvent event) {
		newOrdenCompra = new OrdenCompra();
		newOrdenCompra = selectedOrdenCompra;
		crear = false;
		registrar = false;
		modificar = true;
		tipoColumnTable = "col-md-8";
		resetearFitrosTabla("formTableBienServicio:dataTableBienServicio");
	}

	public void onItemSelect(SelectEvent event) {
		String nombre =  event.getObject().toString();
		
	}

	

	public void registrarCompra(){
		log.info("registrarCompra()");
		//numeroFactura = newCompra.getNumeroFactura();
		//log.info("numeroFactura: "+numeroFactura);

		//registrar comprobante de egreso
		//--------------------------------------

		//registrar comprobante de obligacion

		//modificar Orden Compra
		//estado = procesado
		selectedOrdenCompra.setEstado("PR");
		ordenCompraRegistration.update(selectedOrdenCompra);
		
		//registrar compra
		libroCompra.setEstado("AC");
		libroCompra.setEmpresa(empresaLogin);
		libroCompra.setFechaRegistro(new Date());
		compraRegistration.create(libroCompra);

		//cargar datos por defecto
		loadDefault();

		//actualizar tabla
		resetearFitrosTabla("formTableOrdenCompra:dataTableOrdenCompra");


		//mostrar dialog orden compra

		//RequestContext context = RequestContext.getCurrentInstance();
		//context.execute("PF('dlgCotizacionVistaPrevia').show();");

	}
	
	public String obtenerMontoLiteral(double totalFactura) {
		log.info("Total Entero Factura >>>>> " + totalFactura);
		NumerosToLetras convert = new NumerosToLetras();
		String totalLiteral;
		try {
			totalLiteral = convert.convertNumberToLetter(totalFactura);
			return totalLiteral;
		} catch (Exception e) {
			log.info("Error en obtenerMontoLiteral: "
					+ e.getMessage());
			return "Error Literal";
		}
	}
	
	private String obtenerCorrelativo(int comprobante){
		// pather = "1508-000001";
		Date fecha = new Date(); 
		String year = new SimpleDateFormat("yy").format(fecha);
		String mes = new SimpleDateFormat("MM").format(fecha);
		return year+mes+"-"+String.format("%06d", comprobante);
	}
	
	

	// ---------    get and set  -------

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

	public OrdenCompra getNewOrdenCompra() {
		return newOrdenCompra;
	}

	public void setNewOrdenCompra(OrdenCompra newOrdenCompra) {
		this.newOrdenCompra = newOrdenCompra;
	}

	public List<Proveedor> getListProveedor() {
		return listProveedor;
	}

	public void setListProveedor(List<Proveedor> listProveedor) {
		this.listProveedor = listProveedor;
	}

	public Proveedor getSelectedProveedor() {
		return selectedProveedor;
	}

	public void setSelectedProveedor(Proveedor selectedProveedor) {
		this.selectedProveedor = selectedProveedor;
	}

	public String getNombreProveedor() {
		return nombreProveedor;
	}

	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
		selectedProveedor = obtenerProveedorByLocal(nombreProveedor);
	}

	public String[] getListEstado() {
		return listEstado;
	}

	public void setListEstado(String[] listEstado) {
		this.listEstado = listEstado;
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

	public String getTipoColumnTable() {
		return tipoColumnTable;
	}

	public void setTipoColumnTable(String tipoColumnTable) {
		this.tipoColumnTable = tipoColumnTable;
	}

	public List<OrdenCompra> getListOrdenCompra() {
		return listOrdenCompra;
	}

	public void setListOrdenCompra(List<OrdenCompra> listOrdenCompra) {
		this.listOrdenCompra = listOrdenCompra;
	}

	public OrdenCompra getSelectedOrdenCompra() {
		return selectedOrdenCompra;
	}

	public void setSelectedOrdenCompra(OrdenCompra selectedOrdenCompra) {
		this.selectedOrdenCompra = selectedOrdenCompra;
	}

	public List<OrdenCompra> getListFilterOrdenCompra() {
		return listFilterOrdenCompra;
	}

	public void setListFilterOrdenCompra(List<OrdenCompra> listFilterOrdenCompra) {
		this.listFilterOrdenCompra = listFilterOrdenCompra;
	}

	public String getTextoServicio() {
		return textoServicio;
	}

	public void setTextoServicio(String textoServicio) {
		this.textoServicio = textoServicio;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public double getTotalImportePorServicio() {
		return totalImportePorServicio;
	}

	public void setTotalImportePorServicio(double totalImportePorServicio) {
		this.totalImportePorServicio = totalImportePorServicio;
	}

	public double getDescuento() {
		return descuento;
	}

	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}

	public double getTotalImporte() {
		return totalImporte;
	}

	public void setTotalImporte(double totalImporte) {
		this.totalImporte = totalImporte;
	}

	public List<Servicio> getListServicioXOrdenCompra() {
		return listServicioXOrdenCompra;
	}

	public void setListServicioXOrdenCompra(List<Servicio> listServicioXOrdenCompra) {
		this.listServicioXOrdenCompra = listServicioXOrdenCompra;
	}

	public Servicio getServicioXOrdenCompra() {
		return servicioXOrdenCompra;
	}

	public void setServicioXOrdenCompra(Servicio servicioXOrdenCompra) {
		this.servicioXOrdenCompra = servicioXOrdenCompra;
	}
public boolean isOrdenCompraSeleccionada() {
		return ordenCompraSeleccionada;
	}

	public void setOrdenCompraSeleccionada(boolean ordenCompraSeleccionada) {
		this.ordenCompraSeleccionada = ordenCompraSeleccionada;
	}

	public Compra getLibroCompra() {
		return libroCompra;
	}

	public void setLibroCompra(Compra libroCompra) {
		this.libroCompra = libroCompra;
	}

	public String getNombreTipoCompra() {
		return nombreTipoCompra;
	}

	public void setNombreTipoCompra(String nombreTipoCompra) {
		this.nombreTipoCompra = nombreTipoCompra;
	}
}
