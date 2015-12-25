package mx.com.jammexico.jamdb;

import java.io.Serializable;
import java.util.ArrayList;
import mx.com.jammexico.jamsrv.JAMDataActionsGroup;
import mx.com.jammexico.jamsrv.JAMRowSet;

public class JAMSendSql
  implements Serializable, Cloneable
{
  private static final long serialVersionUID = 1L;
  private String strInstancias = null;
  private String strUsuario = null;
  private String strClave = null;
  private String[] arrSqls = null;
  private String strError = null;
  private JAMRowSet[] arrRowSets = null;
  private String strInstanciasAlternativa = null;
  private JAMDataActionsGroup objGroupTransactions = null;
  private ArrayList<JAMDataConexionesInfo> arrConexiones = null;
  private String strConexionCerrar = null;
  private JAMMensajes objMensaje = null;
  private boolean logConexionesHistoricas = false;
  private boolean logConexionBloquea = false;
  
  public JAMSendSql(String argInstancia, String argUsuario, String argClave)
  {
    this.strInstancias = argInstancia;
    this.strUsuario = argUsuario;
    this.strClave = argClave;
  }
  
  public void setRowSets(JAMRowSet[] argRowSets)
  {
    this.arrRowSets = argRowSets;
  }
  
  public JAMRowSet[] getRowSets()
  {
    return this.arrRowSets;
  }
  
  public void setCommands(String[] argSqls)
  {
    this.arrSqls = argSqls;
  }
  
  public String[] getCommands()
  {
    return this.arrSqls;
  }
  
  public void setError(String argError)
  {
    this.strError = argError;
  }
  
  public String getError()
  {
    return this.strError;
  }
  
  public String getInstancia()
  {
    return this.strInstancias;
  }
  
  public String getUsuario()
  {
    return this.strUsuario;
  }
  
  public String getClave()
  {
    return this.strClave;
  }
  
  public ArrayList<JAMDataConexionesInfo> getConexiones()
  {
    return this.arrConexiones;
  }
  
  public String getConexionCerrar()
  {
    return this.strConexionCerrar;
  }
  
  public JAMMensajes getMensaje()
  {
    return this.objMensaje;
  }
  
  public void setMensaje(JAMMensajes argMensaje)
  {
    this.objMensaje = argMensaje;
  }
  
  public void setConexionCerrar(String argCerrar, boolean argBloqueaConexion)
  {
    this.strConexionCerrar = argCerrar;
    this.logConexionBloquea = argBloqueaConexion;
  }
  
  public void setConexiones(ArrayList<JAMDataConexionesInfo> argConnActivas, boolean argHistorico)
  {
    this.arrConexiones = argConnActivas;
    this.logConexionesHistoricas = argHistorico;
  }
  
  public boolean isHistorico()
  {
    return this.logConexionesHistoricas;
  }
  
  public boolean isBloquea()
  {
    return this.logConexionBloquea;
  }
  
  public void setInstanciasAlternativa(String argInstanciaAlternativa)
  {
    this.strInstanciasAlternativa = argInstanciaAlternativa;
  }
  
  public String getInstanciaAlternativa()
  {
    return this.strInstanciasAlternativa;
  }
  
  public void setTransactions(JAMDataActionsGroup argGroupTransactions)
  {
    this.objGroupTransactions = argGroupTransactions;
  }
  
  public JAMDataActionsGroup getTransactions()
  {
    return this.objGroupTransactions;
  }
}
