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

import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.AlmacenProductoRepository;
import bo.com.qbit.webapp.data.AlmacenRepository;
import bo.com.qbit.webapp.data.DetalleOrdenIngresoRepository;
import bo.com.qbit.webapp.data.KardexProductoRepository;
import bo.com.qbit.webapp.data.OrdenIngresoRepository;
import bo.com.qbit.webapp.data.ProductoRepository;
import bo.com.qbit.webapp.data.ProveedorRepository;
import bo.com.qbit.webapp.data.UsuarioRepository;
import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.AlmacenProducto;
import bo.com.qbit.webapp.model.DetalleOrdenIngreso;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.KardexProducto;
import bo.com.qbit.webapp.model.OrdenIngreso;
import bo.com.qbit.webapp.model.Producto;
import bo.com.qbit.webapp.model.Proveedor;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.service.AlmacenProductoRegistration;
import bo.com.qbit.webapp.service.DetalleOrdenIngresoRegistration;
import bo.com.qbit.webapp.service.KardexProductoRegistration;
import bo.com.qbit.webapp.service.OrdenIngresoRegistration;
import bo.com.qbit.webapp.util.FacesUtil;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "ordenIngresoController")
@ConversationScoped
public class OrdenIngresoController implements Serializable {

	private static final long serialVersionUID = 749163787421586877L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	private @Inject AlmacenRepository almacenRepository;
	private @Inject UsuarioRepository usuarioRepository;
	private @Inject OrdenIngresoRepository ordenIngresoRepository;
	private @Inject ProveedorRepository proveedorRepository;
	private @Inject ProductoRepository productoRepository;
	private @Inject DetalleOrdenIngresoRepository detalleOrdenIngresoRepository;
	private @Inject AlmacenProductoRepository almacenProductoRepository;
	private @Inject KardexProductoRepository kardexProductoRepository;

	private @Inject OrdenIngresoRegistration ordenIngresoRegistration;
	private @Inject DetalleOrdenIngresoRegistration detalleOrdenIngresoRegistration;
	private @Inject AlmacenProductoRegistration almacenProductoRegistration;
	private @Inject KardexProductoRegistration kardexProductoRegistration;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	@Inject
	private FacesContext facesContext;

	//ESTADOS
	private boolean modificar = false;
	private boolean registrar = false;
	private boolean crear = true;
	private boolean verButtonDetalle = true;
	private boolean editarOrdenIngreso = false;
	private boolean verProcesar = true;

	private String tituloProducto = "Agregar Producto";
	private String tituloPanel = "Registrar Almacen";
	private String urlOrdenIngreso = "";

	//OBJECT
	private Proveedor selectedProveedor;
	private Producto selectedProducto;
	private Almacen selectedAlmacen;
	private Almacen selectedAlmacenOrigen;
	private OrdenIngreso selectedOrdenIngreso;
	private OrdenIngreso newOrdenIngreso;
	private DetalleOrdenIngreso selectedDetalleOrdenIngreso;

	//LIST
	private List<Usuario> listUsuario = new ArrayList<Usuario>();
	private List<DetalleOrdenIngreso> listaDetalleOrdenIngreso = new ArrayList<DetalleOrdenIngreso>(); // ITEMS
	private List<OrdenIngreso> listaOrdenIngreso = new ArrayList<OrdenIngreso>();
	private List<Almacen> listaAlmacen = new ArrayList<Almacen>();
	private List<Proveedor> listaProveedor = new ArrayList<Proveedor>();
	private List<DetalleOrdenIngreso> listDetalleOrdenIngresoEliminados = new ArrayList<DetalleOrdenIngreso>();

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;
	private Gestion gestionSesion;

	private boolean atencionCliente=false;

