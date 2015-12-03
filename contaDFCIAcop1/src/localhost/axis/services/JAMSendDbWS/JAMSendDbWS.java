package localhost.axis.services.JAMSendDbWS;

import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.activation.DataHandler;

public abstract interface JAMSendDbWS
  extends Remote
{
  public abstract DataHandler[] procesa(DataHandler[] paramArrayOfDataHandler)
    throws RemoteException;
  
  public abstract void main(String[] paramArrayOfString)
    throws RemoteException;
}
