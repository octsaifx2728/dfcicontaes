package mx.com.jammexico.jamsrv;

import java.io.Serializable;
import java.sql.ResultSetMetaData;
import java.util.Vector;

public class JAMDataActionsGroup
  implements Serializable
{
  private Vector Actions = new Vector();
  private Vector newIds;
  private int pvtIntLastSubindex = -1;
  private int errorsCounter = 0;
  private int rowsetCounter = 0;
  private int queryCounter = 0;
  private String[] cTransactionDirectives = null;
  
  public JAMDataActionsGroup()
  {
    this.Actions = new Vector();
    this.newIds = new Vector();
    this.pvtIntLastSubindex = -1;
  }
  
  public int getNewIdsCount()
  {
    return this.newIds.size();
  }
  
  public int getLength()
  {
    return this.pvtIntLastSubindex + 1;
  }
  
  public JAMDataAction getLastAction()
  {
    if (this.Actions.size() == 0) {
      return null;
    }
    return (JAMDataAction)this.Actions.get(this.Actions.size() - 1);
  }
  
  public void addNewid(String name, int value)
  {
    this.newIds.add(new JAMNewId(name, value));
  }
  
  public int getNewId(String name)
  {
    for (int i = 0; i < this.newIds.size(); i++)
    {
      JAMNewId n = (JAMNewId)this.newIds.get(i);
      if (n.getName().compareToIgnoreCase(name) == 0) {
        return n.getValue();
      }
    }
    return -1;
  }
  
  public JAMNewId getNewIdObject(int index)
  {
    return (JAMNewId)this.newIds.get(index);
  }
  
  public JAMDataAction getAction(int index)
  {
    return (JAMDataAction)this.Actions.get(index);
  }
  
  public JAMDataAction getAction(String actionName)
    throws Exception
  {
    int index = -1;
    for (int j = 0; j < this.Actions.size(); j++)
    {
      JAMDataAction act = (JAMDataAction)this.Actions.get(j);
      if (act.getname().trim().compareToIgnoreCase(actionName.trim()) == 0)
      {
        index = j;
        break;
      }
    }
    if (index == -1) {
      throw new Exception("No existe la accion " + actionName);
    }
    return (JAMDataAction)this.Actions.get(index);
  }
  
  public int getActionIndex(String actionName)
    throws Exception
  {
    int index = -1;
    for (int j = 0; j < this.Actions.size(); j++)
    {
      JAMDataAction act = (JAMDataAction)this.Actions.get(j);
      if (act.getname().trim().compareToIgnoreCase(actionName.trim()) == 0)
      {
        index = j;
        break;
      }
    }
    return index;
  }
  
  public void addAction(JAMRowSet pRowSet)
  {
    this.Actions.add(new JAMDataAction(pRowSet, this.cTransactionDirectives));
    this.pvtIntLastSubindex += 1;
    this.rowsetCounter += 1;
    
    JAMDataAction act = (JAMDataAction)this.Actions.lastElement();
    act.setName("ROWSET" + this.rowsetCounter);
  }
  
  public void addAction(JAMRowSet pRowSet, String[] transactionDirectives)
    throws Exception
  {
    this.cTransactionDirectives = transactionDirectives;
    addAction(pRowSet);
    JAMDataAction act = (JAMDataAction)this.Actions.lastElement();
    JAMParsingString p = null;
    for (int i = 0; i < transactionDirectives.length; i++)
    {
      String dir = transactionDirectives[i];
      
      boolean procesed = false;
      if (dir.indexOf("NAME=") == 0)
      {
        p = new JAMParsingString(dir);
        p.Extract("=");
        String actName = p.Extract();
        if (actName == "") {
          throw new Exception("No especifico el nombre en la directiva NAME");
        }
        String idfld = p.Extract();
        
        JAMRowSet Rw = act.getRowSet();
        if (idfld == "") {
          try
          {
            int[] pk = Rw.getKeyColumns();
            if (pk != null) {
              if ((pk.length == 1) && 
                (Rw.getMetaData().getColumnTypeName(pk[0]).trim().compareToIgnoreCase("NUMBER") == 0)) {
                idfld = Rw.getMetaData().getColumnName(pk[0]);
              }
            }
          }
          catch (Exception e)
          {
            throw e;
          }
        }
        if (idfld == "") {
          throw new Exception("No especifico el nombre del campo id en la directiva NAME");
        }
        boolean existField = false;
        try
        {
          for (int j = 1; j <= Rw.getMetaData().getColumnCount(); j++) {
            if (idfld.trim().compareToIgnoreCase(Rw.getMetaData().getColumnName(j).trim()) == 0)
            {
              existField = true;
              break;
            }
          }
        }
        catch (Exception e)
        {
          throw new Exception("No pudo validarse el campo id especificado en la directiva (" + idfld + "). El Error fue " + e.getMessage());
        }
        if (!existField) {
          throw new Exception("El campo id especificado en la directiva (" + idfld + ") no es un campo del Rowset ");
        }
        act.setName(actName);
        Rw.setidFieldName(idfld);
        procesed = true;
      }
      if (dir.indexOf("HEADER=") == 0)
      {
        p = new JAMParsingString(dir);
        p.Extract("=");
        String headerName = p.Extract();
        if (headerName == "") {
          throw new Exception("No especifico el nombre del Header en la directiva");
        }
        if (getActionIndex(headerName) == -1) {
          throw new Exception("El Header especificado (" + headerName + ") no existe en el grupo.");
        }
        String fkfld = p.Extract();
        if (fkfld == "") {
          throw new Exception("No se especifico el campo del Rowset Asociado al Header");
        }
        JAMRowSet Rw = act.getRowSet();
        
        boolean existField = false;
        try
        {
          for (int j = 1; j <= Rw.getMetaData().getColumnCount(); j++) {
            if (fkfld.trim().compareToIgnoreCase(Rw.getMetaData().getColumnName(j).trim()) == 0)
            {
              existField = true;
              break;
            }
          }
        }
        catch (Exception e)
        {
          throw new Exception("No pudo validarse el campo asociado especificado en la directiva (" + fkfld + "). El Error fue " + e.getMessage());
        }
        if (!existField) {
          throw new Exception("El campo asociado especificado en la directiva (" + fkfld + ") no es un campo del Rowset ");
        }
        try
        {
          if (getAction(headerName).getType() == 5)
          {
            JAMRowSet Rwh = getAction(headerName).getRowSet();
            
            Rwh.addChildren(act.getname(), fkfld);
          }
          else
          {
            throw new Exception("El Hedaer espeificado no es un rowset");
          }
        }
        catch (Exception e)
        {
          throw new Exception("Add child error " + e.getMessage());
        }
        procesed = true;
      }
      if (dir.indexOf("SYSDATEFIELD=") == 0) {
        procesed = true;
      }
      if (!procesed) {
        throw new Exception("Directiva de transacion invalida : " + dir);
      }
    }
  }
  
  public void addAction(String pSqlCmd)
  {
    this.Actions.add(new JAMDataAction(pSqlCmd));
    this.pvtIntLastSubindex += 1;
    JAMDataAction act = (JAMDataAction)this.Actions.lastElement();
    
    this.queryCounter += 1;
    act.setName("QUERY" + this.queryCounter);
  }
  
  public void addAction(String pSqlCmd, String cmdName)
  {
    addAction(pSqlCmd);
    JAMDataAction act = (JAMDataAction)this.Actions.lastElement();
    act.setName(cmdName);
  }
  
  public void addAction(JAMDataAction pacc)
  {
    this.Actions.add(pacc);
    this.pvtIntLastSubindex += 1;
  }
  
  public void disposeAction(int index)
  {
    JAMDataAction act = (JAMDataAction)this.Actions.get(index);
    act.dispose();
  }
  
  public int getErrorCount()
  {
    return this.errorsCounter;
  }
}
