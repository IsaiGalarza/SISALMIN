package bo.com.qbit.webapp.report;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;

import java.util.HashMap;
import java.util.Map;



//--datasource
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;

@WebServlet("/ReporteComprobante")
public class ReporteComprobante  extends HttpServlet{

	private static final long serialVersionUID = -6785639296265323093L;
	
	@Inject
    private EntityManager em;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		ServletOutputStream servletOutputStream = response.getOutputStream();
		JasperReport jasperReport;
		
		Connection conn = null;
        
        try {
            //---conn datasource-------------------------------
            
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:jboss/datasources/WebAppContabilidadDS");
            conn = ds.getConnection();
            
            //---------------------------------------------

        	
        	if(conn!=null){
        		System.out.println("Conexion Exitosa datasource...");
        	}else{
        		System.out.println("Error Conexion datasource...");
        	}
        	
		} catch (Exception e) {
			System.out.println("Error al conectar JDBC: "+e.getMessage());
		}
        try { 
       	 //CAPTURANDO PARAMETROS
        /*
         * $P{pMes} *
         * $P{pGestion}
 		   $P{pTipoComprobante} *
 		   $P{pSucursal} *
 		   $P{pEmpresa}
         */
        Integer pMes = Integer.parseInt(request.getParameter("pMes"));
        Integer pGestion = Integer.parseInt(request.getParameter("pGestion"));
   		Integer pTipoComprobante = Integer.parseInt(request.getParameter("pTipoComprobante"));
   		Integer pSucursal = Integer.parseInt(request.getParameter("pSucursal"));
   		Integer pEmpresa = Integer.parseInt(request.getParameter("pEmpresa"));
   		Integer pNumeroComprobante = Integer.parseInt(request.getParameter("pNumeroComprobante"));
   		
   		System.out.println("COMPROBANTE =============> pNumeroComprobante:"+pNumeroComprobante+"  pMes: "+pMes+", pGestion: "+pGestion+" ,pTipoComprobante: "+pTipoComprobante+", pSucursal: "+pSucursal+" ,pEmpresa: "+pEmpresa);
   			
   		System.out.println("Conexion em: "+em.isOpen());
   			
   		@SuppressWarnings("deprecation")
			String realPath = request.getRealPath("/");
   		System.out.println("Real Path: "+realPath);

   		// load JasperDesign from XML and compile it into JasperReport
   		System.out.println("Context getServletContext: "+request.getServletContext().getContextPath());
   		System.out.println("Context getServletPath: "+request.getServletPath());
   		System.out.println("Context getSession().getServletContext(): "+request.getSession().getServletContext().getRealPath("/"));
   			
   			
   		String urlPath = request.getRequestURL().toString();
   		urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
   		System.out.println("URL ::::: "+urlPath);
   		
   		String rutaReporte = urlPath+"resources/report/comprobante.jasper";
   		System.out.println("rutaReporte: "+rutaReporte);
   		
   		// create a map of parameters to pass to the report.   
    	//	pFechaIni   pFechaFin pIdTaxista
   		@SuppressWarnings("rawtypes")
		Map parameters = new HashMap();
   		parameters.put("pMes", pMes);
   		parameters.put("pGestion", pGestion);
   		parameters.put("pTipoComprobante",  pTipoComprobante);
   		parameters.put("pSucursal", pSucursal);
   		parameters.put("pEmpresa", pEmpresa);
   		parameters.put("pNumeroComprobante", pNumeroComprobante);
   		
   		
   		
   		System.out.println("parameters "+parameters.toString());
   		
   		//find file .jasper
			jasperReport = (JasperReport)JRLoader.loadObject (new URL(rutaReporte));
			
			if(jasperReport!=null){
				System.out.println("jasperReport name: "+jasperReport.getName());
				System.out.println("jasperReport query: "+jasperReport.getQuery().getText());
			}
			
			JasperPrint jasperPrint2 = JasperFillManager.fillReport(jasperReport, parameters, conn);
			
			if(jasperPrint2!=null){
			System.out.println("jasperPrint name"+jasperPrint2.getName());
			}else{
				System.out.println("jasperPrint null");
			}
   			
   		//save report to path
//   		JasperExportManager.exportReportToPdfFile(jasperPrint,"C:/etiquetas/Etiqueta+"+pCodigoPre+"-"+pNombreElaborado+".pdf");
   		response.setContentType("application/pdf");
   		JasperExportManager.exportReportToPdfStream(jasperPrint2,servletOutputStream);

   		servletOutputStream.flush();
   		servletOutputStream.close();
   	
		} catch (Exception e) {
			// display stack trace in the browser
			e.printStackTrace();
			System.out.println("Error en reporte Comprobante: " + e.getMessage());
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			response.setContentType("text/plain");
			response.getOutputStream().print(stringWriter.toString());			
		} 
		
	}
}