	@PostConstruct
	public void initNewOrdenIngreso() {
		
		System.out.println(" ... initNewOrdenIngreso ...");
		
		usuarioSession = sessionMain.getUsuarioLogin().getLogin();
		gestionSesion = sessionMain.getGestionLogin();
		listUsuario = usuarioRepository.findAllOrderedByID();

		selectedProducto = new Producto();
		selectedAlmacen = new Almacen();
		selectedProveedor = new Proveedor();

		selectedOrdenIngreso = null;
		selectedDetalleOrdenIngreso = new DetalleOrdenIngreso();

		// tituloPanel
		tituloPanel = "Registrar Orden Ingreso";

		modificar = false;
		registrar = false;
		crear = true;
		atencionCliente=false;
		verProcesar = true;

		listaDetalleOrdenIngreso = new ArrayList<DetalleOrdenIngreso>();
		listaOrdenIngreso = ordenIngresoRepository.findAllOrderedByID();
		listaAlmacen = almacenRepository.findAllOrderedByID();
		listaProveedor = proveedorRepository.findAllOrderedByID();

		newOrdenIngreso = new OrdenIngreso();
		newOrdenIngreso.setCorrelativo(cargarCorrelativo(listaOrdenIngreso.size()+1));
		newOrdenIngreso.setEstado("AC");
		newOrdenIngreso.setGestion(gestionSesion);
		newOrdenIngreso.setFechaRegistro(new Date());
		newOrdenIngreso.setUsuarioRegistro(usuarioSession);

	}

	public void cambiarAspecto(){
		//verificar si el usuario logeado tiene almacen registrado
		selectedAlmacenOrigen = almacenRepository.findAlmacenForUser(sessionMain.getUsuarioLogin());
		if(selectedAlmacenOrigen.getId() == -1){
			FacesUtil.infoMessage("Usuario "+usuarioSession, "No tiene asignado un almacen");
			return;
		}
		modificar = false;
		registrar = true;
		crear = false;
	}

	public void cambiarAspectoModificar(){
		modificar = true;
		registrar = false;
		crear = false;
		newOrdenIngreso = selectedOrdenIngreso;
		selectedAlmacen = newOrdenIngreso.getAlmacen();
		selectedProveedor = newOrdenIngreso.getProveedor();
		listaDetalleOrdenIngreso = detalleOrdenIngresoRepository.findAllByOrdenIngreso(selectedOrdenIngreso);
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

	// SELECT ORDEN INGRESO CLICK
	public void onRowSelectOrdenIngresoClick(SelectEvent event) {
		try {
			if(selectedOrdenIngreso.getEstado().equals("PR")){
				verProcesar = false;
			}else{
				verProcesar = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in onRowSelectOrdenIngresoClick: "
					+ e.getMessage());
		}
	}

	// SELECT DETALLE ORDEN INGRESO CLICK
	public void onRowSelectDetalleOrdenIngresoClick(SelectEvent event) {
		try {
			verButtonDetalle = false;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in onRowSelectOrdenIngresoClick: "
					+ e.getMessage());
		}
	}

