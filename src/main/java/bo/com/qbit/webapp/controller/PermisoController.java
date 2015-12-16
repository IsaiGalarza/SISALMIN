package bo.com.qbit.webapp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.data.DetallePaginaRepository;
import bo.com.qbit.webapp.data.PermisoRepository;
import bo.com.qbit.webapp.data.RolesRepository;
import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.Gestion;
import bo.com.qbit.webapp.model.security.Accion;
import bo.com.qbit.webapp.model.security.DetallePagina;
import bo.com.qbit.webapp.model.security.Modulo;
import bo.com.qbit.webapp.model.security.Pagina;
import bo.com.qbit.webapp.model.security.Permiso;
import bo.com.qbit.webapp.model.security.Roles;
import bo.com.qbit.webapp.service.PermisoRegistration;
import bo.com.qbit.webapp.util.EDAccion;
import bo.com.qbit.webapp.util.EDPermisoV1;
import bo.com.qbit.webapp.util.FacesUtil;
import bo.com.qbit.webapp.util.SessionMain;

@Named(value = "permisoController")
@ConversationScoped
public class PermisoController implements Serializable {

	private static final long serialVersionUID = -7144100519418489169L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;


	private @Inject PermisoRepository permisoRepository;
	private @Inject RolesRepository rolesRepository;
	private @Inject DetallePaginaRepository detallePaginaRepository;

	private @Inject PermisoRegistration permisoRegistration;

	//private Logger log = Logger.getLogger(this.getClass());

	//login
	private String nombreUsuario;
	private @Inject SessionMain sessionMain; //variable del login
	private Empresa empresaLogin;
	private Gestion gestionLogin;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	private String nombreRol;

	//------------------------------------------------------------------
	private List<Permiso> listPermisoV1 = new ArrayList<Permiso>();
	private Roles selectedRol;
	private List<Roles> listRol = new ArrayList<Roles>();
	private List<DetallePagina> listDetallePagina = new ArrayList<DetallePagina>();
	//------------------------------------------------------------------
	private List<Modulo> listModulo = new ArrayList<Modulo>();
	private List<Pagina> listPagina = new ArrayList<Pagina>();
	private List<EDAccion> listAccion = new ArrayList<EDAccion>();

	//tree
	private TreeNode root;
	private TreeNode[] selectedNodes2;

	@PostConstruct
	public void initNewPermiso() {

		System.out.println(" init new initNewPermiso");
		nombreUsuario = sessionMain.getUsuarioLogin().getLogin();
		empresaLogin = sessionMain.getEmpresaLogin();
		gestionLogin = sessionMain.getGestionLogin();

		listDetallePagina = detallePaginaRepository.findAll();
		listRol = rolesRepository.findAllOrderByAsc();
		loadDefault();
	}

	private void loadDefault(){
		nombreRol = listRol.get(0).getNombre();
		selectedRol = listRol.get(0);
		listPermisoV1 = permisoRepository.findByRol(selectedRol);
		cargarPermiso();
		cargarNodos();
	}

	//------------------------------------------------------------------

	private Roles obtenerRolByLocal(String nombreRol){
		for(Roles r: listRol){
			if(r.getNombre().equals(nombreRol)){
				return r;
			}
		}
		return null; 
	}

	/**
	 * 1.-  obtener los modulos
	 * 2.-  obtener los submodulos
	 * 3.-  obtener las paginas
	 * 4.-  obtener las acciones
	 */
	public void cargarNodos(){
		List<Modulo> listModuloRoot = findModuloRootByLocal();
		loadTreeNode( listModuloRoot);
	}

	private void cargarPermiso(){
		listModulo = new ArrayList<Modulo>();
		listPagina = new ArrayList<Pagina>();
		listAccion = new ArrayList<EDAccion>();

		listModulo = findModuloByLocal();
		listPagina = findPaginaByLocal();
		listAccion = findEDAccionByLocal();
	}

