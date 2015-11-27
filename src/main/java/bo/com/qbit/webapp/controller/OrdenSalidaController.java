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
import bo.com.qbit.webapp.data.DetalleOrdenSalidaRepository;
import bo.com.qbit.webapp.data.FuncionarioRepository;
import bo.com.qbit.webapp.data.KardexProductoRepository;
import bo.com.qbit.webapp.data.OrdenIngresoRepository;
import bo.com.qbit.webapp.data.OrdenSalidaRepository;
import bo.com.qbit.webapp.data.ProductoRepository;
import bo.com.qbit.webapp.data.ProveedorRepository;
import bo.com.qbit.webapp.data.UsuarioRepository;
import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.AlmacenProducto;
import bo.com.qbit.webapp.model.DetalleOrdenIngreso;
import bo.com.qbit.webapp.model.DetalleOrdenSalida;
import bo.com.qbit.webapp.model.Funcionario;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.KardexProducto;
import bo.com.qbit.webapp.model.OrdenIngreso;
import bo.com.qbit.webapp.model.OrdenSalida;
import bo.com.qbit.webapp.model.Producto;
import bo.com.qbit.webapp.model.Proveedor;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.service.AlmacenProductoRegistration;
import bo.com.qbit.webapp.service.DetalleOrdenIngresoRegistration;
import bo.com.qbit.webapp.service.DetalleOrdenSalidaRegistration;
import bo.com.qbit.webapp.service.KardexProductoRegistration;
import bo.com.qbit.webapp.service.OrdenIngresoRegistration;
import bo.com.qbit.webapp.service.OrdenSalidaRegistration;
import bo.com.qbit.webapp.util.FacesUtil;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "ordenSalidaController")
@ConversationScoped
public class OrdenSalidaController implements Serializable {

	private static final long serialVersionUID = 749163787421586877L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	private @Inject AlmacenRepository almacenRepository;
	private @Inject UsuarioRepository usuarioRepository;
	private @Inject OrdenSalidaRepository ordenSalidaRepository;
	private @Inject ProductoRepository productoRepository;
	private @Inject DetalleOrdenSalidaRepository detalleOrdenSalidaRepository;
	private @Inject AlmacenProductoRepository almacenProductoRepository;
	private @Inject KardexProductoRepository kardexProductoRepository;
	private @Inject FuncionarioRepository funcionarioRepository;

	private @Inject OrdenSalidaRegistration ordenSalidaRegistration;
	private @Inject DetalleOrdenSalidaRegistration detalleOrdenSalidaRegistration;
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
	private String tituloPanel = "Registrar Orden Salida";
	private String urlOrdenIngreso = "";

	//OBJECT
	private Funcionario selectedFuncionario;
	private Producto selectedProducto;
	private Almacen selectedAlmacen;
	private Almacen selectedAlmacenOrigen;
	private OrdenSalida selectedOrdenSalida;
	private OrdenSalida newOrdenSalida;
	private DetalleOrdenSalida selectedDetalleOrdenSalida;

	//LIST
	private List<Usuario> listUsuario = new ArrayList<Usuario>();
	private List<DetalleOrdenSalida> listaDetalleOrdenSalida = new ArrayList<DetalleOrdenSalida>(); // ITEMS
	private List<OrdenSalida> listaOrdenSalida = new ArrayList<OrdenSalida>();
	private List<Almacen> listaAlmacen = new ArrayList<Almacen>();
	private List<Funcionario> listaFuncionario = new ArrayList<Funcionario>();
	private List<DetalleOrdenSalida> listDetalleOrdenSalidaEliminados = new ArrayList<DetalleOrdenSalida>();

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;
	private Gestion gestionSesion;

	private boolean atencionCliente=false;

	@PostConstruct
	public void initNewOrdenSalida() {

		beginConversation();

		usuarioSession = sessionMain.getUsuarioLogin().getLogin();
		gestionSesion = sessionMain.getGestionLogin();
		listUsuario = usuarioRepository.findAllOrderedByID();

		selectedProducto = new Producto();
		selectedAlmacen = new Almacen();
		selectedFuncionario = new Funcionario();

		selectedOrdenSalida = null;
		selectedDetalleOrdenSalida = new DetalleOrdenSalida();

		// tituloPanel
		tituloPanel = "Registrar Orden Salida";

		modificar = false;
		registrar = false;
		crear = true;
		atencionCliente=false;
		verProcesar = true;

		listaDetalleOrdenSalida = new ArrayList<DetalleOrdenSalida>();
		listaOrdenSalida = ordenSalidaRepository.findAllOrderedByID();
		listaAlmacen = almacenRepository.findAllOrderedByID();
		listaFuncionario = funcionarioRepository.findAllOrderedByID();

		newOrdenSalida = new OrdenSalida();
		newOrdenSalida.setCorrelativo(cargarCorrelativo(listaOrdenSalida.size()+1));
		newOrdenSalida.setEstado("AC");
		newOrdenSalida.setGestion(gestionSesion);
		newOrdenSalida.setFechaRegistro(new Date());
		newOrdenSalida.setUsuarioRegistro(usuarioSession);

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
		newOrdenSalida = selectedOrdenSalida;
		selectedAlmacen = newOrdenSalida.getAlmacen();
		selectedFuncionario = newOrdenSalida.getFuncionario();
		listaDetalleOrdenSalida = detalleOrdenSalidaRepository.findAllByOrdenSalida(selectedOrdenSalida);
	}

