package bo.com.qbit.webapp.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;

import bo.com.qbit.webapp.data.OrdenIngresoRepository;
import bo.com.qbit.webapp.data.OrdenSalidaRepository;
import bo.com.qbit.webapp.data.OrdenTraspasoRepository;
import bo.com.qbit.webapp.util.SessionMain;

@ManagedBean
public class DashboardController implements Serializable {

	private static final long serialVersionUID = 1L;
	private CartesianChartModel combinedModel;
	
	private @Inject OrdenIngresoRepository ordenIngresoRepository;
	private @Inject OrdenSalidaRepository ordenSalidaRepository;
	private @Inject OrdenTraspasoRepository ordenTraspasoRepository;
	private @Inject SessionMain sessionMain; //variable del login
	
	//contador orden de Ingreso
	private int countOrdenIngresoNuevos;
	private int countOrdenIngresoProcesados;
	private int countOrdenIngresoTotal;
	//contador orden de Salida
	private int countOrdenSalidaNuevos;
	private int countOrdenSalidaProcesados;
	private int countOrdenSalidaTotal;
	//contador orden de traspaso
	private int countOrdenTraspasoNuevos;
	private int countOrdenTraspasoProcesados;
	private int countOrdenTraspasoTotal;
	
	@PostConstruct
	public void init() {
		createCombinedModel();
		
		//actualizar contadores
		actualizarContadoresOrdenIngreso();
		actualizarContadoresOrdenSalida();
		actualizarContadoresOrdenTraspaso();
	}
	
	public CartesianChartModel getCombinedModel() {
        return combinedModel;
    }
     
    private void createCombinedModel() {
        combinedModel = new BarChartModel();
 
        BarChartSeries compra = new BarChartSeries();
        compra.setLabel("Compra");
 
        compra.set("2004", 120);
        compra.set("2005", 100);
        compra.set("2006", 44);
        compra.set("2007", 150);
        compra.set("2008", 25);
 
        LineChartSeries donacion = new LineChartSeries();
        donacion.setLabel("Donacion");
 
        donacion.set("2004", 52);
        donacion.set("2005", 60);
        donacion.set("2006", 110);
        donacion.set("2007", 135);
        donacion.set("2008", 120);
        
        LineChartSeries devolucion = new LineChartSeries();
        devolucion.setLabel("Devolucion");
 
        devolucion.set("2004", 25);
        devolucion.set("2005", 30);
        devolucion.set("2006", 55);
        devolucion.set("2007", 40);
        devolucion.set("2008", 80);
 
        combinedModel.addSeries(compra);
        combinedModel.addSeries(donacion);
        combinedModel.addSeries(devolucion);
         
        combinedModel.setTitle("ORDENES DE INGRESO");
        combinedModel.setLegendPosition("ne");
        combinedModel.setMouseoverHighlight(false);
        combinedModel.setShowDatatip(false);
        combinedModel.setShowPointLabels(true);
        Axis yAxis = combinedModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(200);
    }
    
    private void actualizarContadoresOrdenIngreso(){
    	countOrdenIngresoNuevos = (int) ordenIngresoRepository.contarOrdenesActivas(sessionMain.getGestionLogin());
    	countOrdenIngresoProcesados = (int) ordenIngresoRepository.contarOrdenesProcesadas(sessionMain.getGestionLogin());
    	countOrdenIngresoTotal = (int) ordenIngresoRepository.contarOrdenesTotales(sessionMain.getGestionLogin());
    }
    
    private void actualizarContadoresOrdenSalida(){
    	countOrdenSalidaNuevos = (int) ordenSalidaRepository.contarOrdenesActivas(sessionMain.getGestionLogin());
    	countOrdenSalidaProcesados = (int) ordenSalidaRepository.contarOrdenesProcesadas(sessionMain.getGestionLogin());
    	countOrdenSalidaTotal = (int) ordenSalidaRepository.contarOrdenesTotales(sessionMain.getGestionLogin());
    }
    
