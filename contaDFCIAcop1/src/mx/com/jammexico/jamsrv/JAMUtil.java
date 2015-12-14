package mx.com.jammexico.jamsrv;

import ch.ubique.inieditor.IniEditor;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import mx.com.jammexico.jamcomponents.JAMDate;
import mx.com.jammexico.jamcomponents.JAMLibKernel;
import mx.com.jammexico.jamcomponents.jammail.JAMMail;
import mx.com.jammexico.jamcomponents.visual.JAMAreaTexto;
import mx.com.jammexico.jamcomponents.visual.JAMInputNumber;
import mx.com.jammexico.jamcomponents.visual.JAMLabel;
import mx.com.jammexico.jamdb.JAMClienteDB;

public abstract class JAMUtil
{
  private static String dtabasedateformat = "dd/mm/yyyy hh24:mi:ss";
  private static final String dtabasedateformatFireBird = "dd/mm/yyyy hh24:mi:ss";
  private static final String dtabasedateformatMysql = "%d/%m/%Y %H:%i:%s";
  private static final String dtabasedateformatOralce = "%d/%m/%Y %H:%i:%s";
  private static final String dtabasedateformatSqlServer = "%d/%m/%Y %H:%i:%s";
  private static String fulldateformat = "dd/MM/yyyy HH:mm:ss";
  private static SimpleDateFormat fulldateformatter = new SimpleDateFormat(fulldateformat);
  private static String shortdateformat = "dd/MM/yyyy";
  private static SimpleDateFormat shortdateformatter = new SimpleDateFormat(shortdateformat);
  public static final String cTituloShowDialog = "JAM V2.01 ";
  private static Date initialTime = new Date();
  public static final String JAM_WEB_CONFIG = "C:\\JAMWebService\\JAMDBConfig\\";
  public static final String JAMURL_JAR_IMAGES = "/mx/com/jammexico/jamcomponents/jamimages/";
  public static final String JAMURL_JAR_IMAGES_MEDICAL = "/mx/com/jammexico/jammedicalnet2/images/";
  public static final String JAM_PATH_INI_CONNECT = "C:\\JAMWebService\\JAMDBConfig\\conections.ini";
  public static final String JAM_PATH_INI_CONNECT_WS = "C:\\JAMWebService\\JAMDBConfig\\webservices.ini";
  public static final String JAM_PATH_INI_PRINTER = "C:\\JAMWebService\\JAMDBConfig\\JAMsrvprint.ini";
  public static final String JAM_PATH_INI_COPYINSTANCE = "C:\\JAMWebService\\JAMDBConfig\\autoinstancias.ini";
  public static final int ARCH_NONE = -1;
  public static final int ARCH_IMAGES = 0;
  public static final int ARCH_FILES = 1;
  public static final int ARCH_FILES_XML = 2;
  private static JAMInputNumber objNumTmp = null;
  
  public static void main(String[] arg) {}
  
  public static String getDatabaseDateFormat()
  {
    return dtabasedateformat;
  }
  
  public static String toDataBaseDate(String strdate)
    throws Exception
  {
    return toDataBaseDate(strdate, "La Fecha");
  }
  
  public static String toDataBaseDate(String strdate, String msg)
    throws Exception
  {
    DateValidation(strdate, msg);
    return "To_Date ('" + strdate + "' , '" + dtabasedateformat + "')";
  }
  
  public static void DateValidation(String strdate)
    throws Exception
  {
    DateValidation(strdate, "La Fecha");
  }
  
  public static void DateValidation(String strdate, String msg)
    throws Exception
  {
    try
    {
      Timestamp t = new Timestamp(getFullDateFormatter().parse(strdate).getTime());
      if (getFullDateFormatter().format(t).compareToIgnoreCase(strdate) != 0) {
        throw new Exception(msg + "debe estar en el formato : " + getFullDateFormat());
      }
    }
    catch (Exception e)
    {
      Timestamp t = new Timestamp(getShortDateFormatter().parse(strdate).getTime());
      if (getShortDateFormatter().format(t).compareToIgnoreCase(strdate) != 0) {
        throw new Exception(msg + "debe estar en el formato : " + getShortDateFormat());
      }
    }
  }
  
  public static String getDatabaseDateFormat(String dbname)
  {
    dtabasedateformat = "";
    if (dbname.toLowerCase().contains("oracle")) {
      dtabasedateformat = "%d/%m/%Y %H:%i:%s";
    } else if (dbname.toLowerCase().contains("mysql")) {
      dtabasedateformat = "%d/%m/%Y %H:%i:%s";
    } else if (dbname.toLowerCase().contains("firebird")) {
      dtabasedateformat = "dd/mm/yyyy hh24:mi:ss";
    } else if (dbname.toLowerCase().contains("microsoft")) {
      dtabasedateformat = "%d/%m/%Y %H:%i:%s";
    }
    return dtabasedateformat;
  }
  
  public static String getFullDateFormat()
  {
    return fulldateformat;
  }
  
  public static String getCrlf()
  {
    char[] a = { '\r', '\n' };
    return new String(a);
  }
  
  public static String getTab()
  {
    char[] a = { '\t' };
    return new String(a);
  }
  
  public static String getComillas()
  {
    return "\"";
  }
  
  public static String getTab(int argVeces)
  {
    String cRetorna = "";
    char[] a = { '\t' };
    for (int i = 0; i < argVeces; i++) {
      cRetorna = cRetorna + new String(a);
    }
    return cRetorna;
  }
  
  public static String getSpace(int argVeces)
  {
    String cRetorna = "";
    for (int i = 0; i < argVeces; i++) {
      cRetorna = cRetorna + " ";
    }
    return cRetorna;
  }
  
  public static String getStuff(String argCadena, String argCaracter, int argVeces)
  {
    String cRetorna = "";
    for (int i = 0; i < argVeces; i++) {
      cRetorna = cRetorna + argCaracter;
    }
    return argCadena.concat(cRetorna);
  }
  
  public static boolean showDialogOkCancel(String mensaje)
  {
    int reto = JOptionPane.showConfirmDialog(null, mensaje, "JAM V2.01 ", 
      2, 
      3);
    if (reto == 0) {
      return true;
    }
    return false;
  }
  
  public static boolean showDialogOkCancel(String mensaje, Component parentComponent)
  {
    int reto = JOptionPane.showConfirmDialog(parentComponent, mensaje, "JAM V2.01 ", 
      2, 
      3);
    if (reto == 0) {
      return true;
    }
    return false;
  }
  
  public static void showDialog(String mensaje, String titulo)
  {
    JOptionPane.showMessageDialog(null, 
      filtraMensajeError(mensaje), 
      "JAM V2.01 " + titulo, 
      1);
  }
  
