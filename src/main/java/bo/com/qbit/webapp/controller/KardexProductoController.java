package bo.com.qbit.webapp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.GestionRepository;
import bo.com.qbit.webapp.data.KardexProductoRepository;
import bo.com.qbit.webapp.data.ProductoRepository;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.KardexProducto;
import bo.com.qbit.webapp.model.Producto;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "kardexProductoController")
@ConversationScoped
public class KardexProductoController implements Serializable {

	private static final long serialVersionUID = 749163787421586877L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	private @Inject KardexProductoRepository kardexProductoRepository;
	private @Inject GestionRepository gesionRepository;
	private @Inject ProductoRepository productoRepository;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	private String nuevaGestion;

	//OBJECT
	private Producto selectedProducto;
	private Gestion selectedGestion;

	//LIST
	private List<KardexProducto> listaKardexProducto = new ArrayList<KardexProducto>();
	private List<Producto> listaProducto = new ArrayList<Producto>();
	private List<Gestion> listGestion = new ArrayList<Gestion>();

	//FILTER

	//SESSION
	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;
	private Gestion gestionLogin;

	@PostConstruct
	public void initNewOrdenIngreso() {

		beginConversation();

		usuarioSession = sessionMain.getUsuarioLoggin().getLogin();
		gestionLogin = sessionMain.getGestionLogin();

		//listaKardexProducto  = kardexProductoRepository.findAllOrderedByID();
		listGestion = gesionRepository.findAll();

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

	public void procesarConsulta(){
		System.out.println("procesarConsulta ");
		if(selectedProducto!= null){
			listaKardexProducto  = kardexProductoRepository.findByProductoAndGestion(selectedProducto,gestionLogin);
			System.out.println("listaKardexProducto "+listaKardexProducto.size());
		}
	}

	// -------- get and set -------
	public List<KardexProducto> getListaKardexProducto() {
		return listaKardexProducto;
	}

	public void setListaKardexProducto(List<KardexProducto> listaKardexProducto) {
		this.listaKardexProducto = listaKardexProducto;
	}

	public Producto getSelectedProducto() {
		return selectedProducto;
	}

	public void setSelectedProducto(Producto selectedProducto) {
		this.selectedProducto = selectedProducto;
	}

	public List<Producto> getListaProducto() {
		return listaProducto;
	}

	public void setListaProducto(List<Producto> listaProducto) {
		this.listaProducto = listaProducto;
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

	public String getNuevaGestion() {
		return nuevaGestion;
	}

	public void setNuevaGestion(String nuevaGestion) {
		this.nuevaGestion = nuevaGestion;
		selectedGestion = findGestionByLocal(nuevaGestion);
	}

}
