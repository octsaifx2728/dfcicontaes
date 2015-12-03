package mx.com.jammexico.jamdb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.activation.DataHandler;
import javax.xml.rpc.ServiceException;
import localhost.axis.services.JAMSendDbWS.JAMSendDbWS;
import localhost.axis.services.JAMSendDbWS.JAMSendDbWSServiceLocator;
import mx.com.jammexico.jamcomponents.JAMLibKernel;
import mx.com.jammexico.jamsrv.JAMDataActionsGroup;
import mx.com.jammexico.jamsrv.JAMRowSet;
import mx.com.jammexico.jamsrv.JAMUtil;
import org.apache.soap.util.mime.ByteArrayDataSource;

public abstract class JAMClienteDB
{
  public static final int METODO_SERVLET = 0;
  public static final int METODO_WEBSERVICE = 1;
  private static int intTimeOut = 120000;
  private static final int DBON2 = 0;
  private static final int DBSEG = 1;
  private static String[] arrDBs = null;
  private static int intMetodo = 1;
  private static String[] PARAMargSqls;
  private static String PARAMargInstanciasAlternativas;
  private static JAMDataActionsGroup PARAMargGroupTransactions;
  private static boolean PARAMlogDepuraComandsSqls = false;
  private static ArrayList<JAMDataConexionesInfo> PARAMargConexionesActivas;
  private static String PARAMargConexionCerrar;
  private static JAMMensajes PARAMargMensajes;
  private static boolean PARAMlogConexionesHistoricas;
  private static boolean PARAMlogCierraYBloqueaConexion;
  
  private static void parametros()
  {
    PARAMargSqls = null;
    PARAMargInstanciasAlternativas = null;
    PARAMargGroupTransactions = null;
    PARAMlogDepuraComandsSqls = false;
    PARAMargConexionesActivas = null;
    PARAMargConexionCerrar = null;
    PARAMargMensajes = null;
    PARAMlogConexionesHistoricas = false;
    PARAMlogCierraYBloqueaConexion = false;
  }
  
  public static JAMRowSet[] getRowSets(String[] argSqls)
    throws Exception
  {
    parametros();
    PARAMargSqls = argSqls;
    if (intMetodo == 0) {
      return doCallToServer();
    }
    return doCallToServerWS();
  }
  
  public static JAMRowSet[] getRowSets(String[] argSqls, String argInstanciasAlternativas)
    throws Exception
  {
    parametros();
    PARAMargSqls = argSqls;
    PARAMargInstanciasAlternativas = argInstanciasAlternativas;
    if (intMetodo == 0) {
      return doCallToServer();
    }
    return doCallToServerWS();
  }
  
  public static JAMRowSet[] getRowSets(String[] argSqls, boolean logDepuraComandosSql)
    throws Exception
  {
    parametros();
    PARAMargSqls = argSqls;
    PARAMlogDepuraComandsSqls = logDepuraComandosSql;
    if (intMetodo == 0) {
      return doCallToServer();
    }
    return doCallToServerWS();
  }
  
  public static void setTransaction(JAMDataActionsGroup argGroupTransactions)
    throws Exception
  {
    parametros();
    PARAMargGroupTransactions = argGroupTransactions;
    if (intMetodo == 0) {
      doCallToServer();
    } else {
      doCallToServerWS();
    }
  }
  
  public static void setTransaction(JAMDataActionsGroup argGroupTransactions, String argInstanciasAlternativas)
    throws Exception
  {
    parametros();
    PARAMargGroupTransactions = argGroupTransactions;
    PARAMargInstanciasAlternativas = argInstanciasAlternativas;
    if (intMetodo == 0) {
      doCallToServer();
    } else {
      doCallToServerWS();
    }
  }
  
  public static ArrayList<JAMDataConexionesInfo> getConexionesActivas()
    throws Exception
  {
    parametros();
    PARAMargConexionesActivas = new ArrayList();
    if (intMetodo == 0) {
      doCallToServer();
    } else {
      doCallToServerWS();
    }
    return PARAMargConexionesActivas;
  }
  
  public static ArrayList<JAMDataConexionesInfo> getConexionesHistoricas()
    throws Exception
  {
    parametros();
    PARAMargConexionesActivas = new ArrayList();
    PARAMlogConexionesHistoricas = true;
    if (intMetodo == 0) {
      doCallToServer();
    } else {
      doCallToServerWS();
    }
    return PARAMargConexionesActivas;
  }
  
  public static String setConexionCerrar(String argCerrar, boolean logBloquea)
    throws Exception
  {
    parametros();
    PARAMargConexionCerrar = argCerrar;
    PARAMlogCierraYBloqueaConexion = logBloquea;
    if (intMetodo == 0) {
      doCallToServer();
    } else {
      doCallToServerWS();
    }
    return PARAMargConexionCerrar;
  }
  
