package mx.com.jammexico.jamdrivers;

import java.io.Serializable;

public class JAMSendLogon
  implements Serializable, Cloneable
{
  private String strConnectionDB = null;
  private String strUser = null;
  private String strPass = null;
  private String strChat = "0";
  private int intAcceso = 0;
  private int intTimeout = 0;
  private String strNewPass = null;
  private String strError = null;
  private String strServidorname = null;
  private int intHuelladigital = 0;
  
  public String getConnectionDB()
  {
    return this.strConnectionDB;
  }
  
  public String getUser()
  {
    return this.strUser;
  }
  
  public String getPass()
  {
    return this.strPass;
  }
  
  public int getAcceso()
  {
    return this.intAcceso;
  }
  
  public String getNewPass()
  {
    return this.strNewPass;
  }
  
  public void setConnectionDB(String argConnectionDB)
  {
    this.strConnectionDB = argConnectionDB;
  }
  
  public void setUser(String argUser)
  {
    this.strUser = argUser;
  }
  
  public void setPass(String argPass)
  {
    this.strPass = argPass;
  }
  
  public void setAcceso(int argAcceso)
  {
    this.intAcceso = argAcceso;
  }
  
  public void setNewPass(String argNewPass)
  {
    this.strNewPass = argNewPass;
  }
  
  public void setError(String argError)
  {
    this.strError = argError;
  }
  
  public String getError()
  {
    return this.strError;
  }
  
  public int getTimeout()
  {
    return this.intTimeout;
  }
  
  public void setTimeout(int argTimeout)
  {
    this.intTimeout = argTimeout;
  }
  
  public int getHuelladigital()
  {
    return this.intHuelladigital;
  }
  
  public void setHuelladigital(int argHuelladigital)
  {
    this.intHuelladigital = argHuelladigital;
  }
  
  public String getServidorname()
  {
    return this.strServidorname;
  }
  
  public void setServidorname(String argServidorname)
  {
    this.strServidorname = argServidorname;
  }
  
  public String getChat()
  {
    return this.strChat;
  }
  
  public void setChat(String argChat)
  {
    this.strChat = argChat;
  }
}