  public static void showDialog(String mensaje, String titulo, Component parentComponent)
  {
    if (parentComponent == null)
    {
      System.out.println(mensaje);
      return;
    }
    JOptionPane.showMessageDialog(parentComponent, 
      filtraMensajeError(mensaje), 
      "JAM V2.01 " + titulo, 
      1);
  }
  
  public static void showDialogInf(String mensaje)
  {
    JOptionPane.showMessageDialog(null, 
      mensaje, 
      "JAM V2.01 ", 
      1);
  }
  
  public static void showDialogInf(String mensaje, Component parentComponent)
  {
    if (parentComponent == null)
    {
      System.out.println(mensaje);
      return;
    }
    JOptionPane.showMessageDialog(parentComponent, 
      mensaje, 
      "JAM V2.01 ", 
      1);
  }
  
  public static void showDialog(String mensaje)
  {
    showDialog(filtraMensajeError(mensaje), 
      "JAM V2.01 ");
  }
  
  public static void showDialog(String mensaje, Component parentComponent)
  {
    if (parentComponent == null)
    {
      System.out.println(mensaje);
      return;
    }
    showDialog(filtraMensajeError(mensaje), 
      "JAM V2.01 ", parentComponent);
  }
  
  private static String filtraMensajeError(String argCadena)
  {
    String cRetorna = argCadena;
    if (JAMRowSet.logEnDesarrollo) {
      return argCadena;
    }
    if (argCadena == null) {
      return "";
    }
    return cRetorna;
  }
  
  public static String getShortDateFormat()
  {
    return fulldateformat;
  }
  
  public static SimpleDateFormat getFullDateFormatter()
  {
    return fulldateformatter;
  }
  
  public static SimpleDateFormat getShortDateFormatter()
  {
    return shortdateformatter;
  }
  
  public static void UpdateField(String colname, JAMRowSet targetrst, JAMRowSet sourceRst, int argIdFalso, boolean logSoloCopia)
    throws Exception
  {
    try
    {
      int intType = 55537;
      try
      {
        targetrst.findColumn(colname);
        intType = sourceRst.findColumn(colname);
      }
      catch (Exception localException1) {}
      switch (sourceRst.getMetaData().getColumnType(intType))
      {
      case 0: 
        targetrst.updateNull(colname);
        break;
      case -6: 
      case 4: 
      case 5: 
        if (logSoloCopia)
        {
          targetrst.updateInt(colname, sourceRst.getInt(colname));
        }
        else if (colname.toUpperCase().indexOf("ID_") != -1)
        {
          targetrst.updateInt(colname, argIdFalso);
        }
        else if (colname.toUpperCase().indexOf("RELA_") != -1)
        {
          int intPaso = sourceRst.getInt(colname);
          if (intPaso == 0) {
            targetrst.updateNull(colname);
          } else {
            targetrst.updateInt(colname, sourceRst.getInt(colname));
          }
        }
        else
        {
          targetrst.updateInt(colname, sourceRst.getInt(colname));
        }
        break;
      case -5: 
      case 2: 
        if (logSoloCopia)
        {
          targetrst.updateInt(colname, sourceRst.getInt(colname));
        }
        else if (colname.toUpperCase().indexOf("ID_") != -1)
        {
          targetrst.updateLong(colname, argIdFalso);
        }
        else if (colname.toUpperCase().indexOf("RELA_") != -1)
        {
          int intPaso = sourceRst.getInt(colname);
          if (intPaso == 0) {
            targetrst.updateNull(colname);
          } else {
            targetrst.updateLong(colname, sourceRst.getInt(colname));
          }
        }
        else
        {
          targetrst.updateLong(colname, sourceRst.getInt(colname));
        }
        break;
      case 16: 
        targetrst.updateBoolean(colname, sourceRst.getBoolean(colname));
        break;
      case 3: 
      case 7: 
      case 8: 
        targetrst.updateDouble(colname, sourceRst.getDouble(colname));
        break;
      case 6: 
        targetrst.updateFloat(colname, sourceRst.getFloat(colname));
        break;
      case -1: 
      case 1: 
      case 12: 
        targetrst.updateString(colname, sourceRst.getString(colname));
        break;
      case 91: 
        targetrst.updateDate(colname, sourceRst.getDate(colname));
        break;
      case 92: 
        targetrst.updateTime(colname, sourceRst.getTime(colname));
        break;
      case 93: 
        targetrst.updateTimestamp(colname, sourceRst.getTimestamp(colname));
        break;
      case 2003: 
        targetrst.updateArray(colname, sourceRst.getArray(colname));
        break;
      case -4: 
      case -3: 
      case -2: 
        if (sourceRst.getBytes(colname) == null) {
          targetrst.updateNull(colname);
        } else {
          targetrst.updateBytes(colname, sourceRst.getBytes(colname));
        }
        break;
      case 2004: 
        targetrst.updateBlob(colname, sourceRst.getBlob(colname));
        break;
      case 2005: 
        targetrst.updateClob(colname, sourceRst.getClob(colname));
        break;
      case -7: 
      case 70: 
      case 1111: 
      case 2000: 
      case 2001: 
      case 2002: 
        targetrst.updateNull(colname);
        break;
      case 2006: 
        targetrst.updateRef(colname, sourceRst.getRef(colname));
        break;
      default: 
        targetrst.updateNull(colname);
      }
    }
    catch (Exception e)
    {
      if ((e.getMessage() == null) || (e.getMessage().compareToIgnoreCase("null") == 0)) {
        targetrst.updateNull(colname);
      } else {
        throw new Exception("Error Actualizando columna " + colname + e.getMessage() + " ");
      }
    }
  }
  
  public static double dblMilesFormat(String argNumber)
  {
    String cNuevo = "";
    argNumber = argNumber.trim();
    for (int i = 0; i < argNumber.length(); i++) {
      if (!argNumber.substring(i, i + 1).equalsIgnoreCase(",")) {
        cNuevo = cNuevo + argNumber.substring(i, i + 1);
      }
    }
    double argPaso = 0.0D;
    try
    {
      argPaso = new Double(cNuevo).doubleValue();
    }
    catch (Exception localException) {}
    return argPaso;
  }
  
  public static double setFormatInCeroResultFraccion(double argValor)
  {
    JAMInputNumber tmpValor = new JAMInputNumber(12, JAMInputNumber.JAMDBL);
    tmpValor.setDecimal(0);
    tmpValor.setNumber(argValor);
    if (tmpValor.equals(0.0D)) {
      return 0.0D;
    }
    return argValor;
  }
  
