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
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import java.util.HashMap;
import java.util.Map;





//--datasource
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;


@WebServlet("/ReporteOrdenSalida")
public class ReporteOrdenSalida  extends HttpServlet{

	private static final long serialVersionUID = 8446900157389628757L;

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
			Integer pIdOrdenSalida = Integer.parseInt(request.getParameter("pIdOrdenSalida"));
			String  pUsuario = request.getParameter("pUsuario");
			String  pTypeExport = request.getParameter("pTypeExport");

			String realPath = request.getRealPath("/");
			System.out.println("Real Path: "+realPath);

			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
			System.out.println("URL ::::: "+urlPath);

			String rutaReporte = urlPath+"resources/report/orden_salida.jasper";
			System.out.println("rutaReporte: "+rutaReporte);
			
			// create a map of parameters to pass to the report.   
			@SuppressWarnings("rawtypes")
			Map parameters = new HashMap();
			parameters.put("pIdOrdenSalida", pIdOrdenSalida);
			parameters.put("pNombreEmpresa", pNombreEmpresa);
			parameters.put("pNitEmpresa", pNitEmpresa);
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
			
			if (pTypeExport.equals("pdf")) {
				System.out.println("Entro a pdf");
				response.setContentType("application/pdf");
				//response.setHeader("Content-disposition", "attachment; filename=" + jasperPrint2.getName() + ".pdf");
				JasperExportManager.exportReportToPdfStream(jasperPrint2, servletOutputStream);
				//servletOutputStream.flush();
				//servletOutputStream.close();
			} else {
				if (pTypeExport.equals("excel")) {
					System.out.println("Entro a excel");
					response.setContentType("application/xls");
					response.setHeader("Content-disposition", "attachment; filename=" + jasperPrint2.getName() + ".xls");
					JRXlsxExporter docxExporter = new JRXlsxExporter();
					docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint2);
					docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM,servletOutputStream);
					docxExporter.exportReport();
				}
			}

			//save report to path
			//JasperExportManager.exportReportToPdfFile(jasperPrint,"C:/etiquetas/Etiqueta+"+pCodigoPre+"-"+pNombreElaborado+".pdf");
			//response.setContentType("application/pdf");
			//JasperExportManager.exportReportToPdfStream(jasperPrint2,servletOutputStream);

			servletOutputStream.flush();
			servletOutputStream.close();

		} catch (Exception e) {
			// display stack trace in the browser
			e.printStackTrace();
			System.out.println("Error en reporte OrdenIngreso: " + e.getMessage());
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			response.setContentType("text/plain");
			response.getOutputStream().print(stringWriter.toString());			
		} 

	}
}
