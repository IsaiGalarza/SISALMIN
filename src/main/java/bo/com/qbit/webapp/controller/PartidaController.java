package bo.com.qbit.webapp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.PartidaRepository;
import bo.com.qbit.webapp.data.ProductoRepository;
import bo.com.qbit.webapp.data.UsuarioRepository;
import bo.com.qbit.webapp.model.Partida;
import bo.com.qbit.webapp.model.Producto;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.service.EstadoUsuarioLogin;
import bo.com.qbit.webapp.service.PartidaRegistration;
import bo.com.qbit.webapp.service.ProductoRegistration;

@Named(value = "partidaController")
@ConversationScoped
public class PartidaController implements Serializable {

	private static final long serialVersionUID = 4712585021390261661L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	private FacesContext facesContext;

	@Inject
	private EntityManager em;

	@Inject
	Conversation conversation;

	@Inject
	private PartidaRegistration partidaRegistration;

	@Inject
	private PartidaRepository partidaRepository;
	
	@Inject ProductoRegistration productoRegistration;
	
	@Inject ProductoRepository productoRepository;

	private @Inject UsuarioRepository usuarioRepository;
//	private Usuario usuarioSession;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	private boolean modificar = false;
	private boolean registrar = false;
	private boolean crear = true;
	
	private String tituloPanel = "Registrar Partida";
	private Partida selectedPartida;
	private Partida newPartida= new Partida();
	private List<Usuario> listUsuario = new ArrayList<Usuario>();

	private List<Partida> listaPartida;
	private EstadoUsuarioLogin estadoUsuarioLogin;
	
	
	private List<Producto> listaProductos = new ArrayList<Producto>(); // ITEMS
	private String tituloProducto = "Agregar Producto";
	private Producto newProducto;
	private Producto selectedProducto;
	private boolean diagloProducto;
	
	private boolean atencionCliente=false;

	// @Named provides access the return value via the EL variable name
	// "servicios" in the UI (e.g.
	// Facelets or JSP view)
	@Produces
	@Named
	public List<Partida> getListaPartida() {
		return listaPartida;
	}
	
	private String usuarioSession;
	
	@PostConstruct
	public void initNewPartida() {

		// initConversation();
		beginConversation();

		HttpServletRequest request = (HttpServletRequest) facesContext
				.getExternalContext().getRequest();
		System.out
				.println("init Tipo Producto*********************************");
		System.out.println("request.getClass().getName():"
				+ request.getClass().getName());
		System.out.println("isVentas:" + request.isUserInRole("ventas"));
		System.out.println("remoteUser:" + request.getRemoteUser());
		System.out.println("userPrincipalName:"
				+ (request.getUserPrincipal() == null ? "null" : request
						.getUserPrincipal().getName()));
		
		estadoUsuarioLogin = new EstadoUsuarioLogin(facesContext);
		usuarioSession =  estadoUsuarioLogin.getNombreUsuarioSession();
		listUsuario = usuarioRepository.findAllOrderedByID();

		newPartida = new Partida();
		newPartida.setEstado("AC");
		newPartida.setFechaRegistro(new Date());
		newPartida.setUsuarioRegistro(usuarioSession);
		

		// tituloPanel
		tituloPanel = "Registrar Partida";

		// traer todos las Partidaes ordenados por ID Desc
		listaPartida = partidaRepository.traerPartidaActivas();
		
		modificar = false;
		registrar = false;
		crear = true;
		atencionCliente=false;
		
		
		listaProductos = new ArrayList<Producto>();
		newProducto = new Producto();
	}
	
	public void cambiarAspecto(){
		modificar = false;
		registrar = true;
		crear = false;
		
		listaProductos = new ArrayList<Producto>();
		newProducto = new Producto();
	}
	
	public void cambiarAspectoModificar(){
		modificar = true;
		registrar = false;
		crear = false;
		
		listaProductos = productoRepository.findAllProductoForPartidaID(newPartida.getId());
		newProducto = new Producto();
	}
	
	public void crearProducto(){
		newProducto = new Producto();
		tituloProducto = "Agregar Producto";
	}
	
	public void modificarProducto(){
		newProducto = selectedProducto;
		tituloProducto = "Modificar Producto";
	}
	
