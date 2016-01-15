package bo.com.qbit.webapp.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.AlmacenProductoRepository;
import bo.com.qbit.webapp.data.AlmacenRepository;
import bo.com.qbit.webapp.data.CierreGestionAlmacenRepository;
import bo.com.qbit.webapp.data.DetalleOrdenTraspasoRepository;
import bo.com.qbit.webapp.data.DetalleProductoRepository;
import bo.com.qbit.webapp.data.FuncionarioRepository;
import bo.com.qbit.webapp.data.KardexProductoRepository;
import bo.com.qbit.webapp.data.OrdenTraspasoRepository;
import bo.com.qbit.webapp.data.ProductoRepository;
import bo.com.qbit.webapp.data.ProyectoRepository;
import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.AlmacenProducto;
import bo.com.qbit.webapp.model.DetalleOrdenTraspaso;
import bo.com.qbit.webapp.model.DetalleProducto;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.Funcionario;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.KardexProducto;
import bo.com.qbit.webapp.model.OrdenTraspaso;
import bo.com.qbit.webapp.model.Partida;
import bo.com.qbit.webapp.model.Producto;
import bo.com.qbit.webapp.model.Proveedor;
import bo.com.qbit.webapp.model.Proyecto;
import bo.com.qbit.webapp.service.AlmacenProductoRegistration;
import bo.com.qbit.webapp.service.DetalleOrdenTraspasoRegistration;
import bo.com.qbit.webapp.service.DetalleProductoRegistration;
import bo.com.qbit.webapp.service.KardexProductoRegistration;
import bo.com.qbit.webapp.service.OrdenTraspasoRegistration;
import bo.com.qbit.webapp.util.Cifrado;
import bo.com.qbit.webapp.util.FacesUtil;
import bo.com.qbit.webapp.util.NumberUtil;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "ordenTraspasoController")
@ConversationScoped
public class OrdenTraspasoController implements Serializable {

	private static final long serialVersionUID = 749163787421586877L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	//Repository
	private @Inject AlmacenRepository almacenRepository;
	private @Inject OrdenTraspasoRepository ordenTraspasoRepository;
	private @Inject ProyectoRepository proyectoRepository;
	private @Inject ProductoRepository productoRepository;
	private @Inject DetalleOrdenTraspasoRepository detalleOrdenTraspasoRepository;
	private @Inject AlmacenProductoRepository almacenProductoRepository;
	private @Inject FuncionarioRepository funcionarioRepository;
	private @Inject KardexProductoRepository kardexProductoRepository;
	private @Inject DetalleProductoRepository detalleProductoRepository;

	//Registration
	private @Inject OrdenTraspasoRegistration ordenTraspasoRegistration;
	private @Inject DetalleOrdenTraspasoRegistration detalleOrdenTraspasoRegistration;
	private @Inject AlmacenProductoRegistration almacenProductoRegistration;
	private @Inject KardexProductoRegistration kardexProductoRegistration;
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
	private boolean editarOrdenTraspaso = false;
	private boolean verProcesar = true;
	private boolean verReport = false;
	private boolean verExport = false;

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
	private List<DetalleOrdenTraspaso> listDetalleOrdenTraspasoSinStock = new ArrayList<DetalleOrdenTraspaso>();

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;
	private Gestion gestionSesion;
	private Empresa empresaLogin;

	private boolean atencionCliente=false;

	//archivo de exportacion
	private StreamedContent dFile;

	@PostConstruct
	public void initNewOrdenTraspaso() {

		beginConversation();

		usuarioSession = sessionMain.getUsuarioLogin().getLogin();
		gestionSesion = sessionMain.getGestionLogin();
		empresaLogin = sessionMain.getEmpresaLogin();

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
		verReport = false;
		verExport = false;

		listaDetalleOrdenTraspaso = new ArrayList<DetalleOrdenTraspaso>();
		listaOrdenTraspaso = ordenTraspasoRepository.findAllOrderedByID();

		listaProyecto = proyectoRepository.findAllActivosOrderedByID();
		listFuncionario = funcionarioRepository.findAllActivoOrderedByID();
		//-::::::::: OJO :::::::
		//la lista de almacen se obtendra al hacer click en nuevo orden traspaso
		// y luego de verificar que almacen tiene el usuario, no mostrara dicho almacen en la lista
		//de almacenes
		int numeroCorrelativo = ordenTraspasoRepository.obtenerNumeroOrdenTraspaso(gestionSesion);
		newOrdenTraspaso = new OrdenTraspaso();
		newOrdenTraspaso.setCorrelativo(cargarCorrelativo(numeroCorrelativo));
		newOrdenTraspaso.setEstado("AC");
		newOrdenTraspaso.setGestion(gestionSesion);
		newOrdenTraspaso.setFechaDocumento(new Date());
		newOrdenTraspaso.setFechaRegistro(new Date());
		newOrdenTraspaso.setUsuarioRegistro(usuarioSession);
	}

	private void cargarAlmacen(){
		listaAlmacen = almacenRepository.findAllActivosOrderedByID();
		if(listaAlmacen.size()>0){
			listaAlmacen.remove(selectedAlmacenOrigen);
		}
	}
	private @Inject CierreGestionAlmacenRepository cierreGestionAlmacenRepository;

