package bo.com.qbit.webapp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.AlmacenProductoRepository;
import bo.com.qbit.webapp.data.GestionRepository;
import bo.com.qbit.webapp.data.ProductoRepository;
import bo.com.qbit.webapp.model.AlmacenProducto;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.Producto;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "stockProductoController")
@ConversationScoped
public class StockProductoController implements Serializable {

	private static final long serialVersionUID = 749163787421586877L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	private @Inject ProductoRepository productoRepository;
	private @Inject AlmacenProductoRepository almacenProductoRepository;
	private @Inject GestionRepository gesionRepository;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	@Inject
	private FacesContext facesContext;

	private String tipoConsulta;
	private String nuevaGestion;
	private double totalStockUnificado;

	//OBJECT
	private Producto selectedProducto;
	private Gestion selectedGestion;

	//LIST
	private List<Producto> listaProducto = new ArrayList<Producto>();
	private List<Gestion> listGestion = new ArrayList<Gestion>();
	private List<AlmacenProducto> listaAlmacenProducto = new ArrayList<AlmacenProducto>();

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;

	@PostConstruct
	public void initNewStockProducto() {
		
		System.out.println("initNewStockProducto()");

		beginConversation();

		usuarioSession = sessionMain.getUsuarioLoggin().getLogin();

		listaProducto = productoRepository.traerProductoActivas();
		listaAlmacenProducto = almacenProductoRepository.findProductoConStockOrderedByID();
		listGestion = gesionRepository.findAll();

		selectedProducto = new Producto();
		totalStockUnificado = 0;
		tipoConsulta = "PROVEEDOR";

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

	public List<Producto> completeProducto(String query) {
		listaProducto = productoRepository.findAllProductoForQueryNombre(query);
		return listaProducto;
	}

	public void onRowSelectProductoClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Producto i : listaProducto){
			if(i.getNombre().equals(nombre)){
				setSelectedProducto(i);
				if(tipoConsulta.equals("PRODUCTO")){
					calcularStockUnificado(i);
				}
				return;
			}
		}
	}

	private Gestion findGestionByLocal(String gestion){
		Integer gestionAux = Integer.valueOf(gestion);
		for(Gestion g: listGestion){
			if(g.getGestion() == gestionAux){
				return g;
			}
		}
		return null;
	}

	private double calcularStockUnificado(Producto prod){
		System.out.println("calcularStockUnificado("+prod+")");
		totalStockUnificado = 0;
		List<AlmacenProducto> list = almacenProductoRepository.findAllByProducto(prod);
		for(AlmacenProducto alm : list){
			totalStockUnificado = totalStockUnificado + alm.getStock();
		}
		return 0;
	}

	// -------- get and set -------

	public List<Producto> getListaProducto() {
		return listaProducto;
	}

	public void setListaProducto(List<Producto> listaProducto) {
		this.listaProducto = listaProducto;
	}

	public Producto getSelectedProducto() {
		return selectedProducto;
	}

	public void setSelectedProducto(Producto selectedProducto) {
		this.selectedProducto = selectedProducto;
	}

	public String getTipoConsulta() {
		return tipoConsulta;
	}

	public void setTipoConsulta(String tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
		System.out.println("tipoConsulta = "+tipoConsulta);
	}

	public String getNuevaGestion() {
		return nuevaGestion;
	}

	public void setNuevaGestion(String nuevaGestion) {
		this.nuevaGestion = nuevaGestion;
		setSelectedGestion(findGestionByLocal(nuevaGestion));
	}

	public List<Gestion> getListGestion() {
		return listGestion;
	}

	public void setListGestion(List<Gestion> listGestion) {
		this.listGestion = listGestion;
	}

	public Gestion getSelectedGestion() {
		return selectedGestion;
	}

	public void setSelectedGestion(Gestion selectedGestion) {
		this.selectedGestion = selectedGestion;
	}

	public List<AlmacenProducto> getListaAlmacenProducto() {
		return listaAlmacenProducto;
	}

	public void setListaAlmacenProducto(List<AlmacenProducto> listaAlmacenProducto) {
		this.listaAlmacenProducto = listaAlmacenProducto;
	}

	public double getTotalStockUnificado() {
		return totalStockUnificado;
	}

	public void setTotalStockUnificado(double totalStockUnificado) {
		this.totalStockUnificado = totalStockUnificado;
	}

}
