package localhost.axis.services.JAMSendDbWS;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.HashSet;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import org.apache.axis.AxisFault;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

public class JAMSendDbWSServiceLocator
  extends Service
  implements JAMSendDbWSService
{
  private static final String JAMSendDbWS_address = "http://localhost:8024/axis/services/JAMSendDbWS";
  
  public String getJAMSendDbWSAddress()
  {
    return "http://localhost:8024/axis/services/JAMSendDbWS";
  }
  
  private String JAMSendDbWSWSDDServiceName = "JAMSendDbWS";
  
  public String getJAMSendDbWSWSDDServiceName()
  {
    return this.JAMSendDbWSWSDDServiceName;
  }
  
  public void setJAMSendDbWSWSDDServiceName(String name)
  {
    this.JAMSendDbWSWSDDServiceName = name;
  }
  
  public JAMSendDbWS getJAMSendDbWS()
    throws ServiceException
  {
    try
    {
      endpoint = new URL(getJAMSendDbWSAddress());
    }
    catch (MalformedURLException e)
    {
      URL endpoint;
      throw new ServiceException(e);
    }
    URL endpoint;
    return getJAMSendDbWS(endpoint);
  }
  
  public JAMSendDbWS getJAMSendDbWS(URL portAddress)
    throws ServiceException
  {
    try
    {
      JAMSendDbWSSoapBindingStub _stub = new JAMSendDbWSSoapBindingStub(portAddress, this);
      _stub.setPortName(getJAMSendDbWSWSDDServiceName());
      return _stub;
    }
    catch (AxisFault e) {}
    return null;
  }
  
  public Remote getPort(Class serviceEndpointInterface)
    throws ServiceException
  {
    try
    {
      if (JAMSendDbWS.class.isAssignableFrom(serviceEndpointInterface))
      {
        JAMSendDbWSSoapBindingStub _stub = 
          new JAMSendDbWSSoapBindingStub(new URL(getJAMSendDbWSAddress()), this);
        _stub.setPortName(getJAMSendDbWSWSDDServiceName());
        return _stub;
      }
    }
    catch (Throwable t)
    {
      throw new ServiceException(t);
    }
    throw new ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
  }
  
  public Remote getPort(QName portName, Class serviceEndpointInterface)
    throws ServiceException
  {
    if (portName == null) {
      return getPort(serviceEndpointInterface);
    }
    String inputPortName = portName.getLocalPart();
    if ("JAMSendDbWS".equals(inputPortName)) {
      return getJAMSendDbWS();
    }
    Remote _stub = getPort(serviceEndpointInterface);
    ((Stub)_stub).setPortName(portName);
    return _stub;
  }
  
  public QName getServiceName()
  {
    return new QName(getJAMSendDbWSAddress(), "JAMSendDbWSService");
  }
  
  private HashSet ports = null;
  
  public Iterator getPorts()
  {
    if (this.ports == null)
    {
      this.ports = new HashSet();
      this.ports.add(new QName("JAMSendDbWS"));
    }
    return this.ports.iterator();
  }
}
