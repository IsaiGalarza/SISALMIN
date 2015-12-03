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
	private double countOrdenIngresoNuevos;
	private double countOrdenIngresoProcesados;
	private double countOrdenIngresoTotal;
	//contador orden de Salida
	private double countOrdenSalidaNuevos;
	private double countOrdenSalidaProcesados;
	private double countOrdenSalidaTotal;
	//contador orden de traspaso
	private double countOrdenTraspasoNuevos;
	private double countOrdenTraspasoProcesados;
	private double countOrdenTraspasoTotal;
	
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
    	countOrdenIngresoNuevos = ordenIngresoRepository.contarOrdenesActivas(sessionMain.getGestionLogin());
    	countOrdenIngresoProcesados = ordenIngresoRepository.contarOrdenesProcesadas(sessionMain.getGestionLogin());
    	countOrdenIngresoTotal = ordenIngresoRepository.contarOrdenesTotales(sessionMain.getGestionLogin());
    }
    
    private void actualizarContadoresOrdenSalida(){
    	countOrdenSalidaNuevos = ordenSalidaRepository.contarOrdenesActivas(sessionMain.getGestionLogin());
    	countOrdenSalidaProcesados = ordenSalidaRepository.contarOrdenesProcesadas(sessionMain.getGestionLogin());
    	countOrdenSalidaTotal = ordenSalidaRepository.contarOrdenesTotales(sessionMain.getGestionLogin());
    }
    
    private void actualizarContadoresOrdenTraspaso(){
    	countOrdenTraspasoNuevos =  ordenTraspasoRepository.contarOrdenesActivas(sessionMain.getGestionLogin());
    	countOrdenTraspasoProcesados =  ordenTraspasoRepository.contarOrdenesProcesadas(sessionMain.getGestionLogin());
    	countOrdenTraspasoTotal =  ordenTraspasoRepository.contarOrdenesTotales(sessionMain.getGestionLogin());
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

	public double getCountOrdenIngresoNuevos() {
		return countOrdenIngresoNuevos;
	}

	public void setCountOrdenIngresoNuevos(int countOrdenIngresoNuevos) {
		this.countOrdenIngresoNuevos = countOrdenIngresoNuevos;
	}

	public double getCountOrdenIngresoProcesados() {
		return countOrdenIngresoProcesados;
	}

	public void setCountOrdenIngresoProcesados(double countOrdenIngresoProcesados) {
		this.countOrdenIngresoProcesados = countOrdenIngresoProcesados;
	}

	public double getCountOrdenIngresoTotal() {
		return countOrdenIngresoTotal;
	}

	public void setCountOrdenIngresoTotal(double countOrdenIngresoTotal) {
		this.countOrdenIngresoTotal = countOrdenIngresoTotal;
	}

	public double getCountOrdenSalidaNuevos() {
		return countOrdenSalidaNuevos;
	}

	public void setCountOrdenSalidaNuevos(double countOrdenSalidaNuevos) {
		this.countOrdenSalidaNuevos = countOrdenSalidaNuevos;
	}

	public double getCountOrdenSalidaProcesados() {
		return countOrdenSalidaProcesados;
	}

	public void setCountOrdenSalidaProcesados(double countOrdenSalidaProcesados) {
		this.countOrdenSalidaProcesados = countOrdenSalidaProcesados;
	}

	public double getCountOrdenSalidaTotal() {
		return countOrdenSalidaTotal;
	}

	public void setCountOrdenSalidaTotal(double countOdenSalidaTotal) {
		this.countOrdenSalidaTotal = countOdenSalidaTotal;
	}

	public double getCountOrdenTraspasoNuevos() {
		return countOrdenTraspasoNuevos;
	}

	public void setCountOrdenTraspasoNuevos(double countOrdenTraspasoNuevos) {
		this.countOrdenTraspasoNuevos = countOrdenTraspasoNuevos;
	}

	public double getCountOrdenTraspasoProcesados() {
		return countOrdenTraspasoProcesados;
	}

	public void setCountOrdenTraspasoProcesados(double countOrdenTraspasoProcesados) {
		this.countOrdenTraspasoProcesados = countOrdenTraspasoProcesados;
	}

	public double getCountOrdenTraspasoTotal() {
		return countOrdenTraspasoTotal;
	}

	public void setCountOrdenTraspasoTotal(double countOdenTraspasoTotal) {
		this.countOrdenTraspasoTotal = countOdenTraspasoTotal;
	}

	

}