  public static double setFormatInCeroResultFraccion(double argValor, int intDecimal)
  {
    JAMInputNumber tmpValor = new JAMInputNumber(12, JAMInputNumber.JAMDBL);
    tmpValor.setDecimal(intDecimal);
    tmpValor.setNumber(argValor);
    if (tmpValor.equals(0.0D)) {
      return 0.0D;
    }
    return tmpValor.getNumber().doubleValue();
  }
  
  public static int intMilesFormat(String argNumber)
  {
    return new Double(dblMilesFormat(argNumber)).intValue();
  }
  
  public static long dlbTomaEntero(double argNumber)
  {
    String cNumber = new Double(argNumber).toString();
    String cRetorna = "";
    for (int i = 0; i < cNumber.length(); i++) {
      if (cNumber.substring(i, i + 1).equalsIgnoreCase("."))
      {
        cRetorna = cNumber.substring(0, i);
        break;
      }
    }
    return new Long(cRetorna).longValue();
  }
  
  public static Long dlbTomaDecimal(double argNumber)
  {
    String cNumber = new Double(argNumber).toString();
    String cRetorna = "";
    for (int i = 0; i < cNumber.length(); i++) {
      if (cNumber.substring(i, i + 1).equalsIgnoreCase("."))
      {
        cRetorna = cNumber.substring(i + 1);
        break;
      }
    }
    return new Long(cRetorna);
  }
  
  public static String strMilesFormat(String argNumber)
  {
    String cNuevo = "";
    argNumber = argNumber.trim();
    for (int i = 0; i < argNumber.length(); i++) {
      if (!argNumber.substring(i, i + 1).equalsIgnoreCase(",")) {
        cNuevo = cNuevo + argNumber.substring(i, i + 1);
      }
    }
    return cNuevo;
  }
  
  public static boolean JAMConvBoolean(String arg2)
  {
    if (arg2 == null) {
      return false;
    }
    String cVar = arg2.trim();
    if (cVar.length() == 0) {
      return false;
    }
    if (arg2.trim().equalsIgnoreCase("0")) {
      return false;
    }
    return true;
  }
  
  public static int JAMConvBoolean(boolean arg1)
  {
    if (arg1) {
      return 1;
    }
    return 0;
  }
  
  public static int JAMConvBoolean(Object[] arg1)
  {
    if (arg1 != null) {
      return 1;
    }
    return 0;
  }
  
  public static boolean JAMConvBoolean(Object arg1)
  {
    if (arg1 == null) {
      return false;
    }
    return true;
  }
  
  public static boolean JAMConvBoolean(int arg1)
  {
    if (arg1 == 1) {
      return true;
    }
    return false;
  }
  
  public static boolean JAMConvNullRst(int arg1)
  {
    try
    {
      boolean flag = true;
      if (arg1 == 0) {}
      return false;
    }
    catch (Exception e) {}
    return false;
  }
  
  public static String JAMConvNullStr(String arg1)
  {
    return arg1 == null ? "" : arg1.trim();
  }
  
  public static String JAMConvStrNull(String arg1)
  {
    return arg1.trim().equalsIgnoreCase("") ? null : arg1.trim();
  }
  
  public static Double JAMConvNullDbl(Double arg1)
  {
    try
    {
      if (arg1 == null) {
        return new Double(0.0D);
      }
      return arg1;
    }
    catch (Exception e) {}
    return new Double(0.0D);
  }
  
  public static String JAMFindeStr(String argCadena, boolean argComporta, String argCadeMarca)
  {
    String cCadena = argCadena.trim();
    String cRetoCade = argCadena;
    if (argComporta) {
      for (int i = cCadena.length(); i >= 0; i--)
      {
        String cPaso = cCadena.substring(i);
        if (cPaso.indexOf(argCadeMarca) != -1)
        {
          cRetoCade = cPaso.substring(1).trim();
          break;
        }
      }
    } else {
      for (int i = 0; i < cCadena.length(); i++)
      {
        String cPaso = cCadena.substring(i);
        if (cPaso.indexOf(argCadeMarca) != -1)
        {
          cRetoCade = cPaso.substring(0, cPaso.indexOf(argCadeMarca)).trim();
          break;
        }
      }
    }
    return cRetoCade;
  }
  
  public static String JAMFindeStrAll(String argCadena, boolean argComporta, String argCadeMarca)
  {
    String cCadena = argCadena.trim();
    String cRetoCade = argCadena;
    if (argComporta) {
      for (int i = cCadena.length(); i >= 0; i--)
      {
        String cPaso = cCadena.substring(i);
        if (cPaso.indexOf(argCadeMarca) != -1)
        {
          cRetoCade = argCadena.substring(0, i).trim();
          break;
        }
      }
    } else {
      for (int i = 0; i < cCadena.length(); i++)
      {
        String cPaso = cCadena.substring(i);
        if (cPaso.indexOf(argCadeMarca) != -1)
        {
          cRetoCade = cPaso.substring(i + 1).trim();
          break;
        }
      }
    }
    return cRetoCade;
  }
  
  public static Object JAMAddCombos(String item)
  {
    return new Object()
    {
      public String toString()
      {
        return item;
      }
    };
  }
  
  public static int JAMFindVector(Vector argVector, String argValor)
  {
    int reto = -1;
    for (int i = 0; i < argVector.size(); i++) {
      if (argVector.get(i).toString().contains(argValor))
      {
        reto = i;
        break;
      }
    }
    return reto;
  }
  
  public static int JAMFindArray(String[] argArray, String argValor)
  {
    int reto = -1;
    for (int i = 0; i < argArray.length; i++)
    {
      String cCadeTemp = JAMConvNullStr(argArray[i]);
      if (cCadeTemp.trim().toUpperCase().contains(argValor.trim().toUpperCase()))
      {
        reto = i;
        break;
      }
    }
    return reto;
  }
  
  public static int JAMFindArray(int[] argArray, int argValor)
  {
    int reto = -1;
    for (int i = 0; i < argArray.length; i++) {
      if (argArray[i] == argValor)
      {
        reto = i;
        break;
      }
    }
    return reto;
  }
  
  public static int JAMFindVector(Vector argVector, int argValor)
  {
    int argRetro = -1;
    for (int i = 0; i < argVector.size(); i++)
    {
      Integer tmpIdref = (Integer)argVector.get(i);
      if (tmpIdref.intValue() == argValor)
      {
        argRetro = i;
        break;
      }
    }
    return argRetro;
  }
  
  public static String JAMFindVector2(Vector argVector, String argDefinidor)
  {
    String argRetro = null;
    if (argVector != null) {
      for (int i = 0; i < argVector.size(); i++)
      {
        String[][] arrRecibo = (String[][])argVector.get(i);
        if (arrRecibo[0][0].trim().equalsIgnoreCase(argDefinidor))
        {
          argRetro = arrRecibo[0][1].trim();
          break;
        }
      }
    }
    return argRetro;
  }
  
