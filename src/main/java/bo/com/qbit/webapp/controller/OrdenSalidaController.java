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

import bo.com.qbit.webapp.data.AlmacenRepository;
import bo.com.qbit.webapp.data.CierreGestionAlmacenRepository;
import bo.com.qbit.webapp.data.DetalleOrdenSalidaRepository;
import bo.com.qbit.webapp.data.DetalleProductoRepository;
import bo.com.qbit.webapp.data.DetalleUnidadRepository;
import bo.com.qbit.webapp.data.FuncionarioRepository;
import bo.com.qbit.webapp.data.OrdenSalidaRepository;
import bo.com.qbit.webapp.data.ProductoRepository;
import bo.com.qbit.webapp.data.ProyectoRepository;
import bo.com.qbit.webapp.data.UsuarioRepository;
import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.DetalleOrdenSalida;
import bo.com.qbit.webapp.model.DetalleProducto;
import bo.com.qbit.webapp.model.DetalleUnidad;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.FachadaOrdenSalida;
import bo.com.qbit.webapp.model.Funcionario;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.OrdenSalida;
import bo.com.qbit.webapp.model.Producto;
import bo.com.qbit.webapp.model.Proyecto;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.service.DetalleOrdenSalidaRegistration;
import bo.com.qbit.webapp.service.DetalleProductoRegistration;
import bo.com.qbit.webapp.service.OrdenSalidaRegistration;
import bo.com.qbit.webapp.util.FacesUtil;
import bo.com.qbit.webapp.util.NumberUtil;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "ordenSalidaController")
@ConversationScoped
public class OrdenSalidaController implements Serializable {

	private static final long serialVersionUID = 749163787421586877L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	//Repository
	private @Inject AlmacenRepository almacenRepository;
	private @Inject UsuarioRepository usuarioRepository;
	private @Inject OrdenSalidaRepository ordenSalidaRepository;
	private @Inject ProductoRepository productoRepository;
	private @Inject DetalleOrdenSalidaRepository detalleOrdenSalidaRepository;
	private @Inject FuncionarioRepository funcionarioRepository;
	private @Inject DetalleUnidadRepository detalleUnidadRepository;
	private @Inject ProyectoRepository proyectoRepository;
	private @Inject DetalleProductoRepository detalleProductoRepository;
	private @Inject CierreGestionAlmacenRepository cierreGestionAlmacenRepository;

	//Registration
	private @Inject OrdenSalidaRegistration ordenSalidaRegistration;
	private @Inject DetalleOrdenSalidaRegistration detalleOrdenSalidaRegistration;
	private @Inject DetalleProductoRegistration detalleProductoRegistration;
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
	private boolean editarOrdenSalida = false;
	private boolean verProcesar = true;
	private boolean verReport = false;

	private String tituloProducto = "Agregar Producto";
	private String tituloPanel = "Registrar Orden Salida";
	private String urlOrdenSalida = "";

	//OBJECT
	private Funcionario selectedFuncionario;
	private DetalleUnidad selectedDetalleUnidad;
	private Producto selectedProducto;
	private Almacen selectedAlmacen;
	private Almacen selectedAlmacenOrigen;
	private OrdenSalida selectedOrdenSalida;
	private OrdenSalida newOrdenSalida;
	private DetalleOrdenSalida selectedDetalleOrdenSalida;
	private Proyecto selectedProyecto;

	//LIST
	private List<Usuario> listUsuario = new ArrayList<Usuario>();
	private List<DetalleOrdenSalida> listaDetalleOrdenSalida = new ArrayList<DetalleOrdenSalida>(); // ITEMS
	private List<OrdenSalida> listaOrdenSalida = new ArrayList<OrdenSalida>();
	private List<Almacen> listaAlmacen = new ArrayList<Almacen>();
	private List<Funcionario> listaFuncionario = new ArrayList<Funcionario>();
	private List<DetalleOrdenSalida> listDetalleOrdenSalidaEliminados = new ArrayList<DetalleOrdenSalida>();
	private List<DetalleUnidad> listDetalleUnidad = new ArrayList<DetalleUnidad>();
	private List<Proyecto> listaProyecto = new ArrayList<Proyecto>();
	private List<Producto> listaProducto= new ArrayList<Producto>();

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;
	private Gestion gestionSesion;
	private Empresa empresaLogin;

