package mx.com.jammexico.jamdb;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mx.com.jammexico.jamservlets.JAMServLet;
import mx.com.jammexico.jamsrv.JAMRowSet;

public class JAMServeletDB
  extends JAMServLet
{
  private static final long serialVersionUID = 8314547585838083240L;
  private JAMSendSql objSendSqls = null;
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException
  {
    setSession(request, response, 0);
    processRequest();
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException
  {
    setSession(request, response, 0);
    processRequest();
  }
  
  protected void processRequest()
    throws ServletException
  {
    try
    {
      ObjectInputStream input = new ObjectInputStream(getRequest().getInputStream());
      Object dataInput = input.readObject();
      this.objSendSqls = ((JAMSendSql)dataInput);
      
      JAMDataBase objReadDB = new JAMDataBase(this.objSendSqls.getInstancia(), 
        this.objSendSqls.getUsuario(), 
        this.objSendSqls.getClave(), 
        this.objSendSqls.getInstanciaAlternativa());
      if (this.objSendSqls.getCommands() != null)
      {
        JAMRowSet[] rstSend = objReadDB.getRowSet(this.objSendSqls.getCommands());
        this.objSendSqls = new JAMSendSql("OK", "OK", "OK");
        this.objSendSqls.setRowSets(rstSend);
      }
      else if (this.objSendSqls.getTransactions() != null)
      {
        objReadDB.setTransactions(this.objSendSqls.getTransactions());
        this.objSendSqls = new JAMSendSql("OK", "OK", "OK");
      }
      else if (this.objSendSqls.getConexiones() != null)
      {
        boolean logHistorico = this.objSendSqls.isHistorico();
        this.objSendSqls = new JAMSendSql("OK", "OK", "OK");
        if (logHistorico) {
          this.objSendSqls.setConexiones(objReadDB.getConexionesHistoricas(), logHistorico);
        } else {
          this.objSendSqls.setConexiones(objReadDB.getConexionesActivas(), logHistorico);
        }
      }
      else if (this.objSendSqls.getConexionCerrar() != null)
      {
        boolean logBloquea = this.objSendSqls.isBloquea();
        String strStatus = objReadDB.setCerrarConexion(this.objSendSqls.getConexionCerrar(), logBloquea);
        this.objSendSqls = new JAMSendSql("OK", "OK", "OK");
        this.objSendSqls.setConexionCerrar(strStatus, logBloquea);
      }
      else if (this.objSendSqls.getMensaje() != null)
      {
        JAMMensajes objMensaje = objReadDB.setMensajes(this.objSendSqls.getMensaje());
        this.objSendSqls = new JAMSendSql("OK", "OK", "OK");
        this.objSendSqls.setMensaje(objMensaje);
      }
      else
      {
        objReadDB.inactiveDB();
        throw new Exception("No se definieron Comandos");
      }
      getResponse().setContentType("java-internal/" + this.objSendSqls.getClass().getName());
      ObjectOutputStream output = new ObjectOutputStream(getResponse().getOutputStream());
      output.writeObject(this.objSendSqls);
      
      output.flush();
      output.close();
    }
    catch (ClassNotFoundException e1)
    {
      sendErrorResonse(e1.getMessage());
    }
    catch (IOException e1)
    {
      sendErrorResonse(e1.getMessage());
    }
    catch (SQLException e1)
    {
      sendErrorResonse(e1.getMessage());
    }
    catch (Exception e1)
    {
      sendErrorResonse(e1.getMessage());
    }
    getRequest().getSession().invalidate();
  }
  
  protected void sendErrorResonse(String argError)
    throws ServletException
  {
    try
    {
      this.objSendSqls = new JAMSendSql("ERROR", "ERROR", "ERROR");
      this.objSendSqls.setError(argError);
      getResponse().setContentType("java-internal/" + this.objSendSqls.getClass().getName());
      ObjectOutputStream output = new ObjectOutputStream(getResponse().getOutputStream());
      output.writeObject(this.objSendSqls);
      output.flush();
      output.close();
    }
    catch (IOException e)
    {
      throw new ServletException(e.getMessage());
    }
  }
}