  public static String JAMConverStrASCII(String argCadena)
  {
    String cCadeRetorna = argCadena;
    
    cCadeRetorna = cCadeRetorna.replaceAll("a", "a");
    cCadeRetorna = cCadeRetorna.replaceAll("e", "e");
    cCadeRetorna = cCadeRetorna.replaceAll("i", "i");
    cCadeRetorna = cCadeRetorna.replaceAll("o", "o");
    cCadeRetorna = cCadeRetorna.replaceAll("u", "u");
    cCadeRetorna = cCadeRetorna.replaceAll("n", "n");
    
    cCadeRetorna = cCadeRetorna.replaceAll("a", "A");
    cCadeRetorna = cCadeRetorna.replaceAll("e", "E");
    cCadeRetorna = cCadeRetorna.replaceAll("i", "I");
    cCadeRetorna = cCadeRetorna.replaceAll("o", "O");
    cCadeRetorna = cCadeRetorna.replaceAll("u", "U");
    cCadeRetorna = cCadeRetorna.replaceAll("n", "N");
    cCadeRetorna = cCadeRetorna.replaceAll("��", "_");
    cCadeRetorna = cCadeRetorna.replaceAll(" ", "_");
    return cCadeRetorna;
  }
  
  public static String doClearSpaces(String argCadena)
  {
    String strReto = "";
    StringTokenizer strBuscar = new StringTokenizer(argCadena);
    while (strBuscar.hasMoreElements())
    {
      String strCade = (String)strBuscar.nextElement();
      strCade = strCade.trim();
      strReto = strReto + " " + strCade;
    }
    return strReto.trim();
  }
  
  public static boolean JAMValidaASCIIPlano(String argCadena)
  {
    String strAlfa = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
    for (int i = 0; i < argCadena.length(); i++)
    {
      String strCaracter = argCadena.substring(i, i + 1).toUpperCase();
      if (strAlfa.indexOf(strCaracter) == -1) {
        return false;
      }
    }
    return true;
  }
  
  public static String JAMRefill(String argValor, String argRelleno, int argLong)
  {
    String cRelleno = "";
    if (argLong < argValor.length()) {
      return argValor;
    }
    for (int i = 0; i < argLong - argValor.length(); i++) {
      cRelleno = cRelleno + argRelleno;
    }
    return cRelleno + argValor;
  }
  
  public static int JAMLastId(String argUsuario)
  {
    try
    {
      String[] arrSql = new String[1];
      arrSql[0] = 
        ("SELECT ID_SOCUTIL02, SOCUTIL02_ULTID FROM SOCUTIL02_TBL_ULTIDS WHERE SOCUTIL02_USUARIO = '" + argUsuario + "'");
      JAMRowSet[] rstTmp = JAMClienteDB.getRowSets(arrSql);
      rstTmp[0].first();
      return rstTmp[0].getInt("SOCUTIL02_ULTID");
    }
    catch (Exception e) {}
    return -1;
  }
  
  public static int JAMLastId(String argUsuario, String argAliasTabla)
  {
    try
    {
      String[] arrSql = new String[1];
      arrSql[0] = 
      
        ("select max(socutil01_mov_logs.socutil01_ultid) SOCUTIL02_ULTID from socutil01_mov_logs WHERE socutil01_mov_logs.socutil01_usuario = '" + argUsuario + "' " + "AND socutil01_mov_logs.socutil01_accion = 'A' " + "AND socutil01_mov_logs.socutil01_tabla = '" + argAliasTabla + "'");
      JAMRowSet[] rstTmp = JAMClienteDB.getRowSets(arrSql);
      rstTmp[0].first();
      return rstTmp[0].getInt("SOCUTIL02_ULTID");
    }
    catch (Exception e) {}
    return -1;
  }
  
  public static String JAMLeeArchivoHTML(String argUrl)
  {
    String cArchivoHTML = "";
    try
    {
      URL url = new URL(argUrl);
      InputStream is = url.openStream();
      BufferedReader di = new BufferedReader(new InputStreamReader(is));
      for (;;)
      {
        String linea = di.readLine();
        if (linea == null) {
          break;
        }
        cArchivoHTML = cArchivoHTML + linea + "\n";
      }
      return cArchivoHTML;
    }
    catch (Exception e)
    {
      System.out.println("Error al comprobar Correo. " + e.getMessage());
    }
    return null;
  }
  
  public static void copyFile(File in, File out)
    throws Exception
  {
    FileChannel sourceChannel = new FileInputStream(in).getChannel();
    FileChannel destinationChannel = new FileOutputStream(out).getChannel();
    sourceChannel.transferTo(0L, sourceChannel.size(), destinationChannel);
    
    sourceChannel.close();
    destinationChannel.close();
  }
  
  public static String JAMTimeNow()
  {
    Date date = Calendar.getInstance().getTime();
    DateFormat dateFormatter = DateFormat.getTimeInstance();
    return dateFormatter.format(date);
  }
  
  public static void JAMTimeInit()
  {
    initialTime = new Date();
  }
  
  public static long JAMTimeElapsed()
  {
    Date ahora = new Date();
    
    return ahora.getTime() - initialTime.getTime();
  }
  
  public static String[] JAMCargaLineasServlet(BufferedReader bufferentrada)
  {
    return JAMCargaLineasServletPrv(bufferentrada, false);
  }
  
  public static String[] JAMCargaLineasServlet(BufferedReader bufferentrada, boolean argSinEspacios)
  {
    return JAMCargaLineasServletPrv(bufferentrada, argSinEspacios);
  }
  
  private static String[] JAMCargaLineasServletPrv(BufferedReader bufferentrada, boolean argSinEspacios)
  {
    String[] arrayRetorna = (String[])null;
    Vector vctTemp = new Vector();
    String linea = null;
    try
    {
      linea = bufferentrada.readLine();
      while (linea != null)
      {
        vctTemp.add(linea);
        linea = bufferentrada.readLine();
      }
      arrayRetorna = new String[vctTemp.size()];
      for (int i = 0; i < vctTemp.size(); i++)
      {
        String cPaso = (String)vctTemp.get(i);
        if (argSinEspacios)
        {
          if (!cPaso.trim().equalsIgnoreCase("")) {
            arrayRetorna[i] = ((String)vctTemp.get(i));
          }
        }
        else {
          arrayRetorna[i] = ((String)vctTemp.get(i));
        }
      }
    }
    catch (IOException localIOException) {}
    return arrayRetorna;
  }
  
