package bo.com.qbit.webapp.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
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

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.AlmacenProductoRepository;
import bo.com.qbit.webapp.data.AlmacenRepository;
import bo.com.qbit.webapp.data.CierreGestionAlmacenRepository;
import bo.com.qbit.webapp.data.DetalleOrdenIngresoRepository;
import bo.com.qbit.webapp.data.OrdenIngresoRepository;
import bo.com.qbit.webapp.data.PartidaRepository;
import bo.com.qbit.webapp.data.ProductoRepository;
import bo.com.qbit.webapp.data.ProveedorRepository;
import bo.com.qbit.webapp.data.UnidadMedidaRepository;
import bo.com.qbit.webapp.data.UsuarioRepository;
import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.DetalleOrdenIngreso;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.FachadaOrdenIngreso;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.OrdenIngreso;
import bo.com.qbit.webapp.model.Partida;
import bo.com.qbit.webapp.model.Producto;
import bo.com.qbit.webapp.model.Proveedor;
import bo.com.qbit.webapp.model.UnidadMedida;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.service.AlmacenRegistration;
import bo.com.qbit.webapp.service.DetalleOrdenIngresoRegistration;
import bo.com.qbit.webapp.service.OrdenIngresoRegistration;
import bo.com.qbit.webapp.service.PartidaRegistration;
import bo.com.qbit.webapp.service.ProductoRegistration;
import bo.com.qbit.webapp.util.Cifrado;
import bo.com.qbit.webapp.util.FacesUtil;
import bo.com.qbit.webapp.util.SessionMain;
import bo.com.qbit.webapp.util.StreamUtil;

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
	private @Inject PartidaRepository partidaRepository;
	private @Inject UnidadMedidaRepository unidadMedidaRepository;
	private @Inject AlmacenProductoRepository almacenProductoRepository;
	private @Inject CierreGestionAlmacenRepository cierreGestionAlmacenRepository;

	private @Inject OrdenIngresoRegistration ordenIngresoRegistration;
	private @Inject DetalleOrdenIngresoRegistration detalleOrdenIngresoRegistration;
	private @Inject ProductoRegistration productoRegistration;
	private @Inject AlmacenRegistration almacenRegistration;
	private @Inject PartidaRegistration partidaRegistration;


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
	private boolean verReport = false;
	private boolean nuevoProducto = false;
	private boolean importarFile = false;//para habilitar importacion
	private boolean devolucion = false;//tipo DEVOLUCION PARA SACAR PRECIO PROMEDIO(SE PUSO TODO EN FALSO PARA QUE NO PROMEDIE)

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
	private UnidadMedida selectedUnidadMedida;
	private double precioPromedio = 0;

	//LIST
	private List<Usuario> listUsuario = new ArrayList<Usuario>();
	private List<DetalleOrdenIngreso> listaDetalleOrdenIngreso = new ArrayList<DetalleOrdenIngreso>(); // ITEMS
	private List<OrdenIngreso> listaOrdenIngreso = new ArrayList<OrdenIngreso>();
	private List<Almacen> listaAlmacen = new ArrayList<Almacen>();
	private List<Proveedor> listaProveedor = new ArrayList<Proveedor>();
	private List<DetalleOrdenIngreso> listDetalleOrdenIngresoEliminados = new ArrayList<DetalleOrdenIngreso>();
	private List<UnidadMedida> listUnidadMedida = new ArrayList<UnidadMedida>();

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;
	private Gestion gestionSesion;
	private Empresa empresaLogin;

	private boolean atencionCliente = false;

	//CREACION NUEVO PRODUCTO
	private Producto newProducto= new Producto();

	//FACADE
	private @Inject  FachadaOrdenIngreso fachadaOrdenIngreso;

	@PostConstruct
	public void initNewOrdenIngreso() {

		System.out.println(" ... initNewOrdenIngreso ...");

		usuarioSession = sessionMain.getUsuarioLogin().getLogin();
		gestionSesion = sessionMain.getGestionLogin();
		listUsuario = usuarioRepository.findAllOrderedByID();
		empresaLogin = sessionMain.getEmpresaLogin();

		//inicializar fachadaOrdenIngreso
		//fachadaOrdenIngreso = new FachadaOrdenIngreso();

		selectedProducto = new Producto();
		selectedAlmacen = new Almacen();
		selectedProveedor = new Proveedor();

		selectedOrdenIngreso = null;
		selectedDetalleOrdenIngreso = new DetalleOrdenIngreso();
		precioPromedio = 0;
		devolucion = false;

		// tituloPanel
		tituloPanel = "Registrar Orden Ingreso";

		modificar = false;
		registrar = false;
		crear = true;
		atencionCliente=false;
		verProcesar = true;
		nuevoProducto = false;
		importarFile = false;
		verReport = false;

		listaDetalleOrdenIngreso = new ArrayList<DetalleOrdenIngreso>();
		listaOrdenIngreso = ordenIngresoRepository.findAllOrderedByIDGestion(gestionSesion);
		listaAlmacen = almacenRepository.findAllActivosOrderedByID();
		listaProveedor = proveedorRepository.findAllActivoOrderedByID();

		int numeroCorrelativo = ordenIngresoRepository.obtenerNumeroOrdenIngreso(gestionSesion);
		newOrdenIngreso = new OrdenIngreso();
		//newOrdenIngreso.setCorrelativo(cargarCorrelativo(listaOrdenIngreso.size()+1));
		newOrdenIngreso.setCorrelativo(cargarCorrelativo(numeroCorrelativo));
		newOrdenIngreso.setEstado("AC");
		newOrdenIngreso.setGestion(gestionSesion);
		newOrdenIngreso.setFechaDocumento(new Date());
		newOrdenIngreso.setFechaRegistro(new Date());
		newOrdenIngreso.setUsuarioRegistro(usuarioSession);

		//cuando agreguen un nuevo producto
		newProducto = new Producto();

	}

	public void cambiarAspecto(){

		//Verifica la gestion , sobre el levantamiento si ya se hizo el inicial
		if(gestionSesion.isIniciada()){

			//verificar si el usuario logeado tiene almacen registrado
			selectedAlmacenOrigen = almacenRepository.findAlmacenForUser(sessionMain.getUsuarioLogin());
			if(selectedAlmacenOrigen.getId() == -1){
				FacesUtil.infoMessage("Usuario "+usuarioSession, "No tiene asignado un almacen");
				return;
			}
			modificar = false;
			registrar = true;
			crear = false;
		}else{
			FacesUtil.infoMessage("INFORMACION", "Antes debe proceder a realizar una Toma de Inventario Inicial ");
		}
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

	public void verificarEstadoImportacion(){
		if(newOrdenIngreso.getMotivoIngreso().equals("TRASPASO")){
			importarFile = true;
		}else{
			importarFile = false;
		}
		if(newOrdenIngreso.getMotivoIngreso().equals("DEVOLUCION")){
			devolucion = false;
		}else{
			devolucion = false;
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
		if( selectedAlmacen.getId()==0 || selectedProveedor.getId()==0 || newOrdenIngreso.getNumeroDocumento().isEmpty()){
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios.");
			return;
		}
		if(listaDetalleOrdenIngreso.size()==0 ){
			FacesUtil.infoMessage("VALIDACION", "Debe Agregar items..");
			return;
		}
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
			FacesUtil.infoMessage("Orden de Ingreso Registrada", ""+newOrdenIngreso.getCorrelativo());
			initNewOrdenIngreso();
		} catch (Exception e) {
			System.out.println("registrarOrdenIngreso() ERROR: "+e.getMessage());
		}
	}

	public void modificarOrdenIngreso() {
		try {
			System.out.println("Ingreso a modificarOrdenIngreso: ");
			Date fechaActual = new Date();
			double total = 0;
			for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){
				if(d.getId()==0){//si es un nuevo registro
					d.setFechaRegistro(fechaActual);
					d.setUsuarioRegistro(usuarioSession);
					d.setEstado("AC");
					d.setOrdenIngreso(newOrdenIngreso);
					detalleOrdenIngresoRegistration.register(d);
				}
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
			FacesUtil.infoMessage("Orden de Ingreso Modificada", ""+newOrdenIngreso.getCorrelativo());
			initNewOrdenIngreso();
		} catch (Exception e) {
			System.out.println("modificarOrdenIngreso() ERROR: "+e.getMessage());
		}
	}

	public void eliminarOrdenIngreso() {
		try {
			System.out.println("Ingreso a eliminarOrdenIngreso: ");
			ordenIngresoRegistration.remover(selectedOrdenIngreso);
			//			for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){
			//				detalleOrdenIngresoRegistration.remover(d);
			//			}
			FacesUtil.infoMessage("Orden de Ingreso Eliminada!", ""+newOrdenIngreso.getCorrelativo());
			initNewOrdenIngreso();
		} catch (Exception e) {
			System.out.println("eliminarOrdenIngreso() ERROR: "+e.getMessage());
		}
	}

	public void procesarOrdenIngreso(){
		try {
			System.out.println("procesarOrdenIngreso() ");
			Date fechaActual = new Date();
			// actualizar estado de orden ingreso
			selectedOrdenIngreso.setEstado("PR");
			selectedOrdenIngreso.setFechaAprobacion(fechaActual);
			ordenIngresoRegistration.updated(selectedOrdenIngreso);

			Proveedor proveedor = selectedOrdenIngreso.getProveedor();
			// actuaizar stock de AlmacenProducto
			listaDetalleOrdenIngreso = detalleOrdenIngresoRepository.findAllByOrdenIngreso(selectedOrdenIngreso);
			for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){
				Producto prod = d.getProducto();
				//actualiza el esstock por producto almacen(teniendo en cuenta la agrupacion de productos)
				System.out.println("fachadaOrdenIngreso.actualizarStock() ");
				fachadaOrdenIngreso.actualizarStock(selectedOrdenIngreso.getAlmacen(),proveedor,prod, d.getCantidad(),fechaActual,d.getPrecioUnitario(),usuarioSession);
				//registra la transaccion de entrada del producto
				System.out.println("fachadaOrdenIngreso.actualizarKardexProducto() ");
				fachadaOrdenIngreso.actualizarKardexProducto(selectedOrdenIngreso.getCorrelativo(),selectedOrdenIngreso.getAlmacen(),gestionSesion, prod,fechaActual, d.getCantidad(),d.getPrecioUnitario(),usuarioSession);
				//registra los stock de los producto , para luego utilizar PEPS en ordenes de traspaso y salida
				System.out.println("fachadaOrdenIngreso.cargarDetalleProducto() ");
				fachadaOrdenIngreso.cargarDetalleProducto(fechaActual,selectedOrdenIngreso.getAlmacen(),d.getProducto(), d.getCantidad(), d.getPrecioUnitario(), d.getFechaRegistro(), selectedOrdenIngreso.getCorrelativo(),usuarioSession);
			}

			FacesUtil.infoMessage("Orden de Ingreso Procesada!", "");
			initNewOrdenIngreso();
		} catch (Exception e) {
			System.out.println("ERROR "+e.getMessage());
			FacesUtil.errorMessage("Error al Procesar!");
		}
	}

	public void cargarReporte(){
		try {
			urlOrdenIngreso = loadURL();
			//RequestContext context = RequestContext.getCurrentInstance();
			//context.execute("PF('dlgVistaPreviaOrdenIngreso').show();");
			verReport = true;

			//initNewOrdenIngreso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Proceso Incorrecto.");
		}
	}

	public String loadURL(){
		try{
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();  
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
			String urlPDFreporte = urlPath+"ReporteOrdenIngreso?pIdOrdenIngreso="+selectedOrdenIngreso.getId()+"&pUsuario="+usuarioSession+"&pNitEmpresa="+empresaLogin.getNIT()+"&pNombreEmpresa="+empresaLogin.getRazonSocial();
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
		FacesUtil.resetDataTable("formTableOrdenIngreso:itemsTable1");
		verButtonDetalle = true;
	}

	public void limpiarDatosProducto(){
		selectedProducto = new Producto();
		selectedDetalleOrdenIngreso = new DetalleOrdenIngreso();
		FacesUtil.resetDataTable("formTableOrdenIngreso:itemsTable1");
		verButtonDetalle = true;
		editarOrdenIngreso = false;
	}

	public void agregarDetalleOrdenIngreso(){
		System.out.println("agregarDetalleOrdenIngreso ");
		//verificar si el producto ya fue agregado
		if(verificarProductoAgregado(selectedProducto)){
			FacesUtil.infoMessage("OBSERVACION", "Este producto ya fue agregado.");
			return ;
		}
		selectedDetalleOrdenIngreso.setProducto(selectedProducto);
		listaDetalleOrdenIngreso.add(0, selectedDetalleOrdenIngreso);
		selectedProducto = new Producto();
		selectedDetalleOrdenIngreso = new DetalleOrdenIngreso();
		FacesUtil.resetDataTable("formTableOrdenIngreso:itemsTable1");
		verButtonDetalle = true;
	}

	private boolean verificarProductoAgregado(Producto selectedProducto){
		for(DetalleOrdenIngreso detalle : listaDetalleOrdenIngreso){
			if(detalle.getProducto().getId()==selectedProducto.getId()){
				return true;
			}
		}
		return false;
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
		FacesUtil.resetDataTable("formTableOrdenIngreso:itemsTable1");
		verButtonDetalle = true;
		editarOrdenIngreso = false;
	}

	//calcular totales
	public void calcular(){
		System.out.println("calcular()");
		double precio = selectedDetalleOrdenIngreso.getPrecioUnitario();
		double cantidad = selectedDetalleOrdenIngreso.getCantidad();
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
				//verificar si el almacen-gestion ya fue cerrado
				if(cierreGestionAlmacenRepository.finAlmacenGestionCerrado(i,gestionSesion) != null){
					FacesUtil.infoMessage("INFORMACION", "El lmacen "+i.getNombre()+" fué cerrado");
					listaAlmacen.remove(i);
					selectedAlmacen = new Almacen();
					return ;
				}
				selectedAlmacen = i;
				return;
			}
		}
	}

	// ONCOMPLETETEXT PRODUCTO
	public List<Producto> completeProducto(String query) {
		String upperQuery = query.toUpperCase();
		return productoRepository.findAllProductoForQueryNombre(upperQuery);
	}

	public void onRowSelectProductoClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		List<Producto> listProducto = productoRepository.findAllProductoActivosByID();
		for(Producto i : listProducto){
			if(i.getNombre().equals(nombre)){
				selectedProducto = i;
//				if(devolucion){
//					calcularPrecioPromedioForDevolucion(selectedProducto);
//				}else{
//					selectedDetalleOrdenIngreso.setPrecioUnitario(0);
//				}
				calcular();
				return;
			}
		}
	}

	private void calcularPrecioPromedioForDevolucion(Producto producto){
		precioPromedio = almacenProductoRepository.findPrecioPromedioByProducto(producto);
		selectedDetalleOrdenIngreso.setPrecioUnitario(precioPromedio);
	}

	// SELECCIONAR AUTOCOMPLETE UNIDAD DE MEDIDA
	public List<UnidadMedida> completeUnidadMedida(String query) {
		String upperQuery = query.toUpperCase();
		listUnidadMedida = unidadMedidaRepository.findAllUnidadMedidaForDescription(upperQuery);
		System.out.println("listUnidadMedida.size(): "+listUnidadMedida.size());
		return listUnidadMedida;
	}

	public void onRowSelectUnidadMedidaClick(SelectEvent event) {
		String nombre = event.getObject().toString();
		System.out.println("Seleccionado onRowSelectUnidadMedidaClick: selectedMedida.getNombre():"+selectedUnidadMedida.getNombre());
		for(UnidadMedida um: listUnidadMedida){
			if(um.getNombre().equals(nombre)){
				selectedUnidadMedida = um;
			}
		}

	}

	//IMPORT - EXPORT EXCEL

	private UploadedFile uploadedFile;

	public void handleFileUpload(FileUploadEvent event) {
		uploadedFile = event.getFile();
		FacesUtil.infoMessage("Correcto", event.getFile().getFileName() + ", archivo cargado.");
	}

	public void convertJava() {
		if(uploadedFile==null){
			//FacesUtil.infoMessage("VALIDACION", "No se cargo ningun archivo.");
			return;
		}
		System.out.println("convertJava() ");
		listaDetalleOrdenIngreso = new ArrayList<DetalleOrdenIngreso>();
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		try {
			InputStream input = uploadedFile.getInputstream();
			//String ext = FilenameUtils.getExtension(uploadedFile.getFileName());

			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			archivo = StreamUtil.stream2file(input);
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);

			// Lectura del fichero
			//DATOS ORDENTRASPASO
			//0 correlativo
			String correlativoOrdenTraspaso = Cifrado.Desencriptar( br.readLine(),12);
			newOrdenIngreso.setTipoDocumento("ACTA DE TRASPASO");
			newOrdenIngreso.setNumeroDocumento(correlativoOrdenTraspaso);
			//ALMACEN
			//1 direccion
			String direccion = Cifrado.Desencriptar( br.readLine(),12);
			//2 codigo
			String codigo = Cifrado.Desencriptar( br.readLine(),12);
			//3 online
			String online = Cifrado.Desencriptar( br.readLine(),12);
			//4 nombre
			String nombre = Cifrado.Desencriptar( br.readLine(),12);
			//5 telefono
			String telefono = Cifrado.Desencriptar( br.readLine(),12);
			//6 tipoAlmacen
			String tipoAlmacen = Cifrado.Desencriptar( br.readLine(),12);
			selectedAlmacen = almacenRepository.findByCodigo(codigo.toUpperCase());
			if(selectedAlmacen == null){
				Almacen almacen = new Almacen();
				almacen.setCodigo(codigo);
				almacen.setDireccion(direccion);
				almacen.setEstado("AC");
				almacen.setFechaRegistro(new Date());
				almacen.setNombre(nombre);
				almacen.setOnline(online.equals("true")?false:true);//invertido
				almacen.setTelefono(telefono);
				almacen.setTipoAlmacen(tipoAlmacen);
				almacen.setUsuarioRegistro(usuarioSession);
				selectedAlmacen = almacenRegistration.register(almacen);
			}
			String linea;
			while((linea=br.readLine())!=null){
				//PRODUCTO
				//7 codigo
				String codigoProducto = Cifrado.Desencriptar(linea,12);
				//8 nombre
				String nombreProducto = Cifrado.Desencriptar( br.readLine(),12);
				//9 descripcion
				String descripcionProducto = Cifrado.Desencriptar( br.readLine(),12);
				//10 precioUnitario
				String precioUnitarioProducto = Cifrado.Desencriptar( br.readLine(),12);
				//11 tipoProducto
				String tipoProductoProducto = Cifrado.Desencriptar( br.readLine(),12);
				//12 unidadMedida
				String unidadMedidaProducto = Cifrado.Desencriptar( br.readLine(),12);
				//>>>>>>>>PARTIDA<<<<<<<<
				//13 codigoPartida
				String codigoPartida = Cifrado.Desencriptar( br.readLine(),12);
				//14 nombre
				String nombrePartida = Cifrado.Desencriptar( br.readLine(),12);
				//15 descripcion
				String descripcionPartida = Cifrado.Desencriptar( br.readLine(),12);
				//>>>>>>>>DETALLE ORDEN INGRESO<<<<<<<<<
				//16 cantidadDOI
				String cantidadDOI = Cifrado.Desencriptar( br.readLine(),12);
				//17 observacionDOI
				String observacionDOI = Cifrado.Desencriptar( br.readLine(),12);
				//18 totalDOI
				String totalDOI = Cifrado.Desencriptar( br.readLine(),12);
				//19 precioUnitario
				String precioUnitarioDOI = Cifrado.Desencriptar( br.readLine(),12);

				Partida partida = partidaRepository.findByCodigo(codigoPartida.toUpperCase());
				if(partida==null){
					partida = new Partida();
					partida.setCodigo(codigoPartida);
					partida.setDescripcion(descripcionPartida);
					partida.setEstado("AC");
					partida.setFechaRegistro(new Date());
					partida.setNombre(nombrePartida);
					partida.setUsuarioRegistro(usuarioSession);
					partida = partidaRegistration.register(partida);
				}
				UnidadMedida unidadMedida =  unidadMedidaRepository.findByNombre(unidadMedidaProducto.toUpperCase());
				if(unidadMedida == null){
					unidadMedida = new UnidadMedida();
					unidadMedida.setNombre(unidadMedidaProducto);
					unidadMedida.setDescripcion(unidadMedidaProducto);
					unidadMedida.setEstado("AC");
					unidadMedida.setUsuarioRegistro(usuarioSession);
					unidadMedida.setFechaRegistro(new Date());
				}
				Producto producto = productoRepository.findByCodigo(codigoProducto.toUpperCase());
				if(producto == null){
					producto = new Producto();
					producto.setCodigo(codigoProducto);
					producto.setDescripcion(descripcionProducto);
					producto.setEstado("AC");
					producto.setFechaRegistro(new Date());
					producto.setNombre(nombreProducto);
					producto.setPartida(partida);
					producto.setPrecioUnitario(Double.parseDouble(precioUnitarioProducto));
					producto.setTipoProducto(tipoProductoProducto);
					producto.setUnidadMedidas(unidadMedida);
					producto.setUsuarioRegistro(usuarioSession);
					producto = productoRegistration.register(producto);
				}
				DetalleOrdenIngreso detalle = new DetalleOrdenIngreso();
				detalle.setCantidad(Double.parseDouble(cantidadDOI));
				detalle.setEstado("AC");
				detalle.setFechaRegistro(new Date());
				detalle.setObservacion(observacionDOI);
				detalle.setOrdenIngreso(newOrdenIngreso);
				detalle.setProducto(producto);
				detalle.setTotal(Double.parseDouble(totalDOI));
				detalle.setPrecioUnitario(Double.parseDouble(precioUnitarioDOI));
				detalle.setUsuarioRegistro(usuarioSession);
				listaDetalleOrdenIngreso.add(detalle);
			}
			importarFile = false;//ocultar boton importar
		}
		catch(Exception e){
			listaDetalleOrdenIngreso = new ArrayList<DetalleOrdenIngreso>();
			e.printStackTrace();
		}finally{
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta 
			// una excepcion.
			try{                    
				if( null != fr ){   
					fr.close();     
				}                  
			}catch (Exception e2){ 
				e2.printStackTrace();
			}
		}
	}

	// SELECCIONAR AUTOCOMPLETES AREA PRODUCTO
	public List<Partida> completePartida(String query) {
		String upperQuery = query.toUpperCase();
		return partidaRepository.findAllPartidaForDescription(upperQuery);
	}

	public void onRowSelectPartidaClick() {
		System.out.println("Seleccionado onRowSelectPartidaClick: "
				+ this.newProducto.getPartida().getNombre());

		List<Partida> listPartida = partidaRepository.traerPartidaActivas();
		for (Partida row : listPartida) {
			if (row.getNombre().equals(this.newProducto.getPartida().getNombre())) {
				this.newProducto.setPartida(row);
			}
		}
	}

	public void registrarProducto() {
		try {
			System.out.println("Ingreso a registrarProducto: ");
			newProducto.setUnidadMedidas(selectedUnidadMedida);
			newProducto.setEstado("AC");
			newProducto.setFechaRegistro(new Date());
			newProducto.setUsuarioRegistro(usuarioSession);
			newProducto.setUsuarioRegistro(usuarioSession);
			newProducto.setFechaRegistro(new Date());
			newProducto = productoRegistration.register(newProducto);
			selectedProducto = newProducto;
			calcular();
			FacesUtil.infoMessage("Producto Registrado!",newProducto.getNombre());
			setNuevoProducto(false);
			newProducto = new Producto();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al registrar");
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

	public boolean isVerReport() {
		return verReport;
	}

	public void setVerReport(boolean verReport) {
		this.verReport = verReport;
	}

	public boolean isNuevoProducto() {
		return nuevoProducto;
	}

	public void setNuevoProducto(boolean nuevoProducto) {
		this.nuevoProducto = nuevoProducto;
	}

	public Producto getNewProducto() {
		return newProducto;
	}

	public void setNewProducto(Producto newProducto) {
		this.newProducto = newProducto;
	}

	public boolean isImportarFile() {
		return importarFile;
	}

	public void setImportarFile(boolean importarFile) {
		this.importarFile = importarFile;
	}

	public UnidadMedida getSelectedUnidadMedida() {
		return selectedUnidadMedida;
	}

	public void setSelectedUnidadMedida(UnidadMedida selectedUnidadMedida) {
		this.selectedUnidadMedida = selectedUnidadMedida;
	}

	public double getPrecioPromedio() {
		return precioPromedio;
	}

	public void setPrecioPromedio(double precioPromedio) {
		this.precioPromedio = precioPromedio;
	}

	public boolean getDevolucion() {
		return devolucion;
	}

	public void setDevolucion(boolean devolucion) {
		this.devolucion = devolucion;
	}

}
