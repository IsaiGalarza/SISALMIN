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
import bo.com.qbit.webapp.data.DetalleOrdenTraspasoRepository;
import bo.com.qbit.webapp.data.FuncionarioRepository;
import bo.com.qbit.webapp.data.OrdenTraspasoRepository;
import bo.com.qbit.webapp.data.ProductoRepository;
import bo.com.qbit.webapp.data.ProyectoRepository;
import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.AlmacenProducto;
import bo.com.qbit.webapp.model.DetalleOrdenTraspaso;
import bo.com.qbit.webapp.model.Funcionario;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.OrdenTraspaso;
import bo.com.qbit.webapp.model.Producto;
import bo.com.qbit.webapp.model.Proyecto;
import bo.com.qbit.webapp.service.AlmacenProductoRegistration;
import bo.com.qbit.webapp.service.DetalleOrdenTraspasoRegistration;
import bo.com.qbit.webapp.service.OrdenTraspasoRegistration;
import bo.com.qbit.webapp.util.FacesUtil;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "ordenTraspasoController")
@ConversationScoped
public class OrdenTraspasoController implements Serializable {

	private static final long serialVersionUID = 749163787421586877L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	private @Inject AlmacenRepository almacenRepository;
	private @Inject OrdenTraspasoRepository ordenTraspasoRepository;
	private @Inject ProyectoRepository proyectoRepository;
	private @Inject ProductoRepository productoRepository;
	private @Inject DetalleOrdenTraspasoRepository detalleOrdenTraspasoRepository;
	private @Inject AlmacenProductoRepository almacenProductoRepository;
	private @Inject FuncionarioRepository funcionarioRepository;

	private @Inject OrdenTraspasoRegistration ordenTraspasoRegistration;
	private @Inject DetalleOrdenTraspasoRegistration detalleOrdenTraspasoRegistration;
	private @Inject AlmacenProductoRegistration almacenProductoRegistration;

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
	private boolean editarOrdenTraspaso = false;
	private boolean verProcesar = true;

	private String tituloProducto = "Agregar Producto";
	private String tituloPanel = "Registrar Almacen";
	private String urlOrdenTraspaso = "";

	//OBJECT
	private Proyecto selectedProyecto;
	private Producto selectedProducto;
	private Almacen selectedAlmacen;
	private Almacen selectedAlmacenOrigen;
	private OrdenTraspaso selectedOrdenTraspaso;
	private OrdenTraspaso newOrdenTraspaso;
	private DetalleOrdenTraspaso selectedDetalleOrdenTraspaso;
	private Funcionario selectedFuncionario;

	//LIST
	private List<DetalleOrdenTraspaso> listaDetalleOrdenTraspaso = new ArrayList<DetalleOrdenTraspaso>(); // ITEMS
	private List<OrdenTraspaso> listaOrdenTraspaso = new ArrayList<OrdenTraspaso>();
	private List<Almacen> listaAlmacen = new ArrayList<Almacen>();
	private List<Proyecto> listaProyecto = new ArrayList<Proyecto>();
	private List<DetalleOrdenTraspaso> listDetalleOrdenTraspasoEliminados = new ArrayList<DetalleOrdenTraspaso>();
	private List<Funcionario> listFuncionario = new ArrayList<Funcionario>();

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;
	private Gestion gestionSesion ;

	private boolean atencionCliente=false;

