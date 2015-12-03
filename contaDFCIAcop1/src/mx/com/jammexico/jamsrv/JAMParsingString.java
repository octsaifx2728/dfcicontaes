package mx.com.jammexico.jamsrv;

public class JAMParsingString
{
  private String internalStr;
  
  public JAMParsingString(String p)
  {
    this.internalStr = p;
  }
  
  public String Extract(String separ)
  {
    String retval = null;
    if (this.internalStr.indexOf(separ) == -1)
    {
      retval = this.internalStr;
      this.internalStr = "";
    }
    else
    {
      retval = this.internalStr.substring(0, this.internalStr.indexOf(separ));
      this.internalStr = this.internalStr.substring(this.internalStr.indexOf(separ) + separ.length());
      if (this.internalStr.length() == 0) {
        this.internalStr = "";
      }
    }
    return retval;
  }
  
  public String Extract()
  {
    return Extract(";");
  }
  
  public String toString()
  {
    return this.internalStr;
  }
}
