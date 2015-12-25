package mx.com.jammexico.jamservlets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mx.com.jammexico.jamdb.JAMDataBase;
import mx.com.jammexico.jamdrivers.JAMSendLogon;
import mx.com.jammexico.jamsrv.JAMRowSet;

public class JAMServeletLogon
  extends JAMServLet
{
  private static final long serialVersionUID = 1645009155735301L;
  public static final int IDX_CONNECTDB = 0;
  public static final int IDX_USER = 1;
  public static final int IDX_PASSWORD = 2;
  public static final int IDX_NEWPASS = 3;
  public static final int IDX_ACCESO = 4;
  public static final int IDX_USER_WEB = 0;
  public static final int IDX_USER_SYS = 1;
  public static final int IDX_USER_REG = 2;
  private HttpSession sess = null;
  private JAMDataBase objReadDB = null;
  private static final String JAM_PATH_INI_SEGURIDAD = "C:\\JAMWebService\\JAMDBConfig\\seguridad.ini";
  
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
    JAMSendLogon objSendLogon = null;
    try
    {
      ObjectInputStream input = new ObjectInputStream(getRequest().getInputStream());
      Object dataInput = input.readObject();
      objSendLogon = (JAMSendLogon)dataInput;
      
      this.sess = getSession(objSendLogon.getConnectionDB() + "JAM$_" + objSendLogon.getUser());
      
      this.sess.setAttribute("connectdb", objSendLogon.getConnectionDB());
      this.sess.setAttribute("user", objSendLogon.getUser());
      this.sess.setAttribute("password", objSendLogon.getPass());
      this.sess.setAttribute("newpass", objSendLogon.getNewPass());
      this.sess.setAttribute("acceso", Integer.valueOf(objSendLogon.getAcceso()));
      this.sess.setAttribute("chatmodo", objSendLogon.getChat());
      this.sess.setAttribute("huelladigital", Integer.valueOf(objSendLogon.getHuelladigital()));
      this.sess.setAttribute("servidorname", objSendLogon.getServidorname());
      
      connection(objSendLogon.getUser(), 
        objSendLogon.getPass(), 
        objSendLogon.getConnectionDB());
      if (!objSendLogon.getNewPass().equalsIgnoreCase("")) {
        modificapassword(objSendLogon.getUser(), 
          objSendLogon.getPass(), 
          objSendLogon.getConnectionDB(), 
          objSendLogon.getNewPass());
      }
      objSendLogon.setChat((String)this.sess.getAttribute("chatmodo"));
      objSendLogon.setServidorname((String)this.sess.getAttribute("servidorname"));
      Integer intHD = (Integer)this.sess.getAttribute("huelladigital");
      objSendLogon.setHuelladigital(intHD.intValue());
      objSendLogon.setTimeout(this.objReadDB.getTimeOut());
      
      getResponse().setContentType("java-internal/" + objSendLogon.getClass().getName());
      ObjectOutputStream output = new ObjectOutputStream(getResponse().getOutputStream());
      output.writeObject(objSendLogon);
      
      output.flush();
      output.close();
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
  
  private void connection(String argUsuario, String argPassword, String argConnection)
    throws Exception
  {
    try
    {
      this.objReadDB = new JAMDataBase(argConnection, "JAM$_" + argUsuario, argPassword);
      String strSql = "SELECT * FROM SOCUSUA02_MAE_USUARIOS where SOCUSUA02_USUARIO = '" + argUsuario + "' and SOCUSUA02_PASSWORD = '" + argPassword + "'";
      
      JAMRowSet rstLogon = this.objReadDB.getRowSet(new String[] { strSql })[0];
      if (rstLogon.getRowcount() == 0L) {
        throw new Exception("Usuario No Reconocido para el Sistema");
      }
      rstLogon.first();
      try
      {
        if (rstLogon.getInt("SOCUSUA02_ACCESAHD") != 0) {
          this.sess.setAttribute("huelladigital", new Integer(rstLogon.getInt("ID_SOCUSUA02")));
        }
      }
      catch (Exception localException1) {}
      if (rstLogon.getInt("RELA_SOCSYST10_CHAT") != 0)
      {
        strSql = "SELECT SOCSYST10_CODIGO FROM CBO_CHATACCESOS where ID_SOCSYST10 = " + rstLogon.getInt("RELA_SOCSYST10_CHAT");
        JAMRowSet rstChat = this.objReadDB.getRowSet(new String[] { strSql })[0];
        rstChat.first();
        this.sess.setAttribute("chatmodo", rstChat.getString("SOCSYST10_CODIGO"));
      }
    }
    catch (Exception eg)
    {
      if (this.objReadDB != null) {
        this.objReadDB.inactiveDB();
      }
      throw new Exception("Error : Al validad Usuario. Sin Autorizaci{on" + eg.getMessage());
    }
  }
  
  private void modificapassword(String argUsuario, String argPassword, String argConnection, String argNewPass)
    throws Exception
  {
    String[] arrSqls = new String[2];
    arrSqls[0] = ("select *  from JAM$_USER_MOD('" + argUsuario + "','" + argNewPass + "','I\tLOGON='" + ",'modifica pass','modifica pass')");
    arrSqls[1] = ("UPDATE socusua02_mae_usuarios SET socusua02_password='" + argNewPass + "' WHERE socusua02_usuario = '" + argUsuario + "'");
    try
    {
      JAMRowSet rstTempProcedure = this.objReadDB.getRowSet(arrSqls)[0];
      try
      {
        rstTempProcedure.first();
        if (rstTempProcedure.getInt("OK") != 0) {
          throw new Exception("No se pudo actualizar la clave correctamente");
        }
      }
      catch (Exception localException) {}
      return;
    }
    catch (SQLException e)
    {
      if (this.objReadDB != null) {
        this.objReadDB.inactiveDB();
      }
      throw new Exception("Error al actualizar la clave " + e.getMessage());
    }
  }
  
  protected void sendErrorResonse(String argError)
    throws ServletException, IOException
  {
    try
    {
      JAMSendLogon objSendLogon = new JAMSendLogon();
      objSendLogon.setError(argError);
      getResponse().setContentType("java-internal/" + objSendLogon.getClass().getName());
      ObjectOutputStream output = new ObjectOutputStream(getResponse().getOutputStream());
      output.writeObject(objSendLogon);
      output.flush();
      output.close();
    }
    catch (IOException e)
    {
      throw new ServletException(e.getMessage());
    }
  }
}
