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


@WebServlet("/ReporteTotalPartida")
public class ReporteTotalPartida  extends HttpServlet{

	private static final long serialVersionUID = 1031215904122053423L;

	@SuppressWarnings({ "unchecked", "deprecation" })
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ServletOutputStream servletOutputStream = response.getOutputStream();
		JasperReport jasperReport;

		Connection conn = null;

		try {
			//---conn datasource-------------------------------

			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:jboss/datasources/WebAppInventarioDS");
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
			String pNombreEmpresa = request.getParameter("pNombreEmpresa");
			String pNitEmpresa = request.getParameter("pNitEmpresa");
			String pIdPartida = request.getParameter("pIdPartida");
			String pFechaInicio =  request.getParameter("pFechaInicio");
			String pFechaFin = request.getParameter("pFechaFin");
			Integer pIdGestion = Integer.parseInt(request.getParameter("pIdGestion"));
			String  pUsuario = request.getParameter("pUsuario");

			String realPath = request.getRealPath("/");
			System.out.println("Real Path: "+realPath);

			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
			System.out.println("URL ::::: "+urlPath);

			// create a map of parameters to pass to the report.   
			@SuppressWarnings("rawtypes")
			Map parameters = new HashMap();
			String rutaReporte = "";
			String rutaSubReporte = "";
			if(pIdPartida.equals("-1")){
				 rutaReporte = urlPath+"resources/report/totales_partida_todos.jasper";
				rutaSubReporte = urlPath+"resources/report/";
				parameters.put("SUBREPORT_DIR", rutaSubReporte);
			}else{
			 rutaReporte = urlPath+"resources/report/totales_partida_individual.jasper";
			 parameters.put("pIdPartida", new Integer( pIdPartida));
			}
			 
			System.out.println("rutaReporte: "+rutaReporte);
			System.out.println("rutaSubReporte: "+rutaSubReporte);

			String URL_SERVLET_LOGO = urlPath+"ServletImageLogo?id=1&type=EMPRESA";

			parameters.put("pDirPhoto", URL_SERVLET_LOGO);
			parameters.put("pNombreEmpresa", pNombreEmpresa);
			parameters.put("pNitEmpresa", pNitEmpresa);
			parameters.put("pIdGestion", pIdGestion);
			parameters.put("pFechaInicio",  new Integer(pFechaInicio));
			parameters.put("pFechaFin",  new Integer(pFechaFin));
			parameters.put("pUsuario", pUsuario);

			System.out.println("parameters "+parameters.toString());

			//find file .jasper
			jasperReport = (JasperReport)JRLoader.loadObject (new URL(rutaReporte));

			if(jasperReport!=null){
				System.out.println("jasperReport : "+jasperReport.getName()+" loading.....");
				//System.out.println("jasperReport query: "+jasperReport.getQuery().getText());
			}

			JasperPrint jasperPrint2 = JasperFillManager.fillReport(jasperReport, parameters, conn);

			if(jasperPrint2!=null){
				System.out.println("jasperPrint name: "+jasperPrint2.getName());
			}else{
				System.out.println("jasperPrint null");
			}

			//save report to path
			//JasperExportManager.exportReportToPdfFile(jasperPrint,"C:/etiquetas/Etiqueta+"+pCodigoPre+"-"+pNombreElaborado+".pdf");
			response.setContentType("application/pdf");// text/html  application/pdf   application/html
			JasperExportManager.exportReportToPdfStream(jasperPrint2,servletOutputStream);

			servletOutputStream.flush();
			servletOutputStream.close();

		} catch (Exception e) {
			// display stack trace in the browser
			e.printStackTrace();
			System.out.println("Error en reporte Total Partida: " + e.getMessage());
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			response.setContentType("text/plain");
			response.getOutputStream().print(stringWriter.toString());			
		} 

	}
}