	public void registrarOrdenIngreso() {
		try {
			Date date = new Date();
			calcularTotal();
			System.out.println("Ingreso a registrarOrdenIngreso: ");
			newOrdenIngreso.setFechaRegistro(date);
			newOrdenIngreso.setAlmacen(selectedAlmacen);
			newOrdenIngreso.setProveedor(selectedProveedor);
			newOrdenIngreso = ordenIngresoRegistration.register(newOrdenIngreso);
			for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){
				d.setFechaRegistro(date);
				d.setUsuarioRegistro(usuarioSession);
				d.setOrdenIngreso(newOrdenIngreso);
				detalleOrdenIngresoRegistration.register(d);
			}
			FacesUtil.infoMessage("Orden de Ingreso Registrada!", ""+newOrdenIngreso.getId());
			initNewOrdenIngreso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Registrar.");
		}
	}

	public void modificarOrdenIngreso() {
		try {
			System.out.println("Ingreso a modificarOrdenIngreso: ");
			double total = 0;
			for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){
				total = total + d.getTotal();
				detalleOrdenIngresoRegistration.updated(d);
			}
			//borrado logico 
			for(DetalleOrdenIngreso d: listDetalleOrdenIngresoEliminados){
				if(d.getId() != 0){
					d.setEstado("RM");
					detalleOrdenIngresoRegistration.updated(d);
				}
			}
			newOrdenIngreso.setAlmacen(selectedAlmacen);
			newOrdenIngreso.setProveedor(selectedProveedor);
			newOrdenIngreso.setTotalImporte(total);
			ordenIngresoRegistration.updated(newOrdenIngreso);
			FacesUtil.infoMessage("Orden de Ingreso Modificada!", ""+newOrdenIngreso.getId());
			initNewOrdenIngreso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Modificar.");
		}
	}

	public void eliminarOrdenIngreso() {
		try {
			System.out.println("Ingreso a eliminarOrdenIngreso: ");
			ordenIngresoRegistration.remover(newOrdenIngreso);
			for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){
				detalleOrdenIngresoRegistration.remover(d);
			}
			FacesUtil.infoMessage("Orden de Ingreso Eliminada!", ""+newOrdenIngreso.getId());
			initNewOrdenIngreso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Eliminar.");
		}
	}

	public void procesarOrdenIngreso(){
		try {
			Date fechaActual = new Date();
			//actualizar estado de orden ingreso
			selectedOrdenIngreso.setEstado("PR");
			selectedOrdenIngreso.setFechaAprobacion(fechaActual);
			ordenIngresoRegistration.updated(selectedOrdenIngreso);

			//actuaizar stock de AlmacenProducto
			listaDetalleOrdenIngreso = detalleOrdenIngresoRepository.findAllByOrdenIngreso(selectedOrdenIngreso);
			for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){
				Producto prod = d.getProducto();
				actualizarStock(prod, d.getCantidad(),fechaActual);
				actualizarKardexProducto( prod,fechaActual, d.getCantidad());
			}

			FacesUtil.infoMessage("Orden de Ingreso Procesada!", "");
			initNewOrdenIngreso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Procesar!");
		}
	}

	//registro en la tabla kardex_producto
	private void actualizarKardexProducto(Producto prod,Date fechaActual,double cantidad) throws Exception{
		//registrar Kardex
		KardexProducto kardexProductoAnt = kardexProductoRepository.findKardexStockAnteriorByProducto(prod);
		double stockAnterior = 0;
		if(kardexProductoAnt != null){
			//se obtiene el saldo anterior del producto
			stockAnterior = kardexProductoAnt.getStockActual();
		}
		KardexProducto kardexProducto = new KardexProducto();
		kardexProducto.setUnidadSolicitante("ORDEN INGRESO");
		kardexProducto.setFecha(fechaActual);
		kardexProducto.setAlmacen(selectedOrdenIngreso.getAlmacen());
		kardexProducto.setCantidad(cantidad);
		kardexProducto.setEstado("AC");
		kardexProducto.setFechaRegistro(fechaActual);
		kardexProducto.setGestion(gestionSesion);
		kardexProducto.setNumeroTransaccion(selectedOrdenIngreso.getCorrelativo());
		kardexProducto.setPrecioCompra(0);
		kardexProducto.setPrecioVenta(0);
		kardexProducto.setProducto(prod);
		kardexProducto.setStock(cantidad);//estock que esta ingresando
		kardexProducto.setStockActual(stockAnterior+cantidad);//anterior + cantidad
		kardexProducto.setStockAnterior(stockAnterior);
		kardexProducto.setTipoMovimiento("ORDEN INGRESO");
		kardexProducto.setUsuarioRegistro(usuarioSession);
		kardexProductoRegistration.register(kardexProducto);
	}
	
	//registro en la tabla almacen_producto
	private void actualizarStock(Producto prod ,int newStock,Date date) throws Exception {
		//0 . verificar si existe el producto en el almacen
		System.out.println("actualizarStock()");
		AlmacenProducto almProd =  almacenProductoRepository.findByProducto(prod);
		if(almProd != null){
			// 1 .  si existe el producto
			double oldStock = almProd.getStock();
			almProd.setStock(oldStock + newStock);
			almacenProductoRegistration.updated(almProd);
			return ;
		}
		// 2 . no existe el producto
		almProd = new AlmacenProducto();
		almProd.setAlmacen(selectedOrdenIngreso.getAlmacen());
		almProd.setProducto(prod);
		almProd.setProveedor(selectedOrdenIngreso.getProveedor());
		almProd.setStock(newStock);

		almProd.setEstado("AC");
		almProd.setFechaRegistro(date);
		almProd.setUsuarioRegistro(usuarioSession);

		almacenProductoRegistration.register(almProd);
	}

	public void cargarReporte(){
		try {
			urlOrdenIngreso = loadURL();
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgVistaPreviaOrdenIngreso').show();");

			initNewOrdenIngreso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Proceso Incorrecto.");
		}
	}

	public String loadURL(){
		try{
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();  
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
			String urlPDFreporte = urlPath+"ReporteOrdenIngreso?pIdOrdenIngreso="+selectedOrdenIngreso.getId()+"&pIdEmpresa=1&pUsuario="+usuarioSession;
			return urlPDFreporte;
		}catch(Exception e){
			return "error";
		}
	}

	// DETALLE ORDEN INGRESO ITEMS

	public void editarDetalleOrdenIngreso(){
		tituloProducto = "Modificar Producto";
		selectedProducto = selectedDetalleOrdenIngreso.getProducto();
		verButtonDetalle = true;
		editarOrdenIngreso = true;
		calcular();
	}

	public void borrarDetalleOrdenIngreso(){
		listaDetalleOrdenIngreso.remove(selectedDetalleOrdenIngreso);
		listDetalleOrdenIngresoEliminados.add(selectedDetalleOrdenIngreso);
		updateDataTable("formTableOrdenIngreso:itemsTable1");
		verButtonDetalle = true;
	}

	public void limpiarDatosProducto(){
		selectedProducto = new Producto();
		selectedDetalleOrdenIngreso = new DetalleOrdenIngreso();
		updateDataTable("formTableOrdenIngreso:itemsTable1");
		verButtonDetalle = true;
		editarOrdenIngreso = false;
	}

	public void agregarDetalleOrdenIngreso(){
		System.out.println("agregarDetalleOrdenIngreso ");
		selectedDetalleOrdenIngreso.setProducto(selectedProducto);
		listaDetalleOrdenIngreso.add(0, selectedDetalleOrdenIngreso);
		for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){
			System.out.println("for  listaDetalleOrdenIngreso -> d= "+d.getPrecioUnitario());
		}

		selectedProducto = new Producto();
		selectedDetalleOrdenIngreso = new DetalleOrdenIngreso();
		updateDataTable("formTableOrdenIngreso:itemsTable1");
		verButtonDetalle = true;
	}

	public void modificarDetalleOrdenIngreso(){
		System.out.println("modificarDetalleOrdenIngreso ");
		for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){
			if(d.equals(selectedDetalleOrdenIngreso)){
				d = selectedDetalleOrdenIngreso;
			}
		}
		selectedProducto = new Producto();
		selectedDetalleOrdenIngreso = new DetalleOrdenIngreso();
		updateDataTable("formTableOrdenIngreso:itemsTable1");
		verButtonDetalle = true;
		editarOrdenIngreso = false;
	}

	//calcular totales
	public void calcular(){
		System.out.println("calcular()");
		double precio = selectedProducto.getPrecioUnitario();
		double cantidad = selectedDetalleOrdenIngreso.getCantidad();
		selectedDetalleOrdenIngreso.setPrecioUnitario(precio);
		selectedDetalleOrdenIngreso.setTotal(precio * cantidad);
	}

	public void calcularTotal(){
		double totalImporte = 0;
		for(DetalleOrdenIngreso d : listaDetalleOrdenIngreso){
			totalImporte =totalImporte + d.getTotal();
		}
		newOrdenIngreso.setTotalImporte(totalImporte);
	}

	// ONCOMPLETETEXT PROVEEDOR
	public List<Proveedor> completeProveedor(String query) {
		String upperQuery = query.toUpperCase();
		List<Proveedor> results = new ArrayList<Proveedor>();
		for(Proveedor i : listaProveedor) {
			if(i.getNombre().toUpperCase().startsWith(upperQuery)){
				results.add(i);
			}
		}         
		return results;
	}

	public void onRowSelectProveedorClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Proveedor i : listaProveedor){
			if(i.getNombre().equals(nombre)){
				selectedProveedor = i;
				return;
			}
		}
	}

	// ONCOMPLETETEXT ALMACEN
	public List<Almacen> completeAlmacen(String query) {
		String upperQuery = query.toUpperCase();
		List<Almacen> results = new ArrayList<Almacen>();
		for(Almacen i : listaAlmacen) {
			if(i.getNombre().toUpperCase().startsWith(upperQuery)){
				results.add(i);
			}
		}         
		return results;
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

	// ONCOMPLETETEXT PRODUCTO
	public List<Producto> completeProducto(String query) {
		return productoRepository.findAllProductoForQueryNombre(query);
	}

	public void onRowSelectProductoClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		List<Producto> listProducto = productoRepository.findAllProductoActivosByID();
		for(Producto i : listProducto){
			if(i.getNombre().equals(nombre)){
				selectedProducto = i;
				calcular();
				return;
			}
		}
	}

	public void updateDataTable(String id) {
		DataTable table = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(id);
		table.setSelection(null);
		table.reset();
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

	public String getTituloProducto() {
		return tituloProducto;
	}

	public void setTituloProducto(String tituloProducto) {
		this.tituloProducto = tituloProducto;
	}

	public List<DetalleOrdenIngreso> getListaDetalleOrdenIngreso() {
		return listaDetalleOrdenIngreso;
	}

	public void setListaDetalleOrdenIngreso(List<DetalleOrdenIngreso> listaDetalleOrdenIngreso) {
		this.listaDetalleOrdenIngreso = listaDetalleOrdenIngreso;
	}

	public List<OrdenIngreso> getListaOrdenIngreso() {
		return listaOrdenIngreso;
	}

	public void setListaOrdenIngreso(List<OrdenIngreso> listaOrdenIngreso) {
		this.listaOrdenIngreso = listaOrdenIngreso;
	}

	public List<Almacen> getListaAlmacen() {
		return listaAlmacen;
	}

	public void setListaAlmacen(List<Almacen> listaAlmacen) {
		this.listaAlmacen = listaAlmacen;
	}

	public OrdenIngreso getSelectedOrdenIngreso() {
		return selectedOrdenIngreso;
	}

	public void setSelectedOrdenIngreso(OrdenIngreso selectedOrdenIngreso) {
		this.selectedOrdenIngreso = selectedOrdenIngreso;
	}

	public OrdenIngreso getNewOrdenIngreso() {
		return newOrdenIngreso;
	}

	public void setNewOrdenIngreso(OrdenIngreso newOrdenIngreso) {
		this.newOrdenIngreso = newOrdenIngreso;
	}

	public Producto getSelectedProducto() {
		return selectedProducto;
	}

	public void setSelectedProducto(Producto selectedProducto) {
		this.selectedProducto = selectedProducto;
	}

	public Proveedor getSelectedProveedor() {
		return selectedProveedor;
	}

	public void setSelectedProveedor(Proveedor selectedProveedor) {
		this.selectedProveedor = selectedProveedor;
	}

	public List<Proveedor> getListaProveedor() {
		return listaProveedor;
	}

	public void setListaProveedor(List<Proveedor> listaProveedor) {
		this.listaProveedor = listaProveedor;
	}

	public DetalleOrdenIngreso getSelectedDetalleOrdenIngreso() {
		return selectedDetalleOrdenIngreso;
	}

	public void setSelectedDetalleOrdenIngreso(
			DetalleOrdenIngreso selectedDetalleOrdenIngreso) {
		this.selectedDetalleOrdenIngreso = selectedDetalleOrdenIngreso;
	}

	public boolean isVerButtonDetalle() {
		return verButtonDetalle;
	}

	public void setVerButtonDetalle(boolean verButtonDetalle) {
		this.verButtonDetalle = verButtonDetalle;
	}

	public boolean isVerProcesar() {
		return verProcesar;
	}

	public void setVerProcesar(boolean verProcesar) {
		this.verProcesar = verProcesar;
	}

	public String getUrlOrdenIngreso() {
		return urlOrdenIngreso;
	}

	public void setUrlOrdenIngreso(String urlOrdenIngreso) {
		this.urlOrdenIngreso = urlOrdenIngreso;
	}

	public boolean isEditarOrdenIngreso() {
		return editarOrdenIngreso;
	}

	public void setEditarOrdenIngreso(boolean editarOrdenIngreso) {
		this.editarOrdenIngreso = editarOrdenIngreso;
	}

	public Almacen getSelectedAlmacenOrigen() {
		return selectedAlmacenOrigen;
	}

	public void setSelectedAlmacenOrigen(Almacen selectedAlmacenOrigen) {
		this.selectedAlmacenOrigen = selectedAlmacenOrigen;
	}

}