  public static int validaTipoArchivo(String argArchivo)
  {
    if ((argArchivo.toUpperCase().indexOf(".JPG") != -1) || 
      (argArchivo.toUpperCase().indexOf(".TIF") != -1) || 
      (argArchivo.toUpperCase().indexOf(".GIF") != -1) || 
      (argArchivo.toUpperCase().indexOf(".ICO") != -1) || 
      (argArchivo.toUpperCase().indexOf(".BMP") != -1) || 
      (argArchivo.toUpperCase().indexOf(".PNG") != -1)) {
      return 0;
    }
    if (argArchivo.toUpperCase().indexOf(".XML") != -1) {
      return 2;
    }
    return 1;
  }
  
  public static Time SparseToTime(String hora)
  {
    int h = Integer.parseInt(""+hora.charAt(0) + hora.charAt(1));
    int m = Integer.parseInt(""+hora.charAt(3) + hora.charAt(4));
    int s = 0;
    if (hora.length() > 5) {
      s = Integer.parseInt(""+hora.charAt(6) + hora.charAt(7));
    }
    return new Time(h, m, s);
  }
  
  public static String JAMFiltroSoloLetras(int intCharCode)
  {
    int[] chrNoValidos = { 40, 16, 8, 127, 74, 17 };
    for (int i = 0; i < chrNoValidos.length; i++) {
      if (chrNoValidos[i] == intCharCode) {
        return "";
      }
    }
    return String.valueOf((char)intCharCode);
  }
  
  public static int fechasDiferenciaEnDias(Date fechaInicial, Date fechaFinal)
  {
    DateFormat df = DateFormat.getDateInstance(2);
    
    String fechaInicioString = df.format(fechaInicial);
    try
    {
      fechaInicial = df.parse(fechaInicioString);
    }
    catch (ParseException localParseException) {}
    String fechaFinalString = df.format(fechaFinal);
    try
    {
      fechaFinal = df.parse(fechaFinalString);
    }
    catch (ParseException localParseException1) {}
    long fechaInicialMs = fechaInicial.getTime();
    long fechaFinalMs = fechaFinal.getTime();
    
    long diferencia = fechaFinalMs - fechaInicialMs;
    
    double dias = Math.floor(diferencia / 86400000L);
    return (int)dias;
  }
  
  public static int horasDiferencia(Time horaOrigen, Time horaCompara)
  {
    Time tmpTimeOrig = horaOrigen;
    Time tmpTimeComp = horaCompara;
    
    int intHoraOrig = tmpTimeOrig.getHours();
    int intMinuOrig = tmpTimeOrig.getMinutes();
    int intSeguOrig = tmpTimeOrig.getSeconds();
    
    int intHoraComp = tmpTimeComp.getHours();
    int intMinuComp = tmpTimeComp.getMinutes();
    int intSeguComp = tmpTimeComp.getSeconds();
    
    int intDifSegundos = intSeguComp - intSeguOrig;
    int intDifMinutos = intMinuComp - intMinuOrig;
    int intDifHoras = intHoraComp - intHoraComp;
    
    return 1;
  }
  
  public static String JAMLeeArchivoINI(String argEtiqueta, String argLabel)
  {
    String cReto = null;
    FileInputStream file = null;
    try
    {
      file = new FileInputStream("..//webapps/JAMMexico/conections.ini");
      IniEditor e = new IniEditor();
      e.load(file);
      cReto = e.get(argEtiqueta, argLabel);
      file.close();
    }
    catch (Exception e)
    {
      try
      {
        file.close();
      }
      catch (Exception localException1) {}
    }
    return cReto;
  }
  
  public static void JAMEnabledComponents(JComponent argComponent, boolean argEstado)
  {
    for (int i = 0; i < argComponent.getComponentCount(); i++) {
      if ((argComponent.getComponent(i) instanceof JAMLabel))
      {
        JAMLabel objTmp = (JAMLabel)argComponent.getComponent(i);
        objTmp.setJamEnabled(argEstado);
      }
      else if ((argComponent.getComponent(i) instanceof JAMAreaTexto))
      {
        JAMAreaTexto objTmp = (JAMAreaTexto)argComponent.getComponent(i);
        objTmp.setEditable(argEstado);
      }
      else if ((argComponent.getComponent(i) instanceof JPanel))
      {
        JPanel objTmp = (JPanel)argComponent.getComponent(i);
        JAMEnabledComponents(objTmp, argEstado);
      }
      else
      {
        argComponent.getComponent(i).setEnabled(argEstado);
      }
    }
  }
  
  public static void JAMRemoveComponents(JComponent argComponent)
  {
    for (int i = 0; i < argComponent.getComponentCount(); i++) {
      argComponent.remove(argComponent.getComponent(i));
    }
  }
  
  public static BorderLayout JAMBorderLayout(int setHgap, int setVgap)
  {
    BorderLayout BderLay = new BorderLayout();
    BderLay.setHgap(setHgap);
    BderLay.setVgap(setVgap);
    return BderLay;
  }
  
  public static GridLayout JAMGridLayout(int setRow, int setCol, int setHgap, int setVgap)
  {
    GridLayout gridLayout = new GridLayout();
    gridLayout.setRows(setRow);
    gridLayout.setHgap(setHgap);
    gridLayout.setVgap(setVgap);
    gridLayout.setColumns(setCol);
    return gridLayout;
  }
  
  public static JAMRowSet JAMCopyRegistro(JAMRowSet argTarget, JAMRowSet argSource)
  {
    JAMRowSet rstReto = argTarget;
    try
    {
      rstReto.moveToInsertRow();
      for (int i = 1; i <= argTarget.getMetaData().getColumnCount(); i++)
      {
        String cCampo = argTarget.getMetaData().getColumnName(i);
        UpdateField(cCampo, rstReto, argSource, -1, true);
      }
      rstReto.insertRow();
      rstReto.moveToCurrentRow();
    }
    catch (Exception e)
    {
      System.out.println(e.getMessage());
      return null;
    }
    return rstReto;
  }
  
  public static void JAMRowSetReemplazaCerosPorNull(JAMRowSet argRowSet)
    throws SQLException
  {
    for (int i = 1; i <= argRowSet.getMetaData().getColumnCount(); i++)
    {
      String cCampo = argRowSet.getMetaData().getColumnName(i);
      if ((cCampo.toUpperCase().startsWith("RELA_")) && 
        (argRowSet.getInt(cCampo) == 0))
      {
        argRowSet.moveToCurrentRow();
        argRowSet.updateNull(cCampo);
        argRowSet.updateRow();
      }
    }
  }
  