	@PostConstruct
	public void initNewOrdenTraspaso() {

		beginConversation();

		usuarioSession = sessionMain.getUsuarioLoggin().getLogin();
		gestionSesion = sessionMain.getGestionLogin();

		selectedProducto = new Producto();
		selectedAlmacen = new Almacen();
		selectedProyecto = new Proyecto();

		selectedOrdenTraspaso = null;
		selectedDetalleOrdenTraspaso = new DetalleOrdenTraspaso();
		selectedFuncionario = new Funcionario();

		// tituloPanel
		tituloPanel = "Registrar Orden Traspaso";

		modificar = false;
		registrar = false;
		crear = true;
		atencionCliente=false;
		verProcesar = true;

		listaDetalleOrdenTraspaso = new ArrayList<DetalleOrdenTraspaso>();
		listaOrdenTraspaso = ordenTraspasoRepository.findAllOrderedByID();
		listaAlmacen = almacenRepository.findAllOrderedByID();
		listaProyecto = proyectoRepository.findAllOrderedByID();
		listFuncionario = funcionarioRepository.findAllOrderedByID();

		newOrdenTraspaso = new OrdenTraspaso();
		newOrdenTraspaso.setCorrelativo(cargarCorrelativo(listaOrdenTraspaso.size()+1));
		newOrdenTraspaso.setEstado("AC");
		newOrdenTraspaso.setGestion(gestionSesion);
		newOrdenTraspaso.setFechaRegistro(new Date());
		newOrdenTraspaso.setUsuarioRegistro(usuarioSession);
	}

	public void cambiarAspecto(){
		//verificar si el usuario logeado tiene almacen registrado
		selectedAlmacenOrigen = almacenRepository.findAlmacenForUser(sessionMain.getUsuarioLoggin());
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
		newOrdenTraspaso = selectedOrdenTraspaso;
		selectedAlmacen = newOrdenTraspaso.getAlmacenDestino();
		selectedProyecto = newOrdenTraspaso.getProyecto();
		selectedFuncionario = newOrdenTraspaso.getFuncionario();
		listaDetalleOrdenTraspaso = detalleOrdenTraspasoRepository.findAllByOrdenTraspaso(selectedOrdenTraspaso);
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
	private String cargarCorrelativo(int nroOrdenTraspaso){
		// pather = "000001";
		//Date fecha = new Date(); 
		//String year = new SimpleDateFormat("yy").format(fecha);
		//String mes = new SimpleDateFormat("MM").format(fecha);
		return String.format("%06d", nroOrdenTraspaso);
	}

	// SELECT ORDEN Traspaso CLICK
	public void onRowSelectOrdenTraspasoClick(SelectEvent event) {
		try {
			if(selectedOrdenTraspaso.getEstado().equals("PR")){
				verProcesar = false;
			}else{
				verProcesar = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in onRowSelectOrdenTraspasoClick: "
					+ e.getMessage());
		}
	}

	// SELECT DETALLE ORDEN Traspaso CLICK
	public void onRowSelectDetalleOrdenTraspasoClick(SelectEvent event) {
		try {
			verButtonDetalle = false;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in onRowSelectOrdenTraspasoClick: "
					+ e.getMessage());
		}
	}

	public void registrarOrdenTraspaso() {
		try {
			Date date = new Date();
			calcularTotal();
			System.out.println("Traspaso a registrarOrdenTraspaso: ");
			newOrdenTraspaso.setFechaRegistro(date);
			newOrdenTraspaso.setProyecto(selectedProyecto);
			newOrdenTraspaso.setFuncionario(selectedFuncionario);
			newOrdenTraspaso.setAlmacenOrigen(selectedAlmacenOrigen);
			newOrdenTraspaso.setAlmacenDestino(selectedAlmacen);
			newOrdenTraspaso = ordenTraspasoRegistration.register(newOrdenTraspaso);
			for(DetalleOrdenTraspaso d: listaDetalleOrdenTraspaso){
				d.setFechaRegistro(date);
				d.setUsuarioRegistro(usuarioSession);
				d.setOrdenTraspaso(newOrdenTraspaso);
				detalleOrdenTraspasoRegistration.register(d);
			}
			FacesUtil.infoMessage("Orden de Traspaso Registrada!", ""+newOrdenTraspaso.getId());
			initNewOrdenTraspaso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Registrar.");
		}
	}

