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
import bo.com.qbit.webapp.data.CierreGestionAlmacenRepository;
import bo.com.qbit.webapp.data.DetalleOrdenIngresoRepository;
import bo.com.qbit.webapp.data.DetalleTomaInventarioOrdenIngresoRepository;
import bo.com.qbit.webapp.data.DetalleTomaInventarioRepository;
import bo.com.qbit.webapp.data.OrdenIngresoRepository;
import bo.com.qbit.webapp.data.PartidaRepository;
import bo.com.qbit.webapp.data.ProductoRepository;
import bo.com.qbit.webapp.data.ProveedorRepository;
import bo.com.qbit.webapp.data.TomaInventarioRepository;
import bo.com.qbit.webapp.data.UnidadMedidaRepository;
import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.AlmacenProducto;
import bo.com.qbit.webapp.model.BajaProducto;
import bo.com.qbit.webapp.model.CierreGestionAlmacen;
import bo.com.qbit.webapp.model.DetalleOrdenIngreso;
import bo.com.qbit.webapp.model.DetalleTomaInventario;
import bo.com.qbit.webapp.model.DetalleTomaInventarioOrdenIngreso;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.FachadaOrdenIngreso;
import bo.com.qbit.webapp.model.FachadaOrdenSalida;
import bo.com.qbit.webapp.model.FachadaOrdenTraspaso;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.OrdenIngreso;
import bo.com.qbit.webapp.model.Partida;
import bo.com.qbit.webapp.model.Producto;
import bo.com.qbit.webapp.model.Proveedor;
import bo.com.qbit.webapp.model.TomaInventario;
import bo.com.qbit.webapp.model.UnidadMedida;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.service.BajaProductoRegistration;
import bo.com.qbit.webapp.service.CierreGestionAlmacenRegistration;
import bo.com.qbit.webapp.service.DetalleOrdenIngresoRegistration;
import bo.com.qbit.webapp.service.DetalleTomaInventarioOrdenIngresoRegistration;
import bo.com.qbit.webapp.service.DetalleTomaInventarioRegistration;
import bo.com.qbit.webapp.service.GestionRegistration;
import bo.com.qbit.webapp.service.OrdenIngresoRegistration;
import bo.com.qbit.webapp.service.ProductoRegistration;
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
	private @Inject DetalleTomaInventarioOrdenIngresoRepository detalleTomaInventarioOrdenIngresoRepository;
	private @Inject TomaInventarioRepository tomaInventarioRepository;
	private @Inject ProductoRepository productoRepository;
	private @Inject DetalleOrdenIngresoRepository detalleOrdenIngresoRepository;
	private @Inject UnidadMedidaRepository unidadMedidaRepository;
	private @Inject PartidaRepository partidaRepository;
	private @Inject ProveedorRepository proveedorRepository;
	private @Inject OrdenIngresoRepository ordenIngresoRepository;
	private @Inject CierreGestionAlmacenRepository cierreGestionAlmacenRepository;

	private @Inject TomaInventarioRegistration tomaInventarioRegistration;
	private @Inject DetalleTomaInventarioRegistration detalleTomaInventarioRegistration;
	private @Inject ProductoRegistration productoRegistration;
	private @Inject OrdenIngresoRegistration ordenIngresoRegistration;
	private @Inject DetalleOrdenIngresoRegistration detalleOrdenIngresoRegistration;
	private @Inject GestionRegistration gestionRegistration;
	private @Inject DetalleTomaInventarioOrdenIngresoRegistration detalleTomaInventarioOrdenIngresoRegistration;
	private @Inject BajaProductoRegistration bajaProductoRegistration;
	private @Inject CierreGestionAlmacenRegistration CierreGestionAlmacenRegistration;

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
	private boolean buttonConciliar = false;//mostrar button conciliar
	private boolean conciliar = false;
	private boolean cierreAlmacen = false;
	private boolean buttonEditarTomaInventarioIncial;
	private boolean buttonProcesarTomaInventarioIncial;
	private boolean verButtonDetalle = true;
	private boolean editarOrdenIngreso = false;
	private boolean nuevoProducto = false;
	private boolean editarTomaInventarioIncial = false;//estado, modificar toma de inventario inicial

	//VAR
	private String tituloPanel = "Registrar Almacen";
	private String urlTomaInventario = "";
	private String tipoTomaInventario;

	//OBJECT
	private Almacen selectedAlmacen;
	private TomaInventario newTomaInventario;
	private TomaInventario selectedTomaInventario;

	//LIST
	private List<Usuario> listUsuario = new ArrayList<Usuario>();
	private List<Almacen> listaAlmacen = new ArrayList<Almacen>();
	private List<Proveedor> listaProveedor = new ArrayList<Proveedor>();
	private List<DetalleTomaInventario> listDetalleTomaInventario = new ArrayList<DetalleTomaInventario>();
	private List<DetalleTomaInventario> listDetalleTomaInventarioEliminadas = new ArrayList<DetalleTomaInventario>();
	private List<TomaInventario> listTomaInventario = new ArrayList<TomaInventario>();
	private List<AlmacenProducto> listAlmacenProducto = new ArrayList<AlmacenProducto>();
	private List<String> listTipo = new ArrayList<String>();//INICIAL,PARCIAL,FINAL
	private List<DetalleTomaInventario> listSelectedDetalleTomaInventario = new ArrayList<DetalleTomaInventario>();

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;
	private Gestion gestionSesion;
	private Empresa empresaLogin;

	//PRODUTO
	private Producto newProducto ;
	private Producto selectedProducto;
	private UnidadMedida selectedUnidadMedida;
	private DetalleOrdenIngreso selectedDetalleOrdenIngreso;
	private List<UnidadMedida> listUnidadMedida = new ArrayList<UnidadMedida>();
	private List<Partida> listPartida = new ArrayList<Partida>();
	private List<DetalleOrdenIngreso> listaDetalleOrdenIngreso = new ArrayList<DetalleOrdenIngreso>(); // ITEMS
	private List<DetalleOrdenIngreso> listDetalleOrdenIngresoEliminados = new ArrayList<DetalleOrdenIngreso>();
	private String tituloProducto = "Agregar Producto";
	private List<Proveedor> listProveedor = new ArrayList<Proveedor>();
	private Proveedor selectedProveedor;
	//ORDEN INGRESO
	private OrdenIngreso newOrdenIngreso;

	//FACADE
	private @Inject FachadaOrdenIngreso fachadaOrdenIngreso;
	private @Inject FachadaOrdenSalida fachadaOrdenSalida;
	private @Inject FachadaOrdenTraspaso fachadaOrdenTraspaso;

	@PostConstruct
	public void initNewTomaInventario() {

		System.out.println(" ... initNewTomaInventario ...");

		usuarioSession = sessionMain.getUsuarioLogin().getLogin();
		gestionSesion = sessionMain.getGestionLogin();
		empresaLogin = sessionMain.getEmpresaLogin();

		// tituloPanel
		tituloPanel = "Registrar Toma Inventario";

		crear = true;
		verProcesar = true;
		verReport = false;
		revisarReport = false;
		verGuardar = false;
		verButtonReport = false;
		conciliar = false;
		buttonConciliar = false;
		setCierreAlmacen(false);
		editarTomaInventarioIncial = false;
		buttonProcesarTomaInventarioIncial = false;
		buttonEditarTomaInventarioIncial = false;

		tipoTomaInventario = "PARCIAL";

		//---
		verLista = true;
		modificar = false;
		registrar = false;

		listTomaInventario = tomaInventarioRepository.findAllOrderedByIDGestion(gestionSesion);
		listDetalleTomaInventario = new ArrayList<DetalleTomaInventario>();
		listaAlmacen = almacenRepository.findAllOrderedByID();
		listTipo  = new ArrayList<String>();

		selectedTomaInventario = null;

		newTomaInventario = new TomaInventario();
		//newTomaInventario.setCorrelativo(cargarCorrelativo(listaOrdenIngreso.size()+1));
		newTomaInventario.setEstado("AC");
		newTomaInventario.setFecha(new Date());
		newTomaInventario.setFechaRegistro(new Date());
		newTomaInventario.setGestion(gestionSesion);
		newTomaInventario.setUsuarioRegistro(usuarioSession);

		//PROUDCTO
		nuevoProducto = false;
		selectedDetalleOrdenIngreso = new DetalleOrdenIngreso();
		selectedProducto= new Producto();
		newProducto= new Producto();
		listaDetalleOrdenIngreso = new ArrayList<DetalleOrdenIngreso>();
		selectedUnidadMedida = new UnidadMedida();
		selectedProveedor = new Proveedor();
		verButtonDetalle = true;
		selectedAlmacen = new Almacen();
		//ORDEN INGRESO
		newOrdenIngreso = new OrdenIngreso();

		//Verifica la gestion , sobre el levantamiento si ya se hizo el inicial
		if(gestionSesion.isIniciada()){
			newTomaInventario.setTipo("PARCIAL");
			listTipo.add("PARCIAL");
			listTipo.add("FINAL");
		}else{
			newTomaInventario.setTipo("INICIAL");
			listTipo.add("INICIAL");
			//listTipo.add("PARCIAL");//test , luego eliminar esta linea
		}

	}

	public void cambiarAspecto(){
		//verificar si ya hay una tomma de inventario inicial en esa gestion
		if(existeTomaInventarioIncialByGestion()){
			FacesUtil.infoMessage("VALIDACION", "Ya se registro una toma Inventario Inicial, gestion "+gestionSesion.getGestion());
			return;
		}
		verLista = false;
		modificar = false;
		registrar = true;
		crear = false;
	}
	private boolean existeTomaInventarioIncialByGestion(){
		return tomaInventarioRepository.findTIInicialByGestion(gestionSesion);
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

	public void procesarTomaInventarioInicial(){
		// DetalleTomaInventarioOrdenIngreso
		DetalleTomaInventarioOrdenIngreso detalle = detalleTomaInventarioOrdenIngresoRepository.findByTomaInventario(selectedTomaInventario);
		//almacen
		selectedAlmacen = selectedTomaInventario.getAlmacen();
		//orden ingreso
		newOrdenIngreso = detalle.getOrdenIngreso();
		//proveedor
		//selectedProveedor = newOrdenIngreso.getProveedor();
		//detalle orden ingreso
		//listaDetalleOrdenIngreso = detalleOrdenIngresoRepository.findAllByOrdenIngreso(newOrdenIngreso);
		Date fechaActual = new Date();
		try{
			//si es de Tipo INICIAL
			if(selectedTomaInventario.getTipo().equals("INICIAL")){
				//Actualizar estado de gestion
				gestionSesion.setIniciada(true);
				sessionMain.getGestionLogin().setIniciada(true);//actualaizar la gestion en la session
				gestionRegistration.update(gestionSesion);
				//				DetalleTomaInventarioOrdenIngreso detalle = new DetalleTomaInventarioOrdenIngreso();
				//				detalle.setEstado("AC");
				//				detalle.setFechaRegistro(fechaActual);
				//				detalle.setOrdenIngreso(newOrdenIngreso);
				//				detalle.setTomaInventario(selectedTomaInventario);
				//				detalle.setUsuarioRegistro(usuarioSession);
				detalle.setEstado("AC");
				detalleTomaInventarioOrdenIngresoRegistration.updated(detalle);
				//procesar OrdenIngreso
				procesarOrdenIngreso();
				//actualizar estado
				selectedTomaInventario.setFechaRevision(fechaActual);
				selectedTomaInventario.setEstado("PR");
				selectedTomaInventario.setEstadoRevision("SI");
				tomaInventarioRegistration.updated(selectedTomaInventario);
				FacesUtil.infoMessage("Toma Inventario Registrada!", "");
				initNewTomaInventario();
			}
			initNewTomaInventario();
		}catch(Exception e){
			System.out.println("procesarTomaInventarioInicial() Error "+e.getMessage());
		}

	}

	public void registrarTomaInventario() {
		//validaciones
		if(selectedProveedor.getId()==0 && newTomaInventario.getTipo().equals("INICIAL")){
			FacesUtil.infoMessage("VALIDACION", "Seleccione un proveedor");
			return;
		}
		if( selectedAlmacen.getId()==0 || newTomaInventario.getNombreInventariador().isEmpty() || newTomaInventario.getNombreResponsable().isEmpty() || newTomaInventario.getHoja().isEmpty()){
			FacesUtil.infoMessage("VALIDACION", "No pueden haber campos vacios");
			return;
		}
		if(newTomaInventario.getTipo().equals("INICIAL") && listaDetalleOrdenIngreso.size()==0){
			FacesUtil.infoMessage("VALIDACION", "Debe Agregar items..");
			return;
		}else if(! newTomaInventario.getTipo().equals("INICIAL") && listDetalleTomaInventario.size()==0){
			FacesUtil.infoMessage("VALIDACION", "Debe Agregar items.");
			return;
		}
		try {
			Date fechaActual = new Date();
			newTomaInventario.setAlmacen(selectedAlmacen);
			newTomaInventario.setEstado("AC");
			newTomaInventario.setFechaRegistro(fechaActual);
			newTomaInventario = tomaInventarioRegistration.register(newTomaInventario);
			System.out.println("newTomaInventario:"+newTomaInventario.getId());
			//			for(DetalleTomaInventario detalle : listDetalleTomaInventario){
			//				detalle.setTomaInventario(newTomaInventario);
			//				detalle.setFechaRegistro(fechaActual);
			//				detalle.setUsuarioRegistro(usuarioSession);
			//				detalleTomaInventarioRegistration.register(detalle);
			//			}
			//si es de Tipo INICIAL
			if(newTomaInventario.getTipo().equals("INICIAL")){
				//registrar ordenIngreso
				registrarOrdenIngreso();
				DetalleTomaInventarioOrdenIngreso detalle = new DetalleTomaInventarioOrdenIngreso();
				detalle.setEstado("IN");
				detalle.setFechaRegistro(fechaActual);
				detalle.setOrdenIngreso(newOrdenIngreso);
				detalle.setTomaInventario(newTomaInventario);
				detalle.setUsuarioRegistro(usuarioSession);
				detalleTomaInventarioOrdenIngresoRegistration.register(detalle);
			}
			FacesUtil.infoMessage("Toma Inventario Registrada!", "");
			initNewTomaInventario();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : "+e.getMessage());
			FacesUtil.errorMessage("Error al Registrar.");
		}
	}

	public void modificarTomaInventario() {
		try {
			System.out.println("Ingreso a modificarTomaInvenario: ");
			Date fechaActual = new Date();
			newTomaInventario.setAlmacen(selectedAlmacen);
			newTomaInventario.setEstado("AC");
			tomaInventarioRegistration.updated(newTomaInventario);
			//modificar detalle orden ingreso(Solo para inicial)
			for(DetalleOrdenIngreso detalleOI1 : listaDetalleOrdenIngreso){
				System.out.println("detalleOI1 id:"+detalleOI1.getId());
				if(detalleOI1.getId()==0){
					detalleOI1.setEstado("AC");
					detalleOI1.setFechaRegistro(fechaActual);
					detalleOI1.setOrdenIngreso(newOrdenIngreso);
					detalleOI1.setUsuarioRegistro(usuarioSession);
					detalleOrdenIngresoRegistration.register(detalleOI1);
					//nuevo DetalleTomaInventario
					DetalleTomaInventario detalle = new DetalleTomaInventario();
					detalle.setCantidad((int)detalleOI1.getCantidad());
					detalle.setCantidadRegistrada(detalleOI1.getCantidad());
					detalle.setCantidadVerificada(detalleOI1.getCantidad());
					detalle.setDiferencia(0d);
					detalle.setEstado("AC");
					detalle.setFechaRegistro(fechaActual);
					detalle.setProducto(detalleOI1.getProducto());
					detalle.setTomaInventario(newTomaInventario);
					detalle.setUsuarioRegistro(usuarioSession);
					detalle = detalleTomaInventarioRegistration.register(detalle);
					listDetalleTomaInventario.add(detalle);
				}else{
					detalleOrdenIngresoRegistration.updated(detalleOI1);
					DetalleTomaInventario detalleAux = obtenerDetalleTomaInventarioByProducto( detalleOI1.getProducto());
					detalleAux.setCantidad((int)detalleOI1.getCantidad());
					detalleAux.setCantidadRegistrada(detalleOI1.getCantidad());
					detalleAux.setCantidadVerificada(detalleOI1.getCantidad());
					detalleAux.setProducto(detalleOI1.getProducto());
					detalleTomaInventarioRegistration.updated(detalleAux);
				}
			}
			for(DetalleOrdenIngreso detalleOI2: listDetalleOrdenIngresoEliminados){
				if(detalleOI2.getId()!=0){
					detalleOI2.setEstado("RM");
					detalleOrdenIngresoRegistration.updated(detalleOI2);
					//actualizar DetalleTomaInventario
					DetalleTomaInventario detalleAux = obtenerDetalleTomaInventarioByProducto( detalleOI2.getProducto());
					detalleAux.setEstado("RM");
					detalleTomaInventarioRegistration.updated(detalleAux);
				}
			}
			FacesUtil.infoMessage("Orden de Ingreso Modificada!", "");
			initNewTomaInventario();
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println("Error "+e.getMessage());
			FacesUtil.errorMessage("Error al Modificar.");
		}
	}

	private DetalleTomaInventario obtenerDetalleTomaInventarioByProducto(Producto producto){
		for(DetalleTomaInventario detalle : listDetalleTomaInventario){
			if(detalle.getProducto().equals(producto)){
				return detalle;
			}
		}
		return null;
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

	//cierre de almacen por gestion
	public void cerrarAlmacenPorGestion(){
		//validaciones
		if(selectedAlmacen.getId() == 0){
			FacesUtil.infoMessage("VALIDACION", "Seleccione un Almacém");
			return;
		}
		if(  newTomaInventario.getNombreInventariador().isEmpty() ||  newTomaInventario.getNombreResponsable().isEmpty() || newTomaInventario.getHoja().isEmpty()){
			FacesUtil.infoMessage("VALIDACION", "No pueden haber campos vacios");
			return;
		}

		try{
			System.out.println("cerrarAlmacenPorGestion()");
			//1, registrar  toma invenario con estado= CE
			Date fechaActual = new Date();
			newTomaInventario.setAlmacen(selectedAlmacen);
			newTomaInventario.setEstado("CE");
			newTomaInventario.setFechaRegistro(fechaActual);
			newTomaInventario = tomaInventarioRegistration.register(newTomaInventario);
			for(DetalleTomaInventario detalle : listDetalleTomaInventario){
				double diferencia = 0;
				String observacion = "CIERRE";
				double registrada = detalle.getCantidadRegistrada();
				detalle.setCantidadVerificada(registrada);
				detalle.setDiferencia(diferencia);
				detalle.setObservacion(observacion);
				detalle.setTomaInventario(newTomaInventario);
				detalle.setFechaRegistro(fechaActual);
				detalle.setUsuarioRegistro(usuarioSession);
				detalleTomaInventarioRegistration.register(detalle);
			}
			//2. registrar objeto de cierre
			CierreGestionAlmacen cierre = new CierreGestionAlmacen();
			cierre.setEstado("AC");
			cierre.setFechaRegistro(fechaActual);
			cierre.setUsuarioRegistro(usuarioSession);
			cierre.setGestion(gestionSesion);
			cierre.setAlmacen(newTomaInventario.getAlmacen());
			cierre.setTomaInventario(newTomaInventario);
			cierre = CierreGestionAlmacenRegistration.register(cierre);
			FacesUtil.infoMessage("CIERRE DE ALMACEN CORRECTO", newTomaInventario.getAlmacen()+" CERRADO - GESTION "+gestionSesion.getGestion());
			initNewTomaInventario();
		}catch(Exception e){
			System.out.println("cerrarAlmacenPorGestion ERROR: "+e.getMessage());
		}
	}

	//Agregar producto
	public void registrarOrdenIngreso() {
		try {
			Date fechaActual = new Date();
			calcularTotal();
			System.out.println("Ingreso a registrarOrdenIngreso: ");
			int numeroCorrelativo = ordenIngresoRepository.obtenerNumeroOrdenIngreso(new Date(),gestionSesion);
			System.out.println("numeroCorrelativo="+numeroCorrelativo);
			newOrdenIngreso.setMotivoIngreso("TOMA INVENTARIO INICIAL");
			newOrdenIngreso.setTipoDocumento("SIN DOCUMENTO");
			newOrdenIngreso.setNumeroDocumento("0");
			newOrdenIngreso.setFechaDocumento(fechaActual);
			newOrdenIngreso.setObservacion("Generado a partir de la Toma de Inventario Numero:"+newTomaInventario.getId()+", Inventario Inicial");
			newOrdenIngreso.setCorrelativo(cargarCorrelativo(numeroCorrelativo));
			newOrdenIngreso.setGestion(gestionSesion);
			newOrdenIngreso.setFechaDocumento(fechaActual);
			newOrdenIngreso.setUsuarioRegistro(usuarioSession);
			newOrdenIngreso.setEstado("IN");
			newOrdenIngreso.setFechaRegistro(fechaActual);
			newOrdenIngreso.setAlmacen(selectedAlmacen);
			newOrdenIngreso.setProveedor(selectedProveedor);
			newOrdenIngreso = ordenIngresoRegistration.register(newOrdenIngreso);
			System.out.println("newOrdenIngreso: "+newOrdenIngreso.getId());
			for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){

				d.setFechaRegistro(fechaActual);
				d.setUsuarioRegistro(usuarioSession);
				d.setOrdenIngreso(newOrdenIngreso);
				d= detalleOrdenIngresoRegistration.register(d);
				//Registrar detalle toma inventario
				DetalleTomaInventario detalle = new DetalleTomaInventario();
				detalle.setCantidadRegistrada(d.getCantidad());
				detalle.setCantidadVerificada(d.getCantidad());
				detalle.setDiferencia(0d);
				detalle.setEstado("AC");
				detalle.setFechaRegistro(fechaActual);
				detalle.setObservacion("Ninguna");
				detalle.setProducto(d.getProducto());
				detalle.setTomaInventario(newTomaInventario);
				detalle.setUsuarioRegistro(usuarioSession);
				detalleTomaInventarioRegistration.register(detalle);
			}
		} catch (Exception e) {
		}
	}

	public void calcularTotal(){
		double totalImporte = 0;
		for(DetalleOrdenIngreso d : listaDetalleOrdenIngreso){
			totalImporte =totalImporte + d.getTotal();
		}
		newOrdenIngreso.setTotalImporte(totalImporte);
	}

	private void procesarOrdenIngreso(){
		try {
			Date fechaActual = new Date();
			// actualizar estado de orden ingreso
			newOrdenIngreso.setEstado("PR");
			newOrdenIngreso.setFechaAprobacion(fechaActual);
			ordenIngresoRegistration.updated(newOrdenIngreso);

			Proveedor proveedor = newOrdenIngreso.getProveedor();
			// actuaizar stock de AlmacenProducto
			listaDetalleOrdenIngreso = detalleOrdenIngresoRepository.findAllByOrdenIngreso(newOrdenIngreso);
			for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){
				Producto prod = d.getProducto();
				//actualiza el esstock por producto almacen(teniendo en cuenta la agrupacion de productos)
				fachadaOrdenIngreso.actualizarStock(newOrdenIngreso.getAlmacen(),proveedor,prod, d.getCantidad(),fechaActual,d.getPrecioUnitario(),usuarioSession);
				//registra la transaccion de entrada del producto
				fachadaOrdenIngreso.actualizarKardexProducto(newOrdenIngreso.getCorrelativo(),newOrdenIngreso.getAlmacen(),gestionSesion, prod,fechaActual, d.getCantidad(),d.getPrecioUnitario(),usuarioSession);
				//registra los stock de los producto , para luego utilizar PEPS en ordenes de traspaso y salida
				fachadaOrdenIngreso.cargarDetalleProducto(fechaActual,newOrdenIngreso.getAlmacen(),d.getProducto(), d.getCantidad(), d.getPrecioUnitario(), d.getFechaRegistro(), newOrdenIngreso.getCorrelativo(),usuarioSession);
			}

		} catch (Exception e) {
		}
	}

	public void procesarConsulta(){
		try {
			if(selectedAlmacen.getId()==0){
				FacesUtil.infoMessage("INFORMACION", "Seleccione un almacen ");
				return;
			}
			//verificar si el almacen-gestion ya fue cerrado
			if(cierreGestionAlmacenRepository.finAlmacenGestionCerrado(selectedAlmacen,gestionSesion) != null){
				FacesUtil.infoMessage("INFORMACION", "El lmacen "+selectedAlmacen.getNombre()+" fué cerrado");
				return ;
			}
			listAlmacenProducto = almacenProductoRepository.findByAlmacen(selectedAlmacen);
			if(listAlmacenProducto.size()==0){//validacion de almacen
				FacesUtil.infoMessage("INFORMACION", "No se encontraron existencias en el almacen "+selectedAlmacen.getNombre());
				return ;
			}
			listDetalleTomaInventario = new ArrayList<DetalleTomaInventario>();
			for(AlmacenProducto ap : listAlmacenProducto){
				DetalleTomaInventario detalle = new DetalleTomaInventario();
				detalle.setProducto(ap.getProducto());
				detalle.setCantidadRegistrada(ap.getStock());
				listDetalleTomaInventario.add(detalle);
			}
			if(newTomaInventario.getTipo().equals("FINAL")){
				cierreAlmacen = true;
			}else{
				cierreAlmacen = false;
				verGuardar = listDetalleTomaInventario.size()>0?true:false;
			}
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Procesar!");
		}
	}

	public void buttonConciliarTomaInventario(){
		System.out.println("buttonConciliarTomaInventario()");
		try {
			newTomaInventario = selectedTomaInventario;
			selectedAlmacen = selectedTomaInventario.getAlmacen();
			listDetalleTomaInventario = new ArrayList<DetalleTomaInventario>();
			listDetalleTomaInventario = detalleTomaInventarioRepository.findAllByTomaInventario(selectedTomaInventario);

			verLista = false;
			modificar = false;
			registrar = false;
			verButtonReport = false;
			buttonConciliar = false;
			conciliar = true;
		} catch (Exception e) {
			System.out.println("ERROR "+e.getMessage());
		}
	}

	public void conciliarTomaInventario(){
		try{
			if(listSelectedDetalleTomaInventario.size()>0){
				Date fechaActual = new Date();
				selectedTomaInventario.setEstado("CN");//CONCILIADO
				tomaInventarioRegistration.updated(selectedTomaInventario);
				BajaProducto baja = new BajaProducto();
				Almacen almacen = selectedTomaInventario.getAlmacen();
				System.out.println("conciliarTomaInventario() size:"+listSelectedDetalleTomaInventario.size());
				for(DetalleTomaInventario d: listSelectedDetalleTomaInventario){
					System.out.println("d :"+d.getProducto().getNombre());
					baja = new BajaProducto();
					baja.setDetalleTomaInventario(d);
					baja.setEstado("AC");
					baja.setFechaRegistro(fechaActual);
					baja.setObservacion(d.getObservacion());
					baja.setStockActual(d.getCantidadVerificada());
					baja.setStockAnterior(d.getCantidadRegistrada());
					baja.setUsuarioRegistro(usuarioSession);

					//actualizar en DetalleProducto
					if(d.getCantidadRegistrada() - d.getCantidadVerificada() > 0){//si faltaron
						//actualizar en AlmacenProducto
						fachadaOrdenTraspaso.actualizarStock(almacen,d);//-1 para que no actualize el precio
						fachadaOrdenSalida.actualizarDetalleProducto(selectedTomaInventario.getAlmacen(), d.getProducto(), d.getDiferencia());
						//actualizar en kardex(NOSE) como una salida (como baja de producto)
						//fachadaOrdenSalida.actualizarKardexProducto("Por Baja de Producto", gestionSesion, selectedOrdenSalida, prod, fechaActual, cantidad, precioUnitario, usuarioSession);
						bajaProductoRegistration.register(baja);

					}
				}
				FacesUtil.infoMessage("INFORMACION", "Toma Inventario "+selectedTomaInventario.getId()+" Conciliada.");
				initNewTomaInventario();
			}else{
				FacesUtil.infoMessage("INFORMACION", "Seleccione los items para conciliarlos");
			}
		}catch(Exception e){
			System.out.println("ERROR "+e.getMessage());
		}

	}

	public void buttonRevisar(){
		try {
			newTomaInventario = selectedTomaInventario;
			selectedAlmacen = selectedTomaInventario.getAlmacen();
			listDetalleTomaInventario = new ArrayList<DetalleTomaInventario>();
			listDetalleTomaInventario = detalleTomaInventarioRepository.findAllByTomaInventario(selectedTomaInventario);

			verLista = false;
			modificar = true;
			registrar = false;
			verButtonReport = false;
			if(selectedTomaInventario.getTipo().equals("INICIAL")){
				cargarSelectedTomaInventario();
			}
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
			String urlPDFreporte = urlPath+"ReporteTomaInventario?pIdTomaInventario="+selectedTomaInventario.getId()+"&pUsuario="+usuarioSession+"&pNitEmpresa="+empresaLogin.getNIT()+"&pNombreEmpresa="+empresaLogin.getRazonSocial();
			return urlPDFreporte;
		}catch(Exception e){
			return "error";
		}
	}

	public void cargarSelectedTomaInventario(){
		// DetalleTomaInventarioOrdenIngreso
		DetalleTomaInventarioOrdenIngreso detalle = detalleTomaInventarioOrdenIngresoRepository.findByTomaInventario(selectedTomaInventario);
		//almacen
		selectedAlmacen = selectedTomaInventario.getAlmacen();
		//orden ingreso
		newOrdenIngreso = detalle.getOrdenIngreso();
		//proveedor
		selectedProveedor = newOrdenIngreso.getProveedor();
		//detalle orden ingreso
		listaDetalleOrdenIngreso = detalleOrdenIngresoRepository.findAllByOrdenIngreso(newOrdenIngreso);

		//detalle toma inventario
		editarTomaInventarioIncial = true;
		verLista = false;
		modificar = false;
		registrar = true;
		verButtonReport = false;
		newTomaInventario = selectedTomaInventario;
		// ocultar botones
		buttonEditarTomaInventarioIncial = false;
		buttonProcesarTomaInventarioIncial = false;
		
		//
		listDetalleTomaInventario = detalleTomaInventarioRepository.findAllByTomaInventario(selectedTomaInventario);
	}

	public void onRowSelectTomaInventarioClick(SelectEvent event){
		verButtonReport = true;
		crear = false;
		if(selectedTomaInventario.getTipo().equals("FINAL") ){
			buttonConciliar = false;
			revisarReport = false;
			return;			
		}
		if(selectedTomaInventario.getTipo().equals("INICIAL")){
			if(selectedTomaInventario.getEstado().equals("AC")){
				System.out.println("TomaInventario Incial - Modificar - Procesar");
				buttonEditarTomaInventarioIncial = true;
				buttonProcesarTomaInventarioIncial = true;
			}else{
				buttonEditarTomaInventarioIncial = false;
				buttonProcesarTomaInventarioIncial = false;
			}

			return;
		}
		if(selectedTomaInventario.getEstadoRevision().equals("NO")){
			revisarReport = true;
			buttonConciliar = false;
		}else{
			revisarReport = false;
			if(selectedTomaInventario.getEstado().equals("CN")){
				buttonConciliar = false;
			}else{
				buttonConciliar = true;
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
				selectedAlmacen = i;
				newTomaInventario.setNombreResponsable(selectedAlmacen.getEncargado().getNombre());
				return;
			}
		}
	}

	//PRODUCTO
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
			setSelectedProducto(newProducto);
			calcular();
			FacesUtil.infoMessage("Producto Registrado!",newProducto.getNombre());
			setNuevoProducto(false);
			newProducto = new Producto();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al registrar");
		}
	}

	//calcular totales
	public void calcular(){
		System.out.println("calcular()");
		double precio = selectedDetalleOrdenIngreso.getPrecioUnitario();
		double cantidad = selectedDetalleOrdenIngreso.getCantidad();
		selectedDetalleOrdenIngreso.setTotal(precio * cantidad);
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
				newProducto.setUnidadMedidas(selectedUnidadMedida);
			}
		}

	}

	// SELECCIONAR AUTOCOMPLETES AREA PRODUCTO
	public List<Partida> completePartida(String query) {
		String upperQuery = query.toUpperCase();
		listPartida =  partidaRepository.findAllPartidaForDescription(upperQuery);
		return listPartida;
	}

	public void onRowSelectPartidaClick() {
		System.out.println("Seleccionado onRowSelectPartidaClick: "
				+ this.newProducto.getPartida().getNombre());
		for (Partida row : listPartida) {
			if (row.getNombre().equals(this.newProducto.getPartida().getNombre())) {
				this.newProducto.setPartida(row);
			}
		}
	}
	private List<Producto> listProducto = new ArrayList<Producto>();
	// ONCOMPLETETEXT PRODUCTO
	public List<Producto> completeProducto(String query) {
		String upperQuery = query.toUpperCase();
		listProducto =  productoRepository.findAllProductoForQueryNombre(upperQuery);
		return listProducto;
	}

	public void onRowSelectProductoClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Producto i : listProducto){
			if(i.getNombre().toUpperCase().equals(nombre.toUpperCase())){
				selectedProducto = i;
				calcular();
				return;
			}
		}
	}

	// ONCOMPLETETEXT PROVEEDOR
	public List<Proveedor> completeProveedor(String query) {
		String upperQuery = query.toUpperCase();
		listProveedor = proveedorRepository.findAllProveedorForQueryNombre(upperQuery);
		return listProveedor;
	}

	public void onRowSelectProveedorClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Proveedor i : listProveedor){
			if(i.getNombre().equals(nombre)){
				setSelectedProveedor(i);
				newOrdenIngreso.setProveedor(selectedProveedor);
				return;
			}
		}
	}

	// DETALLE ORDEN INGRESO ITEMS
	public void editarDetalleOrdenIngreso(){
		setTituloProducto("Modificar Producto");
		selectedProducto = selectedDetalleOrdenIngreso.getProducto();
		setVerButtonDetalle(true);
		setEditarOrdenIngreso(true);
		calcular();
	}

	public void borrarDetalleOrdenIngreso(){
		listaDetalleOrdenIngreso.remove(selectedDetalleOrdenIngreso);
		listDetalleOrdenIngresoEliminados.add(selectedDetalleOrdenIngreso);
		FacesUtil.resetDataTable("formTableTomaInventario:itemsTable1");
		setVerButtonDetalle(true);
	}

	public void limpiarDatosProducto(){
		selectedProducto = new Producto();
		selectedDetalleOrdenIngreso = new DetalleOrdenIngreso();
		FacesUtil.resetDataTable("formTableTomaInventario:itemsTable1");
		setVerButtonDetalle(true);
		setEditarOrdenIngreso(false);
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
		FacesUtil.resetDataTable("formTableTomaInventario:itemsTable1");
		setVerButtonDetalle(true);
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
		FacesUtil.resetDataTable("formTableTomaInventario:itemsTable1");
		setVerButtonDetalle(true);
		setEditarOrdenIngreso(false);
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

	public String getTipoTomaInventario() {
		return tipoTomaInventario;
	}

	public void setTipoTomaInventario(String tipoTomaInventario) {
		this.tipoTomaInventario = tipoTomaInventario;
	}

	public boolean isNuevoProducto() {
		return nuevoProducto;
	}

	public void setNuevoProducto(boolean nuevoProducto) {
		this.nuevoProducto = nuevoProducto;
	}

	public UnidadMedida getSelectedUnidadMedida() {
		return selectedUnidadMedida;
	}

	public void setSelectedUnidadMedida(UnidadMedida selectedUnidadMedida) {
		this.selectedUnidadMedida = selectedUnidadMedida;
	}

	public Producto getSelectedProducto() {
		return selectedProducto;
	}

	public void setSelectedProducto(Producto selectedProducto) {
		this.selectedProducto = selectedProducto;
	}

	public DetalleOrdenIngreso getSelectedDetalleOrdenIngreso() {
		return selectedDetalleOrdenIngreso;
	}

	public void setSelectedDetalleOrdenIngreso(
			DetalleOrdenIngreso selectedDetalleOrdenIngreso) {
		this.selectedDetalleOrdenIngreso = selectedDetalleOrdenIngreso;
	}

	public List<Partida> getListPartida() {
		return listPartida;
	}

	public void setListPartida(List<Partida> listPartida) {
		this.listPartida = listPartida;
	}

	public List<Producto> getListProducto() {
		return listProducto;
	}

	public void setListProducto(List<Producto> listProducto) {
		this.listProducto = listProducto;
	}

	public List<DetalleOrdenIngreso> getListaDetalleOrdenIngreso() {
		return listaDetalleOrdenIngreso;
	}

	public void setListaDetalleOrdenIngreso(List<DetalleOrdenIngreso> listaDetalleOrdenIngreso) {
		this.listaDetalleOrdenIngreso = listaDetalleOrdenIngreso;
	}

	public String getTituloProducto() {
		return tituloProducto;
	}

	public void setTituloProducto(String tituloProducto) {
		this.tituloProducto = tituloProducto;
	}

	public boolean isVerButtonDetalle() {
		return verButtonDetalle;
	}

	public void setVerButtonDetalle(boolean verButtonDetalle) {
		this.verButtonDetalle = verButtonDetalle;
	}

	public boolean isEditarOrdenIngreso() {
		return editarOrdenIngreso;
	}

	public void setEditarOrdenIngreso(boolean editarOrdenIngreso) {
		this.editarOrdenIngreso = editarOrdenIngreso;
	}

	public Producto getNewProducto() {
		return newProducto;
	}

	public void setNewProducto(Producto newProducto) {
		this.newProducto = newProducto;
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

	public OrdenIngreso getNewOrdenIngreso() {
		return newOrdenIngreso;
	}

	public void setNewOrdenIngreso(OrdenIngreso newOrdenIngreso) {
		this.newOrdenIngreso = newOrdenIngreso;
	}

	public Gestion getGestionSesion() {
		return gestionSesion;
	}

	public void setGestionSesion(Gestion gestionSesion) {
		this.gestionSesion = gestionSesion;
	}

	public List<String> getListTipo() {
		return listTipo;
	}

	public void setListTipo(List<String> listTipo) {
		this.listTipo = listTipo;
	}

	public boolean isConciliar() {
		return conciliar;
	}

	public void setConciliar(boolean conciliar) {
		this.conciliar = conciliar;
	}

	public boolean isButtonConciliar() {
		return buttonConciliar;
	}

	public void setButtonConciliar(boolean buttonConciliar) {
		this.buttonConciliar = buttonConciliar;
	}

	public List<DetalleTomaInventario> getListSelectedDetalleTomaInventario() {
		return listSelectedDetalleTomaInventario;
	}

	public void setListSelectedDetalleTomaInventario(
			List<DetalleTomaInventario> listSelectedDetalleTomaInventario) {
		this.listSelectedDetalleTomaInventario = listSelectedDetalleTomaInventario;
	}

	public boolean isCierreAlmacen() {
		return cierreAlmacen;
	}

	public void setCierreAlmacen(boolean cierreAlmacen) {
		this.cierreAlmacen = cierreAlmacen;
	}

	public boolean isButtonEditarTomaInventarioIncial() {
		return buttonEditarTomaInventarioIncial;
	}

	public void setButtonEditarTomaInventarioIncial(
			boolean buttonEditarTomaInventarioIncial) {
		this.buttonEditarTomaInventarioIncial = buttonEditarTomaInventarioIncial;
	}

	public boolean isButtonProcesarTomaInventarioIncial() {
		return buttonProcesarTomaInventarioIncial;
	}

	public void setButtonProcesarTomaInventarioIncial(
			boolean buttonProcesarTomaInventarioIncial) {
		this.buttonProcesarTomaInventarioIncial = buttonProcesarTomaInventarioIncial;
	}

	public List<DetalleTomaInventario> getListDetalleTomaInventarioEliminadas() {
		return listDetalleTomaInventarioEliminadas;
	}

	public void setListDetalleTomaInventarioEliminadas(
			List<DetalleTomaInventario> listDetalleTomaInventarioEliminadas) {
		this.listDetalleTomaInventarioEliminadas = listDetalleTomaInventarioEliminadas;
	}

	public boolean isEditarTomaInventarioIncial() {
		return editarTomaInventarioIncial;
	}

	public void setEditarTomaInventarioIncial(boolean editarTomaInventarioIncial) {
		this.editarTomaInventarioIncial = editarTomaInventarioIncial;
	}

}