  public static JAMRowSet JAMMergeRowSets(String argSql, JAMRowSet argSource, JAMRowSet argTarget)
    throws Exception
  {
    int intIdFalso = 0;
    String[] arrStrSql = new String[1];
    arrStrSql[0] = argSql;
    try
    {
      if (argSql != null)
      {
        JAMRowSet[] rstTmp = JAMClienteDB.getRowSets(arrStrSql);
        
        argSource.beforeFirst();
        while (argSource.next())
        {
          rstTmp[0].moveToInsertRow();
          for (int i = 1; i <= argSource.getMetaData().getColumnCount(); i++)
          {
            String cCampo = argSource.getMetaData().getColumnName(i);
            if (cCampo.toUpperCase().indexOf("ID_") != -1) {
              intIdFalso--;
            }
            UpdateField(cCampo, rstTmp[0], argSource, intIdFalso, false);
          }
          rstTmp[0].insertRow();
          rstTmp[0].moveToCurrentRow();
        }
        argTarget.beforeFirst();
        while (argTarget.next())
        {
          rstTmp[0].moveToInsertRow();
          for (int i = 1; i <= argTarget.getMetaData().getColumnCount(); i++)
          {
            String cCampo = argTarget.getMetaData().getColumnName(i);
            if (cCampo.toUpperCase().indexOf("ID_") != -1) {
              intIdFalso--;
            }
            UpdateField(cCampo, rstTmp[0], argTarget, intIdFalso, false);
          }
          rstTmp[0].insertRow();
          rstTmp[0].moveToCurrentRow();
        }
        return rstTmp[0];
      }
      argTarget.moveToInsertRow();
      for (int i = 1; i <= argSource.getMetaData().getColumnCount(); i++)
      {
        String cCampo = argSource.getMetaData().getColumnName(i);
        UpdateField(cCampo, argTarget, argSource, -1, true);
      }
      argTarget.insertRow();
      argTarget.moveToCurrentRow();
      
      return argTarget;
    }
    catch (Exception e)
    {
      throw new Exception("Error en el proceso de Merger de Rowsets: " + e.getMessage());
    }
  }
  
  public static Vector listDirectoryVector(String dir, String mascara, int maximo)
  {
    int intMaximo = maximo;
    Vector v = new Vector();
    
    File f = new File(dir);
    f.mkdir();
    File[] array = f.listFiles();
    if ((intMaximo == 0) || (intMaximo > array.length)) {
      intMaximo = array.length;
    }
    for (int i = 0; i < intMaximo; i++) {
      if ((array[i].isFile()) && (!array[i].isHidden())) {
        if (mascara == null) {
          v.addElement(new String(array[i].getName()));
        } else if (array[i].getName().toLowerCase().indexOf(mascara.toLowerCase()) != -1) {
          v.addElement(new String(array[i].getName()));
        }
      }
    }
    return v;
  }
  
  public static String getStrServLetParametros(String[] argArray)
  {
    String strReto = "";
    for (int i = 1; i < argArray.length; i++) {
      strReto = strReto + argArray[i] + getCrlf();
    }
    return strReto;
  }
  
  public static String getStrDirectoryTmpFiles(String Instancia, int intTipo)
    throws Exception
  {
    try
    {
      FileInputStream file = new FileInputStream("C:\\JAMWebService\\JAMDBConfig\\conections.ini");
      IniEditor e = new IniEditor();
      e.load(file);
      String strPathPicture = e.get(Instancia, "RootPicture");
      String strPathFiles = e.get(Instancia, "RootFiles");
      String strPathFilesXml = e.get(Instancia, "RootFilesXml");
      file.close();
      switch (intTipo)
      {
      case 0: 
        return strPathPicture + Instancia + "\\";
      case 1: 
        return strPathFiles + Instancia + "\\";
      case 2: 
        return strPathFilesXml + Instancia + "\\";
      }
      return null;
    }
    catch (IOException e)
    {
      throw new Exception("Error de Aplicacion " + e.getMessage());
    }
    catch (Exception e)
    {
      throw new Exception("Error de Aplicacion " + e.getMessage());
    }
  }
  
  public static File getDirectoryTmpFilesForType(String fileName, String Instancia)
    throws Exception
  {
    File fichero = new File(fileName);
    try
    {
      FileInputStream file = new FileInputStream("C:\\JAMWebService\\JAMDBConfig\\conections.ini");
      IniEditor e = new IniEditor();
      e.load(file);
      String strPathPicture = e.get(Instancia, "RootPicture");
      String strPathFiles = e.get(Instancia, "RootFiles");
      String strPathFilesXml = e.get(Instancia, "RootFilesXml");
      file.close();
      switch (validaTipoArchivo(fichero.getName()))
      {
      case 0: 
        fichero = new File(strPathPicture + Instancia + "\\" + fichero.getName());
        break;
      case 1: 
        fichero = new File(strPathFiles + Instancia + "\\" + fichero.getName());
        break;
      case 2: 
        fichero = new File(strPathFilesXml + Instancia + "\\" + fichero.getName());
      }
    }
    catch (IOException e)
    {
      throw new Exception("Error de Aplicacion " + e.getMessage());
    }
    catch (Exception e)
    {
      throw new Exception("Error de Aplicacion " + e.getMessage());
    }
    return fichero;
  }
  
  public static boolean JAMValidaUsuarioSecurity(String argUsuario)
  {
    String[] arrSql = (String[])null;
    JAMRowSet[] arrRst = (JAMRowSet[])null;
    try
    {
      arrSql = new String[1];
      arrSql[0] = ("select id_socusua02 from socusua02_mae_usuarios where socusua02_mae_usuarios.socusua02_usuario = '" + argUsuario.trim() + "'");
      arrRst = JAMClienteDB.getRowSets(arrSql);
      if (arrRst[0].getRowcount() != 0L) {
        return true;
      }
      arrSql = new String[1];
      arrSql[0] = ("select uid from users where user_name = 'JAM$_" + argUsuario.trim() + "'");
      arrRst = JAMClienteDB.getRowSets(arrSql, JAMClienteDB.getDbSeg());
      if (arrRst[0].getRowcount() != 0L) {
        return true;
      }
    }
    catch (Exception localException) {}
    return false;
  }
  
  public static void doDebugArchive(String cMensaje, String argDirectorioYArchivo)
  {
    try
    {
      FileWriter fw = new FileWriter(argDirectorioYArchivo, true);
      BufferedWriter bw = new BufferedWriter(fw);
      PrintWriter salida = new PrintWriter(bw);
      salida.append(cMensaje + getCrlf());
      salida.close();
    }
    catch (IOException ioex)
    {
      System.out.println("se presento el error al grabar el log de archivos copiados : " + ioex.toString());
    }
  }
  
  public static void doUpdate(Runnable r)
  {
    try
    {
      SwingUtilities.invokeAndWait(r);
    }
    catch (InvocationTargetException e1)
    {
      System.err.println(e1);
    }
    catch (InterruptedException e2)
    {
      System.err.println(e2);
    }
  }
  