	public void modificarOrdenTraspaso() {
		try {
			System.out.println("Traspaso a modificarOrdenTraspaso: ");
			double total = 0;
			for(DetalleOrdenTraspaso d: listaDetalleOrdenTraspaso){
				total = total + d.getTotal();
				detalleOrdenTraspasoRegistration.updated(d);
			}
			//borrado logico 
			for(DetalleOrdenTraspaso d: listDetalleOrdenTraspasoEliminados){
				if(d.getId() != 0){
					d.setEstado("RM");
					detalleOrdenTraspasoRegistration.updated(d);
				}
			}
			newOrdenTraspaso.setAlmacenDestino(selectedAlmacen);
			newOrdenTraspaso.setProyecto(selectedProyecto);
			newOrdenTraspaso.setTotalImporte(total);
			ordenTraspasoRegistration.updated(newOrdenTraspaso);
			FacesUtil.infoMessage("Orden de Traspaso Modificada!", ""+newOrdenTraspaso.getId());
			initNewOrdenTraspaso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Modificar.");
		}
	}

	public void eliminarOrdenTraspaso() {
		try {
			System.out.println("Traspaso a eliminarOrdenTraspaso: ");
			ordenTraspasoRegistration.remover(newOrdenTraspaso);
			for(DetalleOrdenTraspaso d: listaDetalleOrdenTraspaso){
				detalleOrdenTraspasoRegistration.remover(d);
			}
			FacesUtil.infoMessage("Orden de Traspaso Eliminada!", ""+newOrdenTraspaso.getId());
			initNewOrdenTraspaso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Eliminar.");
		}
	}

	public void procesarOrdenTraspaso(){
		try {
			Date date = new Date();
			//actualizar estado de orden Traspaso
			//selectedOrdenTraspaso.setEstado("PR");
			//selectedOrdenTraspaso.setFechaAprobacion(date);
			//ordenTraspasoRegistration.updated(selectedOrdenTraspaso);

			//actuaizar stock de AlmacenProducto
			//listaDetalleOrdenTraspaso = detalleOrdenTraspasoRepository.findAllByOrdenTraspaso(selectedOrdenTraspaso);
			//Almacen alm = selectedOrdenTraspaso.getAlmacen();
			//Proyecto prov = selectedOrdenTraspaso.getProyecto();
			//for(DetalleOrdenTraspaso d: listaDetalleOrdenTraspaso){
			//	Producto prod = d.getProducto();
			//	actualizarStock(prod, alm, prov, d.getCantidad(),date);
			//}
			FacesUtil.infoMessage("Orden de Traspaso Procesada!", "");
			initNewOrdenTraspaso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Proceso Incorrecto.");
		}
	}

	private void actualizarStock(Producto prod ,Almacen alm,Proyecto prov, int newStock,Date date) throws Exception {
		//0 . verificar si existe el producto en el almacen
		System.out.println("actualizarStock()");
		AlmacenProducto almProd =  almacenProductoRepository.findByProducto(prod);
		System.out.println("almProd = "+almProd);
		if(almProd != null){
			// 1 .  si existe el producto
			double oldStock = almProd.getStock();
			almProd.setStock(oldStock + newStock);
			almacenProductoRegistration.updated(almProd);
			return ;
		}
		// 2 . no existe el producto
		almProd = new AlmacenProducto();
		almProd.setAlmacen(alm);
		almProd.setProducto(prod);
		//almProd.setProveedor(prov);
		almProd.setStock(newStock);

		almProd.setEstado("AC");
		almProd.setFechaRegistro(date);
		almProd.setUsuarioRegistro(usuarioSession);

		almacenProductoRegistration.register(almProd);
	}

	public void cargarReporte(){
		try {
			urlOrdenTraspaso = loadURL();
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgVistaPreviaOrdenTraspaso').show();");

			initNewOrdenTraspaso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Proceso Incorrecto.");
		}
	}

	public String loadURL(){
		try{
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();  
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
			String urlPDFreporte = urlPath+"ReporteOrdenTraspaso?pIdOrdenTraspaso="+selectedOrdenTraspaso.getId()+"&pIdEmpresa=1&pUsuario="+usuarioSession;
			return urlPDFreporte;
		}catch(Exception e){
			return "error";
		}
	}

