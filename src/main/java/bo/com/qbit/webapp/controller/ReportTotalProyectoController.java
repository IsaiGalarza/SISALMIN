package bo.com.qbit.webapp.controller;

import java.io.Serializable;
import java.net.URLEncoder;
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

import bo.com.qbit.webapp.data.DetalleOrdenSalidaRepository;
import bo.com.qbit.webapp.data.GestionRepository;
import bo.com.qbit.webapp.data.OrdenSalidaRepository;
import bo.com.qbit.webapp.data.ProyectoRepository;
import bo.com.qbit.webapp.model.DetalleOrdenSalida;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.OrdenSalida;
import bo.com.qbit.webapp.model.Producto;
import bo.com.qbit.webapp.model.Proyecto;
import bo.com.qbit.webapp.util.FacesUtil;
import bo.com.qbit.webapp.util.Fechas;
import bo.com.qbit.webapp.util.NumberUtil;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "reportTotalProyectoController")
@ConversationScoped
public class ReportTotalProyectoController implements Serializable {

	private static final long serialVersionUID = 1729413085201098608L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	private @Inject DetalleOrdenSalidaRepository detalleOrdenSalidaRepository;
	private @Inject OrdenSalidaRepository ordenSalidaRepository;
	private @Inject GestionRepository gesionRepository;
	private @Inject ProyectoRepository proyectoRepository;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;
	
	@Inject
	private FacesContext facesContext;
	
	private String urlTotalProyecto;
	private String tipoConsulta;
	private String nuevaGestion;
	private double total;
	
	private boolean verReporte ;

	//OBJECT
	private Producto selectedProducto;
	private Gestion selectedGestion;
	private Proyecto selectedProyecto;

	//LIST
	private List<Gestion> listGestion = new ArrayList<Gestion>();
	private List<DetalleOrdenSalida> listaDetalleOrdenSalida = new ArrayList<DetalleOrdenSalida>();
	private List<OrdenSalida> listaOrdenSalida = new ArrayList<OrdenSalida>();
	private List<Proyecto> listaProyecto;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private Gestion gestionLogin;
	private Empresa empresaLogin;
	private String usuarioLogin;
	
	//
	private Date fechaInicial ;
	private Date fechaFinal;

