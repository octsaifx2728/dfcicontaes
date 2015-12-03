package mx.com.jammexico.jamservlets;

import JAMMessenger.Servidor.JAMChatServer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAMServeletChat
  extends HttpServlet
{
  public static final int IDX_CONNECTDB = 0;
  public static final int IDX_USER = 1;
  public static final int IDX_PASSWORD = 2;
  public static final int IDX_ACCION = 3;
  public static final int IDX_IPLOCAL = 4;
  private HttpSession sess = null;
  private int contador = 0;
  protected JAMChatServer listenerchat = null;
  
  public void init(ServletConfig config)
    throws ServletException
  {
    super.init(config);
    System.out.println("Iniciando ServletOpinion...");
  }
  
  public void destroy()
  {
    System.out.println("No hay nada que hacer...");
  }
  
  protected synchronized void entrandoEnService()
  {
    this.contador += 1;
  }
  
  protected synchronized void saliendoDeService()
  {
    this.contador -= 1;
  }
  
  protected synchronized int numeroDeServicios()
  {
    return this.contador;
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    entrandoEnService();
    processRequest(request, response);
    saliendoDeService();
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    entrandoEnService();
    processRequest(request, response);
    saliendoDeService();
  }
  
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    try
    {
      if (this.listenerchat == null) {
        this.listenerchat = new JAMChatServer(5000);
      }
      String[] arrParam = (String[])null;
      ObjectInputStream bufferentrada = new ObjectInputStream(request.getInputStream());
      arrParam = (String[])bufferentrada.readObject();
      
      this.sess = request.getSession(true);
      this.sess.setAttribute("connectdb", arrParam[0]);
      this.sess.setAttribute("user", arrParam[1]);
      this.sess.setAttribute("password", arrParam[2]);
      this.sess.setAttribute("accion", arrParam[3]);
      this.sess.setAttribute("iplocal", arrParam[4]);
      
      String[] arrValores = new String[5];
      arrValores[1] = new String(this.sess.getAttribute("user").toString()).trim();
      arrValores[2] = new String(this.sess.getAttribute("password").toString().trim());
      arrValores[0] = new String(this.sess.getAttribute("connectdb").toString().trim());
      arrValores[3] = new String(this.sess.getAttribute("accion").toString().trim());
      arrValores[4] = new String(this.sess.getAttribute("iplocal").toString().trim());
      
      String cLogon = arrValores[3];
      
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      out.println("error@OK");
      out.println("retorno@OK");
      out.println("logon@" + cLogon);
      out.flush();
      out.close();
    }
    catch (Exception e)
    {
      sendErrorResonse(response, "Error antes de disparar el Demonio : " + e.getMessage());
    }
  }
  
  private void sendErrorResonse(HttpServletResponse response, String errorMsg)
    throws IOException
  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("error@" + errorMsg + JAMUtil.getCrlf());
    out.println("retorno@null");
    out.println("logon@null");
    out.flush();
    out.close();
  }
}
