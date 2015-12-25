package mx.com.jammexico.jamservlets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mx.com.jammexico.jamdrivers.JAMCopiaInstancias;

public class JAMServeletCreaInstancias
  extends JAMServLet
{
  private HttpSession sess = null;
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException
  {
    setSession(request, response, 0);
    processRequest();
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException
  {
    setSession(request, response, 1);
    processRequest();
  }
  
  protected void processRequest()
    throws ServletException
  {
    try
    {
      String[] arrParam = (String[])null;
      ObjectInputStream bufferentrada = new ObjectInputStream(getRequest().getInputStream());
      arrParam = (String[])bufferentrada.readObject();
      
      this.sess = getSession(arrParam[0] + arrParam[1]);
      this.sess.setAttribute("instancia", arrParam[0]);
      this.sess.setAttribute("usuario", arrParam[1]);
      this.sess.setAttribute("clave", arrParam[2]);
      this.sess.setAttribute("cliente", arrParam[3]);
      this.sess.setAttribute("regfiscal", arrParam[4]);
      this.sess.setAttribute("sucursal", arrParam[5]);
      try
      {
        JAMCopiaInstancias objCreaInstancia = null;
        if (this.sess.getAttribute("sucursal").toString().trim().equalsIgnoreCase("")) {
          objCreaInstancia = new JAMCopiaInstancias();
        } else {
          objCreaInstancia = new JAMCopiaInstancias(this.sess.getAttribute("sucursal").toString().trim());
        }
        String[] arrParameters = new String[6];
        
        arrParameters[0] = new String(this.sess.getAttribute("instancia").toString().trim());
        arrParameters[1] = new String(this.sess.getAttribute("usuario").toString().trim());
        arrParameters[2] = new String(this.sess.getAttribute("clave").toString().trim());
        arrParameters[3] = new String(this.sess.getAttribute("cliente").toString().trim());
        arrParameters[4] = new String(this.sess.getAttribute("regfiscal").toString().trim());
        arrParameters[5] = new String(this.sess.getAttribute("sucursal").toString().trim());
        objCreaInstancia.JAMStartUp(arrParameters);
      }
      catch (Exception e)
      {
        System.out.println(e.getMessage());
        try
        {
          sendErrorResonse(e.getMessage());
        }
        catch (IOException e1)
        {
          throw new ServletException(e1.getMessage());
        }
      }
      getResponse().setContentType("text/html");
      PrintWriter out = getResponse().getWriter();
      out.println("retorno@OK");
      out.flush();
      out.close();
    }
    catch (Exception e)
    {
      System.out.println("Error al Crear cargar parametros" + e.getMessage());
      try
      {
        sendErrorResonse(e.getMessage());
      }
      catch (IOException e1)
      {
        throw new ServletException(e1.getMessage());
      }
    }
  }
  
  protected void sendErrorResonse(String errorMsg)
    throws ServletException, IOException
  {
    getResponse().setContentType("text/html");
    PrintWriter out = getResponse().getWriter();
    out.println("error@" + errorMsg);
    out.flush();
    out.close();
  }
}