	public void cambiarAspecto(){

		//verificar si el usuario logeado tiene almacen registrado
		selectedAlmacenOrigen = almacenRepository.findAlmacenForUser(sessionMain.getUsuarioLogin());
		if(selectedAlmacenOrigen.getId() == -1){
			FacesUtil.infoMessage("Usuario "+usuarioSession, "No tiene asignado un almacén");
			return;
		}
		//verificar si el almacen-gestion ya fue cerrado
		if(cierreGestionAlmacenRepository.finAlmacenGestionCerrado(selectedAlmacenOrigen,gestionSesion) != null){
			FacesUtil.infoMessage("INFORMACION", "Encargado "+sessionMain.getUsuarioLogin().getNombre()+" -  El Almacén "+selectedAlmacenOrigen.getNombre()+" fué cerrado");
			return ;
		}
		//cargara la lista de almacen pero no mostrara el almacen del usuario logeado
		cargarAlmacen();
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
				//solo se mmostrara el boton export si la orden de  traspaso esta procesada, porq al procesar una orden de traspaso,
				//se actualizan los precios unitarios de los productos
				//verifica la conexion del almacen
				if(  selectedOrdenTraspaso.getAlmacenDestino().isOnline()){
					verExport = false;
				}else{verExport = true;}
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

	public void redireccionarPgina(){
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			//http://localhost:8080/webapp/pages/dashboard.xhtml
			context.getExternalContext().redirect("http://localhost:8080/webapp/pages/dashboard.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void registrarOrdenTraspaso() {
		if( selectedAlmacen.getId()==0 || selectedFuncionario.getId()==0  ||  selectedProyecto.getId()==0 ){
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios.");
			return;
		}
		if(listaDetalleOrdenTraspaso.size()==0 ){
			FacesUtil.infoMessage("VALIDACION", "Debe Agregar items..");
			return;
		}
		try {
			//if(validarStock()){//valida el stock de los productos
			//	FacesUtil.showDialog("dlgValidacionStock");
			//	return ;
			//}
			Date date = new Date();
			calcularTotal();
			System.out.println("paso a registrarOrdenTraspaso: ");
			newOrdenTraspaso.setFechaRegistro(date);
			newOrdenTraspaso.setProyecto(selectedProyecto);
			newOrdenTraspaso.setFuncionario(selectedFuncionario);
			newOrdenTraspaso.setAlmacenOrigen(selectedAlmacenOrigen);//selectedAlmacen; -> almacen destino
			newOrdenTraspaso.setAlmacenDestino(selectedAlmacen);
			newOrdenTraspaso = ordenTraspasoRegistration.register(newOrdenTraspaso);
			for(DetalleOrdenTraspaso d: listaDetalleOrdenTraspaso){
				d.setCantidadEntregada(0);
				d.setFechaRegistro(date);
				d.setUsuarioRegistro(usuarioSession);
				d.setOrdenTraspaso(newOrdenTraspaso);
				d = detalleOrdenTraspasoRegistration.register(d);
			}
			FacesUtil.infoMessage("Orden de Traspaso Registrada!", ""+newOrdenTraspaso.getCorrelativo());
			// Verificar si el almacen destino es offline
			if( ! selectedAlmacen.isOnline()){

				// lo comente para que solo se pueda exportar desde la grilla principal al seleccionar la orden de traspaso 


				//armar archivo txt(backup)
				//armarFileBackup(this.newOrdenTraspaso,this.listaDetalleOrdenTraspaso);

				// Lanzar dialog de aviso de exportacion
				//FacesUtil.showDialog("dlgExportExcel");
			}
			initNewOrdenTraspaso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Registrar.");
		}
	}

	public void exportar(){
		System.out.println("exportar() ");
		listaDetalleOrdenTraspaso = detalleOrdenTraspasoRepository.findAllByOrdenTraspaso(selectedOrdenTraspaso);
		armarFileBackup(selectedOrdenTraspaso,listaDetalleOrdenTraspaso);
		// Lanzar dialog de aviso de exportacion
		FacesUtil.showDialog("dlgExportExcel");
	}

	private void armarFileBackup(OrdenTraspaso newOrdenTraspaso,List<DetalleOrdenTraspaso> listaDetalleOrdenTraspaso){
		System.out.println("paso a armarFileBackup() ");
		File file = new File("import.txt");
		//Escritura
		try{
			FileWriter w = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(w);
			PrintWriter wr = new PrintWriter(bw);

			//test
			//newOrdenTraspaso = ordenTraspasoRepository.findById(10);
			//listaDetalleOrdenTraspaso = detalleOrdenTraspasoRepository.findAllByOrdenTraspaso(newOrdenTraspaso);
			//>>>>>>>>DATOS ORDENTRASPASO<<<<<<<<<<<<<<<<
			//0 correlativo
			wr.write(Cifrado.Encriptar(newOrdenTraspaso.getCorrelativo(), 12)+"\r\n");
			//>>>>>>>> ALMACEN <<<<<<<<<
			//1 direccion
			wr.write(Cifrado.Encriptar(newOrdenTraspaso.getAlmacenDestino().getDireccion(), 12)+"\r\n");
			//2 codigo
			wr.write( Cifrado.Encriptar(newOrdenTraspaso.getAlmacenDestino().getCodigo(), 12)+"\r\n");
			//3 online
			wr.write(Cifrado.Encriptar(newOrdenTraspaso.getAlmacenDestino().isOnline()?"TRUE":"FALSE", 12)+"\r\n");
			//4 nombre
			wr.write(Cifrado.Encriptar(newOrdenTraspaso.getAlmacenDestino().getNombre(), 12)+"\r\n");
			//5 telefono
			wr.write(Cifrado.Encriptar(newOrdenTraspaso.getAlmacenDestino().getTelefono(), 12)+"\r\n");
			//6 tipoAlmacen
			wr.write(Cifrado.Encriptar(newOrdenTraspaso.getAlmacenDestino().getTipoAlmacen(), 12)+"\r\n");

			for(DetalleOrdenTraspaso detalle: listaDetalleOrdenTraspaso){
				//>>>>>>>>>PRODUCTO<<<<<<<
				Producto producto = detalle.getProducto();
				//7 codigo
				wr.write(Cifrado.Encriptar(producto.getCodigo(), 12)+"\r\n");
				//8 nombre
				wr.write(Cifrado.Encriptar(producto.getNombre(), 12)+"\r\n");
				//9 descripcion
				wr.write(Cifrado.Encriptar(producto.getDescripcion(), 12)+"\r\n");
				//10 precioUnitario
				wr.write(Cifrado.Encriptar(String.valueOf(producto.getPrecioUnitario()), 12)+"\r\n");
				//11 tipoProducto
				wr.write(Cifrado.Encriptar(producto.getTipoProducto(), 12)+"\r\n");
				//12 unidadMedida
				wr.write(Cifrado.Encriptar(producto.getUnidadMedidas().getNombre(), 12)+"\r\n");
				//>>>>>>>>PARTIDA<<<<<<<<
				Partida partida = producto.getPartida();
				//13 codigo
				wr.write(Cifrado.Encriptar(partida.getCodigo(), 12)+"\r\n");
				//14 nombre
				wr.write(Cifrado.Encriptar(partida.getNombre(), 12)+"\r\n");
				//15 descripcion
				wr.write(Cifrado.Encriptar(partida.getDescripcion(), 12)+"\r\n");
				//>>>>>>>>DETALLE ORDEN INGRESO<<<<<<<<<
				//16 cantidad
				wr.write(Cifrado.Encriptar(String.valueOf(detalle.getCantidadEntregada()), 12)+"\r\n");
				//17 observacion
				wr.write(Cifrado.Encriptar(detalle.getObservacion(), 12)+"\r\n");
				//18 total
				wr.write(Cifrado.Encriptar(String.valueOf(detalle.getTotal()), 12)+"\r\n");
				//19 precio unitario
				wr.write(Cifrado.Encriptar(String.valueOf(detalle.getPrecioUnitario()), 12)+"\r\n");
			}
			wr.close();
			bw.close();
			InputStream stream = new FileInputStream(file);
			Date fecha = new Date(); 
			String s = String.format("%td%tm%ty", fecha,fecha,fecha);
			dFile = new DefaultStreamedContent(stream, "text/plain", "import_"+s+newOrdenTraspaso.getCorrelativo()+".txt") ;
			System.out.println("-FIN- dFile:"+dFile);
		}catch(IOException e){
		}
	}

	//	/**
	//	 * Validar el stock actual de los productos del detalle de Orden de Traspaso
	//	 * @return
	//	 */
	//	private boolean validarStock(){
	//		System.out.println("validarStock() ");
	//		listDetalleOrdenTraspasoSinStock = new ArrayList<DetalleOrdenTraspaso>();
	//		for(DetalleOrdenTraspaso d: listaDetalleOrdenTraspaso){
	//			//double stockAPedir = d.getCantidadSolicitada();
	//			Producto producto = d.getProducto();
	//			//AlmacenProducto almProd =  almacenProductoRepository.findByProducto(prod);
	//			//if(stockAPedir > almProd.getStock()){
	//			//listDetalleOrdenTraspasoSinStock.add(d);
	//			//}
	//
	//			//verificar en detalle_producto, si existen stock
	//			double cantidadSolicitada = d.getCantidadSolicitada();
	//			double precioPonderado = 0;
	//			//obtener todos los detalles del producto, para poder descontar stock de acuerdo a la cantidad solicitada
	//			List<DetalleProducto> listDetalleProducto = detalleProductoRepository.findAllByProductoOrderByFecha(producto);
	//			for(DetalleProducto detalle : listDetalleProducto){
	//				double stockActual = detalle.getStockActual();
	//				if(cantidadSolicitada > 0){// si la  cantidad Solicitada lo obtiene
	//					precioPonderado = detalle.getPrecio();
	//					double stockFinal = stockActual-cantidadSolicitada;
	//					detalle.setStockActual( stockFinal < 0 ? 0 : stockFinal);
	//					d.setEstado((stockActual-cantidadSolicitada)==0?"AC":"IN");
	//					cantidadSolicitada = cantidadSolicitada - stockActual  ;//actualizar cantidad solicitada
	//				}
	//			}
	//			//actualizar cantidad entregada, si no se obtuvo la cantidad solicitada
	//			if(cantidadSolicitada > 0){
	//				detalle.setCantidadEntregada(cantidadSolicitada);//registrar el resto
	//			}
	//			//actualizar el precio
	//			precioPonderado = (listDetalleProducto.size()>0? (precioPonderado/listDetalleProducto.size()):0);
	//			detalle.setPrecioUnitario(precioPonderado);
	//			detalle.setTotal(precioPonderado*detalle.getCantidad());
	//			detalleOrdenTraspasoRegistration.updated(detalle);
	//			
	//		}
	//		return listDetalleOrdenTraspasoSinStock.size()>0?true:false;
	//	}

	public void modificarOrdenTraspaso() {
		try {
			System.out.println("Traspaso a modificarOrdenTraspaso: ");
			Date fechaActual = new Date();
			double total = 0;
			for(DetalleOrdenTraspaso d: listaDetalleOrdenTraspaso){
				d.setCantidadEntregada(0);
				if(d.getId()==0){//si es un nuevo registro
					d.setFechaRegistro(fechaActual);
					d.setUsuarioRegistro(usuarioSession);
					d.setEstado("AC");
					d.setOrdenTraspaso(newOrdenTraspaso);
					detalleOrdenTraspasoRegistration.register(d);
				}
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
			FacesUtil.infoMessage("Orden de Traspaso Modificada!", ""+newOrdenTraspaso.getCorrelativo());
			initNewOrdenTraspaso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Modificar.");
		}
	}

	public void eliminarOrdenTraspaso() {
		try {
			System.out.println("Traspaso a eliminarOrdenTraspaso: ");
			ordenTraspasoRegistration.remover(selectedOrdenTraspaso);
			//			for(DetalleOrdenTraspaso d: listaDetalleOrdenTraspaso){
			//				detalleOrdenTraspasoRegistration.remover(d);
			//			}
			FacesUtil.infoMessage("Orden de Traspaso Eliminada!", ""+newOrdenTraspaso.getCorrelativo());
			initNewOrdenTraspaso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Eliminar.");
		}
	}

	private double total = 0;

	public void procesarOrdenTraspaso(){
		try {
			System.out.println("procesarOrdenTraspaso()");
			total = 0;
			Date fechaActual = new Date();
			//actualizar estado de orden Traspaso
			selectedOrdenTraspaso.setEstado("PR");
			selectedOrdenTraspaso.setFechaAprobacion(fechaActual);

			Proveedor proveedor = null;

			//	newOrdenTraspaso.setAlmacenOrigen(selectedAlmacenOrigen);//selectedAlmacen; -> almacen destino

			//actualizar stock de AlmacenProducto
			listaDetalleOrdenTraspaso = detalleOrdenTraspasoRepository.findAllByOrdenTraspaso(selectedOrdenTraspaso);

			Almacen almOrig = selectedOrdenTraspaso.getAlmacenOrigen();
			Almacen almDest = selectedOrdenTraspaso.getAlmacenDestino();
			for(DetalleOrdenTraspaso d: listaDetalleOrdenTraspaso){
				Producto prod = d.getProducto();
				//double cantidadSolicitada = d.getCantidadSolicitada();
				//1.- Actualizar detalle producto (PEPS) y tambiaen actualizar precio en detalleOrdenIngreso
				if(  actualizarDetalleProducto(almOrig,d)){
					//mostrar mensaje
					//FacesUtil.showDialog("dlgAlmacenSinExistencias");
					//initNewOrdenTraspaso();
					//return;//no se econtro stock disponible

					//2.- 
					System.out.println("actualizarStockAlmacenOrigen("+almOrig.getNombre()+","+prod.getNombre()+","+totalCantidaEntregada);
					actualizarStockAlmacenOrigen(almOrig, prod,totalCantidaEntregada);

					//4.-
					System.out.println("actualizarKardexProducto("+almOrig.getNombre()+","+almDest.getNombre()+","+prod.getNombre()+","+fechaActual+","+totalCantidaEntregada+","+d.getPrecioUnitario());
					actualizarKardexProducto(almOrig,almDest,prod, fechaActual, totalCantidaEntregada,d.getPrecioUnitario());
					//total = total + (totalCantidaEntregada * d.getPrecioUnitario());
				}
				//agregar detalleProductos al almacen destino
				if(verificacionCantidadEntregada>0){
					//3.-
					System.out.println("actualizarStockAlmacenDestino("+proveedor+","+prod.getNombre()+","+almDest.getNombre()+","+totalCantidaEntregada+","+fechaActual+","+d.getPrecioUnitario());
					actualizarStockAlmacenDestino(proveedor,prod,almDest, totalCantidaEntregada,fechaActual,d.getPrecioUnitario());
					//
					System.out.println("cargarDetalleProducto("+fechaActual+","+almDest.getNombre()+","+prod.getNombre()+","+totalCantidaEntregada+","+d.getPrecioUnitario()+","+d.getFechaRegistro()+","+selectedOrdenTraspaso.getCorrelativo()+","+usuarioSession);
					cargarDetalleProducto(fechaActual, almDest, prod, totalCantidaEntregada, d.getPrecioUnitario(), d.getFechaRegistro(), selectedOrdenTraspaso.getCorrelativo(), usuarioSession);
				}


			}
			//actualizar ordenTraspaso
			selectedOrdenTraspaso.setTotalImporte(total);
			ordenTraspasoRegistration.updated(selectedOrdenTraspaso);
			FacesUtil.infoMessage("Orden de Traspaso Procesada!", "");
			initNewOrdenTraspaso();
		} catch (Exception e) {
			System.out.println("procesarOrdenTraspaso() Error: "+e.getMessage());
			FacesUtil.errorMessage("Proceso Incorrecto.");
		}
	}

	public void cargarDetalleProducto(Date fechaActual,Almacen almacen,Producto producto,double cantidad, double precio, Date fecha, String correlativoTransaccion,String usuarioSession ) {
		try{
			DetalleProducto detalleProducto = new DetalleProducto();
			detalleProducto.setCodigo("OT"+correlativoTransaccion+fecha.toString());
			detalleProducto.setAlmacen(almacen);
			detalleProducto.setEstado("AC");
			detalleProducto.setPrecio(precio);
			detalleProducto.setStockActual(cantidad);
			detalleProducto.setStockInicial(cantidad);
			detalleProducto.setCorrelativoTransaccion(correlativoTransaccion);
			detalleProducto.setFecha(fecha);
			detalleProducto.setFechaRegistro(fechaActual);
			detalleProducto.setProducto(producto);
			detalleProducto.setUsuarioRegistro(usuarioSession);
			detalleProducto.setGestion(gestionSesion);
			detalleProductoRegistration.register(detalleProducto);
		}catch(Exception e){
			System.out.println("ERROR cargarDetalleProducto() "+e.getMessage());
			e.printStackTrace();
		}
	}

	private double totalCantidaEntregada = 0;
	private double verificacionCantidadEntregada = 0;
	/**
	 * Actualiza el stock, verifica existencias de acuerdo al metodo PEPS
	 * @param almacen De que almacen se sacara los productos
	 * @param detalle
	 * @return true si hay stock, false si no hay existncias
	 */
	private boolean actualizarDetalleProducto(Almacen almacen,DetalleOrdenTraspaso detalle){
		try{
			totalCantidaEntregada = 0;
			verificacionCantidadEntregada = 0;
			Producto producto = detalle.getProducto();
			double cantidadAux = detalle.getCantidadSolicitada();
			double cantidadSolicitada = detalle.getCantidadSolicitada();// 15
			int cantidad = 1;
			//obtener todos los detalles del producto, para poder descontar stock de acuerdo a la cantidad solicitada
			List<DetalleProducto> listDetalleProducto = detalleProductoRepository.findAllByProductoAndAlmacenOrderByFecha(almacen,producto,gestionSesion);
			System.out.println("listDetalleProducto.size()"+listDetalleProducto.size());
			//50 | 10
			//52 | 10
			if(listDetalleProducto.size()>0){
				for(DetalleProducto d : listDetalleProducto){
					double stockActual = d.getStockActual();//10 |10
					double precio = d.getPrecio(); // 50 | 52
					if(cantidadSolicitada > 0){// 15 | 5
						double stockFinal = stockActual- cantidadSolicitada; // 10-15=-5 | 10-5=5 |
						double cantidadRestada = stockFinal < 0 ? cantidadSolicitada -(cantidadSolicitada - stockActual) : cantidadSolicitada; //15-(15-10)=10 | 5 |
						d.setStockActual( stockFinal <= 0 ? 0 : stockFinal); // 0 | 5
						d.setEstado(stockFinal<=0?"IN":"AC"); // IN | AC
						detalleProductoRegistration.updated(d);
						cantidadSolicitada = cantidadSolicitada - cantidadRestada  ;//actualizar cantidad solicitada // 15-10=5| 5-5=0|
						if(cantidad == 1){
							detalle.setCantidadEntregada(cantidadRestada);
							detalle.setCantidadSolicitada(cantidadAux);
							detalle.setPrecioUnitario(precio);
							detalle.setTotal(precio*cantidadRestada);
							detalleOrdenTraspasoRegistration.updated(detalle);
							total = total + detalle.getTotal();
						}else{//nuevo DetalleOrdenSalida con otro precio
							detalle.setId(0);
							detalle.setCantidadEntregada(cantidadRestada );
							detalle.setCantidadSolicitada(cantidadAux);
							detalle.setPrecioUnitario(precio);
							detalle.setTotal(precio*cantidadRestada);
							detalleOrdenTraspasoRegistration.register(detalle);
							total = total + detalle.getTotal();
						}
						verificacionCantidadEntregada = verificacionCantidadEntregada + cantidadRestada;
						cantidad = cantidad + 1;
					}
				}
				totalCantidaEntregada = cantidadAux - cantidadSolicitada;
				return true;
			}
			return false;
		}catch(Exception e){
			System.out.println("actualizarDetalleProductoByOrdenSalida() ERROR: "+e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	//aumentar stock de almacen destino
	private void actualizarStockAlmacenDestino(Proveedor proveedor,Producto prod ,Almacen almDest, double newStock,Date date,double precioUnitario) throws Exception {
		try{
			System.out.println("actualizarStockAlmacenDestino()");
			//0 . verificar si existe el producto en el almacen

			AlmacenProducto almProd =  new AlmacenProducto();
			/*
			almProd =  almacenProductoRepository.findByAlmacenProducto(almDest,prod);
			System.out.println("almProd = "+almProd);
			if(almProd != null){
				// 1 .  si existe el producto
				double oldStock = almProd.getStock();
				double oldPrecioUnitario = almProd.getPrecioUnitario();
				almProd.setStock(oldStock + newStock);
				almProd.setPrecioUnitario((oldPrecioUnitario+precioUnitario)/2);//precioPonderado
				almacenProductoRegistration.updated(almProd);
				return ;
			}
			 */
			// 2 . no existe el producto
			almProd = new AlmacenProducto();
			almProd.setAlmacen(almDest);
			almProd.setPrecioUnitario(precioUnitario);
			almProd.setProducto(prod);
			almProd.setProveedor(proveedor);//proveedor = null (Ingreso or Orden Traspaso)
			almProd.setStock(newStock);
			almProd.setEstado("AC");
			almProd.setGestion(gestionSesion);
			almProd.setFechaRegistro(date);
			almProd.setUsuarioRegistro(usuarioSession);
			almacenProductoRegistration.register(almProd);
		}catch(Exception e){
			System.out.println("actualizarStockAlmacenDestino() Error: "+e.getMessage());
		}
	}

	//disminuir stock de almacen origen
	private void actualizarStockAlmacenOrigen(Almacen almOrig,Producto producto,double cantidadSolicitada) throws Exception {
		try{
			System.out.println("actualizarStockAlmacenOrigen()");
			/*
			//0 . verificar si existe el producto en el almacen

			AlmacenProducto almProd =  almacenProductoRepository.findByAlmacenProducto(almOrig,prod);
			if(almProd != null){
				// 1 .  si existe el producto
				double oldStock = almProd.getStock();
				double oldPrecioUnitario = almProd.getPrecioUnitario();
				almProd.setStock(oldStock - newStock);
				almProd.setPrecioUnitario((oldPrecioUnitario+precioUnitario)/2);//precioPonderado
				almacenProductoRegistration.updated(almProd);
				return ;
			}
			 */
			//Producto producto = detalle.getProducto();
			//double cantidadSolicitada = detalle.getCantidadSolicitada();// 15
			//obtener listAlmacenProducto ordenado por fecha segun metodo PEPS
			List<AlmacenProducto> listAlmacenProducto =  almacenProductoRepository.findAllByProductoAndAlmacenOrderByFecha(almOrig,producto);
			System.out.println("listAlmacenProducto.size()"+listAlmacenProducto.size());

			if(listAlmacenProducto.size()>0){
				for(AlmacenProducto d : listAlmacenProducto){
					double stockActual = d.getStock();//10 
					if(cantidadSolicitada > 0){// 15 
						double stockFinal = stockActual- cantidadSolicitada; // 10-15=-5 | 10-5=5 |
						double cantidadRestada = stockFinal < 0 ? cantidadSolicitada -(cantidadSolicitada - stockActual) : cantidadSolicitada; //15-(15-10)=10 
						d.setStock( stockFinal <= 0 ? 0 : stockFinal); // 0 | 5
						d.setEstado(stockFinal<=0?"IN":"AC"); // IN | AC
						almacenProductoRegistration.updated(d);
						cantidadSolicitada = cantidadSolicitada - cantidadRestada  ;//actualizar cantidad solicitada // 15-10=5
					}
				}
			}
		}catch(Exception e){
			System.out.println("actualizarStockAlmacenOrigen() Error: "+e.getMessage());
		}
	}

	//registro en la tabla kardex_producto, un ingreso y una salida 
	private void actualizarKardexProducto(Almacen almOrig,Almacen almDest,Producto prod,Date fechaActual,double cantidad,double precioUnitario) throws Exception{
		System.out.println("actualizarKardexProducto()");
		try{
			//registrar Kardex
			KardexProducto kardexProductoAnt = kardexProductoRepository.findKardexStockAnteriorByProductoAlmacen(prod,selectedOrdenTraspaso.getAlmacenDestino());
			System.out.println("kardexProductoAnt "+kardexProductoAnt);
			double stockAnterior = 0;
			if(kardexProductoAnt != null){
				//se obtiene el saldo anterior del producto
				stockAnterior = kardexProductoAnt.getStockAnterior();
			}

			double entrada = cantidad;
			double salida = 0;
			double saldo = stockAnterior + cantidad;//(+) aumenta en el almacen de destino
			System.out.println("Aumentando stock al almacen destino = "+selectedOrdenTraspaso.getAlmacenDestino().getNombre());
			System.out.println("entrada="+entrada);
			System.out.println("salida="+salida);
			System.out.println("saldo="+saldo);

			//ingresando al almacen destino
			KardexProducto kardexProducto = new KardexProducto();
			kardexProducto.setId(0);
			kardexProducto.setUnidadSolicitante(selectedOrdenTraspaso.getAlmacenDestino().getNombre());
			kardexProducto.setFecha(fechaActual);
			kardexProducto.setAlmacen(selectedOrdenTraspaso.getAlmacenDestino());
			kardexProducto.setCantidad(cantidad);
			kardexProducto.setEstado("AC");
			kardexProducto.setFechaRegistro(fechaActual);
			kardexProducto.setGestion(gestionSesion);
			kardexProducto.setNumeroTransaccion("T-"+selectedOrdenTraspaso.getCorrelativo());

			//EN BOLIVIANOS
			kardexProducto.setPrecioUnitario(precioUnitario);
			kardexProducto.setTotalEntrada(precioUnitario * entrada);
			kardexProducto.setTotalSalida(precioUnitario * salida);
			kardexProducto.setTotalSaldo(precioUnitario * saldo);

			//CANTIDADES
			kardexProducto.setStock(entrada);//ENTRADA
			kardexProducto.setStockActual(salida);//SALIDA
			kardexProducto.setStockAnterior(saldo);//SALDO

			kardexProducto.setProducto(prod);
			kardexProducto.setTipoMovimiento("ORDEN TRASPASO");
			kardexProducto.setUsuarioRegistro(usuarioSession);
			//register orden traspaso - almacen de destino
			kardexProductoRegistration.register(kardexProducto);
			///----------------------------------------------------------------------------------------

			//registrar Kardex
			kardexProductoAnt = kardexProductoRepository.findKardexStockAnteriorByProductoAlmacen(prod,selectedOrdenTraspaso.getAlmacenOrigen());
			System.out.println("kardexProductoAnt "+kardexProductoAnt);
			stockAnterior = 0;
			if(kardexProductoAnt != null){
				//se obtiene el saldo anterior del producto
				stockAnterior = kardexProductoAnt.getStockAnterior();
			}
			entrada = 0;
			salida = cantidad;
			saldo = stockAnterior -  cantidad;//(-) disminuye en el almacen origen
			System.out.println("Disminuyecto stock al almacen origen = "+selectedOrdenTraspaso.getAlmacenOrigen().getNombre());
			System.out.println("entrada="+entrada);
			System.out.println("salida="+salida);
			System.out.println("saldo="+saldo);
			//saliendo del almacen de origen
			kardexProducto = new KardexProducto();
			kardexProducto.setId(0);
			kardexProducto.setUnidadSolicitante(selectedOrdenTraspaso.getAlmacenOrigen().getNombre());
			kardexProducto.setFecha(fechaActual);
			kardexProducto.setAlmacen(selectedOrdenTraspaso.getAlmacenOrigen());
			kardexProducto.setCantidad(cantidad);
			kardexProducto.setEstado("AC");
			kardexProducto.setFechaRegistro(fechaActual);
			kardexProducto.setGestion(gestionSesion);
			kardexProducto.setNumeroTransaccion("T-"+selectedOrdenTraspaso.getCorrelativo());

			//EN BOLIVIANOS
			kardexProducto.setPrecioUnitario(precioUnitario);
			kardexProducto.setTotalEntrada(precioUnitario * entrada);
			kardexProducto.setTotalSalida(precioUnitario * salida);
			kardexProducto.setTotalSaldo(precioUnitario * saldo);

			//CANTIDADES
			kardexProducto.setStock(entrada);//ENTRADA
			kardexProducto.setStockActual(salida);//SALIDA
			kardexProducto.setStockAnterior(saldo);//SALDO

			kardexProducto.setProducto(prod);
			kardexProducto.setTipoMovimiento("ORDEN TRASPASO");
			kardexProducto.setUsuarioRegistro(usuarioSession);
			kardexProductoRegistration.register(kardexProducto);
		}catch(Exception e){
			System.out.println("actualizarKardexProducto() Error: "+e.getMessage());
		}
	}

	public void cargarReporte(){
		try {
			urlOrdenTraspaso = loadURL();
			//RequestContext context = RequestContext.getCurrentInstance();
			//context.execute("PF('dlgVistaPreviaOrdenTraspaso').show();");

			verReport = true;

			//initNewOrdenTraspaso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Proceso Incorrecto.");
		}
	}

	public String loadURL(){
		try{
			//ReporteOrdenTraspaso?pIdOrdenTraspaso=7&pUsuario=admin&pTypeExport=pdf
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();  
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
			String urlPDFreporte = urlPath+"ReporteOrdenTraspaso?pIdOrdenTraspaso="+selectedOrdenTraspaso.getId()+"&pUsuario="+usuarioSession+"&pTypeExport=pdf"+"&pNitEmpresa="+empresaLogin.getNIT()+"&pNombreEmpresa="+empresaLogin.getRazonSocial();
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
		FacesUtil.resetDataTable("formTableOrdenTraspaso:itemsTable1");
		verButtonDetalle = true;
	}

	public void limpiarDatosProducto(){
		selectedProducto = new Producto();
		selectedDetalleOrdenTraspaso = new DetalleOrdenTraspaso();
		FacesUtil.resetDataTable("formTableOrdenTraspaso:itemsTable1");
		verButtonDetalle = true;
		editarOrdenTraspaso = false;
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

	public void agregarDetalleOrdenTraspaso(){
		//verificar que seleccione el almacen
		if(selectedProducto.getId()==0){
			FacesUtil.infoMessage("VALIDACION", "Seleccione un producto");
			//FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			return;
		}
		//verificar si hay stock del producto
		double cantidad =  cantidadExistenciasByProductoAlmacen(selectedAlmacenOrigen,selectedProducto);
		if( cantidad==0 ){ 
			setTextDialogExistencias("El almacen "+selectedAlmacenOrigen.getNombre()+" no tiene existencias del producto "+selectedProducto.getNombre());
			//ocultar dialgo
			FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			//abrir dialog
			FacesUtil.showDialog("dlgValidacionExistenciasAlmacen");
			return;
		}else if(cantidad < selectedDetalleOrdenTraspaso.getCantidadSolicitada()){
			setTextDialogExistencias("El almacen "+selectedAlmacenOrigen.getNombre()+" solo tiene "+cantidad+" existencias del producto "+selectedProducto.getNombre());
			//selectedDetalleOrdenSalida.setCantidadSolicitada(cantidad);
			//ocultar dialgo
			FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			//abrir dialog
			FacesUtil.showDialog("dlgValidacionExistenciasAlmacen");
			return;
		}
		System.out.println("agregarDetalleOrdenTraspaso ");
		selectedDetalleOrdenTraspaso.setProducto(selectedProducto);
		listaDetalleOrdenTraspaso.add(0, selectedDetalleOrdenTraspaso);
		selectedProducto = new Producto();
		selectedDetalleOrdenTraspaso = new DetalleOrdenTraspaso();
		FacesUtil.resetDataTable("formTableOrdenTraspaso:itemsTable1");
		verButtonDetalle = true;
		FacesUtil.hideDialog("dlgProducto");//ocultar dialog
	}

	public void modificarDetalleOrdenTraspaso(){
		//verificar que seleccione el almacen
		if(selectedProducto.getId()==0){
			FacesUtil.infoMessage("VALIDACION", "Seleccione un producto");
			//FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			return;
		}
		//verificar si hay stock del producto
		double cantidad =  cantidadExistenciasByProductoAlmacen(selectedAlmacenOrigen,selectedProducto);
		if( cantidad==0 ){ 
			setTextDialogExistencias("El almacen "+selectedAlmacenOrigen.getNombre()+" no tiene existencias del producto "+selectedProducto.getNombre());
			//ocultar dialgo
			FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			//abrir dialog
			FacesUtil.showDialog("dlgValidacionExistenciasAlmacen");
			return;
		}else if(cantidad < selectedDetalleOrdenTraspaso.getCantidadSolicitada()){
			setTextDialogExistencias("El almacen "+selectedAlmacenOrigen.getNombre()+" solo tiene "+cantidad+" existencias del producto "+selectedProducto.getNombre());
			//selectedDetalleOrdenSalida.setCantidadSolicitada(cantidad);
			//ocultar dialgo
			FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			//abrir dialog
			FacesUtil.showDialog("dlgValidacionExistenciasAlmacen");
			return;
		}
		System.out.println("modificarDetalleOrdenTraspaso ");
		for(DetalleOrdenTraspaso d: listaDetalleOrdenTraspaso){
			if(d.equals(selectedDetalleOrdenTraspaso)){
				d = selectedDetalleOrdenTraspaso;
			}
		}
		selectedProducto = new Producto();
		selectedDetalleOrdenTraspaso = new DetalleOrdenTraspaso();
		FacesUtil.resetDataTable("formTableOrdenTraspaso:itemsTable1");
		verButtonDetalle = true;
		editarOrdenTraspaso = false;
		FacesUtil.hideDialog("dlgProducto");//ocultar dialog
	}

	//calcular totales
	public void calcular(){
		System.out.println("calcular()");
		//double precio = selectedProducto.getPrecioUnitario();
		//double cantidad = selectedDetalleOrdenTraspaso.getCantidadSolicitada();
		//selectedDetalleOrdenTraspaso.setTotal(precio * cantidad);
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
		if(NumberUtil.isNumeric(query)){//si es numero
			for(Proyecto i : listaProyecto) {
				if(i.getCodigo().startsWith(query)){
					results.add(i);
				}
			}
		}else{//es letra
			for(Proyecto i : listaProyecto) {
				if(i.getNombre().toUpperCase().startsWith(upperQuery)){
					results.add(i);
				}
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
		String upperQuery = query.toUpperCase();
		return productoRepository.findAllProductoForQueryNombre(upperQuery);
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
		return selectedAlmacen;//almacen destino
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

	public List<DetalleOrdenTraspaso> getListDetalleOrdenTraspasoSinStock() {
		return listDetalleOrdenTraspasoSinStock;
	}

	public void setListDetalleOrdenTraspasoSinStock(
			List<DetalleOrdenTraspaso> listDetalleOrdenTraspasoSinStock) {
		this.listDetalleOrdenTraspasoSinStock = listDetalleOrdenTraspasoSinStock;
	}

	public boolean isVerReport() {
		return verReport;
	}

	public void setVerReport(boolean verReport) {
		this.verReport = verReport;
	}

	public StreamedContent getdFile() {
		System.out.println("getdFile "+dFile);
		return dFile;
	}

	public void setdFile(StreamedContent dFile) {
		System.out.println("setdFile "+dFile);
		this.dFile = dFile;
	}

	public boolean isVerExport() {
		return verExport;
	}

	public void setVerExport(boolean verExport) {
		this.verExport = verExport;
	}

	public static void main(String[] args){
		System.out.println("ingreso");
		String[] arg = {"1","2","3","4","5","6"};
		for(String s : arg){
			System.out.println("s: "+s);
			if(s.equals("4")){
				return;
			}
		}
		System.out.println("salida ");
	}

	public String getTextDialogExistencias() {
		return textDialogExistencias;
	}

	public void setTextDialogExistencias(String textDialogExistencias) {
		this.textDialogExistencias = textDialogExistencias;
	}

}
