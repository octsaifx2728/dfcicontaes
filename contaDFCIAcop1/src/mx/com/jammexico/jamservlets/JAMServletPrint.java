package mx.com.jammexico.jamservlets;

import ch.ubique.inieditor.IniEditor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

public class JAMServletPrint
  extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  private String VOS_INSTANCIA = "JAMMSGX";
  private static String PATH_REPORTES = "C:\\JAMWebService\\Tomcat 5.5\\webapps\\JAMMexico\\Reportes\\";
  private static String PATH_IMAGES = "C:\\JAMWebService\\Tomcat 5.5\\webapps\\JAMMexico\\Reportes\\Images";
  private static String PATH_PICTURES = "C:\\JAMWebService\\Tomcat 5.5\\webapps\\JAMMexico\\Images\\dbpictures";
  private static String DB_DRIVER_FIREBIRD = "org.firebirdsql.jdbc.FBDriver";
  private static String DB_CONECCION = "jdbc:firebirdsql:localhost/3050:soc";
  private static String DB_JAM_USER = "JAM$_";
  private static String DB_JAM_ROLE = "JAM_SOC";
  private static String DB_SYS_USER = "SYSDBA";
  private static String DB_SYS_PASS = "ChkMts5";
  public static String P_VALOR_REPORAUTO = "JAMAutoPrint.jasper";
  public static final String P_CLAVE_MASTER = "masterreport";
  public static final String P_CLAVE_USRSESS = "usrsession";
  public static final String P_CLAVE_IIDSESS = "idsession";
  public static final String P_CLAVE_FRAME = "callframe";
  public static final String P_CLAVE_MODO = "modeout";
  public static final String P_CLAVE_CONNECTDB = "connectdb";
  public static final String P_CLAVE_MODO_PDF = "0";
  public static final String P_CLAVE_MODO_XLM = "1";
  public static final String P_CLAVE_MODO_XLS = "2";
  public static final String P_CLAVE_MODO_CSV = "3";
  public static final String FLG_PARAM_MASTER = ":m:";
  public static final String FLG_PARAM_SUBREP = ":p:";
  public static final String FLG_PARAM_INT = ":i:";
  public static final String FLG_PARAM_LONG = ":l:";
  public static final String FLG_PARAM_DOUBLE = ":d:";
  public static final String FLG_PARAM_FLOAT = ":f:";
  public static final String FLG_PARAM_STR = ":s:";
  public static final String FLG_PARAM_DATE = ":e:";
  public static final String FLG_PARAM_TIME = ":t:";
  public static final String FLG_PARAM_TIMESTAMP = ":m:";
  public static final String FLG_PARAM_WHERE = ":w:";
  public static final String FLG_PARAM_IGUAL = ":q:";
  public static final String FLG_PARAM_DISTINTO = ":r:";
  public static final String FLG_PARAM_MAYOR = ":z:";
  public static final String FLG_PARAM_MAYOR_IGU = ":y:";
  public static final String FLG_PARAM_MENOR = ":a:";
  public static final String FLG_PARAM_MENOR_IGU = ":b:";
  public static final String FLG_PARAM_LIKE = ":k:";
  private static final int IND_VARIABLE = 0;
  private static final int IND_TIPO = 1;
  private static final int IND_OPERADOR = 2;
  private static final int IND_VALOR = 3;
  private static final int DOGET = 0;
  private static final int DOPOST = 1;
  private HttpSession sess = null;
  private int intEjecutadoVia = 0;
  private String cFileActivo = null;
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException
  {
    this.intEjecutadoVia = 0;
    processRequest(request, response);
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException
  {
    this.intEjecutadoVia = 1;
    processRequest(request, response);
  }
  
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException
  {
    try
    {
      if (this.intEjecutadoVia == 0) {
        processRequestDoGet(request, response);
      } else if (this.intEjecutadoVia == 1) {
        processRequestDoPost(request, response);
      }
    }
    catch (ServletException e)
    {
      throw new ServletException(e.getMessage());
    }
    catch (IOException e)
    {
      throw new ServletException(e.getMessage());
    }
    catch (Exception e)
    {
      throw new ServletException(e.getMessage());
    }
    this.sess.invalidate();
  }
  
  protected void processRequestDoGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    Enumeration<?> eParam = request.getParameterNames();
    int p = 0;
    String[][] arrTemp = new String[50][2];
    while (eParam.hasMoreElements())
    {
      arrTemp[p][0] = eParam.nextElement().toString();
      arrTemp[p][1] = request.getParameter(arrTemp[p][0]);
      p++;
    }
    p--;
    String[][] arrParam = new String[p + 1][2];
    for (int i = p; i >= 0; i--)
    {
      arrParam[(p - i)][0] = arrTemp[i][0];
      arrParam[(p - i)][1] = arrTemp[i][1];
    }
    arrTemp = (String[][])null;
    
    SetupIni(request, response, arrParam);
    JAMOpenDataBase(request, response, arrParam);
  }
  
  protected void processRequestDoPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String[][] arrParam = (String[][])null;
    try
    {
      ObjectInputStream bufferentrada = new ObjectInputStream(request.getInputStream());
      arrParam = (String[][])bufferentrada.readObject();
    }
    catch (Exception e)
    {
      System.out.println("Error al Crear cargar parametros" + e.getMessage());
      sendOldErrorResonse(response, e.getMessage());
    }
    SetupIni(request, response, arrParam);
    JAMOpenDataBase(request, response, arrParam);
    
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("retorno@" + this.cFileActivo);
    out.println("error@null");
    out.flush();
    out.close();
  }
  
  private void JAMOpenDataBase(HttpServletRequest request, HttpServletResponse response, String[][] argParam)
    throws ServletException, IOException
  {
    Statement stmt = null;
    ResultSet rstt = null;
    String cUsuarioSYS = null;
    String cUsuarioDB = null;
    String cPassword = null;
    String cIdMaster = null;
    String cTroncal = null;
    String cOrderDefault = null;
    try
    {
      Class.forName(DB_DRIVER_FIREBIRD);
    }
    catch (ClassNotFoundException e)
    {
      sendOldErrorResonse(response, "Firebird JDBC Driver not found.");
    }
    Connection con = null;
    try
    {
      con = DriverManager.getConnection(DB_CONECCION, DB_SYS_USER, DB_SYS_PASS);
    }
    catch (SQLException e)
    {
      sendOldErrorResonse(response, "Error de conexion: " + e.getMessage());
    }
    try
    {
      stmt = con.createStatement();
      rstt = stmt.executeQuery("SELECT SOCUTIL02_USUARIO FROM SOCUTIL02_TBL_ULTIDS WHERE ID_SOCUTIL02=" + 
        findArray(argParam, "usrsession"));
      while (rstt.next()) {
        cUsuarioDB = rstt.getString("SOCUTIL02_USUARIO");
      }
      if (cUsuarioDB.indexOf(DB_JAM_USER) != -1) {
        cUsuarioSYS = cUsuarioDB.substring(DB_JAM_USER.length(), cUsuarioDB.length());
      }
      rstt = null;
      rstt = stmt.executeQuery("SELECT SOCUSUA02_PASSWORD FROM SOCUSUA02_MAE_USUARIOS WHERE SOCUSUA02_USUARIO='" + 
        cUsuarioSYS + "'");
      while (rstt.next()) {
        cPassword = rstt.getString("SOCUSUA02_PASSWORD");
      }
      if ((cUsuarioDB == null) || (cUsuarioSYS == null) || (cPassword == null))
      {
        System.out.println("Error de Seguridad : No esta autorizado 11");
        sendOldErrorResonse(response, "Error de Seguridad : No esta autorizado 11");
      }
      rstt = null;
      rstt = stmt.executeQuery("SELECT ID_SOCSYST13, SOCSYST13_TRONCAL, SOCSYST13_DEFAULTORDERBY  FROM SOCSYST13_ARB_REPORTES WHERE SOCSYST13_REPORTE='" + 
        findArray(argParam, "masterreport").trim() + "'");
      while (rstt.next())
      {
        cIdMaster = new Integer(rstt.getInt("ID_SOCSYST13")).toString();
        cTroncal = rstt.getString("SOCSYST13_TRONCAL").trim();
        cOrderDefault = rstt.getString("SOCSYST13_DEFAULTORDERBY");
      }
      Properties props = new Properties();
      props.setProperty("user", cUsuarioDB.trim());
      props.setProperty("password", cPassword.trim());
      props.setProperty("roleName", DB_JAM_ROLE);
      
      con.close();
      con = DriverManager.getConnection(DB_CONECCION, props);
      
      this.sess = request.getSession();
      this.sess.setAttribute("loginName", cUsuarioDB + cPassword);
      this.sess.setAttribute("Login", cUsuarioDB);
      this.sess.setAttribute("Password", cPassword);
      this.sess.setAttribute("loginSys", cUsuarioSYS);
      this.sess.setAttribute("UserDb", cUsuarioDB);
      this.sess.setAttribute("IdCabe", "-1");
      this.sess.setAttribute("VarCabe", "");
      this.sess.setAttribute("idmaster", new Integer(cIdMaster));
      this.sess.setAttribute("troncal", cTroncal);
      this.sess.setAttribute("orderdefault", cOrderDefault);
    }
    catch (SQLException sql)
    {
      sendOldErrorResonse(response, "Error de Seguridad al autentificarse: " + sql.getMessage());
    }
    catch (Exception e)
    {
      sendOldErrorResonse(response, "Error de Seguridad al autentificarse: " + e.getMessage());
    }
    JAMOpenReporte(request, response, con, argParam);
  }
  
  private void JAMOpenReporte(HttpServletRequest request, HttpServletResponse response, Connection con, String[][] argParam)
    throws ServletException, IOException
  {
    JasperReport masterReport = CargaReporteFile(argParam, response, "masterreport");
    HashMap<String, Object> parameters = ParametrosRep(argParam, response);
    if (findArray(argParam, "modeout").equalsIgnoreCase("0"))
    {
      OpenPDF(request, 
        response, 
        con, 
        argParam, 
        masterReport, 
        parameters);
    }
    else if (findArray(argParam, "modeout").equalsIgnoreCase("2"))
    {
      OpenXLS(request, 
        response, 
        con, 
        argParam, 
        masterReport, 
        parameters);
    }
    else if (findArray(argParam, "modeout").equalsIgnoreCase("1"))
    {
      OpenHTML(request, 
        response, 
        con, 
        argParam, 
        masterReport, 
        parameters);
    }
    else if (findArray(argParam, "modeout").equalsIgnoreCase("3"))
    {
      OpenCSV(request, 
        response, 
        con, 
        argParam, 
        masterReport, 
        parameters);
    }
    else
    {
      System.out.println("ERROR de salida: No se definio el out");
      sendOldErrorResonse(response, "ERROR de salida: No se definio el out 5");
    }
    try
    {
      Statement stmt = null;
      stmt = con.createStatement();
      stmt.execute("INSERT INTO SOCUTIL01_MOV_LOGS(SOCUTIL01_ACCION,SOCUTIL01_TABLA, SOCUTIL01_ULTID,SOCUTIL01_USUARIO) VALUES ('P','" + 
      
        findArray(argParam, "masterreport") + " /$V/ " + 
        this.sess.getAttribute("VarCabe") + "'," + 
        this.sess.getAttribute("IdCabe") + ",'" + 
        this.sess.getAttribute("UserDb") + "')");
    }
    catch (Exception e)
    {
      System.out.println("Error de Seguridad al registrar la impresion: " + e.getMessage());
      sendOldErrorResonse(response, "Error de Seguridad al registrar la impresion: " + e.getMessage() + "100");
    }
  }
  
  private String findArray(String[][] argArray, String argClave)
  {
    String cCadRetro = "";
    for (int i = 0; i <= argArray.length - 1; i++) {
      if (argArray[i][0].equalsIgnoreCase(argClave))
      {
        cCadRetro = argArray[i][1].toString();
        break;
      }
    }
    return cCadRetro;
  }
  
  private String[][] FiltraParametros(String[][] argArray, String cTipo)
  {
    String[][] arrReto = (String[][])null;
    boolean flagNothing = false;
    arrReto = new String[argArray.length - 6][4];
    
    int p = 0;
    for (int i = 0; i < argArray.length; i++) {
      if (argArray[i][0].startsWith(cTipo))
      {
        arrReto[p][0] = argArray[i][0].substring(9);
        arrReto[p][1] = argArray[i][0].substring(3, 6);
        arrReto[p][2] = argArray[i][0].substring(6, 9);
        arrReto[p][3] = argArray[i][1];
        p++;
        flagNothing = true;
      }
    }
    if (!flagNothing) {
      arrReto = (String[][])null;
    }
    return arrReto;
  }
  
  private HashMap<String, Object> ParametrosRep(String[][] argParam, HttpServletResponse response)
    throws IOException
  {
    HashMap<String, Object> parameters = new HashMap();
    try
    {
      JasperReport tempAuto = (JasperReport)JRLoader.loadObject(PATH_REPORTES + P_VALOR_REPORAUTO);
      
      parameters.put("JAM_FORMA", findArray(argParam, "callframe"));
      parameters.put("JAM_USER", this.sess.getAttribute("loginSys").toString());
      parameters.put("JAM_URL", PATH_REPORTES);
      parameters.put("JAM_URL_IMG", PATH_IMAGES);
      parameters.put("JAM_URL_PIC", PATH_PICTURES);
      parameters.put("JAM_INSTANCIA", this.VOS_INSTANCIA);
      
      parameters.put("JAM_TRONCAL", this.sess.getAttribute("troncal"));
      parameters.put("JAM_IDMASTER", this.sess.getAttribute("idmaster"));
      parameters.put("DSUB_AUTO", tempAuto);
      
      String[][] arrParamMasterReport = FiltraParametros(argParam, ":m:");
      CargoParametros(arrParamMasterReport, parameters, response);
    }
    catch (Exception e)
    {
      System.out.println("Error de conexion AQUI YO: " + e.getMessage());
      sendOldErrorResonse(response, e.getMessage() + "101010");
    }
    return parameters;
  }
  
  private void CargoParametros(String[][] arrParametros, HashMap<String, Object> parameters, HttpServletResponse response)
    throws IOException
  {
    if (arrParametros == null) {
      return;
    }
    boolean logTieneParamWhereConOrderByDefault = false;
    for (int i = 0; i < arrParametros.length; i++)
    {
      if (arrParametros[i][1].toString().equalsIgnoreCase(":w:"))
      {
        String cValor = arrParametros[i][3].toString();
        String cValorNew = "";
        cValorNew = cValor.replaceAll("~", "%");
        
        int intPuntero = cValorNew.toUpperCase().indexOf("ORDER BY");
        if (intPuntero != -1)
        {
          String cValorWhere = cValorNew.substring(0, intPuntero);
          String cValorOrder = cValorNew.substring(intPuntero + 8, cValorNew.length());
          if (this.sess.getAttribute("orderdefault") != null)
          {
            logTieneParamWhereConOrderByDefault = true;
            cValorOrder = this.sess.getAttribute("orderdefault") + "," + cValorOrder;
          }
          parameters.put(arrParametros[i][0], cValorWhere);
          parameters.put("JAMSQLORDER", "ORDER BY " + cValorOrder);
        }
        else
        {
          parameters.put(arrParametros[i][0], cValorNew);
          if (this.sess.getAttribute("orderdefault") != null)
          {
            logTieneParamWhereConOrderByDefault = true;
            parameters.put("JAMSQLORDER", "ORDER BY " + this.sess.getAttribute("orderdefault"));
          }
        }
      }
      else if (arrParametros[i][1].toString().equalsIgnoreCase(":i:"))
      {
        parameters.put(arrParametros[i][0], 
          new Integer(Integer.valueOf(arrParametros[i][3]).intValue()));
      }
      else if (arrParametros[i][1].toString().equalsIgnoreCase(":d:"))
      {
        parameters.put(arrParametros[i][0], 
          new Double(Double.valueOf(arrParametros[i][3]).intValue()));
      }
      else if (arrParametros[i][1].toString().equalsIgnoreCase(":f:"))
      {
        parameters.put(arrParametros[i][0], 
          new Float(Float.valueOf(arrParametros[i][3]).intValue()));
      }
      else if (arrParametros[i][1].toString().equalsIgnoreCase(":s:"))
      {
        parameters.put(arrParametros[i][0], 
          arrParametros[i][3].toString());
      }
      else if (arrParametros[i][1].toString().equalsIgnoreCase(":t:"))
      {
        parameters.put(arrParametros[i][0], 
          new Timer(arrParametros[i][3]));
      }
      else if (arrParametros[i][1].toString().equalsIgnoreCase(":e:"))
      {
        String patron = "dd-MM-yyyy";
        SimpleDateFormat formato = new SimpleDateFormat(patron);
        Date fecha = formato.parse(arrParametros[i][3], new ParsePosition(0));
        parameters.put(arrParametros[i][0], fecha);
      }
      else if (arrParametros[i][1].toString().equalsIgnoreCase(":m:"))
      {
        String patron = "dd-MM-yyyy";
        SimpleDateFormat formato = new SimpleDateFormat(patron);
        Date fecha = formato.parse(arrParametros[i][3], new ParsePosition(0));
        Timestamp tiempo = new Timestamp(fecha.getTime());
        parameters.put(arrParametros[i][0], tiempo);
      }
      else if (arrParametros[i][1].toString().equalsIgnoreCase(":p:"))
      {
        try
        {
          JasperReport tempReport = (JasperReport)JRLoader.loadObject(PATH_REPORTES + arrParametros[i][3]);
          parameters.put(arrParametros[i][0], tempReport);
        }
        catch (Exception yu)
        {
          System.out.println("Error cargando el reporte maestro: " + yu.getMessage());
          sendOldErrorResonse(response, yu.getMessage() + "34");
        }
      }
      if (arrParametros[i][0].toString().startsWith("IDC"))
      {
        this.sess.setAttribute("IdCabe", arrParametros[i][3]);
        this.sess.setAttribute("VarCabe", arrParametros[i][0]);
      }
    }
    if ((!logTieneParamWhereConOrderByDefault) && 
      (this.sess.getAttribute("orderdefault") != null)) {
      parameters.put("JAMSQLORDER", "ORDER BY " + this.sess.getAttribute("orderdefault"));
    }
  }
  
  private JasperReport CargaReporteFile(String[][] argParam, HttpServletResponse response, String argClave)
    throws IOException
  {
    JasperReport retoReport = null;
    try
    {
      if (!findArray(argParam, argClave).equalsIgnoreCase("")) {
        retoReport = (JasperReport)JRLoader.loadObject(PATH_REPORTES + findArray(argParam, argClave));
      }
    }
    catch (JRException e)
    {
      System.out.println("Error cargando el reporte maestro: " + e.getMessage());
      sendOldErrorResonse(response, e.getMessage() + "4");
    }
    return retoReport;
  }
  
  public void sendErrorResonse(HttpServletResponse response, Object objParam)
    throws ServletException
  {}
  
  private void sendOldErrorResonse(HttpServletResponse response, String errorMsg)
    throws IOException
  {
    if (this.intEjecutadoVia == 0)
    {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      out.println("<B>Error: </B>" + errorMsg);
      out.flush();
      out.close();
      throw new IOException(errorMsg);
    }
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("retorno@0");
    out.println("error@" + errorMsg);
    out.flush();
    out.close();
    throw new IOException(errorMsg);
  }
  
  private void SetupIni(HttpServletRequest request, HttpServletResponse response, String[][] argParam)
    throws IOException
  {
    FileInputStream file = new FileInputStream("C:\\JAMWebService\\JAMDBConfig\\JAMsrvprint.ini");
    IniEditor e = new IniEditor();
    e.load(file);
    try
    {
      this.VOS_INSTANCIA = findArray(argParam, "connectdb");
      PATH_REPORTES = e.get(this.VOS_INSTANCIA, "PATH_REPORTES");
      PATH_IMAGES = e.get(this.VOS_INSTANCIA, "PATH_IMAGES");
      PATH_PICTURES = e.get(this.VOS_INSTANCIA, "PATH_PICTURES");
      DB_DRIVER_FIREBIRD = e.get(this.VOS_INSTANCIA, "DB_DRIVER_FIREBIRD");
      DB_CONECCION = e.get(this.VOS_INSTANCIA, "DB_CONECCION");
      DB_JAM_USER = e.get(this.VOS_INSTANCIA, "DB_JAM_USER");
      DB_JAM_ROLE = e.get(this.VOS_INSTANCIA, "DB_JAM_ROLE");
      DB_SYS_USER = e.get(this.VOS_INSTANCIA, "DB_SYS_USER");
      DB_SYS_PASS = e.get(this.VOS_INSTANCIA, "DB_SYS_PASS");
      P_VALOR_REPORAUTO = e.get(this.VOS_INSTANCIA, "P_VALOR_REPORAUTO");
      
      this.sess = request.getSession(true);
      this.sess.setAttribute("loginName", DB_SYS_USER + DB_SYS_PASS);
      this.sess.setAttribute("Login", DB_SYS_USER);
      this.sess.setAttribute("Password", DB_SYS_PASS);
    }
    catch (Exception yu)
    {
      System.out.println("Error cargando el reporte maestro: " + yu.getMessage());
    }
    file.close();
  }
  
  private void OpenPDF(HttpServletRequest request, HttpServletResponse response, Connection con, String[][] argParam, JasperReport argMasterReport, HashMap<String, Object> argParameters)
    throws ServletException, IOException
  {
    this.cFileActivo = ("tmp" + findArray(argParam, "usrsession") + findArray(argParam, "idsession") + ".pdf");
    String pdfFilePath = PATH_REPORTES + this.cFileActivo;
    String jasperFileName = PATH_REPORTES + findArray(argParam, "masterreport");
    
    JAMIReportPDF ic = new JAMIReportPDF(jasperFileName, pdfFilePath, argParameters, con);
    ic.start();
    if (ic.getMensajeError() != null)
    {
      System.out.println("Error en crear el maldito PDF : " + ic.getMensajeError());
      sendOldErrorResonse(response, ic.getMensajeError() + "666");
    }
    if (this.intEjecutadoVia == 1) {
      return;
    }
    File f = new File(pdfFilePath);
    response.setContentType("application/pdf");
    
    InputStream in = new FileInputStream(f);
    ServletOutputStream outs = response.getOutputStream();
    
    int bit = 1024;
    while (bit >= 0)
    {
      bit = in.read();
      outs.write(bit);
    }
    outs.flush();
    outs.close();
    in.close();
  }
  
  private void OpenXLS(HttpServletRequest request, HttpServletResponse response, Connection con, String[][] argParam, JasperReport argMasterReport, HashMap<String, Object> argParameters)
    throws ServletException, IOException
  {
    this.cFileActivo = ("tmp" + findArray(argParam, "usrsession") + findArray(argParam, "idsession") + ".xls");
    String xlsFileName = PATH_REPORTES + this.cFileActivo;
    String jasperFileName = PATH_REPORTES + findArray(argParam, "masterreport");
    
    JAMIReportXLS ic = new JAMIReportXLS(jasperFileName, xlsFileName, argParameters, con);
    ic.start();
    if (ic.getMensajeError() != null)
    {
      System.out.println("Error en crear el maldito XLS : " + ic.getMensajeError());
      sendOldErrorResonse(response, ic.getMensajeError() + "666");
    }
    if (this.intEjecutadoVia == 1) {
      return;
    }
    File f = new File(xlsFileName);
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"" + xlsFileName + "\"");
    
    InputStream in = new FileInputStream(f);
    ServletOutputStream outs = response.getOutputStream();
    
    int bit = 256;
    while (bit >= 0)
    {
      bit = in.read();
      outs.write(bit);
    }
    outs.flush();
    outs.close();
    in.close();
  }
  
  private void OpenCSV(HttpServletRequest request, HttpServletResponse response, Connection con, String[][] argParam, JasperReport argMasterReport, HashMap<String, Object> argParameters)
    throws ServletException, IOException
  {
    this.cFileActivo = ("tmp" + findArray(argParam, "usrsession") + findArray(argParam, "idsession") + ".csv");
    String csvFileName = PATH_REPORTES + this.cFileActivo;
    String jasperFileName = PATH_REPORTES + findArray(argParam, "masterreport");
    
    JAMIReportCSV ic = new JAMIReportCSV(jasperFileName, csvFileName, argParameters, con);
    ic.start();
    if (ic.getMensajeError() != null)
    {
      System.out.println("Error en crear el maldito XLS : " + ic.getMensajeError());
      sendOldErrorResonse(response, ic.getMensajeError() + "666");
    }
    if (this.intEjecutadoVia == 1) {
      return;
    }
    File f = new File(csvFileName);
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"" + csvFileName + "\"");
    
    InputStream in = new FileInputStream(f);
    ServletOutputStream outs = response.getOutputStream();
    
    int bit = 256;
    while (bit >= 0)
    {
      bit = in.read();
      outs.write(bit);
    }
    outs.flush();
    outs.close();
    in.close();
  }
  
  private void OpenHTML(HttpServletRequest request, HttpServletResponse response, Connection con, String[][] argParam, JasperReport argMasterReport, HashMap<String, Object> argParameters)
    throws ServletException, IOException
  {
    try
    {
      this.cFileActivo = ("tmp" + findArray(argParam, "usrsession") + findArray(argParam, "idsession") + ".html");
      JasperPrint jasperPrint = JasperFillManager.fillReport(argMasterReport, argParameters, con);
      if (this.intEjecutadoVia == 1) {
        return;
      }
      JRHtmlExporter exporter = new JRHtmlExporter();
      
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      
      Map<Object, Object> imagesMap = new HashMap();
      request.getSession().setAttribute("IMAGES_MAP", imagesMap);
      
      exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
      exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, out);
      exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, imagesMap);
      exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "image?image=");
      
      exporter.exportReport();
    }
    catch (JRException eG)
    {
      System.out.println("Error al Crear Archivo XLS: " + eG.getMessage());
      sendOldErrorResonse(response, eG.getMessage() + "20");
    }
  }
}
