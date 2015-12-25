package mx.com.jammexico.jamdb;

import ch.ubique.inieditor.IniEditor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.rowset.spi.SyncProviderException;
import javax.sql.rowset.spi.SyncResolver;
import mx.com.jammexico.jamsrv.JAMDataAction;
import mx.com.jammexico.jamsrv.JAMDataActionsGroup;
import mx.com.jammexico.jamsrv.JAMRowSet;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAMDataBase
{
  private static final int INSTANCIA = 0;
  private static final int USUARIO = 1;
  private static final int PASSWORD = 2;
  private static final int DRIVER = 3;
  private static final int CONNECT = 4;
  private static ConcurrentHashMap<String, JAMDataConn> htbConnections = new ConcurrentHashMap();
  private static ConcurrentHashMap<String, JAMDataConn> htbConnectionsHistorico = new ConcurrentHashMap();
  private static final long lngTimeOutPool = 600000L;
  private JAMDataConn conDb = null;
  private String strCommandOld = null;
  private int intRegisters = 10000;
  private int intModalidadDbClose = 0;
  private int intTimeOut = 300000;
  private int intMaxPool = 15;
  private int intMaxPoolClose = this.intMaxPool;
  
  public JAMDataBase(String argInstancia, String argUser, String argPass)
    throws Exception, IOException, ClassNotFoundException, SQLException
  {
    synchronized (argInstancia)
    {
      synchronized (argUser)
      {
        synchronized (argPass)
        {
          init(argInstancia, argUser, argPass, null);
        }
      }
    }
  }
  
  public JAMDataBase(String argInstancia, String argUser, String argPass, String argInstanciaAlternativa)
    throws Exception, IOException, ClassNotFoundException, SQLException
  {
    synchronized (argInstancia)
    {
      synchronized (argUser)
      {
        synchronized (argPass)
        {
          if (argInstanciaAlternativa != null) {
            synchronized (argInstanciaAlternativa)
            {
              init(argInstancia, argUser, argPass, argInstanciaAlternativa);
            }
          }
          init(argInstancia, argUser, argPass, null);
        }
      }
    }
  }
  
  private synchronized void init(String argInstancia, String argUser, String argPass, String argInstanciaAlternativa)
    throws Exception, IOException, ClassNotFoundException, SQLException
  {
    doLeeConfiguracionWS();
    String[] strStringDB = doLeeConfiguracionDB(argInstancia, argUser, argPass, argInstanciaAlternativa);
    if (this.intModalidadDbClose == 1) {
      conJAMDataPoolOk(strStringDB);
    } else if (this.intModalidadDbClose == 2) {
      conJAMDataPoolOk(strStringDB);
    } else {
      this.conDb = newJAMDataConn(strStringDB);
    }
    Runtime.getRuntime().gc();
  }
  
  private synchronized String[] doLeeConfiguracionDB(String argInstancia, String argUser, String argPass, String argInstanciaAlternativa)
    throws Exception
  {
    FileInputStream file = new FileInputStream("C:\\JAMWebService\\JAMDBConfig\\conections.ini");
    IniEditor e = new IniEditor();
    e.load(file);
    
    String strInstanciaReal = argInstancia;
    String strUserReal = argUser;
    String strPassReal = argPass;
    if (argInstanciaAlternativa != null) {
      strInstanciaReal = argInstanciaAlternativa;
    }
    String drivername = e.get(strInstanciaReal, "DriverName");
    String connectstring = e.get(strInstanciaReal, "ConnectString");
    String userMaster = e.get(strInstanciaReal, "usrmaster");
    String passMaster = e.get(strInstanciaReal, "pwdmaster");
    String registers = e.get(strInstanciaReal, "registers");
    String timeout = e.get(strInstanciaReal, "timeout");
    if ((drivername == null) || (connectstring == null)) {
      throw new Exception("No se Encontro en el Archivo de Configuraci��n la Instancia");
    }
    if ((userMaster == null) || (passMaster == null)) {
      throw new Exception("No se Encontro el Usuario Master");
    }
    if (registers != null) {
      this.intRegisters = new Integer(registers).intValue();
    }
    if (timeout != null) {
      this.intTimeOut = new Integer(timeout).intValue();
    }
    if (argInstanciaAlternativa != null)
    {
      strUserReal = userMaster;
      strPassReal = passMaster;
    }
    file.close();
    return new String[] { strInstanciaReal, strUserReal, strPassReal, drivername, connectstring };
  }
  
  private synchronized void doLeeConfiguracionWS()
  {
    try
    {
      FileInputStream file = new FileInputStream("C:\\JAMWebService\\JAMDBConfig\\webservices.ini");
      IniEditor e = new IniEditor();
      e.load(file);
      
      String registers = e.get("JAMDataBase", "registers");
      String timeout = e.get("JAMDataBase", "timeout");
      String maxpool = e.get("JAMDataBase", "pool");
      String maxpoolclose = e.get("JAMDataBase", "poolclose");
      String modalidad = e.get("JAMDataBase", "modalidad");
      if (registers != null) {
        this.intRegisters = new Integer(registers).intValue();
      }
      if (timeout != null) {
        this.intTimeOut = new Integer(timeout).intValue();
      }
      if (maxpool != null) {
        this.intMaxPool = new Integer(maxpool).intValue();
      }
      if (modalidad != null) {
        this.intModalidadDbClose = new Integer(modalidad).intValue();
      }
      if (maxpoolclose != null)
      {
        this.intMaxPoolClose = new Integer(maxpoolclose).intValue();
        this.intMaxPoolClose = (this.intMaxPoolClose == 0 ? 1 : this.intMaxPoolClose);
        this.intMaxPoolClose = (this.intMaxPoolClose > this.intMaxPool ? this.intMaxPool : this.intMaxPoolClose);
      }
      file.close();
    }
    catch (Exception e)
    {
      System.out.println("Sin configurar WebServices, se toman valores default : " + e.getMessage());
    }
  }
  
  private synchronized void conJAMDataPoolOk(String[] strStringDB)
    throws Exception, SQLException, ClassNotFoundException
  {
    synchronized (strStringDB)
    {
      try
      {
        if (htbConnections.containsKey(strStringDB[0] + "|" + strStringDB[1]))
        {
          this.conDb = ((JAMDataConn)htbConnections.get(strStringDB[0] + "|" + strStringDB[1]));
          if (this.conDb.isBloqueado()) {
            throw new Exception("Usuario Bloqueado por Mantenimiento");
          }
        }
        else if (htbConnections.size() < this.intMaxPool)
        {
          this.conDb = newJAMDataConn(strStringDB);
          this.conDb.setNovedadConexion("Abierto con Pool Liberado");
          putConnections(this.conDb.getClave(), this.conDb);
        }
        else
        {
          doCleanPool(strStringDB, false);
        }
      }
      catch (Exception eg)
      {
        if (eg.getMessage().toLowerCase().indexOf("(cannot start thread)".toLowerCase()) != -1) {
          doCleanPool(strStringDB, true);
        } else {
          throw new Exception(eg);
        }
      }
    }
  }
  
  private synchronized void doCleanPool(String[] strStringDB, boolean logConError)
    throws Exception
  {
    ArrayList<JAMDataConexionesInfo> listConexiones = getConexionesActivas();
    boolean logCerroUnaAbroActiva = false;
    for (int i = 0; i < listConexiones.size(); i++)
    {
      if (i > this.intMaxPoolClose) {
        break;
      }
      if (htbConnections.containsKey(((JAMDataConexionesInfo)listConexiones.get(i)).getClave()))
      {
        JAMDataConn objConn = (JAMDataConn)htbConnections.get(((JAMDataConexionesInfo)listConexiones.get(i)).getClave());
        if ((!objConn.isBloqueado()) && 
          (objConn.close()) && (!logCerroUnaAbroActiva))
        {
          logCerroUnaAbroActiva = true;
          this.conDb = newJAMDataConn(strStringDB);
          if (logConError) {
            this.conDb.setNovedadConexion("Abierto a trav��s de Pool CON error de thread");
          } else {
            this.conDb.setNovedadConexion("Abierto a trav��s de Pool SIN error de thread");
          }
          putConnections(this.conDb.getClave(), this.conDb);
        }
      }
    }
  }
  
  private synchronized void putConnections(String argClave, JAMDataConn argConn)
  {
    htbConnections.put(argClave, argConn);
    htbConnectionsHistorico.put(argClave, argConn);
  }
  
  private synchronized JAMDataConn newJAMDataConn(String[] strStringDB)
    throws Exception, SQLException, ClassNotFoundException
  {
    Properties props = new Properties();
    props.setProperty("user", strStringDB[1]);
    props.setProperty("password", strStringDB[2]);
    props.setProperty("roleName", "JAM_SOC");
    
    return new JAMDataConn(strStringDB[0], 
      strStringDB[3], 
      strStringDB[4], 
      props, 
      htbConnections, 
      new Integer(this.intModalidadDbClose));
  }
  
  public synchronized void inactiveDB()
  {
    if (this.conDb != null) {
      this.conDb.setActivoDB(false);
    }
  }
  
  public synchronized int getTimeOut()
  {
    return this.intTimeOut;
  }
  
  public synchronized JAMRowSet[] getRowSet(String[] argCommand)
    throws Exception
  {
    JAMRowSet[] rst = new JAMRowSet[argCommand.length];
    synchronized (this.conDb)
    {
      try
      {
        this.conDb.setAutoCommit(false);
        for (int i = 0; i < argCommand.length; i++)
        {
          String strCommand = JAMUtil.JAMConvNullStr(argCommand[i]);
          if (!strCommand.equalsIgnoreCase("")) {
            if (strCommand.toUpperCase().startsWith("SELECT")) {
              try
              {
                JAMRowSet rw = new JAMRowSet();
                rw.setCommand(getCommand(strCommand));
                rw.execute(this.conDb.getConnectionInternal());
                rw.setCommand(this.strCommandOld);
                rst[i] = rw;
              }
              catch (SQLException eg)
              {
                throw new SQLException("Error Ejecutar RowSet : " + eg.getMessage());
              }
              catch (Exception eg)
              {
                throw new SQLException("Error Ejecutar RowSet : " + eg.getMessage());
              }
            } else {
              try
              {
                Statement stmt = this.conDb.getConnectionInternal().createStatement();
                stmt.execute(strCommand);
              }
              catch (SQLException eg)
              {
                throw new SQLException("Error Ejecutar Sentencia : " + eg.getMessage() + JAMUtil.getComillas() + strCommand);
              }
              catch (Exception eg)
              {
                throw new SQLException("Error Ejecutar Sentencia : " + eg.getMessage() + JAMUtil.getComillas() + strCommand);
              }
            }
          }
        }
      }
      catch (SQLException e)
      {
        this.conDb.setActivoDB(false);
        throw new Exception(this.conDb.getInstancia() + " " + e.getMessage());
      }
      catch (Exception e)
      {
        this.conDb.setActivoDB(false);
        throw new Exception(this.conDb.getInstancia() + " " + e.getMessage());
      }
      this.conDb.commit();
    }
    return rst;
  }
  
  public synchronized void setTransactions(JAMDataActionsGroup recivedGroup)
    throws Exception
  {
    String strTabla = null;
    synchronized (this.conDb)
    {
      try
      {
        this.conDb.setAutoCommit(false);
        try
        {
          for (int i = recivedGroup.getLength() - 1; i >= 0; i--)
          {
            JAMDataAction action = recivedGroup.getAction(i);
            if (action.getType() == 5)
            {
              JAMRowSet ws = action.getRowSet();
              strTabla = action.getname();
              ws.cUsuario = this.conDb.getUser();
              ws.setGroup(recivedGroup);
              ws.setConn(this.conDb.getConnectionInternal());
              ws.submitDeleted();
            }
          }
        }
        catch (SQLException e)
        {
          strTabla = JAMUtil.JAMConvNullStr(strTabla);
          throw new Exception("ERROR TRANSACCION al DELETE del RowSets (" + strTabla + ") " + JAMUtil.getCrlf() + e.getMessage());
        }
        for (int i = 0; i < recivedGroup.getLength(); i++)
        {
          JAMDataAction action = recivedGroup.getAction(i);
          try
          {
            JAMRowSet ws = action.getRowSet();
            strTabla = action.getname();
            ws.setGroup(recivedGroup);
            ws.setConn(this.conDb.getConnectionInternal());
            ws.submitInsertedUpdated();
          }
          catch (SyncProviderException spe)
          {
            String strReto = "";
            SyncResolver rev = spe.getSyncResolver();
            while (rev.nextConflict()) {
              strReto = strReto + rev.getStatus() + JAMUtil.getCrlf();
            }
            strTabla = JAMUtil.JAMConvNullStr(strTabla);
            throw new Exception("Error TRANSACCION al INSERT y UPDATE del RowSets : (" + strTabla + ") " + JAMUtil.getCrlf() + 
              spe.getLocalizedMessage() + JAMUtil.getCrlf() + strReto);
          }
          catch (SQLException e)
          {
            strTabla = JAMUtil.JAMConvNullStr(strTabla);
            throw new Exception("Error TRANSACCION al INSERT y UPDATE del RowSets : (" + strTabla + ") " + JAMUtil.getCrlf() + 
              e.getLocalizedMessage());
          }
        }
        this.conDb.commit();
        return;
      }
      catch (SQLException e)
      {
        this.conDb.rollback();
        strTabla = JAMUtil.JAMConvNullStr(strTabla);
        throw new Exception(this.conDb.getInstancia() + " SQLException : ProcessTransaction Error de SQL (" + strTabla + ") " + JAMUtil.getCrlf() + e.getMessage());
      }
      catch (Exception e)
      {
        this.conDb.rollback();
        strTabla = JAMUtil.JAMConvNullStr(strTabla);
        throw new Exception(this.conDb.getInstancia() + " Exception : ProcessTransaction Error de SQL (" + strTabla + ") " + JAMUtil.getCrlf() + e.getMessage());
      }
    }
  }
  
  public synchronized ArrayList<JAMDataConexionesInfo> getConexionesActivas()
  {
    return getConexiones(htbConnections);
  }
  
  public synchronized ArrayList<JAMDataConexionesInfo> getConexionesHistoricas()
  {
    return getConexiones(htbConnectionsHistorico);
  }
  
  public synchronized String setCerrarConexion(String argConexion, boolean argBloquea)
  {
    if (!htbConnections.containsKey(argConexion)) {
      return "No se Encontro la Conexi��n";
    }
    JAMDataConn conDbT = (JAMDataConn)htbConnections.get(argConexion);
    if (conDbT.isActivoDB()) {
      return "Instancia Activa";
    }
    if (conDbT.close(argBloquea)) {
      return argBloquea ? "Conexi��n Liberada y bloqueada" : "Conexi��n Liberada";
    }
    if ((conDbT.isBloqueado()) && (!argBloquea)) {
      return "Conexi��n Desbloqueada";
    }
    if ((conDbT.isBloqueado()) && (argBloquea)) {
      return "Conexi��n ya esta bloqueada";
    }
    return "No se pudo Cerrar";
  }
  
  public synchronized JAMMensajes setMensajes(JAMMensajes argMensajes)
  {
    if (argMensajes.getModalidad().equalsIgnoreCase("Enviar"))
    {
      if (htbConnectionsHistorico.containsKey(argMensajes.getUsuario()))
      {
        JAMDataConn conDbMsg = (JAMDataConn)htbConnectionsHistorico.get(argMensajes.getUsuario());
        JAMMensajes objMensajes = conDbMsg.getMensajes();
        objMensajes.setMensaje(this.conDb.getClave(), argMensajes.getMensaje());
        conDbMsg.setMensajes(objMensajes);
      }
    }
    else if (argMensajes.getModalidad().equalsIgnoreCase("Recibir"))
    {
      this.conDb.setMensajes(this.conDb.getMensajes());
      argMensajes.setMensaje(this.conDb.getMensajes().getMensaje(argMensajes.getUsuario()));
    }
    else if (argMensajes.getModalidad().equalsIgnoreCase("Consultar"))
    {
      this.conDb.setMensajes(this.conDb.getMensajes());
      argMensajes.setMensajesNoLeidos(this.conDb.getMensajes().getMensajesNoLeidos());
    }
    return argMensajes;
  }
  
  private synchronized String getCommand(String argCommand)
  {
    this.strCommandOld = argCommand;
    if (argCommand.toLowerCase().indexOf("first") != -1) {
      return this.strCommandOld;
    }
    if (!argCommand.toLowerCase().startsWith("select")) {
      return this.strCommandOld;
    }
    String retoCommand = argCommand.substring(6).trim();
    retoCommand = "SELECT FIRST " + this.intRegisters + " " + retoCommand;
    
    return retoCommand;
  }
  
  private synchronized ArrayList<JAMDataConexionesInfo> getConexiones(ConcurrentHashMap<String, JAMDataConn> arghstMap)
  {
    ArrayList<JAMDataConexionesInfo> listConexiones = new ArrayList();
    Enumeration<JAMDataConn> ek = arghstMap.elements();
    while (ek.hasMoreElements()) {
      listConexiones.add(new JAMDataConexionesInfo((JAMDataConn)ek.nextElement(), 600000L));
    }
    Collections.sort(listConexiones, new Comparator()
    {
      

        @Override
        public int compare(Object o1_, Object o2_) {
            
            JAMDataConexionesInfo o1= (JAMDataConexionesInfo) o1_; 
            JAMDataConexionesInfo o2= (JAMDataConexionesInfo) o2_;
            
            return o2.getTime() - o1.getTime();
        }
    });
    return listConexiones;
  }
}
