package bo.com.qbit.webapp.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import bo.com.qbit.webapp.model.Empresa;
import bo.com.qbit.webapp.model.MonedaEmpresa;
import bo.com.qbit.webapp.model.Nivel;
import bo.com.qbit.webapp.model.TipoCuenta;
import bo.com.qbit.webapp.model.Usuario;
import bo.com.qbit.webapp.util.PlanCuentaUtil;

//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class EmpresaRegistration extends DataAccessService<Empresa>{

	Logger log = Logger.getLogger(EmpresaRegistration.class);

	private Empresa empresa;
	private Usuario usuario;

	private List<Nivel> listNivel = new ArrayList<Nivel>();

	private MonedaEmpresa monedaNacional ;
	private MonedaEmpresa monedaExtranjera;
	
	private List<TipoCuenta> listTipoCuenta = new ArrayList<TipoCuenta>();

	private static final String  RELATIVE_WEB_PATH= "/resources/file/PC_01.txt";

	public EmpresaRegistration(){
		super(Empresa.class);
	}

	public void cargarPlanCuentaDesdeArchivo(List<TipoCuenta> listTipoCuenta,List<Nivel> listNivel ,Empresa empresa,Usuario usuario,MonedaEmpresa monedaNacional,MonedaEmpresa monedaExtranjera){
		this.listTipoCuenta = listTipoCuenta ;
		this.listNivel = listNivel; 
		this.monedaNacional = monedaNacional;
		this.monedaExtranjera = monedaExtranjera;
		this.empresa = empresa;
		this.usuario = usuario;
		ServletContext servletContext = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
		String absoluteDiskPath = servletContext.getRealPath(RELATIVE_WEB_PATH);
		
	}



	private List<Integer> listTamanio = new ArrayList<>();

	private void cargarTamanioDigitos(String codigo){
		int anterior = 0;
		for(int i=0;i<codigo.length();i++){
			String letra = String.valueOf(codigo.charAt(i));
			if(letra.equals(".")){
				String numeroString = codigo.substring(anterior, i);
				int numero = 1;
				for(int j=1 ; j < numeroString.length(); j++){
					numero = numero + 1;
				}
				listTamanio.add(numero);
				anterior = i + 1;
			}
		}

		String numeroString = codigo.substring(anterior, codigo.length());
		int numero = 1;
		for(int j=1 ; j < numeroString.length(); j++){
			numero = numero + 1;
		}
		listTamanio.add(numero);
	}



	private String obtenerColumna(int nroColumna, String cadena){
		String outPut = "";
		int anterior= 0; int actual = 0; int contColumn = 0;
		for(int index= 0;index < cadena.length();index++){
			String letra = String.valueOf(cadena.charAt(index));
			if(letra.equals("|")){
				anterior = actual; 	actual = index; contColumn ++;
				if(nroColumna==1){return  cadena.substring(anterior, actual);}
				if(contColumn==nroColumna){	return  cadena.substring(anterior+1, actual);}
			}			
		}
		return outPut;
	}

	private Nivel obtenerNivel(int parmNivel){
		for(Nivel nivel: listNivel){
			if(nivel.getNivel()==parmNivel){
				return nivel;
			}
		}
		return null;
	}

	private TipoCuenta obtenerTipoCuenta(String tipoCuenta){
		for(TipoCuenta tc : listTipoCuenta){
			if(tc.getNombre().equals(tipoCuenta)){
				return tc;
			}
		}
		return null;
	}

	private MonedaEmpresa obtenerMoneda(String moneda){
		MonedaEmpresa output = null;
		switch (moneda) {
		case "NACIONAL":
			output = monedaNacional;
			break;
		case "EXTRANJERA":
			output = monedaExtranjera;
			break;
		default:
			break;
		}
		return output;
	}


}

