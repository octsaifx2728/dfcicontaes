package mx.com.jammexico.jamdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class JAMDataConn
{
  public static final int MODALIDAD_CERO_CONEXIONES = 0;
  public static final int MODALIDAD_POOL_CONEXIONES = 1;
  private Connection conn = null;
  private boolean logActivo = true;
  private Date timeInicio = null;
  private String strDriverDB = null;
  private String strConnect = null;
  private String strInstancia = null;
  private Properties prpPropiedades = null;
  private String strClave = null;
  private int intModalidadDbClose = 0;
  private String strNovedadConexion = null;
  private ConcurrentHashMap<String, JAMDataConn> htbConnections = new ConcurrentHashMap();
  private JAMMensajes objMensajes = null;
  private boolean logBloquea = false;
  
  public JAMDataConn(String argInstancia, String argDriverDB, String argConnectstring, Properties argProps, ConcurrentHashMap<String, JAMDataConn> argConnections, Integer argModalidadDbClose)
    throws Exception, SQLException, ClassNotFoundException
  {
    synchronized (argInstancia)
    {
      this.strInstancia = argInstancia;
      synchronized (argDriverDB)
      {
        this.strDriverDB = argDriverDB;
        synchronized (argConnectstring)
        {
          this.strConnect = argConnectstring;
          synchronized (argProps)
          {
            this.prpPropiedades = argProps;
            synchronized (argConnections)
            {
              this.htbConnections = argConnections;
              synchronized (argModalidadDbClose)
              {
                this.intModalidadDbClose = new Integer(argModalidadDbClose.intValue()).intValue();
                
                this.strClave = (getInstancia() + "|" + getUser());
                open();
              }
            }
          }
        }
      }
    }
  }
  
  public synchronized String getClave()
  {
    return this.strClave;
  }
  
  public synchronized Date getDiaApertura()
  {
    return this.timeInicio;
  }
  
  public synchronized int getTimeOpen()
  {
    if (new Date().getTime() - this.timeInicio.getTime() > 86400000L) {
      return 86400000;
    }
    return new Long(new Date().getTime() - this.timeInicio.getTime()).intValue();
  }
  
  public synchronized Connection getConnectionInternal()
    throws Exception
  {
    this.timeInicio = Calendar.getInstance().getTime();
    return this.conn;
  }
  
  private synchronized void open()
    throws Exception, ClassNotFoundException, SQLException
  {
    this.timeInicio = Calendar.getInstance().getTime();
    Class.forName(this.strDriverDB);
    this.conn = DriverManager.getConnection(this.strConnect, this.prpPropiedades);
  }
  
  public synchronized boolean isActivoDB()
  {
    if (getTimeOpen() == 0) {
      return true;
    }
    return this.logActivo;
  }
  
  public synchronized void setActivoDB(boolean argActivo)
  {
    this.logActivo = argActivo;
    this.timeInicio = Calendar.getInstance().getTime();
  }
  
  public synchronized void setAutoCommit(boolean argCommit)
    throws Exception
  {
    try
    {
      if (this.conn.isClosed())
      {
        open();
        setNovedadConexion("Conexi��n Cerrada. Se abre la conexi��n en mismo Pool");
      }
    }
    catch (SQLException e)
    {
      throw new Exception(e);
    }
    catch (ClassNotFoundException e)
    {
      throw new Exception(e);
    }
    setActivoDB(true);
    this.conn.setAutoCommit(argCommit);
  }
  
  public synchronized void commit()
    throws Exception
  {
    this.conn.commit();
    setActivoDB(false);
  }
  
  public synchronized void rollback()
    throws Exception
  {
    this.conn.rollback();
    setActivoDB(false);
  }
  
  public synchronized void setBloquea(boolean argBloquea)
  {
    this.logBloquea = argBloquea;
  }
  
  public synchronized boolean isClosed()
  {
    try
    {
      return this.conn.isClosed();
    }
    catch (SQLException e) {}
    return false;
  }
  
  public synchronized boolean isBloqueado()
  {
    return this.logBloquea;
  }
  
  public synchronized boolean close(boolean argBloquea)
  {
    setBloquea(argBloquea);
    return close();
  }
  
  public synchronized boolean close()
  {
    boolean logReto = false;
    try
    {
      if (!isActivoDB())
      {
        if (!this.conn.isClosed())
        {
          this.conn.close();
          logReto = true;
        }
        if (!this.logBloquea)
        {
          this.htbConnections.remove(getClave());
          logReto = true;
        }
      }
    }
    catch (SQLException localSQLException) {}
    return logReto;
  }
  
  public synchronized String getUser()
  {
    return this.prpPropiedades.getProperty("user");
  }
  
  public synchronized String getInstancia()
  {
    return this.strInstancia;
  }
  
  public synchronized int getModalidad()
  {
    return this.intModalidadDbClose;
  }
  
  public synchronized void setNovedadConexion(String argNovedadConexion)
  {
    this.strNovedadConexion = argNovedadConexion;
  }
  
  public synchronized String getNovedadConexion()
  {
    return this.strNovedadConexion;
  }
  
  public synchronized void setMensajes(JAMMensajes argMensajes)
  {
    this.objMensajes = argMensajes;
  }
  
  public synchronized JAMMensajes getMensajes()
  {
    if (this.objMensajes == null) {
      this.objMensajes = new JAMMensajes();
    }
    return this.objMensajes;
  }
}
