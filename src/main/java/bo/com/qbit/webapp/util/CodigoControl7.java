package bo.com.qbit.webapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CodigoControl7 {
	
	private String numeroAutorizacion;
    private long numeroFactura;
    private String nitci;
    private Date fechaTransaccion;
    private String fechaTransaccionStr;
    private int monto;
    private String llaveDosificacion;
    
    public static void main(String[] args){
    	try {
    	
    	CodigoControl7 cc = new CodigoControl7();
    	cc.setNumeroAutorizacion("7904006306693");
    	cc.setNumeroFactura(876814);
    	cc.setNitci("1665979");
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    	String dateInString = "2008/05/19";
    	Date date;
		date = sdf.parse(dateInString);
    	cc.setFechaTransaccion(date);
    	//35.958,60
    	cc.setMonto(35959);
    	cc.setLlaveDosificacion("zZ7Z]xssKqkEf_6K9uH(EcV+%x+u[Cca9T%+_$kiLjT8(zr3T9b5Fx2xG-D+_EBS");
    	
    	String codigoControl = cc.obtener();
    	
    	
    	System.out.println("CC: "+codigoControl);
    	} catch (ParseException e) {
			e.printStackTrace();
		}
    }

    public CodigoControl7() {
    }

    /**
     * @return the numeroFactura
     */
    public long getNumeroFactura() {
        return numeroFactura;
    }

    /**
     * @param numeroFactura the numeroFactura to set
     */
    public void setNumeroFactura(long numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    /**
     * @return the nitci
     */
    public String getNitci() {
        return nitci;
    }

    /**
     * @param nitci the nitci to set
     */
    public void setNitci(String nitci) {
        this.nitci = nitci;
    }

    /**
     * @return the fechaTransaccion
     */
    public Date getFechaTransaccion() {
        return fechaTransaccion;
    }

    /**
     * @param fechaTransaccion the fechaTransaccion to set
     */
    public void setFechaTransaccion(Date fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    /**
     * @return the fechaTransaccionStr
     */
    public String getFechaTransaccionStr() {
        Date fecha = this.getFechaTransaccion();
        String formato = "yyyyMMdd";
        SimpleDateFormat sdf =
                new SimpleDateFormat(formato);
        String resul = sdf.format(fecha);
        return resul;
    }

    /**
     * @return the monto
     */
    public int getMonto() {
        return monto;
    }

    /**
     * @param monto the monto to set
     */
    public void setMonto(int monto) {
        this.monto = monto;
    }

    /**
     * @return the llaveDosificacion
     */
    public String getLlaveDosificacion() {
        return llaveDosificacion;
    }

    /**
     * @param llaveDosificacion the llaveDosificacion to set
     */
    public void setLlaveDosificacion(String llaveDosificacion) {
        this.llaveDosificacion = llaveDosificacion;
    }

    /**
     * @return retorna el codigo de control generado para la factura
     */
    public String obtener() {
        // primer paso
        String numAutorizacion = this.getNumeroAutorizacion();
        String numFactura = String.valueOf(this.getNumeroFactura());
        String numNitci = this.getNitci();
        String numFechaTransaccion = this.getFechaTransaccionStr();
        String numMonto = String.valueOf(this.getMonto());
        String numLlaveDosificacion = this.getLlaveDosificacion();

        Verhoeff ver = new Verhoeff();

        // primero numero


        numFactura = numFactura + ver.obtener(numFactura);
        numNitci = numNitci + ver.obtener(numNitci);
        numFechaTransaccion = numFechaTransaccion + ver.obtener(numFechaTransaccion);
        numMonto = numMonto + ver.obtener(numMonto);
        //numLlaveDosificacion = numLlaveDosificacion + ver.obtener(numLlaveDosificacion);

        // segundo numero
        numFactura = numFactura + ver.obtener(numFactura);
        numNitci = numNitci + ver.obtener(numNitci);
        numFechaTransaccion = numFechaTransaccion + ver.obtener(numFechaTransaccion);
        numMonto = numMonto + ver.obtener(numMonto);
        //numLlaveDosificacion = numLlaveDosificacion + ver.obtener(numLlaveDosificacion);

        long sumatoria = Long.parseLong(numFactura) + Long.parseLong(numNitci) + Long.parseLong(numFechaTransaccion) + Long.parseLong(numMonto);
        String sumaStr = String.valueOf(sumatoria);
        // hallamos 5 numero verhoff
        sumaStr = sumaStr + ver.obtener(sumaStr);
        sumaStr = sumaStr + ver.obtener(sumaStr);
        sumaStr = sumaStr + ver.obtener(sumaStr);
        sumaStr = sumaStr + ver.obtener(sumaStr);
        sumaStr = sumaStr + ver.obtener(sumaStr);

        // 5 digitos
        String digitos = sumaStr.substring(sumaStr.length() - 5, sumaStr.length());

        int uno = Integer.parseInt(digitos.substring(0, 1)) + 1;
        int dos = Integer.parseInt(digitos.substring(1, 2)) + 1;
        int tres = Integer.parseInt(digitos.substring(2, 3)) + 1;
        int cuatro = Integer.parseInt(digitos.substring(3, 4)) + 1;
        int cinco = Integer.parseInt(digitos.substring(4, 5)) + 1;

        String cuno = this.getLlaveDosificacion().substring(0, uno);
        String cdos = this.getLlaveDosificacion().substring(uno, uno + dos);
        String ctres = this.getLlaveDosificacion().substring(uno + dos, uno + dos + tres);
        String ccuatro = this.getLlaveDosificacion().substring(uno + dos + tres, uno + dos + tres + cuatro);
        String ccinco = this.getLlaveDosificacion().substring(uno + dos + tres + cuatro, uno + dos + tres + cuatro + cinco);

        cuno = this.getNumeroAutorizacion() + cuno;
        cdos = numFactura + cdos;
        ctres = numNitci + ctres;
        ccuatro = numFechaTransaccion + ccuatro;
        ccinco = numMonto + ccinco;
        // paso 3
        String mensaje = cuno + cdos + ctres + ccuatro + ccinco;
        String llave = this.getLlaveDosificacion() + digitos;
        AllegedRC4 ged = new AllegedRC4();
        String cifrado = ged.encriptaSinguion(mensaje, llave);
        // paso 4 y 5
        int salto = 5;
        int numero0 = 0;
        int numero1 = 0;
        int numero2 = 0;
        int numero3 = 0;
        int numero4 = 0;
        int num = 0;
        for (int u = 0; u < cifrado.length(); u++) {
            if (num == 0) {
                numero0 = this.obtieneAscii(cifrado.toCharArray()[u]) + numero0;
            }
            if (num == 1) {
                numero1 = this.obtieneAscii(cifrado.toCharArray()[u]) + numero1;
            }
            if (num == 2) {
                numero2 = this.obtieneAscii(cifrado.toCharArray()[u]) + numero2;
            }
            if (num == 3) {
                numero3 = this.obtieneAscii(cifrado.toCharArray()[u]) + numero3;
            }
            if (num == 4) {
                numero4 = this.obtieneAscii(cifrado.toCharArray()[u]) + numero4;
            }
            num++;
            if (num == 5) {
                num = 0;
            }
        }
        int total = numero0 + numero1 + numero2 + numero3 + numero4;
        //paso 5

        numero0 = (total * numero0) / uno;
        numero1 = (total * numero1) / dos;
        numero2 = (total * numero2) / tres;
        numero3 = (total * numero3) / cuatro;
        numero4 = (total * numero4) / cinco;

        int nuevoTotal = numero0 + numero1 + numero2 + numero3 + numero4;
        Base64 b = new Base64(nuevoTotal);
        String enbase = b.getResultado();
        //sexto paso
        String nuevaLlave= this.getLlaveDosificacion() + digitos;
        String nuevoMensaje  = enbase;
        AllegedRC4 ge = new AllegedRC4();
        String codigoControl = ge.encripta(nuevoMensaje,nuevaLlave );
        return codigoControl;
    }

    /**
     * @return the numeroAutorizacion
     */
    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    /**
     * @param numeroAutorizacion the numeroAutorizacion to set
     */
    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public int obtieneAscii(char valor) {
        return (int) valor;
    }

	
}
