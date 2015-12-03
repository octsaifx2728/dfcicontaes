package mx.com.jammexico.jamsrv;

import java.io.Serializable;

public class JAMNewId
  implements Serializable
{
  private String name;
  private int value;
  
  public JAMNewId() {}
  
  public JAMNewId(String pname, int pvalue)
  {
    this.name = pname;
    this.value = pvalue;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public int getValue()
  {
    return this.value;
  }
  
  public String getTest()
  {
    return "Test";
  }
}
