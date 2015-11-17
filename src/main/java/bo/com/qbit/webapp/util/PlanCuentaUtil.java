package bo.com.qbit.webapp.util;

import java.io.IOException;
import java.util.List;

public class PlanCuentaUtil {

	public static String llenarDelanteConCeroCodificacion(String codigo,int tamanioDigito) throws IOException{
		int tamanioDigitoAux = codigo.length();
		if(tamanioDigitoAux < tamanioDigito){
			for(int i=tamanioDigitoAux; i < tamanioDigito ; i++){
				codigo = "0" + codigo;
			}
		}
		return codigo;
	}

	/**
	 *  llena con cero el codigo, hasta el tamanio total de la codificacion
	 * @param codigo
	 * @param tamanioDigito
	 * @param listTamanio
	 * @return
	 * @throws IOException
	 */
	public static String llenarDespuesConCeroCodificacion(String codigo,int tamanioDigito,List<Integer> listTamanio) throws IOException{
		String codigoAux = PlanCuentaUtil.llenarDelanteConCeroCodificacion(codigo,tamanioDigito);
		int tamanioDigitoAux = codigoAux.length();
		int tamanioDigitoAux2 = obtenerTamanioTotalCodificacion(listTamanio);
		for(int i=tamanioDigitoAux; i <= tamanioDigitoAux2 ; i++){
			codigo = codigo + "0";
		}
		return codigo;
	}

	/**
	 * TAMANIO total de digitos de toda la codificacion
	 * @param listTamanio
	 * @return
	 * @throws IOException
	 */
	public static int obtenerTamanioTotalCodificacion(List<Integer> listTamanio) throws IOException{
		int tam = 0;
		for(Integer i : listTamanio){
			tam = tam + i;
		}
		return tam;
	}

	/**
	 * metodo para niveles superiores al 1
	 * @param parteCodigoAuxiliarPadre
	 * @param codigo
	 * @param tamanioDigito
	 * @param listTamanio
	 * @return
	 * @throws IOException
	 */
	public static String cargarCodificacion(String parteCodigoAuxiliarPadre,String codigo,int tamanioDigito,List<Integer> listTamanio) throws IOException{
		String aux  = "";
		//primero obtener la parte del codigo auxiliar del padre
		//String parteCodigoAuxiliarPadre = obtenerParteCodigoAuxiliar(selectedPlanCuenta.getCodigoAuxiliar(),selectedPlanCuenta.getNivel());
		int tamanioCodigoAuxiliarPadre = parteCodigoAuxiliarPadre.length();
		//lenar con cero por delante al codigo
		codigo =  llenarDelanteConCeroCodificacion(codigo,tamanioDigito);
		//agregar adelante la parte del codigo del padre
		aux = parteCodigoAuxiliarPadre + codigo;
		//tamanio total del padre e hijo
		int tamanioTotalPrevio = tamanioDigito + tamanioCodigoAuxiliarPadre;
		//tamanio total de la codificacion
		int tamanioTotalCod = obtenerTamanioTotalCodificacion(listTamanio);
		for(int i= tamanioTotalPrevio ; i <= tamanioTotalCod ; i++ ){
			aux = aux + "0";
		}

		return aux;
	}
	
	public static int obtenerTamanioHastaNIvel(int nivel,List<Integer> listTamanio){
		int tamanio = 0;
		for(int i = 0; i < nivel ;i++){
			Integer tamAux = listTamanio.get(i);
			tamanio = tamanio + tamAux;
		}
		return tamanio;
	}
}