  public static JAMMensajes setMensaje(JAMMensajes argMensaje)
    throws Exception
  {
    parametros();
    PARAMargMensajes = argMensaje;
    if (intMetodo == 0) {
      doCallToServer();
    } else {
      doCallToServerWS();
    }
    return PARAMargMensajes;
  }
  
  public static void setModoConexion(int argModo)
  {
    intMetodo = argModo;
  }
  
  public static int getModoConexion()
  {
    return intMetodo;
  }
  
  public static String getModoConexionDescripcion()
  {
    if (intMetodo == 0) {
      return "ServLet";
    }
    if (intMetodo == 1) {
      return "WebService";
    }
    return "";
  }
  
  public static String getDbOn2()
  {
    return arrDBs[0];
  }
  
  public static String getDbSeg()
  {
    return arrDBs[1];
  }
  
  public static void setDbs(String[] argDbs)
  {
    arrDBs = argDbs;
  }
  
  public static void setTimeOut(int argTimeOut)
  {
    intTimeOut = argTimeOut;
  }
  
  public static int getTimeOut()
  {
    return intTimeOut;
  }
  
  private static JAMRowSet[] doCallToServer()
    throws Exception
  {
    try
    {
      System.out.println("Iniciando Servlet JAMServeletDB " + JAMUtil.JAMTimeNow());
      JAMUtil.JAMTimeInit();
      if (PARAMargSqls != null) {
        System.out.println("Cantidad de rowSets : " + PARAMargSqls.length);
      } else if (PARAMargGroupTransactions != null) {
        System.out.println("Cantidad de Transacciones : " + PARAMargGroupTransactions.getLength());
      } else if (PARAMargConexionesActivas != null) {
        System.out.println("Solicita Conexiones Activas");
      } else if (PARAMargConexionCerrar != null) {
        System.out.println("Envia a Cerrar Conexi��n : " + PARAMargConexionCerrar);
      } else if (PARAMargMensajes != null) {
        System.out.println("Solicita Mensajeria en Modo : " + PARAMargMensajes.getModalidad());
      }
      JAMSendSql obj = new JAMSendSql(JAMLibKernel.ParamJAMInstname, 
        JAMLibKernel.ParamJAMUsername, 
        JAMLibKernel.ParamJAMUserpass);
      
      obj.setTransactions(PARAMargGroupTransactions);
      obj.setInstanciasAlternativa(PARAMargInstanciasAlternativas);
      if (PARAMlogDepuraComandsSqls) {
        obj.setCommands(getDepuraComandosArrayNulos(PARAMargSqls));
      } else {
        obj.setCommands(PARAMargSqls);
      }
      obj.setConexiones(PARAMargConexionesActivas, PARAMlogConexionesHistoricas);
      obj.setConexionCerrar(PARAMargConexionCerrar, PARAMlogCierraYBloqueaConexion);
      obj.setMensaje(PARAMargMensajes);
      
      URL miurl = new URL(JAMLibKernel.JAMURL_SERVLETS + "JAMServeletDB");
      URLConnection conexion = miurl.openConnection();
      conexion.setDoInput(true);
      conexion.setDoOutput(true);
      conexion.setUseCaches(false);
      conexion.setDefaultUseCaches(false);
      conexion.setRequestProperty("Content-Type", "java-internal/" + obj.getClass().getName());
      conexion.connect();
      
      ObjectOutputStream output = new ObjectOutputStream(conexion.getOutputStream());
      output.writeObject(obj);
      output.flush();
      
      ObjectInputStream input = new ObjectInputStream(conexion.getInputStream());
      Object response = input.readObject();
      JAMSendSql recibe = (JAMSendSql)response;
      
      output.close();
      input.close();
      
      System.out.println("Recibido en Miles Segundos : " + JAMUtil.JAMTimeElapsed());
      if (recibe.getError() != null)
      {
        System.out.println("Error al recibir : " + recibe.getError());
        throw new Exception(recibe.getError());
      }
      PARAMargConexionesActivas = recibe.getConexiones();
      PARAMargConexionCerrar = recibe.getConexionCerrar();
      PARAMargMensajes = recibe.getMensaje();
      return recibe.getRowSets();
    }
    catch (IOException e)
    {
      throw new Exception("IOException : " + e.getMessage());
    }
    catch (ClassNotFoundException e)
    {
      throw new Exception("ClassNotFoundException : " + e.getMessage());
    }
    catch (Exception e)
    {
      throw new Exception("Exception : " + e.getMessage());
    }
  }
  
