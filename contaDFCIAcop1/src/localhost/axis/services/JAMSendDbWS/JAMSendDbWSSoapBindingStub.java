package localhost.axis.services.JAMSendDbWS;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import mx.com.jammexico.jamdb.JAMClienteDB;
import org.apache.axis.AxisFault;
import org.apache.axis.NoEndPointException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Stub;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.encoding.ser.ArrayDeserializerFactory;
import org.apache.axis.encoding.ser.ArraySerializerFactory;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.encoding.ser.EnumDeserializerFactory;
import org.apache.axis.encoding.ser.EnumSerializerFactory;
import org.apache.axis.encoding.ser.SimpleDeserializerFactory;
import org.apache.axis.encoding.ser.SimpleSerializerFactory;
import org.apache.axis.soap.SOAPConstants;
import org.apache.axis.utils.JavaUtils;

public class JAMSendDbWSSoapBindingStub
  extends Stub
  implements JAMSendDbWS
{
  private Vector cachedSerClasses = new Vector();
  private Vector cachedSerQNames = new Vector();
  private Vector cachedSerFactories = new Vector();
  private Vector cachedDeserFactories = new Vector();
  static OperationDesc[] _operations = new OperationDesc[2];
  
  static
  {
    OperationDesc oper = new OperationDesc();
    oper.setName("procesa");
    oper.addParameter(new QName("", "ins"), new QName("www.officenet2.info", "ArrayOf_apachesoap_DataHandler"), DataHandler[].class, (byte)1, false, false);
    oper.setReturnType(new QName("www.officenet2.info", "ArrayOf_apachesoap_DataHandler"));
    oper.setReturnClass(DataHandler[].class);
    oper.setReturnQName(new QName("", "procesaReturn"));
    
    oper.setStyle(Style.RPC);
    oper.setUse(Use.ENCODED);
    _operations[0] = oper;
    
    oper = new OperationDesc();
    oper.setName("main");
    oper.addParameter(new QName("", "args"), new QName("www.officenet2.info", "ArrayOf_xsd_string"), String[].class, (byte)1, false, false);
    oper.setReturnType(XMLType.AXIS_VOID);
    oper.setStyle(Style.RPC);
    oper.setUse(Use.ENCODED);
    _operations[1] = oper;
  }
  
  public JAMSendDbWSSoapBindingStub()
    throws AxisFault
  {
    this(null);
  }
  
  public JAMSendDbWSSoapBindingStub(URL endpointURL, javax.xml.rpc.Service service)
    throws AxisFault
  {
    this(service);
    this.cachedEndpoint = endpointURL;
  }
  
  public JAMSendDbWSSoapBindingStub(javax.xml.rpc.Service service)
    throws AxisFault
  {
    if (service == null) {
      this.service = new org.apache.axis.client.Service();
    } else {
      this.service = service;
    }
    Class beansf = BeanSerializerFactory.class;
    Class beandf = BeanDeserializerFactory.class;
    Class enumsf = EnumSerializerFactory.class;
    Class enumdf = EnumDeserializerFactory.class;
    Class arraysf = ArraySerializerFactory.class;
    Class arraydf = ArrayDeserializerFactory.class;
    Class simplesf = SimpleSerializerFactory.class;
    Class simpledf = SimpleDeserializerFactory.class;
    QName qName = new QName("www.officenet2.info", "ArrayOf_xsd_string");
    this.cachedSerQNames.add(qName);
    Class cls = String[].class;
    this.cachedSerClasses.add(cls);
    this.cachedSerFactories.add(arraysf);
    this.cachedDeserFactories.add(arraydf);
    
    qName = new QName("www.officenet2.info", "ArrayOf_apachesoap_DataHandler");
    this.cachedSerQNames.add(qName);
    cls = DataHandler[].class;
    this.cachedSerClasses.add(cls);
    this.cachedSerFactories.add(arraysf);
    this.cachedDeserFactories.add(arraydf);
  }
  
  private Call createCall()
    throws RemoteException
  {
    try
    {
      Call _call = 
        (Call)this.service.createCall();
      if (this.maintainSessionSet) {
        _call.setMaintainSession(this.maintainSession);
      }
      if (this.cachedUsername != null) {
        _call.setUsername(this.cachedUsername);
      }
      if (this.cachedPassword != null) {
        _call.setPassword(this.cachedPassword);
      }
      if (this.cachedEndpoint != null) {
        _call.setTargetEndpointAddress(this.cachedEndpoint);
      }
      if (this.cachedTimeout != null) {
        _call.setTimeout(this.cachedTimeout);
      }
      if (this.cachedPortName != null) {
        _call.setPortName(this.cachedPortName);
      }
      Enumeration keys = this.cachedProperties.keys();
      while (keys.hasMoreElements())
      {
        String key = (String)keys.nextElement();
        _call.setProperty(key, this.cachedProperties.get(key));
      }
      synchronized (this)
      {
        if (firstCall())
        {
          _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
          _call.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");
          for (int i = 0; i < this.cachedSerFactories.size(); i++)
          {
            Class cls = (Class)this.cachedSerClasses.get(i);
            QName qName = 
              (QName)this.cachedSerQNames.get(i);
            Class sf = 
              (Class)this.cachedSerFactories.get(i);
            Class df = 
              (Class)this.cachedDeserFactories.get(i);
            _call.registerTypeMapping(cls, qName, sf, df, false);
          }
        }
      }
      return _call;
    }
    catch (Throwable t)
    {
      throw new AxisFault("Failure trying to get the Call object", t);
    }
  }
  
  public synchronized DataHandler[] procesa(DataHandler[] ins)
    throws RemoteException
  {
    if (this.cachedEndpoint == null) {
      throw new NoEndPointException();
    }
    Call _call = createCall();
    _call.setOperation(_operations[0]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setTimeout(Integer.valueOf(JAMClienteDB.getTimeOut()));
    _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new QName("http://jamdb.jammexico.com.mx", "procesa"));
    
    setRequestHeaders(_call);
    setAttachments(_call);
    Object _resp = _call.invoke(new Object[] { ins });
    if ((_resp instanceof RemoteException)) {
      throw ((RemoteException)_resp);
    }
    extractAttachments(_call);
    try
    {
      return (DataHandler[])_resp;
    }
    catch (Exception _exception) {}
    return (DataHandler[])JavaUtils.convert(_resp, DataHandler[].class);
  }
  
  public synchronized void main(String[] args)
    throws RemoteException
  {
    if (this.cachedEndpoint == null) {
      throw new NoEndPointException();
    }
    Call _call = createCall();
    _call.setOperation(_operations[1]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new QName("http://jamdb.jammexico.com.mx", "main"));
    
    setRequestHeaders(_call);
    setAttachments(_call);
    Object _resp = _call.invoke(new Object[] { args });
    if ((_resp instanceof RemoteException)) {
      throw ((RemoteException)_resp);
    }
    extractAttachments(_call);
  }
}
