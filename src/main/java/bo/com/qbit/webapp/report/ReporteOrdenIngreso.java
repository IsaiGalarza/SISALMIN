package bo.com.qbit.webapp.report;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

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
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;



//session transaction
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;

import javax.persistence.EntityManager;


@WebServlet("/ReporteOrdenIngreso")
public class ReporteOrdenIngreso  extends HttpServlet{

	private static final long serialVersionUID = -4749080310745237615L;
	
	private @Inject EntityManager em;
	
	public Connection getJavaSqlConnectionFromHibernateSession() {
	    Session session = (org.hibernate.Session) em.getDelegate();
	    SessionImpl sessionImpl = (SessionImpl) session;
	    return sessionImpl.connection();
	}
	
	Connection conn = null;
	long start;

	@SuppressWarnings({ "unchecked", "deprecation" })
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ServletOutputStream servletOutputStream = response.getOutputStream();
		JasperReport jasperReport;

		
		JasperPrint jasperPrint2;

		try {
			 start = System.currentTimeMillis();
			System.out.println("INICIANDO REPORTE... "+start);
			//---conn datasource-------------------------------

			//Context ctx = new InitialContext();
			//DataSource ds = (DataSource) ctx.lookup("java:jboss/datasources/WebAppInventarioDS");
			//conn = ds.getConnection();

			//---------------------------------------------
			
			//---session hiberanate -------
			conn = getJavaSqlConnectionFromHibernateSession();
			
			

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
			Integer pIdOrdenIngreso = Integer.parseInt(request.getParameter("pIdOrdenIngreso"));
			String  pUsuario = request.getParameter("pUsuario");

			String realPath = request.getRealPath("/");
			System.out.println("Real Path: "+realPath);

			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
			System.out.println("URL ::::: "+urlPath);

			String rutaReporte = urlPath+"resources/report/orden_ingreso.jasper";
			System.out.println("rutaReporte: "+rutaReporte);
			
			// create a map of parameters to pass to the report.   
			@SuppressWarnings("rawtypes")
			Map parameters = new HashMap();
			parameters.put("pIdOrdenIngreso", pIdOrdenIngreso);
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

			jasperPrint2 = JasperFillManager.fillReport(jasperReport, parameters, conn);
			System.out.println(" - despues de jasperPrint2: " + (System.currentTimeMillis() - start)); 

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
			System.out.println("Error en reporte OrdenIngreso: " + e.getMessage());
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			response.setContentType("text/plain");
			response.getOutputStream().print(stringWriter.toString());			
		} finally{
			
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