	public void borrarProducto(){
		listaProductos.remove(selectedProducto);
	}
	
	public void agregarProducto(){
		listaProductos.add(0, newProducto);
		newProducto = new Producto();
		diagloProducto = false;
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

	public void updatedCantidad() {
		/*
		 * if(!newPartida.isShowCantidad()){ newPartida.setCantidadCaja(0); }
		 */
	}

	// SELECT PARTIDA CLICK
	public void onRowSelectPartidaClick(SelectEvent event) {
		try {
			Partida Partida = (Partida) event.getObject();
			System.out.println("onRowSelectPartidaClick  " + Partida.getId());
			selectedPartida = Partida;
			newPartida = em.find(Partida.class, Partida.getId());
			newPartida.setFechaRegistro(new Date());
			newPartida.setUsuarioRegistro(usuarioSession);

			tituloPanel = "Modificar Partida";
			modificar = false;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Error in onRowSelectPartidaClick: "
					+ e.getMessage());
		}
	}
	
	
	// SELECT PRODUCTO CLICK
	public void onRowSelectProductoClick(SelectEvent event) {
			try {
				Producto producto = (Producto) event.getObject();
				System.out.println("onRowSelectProductoClick  " + producto.getId());
				selectedProducto = producto;
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println("Error in onRowSelectProductoClick: "
						+ e.getMessage());
			}
	}

	public void registrarPartida() {
		try {
			System.out.println("Ingreso a registrarPartida: ");
			partidaRegistration.register(newPartida);
			
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Partida Registrado!", newPartida.getNombre()+"!");
			facesContext.addMessage(null, m);
			
			initNewPartida();
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Registro Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}

	public void modificarPartida() {
		try {
			System.out.println("Ingreso a modificarPartida: "
					+ newPartida.getId());
			partidaRegistration.updated(newPartida);
			
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Partida Modificado!", newPartida.getNombre()+"!");
			facesContext.addMessage(null, m);
			initNewPartida();

		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Modificado Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}
	
	public void modificar(){
		System.out.println("Ingreso a modificar");
	}

	public void eliminarPartida() {
		try {
			System.out.println("Ingreso a eliminarPartida: "
					+ newPartida.getId());
			partidaRegistration.remover(newPartida);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Partida Borrado!", newPartida.getNombre()+"!");
			facesContext.addMessage(null, m);
			initNewPartida();

		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Borrado Incorrecto.");
			facesContext.addMessage(null, m);
		}
	}

	private String getRootErrorMessage(Exception e) {
		// Default to general error message that registration failed.
		String errorMessage = "Registration failed. See server log for more information";
		if (e == null) {
			// This shouldn't happen, but return the default messages
			return errorMessage;
		}

		// Start with the exception and recurse to find the root cause
		Throwable t = e;
		while (t != null) {
			// Get the message from the Throwable class instance
			errorMessage = t.getLocalizedMessage();
			t = t.getCause();
		}
		// This is the root cause message
		return errorMessage;
	}

	// get and set
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

	public Partida getSelectedPartida() {
		return selectedPartida;
	}

	public void setSelectedPartida(Partida selectedPartida) {
		this.selectedPartida = selectedPartida;
	}

	public Partida getNewPartida() {
		return newPartida;
	}

	public void setNewPartida(Partida newPartida) {
		this.newPartida = newPartida;
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

	public List<Producto> getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos(List<Producto> listaProductos) {
		this.listaProductos = listaProductos;
	}

	public Producto getSelectedProducto() {
		return selectedProducto;
	}

	public void setSelectedProducto(Producto selectedProducto) {
		this.selectedProducto = selectedProducto;
	}

	public Producto getNewProducto() {
		return newProducto;
	}

	public void setNewProducto(Producto newProducto) {
		this.newProducto = newProducto;
	}

	public String getTituloProducto() {
		return tituloProducto;
	}

	public void setTituloProducto(String tituloProducto) {
		this.tituloProducto = tituloProducto;
	}

	public boolean isDiagloProducto() {
		return diagloProducto;
	}

	public void setDiagloProducto(boolean diagloProducto) {
		this.diagloProducto = diagloProducto;
	}

}