  public static void setCentraDialog(Component argDialog)
  {
    int newx = (JAMLibKernel.ParamJAMDesktop.getBounds().width - argDialog.getWidth()) / 2;
    int newy = (JAMLibKernel.ParamJAMDesktop.getBounds().height - argDialog.getHeight()) / 2;
    argDialog.setLocation(newx, newy);
  }
  
  public static void doUsersAdd(String arrUsuario, String arrPassword, String arrDescri01, String arrDescri02)
    throws Exception
  {
    String[] arrSql = new String[1];
    arrSql[0] = 
    
      ("select * from JAM$_USER_ADD('" + arrUsuario + "','" + arrPassword + "','" + JAMLibKernel.ParamJAMInstname + "=','" + arrDescri01 + "','" + arrDescri02 + "')");
    try
    {
      JAMRowSet[] arrRowSet = JAMClienteDB.getRowSets(arrSql);
      JAMRowSet rstTemp = arrRowSet[0];
      
      rstTemp.first();
      if (rstTemp.getInt("OK") != 0) {
        throw new Exception("ALTA: No se ha podido Actualizar el Usuario Systema/Db");
      }
    }
    catch (Exception e)
    {
      throw new Exception("ALTA: " + e.getMessage());
    }
  }
  
  public static void doUsersMod(String arrUsuario, String arrPassword, String arrDescri01, String arrDescri02)
    throws Exception
  {
    String[] arrSql = new String[1];
    arrSql[0] = 
    
      ("select * from JAM$_USER_MOD('" + arrUsuario + "','" + arrPassword + "','" + JAMLibKernel.ParamJAMInstname + "=','" + arrDescri01 + "','" + arrDescri02 + "')");
    try
    {
      JAMRowSet[] arrRowSet = JAMClienteDB.getRowSets(arrSql);
      JAMRowSet rstTemp = arrRowSet[0];
      
      rstTemp.first();
      if (rstTemp.getInt("OK") != 0) {
        throw new Exception("MODIFICA: No se ha podido Actualizar el Usuario Systema/Db");
      }
    }
    catch (Exception e)
    {
      throw new Exception("MODIFICA: " + e.getMessage());
    }
  }
  
  public static void doUsersDel(String arrUsuario)
    throws Exception
  {
    String[] arrSql = new String[1];
    arrSql[0] = ("select *  from JAM$_USER_DEL('" + arrUsuario + "')");
    try
    {
      JAMRowSet[] arrRowSet = JAMClienteDB.getRowSets(arrSql);
      JAMRowSet rstTemp = arrRowSet[0];
      
      rstTemp.first();
      if (rstTemp.getInt("OK") != 0) {
        throw new Exception("BAJA: No se ha podido Actualizar el Usuario Systema/Db");
      }
    }
    catch (Exception e)
    {
      throw new Exception("BAJA: " + e.getMessage());
    }
  }
  
  public static String doValidaMailInternet(String argCorreo, String argNombre)
  {
    return doValidaDBMailExisteInternet(argCorreo, argNombre);
  }
  
  public static String doValidaDBMailExiste(String argCorreo, String argNombre)
  {
    String cReto = doValidaDBMailExisteInternet(argCorreo, argNombre);
    if (cReto != null) {
      return cReto;
    }
    return null;
  }
  
  private static String doValidaDBMailExisteInternet(String argCorreo, String argNombre)
  {
    Pattern patron = Pattern.compile("^[\\w-\\.]+\\@[\\w\\.-]+\\.[a-z]{2,4}$");
    Matcher encajador = patron.matcher(argCorreo);
    if (!encajador.matches()) {
      return "Correo Invalido";
    }
    try
    {
      JAMMail objMail = new JAMMail();
      objMail.doConfigMailOfficeNet2(null, null, "OfficeNet2 - Robot DataInfo - OnLine");
      String[] strDirecciones = { JAMLibKernel.ParamJAMServername + "@officenet2.com.mx", argCorreo };
      
      objMail.doDirecciones(strDirecciones);
      
      objMail.doAsunto("Verificacion de Correo");
      
      String cArchiveHtml = JAMLeeArchivoHTML("http://201.175.21.173:8024/JAMMexico/ValidaCorreo.html");
      String cNomUsuario = argNombre;
      
      cArchiveHtml = cArchiveHtml.replace("[NOMCLIENTE]", cNomUsuario);
      cArchiveHtml = cArchiveHtml.replace("[NOMCORREO]", argCorreo.trim());
      
      objMail.doMensajeHTML(cArchiveHtml);
      if (!objMail.doEnviarCorreo()) {
        return "Verificacion en Internet no Valido";
      }
    }
    catch (Exception e)
    {
      return "Error al comprobar Correo. " + e.getMessage();
    }
    return null;
  }
  
  public static String getIPLocal()
  {
    String cReto = "";
    try
    {
      InetAddress address = InetAddress.getByName(InetAddress.getLocalHost().getHostName());
      System.out.println(address);
      byte[] bytes = address.getAddress();
      for (int cnt = 0; cnt < bytes.length; cnt++)
      {
        int uByte = bytes[cnt] < 0 ? bytes[cnt] + 256 : bytes[cnt];
        cReto = cReto + uByte + ".";
      }
      cReto = cReto.substring(0, cReto.length() - 1);
    }
    catch (Exception e)
    {
      return "";
    }
    return cReto;
  }
  
  public static String getIPPublic()
  {
    try
    {
      URL tempURL = new URL("http://www.cualesmiip.com/");
      HttpURLConnection tempConn = (HttpURLConnection)tempURL.openConnection();
      InputStream tempInStream = tempConn.getInputStream();
      InputStreamReader tempIsr = new InputStreamReader(tempInStream);
      BufferedReader tempBr = new BufferedReader(tempIsr);
      
      String strIpPublica = null;
      String strCade = tempBr.readLine();
      while (strCade != null)
      {
        int intPos = strCade.indexOf("Tu IP real es ");
        if (intPos != -1)
        {
          StringTokenizer strFind = new StringTokenizer(strCade.substring(intPos + 14), ".");
          strIpPublica = (String)strFind.nextElement() + "." + 
            (String)strFind.nextElement() + "." + 
            (String)strFind.nextElement();
          String strUlt = (String)strFind.nextElement();
          strUlt = JAMFindeStr(strUlt, false, " ");
          strIpPublica = strIpPublica + "." + strUlt.trim();
          
          break;
        }
        strCade = tempBr.readLine();
      }
      tempBr.close();
      tempInStream.close();
      return strIpPublica;
    }
    catch (Exception e) {}
    return "";
  }
  
