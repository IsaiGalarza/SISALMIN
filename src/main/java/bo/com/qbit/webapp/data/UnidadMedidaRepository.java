package bo.com.qbit.webapp.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import bo.com.qbit.webapp.model.Partida;
import bo.com.qbit.webapp.model.UnidadMedida;

@ApplicationScoped
public class UnidadMedidaRepository {

	@Inject
	private EntityManager em;

	public UnidadMedida findById(int id) {
		return em.find(UnidadMedida.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<UnidadMedida> findAllOrderedByID() {
		String query = "select ser from UnidadMedida ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		System.out.println("Query UnidadMedida: " + query);
		return em.createQuery(query).getResultList();
	}
	
	public UnidadMedida findByNombre(String nombre) {
		try{
			String query = "select em from UnidadMedida em where em.estado='AC' and upper(em.nombre)='"+nombre+"'";
			System.out.println("Query UnidadMedida: " + query);
			return (UnidadMedida) em.createQuery(query).getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<UnidadMedida> findAllActivosOrderedByID() {
		try{
			String query = "select em from UnidadMedida em where em.estado='AC' order by em.nombre asc";
			System.out.println("Query UnidadMedida: " + query);
			return em.createQuery(query).getResultList();
		}catch(Exception e){
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<UnidadMedida> findAllUnidadMedidaForDescription(String criterio) {
		try {//upper(ser.codigo)='"+ codigo+"'"
			String query = "select ser from UnidadMedida ser where upper(ser.nombre) like '%"
					+ criterio + "%' and ser.estado='AC' order by ser.nombre asc";
			System.out.println("Consulta: " + query);
			List<UnidadMedida> listaUnidadMedida = em.createQuery(query).getResultList();
			return listaUnidadMedida;
		} catch (Exception e) {
			System.out.println("Error en findAllUnidadMedidaForDescription: "
					+ e.getMessage());
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<UnidadMedida> findAllByNombre(String criterio) {
		try {//upper(ser.codigo)='"+ codigo+"'"
			String query = "select ser from UnidadMedida ser where upper(ser.nombre) like '%"
					+ criterio + "%' and ser.estado='AC' order by ser.nombre asc";
			System.out.println("Consulta: " + query);
			List<UnidadMedida> listaUnidadMedida = em.createQuery(query).getResultList();
			return listaUnidadMedida;
		} catch (Exception e) {
			System.out.println("Error en findAllUnidadMedidaForDescription: "
					+ e.getMessage());
			return null;
		}
	}

}