	private List<Modulo> findModuloRootByLocal(){
		List<Modulo> list = new ArrayList<Modulo>();
		for(DetallePagina dp : listDetallePagina){
			Modulo m = dp.getPagina().getModulo();
			if(! list.contains(m)){
				list.add(m);
			}
		}
		return list;
	}

	private List<Modulo> findModuloByLocal(){
		List<Modulo> list = new ArrayList<Modulo>();
		for(Permiso per : listPermisoV1){//listPermisoV1
			DetallePagina dp = per.getDetallePagina();
			Modulo m = dp.getPagina().getModulo();
			if(! list.contains(m)){
				list.add(m);
			}
		}
		return list;
	}

	private List<Pagina> findPaginaByLocal(){
		List<Pagina> list = new ArrayList<Pagina>();
		for(Permiso per : listPermisoV1){
			DetallePagina dp = per.getDetallePagina();
			Pagina p = dp.getPagina();
			if(! list.contains(p)){
				list.add(p);
			}
		}
		return list;
	}

	private boolean verificarExisteAccionPagina(Pagina p,Accion ac){
		for(EDAccion eda: listAccion){
			if(eda.getAccion().equals(ac) && eda.getPagina().equals(p)){
				return true;
			}
		}
		return false;
	}

	private List<EDAccion> findEDAccionByLocal(){
		List<EDAccion> list = new ArrayList<EDAccion>();
		for(Permiso per : listPermisoV1){
			DetallePagina dp = per.getDetallePagina();
			Pagina p = dp.getPagina();
			Accion a = dp.getAccion();
			if( ! verificarExisteAccionPagina(p,a)){
				EDAccion eda = new EDAccion();
				eda.setAccion(a);
				eda.setPagina(p);
				list.add(eda);
			}
		}
		return list;
	}

	private void loadTreeNode(List<Modulo> listModulo){
		root = new DefaultTreeNode(new EDPermisoV1("root", "", "",null), null);
		for(Modulo mo : listModulo){
			TreeNode tn = new DefaultTreeNode(new EDPermisoV1(mo.getNombre(),"ICON","MODULO",(Modulo)mo),root);
			tn.setExpanded(true);
			tn.setSelected(tienePermisoModulo(mo));
			List<Pagina> listPagina = new ArrayList<Pagina>();
			listPagina = obtenerPaginasByModulo(mo);
			for(Pagina pa: listPagina){
				TreeNode tn2 = new DefaultTreeNode("2",new EDPermisoV1(pa.getNombre(),"ICON","PAGINA",(Pagina)pa),tn);
				tn2.setExpanded(true);
				tn2.setSelected(tienePermisoPagina(pa));
				List<EDAccion> listAccion = obtenerEDAccionByPagina(pa);
				for(EDAccion ac: listAccion ){
					TreeNode tn3 = new DefaultTreeNode("3",new EDPermisoV1(ac.getAccion().getNombre(),"ICON","ACCION",(EDAccion)ac),tn2);
					tn3.setSelected(tienePermisoAccion(ac));
				}
			}
		}
	}

	private boolean tienePermisoModulo(Modulo m){
		for(Modulo modAux : listModulo){
			if(modAux.equals(m)){
				return true;
			}
		}
		return false;
	}

	private boolean tienePermisoPagina(Pagina p){
		for(Pagina pagAux : listPagina){
			if(pagAux.equals(p)){
				return true;
			}
		}
		return false;
	}

	private boolean tienePermisoAccion(EDAccion eda){
		Pagina p = eda.getPagina();
		Accion a = eda.getAccion();
		for(EDAccion per : listAccion ){
			Pagina pagAux = per.getPagina();
			Accion accAux = per.getAccion();
			if(pagAux.equals(p) && accAux.equals(a)){
				return true;
			}
		}
		return false;
	}