  public static double round(double val, int places)
  {
    long factor = 0;
    if (places == 0) {
      return Math.rint(val);
    }
    factor = (long) Math.pow(10.0D, places);
    val *= factor;
    
    long tmp = Math.round(val);
    
    return tmp / factor;
  }
  
  public static float round(float val, int places)
  {
    return (float)round(val, places);
  }
  
  public static boolean logicArcValid(boolean[] argValues, boolean argRefe)
  {
    for (int i = 0; i < argValues.length; i++) {
      if ((argValues[i] == false) == argRefe) {
        return false;
      }
    }
    return true;
  }
  
  public static boolean logicArcValid(boolean[] argValues)
  {
    int intFalse = 0;
    int intTrue = 0;
    for (int i = 0; i < argValues.length; i++) {
      if (argValues[i] != false) {
        intTrue++;
      } else {
        intFalse++;
      }
    }
    if ((argValues.length == intFalse) || (argValues.length == intTrue)) {
      return true;
    }
    return false;
  }
  
  public static byte[] getBytes(InputStream is)
    throws IOException
  {
    int size = 1024;
    int len;
    byte[] buf = null;
    if ((is instanceof ByteArrayInputStream))
    {
      size = is.available();
      buf = new byte[size];
      len = is.read(buf, 0, size);
    }
    else
    {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      buf = new byte[size];
      
      while ((len = is.read(buf, 0, size)) != -1)
      {
       
        bos.write(buf, 0, len);
      }
      buf = bos.toByteArray();
    }
    return buf;
  }
  
  public static byte[] getBytes(String argPathAndFile)
    throws IOException
  {
    InputStream in = new FileInputStream(argPathAndFile);
    byte[] reto = getBytes(in);
    in.close();
    return reto;
  }
  
  /* Error */
  public static void setBytesFile(String dirDestino, byte[] memoria)
    throws Exception
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: new 697	java/io/FileOutputStream
    //   5: dup
    //   6: aload_0
    //   7: invokespecial 1384	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
    //   10: astore_2
    //   11: aload_2
    //   12: aload_1
    //   13: invokevirtual 1385	java/io/OutputStream:write	([B)V
    //   16: aload_2
    //   17: invokevirtual 1390	java/io/OutputStream:flush	()V
    //   20: goto +15 -> 35
    //   23: astore_3
    //   24: aload_3
    //   25: athrow
    //   26: astore 4
    //   28: aload_2
    //   29: invokevirtual 1393	java/io/OutputStream:close	()V
    //   32: aload 4
    //   34: athrow
    //   35: aload_2
    //   36: invokevirtual 1393	java/io/OutputStream:close	()V
    //   39: return
    // Line number table:
    //   Java source line #1609	-> byte code offset #0
    //   Java source line #1611	-> byte code offset #2
    //   Java source line #1612	-> byte code offset #11
    //   Java source line #1613	-> byte code offset #16
    //   Java source line #1614	-> byte code offset #23
    //   Java source line #1615	-> byte code offset #24
    //   Java source line #1616	-> byte code offset #26
    //   Java source line #1617	-> byte code offset #28
    //   Java source line #1618	-> byte code offset #32
    //   Java source line #1617	-> byte code offset #35
    //   Java source line #1619	-> byte code offset #39
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	40	0	dirDestino	String
    //   0	40	1	memoria	byte[]
    //   1	35	2	out	java.io.OutputStream
    //   23	2	3	e	Exception
    //   26	7	4	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   2	20	23	java/lang/Exception
    //   2	26	26	finally
  }
  
  public static void doWait(double Segundos)
  {
    JAMTimeNow();
    JAMTimeInit();
    double intsegundos = Segundos;
    while (JAMTimeElapsed() < intsegundos) {
      if (JAMTimeElapsed() == intsegundos) {
        break;
      }
    }
  }
  
  public static Date getFechaServidor()
  {
    Date reto = null;
    try
    {
      String[] array = new String[1];
      URL miurl = new URL(JAMLibKernel.JAMURL_SERVLETS + "JAMServeletFecha");
      URLConnection conexion = miurl.openConnection();
      conexion.setDoOutput(true);
      ObjectOutputStream buffersalida = new ObjectOutputStream(conexion.getOutputStream());
      buffersalida.writeObject(array);
      buffersalida.flush();
      
      BufferedReader bufferentrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
      String[] arrayRec = JAMCargaLineasServlet(bufferentrada);
      buffersalida.close();
      bufferentrada.close();
      
      String cFecha = arrayRec[JAMFindArray(arrayRec, "fecha")];
      cFecha = JAMFindeStr(cFecha, true, "@");
      
      JAMDate dat = new JAMDate(cFecha, "-", 3);
      
      return dat.getDate();
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return reto;
  }
  
  public static boolean Compara_BytexByte(String argSource, String argTarget)
  {
    boolean logreto = true;
    String bin1 = argSource;
    String bin2 = argTarget;
    try
    {
      DataInputStream x = new DataInputStream(new BufferedInputStream(new FileInputStream(bin1)));
      DataInputStream y = new DataInputStream(new BufferedInputStream(new FileInputStream(bin2)));
      
      byte[] buffer1 = new byte[1024];
      byte[] buffer2 = new byte[1024];
      while ((x.read(buffer1, 0, 1024) != -1) && (y.read(buffer2, 0, 1024) != -1)) {
        if (!Arrays.equals(buffer1, buffer2))
        {
          logreto = false;
          break;
        }
      }
      x.close();
      y.close();
    }
    catch (IOException e)
    {
      System.out.println("Error:");
    }
    return logreto;
  }
  
  public static void JAMRuntime(String[] argProgram)
    throws Exception
  {
    String os = System.getProperty("os.name");
    try
    {
      if (os.equalsIgnoreCase("Mac OS X"))
      {
        File path = null;
        if (argProgram.length == 1)
        {
          path = new File(argProgram[0]);
          Desktop.getDesktop().open(path);
        }
        else
        {
          path = new File(argProgram[1]);
          Desktop.getDesktop().open(path);
        }
      }
      else
      {
        Runtime rt = Runtime.getRuntime();
        rt.exec(argProgram);
      }
    }
    catch (Exception eg)
    {
      System.out.println(eg.getStackTrace().toString());
      throw new Exception(eg.getMessage());
    }
  }
  
  public static String getImporteStr(double dblValor)
  {
    if (objNumTmp == null)
    {
      objNumTmp = new JAMInputNumber(12, JAMInputNumber.JAMDBL);
      objNumTmp.setNumber(0.0D, 2);
    }
    objNumTmp.setNumber(dblValor, 2);
    return objNumTmp.getNumberStr();
  }
}