	@PostConstruct
	public void initReporteProyecto() {

		System.out.println("initReporteProyecto()");

		usuarioLogin = sessionMain.getUsuarioLogin().getLogin();
		gestionLogin = sessionMain.getGestionLogin();
		empresaLogin = sessionMain.getEmpresaLogin();

		listGestion = gesionRepository.findAll();
		selectedGestion = listGestion.get(0);
		nuevaGestion = String.valueOf(selectedGestion.getGestion());
		
		listaDetalleOrdenSalida = new ArrayList<DetalleOrdenSalida>();
		listaOrdenSalida = new ArrayList<OrdenSalida>();
		listaProyecto = new ArrayList<Proyecto>();

		tipoConsulta = "T";
		verReporte = false;
		selectedProducto = new Producto();
		selectedProyecto= new Proyecto();
		setTotal(0);
		
		setFechaInicial(new Date());
		setFechaFinal(new Date());

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


	private Gestion findGestionByLocal(String gestion){
		for(Gestion g: listGestion){
			if(g.getGestion() == Integer.valueOf(gestion)){
				return g;
			}
		}
		return null;
	}

	private List<OrdenSalida> listOrdenSalidaAux = new ArrayList<OrdenSalida>();
	public void procesarConsulta(){
		setTotal(0);
		System.out.println("procesarConsulta() inicial="+fechaInicial+" - final="+fechaFinal);
		listaDetalleOrdenSalida = detalleOrdenSalidaRepository.findByFechas(fechaInicial,fechaFinal);
		listaOrdenSalida = new ArrayList<OrdenSalida>();
		listaOrdenSalida = ordenSalidaRepository.findByFechas(fechaInicial, fechaFinal);
		if(listaOrdenSalida.size()==0){
			FacesUtil.infoMessage("VALIDACION", "No se encontraron registros");
			return;
		}else{
			listOrdenSalidaAux = new ArrayList<OrdenSalida>();
			for(OrdenSalida d: listaOrdenSalida){
				addOrdenSalida(d);
			}
			listaOrdenSalida = listOrdenSalidaAux;
		}
		for(OrdenSalida d: listOrdenSalidaAux){
			total = total + d.getTotalImporte();
		}
		//setListaOrdenSalida(ordenSalidaRepository.findByFechas(fechaInicial,fechaFinal));
	}

	private void addOrdenSalida(OrdenSalida ordenSalida){
		for(OrdenSalida orden : listOrdenSalidaAux){
			if(orden.getId() == ordenSalida.getId()){
				double cantidadAnterior = orden.getTotalImporte();
				orden.setTotalImporte(cantidadAnterior+ordenSalida.getTotalImporte());
				return;
			}
		}
		listOrdenSalidaAux.add(ordenSalida);
	}
	
	public void cargarReporte(){
		try {
			if(tipoConsulta.equals("S")){
				if(selectedProyecto.getId()==0){
					FacesUtil.infoMessage("VALIDACION", "Seleccione un proyecto");
					return;
				}
			}
			urlTotalProyecto = loadURL();
			verReporte = true;
		} catch (Exception e) {
			FacesUtil.errorMessage("Proceso Incorrecto.");
		}
	}
	
	public String loadURL(){
		try{
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();  
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
			String urlPDFreporte ="";
			System.out.println("tipoConsulta: "+tipoConsulta);
			if(tipoConsulta.equals("T")){
				urlPDFreporte = urlPath+"ReporteTotalProyecto?pFechaInicio="+Fechas.obtenerFormatoYYYYMMDD(fechaInicial)+"&pFechaFin="+Fechas.obtenerFormatoYYYYMMDD(fechaFinal)+"&pIdProyecto=-1"+"&pNitEmpresa="+empresaLogin.getNIT()+"&pNombreEmpresa="+URLEncoder.encode(empresaLogin.getRazonSocial(),"ISO-8859-1")+"&pIdGestion="+gestionLogin.getId()+"&pUsuario="+URLEncoder.encode(usuarioLogin,"ISO-8859-1");
			}else{
				urlPDFreporte = urlPath+"ReporteTotalProyecto?pFechaInicio="+Fechas.obtenerFormatoYYYYMMDD(fechaInicial)+"&pFechaFin="+Fechas.obtenerFormatoYYYYMMDD(fechaFinal)+"&pIdProyecto="+selectedProyecto.getId()+"&pNitEmpresa="+empresaLogin.getNIT()+"&pNombreEmpresa="+URLEncoder.encode(empresaLogin.getRazonSocial(),"ISO-8859-1")+"&pIdGestion="+gestionLogin.getId()+"&pUsuario="+URLEncoder.encode(usuarioLogin,"ISO-8859-1");
			}
			return urlPDFreporte;
		}catch(Exception e){
			return "error";
		}
	}
	
	// ONCOMPLETETEXT PROYECTO ..........
	public List<Proyecto> completeProyecto(String query) {
		if(NumberUtil.isNumeric(query)){//si es numero
			listaProyecto = proyectoRepository.findAllProyectoForQueryCodigo(query,gestionLogin);
		}else{//es letra		
			String upperQuery = query.toUpperCase();
			listaProyecto = proyectoRepository.findAllProyectoForQueryNombre(upperQuery,gestionLogin);
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

	public Producto getSelectedProducto() {
		return selectedProducto;
	}

	public void setSelectedProducto(Producto selectedProducto) {
		this.selectedProducto = selectedProducto;
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

	public Date getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
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

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getUrlTotalProyecto() {
		return urlTotalProyecto;
	}

	public void setUrlTotalProyecto(String urlTotalProyecto) {
		this.urlTotalProyecto = urlTotalProyecto;
	}

	public boolean isVerReporte() {
		return verReporte;
	}

	public void setVerReporte(boolean verReporte) {
		this.verReporte = verReporte;
	}

	public String getTipoConsulta() {
		return tipoConsulta;
	}

	public void setTipoConsulta(String tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
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

}
