package bo.com.qbit.webapp.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;

@ManagedBean
public class DashboardController implements Serializable {

	private static final long serialVersionUID = 1L;
	private CartesianChartModel combinedModel;

	
	@PostConstruct
	public void init() {
		createCombinedModel();
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

	

}
