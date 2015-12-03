package mx.com.jammexico.jamdb;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import mx.com.jammexico.jamsrv.JAMRowSet;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAMMensajes
  implements Serializable
{
  public static final String ENVIAR = "Enviar";
  public static final String RECIBIR = "Recibir";
  public static final String CONSULTAR = "Consultar";
  
  class doMensaje
    implements Serializable
  {
    private String strMensaje = null;
    private boolean logEnviado = false;
    
    protected doMensaje(String argMensaje)
    {
      this.strMensaje = argMensaje;
    }
    
    protected String getMensaje()
    {
      this.logEnviado = true;
      return this.strMensaje;
    }
    
    protected boolean isEnviado()
    {
      return this.logEnviado;
    }
  }
  
  private String strModalidad = null;
  private String strUsuario = null;
  private String strMensaje = null;
  private Hashtable<String, Vector<doMensaje>> htbMensajes = new Hashtable();
  private Hashtable<String, Vector<doMensaje>> htbMensajesSinLeer = null;
  
  public void setModalidad(String argModalidad)
  {
    this.strModalidad = argModalidad;
  }
  
  public String getModalidad()
  {
    return this.strModalidad;
  }
  
  public void setUsuario(String argUsuario)
  {
    this.strUsuario = argUsuario;
  }
  
  public String getUsuario()
  {
    return this.strUsuario;
  }
  
  public void setMensaje(String argMensaje)
  {
    this.strMensaje = argMensaje;
  }
  
  public String getMensaje()
  {
    return this.strMensaje;
  }
  
  public void setMensaje(String argClave, String argMensaje)
  {
    Vector<doMensaje> vctLineas = null;
    if (this.htbMensajes.containsKey(argClave)) {
      vctLineas = (Vector)this.htbMensajes.get(argClave);
    } else {
      vctLineas = new Vector();
    }
    vctLineas.add(new doMensaje(argMensaje));
    
    this.htbMensajes.put(argClave, vctLineas);
  }
  
  public String getMensaje(String argClave)
  {
    String strRetoMensaje = "";
    if (this.htbMensajes.containsKey(argClave))
    {
      Vector<doMensaje> vctLineas = (Vector)this.htbMensajes.get(argClave);
      for (doMensaje msjl : vctLineas) {
        if (!msjl.isEnviado()) {
          strRetoMensaje = strRetoMensaje + argClave + " : " + msjl.getMensaje() + JAMUtil.getCrlf();
        }
      }
    }
    if (!strRetoMensaje.equalsIgnoreCase("")) {
      strRetoMensaje = strRetoMensaje.substring(0, strRetoMensaje.length() - 2);
    }
    return strRetoMensaje;
  }
  
  public Hashtable<String, Vector<doMensaje>> getMensajesNoLeidos()
  {
    this.htbMensajesSinLeer = new Hashtable();
    
    Enumeration<Vector<doMensaje>> ek = this.htbMensajes.elements();
    Enumeration<String> ekClave = this.htbMensajes.keys();
    while (ek.hasMoreElements())
    {
      String keye = (String)ekClave.nextElement();
      Vector<doMensaje> msgk = (Vector)ek.nextElement();
      Vector<doMensaje> msgReto = new Vector();
      for (doMensaje msjl : msgk) {
        if (!msjl.isEnviado()) {
          msgReto.add(msjl);
        }
      }
      if (msgReto.size() != 0) {
        this.htbMensajesSinLeer.put(keye, msgReto);
      }
    }
    return this.htbMensajesSinLeer;
  }
  
  public void setMensajesNoLeidos(Hashtable<String, Vector<doMensaje>> argMensajes)
  {
    this.htbMensajesSinLeer = argMensajes;
  }
  
  public void getMensajesNoLeidos(JAMRowSet argRowSet)
  {
    try
    {
      argRowSet.beforeFirst();
      while (argRowSet.next()) {
        if (this.htbMensajesSinLeer.containsKey(argRowSet.getString("CLAVE")))
        {
          Vector<doMensaje> argMensajes = (Vector)this.htbMensajesSinLeer.get(argRowSet.getString("CLAVE"));
          for (doMensaje msjl : argMensajes) {
            if (!msjl.isEnviado())
            {
              argRowSet.moveToCurrentRow();
              argRowSet.updateInt("ORDEN", 1);
              argRowSet.updateRow();
              break;
            }
          }
        }
      }
    }
    catch (Exception localException) {}
  }
}
