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

import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.ProductoRepository;
import bo.com.qbit.webapp.data.PartidaRepository;
import bo.com.qbit.webapp.model.Producto;
import bo.com.qbit.webapp.model.Partida;
import bo.com.qbit.webapp.service.ProductoRegistration;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "productoController")
@ConversationScoped
public class ProductoController implements Serializable {

	private static final long serialVersionUID = 4656764987882579263L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	private FacesContext facesContext;

	@Inject
	private EntityManager em;

	@Inject
	Conversation conversation;

	@Inject
	private ProductoRegistration productoRegistration;

	@Inject
	private ProductoRepository productoRepository;

	private @Inject PartidaRepository partidaRepository;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	private boolean modificar = false;
	private boolean registrar = false;
	private boolean crear = true;
	
	private String tituloPanel = "Registrar Producto";
	private Producto selectedProducto;
	private Producto newProducto= new Producto();
	private List<Partida> listPartida = new ArrayList<Partida>();

	private List<Producto> listaProducto;

	// @Named provides access the return value via the EL variable name
	// "servicios" in the UI (e.g.
	// Facelets or JSP view)
	@Produces
	@Named
	public List<Producto> getListaProducto() {
		return listaProducto;
	}
	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;
	
	@PostConstruct
	public void initNewProducto() {

		// initConversation();
		beginConversation();

		usuarioSession = sessionMain.getUsuarioLogin().getLogin();
		
		listPartida = partidaRepository.findAllPartidaByID();

		newProducto = new Producto();
		newProducto.setEstado("AC");
		newProducto.setFechaRegistro(new Date());
		newProducto.setUsuarioRegistro(usuarioSession);
		
		selectedProducto = null;

		// tituloPanel
		tituloPanel = "Registrar Producto";

		// traer todos las Productoes ordenados por ID Desc
		listaProducto = productoRepository.traerProductoActivas();
		selectedProducto = null;
		modificar = false;
		registrar = false;
		crear = true;
	}
	
	public void cambiarAspecto(){
		modificar = false;
		registrar = true;
		crear = false;
		
		newProducto = new Producto();
		newProducto.setEstado("AC");
		newProducto.setFechaRegistro(new Date());
		newProducto.setUsuarioRegistro(usuarioSession);
		newProducto.setPartida(new Partida());
	}
	
	public void cambiarAspectoModificar(){
		modificar = true;
		registrar = false;
		crear = false;
		
		newProducto = selectedProducto;
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
	
	
	// SELECCIONAR AUTOCOMPLETES AREA PRODUCTO
	public List<Partida> completePartida(String query) {
		return partidaRepository.findAllPartidaForDescription(query);
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

	public void updatedCantidad() {
		/*
		 * if(!newProducto.isShowCantidad()){ newProducto.setCantidadCaja(0); }
		 */
	}

	// SELECT PRESENTACION CLICK
	public void onRowSelectProductoClick(SelectEvent event) {
		try {
			Producto Producto = (Producto) event.getObject();
			System.out.println("onRowSelectProductoClick  " + Producto.getId());
			selectedProducto = Producto;
			newProducto = em.find(Producto.class, Producto.getId());
			newProducto.setFechaRegistro(new Date());
			newProducto.setUsuarioRegistro(usuarioSession);

			tituloPanel = "Modificar Producto";
			modificar = false;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in onRowSelectProductoClick: "
					+ e.getMessage());
		}
	}

	public void registrarProducto() {
		try {
			System.out.println("Ingreso a registrarProducto: ");
			newProducto.setPrecioUnitario(0);//el precio mediante orden ingreso
			newProducto.setUsuarioRegistro(usuarioSession);
			newProducto.setFechaRegistro(new Date());
			productoRegistration.register(newProducto);
			
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Producto Registrado!", newProducto.getNombre()+"!");
			facesContext.addMessage(null, m);
			
			initNewProducto();
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Error al Registrar.");
			facesContext.addMessage(null, m);
		}
	}

	public void modificarProducto() {
		try {
			System.out.println("Ingreso a modificarProducto: "
					+ newProducto.getId());
			
			newProducto.setUsuarioRegistro(usuarioSession);
			newProducto.setFechaRegistro(new Date());
			productoRegistration.updated(newProducto);
			
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Producto Modificado!", newProducto.getNombre()+"!");
			facesContext.addMessage(null, m);
			initNewProducto();

		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					errorMessage, "Erro al Modificar.");
			facesContext.addMessage(null, m);
		}
	}
	
	public void modificar(){
		System.out.println("Ingreso a modificar");
	}

	public void eliminarProducto() {
		try {
			System.out.println("Ingreso a eliminarProducto: "
					+ newProducto.getId());
			productoRegistration.remover(newProducto);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Producto Eliminado!", newProducto.getNombre()+"!");
			facesContext.addMessage(null, m);
			initNewProducto();

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

	public List<Partida> getListPartida() {
		return listPartida;
	}

	public void setListPartida(List<Partida> listPartida) {
		this.listPartida = listPartida;
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

}
