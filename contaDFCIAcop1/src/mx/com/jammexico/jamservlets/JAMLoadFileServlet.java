package mx.com.jammexico.jamservlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mx.com.jammexico.jamsrv.JAMUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class JAMLoadFileServlet
  extends HttpServlet
{
  private static final long serialVersionUID = -1335855995128816859L;
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException
  {
    processRequest(request, response);
  }
  
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException
  {
    try
    {
      procesaFicheros(request);
      
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      out.println("retorno@True");
      out.println("error@False");
      out.flush();
      out.close();
    }
    catch (Exception e)
    {
      sendErrorResonse(response, e.getMessage());
    }
    request.getSession().invalidate();
  }
  
  protected void sendErrorResonse(HttpServletResponse res, Object objParam)
    throws ServletException
  {
    try
    {
      String errorMsg = (String)objParam;
      res.setContentType("text/html");
      PrintWriter out = res.getWriter();
      out.println("retorno@false");
      out.println("error@" + errorMsg + JAMUtil.getCrlf());
      out.flush();
      out.close();
    }
    catch (Exception eg)
    {
      throw new ServletException(eg.getMessage());
    }
  }
  
  public void procesaFicheros(HttpServletRequest req)
    throws IOException, Exception
  {
    try
    {
      DiskFileItemFactory fu = new DiskFileItemFactory();
      
      fu.setSizeThreshold(10240000);
      
      fu.setRepository(new File("/tmp"));
      
      ServletFileUpload upload = new ServletFileUpload(fu);
      
      upload.setSizeMax(-1L);
      
      List fileItems = upload.parseRequest(req);
      if (fileItems == null) {
        throw new Exception("La lista es nula");
      }
      Iterator i = fileItems.iterator();
      FileItem actual = null;
      while (i.hasNext())
      {
        actual = (FileItem)i.next();
        
        String cSubDir = actual.getFieldName().substring(actual.getFieldName().indexOf("-") + 1);
        cSubDir = cSubDir.substring(0, cSubDir.indexOf("-")).toLowerCase();
        
        String fileName = actual.getName();
        File fichero = JAMUtil.getDirectoryTmpFilesForType(fileName, cSubDir);
        actual.write(fichero);
      }
    }
    catch (FileUploadException e)
    {
      throw new Exception("Error de Aplicacion " + e.getMessage());
    }
    catch (IOException e)
    {
      throw new Exception("Error de Aplicacion " + e.getMessage());
    }
    catch (Exception e)
    {
      throw new Exception("Error de Aplicacion " + e.getMessage());
    }
  }
}
