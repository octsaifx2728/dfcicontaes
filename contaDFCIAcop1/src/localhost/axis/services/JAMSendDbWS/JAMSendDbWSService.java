package localhost.axis.services.JAMSendDbWS;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public abstract interface JAMSendDbWSService
  extends Service
{
  public abstract String getJAMSendDbWSAddress();
  
  public abstract JAMSendDbWS getJAMSendDbWS()
    throws ServiceException;
  
  public abstract JAMSendDbWS getJAMSendDbWS(URL paramURL)
    throws ServiceException;
}
