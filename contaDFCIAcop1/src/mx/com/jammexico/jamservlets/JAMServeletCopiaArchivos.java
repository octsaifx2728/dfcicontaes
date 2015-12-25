package mx.com.jammexico.jamservlets;

import ch.ubique.inieditor.IniEditor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAMServeletCopiaArchivos
  extends HttpServlet
{
  public static final int IDX_FILE_SOURCE = 0;
  public static final int IDX_FILE_TARGET = 1;
  public static final int IDX_INSTANCIA = 2;
  public static final int IDX_MODALIDAD = 3;
  private String PATH_PICTURE = "C:\\JAMWebService\\Tomcat 5.5\\webapps\\JAMMexico\\Images\\dbpictures\\";
  private String PATH_FILES = "C:\\JAMWebService\\Tomcat 5.5\\webapps\\JAMMexico\\tempfiles\\dbfiles\\";
  private HttpSession sess = null;
  private String cFileSource = null;
  private String cFileTarget = null;
  private String cInstancia = null;
  private int intModalidad;
  
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
    String[] arrParam = (String[])null;
    try
    {
      ObjectInputStream bufferentrada = new ObjectInputStream(request.getInputStream());
      arrParam = (String[])bufferentrada.readObject();
      
      this.sess = request.getSession(true);
      this.sess.setAttribute("filesource", arrParam[0]);
      this.sess.setAttribute("filetarget", arrParam[1]);
      this.sess.setAttribute("instancia", arrParam[2]);
      this.sess.setAttribute("modalidad", arrParam[3]);
    }
    catch (Exception e)
    {
      System.out.println("Error al Crear cargar parametros" + e.getMessage());
      sendErrorResonse(response, e.getMessage());
    }
    try
    {
      this.cFileSource = new String(this.sess.getAttribute("filesource").toString()).trim();
      this.cFileTarget = new String(this.sess.getAttribute("filetarget").toString()).trim();
      this.cInstancia = new String(this.sess.getAttribute("instancia").toString()).trim();
      this.intModalidad = new Integer(this.sess.getAttribute("modalidad").toString()).intValue();
      
      FileInputStream file = new FileInputStream("C:\\JAMWebService\\JAMDBConfig\\conections.ini");
      IniEditor e = new IniEditor();
      e.load(file);
      this.PATH_PICTURE = e.get(this.cInstancia, "RootPicture");
      this.PATH_FILES = e.get(this.cInstancia, "RootFiles");
      file.close();
      if (this.intModalidad == 63572)
      {
        this.cFileTarget = (this.PATH_FILES + this.cInstancia + "\\" + this.cFileTarget);
        copia(this.cFileSource, this.cFileTarget, 0);
      }
      else
      {
        goCopiaDentroDeLaInstancia(response);
      }
    }
    catch (IOException e)
    {
      System.out.println(e.getMessage());
      sendErrorResonse(response, e.getMessage());
    }
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("retorno@OK");
    out.println("error@null");
    out.flush();
    out.close();
  }
  
  private void goCopiaDentroDeLaInstancia(HttpServletResponse response)
    throws IOException
  {
    switch (JAMUtil.validaTipoArchivo(this.cFileSource))
    {
    case 0: 
      this.cFileSource = (this.PATH_PICTURE + this.cInstancia + "\\" + this.cFileSource);
      break;
    case 1: 
      this.cFileSource = (this.PATH_FILES + this.cInstancia + "\\" + this.cFileSource);
      break;
    case -1: 
      System.out.println("No se Reconoce Tipo de Archivo Origen");
      sendErrorResonse(response, "No se Reconoce Tipo de Archivo Origen");
    }
    switch (JAMUtil.validaTipoArchivo(this.cFileTarget))
    {
    case 0: 
      this.cFileTarget = (this.PATH_PICTURE + this.cInstancia + "\\" + this.cFileTarget);
      break;
    case 1: 
      this.cFileTarget = (this.PATH_FILES + this.cInstancia + "\\" + this.cFileTarget);
      break;
    case -1: 
      System.out.println("No se Reconoce Tipo de Archivo Destino");
      sendErrorResonse(response, "No se Reconoce Tipo de Archivo Destino");
    }
    copia(this.cFileSource, this.cFileTarget, this.intModalidad);
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
  
  private void copia(String nombreFuente, String nombreDestino, int intModalidad)
    throws IOException
  {
    FileInputStream fis = new FileInputStream(nombreFuente);
    FileOutputStream fos = new FileOutputStream(nombreDestino);
    FileChannel canalFuente = fis.getChannel();
    FileChannel canalDestino = fos.getChannel();
    canalFuente.transferTo(0L, canalFuente.size(), canalDestino);
    fis.close();
    fos.close();
    if (intModalidad == 1) {
      try
      {
        File target = new File(nombreFuente);
        target.delete();
      }
      catch (Exception localException) {}
    }
  }
}
