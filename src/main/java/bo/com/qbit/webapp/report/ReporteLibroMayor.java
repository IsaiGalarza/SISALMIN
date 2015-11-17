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

import org.apache.log4j.Logger;


@WebServlet("/ReporteLibroMayor")
@SuppressWarnings("serial")
public class ReporteLibroMayor  extends HttpServlet{

	private Logger log = Logger.getLogger(this.getClass());

	@SuppressWarnings({ "unchecked", "deprecation" })
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
				log.info("Conexion Exitosa datasource...");
			}else{
				log.info("Error Conexion datasource...");
			}

		} catch (Exception e) {
			log.error("Error al conectar JDBC: "+e.getMessage());
		}
		try {
			String pFechaInicio =  request.getParameter("pFechaInicio");
			String pFechaFin = request.getParameter("pFechaFin");
			Integer pIdGestion = Integer.parseInt(request.getParameter("pIdGestion"));
			Integer pIdEmpresa = Integer.parseInt(request.getParameter("pIdEmpresa"));
			Integer pIdPlanCuenta = Integer.parseInt(request.getParameter("pIdPlanCuenta"));
			String pUsuario = request.getParameter("pUsuario");

			String realPath = request.getRealPath("/");
			log.info("Real Path: "+realPath);

			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
			log.info("URL ::::: "+urlPath);

			String rutaReporte = urlPath+"resources/report/libro_mayor.jasper";
			log.info("rutaReporte: "+rutaReporte);

			String SUBREPORT_DIR = urlPath + "resources/report/";
			log.info("SUBREPORT_DIR: " + SUBREPORT_DIR);
			// create a map of parameters to pass to the report.   
			@SuppressWarnings("rawtypes")
			Map parameters = new HashMap();
			parameters.put("pFechaInicio", pFechaInicio);
			parameters.put("pFechaFin", pFechaFin);
			parameters.put("pIdGestion", pIdGestion);
			parameters.put("pIdEmpresa", pIdEmpresa);
			parameters.put("pUsuario", pUsuario);
			parameters.put("pIdPlanCuenta", pIdPlanCuenta);
			parameters.put("SUBREPORT_DIR", SUBREPORT_DIR);

			log.info("parameters "+parameters.toString());

			//find file .jasper
			jasperReport = (JasperReport)JRLoader.loadObject (new URL(rutaReporte));

			if(jasperReport!=null){
				log.info("jasperReport : "+jasperReport.getName()+" loading.....");
				//log.info("jasperReport query: "+jasperReport.getQuery().getText());
			}

			JasperPrint jasperPrint2 = JasperFillManager.fillReport(jasperReport, parameters, conn);

			if(jasperPrint2!=null){
				log.info("jasperPrint name: "+jasperPrint2.getName());
			}else{
				log.info("jasperPrint null");
			}

			//save report to path
			//JasperExportManager.exportReportToPdfFile(jasperPrint,"C:/etiquetas/Etiqueta+"+pCodigoPre+"-"+pNombreElaborado+".pdf");
			response.setContentType("application/pdf");
			JasperExportManager.exportReportToPdfStream(jasperPrint2,servletOutputStream);

			servletOutputStream.flush();
			servletOutputStream.close();

		} catch (Exception e) {
			// display stack trace in the browser
			e.printStackTrace();
			log.info("Error en reporte LibroDiario: " + e.getMessage());
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			response.setContentType("text/plain");
			response.getOutputStream().print(stringWriter.toString());			
		} 

	}
}

