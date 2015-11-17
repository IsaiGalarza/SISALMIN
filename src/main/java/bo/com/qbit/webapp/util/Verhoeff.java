package bo.com.qbit.webapp.util;

public class Verhoeff {
	
	private int mul[][];
    private int per[][];
    private int inv[];
    public Verhoeff() {
        int mul[][]={{0,1,2,3,4,5,6,7,8,9},{1,2,3,4,0,6,7,8,9,5},{2,3,4,0,1,7,8,9,5,6},{3,4,0,1,2,8,9,5,6,7},{4,0,1,2,3,9,5,6,7,8},{5,9,8,7,6,0,4,3,2,1},{6,5,9,8,7,1,0,4,3,2},{7,6,5,9,8,2,1,0,4,3},{8,7,6,5,9,3,2,1,0,4},{9,8,7,6,5,4,3,2,1,0}};
        this.mul=mul;
        int per[][]={{0,1,2,3,4,5,6,7,8,9},{1,5,7,6,2,8,3,0,9,4},{5,8,0,3,7,9,6,1,4,2},{8,9,1,6,0,4,3,5,2,7},{9,4,5,3,1,2,6,8,7,0},{4,2,8,6,5,7,3,9,0,1},{2,7,9,3,8,0,6,4,1,5},{7,0,4,6,9,1,3,2,5,8} };
        this.per=per;
        int inv[]={0,4,3,2,1,5,6,7,8,9};
        this.inv=inv;
    }
    public int obtener(String cifra){
        int check=0;
        int i=0;
        String numeroInvertido=invierteCifra(cifra);
        for (int o=0;o<numeroInvertido.length();o++){
            check=mul[check][per[((o+1)%8)][Integer.parseInt(numeroInvertido.substring(o, o+1))]];
        }
        return inv[check];
    }
    public String invierteCifra(String cifra){
        String aux="";
        String este=cifra;
        for(int i=0;i<cifra.length();i++){
            aux=este.substring(0,1)+aux;
            este=este.substring(1, este.length());
        }
        return aux;
    }

	
}	
