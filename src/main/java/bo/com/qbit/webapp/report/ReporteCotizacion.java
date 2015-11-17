package bo.com.qbit.webapp.report;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;

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

@WebServlet("/ReporteCotizacion")
public class ReporteCotizacion  extends HttpServlet{

	private static final long serialVersionUID = -6785639296265323093L;

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
        Integer pNumero = Integer.parseInt(request.getParameter("pNumero"));
        Integer pGestion = Integer.parseInt(request.getParameter("pGestion"));
   		Integer pEmpresa = Integer.parseInt(request.getParameter("pEmpresa"));
   		
   		System.out.println("COTIZACION =============>  pNumero: "+pNumero+", pGestion: "+pGestion+" ,pEmpresa: "+pEmpresa);
   			
   		@SuppressWarnings("deprecation")
			String realPath = request.getRealPath("/");
   			
   		String urlPath = request.getRequestURL().toString();
   		urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
   		
   		String rutaReporte = urlPath+"resources/report/cotizacion.jasper";
   		
   		// create a map of parameters to pass to the report.
		Map parameters = new HashMap();
   		parameters.put("pNumero", pNumero);
   		parameters.put("pGestion", pGestion);
   		parameters.put("pEmpresa", pEmpresa);
   		
   		//find file .jasper
		jasperReport = (JasperReport)JRLoader.loadObject (new URL(rutaReporte));

		JasperPrint jasperPrint2 = JasperFillManager.fillReport(jasperReport, parameters, conn);

   		//print
   		response.setContentType("application/pdf");
   		JasperExportManager.exportReportToPdfStream(jasperPrint2,servletOutputStream);

   		servletOutputStream.flush();
   		servletOutputStream.close();
   	
		} catch (Exception e) {
			// display stack trace in the browser
			e.printStackTrace();
			System.out.println("Error en reporte Cotizacion: " + e.getMessage());
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			response.setContentType("text/plain");
			response.getOutputStream().print(stringWriter.toString());			
		} 
		
	}
}