    private void actualizarContadoresOrdenTraspaso(){
    	countOrdenTraspasoNuevos =  (int) ordenTraspasoRepository.contarOrdenesActivas(sessionMain.getGestionLogin());
    	countOrdenTraspasoProcesados = (int) ordenTraspasoRepository.contarOrdenesProcesadas(sessionMain.getGestionLogin());
    	countOrdenTraspasoTotal = (int) ordenTraspasoRepository.contarOrdenesTotales(sessionMain.getGestionLogin());
    }

	public OrdenIngresoRepository getOrdenIngresoRepository() {
		return ordenIngresoRepository;
	}

	public void setOrdenIngresoRepository(
			OrdenIngresoRepository ordenIngresoRepository) {
		this.ordenIngresoRepository = ordenIngresoRepository;
	}

	public OrdenSalidaRepository getOrdenSalidaRepository() {
		return ordenSalidaRepository;
	}

	public void setOrdenSalidaRepository(OrdenSalidaRepository ordenSalidaRepository) {
		this.ordenSalidaRepository = ordenSalidaRepository;
	}

	public OrdenTraspasoRepository getOrdenTraspasoRepository() {
		return ordenTraspasoRepository;
	}

	public void setOrdenTraspasoRepository(
			OrdenTraspasoRepository ordenTraspasoRepository) {
		this.ordenTraspasoRepository = ordenTraspasoRepository;
	}

	public int getCountOrdenIngresoNuevos() {
		return countOrdenIngresoNuevos;
	}

	public void setCountOrdenIngresoNuevos(int countOrdenIngresoNuevos) {
		this.countOrdenIngresoNuevos = countOrdenIngresoNuevos;
	}

	public int getCountOrdenIngresoProcesados() {
		return countOrdenIngresoProcesados;
	}

	public void setCountOrdenIngresoProcesados(int countOrdenIngresoProcesados) {
		this.countOrdenIngresoProcesados = countOrdenIngresoProcesados;
	}

	public int getCountOrdenIngresoTotal() {
		return countOrdenIngresoTotal;
	}

	public void setCountOrdenIngresoTotal(int countOrdenIngresoTotal) {
		this.countOrdenIngresoTotal = countOrdenIngresoTotal;
	}

	public int getCountOrdenSalidaNuevos() {
		return countOrdenSalidaNuevos;
	}

	public void setCountOrdenSalidaNuevos(int countOrdenSalidaNuevos) {
		this.countOrdenSalidaNuevos = countOrdenSalidaNuevos;
	}

	public int getCountOrdenSalidaProcesados() {
		return countOrdenSalidaProcesados;
	}

	public void setCountOrdenSalidaProcesados(int countOrdenSalidaProcesados) {
		this.countOrdenSalidaProcesados = countOrdenSalidaProcesados;
	}

	public int getCountOrdenSalidaTotal() {
		return countOrdenSalidaTotal;
	}

	public void setCountOrdenSalidaTotal(int countOdenSalidaTotal) {
		this.countOrdenSalidaTotal = countOdenSalidaTotal;
	}

	public int getCountOrdenTraspasoNuevos() {
		return countOrdenTraspasoNuevos;
	}

	public void setCountOrdenTraspasoNuevos(int countOrdenTraspasoNuevos) {
		this.countOrdenTraspasoNuevos = countOrdenTraspasoNuevos;
	}

	public int getCountOrdenTraspasoProcesados() {
		return countOrdenTraspasoProcesados;
	}

	public void setCountOrdenTraspasoProcesados(int countOrdenTraspasoProcesados) {
		this.countOrdenTraspasoProcesados = countOrdenTraspasoProcesados;
	}

	public int getCountOrdenTraspasoTotal() {
		return countOrdenTraspasoTotal;
	}

	public void setCountOrdenTraspasoTotal(int countOdenTraspasoTotal) {
		this.countOrdenTraspasoTotal = countOdenTraspasoTotal;
	}

	

}
