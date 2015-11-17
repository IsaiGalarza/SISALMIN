package bo.com.qbit.webapp.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.richfaces.cdi.push.Push;

import bo.com.qbit.webapp.util.CodigoControl7;

@Named(value = "certificacionController")
@SuppressWarnings("serial")
@ConversationScoped
public class CertificacionController implements Serializable {

    public static final String PUSH_CDI_TOPIC = "pushCdi";

    @Inject
    @Push(topic = PUSH_CDI_TOPIC)
    Event<String> pushEvent;
    
    private Logger log = Logger.getLogger(this.getClass());
    
    private String llaveDosificacion;
    private String numeroAutorizacion;
    private int numeroFactura;
    private String nitCI;
    private String fechaTransaccion;
    private int monto;
    
    private String codigoControl;
    
    @PostConstruct
    public void initCertificacion() {
    	
    }
    
    public void generarCodigoControlV7(){
    	try {
    		log.info("Certificar Codigo Control... ");
			CodigoControl7 cc = new CodigoControl7();
	    	cc.setNumeroAutorizacion(this.getNumeroAutorizacion());
	    	cc.setNumeroFactura(this.getNumeroFactura());
	    	cc.setNitci(this.getNitCI());
	    	
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    	Date date;
			date = sdf.parse(this.getFechaTransaccion());
	    	cc.setFechaTransaccion(date);
	    	cc.setMonto(this.getMonto());
	    	cc.setLlaveDosificacion(this.getLlaveDosificacion());
			this.setCodigoControl(cc.obtener());
			log.info("Codigo Control V7: "+this.getCodigoControl());
		} catch (Exception e) {
			log.error("Error al generarCodigoControlV7: "+e.getMessage());
			limpiarCampos();
		}
    }
    
    public void limpiarCampos(){
    	llaveDosificacion = null;
        numeroAutorizacion = null;
        numeroFactura = 0;
        nitCI = null;
        fechaTransaccion = null;
        monto = 0;
        codigoControl = null;
    }

	public String getLlaveDosificacion() {
		return llaveDosificacion;
	}

	public void setLlaveDosificacion(String llaveDosificacion) {
		this.llaveDosificacion = llaveDosificacion;
	}

	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

	public int getNumeroFactura() {
		return numeroFactura;
	}

	public void setNumeroFactura(int numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public String getNitCI() {
		return nitCI;
	}

	public void setNitCI(String nitCI) {
		this.nitCI = nitCI;
	}

	public String getFechaTransaccion() {
		return fechaTransaccion;
	}

	public void setFechaTransaccion(String fechaTransaccion) {
		this.fechaTransaccion = fechaTransaccion;
	}

	public int getMonto() {
		return monto;
	}

	public void setMonto(int monto) {
		this.monto = monto;
	}

	public String getCodigoControl() {
		return codigoControl;
	}

	public void setCodigoControl(String codigoControl) {
		this.codigoControl = codigoControl;
	}

}