	private boolean atencionCliente=false;

	//Fachada
	private @Inject FachadaOrdenSalida fachadaOrdenSalida;

	@PostConstruct
	public void initNewOrdenSalida() {

		usuarioSession = sessionMain.getUsuarioLogin().getLogin();
		gestionSesion = sessionMain.getGestionLogin();
		listUsuario = usuarioRepository.findAllOrderedByID();
		empresaLogin = sessionMain.getEmpresaLogin();

		//inicializar FachadaOrdenSalida
		//fachadaOrdenSalida =new FachadaOrdenSalida();

		selectedProyecto = new Proyecto();
		selectedDetalleUnidad = new DetalleUnidad();
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
		verReport = false;

		listaDetalleOrdenSalida = new ArrayList<DetalleOrdenSalida>();
		listaOrdenSalida = ordenSalidaRepository.findAllOrderedByIDGestion(gestionSesion);

		//obtener la primer unidad solicitante
		//List<DetalleUnidad> l = detalleUnidadRepository.findAll100UltimosDetalleUnidad();
		//selectedDetalleUnidad = l.size()>0?l.get(0):new DetalleUnidad();
		int numeroCorrelativo = ordenSalidaRepository.obtenerNumeroOrdenSalida(gestionSesion);
		newOrdenSalida = new OrdenSalida();
		newOrdenSalida.setCorrelativo(cargarCorrelativo(numeroCorrelativo));
		newOrdenSalida.setEstado("AC");
		newOrdenSalida.setGestion(gestionSesion);
		newOrdenSalida.setFechaPedido(new Date());
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
		selectedAlmacen = selectedOrdenSalida.getAlmacen();
		selectedDetalleUnidad = selectedOrdenSalida.getUnidadSolicitante();
		selectedFuncionario = selectedOrdenSalida.getFuncionario();
		selectedProyecto = selectedOrdenSalida.getProyecto();
		listaDetalleOrdenSalida = detalleOrdenSalidaRepository.findAllByOrdenSalida(selectedOrdenSalida);
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
			System.out.println("Error in onRowSelectDetalleOrdenSalidaClick: "
					+ e.getMessage());
		}
	}

	public void registrarOrdenSalida() {
		if( selectedAlmacen.getId()==0 || selectedDetalleUnidad.getId()==0 || selectedFuncionario.getId()==0 || newOrdenSalida.getNumeroPedidoMateriales().isEmpty() || selectedProyecto.getId()==0 || selectedAlmacen.getId()==0){
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios.");
			return;
		}
		if(listaDetalleOrdenSalida.size()==0 ){
			FacesUtil.infoMessage("VALIDACION", "Debe Agregar items.");
			return;
		}
		try {
			Date fechaActual = new Date();
			calcularTotal();
			System.out.println("Ingreso a registrarOrdenSalida: ");
			newOrdenSalida.setUnidadSolicitante(selectedDetalleUnidad);
			newOrdenSalida.setFuncionario(selectedFuncionario);
			newOrdenSalida.setProyecto(selectedProyecto);
			newOrdenSalida.setAlmacen(selectedAlmacen);
			newOrdenSalida.setGestion(gestionSesion);
			newOrdenSalida.setEstado("AC");
			newOrdenSalida.setFechaRegistro(fechaActual);
			newOrdenSalida.setUsuarioRegistro(usuarioSession);
			newOrdenSalida = ordenSalidaRegistration.register(newOrdenSalida);
			for(DetalleOrdenSalida d: listaDetalleOrdenSalida){
				d.setCantidadEntregada(0);
				d.setFechaRegistro(fechaActual);
				d.setEstado("AC");
				d.setUsuarioRegistro(usuarioSession);
				d.setOrdenSalida(newOrdenSalida);
				detalleOrdenSalidaRegistration.register(d);
			}
			FacesUtil.infoMessage("Orden de Salida Registrada!", ""+newOrdenSalida.getCorrelativo());
			initNewOrdenSalida();
		} catch (Exception e) {
			System.out.println("registrarOrdenSalida() ERROR: "+ e.getMessage());
		}
	}


	public void modificarOrdenSalida() {
		try {
			System.out.println("Ingreso a modificarOrdenSalida: ");
			Date fechaActual = new Date();
			double total = 0;
			for(DetalleOrdenSalida d: listaDetalleOrdenSalida){
				d.setCantidadEntregada(0);
				if(d.getId()==0){//si es un nuevo registro
					d.setFechaRegistro(fechaActual);
					d.setUsuarioRegistro(usuarioSession);
					d.setEstado("AC");
					d.setOrdenSalida(newOrdenSalida);
					detalleOrdenSalidaRegistration.register(d);
				}
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
			FacesUtil.infoMessage("Orden de Salida Modificada!", ""+newOrdenSalida.getCorrelativo());
			initNewOrdenSalida();

		} catch (Exception e) {
			System.out.println("modificarOrdenSalida() ERROR: "+ e.getMessage());
		}
	}

	public void eliminarOrdenSalida() {
		try {
			System.out.println("Ingreso a eliminarOrdenSalida: ");
			ordenSalidaRegistration.remover(selectedOrdenSalida);
			//			for(DetalleOrdenSalida d: listaDetalleOrdenSalida){
			//				detalleOrdenSalidaRegistration.remover(d);
			//			}
			FacesUtil.infoMessage("Orden de Salida Eliminada!", ""+newOrdenSalida.getId());
			initNewOrdenSalida();

		} catch (Exception e) {
			System.out.println("eliminarOrdenSalida() ERROR: "+ e.getMessage());
		}
	}

	public void procesarOrdenSalida(){
		try {
			System.out.println("procesarOrdenSalida()");
			Date fechaActual = new Date();
			//actualizar estado de orden ingreso
			selectedOrdenSalida.setEstado("PR");
			selectedOrdenSalida.setFechaAprobacion(fechaActual);
			DetalleUnidad detalleUnidad = selectedOrdenSalida.getUnidadSolicitante();
			Almacen almacenOrigen = selectedOrdenSalida.getAlmacen();
			double total = 0;
			//actuaizar stock de AlmacenProducto
			listaDetalleOrdenSalida = detalleOrdenSalidaRepository.findAllByOrdenSalida(selectedOrdenSalida);
			for(DetalleOrdenSalida d: listaDetalleOrdenSalida){
				Producto prod = d.getProducto();
				//1.- Actualizar detalle producto (PEPS) y tambien actualizar precio en detalleOrdenIngreso
				if( actualizarDetalleProductoByOrdenSalida(almacenOrigen,d)){
					//mostrar mensaje
					//FacesUtil.showDialog("dlgAlmacenSinExistencias");
					//initNewOrdenSalida();
					//return ; //no se econtro stock disponible
					//2
					fachadaOrdenSalida.actualizarStock(gestionSesion,almacenOrigen,prod,cantidadEntregada);
					//3
					fachadaOrdenSalida.actualizarKardexProducto( detalleUnidad.getNombre(),gestionSesion, selectedOrdenSalida,prod,fechaActual, cantidadEntregada,d.getPrecioUnitario(),usuarioSession);
					total = total + (cantidadEntregada * d.getPrecioUnitario());
				}
			}
			//cactualizar OrdenSalida
			selectedOrdenSalida.setTotalImporte(total);
			ordenSalidaRegistration.updated(selectedOrdenSalida);
			FacesUtil.infoMessage("Orden de Ingreso Procesada!", "");
			initNewOrdenSalida();

		} catch (Exception e) {
			System.out.println("Error : "+e.getMessage());
			FacesUtil.errorMessage("Error al Procesar!");
		}
	}
	//contabiliza la cantidad verdadera que se entrego, aplicado en el metodo actualizarDetalleProductoByOrdenSalida(...);
	private double cantidadEntregada = 0;

	/**
	 * Actualiza el stock, verifica existencias de acuerdo al metodo PEPS
	 * @param almacen De que almacen se sacara los productos
	 * @param detalle
	 * @return true si hay stock, false si no hay existencias
	 */
	public boolean actualizarDetalleProductoByOrdenSalida(Almacen almacen,DetalleOrdenSalida detalle){
		try{
			cantidadEntregada = 0;
			Producto producto = detalle.getProducto();
			double cantidadAux = detalle.getCantidadSolicitada();
			double cantidadSolicitada = detalle.getCantidadSolicitada();//6
			int cantidad = 1;
			//obtener todos los detalles del producto, para poder descontar stock de acuerdo a la cantidad solicitada
			List<DetalleProducto> listDetalleProducto = detalleProductoRepository.findAllByProductoAndAlmacenOrderByFecha(almacen,producto,gestionSesion);
			//5 | 10
			if(listDetalleProducto.size()>0){
				for(DetalleProducto d : listDetalleProducto){
					double stockActual = d.getStockActual();//5 
					double precio = d.getPrecio(); // 50 
					if(cantidadSolicitada > 0){// 6
						double stockFinal = stockActual- cantidadSolicitada; // 5-6=-1 | 10-5=5 |
						double cantidadRestada = stockFinal < 0 ? cantidadSolicitada -(cantidadSolicitada - stockActual) : cantidadSolicitada; //6-(6-5)=5 
						d.setStockActual( stockFinal <= 0 ? 0 : stockFinal); // 0 | 5
						d.setEstado(stockFinal<=0?"IN":"AC"); // IN | AC
						detalleProductoRegistration.updated(d);
						cantidadSolicitada = cantidadSolicitada - cantidadRestada  ;//actualizar cantidad solicitada // 6-5=1
						if(cantidad == 1){
							detalle.setCantidadEntregada(cantidadRestada);
							detalle.setCantidadSolicitada(cantidadAux);
							detalle.setPrecioUnitario(precio);
							detalle.setTotal(precio*cantidadRestada);
							detalleOrdenSalidaRegistration.updated(detalle);
						}else{//nuevo DetalleOrdenSalida con otro precio
							detalle.setId(0);
							detalle.setCantidadEntregada(cantidadRestada );
							detalle.setCantidadSolicitada(cantidadAux);
							detalle.setPrecioUnitario(precio);
							detalle.setTotal(precio*cantidadRestada);
							detalleOrdenSalidaRegistration.register(detalle);
						}
						cantidad = cantidad + 1;
					}
				}
				cantidadEntregada = cantidadAux - cantidadSolicitada;
				return true;
			}
			return false;
		}catch(Exception e){
			System.out.println("actualizarDetalleProductoByOrdenSalida() ERROR: "+e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public void cargarReporte(){
		try {
			urlOrdenSalida = loadURL();
			//RequestContext context = RequestContext.getCurrentInstance();
			//context.execute("PF('dlgVistaPreviaOrdenSalida').show();");

			//initNewOrdenSalida();
			verReport = true;

		} catch (Exception e) {
			FacesUtil.errorMessage("Proceso Incorrecto.");
		}
	}

	public String loadURL(){
		try{
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();  
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
			String urlPDFreporte = urlPath+"ReporteOrdenSalida?pIdOrdenSalida="+selectedOrdenSalida.getId()+"&pUsuario="+usuarioSession+"&pTypeExport=pdf"+"&pNitEmpresa="+empresaLogin.getNIT()+"&pNombreEmpresa="+empresaLogin.getRazonSocial();
			return urlPDFreporte;
		}catch(Exception e){
			return "error";
		}
	}

	// DETALLE ORDEN SALIDA ITEMS

	public void editarDetalleOrdenIngreso(){
		tituloProducto = "Modificar Producto";
		selectedProducto = selectedDetalleOrdenSalida.getProducto();
		verButtonDetalle = true;
		editarOrdenSalida = true;
		calcular();
	}

	public void borrarDetalleOrdenIngreso(){
		listaDetalleOrdenSalida.remove(selectedDetalleOrdenSalida);
		listDetalleOrdenSalidaEliminados.add(selectedDetalleOrdenSalida);
		FacesUtil.resetDataTable("formTableOrdenSalida:itemsTable1");
		verButtonDetalle = true;
	}

	public void limpiarDatosProducto(){
		selectedProducto = new Producto();
		selectedDetalleOrdenSalida = new DetalleOrdenSalida();
		FacesUtil.resetDataTable("formTableOrdenSalida:itemsTable1");
		verButtonDetalle = true;
		editarOrdenSalida = false;
	}

	private String textDialogExistencias = "";


	private double cantidadExistenciasByProductoAlmacen(Almacen almacen,Producto producto){
		double cantidad = 0;
		List<DetalleProducto> listDetalleProducto = detalleProductoRepository.findAllByProductoAndAlmacenOrderByFecha(almacen,producto,gestionSesion);
		for(DetalleProducto detalle:listDetalleProducto){
			cantidad = cantidad + detalle.getStockActual();
		}
		return cantidad;
	}

	public void agregarDetalleOrdenSalida(){
		//verificar que seleccione el almacen
		if(selectedAlmacen.getId()==0){
			FacesUtil.infoMessage("VALIDACION", "Antes debe seleccionar el almacen");
			return;
		}
		if(selectedProducto.getId()==0){
			FacesUtil.infoMessage("VALIDACION", "Seleccione un producto");
			//FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			return;
		}
		//verificar si hay stock del producto
		double cantidad =  cantidadExistenciasByProductoAlmacen(selectedAlmacen,selectedProducto);
		if( cantidad==0 ){ 
			textDialogExistencias = "El almacen "+selectedAlmacen.getNombre()+" no tiene existencias del producto "+selectedProducto.getNombre();
			//ocultar dialgo
			FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			//abrir dialog
			FacesUtil.showDialog("dlgValidacionExistenciasAlmacen");
			return;
		}else if(cantidad < selectedDetalleOrdenSalida.getCantidadSolicitada()){
			textDialogExistencias = "El almacen "+selectedAlmacen.getNombre()+" solo tiene "+cantidad+" existencias del producto "+selectedProducto.getNombre();
			//selectedDetalleOrdenSalida.setCantidadSolicitada(cantidad);
			//ocultar dialgo
			FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			//abrir dialog
			FacesUtil.showDialog("dlgValidacionExistenciasAlmacen");
			return;
		}

		System.out.println("agregarDetalleOrdenIngreso ");
		selectedDetalleOrdenSalida.setProducto(selectedProducto);
		listaDetalleOrdenSalida.add(0, selectedDetalleOrdenSalida);
		selectedProducto = new Producto();
		selectedDetalleOrdenSalida = new DetalleOrdenSalida();
		FacesUtil.resetDataTable("formTableOrdenSalida:itemsTable1");
		verButtonDetalle = true;
		FacesUtil.hideDialog("dlgProducto");//ocultar dialog
	}

	public void modificarDetalleOrdenSalida(){
		//verificar que seleccione el almacen
		if(selectedAlmacen.getId()==0){
			FacesUtil.infoMessage("VALIDACION", "Antes debe seleccionar el almacen");
			return;
		}
		if(selectedProducto.getId()==0){
			FacesUtil.infoMessage("VALIDACION", "Seleccione un producto");
			//FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			return;
		}
		//verificar si hay stock del producto
		double cantidad =  cantidadExistenciasByProductoAlmacen(selectedAlmacen,selectedProducto);
		if( cantidad==0 ){ 
			textDialogExistencias = "El almacen "+selectedAlmacen.getNombre()+" no tiene existencias del producto "+selectedProducto.getNombre();
			//ocultar dialgo
			FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			//abrir dialog
			FacesUtil.showDialog("dlgValidacionExistenciasAlmacen");
			return;
		}else if(cantidad < selectedDetalleOrdenSalida.getCantidadSolicitada()){
			textDialogExistencias = "El almacen "+selectedAlmacen.getNombre()+" solo tiene "+cantidad+" existencias del producto "+selectedProducto.getNombre();
			//selectedDetalleOrdenSalida.setCantidadSolicitada(cantidad);
			//ocultar dialgo
			FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			//abrir dialog
			FacesUtil.showDialog("dlgValidacionExistenciasAlmacen");
			return;
		}

		System.out.println("modificarDetalleOrdenSalida ");
		for(DetalleOrdenSalida d: listaDetalleOrdenSalida){
			if(d.equals(selectedDetalleOrdenSalida)){
				d = selectedDetalleOrdenSalida;
			}
		}
		selectedProducto = new Producto();
		selectedDetalleOrdenSalida = new DetalleOrdenSalida();
		FacesUtil.resetDataTable("formTableOrdenSalida:itemsTable1");
		verButtonDetalle = true;
		editarOrdenSalida = false;
		FacesUtil.hideDialog("dlgProducto");//ocultar dialog
	}

	//calcular totales
	public void calcular(){
		System.out.println("calcular()");
		//double precio = selectedProducto.getPrecioUnitario();
		//double cantidad = selectedDetalleOrdenSalida.getCantidadSolicitada();
		//selectedDetalleOrdenSalida.setTotal(precio * cantidad);
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
		if(selectedDetalleUnidad.getId()==0){
			FacesUtil.infoMessage("VALIDACION", "Antes debe seleccionar Unidad Solicitante");
			return new ArrayList<Funcionario>();
		}
		String upperQuery = query.toUpperCase();
		listaFuncionario = funcionarioRepository.findAllFuncionarioForQueryNombreAndDetalleUnidad(upperQuery,selectedDetalleUnidad,gestionSesion);       
		return listaFuncionario;
	}

	public void onRowSelectFuncionarioClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Funcionario i : listaFuncionario){
			if(i.getNombre().equals(nombre)){
				selectedFuncionario = i;
				return;
			}
		}
	}

	// ONCOMPLETETEXT DETALLE UNIDAD
	public List<DetalleUnidad> completeDetalleUnidad(String query) {
		String upperQuery = query.toUpperCase();
		listDetalleUnidad =  detalleUnidadRepository.findAllDetalleUnidadForQueryNombre(upperQuery);
		return listDetalleUnidad;
	}

	public void onRowSelectDetalleUnidadClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(DetalleUnidad i : listDetalleUnidad){
			if(i.getNombre().equals(nombre)){
				selectedDetalleUnidad = i;
				return;
			}
		}
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
				//verificar si el almacen-gestion ya fue cerrado
				if(cierreGestionAlmacenRepository.finAlmacenGestionCerrado(i,gestionSesion) != null){
					FacesUtil.infoMessage("INFORMACION", "El lmacen "+i.getNombre()+" fué cerrado");
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
		listaProducto =  productoRepository.findAllProductoForQueryNombre(upperQuery);
		return listaProducto;
	}

	public void onRowSelectProductoClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Producto i : listaProducto){
			if(i.getNombre().equals(nombre)){
				selectedProducto = i;
				calcular();
				return;
			}
		}
	}
	
	private  boolean verificarExistencias(Producto producto, double cantidad){
		List<DetalleProducto> listDetalleProducto = detalleProductoRepository.findAllByProductoOrderByFecha(selectedProducto,gestionSesion);
		if(listDetalleProducto.size()>0){

		}
		return true;
	}

	// ONCOMPLETETEXT PROYECTO ..........
	public List<Proyecto> completeProyecto(String query) {
		if(NumberUtil.isNumeric(query)){//si es numero
			listaProyecto = proyectoRepository.findAllProyectoForQueryCodigo(query,gestionSesion);
		}else{//es letra		
			String upperQuery = query.toUpperCase();
			listaProyecto = proyectoRepository.findAllProyectoForQueryNombre(upperQuery,gestionSesion);
		}
		return listaProyecto;
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

	public String getUrlOrdenSalida() {
		return urlOrdenSalida;
	}

	public void setUrlOrdenSalida(String urlOrdenSalida) {
		this.urlOrdenSalida = urlOrdenSalida;
	}

	public boolean isEditarOrdenSalida() {
		return editarOrdenSalida;
	}

	public void setEditarOrdenSalida(boolean editarOrdenSalida) {
		this.editarOrdenSalida = editarOrdenSalida;
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

	public DetalleUnidad getSelectedDetalleUnidad() {
		return selectedDetalleUnidad;
	}

	public void setSelectedDetalleUnidad(DetalleUnidad selectedDetalleUnidad) {
		this.selectedDetalleUnidad = selectedDetalleUnidad;
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

	public List<Producto> getListaProducto() {
		return listaProducto;
	}

	public void setListaProducto(List<Producto> listaProducto) {
		this.listaProducto = listaProducto;
	}

	public boolean isVerReport() {
		return verReport;
	}

	public void setVerReport(boolean verReport) {
		this.verReport = verReport;
	}

	public String getTextDialogExistencias() {
		return textDialogExistencias;
	}

	public void setTextDialogExistencias(String textDialogExistencias) {
		this.textDialogExistencias = textDialogExistencias;
	}

}
