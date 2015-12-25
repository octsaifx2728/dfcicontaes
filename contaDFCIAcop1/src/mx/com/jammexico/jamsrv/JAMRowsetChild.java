package mx.com.jammexico.jamsrv;

import java.io.Serializable;

public class JAMRowsetChild
  implements Serializable
{
  private String RowsetName;
  private String RelatedField;
  
  public JAMRowsetChild(String pRowsetName, String fld)
  {
    this.RowsetName = pRowsetName;
    this.RelatedField = fld;
  }
  
  public String getRowsetName()
  {
    return this.RowsetName;
  }
  
  public String getRelatedFiled()
  {
    return this.RelatedField;
  }
}