	private List<Pagina> obtenerPaginasByModulo(Modulo modulo){
		List<Pagina> list = new ArrayList<Pagina>();
		for(DetallePagina dp : listDetallePagina){
			Pagina pa = dp.getPagina(); 
			if(pa.getModulo().equals(modulo)){
				if( ! list.contains(pa)){
					list.add(pa);
				}
			}
		}
		return list;
	}

	private List<EDAccion> obtenerEDAccionByPagina(Pagina pagina){
		List<EDAccion> list = new ArrayList<EDAccion>();
		for(DetallePagina dp : listDetallePagina){
			if(dp.getPagina().equals(pagina)){
				EDAccion ed = new EDAccion();
				ed.setAccion(dp.getAccion());
				ed.setPagina(dp.getPagina());
				if( ! list.contains(ed)){
					list.add(ed);
				}
			}
		}
		return list;
	}

	public void limpiarPermisos(){
		listModulo = new ArrayList<Modulo>();
		listPagina = new ArrayList<Pagina>();
		listAccion = new ArrayList<EDAccion>();
		cargarNodos();
	}

	public void onNodeSelect(NodeSelectEvent event){
		EDPermisoV1 e = (EDPermisoV1)event.getTreeNode().getData();
		System.out.println("onNodeSelect : "+e.getNombre() +" tipo : "+e.getTipo());
		switch (e.getTipo()) {
		case "MODULO":
			Modulo m = (Modulo)e.getObject();
			if( ! listModulo.contains(m)){
				listModulo.add(m);
			}
			break;
		case "PAGINA":
			Pagina p = (Pagina)e.getObject();
			agregarPermiso(p);
			break;
		case "ACCION":
			EDAccion eda = (EDAccion)e.getObject();
			if( ! listAccion.contains(eda)){
				listAccion.add(eda);
			}
			agregarPermiso(eda.getPagina());
			break;

		default:
			break;
		}
		cargarNodos();
	}

	public void onNodeUnselect(NodeUnselectEvent event){
		EDPermisoV1 e = (EDPermisoV1)event.getTreeNode().getData();
		System.out.println("onNodeUnselect : "+e.getNombre() +" tipo : "+e.getTipo());
		switch (e.getTipo()) {
		case "MODULO":
			Modulo m = (Modulo)e.getObject();
			this.listModulo.remove(m);
			//obtener lista de paginas hijas de este modulo
			List<Pagina> listPaginasHijas = new ArrayList<Pagina>();
			listPaginasHijas = obtenerPaginasByModulo(m);
			//eliminar todas las paginas hijas
			for(Pagina p: listPaginasHijas){
				this.listPagina.remove(p);
				//obtener lista de acciones de cada una de las paginas
				List<EDAccion> listAccionHijas = obtenerEDAccionByPagina(p);
				// eliminar todas las acciones
				for(EDAccion eda : listAccionHijas){
					if(listPaginasHijas.contains(eda.getPagina())){
						this.listAccion.remove(eda);
					}
				}
			}
			break;
		case "PAGINA":
			Pagina p = (Pagina)e.getObject();
			this.listPagina.remove(p);
			List<EDAccion> listAccionHijas = obtenerEDAccionByPagina(p);
			//eliminar las acciones asociadas a esta pagina
			for(EDAccion eda : listAccionHijas){
				if(p.equals(eda.getPagina())){
					this.listAccion.remove(eda);
				}
			}			
			// obtener moudulo padre de la pagina
			Modulo modulo = p.getModulo();		
			//verificar si el modulo que pertenece esta pagina tiene otros hijos, sino eliminar modulo
			List<Pagina> listPaginasHijas2 = obtenerPaginasByModulo(modulo);
			if(listPaginasHijas2.size()==0){
				this.listModulo.remove(modulo);
			}
			break;
		case "ACCION":
			EDAccion eda = (EDAccion)e.getObject();
			this.listAccion.remove(eda);
			Pagina p3 = eda.getPagina();
			Modulo modulo3 = p3.getModulo();
			List<EDAccion> listEda3 = obtenerEDAccionByPagina(p3);
			//si ya no hay acciones verificar, si era la utima de esta pagina y de ese modulo
			if(listEda3.size() == 0){
				//eliminar la pagina
				this.listPagina.remove(p3);
				//obtener las paginas de ese modulo, y verificar si ya no hay paginas eliminar modulo
				List<Pagina> listPaginasHijas3 = obtenerPaginasByModulo(modulo3);
				if(listPaginasHijas3.size()==0){
					//si ya no hay paginas eliminar el modulo
					this.listModulo.remove(modulo3);
				}
			}
			break;
		default:
			break;
		}
		cargarNodos();
		System.out.println("listModulo = "+listModulo.size());
		System.out.println("listPagina = "+listPagina.size());
		System.out.println("listAccion = "+listAccion.size());
	}

