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
import bo.com.qbit.webapp.util.FacesUtil;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "stockProductoController")
@ConversationScoped
public class StockProductoController implements Serializable {

	private static final long serialVersionUID = -14432996097479924L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	private @Inject ProductoRepository productoRepository;
	private @Inject AlmacenProductoRepository almacenProductoRepository;
	private @Inject GestionRepository gesionRepository;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	private String tipoConsulta;
	private String nuevaGestion;
	private double totalStockUnificado;
	private double precioUnificado;

	//OBJECT
	private Producto selectedProducto;
	private Gestion selectedGestion;

	//LIST
	private List<Producto> listaProducto = new ArrayList<Producto>();
	private List<Gestion> listGestion = new ArrayList<Gestion>();
	private List<AlmacenProducto> listaAlmacenProducto = new ArrayList<AlmacenProducto>();

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	//private Gestion gestionSesion;

	@PostConstruct
	public void initNewStockProducto() {

		System.out.println("initNewStockProducto()");

		//gestionSesion = sessionMain.getGestionLogin();

		listGestion = gesionRepository.findAll2();
		selectedGestion = listGestion.get(0);
		nuevaGestion = String.valueOf(selectedGestion.getGestion());

		selectedProducto = new Producto();
		totalStockUnificado = 0;
		precioUnificado = 0;
		tipoConsulta = "PROVEEDOR";

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
		return "stock_producto.xhtml?faces-redirect=true";
	}

	public List<Producto> completeProducto(String query) {
		String upperQuery = query.toUpperCase();
		listaProducto = productoRepository.findAllProductoForQueryNombre(upperQuery);
		return listaProducto;
	}

	public void onRowSelectProductoClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Producto i : listaProducto){
			if(i.getNombre().equals(nombre)){
				setSelectedProducto(i);
				if(tipoConsulta.equals("PRODUCTO")){
					calcularStockAndPrecioUnificados(i);
				}
				return;
			}
		}
	}

	private Gestion findGestionByLocal(String gestion){
		for(Gestion g: listGestion){
			if(g.getGestion().intValue() == Integer.valueOf(gestion)){
				return g;
			}
		}
		
		return null;
	}

	private void calcularStockAndPrecioUnificados(Producto prod){
		System.out.println("calcularStockUnificado("+prod+")");
		totalStockUnificado = 0;
		precioUnificado = 0;
		List<AlmacenProducto> list = almacenProductoRepository.findAllByProducto(selectedGestion,prod);
		for(AlmacenProducto alm : list){
			totalStockUnificado = totalStockUnificado + alm.getStock();
			precioUnificado = precioUnificado + alm.getPrecioUnitario();
		}
		precioUnificado = list.size()>0? precioUnificado / list.size():0;
	}

	public void procesarConsulta(){
		listaAlmacenProducto = new ArrayList<AlmacenProducto>();
		if(selectedProducto.getId() == 0){
			FacesUtil.infoMessage("ADVERTENCIA", "Seleccione un producto");
			return;
		}else{
			if(tipoConsulta.equals("PRODUCTO")){
				calcularStockAndPrecioUnificados(selectedProducto);
			}else if(tipoConsulta.equals("PROVEEDOR")){
				System.out.println("selectedProducto:"+selectedProducto+" - selectedGestion:"+selectedGestion);
				listaAlmacenProducto  = almacenProductoRepository.findAllByProductoAndGestion(selectedProducto,selectedGestion);
			}
		}
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

	public double getPrecioUnificado() {
		return precioUnificado;
	}

	public void setPrecioUnificado(double precioUnificado) {
		this.precioUnificado = precioUnificado;
	}

}
