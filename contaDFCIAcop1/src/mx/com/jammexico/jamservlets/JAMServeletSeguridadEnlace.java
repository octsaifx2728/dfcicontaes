package mx.com.jammexico.jamservlets;

import ch.ubique.inieditor.IniEditor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAMServeletSeguridadEnlace
  extends HttpServlet
{
  private static final String JAM_WEB_CONFIG = "C:\\JAMWebService\\JAMDBConfig\\";
  private static final String JAM_PATH_INI_SEGURIDAD = "C:\\JAMWebService\\JAMDBConfig\\seguridad.ini";
  private String strIpLocalServer = null;
  private HttpSession sess = null;
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    processRequest(request, response);
  }
  
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
      this.sess.setAttribute("iplocalserver", arrParam[0]);
      this.strIpLocalServer = new String(this.sess.getAttribute("iplocalserver").toString()).trim();
      
      FileInputStream file = new FileInputStream("C:\\JAMWebService\\JAMDBConfig\\seguridad.ini");
      IniEditor e = new IniEditor();
      e.load(file);
      
      String strIpAutorizadaSector = e.get(this.strIpLocalServer, "sector");
      
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      if ((strIpAutorizadaSector == null) || (strIpAutorizadaSector.trim().equalsIgnoreCase("")))
      {
        out.println("retorno@false,none");
        out.println("error@null");
      }
      else
      {
        out.println("retorno@true," + strIpAutorizadaSector.trim());
        out.println("error@null");
      }
      out.flush();
      out.close();
    }
    catch (Exception e)
    {
      System.out.println("Error al Verificar la Seguridad de Enlace : " + e.getMessage());
      sendErrorResonse(response, e.getMessage());
    }
  }
  
  private void sendErrorResonse(HttpServletResponse response, String errorMsg)
    throws IOException
  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("retorno@null");
    out.println("error@" + errorMsg + JAMUtil.getCrlf());
    out.flush();
    out.close();
  }
}