	public void beginConversation() {
		if (conversation.isTransient()) {
			System.out.println("beginning conversation : " + this.conversation);
			conversation.begin();
			System.out.println("---> Init Conversation");
		}
	}

	public void endConversation() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
	}

	//correlativo incremental por gestion
	private String cargarCorrelativo(int nroOrdenIngreso){
		//pather = "000001";
		//Date fecha = new Date(); 
		//String year = new SimpleDateFormat("yy").format(fecha);
		//String mes = new SimpleDateFormat("MM").format(fecha);
		return String.format("%06d", nroOrdenIngreso);
	}

	// SELECT ORDEN SALIDA CLICK
	public void onRowSelectOrdenSalidaClick(SelectEvent event) {
		try {
			if(selectedOrdenSalida.getEstado().equals("PR")){
				verProcesar = false;
			}else{
				verProcesar = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in onRowSelectOrdenSalidaClick: "
					+ e.getMessage());
		}
	}

	// SELECT DETALLE ORDEN SALIDA CLICK
	public void onRowSelectDetalleOrdenSalidaClick(SelectEvent event) {
		try {
			verButtonDetalle = false;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in onRowSelectDetalleOrdenSalidaClick: "
					+ e.getMessage());
		}
	}

	public void registrarOrdenSalida() {
		try {
			Date date = new Date();
			calcularTotal();
			System.out.println("Ingreso a registrarOrdenSalida: ");
			newOrdenSalida.setFechaRegistro(date);
			newOrdenSalida.setAlmacen(selectedAlmacen);
			newOrdenSalida.setFuncionario(selectedFuncionario);
			newOrdenSalida = ordenSalidaRegistration.register(newOrdenSalida);
			for(DetalleOrdenSalida d: listaDetalleOrdenSalida){
				d.setFechaRegistro(date);
				d.setUsuarioRegistro(usuarioSession);
				d.setOrdenSalida(newOrdenSalida);
				detalleOrdenSalidaRegistration.register(d);
			}
			FacesUtil.infoMessage("Orden de Salida Registrada!", ""+newOrdenSalida.getId());
			initNewOrdenSalida();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Registrar.");
		}
	}

	public void modificarOrdenSalida() {
		try {
			System.out.println("Ingreso a modificarOrdenSalida: ");
			double total = 0;
			for(DetalleOrdenSalida d: listaDetalleOrdenSalida){
				total = total + d.getTotal();
				detalleOrdenSalidaRegistration.updated(d);
			}
			//borrado logico 
			for(DetalleOrdenSalida d: listDetalleOrdenSalidaEliminados){
				if(d.getId() != 0){
					d.setEstado("RM");
					detalleOrdenSalidaRegistration.updated(d);
				}
			}
			newOrdenSalida.setAlmacen(selectedAlmacen);
			newOrdenSalida.setFuncionario(selectedFuncionario);
			newOrdenSalida.setTotalImporte(total);
			ordenSalidaRegistration.updated(newOrdenSalida);
			FacesUtil.infoMessage("Orden de Salida Modificada!", ""+newOrdenSalida.getId());
			initNewOrdenSalida();
			
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Modificar.");
		}
	}

	public void eliminarOrdenIngreso() {
		try {
			System.out.println("Ingreso a eliminarOrdenIngreso: ");
			ordenSalidaRegistration.remover(newOrdenSalida);
			for(DetalleOrdenSalida d: listaDetalleOrdenSalida){
				detalleOrdenSalidaRegistration.remover(d);
			}
			FacesUtil.infoMessage("Orden de SAlida Eliminada!", ""+newOrdenSalida.getId());
			initNewOrdenSalida();
			
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Eliminar.");
		}
	}

	public void procesarOrdenIngreso(){
		try {
			Date fechaActual = new Date();
			//actualizar estado de orden ingreso
			selectedOrdenSalida.setEstado("PR");
			selectedOrdenSalida.setFechaAprobacion(fechaActual);
			ordenSalidaRegistration.updated(selectedOrdenSalida);

			//actuaizar stock de AlmacenProducto
			listaDetalleOrdenSalida = detalleOrdenSalidaRepository.findAllByOrdenSalida(selectedOrdenSalida);
			for(DetalleOrdenSalida d: listaDetalleOrdenSalida){
				Producto prod = d.getProducto();
				actualizarStock(prod, Integer.valueOf(String.valueOf(d.getCantidadSolicitada())),fechaActual);
				actualizarKardexProducto( prod,fechaActual, Integer.valueOf(String.valueOf(d.getCantidadSolicitada())));
			}

			FacesUtil.infoMessage("Orden de Ingreso Procesada!", "");
			initNewOrdenSalida();
			
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
			stockAnterior = kardexProductoAnt.getStockActual();
		}
		KardexProducto kardexProducto = new KardexProducto();
		kardexProducto.setFecha(fechaActual);
		kardexProducto.setAlmacen(selectedOrdenSalida.getAlmacen());
		kardexProducto.setCantidad(cantidad);
		kardexProducto.setEstado("AC");
		kardexProducto.setFechaRegistro(fechaActual);
		kardexProducto.setGestion(gestionSesion);
		kardexProducto.setNumeroTransaccion(selectedOrdenSalida.getCorrelativo());
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
	
	private void actualizarStock(Producto prod ,int newStock,Date date) throws Exception {
		//0 . verificar si existe el producto en el almacen
		System.out.println("actualizarStock()");
		AlmacenProducto almProd =  almacenProductoRepository.findByProducto(prod);
		System.out.println("almProd = "+almProd);
		if(almProd != null){
			// 1 .  si existe el producto
			double oldStock = almProd.getStock();
			almProd.setStock(oldStock - newStock); //quitar (-)
			almacenProductoRegistration.updated(almProd);
			return ;
		}
		
	}

	public void cargarReporte(){
		try {
			urlOrdenIngreso = loadURL();
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgVistaPreviaOrdenSalida').show();");

			initNewOrdenSalida();
			
		} catch (Exception e) {
			FacesUtil.errorMessage("Proceso Incorrecto.");
		}
	}

	public String loadURL(){
		try{
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();  
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
			String urlPDFreporte = urlPath+"ReporteOrdenIngreso?pIdOrdenIngreso="+selectedOrdenSalida.getId()+"&pIdEmpresa=1&pUsuario="+usuarioSession;
			return urlPDFreporte;
		}catch(Exception e){
			return "error";
		}
	}

	// DETALLE ORDEN INGRESO ITEMS

	public void editarDetalleOrdenIngreso(){
		tituloProducto = "Modificar Producto";
		selectedProducto = selectedDetalleOrdenSalida.getProducto();
		verButtonDetalle = true;
		editarOrdenIngreso = true;
		calcular();
	}

	public void borrarDetalleOrdenIngreso(){
		listaDetalleOrdenSalida.remove(selectedDetalleOrdenSalida);
		listDetalleOrdenSalidaEliminados.add(selectedDetalleOrdenSalida);
		updateDataTable("formTableOrdenSalida:itemsTable1");
		verButtonDetalle = true;
	}

	public void limpiarDatosProducto(){
		selectedProducto = new Producto();
		selectedDetalleOrdenSalida = new DetalleOrdenSalida();
		updateDataTable("formTableOrdenSalida:itemsTable1");
		verButtonDetalle = true;
		editarOrdenIngreso = false;
	}

	public void agregarDetalleOrdenIngreso(){
		System.out.println("agregarDetalleOrdenIngreso ");
		selectedDetalleOrdenSalida.setProducto(selectedProducto);
		listaDetalleOrdenSalida.add(0, selectedDetalleOrdenSalida);
		for(DetalleOrdenSalida d: listaDetalleOrdenSalida){
			System.out.println("for  listaDetalleOrdenSalida -> d= "+d.getPrecioUnitario());
		}

		selectedProducto = new Producto();
		selectedDetalleOrdenSalida = new DetalleOrdenSalida();
		updateDataTable("formTableOrdenSalida:itemsTable1");
		verButtonDetalle = true;
	}

	public void modificarDetalleOrdenSalida(){
		System.out.println("modificarDetalleOrdenSalida ");
		for(DetalleOrdenSalida d: listaDetalleOrdenSalida){
			if(d.equals(selectedDetalleOrdenSalida)){
				d = selectedDetalleOrdenSalida;
			}
		}
		selectedProducto = new Producto();
		selectedDetalleOrdenSalida = new DetalleOrdenSalida();
		updateDataTable("formTableOrdenSalida:itemsTable1");
		verButtonDetalle = true;
		editarOrdenIngreso = false;
	}

	//calcular totales
	public void calcular(){
		System.out.println("calcular()");
		double precio = selectedProducto.getPrecioUnitario();
		double cantidad = selectedDetalleOrdenSalida.getCantidadSolicitada();
		selectedDetalleOrdenSalida.setPrecioUnitario(precio);
		selectedDetalleOrdenSalida.setTotal(precio * cantidad);
	}

	public void calcularTotal(){
		double totalImporte = 0;
		for(DetalleOrdenSalida d : listaDetalleOrdenSalida){
			totalImporte =totalImporte + d.getTotal();
		}
		newOrdenSalida.setTotalImporte(totalImporte);
	}

	// ONCOMPLETETEXT PROVEEDOR
	public List<Funcionario> completeFuncionario(String query) {
		String upperQuery = query.toUpperCase();
		List<Funcionario> results = new ArrayList<Funcionario>();
		for(Funcionario i : listaFuncionario) {
			if(i.getNombre().toUpperCase().contains(upperQuery)){
				results.add(i);
			}
		}         
		return results;
	}

	public void onRowSelectProveedorClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Funcionario i : listaFuncionario){
			if(i.getNombre().equals(nombre)){
				selectedFuncionario = i;
				return;
			}
		}
	}

	// ONCOMPLETETEXT ALMACEN
	public List<Almacen> completeAlmacen(String query) {
		String upperQuery = query.toUpperCase();
		List<Almacen> results = new ArrayList<Almacen>();
		for(Almacen i : listaAlmacen) {
			if(i.getNombre().toUpperCase().contains(upperQuery)){
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

	public List<Almacen> getListaAlmacen() {
		return listaAlmacen;
	}

	public void setListaAlmacen(List<Almacen> listaAlmacen) {
		this.listaAlmacen = listaAlmacen;
	}

	public Producto getSelectedProducto() {
		return selectedProducto;
	}

	public void setSelectedProducto(Producto selectedProducto) {
		this.selectedProducto = selectedProducto;
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

	public OrdenSalida getSelectedOrdenSalida() {
		return selectedOrdenSalida;
	}

	public void setSelectedOrdenSalida(OrdenSalida selectedOrdenSalida) {
		this.selectedOrdenSalida = selectedOrdenSalida;
	}

	public OrdenSalida getNewOrdenSalida() {
		return newOrdenSalida;
	}

	public void setNewOrdenSalida(OrdenSalida newOrdenSalida) {
		this.newOrdenSalida = newOrdenSalida;
	}

	public DetalleOrdenSalida getSelectedDetalleOrdenSalida() {
		return selectedDetalleOrdenSalida;
	}

	public void setSelectedDetalleOrdenSalida(DetalleOrdenSalida selectedDetalleOrdenSalida) {
		this.selectedDetalleOrdenSalida = selectedDetalleOrdenSalida;
	}

	public List<DetalleOrdenSalida> getListaDetalleOrdenSalida() {
		return listaDetalleOrdenSalida;
	}

	public void setListaDetalleOrdenSalida(List<DetalleOrdenSalida> listaDetalleOrdenSalida) {
		this.listaDetalleOrdenSalida = listaDetalleOrdenSalida;
	}

	public List<OrdenSalida> getListaOrdenSalida() {
		return listaOrdenSalida;
	}

	public void setListaOrdenSalida(List<OrdenSalida> listaOrdenSalida) {
		this.listaOrdenSalida = listaOrdenSalida;
	}

	public List<DetalleOrdenSalida> getListDetalleOrdenSalidaEliminados() {
		return listDetalleOrdenSalidaEliminados;
	}

	public void setListDetalleOrdenSalidaEliminados(
			List<DetalleOrdenSalida> listDetalleOrdenSalidaEliminados) {
		this.listDetalleOrdenSalidaEliminados = listDetalleOrdenSalidaEliminados;
	}

	public Funcionario getSelectedFuncionario() {
		return selectedFuncionario;
	}

	public void setSelectedFuncionario(Funcionario selectedFuncionario) {
		this.selectedFuncionario = selectedFuncionario;
	}

	public List<Funcionario> getListaFuncionario() {
		return listaFuncionario;
	}

	public void setListaFuncionario(List<Funcionario> listaFuncionario) {
		this.listaFuncionario = listaFuncionario;
	}

}
