package mx.com.jammexico.jamservlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAMServeletFecha
  extends HttpServlet
{
  private static final long serialVersionUID = 4361765563134606455L;
  
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
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      out.println("error@null");
      out.println("retorno@null");
      out.println("fecha@" + sdf.format(new Date()));
      out.flush();
      out.close();
    }
    catch (Exception e)
    {
      sendErrorResonse(request, response, e.getMessage());
    }
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("retorno@True");
    out.println("error@False");
    out.flush();
    out.close();
    request.getSession().invalidate();
  }
  
  private void sendErrorResonse(HttpServletRequest request, HttpServletResponse response, String errorMsg)
    throws IOException
  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("retorno@false");
    out.println("error@" + errorMsg + JAMUtil.getCrlf());
    out.flush();
    out.close();
  }
}