	private void agregarPermiso(Pagina p){
		if( ! listPagina.contains(p)){
			listPagina.add(p);
		}
		if( ! listModulo.contains(p.getModulo())){
			listModulo.add(p.getModulo());
		}
	}

	//------------------------------------------------------------------

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

	public void registrarPermisos() {
		try {
			Date fechaActual = new Date();
			System.out.println("Ingreso a registrarPrivilegio 1: ");
			for(Permiso perm : listPermisoV1){
				perm.setFechaModificacion(fechaActual);
				perm.setEstado("RM");
				permisoRegistration.update(perm);
			}
			System.out.println("Ingreso a registrarPrivilegio 2");
			for(EDAccion eda : listAccion){
				Pagina pag = eda.getPagina();
				System.out.println("Pagina "+pag.getId());
				Accion ac = eda.getAccion();
				System.out.println("Accion "+ac.getId());
				DetallePagina detalle = detallePaginaRepository.findAccionByPaginaAndAccion(pag,ac);
				System.out.println("detalle "+detalle);
				Permiso permiso = new Permiso();
				permiso.setDetallePagina(detalle);
				permiso.setEstado("AC");
				permiso.setFechaRegistro(fechaActual);
				permiso.setRoles(selectedRol);
				permiso.setUsuarioRegistro(nombreUsuario);
				permisoRegistration.create(permiso);
			}
			FacesUtil.infoMessage("Permisos Registrados!", " ");
		} catch (Exception e) {
			System.out.println("ERROR "+e.getMessage());
			FacesUtil.errorMessage("Error al registrar!");
		}
	}

	// -----------  get and set ------------

	public String getNombreRol() {
		return nombreRol;
	}

	public void setNombreRol(String nombreRol) {
		this.nombreRol = nombreRol;
		selectedRol = obtenerRolByLocal( nombreRol);
		listPermisoV1 = new ArrayList<Permiso>();
		listPermisoV1 = permisoRepository.findByRol(selectedRol);
		System.out.println("setNombreRol:"+nombreRol+" - listPermisoV1:"+listPermisoV1.size());
		cargarPermiso();
		cargarNodos();
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode[] getSelectedNodes2() {
		return selectedNodes2;
	}

	public void setSelectedNodes2(TreeNode[] selectedNodes2) {
		this.selectedNodes2 = selectedNodes2;
	}

	public List<Permiso> getListPermisoV1() {
		return listPermisoV1;
	}

	public void setListPermisoV1(List<Permiso> listPermisoV1) {
		this.listPermisoV1 = listPermisoV1;
	}

	public Roles getSelectedRol() {
		return selectedRol;
	}

	public void setSelectedRol(Roles selectedRol) {
		this.selectedRol = selectedRol;
	}

	public List<Roles> getListRol() {
		return listRol;
	}

	public void setListRol(List<Roles> listRol) {
		this.listRol = listRol;
	}

	public List<DetallePagina> getListDetallePagina() {
		return listDetallePagina;
	}

	public void setListDetallePagina(List<DetallePagina> listDetallePagina) {
		this.listDetallePagina = listDetallePagina;
	}

}
