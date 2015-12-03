package mx.com.jammexico.jamservlets;

import java.io.IOException;
import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class JAMServLet
  extends HttpServlet
{
  public static final int doGet = 0;
  public static final int doPost = 1;
  private int intModo = 0;
  private HttpServletRequest requestX = null;
  private HttpServletResponse responseX = null;
  private Hashtable<String, HttpSession> hstSesiones = null;
  
  protected abstract void doGet(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
    throws ServletException;
  
  protected abstract void doPost(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
    throws ServletException;
  
  protected abstract void processRequest()
    throws ServletException;
  
  protected abstract void sendErrorResonse(String paramString)
    throws ServletException, IOException;
  
  protected void setSession(HttpServletRequest request, HttpServletResponse response, int argModo)
  {
    this.requestX = request;
    this.responseX = response;
    this.intModo = argModo;
  }
  
  protected HttpServletRequest getRequest()
  {
    return this.requestX;
  }
  
  protected HttpServletResponse getResponse()
  {
    return this.responseX;
  }
  
  protected HttpSession getSession(String argClave)
  {
    HttpSession sess = null;
    if (this.hstSesiones == null)
    {
      this.hstSesiones = new Hashtable();
      sess = getRequest().getSession(true);
      sess.setAttribute("servlet", getServletName());
      sess.setAttribute("status", "servlet iniciado");
      this.hstSesiones.put(argClave, sess);
    }
    else if (!this.hstSesiones.containsKey(argClave))
    {
      sess = getRequest().getSession(true);
      sess.setAttribute("servlet", getServletName());
      sess.setAttribute("status", "nuevo usuario");
      this.hstSesiones.put(argClave, sess);
    }
    else
    {
      sess = (HttpSession)this.hstSesiones.get(argClave);
      try
      {
        sess.setAttribute("servlet", getServletName());
        sess.setAttribute("status", "usuario conectado");
      }
      catch (Exception eg)
      {
        sess = getRequest().getSession(true);
        this.hstSesiones.put(argClave, sess);
        sess.setAttribute("servlet", getServletName());
        sess.setAttribute("status", "usuario reconectado");
      }
    }
    return sess;
  }
}
