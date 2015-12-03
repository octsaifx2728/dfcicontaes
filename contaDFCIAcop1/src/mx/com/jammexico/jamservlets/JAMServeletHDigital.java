package mx.com.jammexico.jamservlets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAMServeletHDigital
  extends HttpServlet
{
  private static final long serialVersionUID = 1964L;
  private HttpSession sess = null;
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    processRequest(request, response);
  }
  
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    try
    {
      String[] arrParam = (String[])null;
      ObjectInputStream bufferentrada = new ObjectInputStream(request.getInputStream());
      arrParam = (String[])bufferentrada.readObject();
      this.sess = request.getSession(true);
      this.sess.setAttribute("archivo", arrParam[0]);
      this.sess.setAttribute("instancia", arrParam[1]);
      this.sess.setAttribute("path", "C:\\JAMWebService\\Tomcat 5.5\\webapps\\JAMMexico\\tempfiles\\dbfiles\\" + 
        ((String)this.sess.getAttribute("instancia")).toLowerCase() + "\\");
      
      verificaserver obj = new verificaserver((String)this.sess.getAttribute("archivo"), (String)this.sess.getAttribute("path"));
      
      this.sess.setAttribute("empleado", obj.doverifica());
      
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      out.println("error@false");
      out.println("retorno@true");
      out.println("empleado@" + this.sess.getAttribute("empleado"));
      out.flush();
      out.close();
    }
    catch (Exception e)
    {
      sendErrorResonse(response, e.getMessage());
    }
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("retorno@True");
    out.println("error@False");
    out.flush();
    out.close();
  }
  
  private void sendErrorResonse(HttpServletResponse response, String errorMsg)
    throws IOException
  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("retorno@false");
    out.println("error@" + errorMsg + JAMUtil.getCrlf());
    out.flush();
    out.close();
  }
  
  public static void main(String[] s)
  {
    String strPath = "C:\\JAMWebService\\Tomcat 5.5\\webapps\\JAMMexico\\tempfiles\\dbfiles\\jammsg\\";
    String strPath1 = "C:\\JAMWebService\\Tomcat 5.5\\webapps\\JAMMexico\\tempfiles\\dbfiles\\jammsg\\A.fpt";
    verificaserver obj = new verificaserver("SEC0000002192.fpt", strPath);
    try
    {
      reto = obj.doverifica();
    }
    catch (IllegalArgumentException e)
    {
      String reto;
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