	// DETALLE ORDEN Traspaso ITEMS

	public void editarDetalleOrdenTraspaso(){
		tituloProducto = "Modificar Producto";
		selectedProducto = selectedDetalleOrdenTraspaso.getProducto();
		verButtonDetalle = true;
		editarOrdenTraspaso = true;
		calcular();
	}

	public void borrarDetalleOrdenTraspaso(){
		listaDetalleOrdenTraspaso.remove(selectedDetalleOrdenTraspaso);
		listDetalleOrdenTraspasoEliminados.add(selectedDetalleOrdenTraspaso);
		updateDataTable("formTableOrdenTraspaso:itemsTable1");
		verButtonDetalle = true;
	}

	public void limpiarDatosProducto(){
		selectedProducto = new Producto();
		selectedDetalleOrdenTraspaso = new DetalleOrdenTraspaso();
		updateDataTable("formTableOrdenTraspaso:itemsTable1");
		verButtonDetalle = true;
		editarOrdenTraspaso = false;
	}

	public void agregarDetalleOrdenTraspaso(){
		System.out.println("agregarDetalleOrdenTraspaso ");
		selectedDetalleOrdenTraspaso.setProducto(selectedProducto);
		listaDetalleOrdenTraspaso.add(0, selectedDetalleOrdenTraspaso);
		for(DetalleOrdenTraspaso d: listaDetalleOrdenTraspaso){
			System.out.println("for  listaDetalleOrdenTraspaso -> d= "+d.getPrecioUnitario());
		}

		selectedProducto = new Producto();
		selectedDetalleOrdenTraspaso = new DetalleOrdenTraspaso();
		updateDataTable("formTableOrdenTraspaso:itemsTable1");
		verButtonDetalle = true;
	}

	public void modificarDetalleOrdenTraspaso(){
		System.out.println("modificarDetalleOrdenTraspaso ");
		for(DetalleOrdenTraspaso d: listaDetalleOrdenTraspaso){
			if(d.equals(selectedDetalleOrdenTraspaso)){
				d = selectedDetalleOrdenTraspaso;
			}
		}
		selectedProducto = new Producto();
		selectedDetalleOrdenTraspaso = new DetalleOrdenTraspaso();
		updateDataTable("formTableOrdenTraspaso:itemsTable1");
		verButtonDetalle = true;
		editarOrdenTraspaso = false;
	}

	//calcular totales
	public void calcular(){
		System.out.println("calcular()");
		double precio = selectedProducto.getPrecioUnitario();
		double cantidad = selectedDetalleOrdenTraspaso.getCantidad();
		selectedDetalleOrdenTraspaso.setPrecioUnitario(precio);
		selectedDetalleOrdenTraspaso.setTotal(precio * cantidad);
	}

	public void calcularTotal(){
		double totalImporte = 0;
		for(DetalleOrdenTraspaso d : listaDetalleOrdenTraspaso){
			totalImporte =totalImporte + d.getTotal();
		}
		newOrdenTraspaso.setTotalImporte(totalImporte);
	}

	// ONCOMPLETETEXT Proyecto
	public List<Proyecto> completeProyecto(String query) {
		String upperQuery = query.toUpperCase();
		List<Proyecto> results = new ArrayList<Proyecto>();
		for(Proyecto i : listaProyecto) {
			if(i.getNombre().toUpperCase().startsWith(upperQuery)){
				results.add(i);
			}
		}         
		return results;
	}

