package mx.com.jammexico.jamdb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.activation.DataHandler;
import mx.com.jammexico.jamsrv.JAMRowSet;
import org.apache.soap.util.mime.ByteArrayDataSource;

public class JAMSendDbWS
{
  public synchronized DataHandler[] procesa(DataHandler[] objSendSql)
  {
    try
    {
      GZIPInputStream zis = new GZIPInputStream(objSendSql[0].getInputStream());
      ObjectInputStream ois = new ObjectInputStream(zis);
      JAMSendSql sendSql = (JAMSendSql)ois.readObject();
      try
      {
        JAMDataBase objReadDB = new JAMDataBase(sendSql.getInstancia(), 
          sendSql.getUsuario(), 
          sendSql.getClave(), 
          sendSql.getInstanciaAlternativa());
        if (sendSql.getCommands() != null)
        {
          JAMRowSet[] rstSend = objReadDB.getRowSet(sendSql.getCommands());
          sendSql = new JAMSendSql("OK", "OK", "OK");
          sendSql.setRowSets(rstSend);
        }
        else if (sendSql.getTransactions() != null)
        {
          objReadDB.setTransactions(sendSql.getTransactions());
          sendSql = new JAMSendSql("OK", "OK", "OK");
        }
        else if (sendSql.getConexiones() != null)
        {
          boolean logHistorico = sendSql.isHistorico();
          sendSql = new JAMSendSql("OK", "OK", "OK");
          if (logHistorico) {
            sendSql.setConexiones(objReadDB.getConexionesHistoricas(), logHistorico);
          } else {
            sendSql.setConexiones(objReadDB.getConexionesActivas(), logHistorico);
          }
        }
        else if (sendSql.getConexionCerrar() != null)
        {
          boolean logBloquea = sendSql.isBloquea();
          String strStatus = objReadDB.setCerrarConexion(sendSql.getConexionCerrar(), logBloquea);
          sendSql = new JAMSendSql("OK", "OK", "OK");
          sendSql.setConexionCerrar(strStatus, logBloquea);
        }
        else if (sendSql.getMensaje() != null)
        {
          JAMMensajes objMensaje = objReadDB.setMensajes(sendSql.getMensaje());
          sendSql = new JAMSendSql("OK", "OK", "OK");
          sendSql.setMensaje(objMensaje);
        }
        else
        {
          objReadDB.inactiveDB();
          throw new Exception("Exception : No se definieron Sentencias ni Transacciones");
        }
      }
      catch (SQLException e)
      {
        sendSql = new JAMSendSql("ERROR", "ERROR", "ERROR");
        sendSql.setError(e.getMessage());
      }
      catch (Exception e)
      {
        sendSql = new JAMSendSql("ERROR", "ERROR", "ERROR");
        sendSql.setError(e.getMessage());
      }
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      GZIPOutputStream zos = new GZIPOutputStream(bos);
      ObjectOutputStream os = new ObjectOutputStream(zos);
      os.writeObject(sendSql);
      zos.finish();
      
      DataHandler[] objReto = new DataHandler[1];
      objReto[0] = new DataHandler(new ByteArrayDataSource(bos.toByteArray(), "JAMMexico/dataAction"));
      
      zis.close();
      ois.close();
      
      os.close();
      bos.close();
      zos.close();
      
      Runtime.getRuntime().gc();
      return objReto;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    Runtime.getRuntime().gc();
    return null;
  }
}
