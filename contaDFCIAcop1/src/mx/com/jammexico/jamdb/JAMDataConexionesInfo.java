package mx.com.jammexico.jamdb;

import java.io.Serializable;
import java.util.Date;

public class JAMDataConexionesInfo
  implements Serializable
{
  private static final long serialVersionUID = 5024811827991619717L;
  private String strClave = null;
  private int lngTime = 0;
  private String strStatus = null;
  private String strNovedades = null;
  private String strInstancia = null;
  private String strUsuario = null;
  private Date datDiaAbierto = null;
  private boolean logBloqueado = false;
  private String strBloqueado = null;
  
  public JAMDataConexionesInfo(JAMDataConn argDbT, long argTimeOutPool)
  {
    this.strClave = argDbT.getClave();
    this.lngTime = argDbT.getTimeOpen();
    if (this.lngTime == 0) {
      this.strStatus = "Procesando";
    } else if ((argDbT.isActivoDB()) || (this.lngTime < argTimeOutPool)) {
      this.strStatus = "Activo";
    } else if (this.lngTime == 86400000) {
      this.strStatus = "Inactivo m��s de 1 d��a";
    } else {
      this.strStatus = "Inactivo";
    }
    this.strUsuario = argDbT.getUser();
    this.strInstancia = argDbT.getInstancia();
    this.strNovedades = argDbT.getNovedadConexion();
    this.datDiaAbierto = argDbT.getDiaApertura();
    this.logBloqueado = argDbT.isBloqueado();
    this.strBloqueado = (this.logBloqueado ? "Bloqueado" : "Desbloqueado");
  }
  
  public boolean isBloqueado()
  {
    return this.logBloqueado;
  }
  
  public String getBloqueado()
  {
    return this.strBloqueado;
  }
  
  public Date getDiaApertura()
  {
    return this.datDiaAbierto;
  }
  
  public String getUsuario()
  {
    return this.strUsuario;
  }
  
  public String getInstancia()
  {
    return this.strInstancia;
  }
  
  public String getClave()
  {
    return this.strClave;
  }
  
  public String getStatus()
  {
    return this.strStatus;
  }
  
  public String getNovedades()
  {
    return this.strNovedades;
  }
  
  public int getTime()
  {
    return this.lngTime;
  }
}