  private static String[] getDepuraComandosArrayNulos(String[] argComandos)
  {
    if (argComandos == null) {
      return null;
    }
    Vector<String> vct = new Vector();
    for (int i = 0; i < argComandos.length; i++)
    {
      String strCmd = JAMUtil.JAMConvNullStr(argComandos[i]);
      if (!strCmd.equalsIgnoreCase("")) {
        vct.addElement(strCmd);
      }
    }
    String[] arrReto = new String[vct.size()];
    for (int i = 0; i < vct.size(); i++) {
      arrReto[i] = ((String)vct.get(i));
    }
    return arrReto;
  }
  
  private static JAMRowSet[] doCallToServerWS()
    throws Exception
  {
    JAMSendSql objSendRecibSql = null;
    try
    {
      System.out.println("Iniciando WebService JAMSendDbWS " + JAMUtil.JAMTimeNow());
      JAMUtil.JAMTimeInit();
      if (PARAMargSqls != null) {
        System.out.println("Cantidad de rowSets : " + PARAMargSqls.length);
      } else if (PARAMargGroupTransactions != null) {
        System.out.println("Cantidad de Transacciones : " + PARAMargGroupTransactions.getLength());
      } else if (PARAMargConexionesActivas != null) {
        System.out.println("Solicita Conexiones Activas");
      } else if (PARAMargConexionCerrar != null) {
        System.out.println("Envia a Cerrar Conexi��n : " + PARAMargConexionCerrar);
      } else if (PARAMargMensajes != null) {
        System.out.println("Solicita Mensajeria en Modo : " + PARAMargMensajes.getModalidad());
      }
      objSendRecibSql = new JAMSendSql(JAMLibKernel.ParamJAMInstname, 
        JAMLibKernel.ParamJAMUsername, 
        JAMLibKernel.ParamJAMUserpass);
      
      objSendRecibSql.setTransactions(PARAMargGroupTransactions);
      objSendRecibSql.setInstanciasAlternativa(PARAMargInstanciasAlternativas);
      if (PARAMlogDepuraComandsSqls) {
        objSendRecibSql.setCommands(getDepuraComandosArrayNulos(PARAMargSqls));
      } else {
        objSendRecibSql.setCommands(PARAMargSqls);
      }
      objSendRecibSql.setConexiones(PARAMargConexionesActivas, PARAMlogConexionesHistoricas);
      objSendRecibSql.setConexionCerrar(PARAMargConexionCerrar, PARAMlogCierraYBloqueaConexion);
      objSendRecibSql.setMensaje(PARAMargMensajes);
      
      objSendRecibSql = getSendSqlWS(objSendRecibSql);
      
      System.out.println("Recibido en Miles Segundos : " + JAMUtil.JAMTimeElapsed());
      if (objSendRecibSql.getError() != null)
      {
        System.out.println("Error al recibir : " + objSendRecibSql.getError());
        throw new Exception(objSendRecibSql.getError());
      }
    }
    catch (IOException e)
    {
      throw new Exception("IOException : " + e.getMessage());
    }
    catch (ServiceException e)
    {
      throw new Exception("ServiceException : " + e.getMessage());
    }
    catch (ClassNotFoundException e)
    {
      throw new Exception("ClassNotFoundException : " + e.getMessage());
    }
    catch (Exception e)
    {
      throw new Exception("Exception : " + e.getMessage());
    }
    PARAMargConexionesActivas = objSendRecibSql.getConexiones();
    PARAMargConexionCerrar = objSendRecibSql.getConexionCerrar();
    PARAMargMensajes = objSendRecibSql.getMensaje();
    return objSendRecibSql.getRowSets();
  }
  
  private static JAMSendSql getSendSqlWS(JAMSendSql argSendSql)
    throws IOException, ServiceException, ClassNotFoundException
  {
    DataHandler[] dhsSend = new DataHandler[1];
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    
    GZIPOutputStream zos = new GZIPOutputStream(bos);
    ObjectOutputStream os = new ObjectOutputStream(zos);
    os.writeObject(argSendSql);
    zos.finish();
    dhsSend[0] = new DataHandler(new ByteArrayDataSource(bos.toByteArray(), "JAMMexico/dataAction"));
    
    JAMSendDbWSServiceLocator myServer = new JAMSendDbWSServiceLocator();
    JAMSendDbWS aws = myServer.getJAMSendDbWS(new URL(JAMLibKernel.JAMURL_WEBSERVERDB));
    DataHandler[] dhs = aws.procesa(dhsSend);
    
    GZIPInputStream zis = new GZIPInputStream(dhs[0].getInputStream());
    ObjectInputStream ois = new ObjectInputStream(zis);
    JAMSendSql objRec = (JAMSendSql)ois.readObject();
    
    ois.close();
    os.close();
    zos.close();
    
    return objRec;
  }
}
