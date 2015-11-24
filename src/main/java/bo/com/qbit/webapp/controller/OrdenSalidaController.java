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

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.AlmacenRepository;
import bo.com.qbit.webapp.data.DetalleOrdenIngresoRepository;
import bo.com.qbit.webapp.data.OrdenIngresoRepository;
import bo.com.qbit.webapp.data.ProductoRepository;
import bo.com.qbit.webapp.data.ProveedorRepository;
import bo.com.qbit.webapp.data.UsuarioRepository;
import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.DetalleOrdenIngreso;
import bo.com.qbit.webapp.model.OrdenIngreso;
import bo.com.qbit.webapp.model.Producto;
import bo.com.qbit.webapp.model.Proveedor;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.service.DetalleOrdenIngresoRegistration;
import bo.com.qbit.webapp.service.OrdenIngresoRegistration;
import bo.com.qbit.webapp.util.FacesUtil;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "ordenSalidaController")
@ConversationScoped
public class OrdenSalidaController implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	private @Inject AlmacenRepository almacenRepository;
	private @Inject UsuarioRepository usuarioRepository;
	private @Inject OrdenIngresoRepository ordenIngresoRepository;
	private @Inject ProveedorRepository proveedorRepository;
	private @Inject ProductoRepository productoRepository;
	private @Inject DetalleOrdenIngresoRepository detalleOrdenIngresoRepository;

	private @Inject OrdenIngresoRegistration ordenIngresoRegistration;
	private @Inject DetalleOrdenIngresoRegistration detalleOrdenIngresoRegistration;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//ESTADOS
	private boolean modificar = false;
	private boolean registrar = false;
	private boolean crear = true;
	private boolean verButtonDetalle = true;

	private String tituloProducto = "Agregar Producto";
	private String tituloPanel = "Registrar Almacen";

	//OBJECT
	private Proveedor selectedProveedor;
	private Producto selectedProducto;
	private Almacen selectedAlmacen;
	private OrdenIngreso selectedOrdenIngreso;
	private OrdenIngreso newOrdenIngreso;
	private DetalleOrdenIngreso selectedDetalleOrdenIngreso;

	//LIST
	private List<Usuario> listUsuario = new ArrayList<Usuario>();
	private List<DetalleOrdenIngreso> listaDetalleOrdenIngreso = new ArrayList<DetalleOrdenIngreso>(); // ITEMS
	private List<OrdenIngreso> listaOrdenIngreso = new ArrayList<OrdenIngreso>();
	private List<Almacen> listaAlmacen = new ArrayList<Almacen>();
	private List<Proveedor> listaProveedor = new ArrayList<Proveedor>();

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;


	private boolean atencionCliente=false;

	@PostConstruct
	public void initNewOrdenIngreso() {

		beginConversation();

		usuarioSession = sessionMain.getUsuarioLoggin().getLogin();
		listUsuario = usuarioRepository.findAllOrderedByID();

		selectedProducto = new Producto();
		selectedAlmacen = new Almacen();
		selectedProveedor = new Proveedor();

		newOrdenIngreso = new OrdenIngreso();
		newOrdenIngreso.setEstado("AC");
		newOrdenIngreso.setFechaRegistro(new Date());
		newOrdenIngreso.setUsuarioRegistro(usuarioSession);

		selectedOrdenIngreso = null;
		selectedDetalleOrdenIngreso = new DetalleOrdenIngreso();

		// tituloPanel
		tituloPanel = "Registrar Orden Ingreso";

		modificar = false;
		registrar = false;
		crear = true;
		atencionCliente=false;

		listaDetalleOrdenIngreso = new ArrayList<DetalleOrdenIngreso>();
		listaOrdenIngreso = ordenIngresoRepository.findAllOrderedByID();
		listaAlmacen = almacenRepository.findAllOrderedByID();
		listaProveedor = proveedorRepository.findAllOrderedByID();
	}

	public void cambiarAspecto(){
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

	// SELECT ORDEN INGRESO CLICK
	public void onRowSelectOrdenIngresoClick(SelectEvent event) {
		try {
			//listaDetalleOrdenIngreso = detalleOrdenIngresoRepository.findAllByOrdenIngreso(selectedOrdenIngreso);
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
			FacesUtil.errorMessage("Registro Incorrecto.");
		}
	}

	public void modificarOrdenIngreso() {
		try {
			System.out.println("Ingreso a modificarOrdenIngreso: ");
			newOrdenIngreso.setAlmacen(selectedAlmacen);
			newOrdenIngreso.setProveedor(selectedProveedor);
			ordenIngresoRegistration.updated(newOrdenIngreso);
			for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){
				detalleOrdenIngresoRegistration.updated(d);
			}
			//borrado logico 
			for(DetalleOrdenIngreso d: listDetalleOrdenIngresoEliminados){
				d.setEstado("RM");
				detalleOrdenIngresoRegistration.updated(d);
			}
			FacesUtil.infoMessage("Orden de Ingreso Modificada!", ""+newOrdenIngreso.getId());
			initNewOrdenIngreso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Modificacion Incorrecto.");
		}
	}

	public void eliminarOrdenIngreso() {
		try {
			System.out.println("Ingreso a eliminarOrdenIngreso: ");
			ordenIngresoRegistration.remover(newOrdenIngreso);
			for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){
				detalleOrdenIngresoRegistration.remover(d);
			}
			initNewOrdenIngreso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Registro Incorrecto.");
		}
	}

	// DETALLE ORDEN INGRESO ITEMS

	public void modificarDetalleOrdenIngreso(){
		tituloProducto = "Modificar Producto";
		selectedProducto = selectedDetalleOrdenIngreso.getProducto();
		verButtonDetalle = true;
	}

	private List<DetalleOrdenIngreso> listDetalleOrdenIngresoEliminados = new ArrayList<DetalleOrdenIngreso>();

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
				return;
			}
		}
		calcular();
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

}
