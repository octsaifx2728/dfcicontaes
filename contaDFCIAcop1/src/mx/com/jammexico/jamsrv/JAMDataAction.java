package mx.com.jammexico.jamsrv;

import java.io.Serializable;
import java.util.Vector;

public class JAMDataAction
  implements Serializable
{
  public static final int ACTION_DDLSTATEMENT = 1;
  public static final int ACTION_VIEWSTATEMENT = 2;
  public static final int ACTION_SQLSTATEMENT = 3;
  public static final int ACTION_EXECUTESTATEMENT = 4;
  public static final int ACTION_ROWSET = 5;
  public static final int ACTION_ERRORRESPONSE = 6;
  private int type;
  private String name = null;
  private JAMRowSet rowset = null;
  private String sqlcmd = null;
  private Vector params;
  private String executestatement;
  private String[] cTransactionDirectives;
  
  public JAMDataAction(JAMRowSet p)
  {
    this.rowset = p;
    this.type = 5;
  }
  
  public JAMDataAction(JAMRowSet p, String[] argcTransactionDirectives)
  {
    this.rowset = p;
    this.type = 5;
    this.cTransactionDirectives = argcTransactionDirectives;
  }
  
  private void setProcedureName(String proc)
  {
    this.executestatement = ("{" + proc + "(");
  }
  
  public String[] getTransactionDirectives()
  {
    return this.cTransactionDirectives;
  }
  
  public String getname()
  {
    return this.name;
  }
  
  public int getParamCount()
  {
    return this.params.size();
  }
  
  public void setName(String pName)
  {
    this.name = pName;
  }
  
  public String getExecuteStatement()
  {
    return this.executestatement;
  }
  
  public JAMDataAction(String p)
  {
    this.sqlcmd = p;
    if ((p.toUpperCase().startsWith("EXECUTE ")) || 
      (p.toUpperCase().startsWith("CALL ")))
    {
      setProcedureName(p);
      this.type = 4;
    }
    else if ((p.toUpperCase().startsWith("UPDATE ")) || 
      (p.toUpperCase().startsWith("INSERT ")) || 
      (p.toUpperCase().startsWith("DELETE ")) || 
      (p.toUpperCase().startsWith("CREATE ")))
    {
      this.type = 3;
    }
    else
    {
      this.type = 2;
    }
  }
  
  public void dispose()
  {
    this.sqlcmd = null;
    this.rowset = null;
    this.params = null;
  }
  
  public int getType()
  {
    return this.type;
  }
  
  public JAMRowSet getRowSet()
  {
    return this.rowset;
  }
  
  public String getSqlCommand()
  {
    return this.sqlcmd;
  }
}
