package bo.com.qbit.webapp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.AlmacenRepository;
import bo.com.qbit.webapp.data.GestionRepository;
import bo.com.qbit.webapp.data.KardexProductoRepository;
import bo.com.qbit.webapp.data.ProductoRepository;
import bo.com.qbit.webapp.model.Almacen;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.KardexProducto;
import bo.com.qbit.webapp.model.Producto;
import bo.com.qbit.webapp.util.FacesUtil;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "kardexProductoController")
@ConversationScoped
public class KardexProductoController implements Serializable {

	private static final long serialVersionUID = 2039368857381714460L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	private FacesContext facesContext;

	@Inject
	Conversation conversation;

	private @Inject KardexProductoRepository kardexProductoRepository;
	private @Inject GestionRepository gesionRepository;
	private @Inject ProductoRepository productoRepository;
	private @Inject AlmacenRepository almacenRepository;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	private boolean verReport = false;
	private String nuevaGestion;

	//OBJECT
	private Producto selectedProducto;
	private Gestion selectedGestion;
	private Almacen selectedAlmacen;

	//LIST
	private List<KardexProducto> listaKardexProducto = new ArrayList<KardexProducto>();
	private List<Producto> listaProducto = new ArrayList<Producto>();
	private List<Gestion> listGestion = new ArrayList<Gestion>();
	private List<Almacen> listAlmacen = new ArrayList<Almacen>();

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private Gestion gestionLogin;

	private String urlKardexProducto = "";
	private String usuarioSession;
	private Empresa empresaLogin;
	private String nameSelectedAlmacen;

	@PostConstruct
	public void initNewKardexProducto() {

		System.out.println("... initNewKardexProducto ...");
		gestionLogin = sessionMain.getGestionLogin();
		usuarioSession = sessionMain.getUsuarioLogin().getLogin();
		empresaLogin = sessionMain.getEmpresaLogin();

		verReport = false;
		selectedProducto = new Producto();
		selectedAlmacen = new Almacen();
		listGestion = gesionRepository.findAll();
		selectedGestion = listGestion.get(0);
		listaKardexProducto = new ArrayList<KardexProducto>();
		nuevaGestion =String.valueOf(selectedGestion.getGestion());
		listAlmacen = almacenRepository.findAllActivosOrderedByID();
		
		setNameSelectedAlmacen("");
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
		return "kardex_producto.xhtml?faces-redirect=true";
	}

	public List<Producto> completeProducto(String query) {
		listaProducto = productoRepository.findAllProductoForQueryNombre(query);
		return listaProducto;
	}

	public void onRowSelectProductoClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Producto i : listaProducto){
			if(i.getNombre().equals(nombre)){
				this.selectedProducto = i;
				return;
			}
		}
	}

	private Gestion findGestionByLocal(String nuevaGestion){
		for(Gestion g: listGestion){
			if(g.getGestion() == Integer.parseInt(nuevaGestion)){
				return g;
			}
		}
		return null;
	}

	public void procesarConsulta(){
		listaKardexProducto = new ArrayList<KardexProducto>();
		if(selectedProducto.getId() == 0){
			FacesUtil.infoMessage("ADVERTENCIA", "Seleccione un producto");
			return;
		}else{
			System.out.println("almacen="+selectedAlmacen.getNombre());
			if(selectedAlmacen.getNombre().isEmpty() ){
				listaKardexProducto  = kardexProductoRepository.findByProductoAndGestion(selectedProducto,selectedGestion);
			}else{
				listaKardexProducto  = kardexProductoRepository.findByProductoAlmacenAndGestion(selectedProducto,selectedAlmacen,selectedGestion);
			}
			if(listaKardexProducto.size()==0){
				FacesUtil.infoMessage("VALIDACION", "No se encontraron Registros.");
			}
			FacesUtil.resetDataTable("formTableProducto:productoTable");

		}
	}


	public void cargarReporte(){
		try {
			urlKardexProducto = loadURL();
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
			String urlPDFreporte = urlPath+"ReporteKardexProducto?pIdProducto="+selectedProducto.getId()+"&pIdGestion="+selectedGestion.getId()+"&pUsuario="+usuarioSession+"&pNitEmpresa="+empresaLogin.getNIT()+"&pNombreEmpresa="+empresaLogin.getRazonSocial();
			return urlPDFreporte;
		}catch(Exception e){
			return "error";
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
		setSelectedGestion( findGestionByLocal(nuevaGestion));
	}

	public boolean isVerReport() {
		return verReport;
	}

	public void setVerReport(boolean verReport) {
		this.verReport = verReport;
	}

	public String getUrlKardexProducto() {
		return urlKardexProducto;
	}

	public void setUrlKardexProducto(String urlKardexProducto) {
		this.urlKardexProducto = urlKardexProducto;
	}

	public Almacen getSelectedAlmacen() {
		return selectedAlmacen;
	}

	public void setSelectedAlmacen(Almacen selectedAlmacen) {
		this.selectedAlmacen = selectedAlmacen;
	}

	public List<Almacen> getListAlmacen() {
		return listAlmacen;
	}

	public void setListAlmacen(List<Almacen> listAlmacen) {
		this.listAlmacen = listAlmacen;
	}

	public String getNameSelectedAlmacen() {
		return nameSelectedAlmacen;
	}

	public void setNameSelectedAlmacen(String nameSelectedAlmacen) {
		this.nameSelectedAlmacen = nameSelectedAlmacen;
		for(Almacen a : listAlmacen){
			if(a.getNombre().equals(nameSelectedAlmacen)){
				selectedAlmacen = a;				
			}
		}
	}
}