	public void onRowSelectProyectoClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Proyecto i : listaProyecto){
			if(i.getNombre().equals(nombre)){
				selectedProyecto = i;
				return;
			}
		}
	}

	// ONCOMPLETETEXT FUNCIONARIO
	public List<Funcionario> completeFuncionario(String query) {
		String upperQuery = query.toUpperCase();
		List<Funcionario> results = new ArrayList<Funcionario>();
		for(Funcionario i : listFuncionario) {
			if(i.getNombre().toUpperCase().startsWith(upperQuery)){
				results.add(i);
			}
		}         
		return results;
	}

	public void onRowSelectFuncionarioClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Funcionario i : listFuncionario){
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

	public List<DetalleOrdenTraspaso> getListaDetalleOrdenTraspaso() {
		return listaDetalleOrdenTraspaso;
	}

	public void setListaDetalleOrdenTraspaso(List<DetalleOrdenTraspaso> listaDetalleOrdenTraspaso) {
		this.listaDetalleOrdenTraspaso = listaDetalleOrdenTraspaso;
	}

	public List<OrdenTraspaso> getListaOrdenTraspaso() {
		return listaOrdenTraspaso;
	}

	public void setListaOrdenTraspaso(List<OrdenTraspaso> listaOrdenTraspaso) {
		this.listaOrdenTraspaso = listaOrdenTraspaso;
	}

	public List<Almacen> getListaAlmacen() {
		return listaAlmacen;
	}

	public void setListaAlmacen(List<Almacen> listaAlmacen) {
		this.listaAlmacen = listaAlmacen;
	}

	public OrdenTraspaso getSelectedOrdenTraspaso() {
		return selectedOrdenTraspaso;
	}

	public void setSelectedOrdenTraspaso(OrdenTraspaso selectedOrdenTraspaso) {
		this.selectedOrdenTraspaso = selectedOrdenTraspaso;
	}

	public OrdenTraspaso getNewOrdenTraspaso() {
		return newOrdenTraspaso;
	}

	public void setNewOrdenTraspaso(OrdenTraspaso newOrdenTraspaso) {
		this.newOrdenTraspaso = newOrdenTraspaso;
	}

	public Producto getSelectedProducto() {
		return selectedProducto;
	}

	public void setSelectedProducto(Producto selectedProducto) {
		this.selectedProducto = selectedProducto;
	}

	public Proyecto getSelectedProyecto() {
		return selectedProyecto;
	}

	public void setSelectedProyecto(Proyecto selectedProyecto) {
		this.selectedProyecto = selectedProyecto;
	}

	public List<Proyecto> getListaProyecto() {
		return listaProyecto;
	}

	public void setListaProyecto(List<Proyecto> listaProyecto) {
		this.listaProyecto = listaProyecto;
	}

	public DetalleOrdenTraspaso getSelectedDetalleOrdenTraspaso() {
		return selectedDetalleOrdenTraspaso;
	}

	public void setSelectedDetalleOrdenTraspaso(
			DetalleOrdenTraspaso selectedDetalleOrdenTraspaso) {
		this.selectedDetalleOrdenTraspaso = selectedDetalleOrdenTraspaso;
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

	public String getUrlOrdenTraspaso() {
		return urlOrdenTraspaso;
	}

	public void setUrlOrdenTraspaso(String urlOrdenTraspaso) {
		this.urlOrdenTraspaso = urlOrdenTraspaso;
	}

	public boolean isEditarOrdenTraspaso() {
		return editarOrdenTraspaso;
	}

	public void setEditarOrdenTraspaso(boolean editarOrdenTraspaso) {
		this.editarOrdenTraspaso = editarOrdenTraspaso;
	}

	public Funcionario getSelectedFuncionario() {
		return selectedFuncionario;
	}

	public void setSelectedFuncionario(Funcionario selectedFuncionario) {
		this.selectedFuncionario = selectedFuncionario;
	}

	public List<Funcionario> getListFuncionario() {
		return listFuncionario;
	}

	public void setListFuncionario(List<Funcionario> listFuncionario) {
		this.listFuncionario = listFuncionario;
	}

	public Almacen getSelectedAlmacenOrigen() {
		return selectedAlmacenOrigen;
	}

	public void setSelectedAlmacenOrigen(Almacen selectedAlmacenOrigen) {
		this.selectedAlmacenOrigen = selectedAlmacenOrigen;
	}

}
