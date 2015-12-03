package mx.com.jammexico.jamsrv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class JAMResultSetDbf
{
  private static int ACTION_INSERT = 0;
  private static int ACTION_UPDATE = 1;
  private static String CTOD = "CTOD(" + JAMUtil.getComillas() + "  /  /  " + JAMUtil.getComillas() + ")";
  private Connection con = null;
  private String cDriver = null;
  private String cConnection = null;
  private String cUsuario = "";
  private String cPassword = "";
  private Vector vctName = new Vector();
  private Vector vctResultSet = new Vector();
  private Vector vctInsert = new Vector();
  private Vector vctUpdate = new Vector();
  private Vector vctCampos = new Vector();
  private Vector vctClave = new Vector();
  private Vector vctCTOD = new Vector();
  private boolean logVacio = false;
  private java.sql.Date fechahoy = new java.sql.Date(new java.util.Date().getTime());
  private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
  private int nActionActivo = -1;
  private String cNameActivo = null;
  private Vector vctClaveActivo = new Vector();
  private Vector vctClaveActivoValor = new Vector();
  private Vector vctUpdateCampo = new Vector();
  private Vector vctUpdateValor = new Vector();
  private Vector vctUpdateType = new Vector();
  
  public JAMResultSetDbf() {}
  
  public JAMResultSetDbf(String argDriver, String argConnection)
    throws Exception
  {
    JAMSetConnection(argDriver, argConnection);
  }
  
  public void JAMSetConnection(String argDriver, String argConnection)
    throws Exception
  {
    try
    {
      this.cDriver = argDriver;
      this.cConnection = argConnection;
      Class.forName(this.cDriver);
      this.con = DriverManager.getConnection(this.cConnection, this.cUsuario, this.cPassword);
    }
    catch (Exception e)
    {
      throw new Exception(e.getMessage());
    }
  }
  
  public ResultSet JAMSqlExecute(String argSql, int argModo)
    throws Exception
  {
    ResultSet rstRetorna = null;
    Connection conx = DriverManager.getConnection(this.cConnection, this.cUsuario, this.cPassword);
    Statement stmtx = conx.createStatement();
    try
    {
      if (argModo == 0) {
        return stmtx.executeQuery(argSql);
      }
      if (argModo == 1) {
        stmtx.executeUpdate(argSql);
      }
    }
    catch (Exception e)
    {
      throw new Exception(e.getMessage());
    }
    return rstRetorna;
  }
  
  public void JAMUpdateDefaultFechas() {}
  
  public JAMResultSetDbf JAMSetResultSet(String argSql, String argName)
    throws Exception
  {
    String cClaveCTOD = "";
    Vector vctCTODtmp = new Vector();
    ResultSet tmpRst = null;
    String[] arrCampos = (String[])null;
    
    this.logVacio = true;
    PreparedStatement stmInsert = null;
    PreparedStatement stmUpdate = null;
    argName = argName.toUpperCase().trim();
    try
    {
      tmpRst = JAMSqlExecute(argSql, 0);
    }
    catch (Exception e)
    {
      throw new Exception(e.getMessage());
    }
    if (argSql.toUpperCase().indexOf(" WHERE ") != -1)
    {
      cClaveCTOD = argSql.substring(argSql.toUpperCase().indexOf(" WHERE ") + 7).trim();
      if (cClaveCTOD.toUpperCase().indexOf(" ORDER ") != -1) {
        cClaveCTOD = cClaveCTOD.substring(0, cClaveCTOD.toUpperCase().indexOf(" ORDER "));
      }
    }
    try
    {
      String cCadeInto = "INSERT INTO " + argName + " (";
      String cCadeValu = "VALUES (";
      arrCampos = new String[tmpRst.getMetaData().getColumnCount() + 1];
      for (int j = 1; j <= tmpRst.getMetaData().getColumnCount(); j++)
      {
        arrCampos[j] = tmpRst.getMetaData().getColumnName(j);
        cCadeInto = cCadeInto + tmpRst.getMetaData().getColumnName(j) + ",";
        cCadeValu = cCadeValu + "?,";
      }
      cCadeInto = cCadeInto.substring(0, cCadeInto.length() - 1) + ")";
      cCadeValu = cCadeValu.substring(0, cCadeValu.length() - 1) + ")";
      String cCadenaFinal = cCadeInto + " " + cCadeValu;
      
      stmInsert = this.con.prepareStatement(cCadenaFinal);
      if (tmpRst.next()) {
        this.logVacio = false;
      }
      for (int j = 1; j <= tmpRst.getMetaData().getColumnCount(); j++) {
        if ((tmpRst.getMetaData().getColumnType(j) == 1) || 
          (tmpRst.getMetaData().getColumnType(j) == 12) || 
          (tmpRst.getMetaData().getColumnType(j) == -1))
        {
          stmInsert.setString(j, "");
        }
        else if ((tmpRst.getMetaData().getColumnType(j) == 91) || 
          (tmpRst.getMetaData().getColumnType(j) == 93))
        {
          stmInsert.setDate(j, this.fechahoy);
          String cCampo = tmpRst.getMetaData().getColumnName(j).trim().toUpperCase();
          vctCTODtmp.add(cCampo + " = " + CTOD);
        }
        else if ((tmpRst.getMetaData().getColumnType(j) == 2) || 
          (tmpRst.getMetaData().getColumnType(j) == 4) || 
          (tmpRst.getMetaData().getColumnType(j) == -7))
        {
          stmInsert.setInt(j, 0);
        }
        else
        {
          stmInsert.setNull(j, tmpRst.getMetaData().getColumnType(j));
        }
      }
    }
    catch (Exception e)
    {
      throw new Exception(e.getMessage());
    }
    int i = JAMUtil.JAMFindVector(this.vctName, argName);
    if (i != -1)
    {
      this.vctResultSet.set(i, tmpRst);
      this.vctInsert.set(i, stmInsert);
      this.vctUpdate.set(i, stmUpdate);
      this.vctCampos.set(i, arrCampos);
      this.vctClave.set(i, cClaveCTOD);
      this.vctCTOD.set(i, vctCTODtmp);
    }
    else
    {
      this.vctName.add(argName);
      this.vctResultSet.add(tmpRst);
      this.vctInsert.add(stmInsert);
      this.vctUpdate.add(stmUpdate);
      this.vctCampos.add(arrCampos);
      this.vctClave.add(cClaveCTOD);
      this.vctCTOD.add(vctCTODtmp);
    }
    return this;
  }
  
  public boolean JAMResultSetVacio()
  {
    return this.logVacio;
  }
  
  public void setInsert(String argName)
    throws Exception
  {
    argName = argName.toUpperCase().trim();
    this.cNameActivo = argName;
    this.nActionActivo = ACTION_INSERT;
    this.vctClaveActivo.clear();
    this.vctClaveActivoValor.clear();
    this.vctUpdateCampo.clear();
    this.vctUpdateValor.clear();
    this.vctUpdateType.clear();
  }
  
  public void setUpdate(String argName)
    throws Exception
  {
    argName = argName.toUpperCase().trim();
    this.cNameActivo = argName;
    this.nActionActivo = ACTION_UPDATE;
    this.vctClaveActivo.clear();
    this.vctClaveActivoValor.clear();
    this.vctUpdateCampo.clear();
    this.vctUpdateValor.clear();
    this.vctUpdateType.clear();
  }
  
  public void setUpdateClaveString(String argCampo, String argValor)
    throws Exception
  {
    int p = JAMUtil.JAMFindVector(this.vctName, this.cNameActivo);
    if (p == -1) {
      return;
    }
    if (!findCampo(argCampo)) {
      return;
    }
    int i = JAMUtil.JAMFindVector(this.vctClaveActivo, argCampo);
    if (i == -1)
    {
      this.vctClaveActivo.add(argCampo);
      this.vctClaveActivoValor.add(argValor);
    }
    else
    {
      this.vctClaveActivo.set(i, argCampo);
      this.vctClaveActivoValor.set(i, argValor);
    }
  }
  
  public void JAMExecuteTransaction()
    throws Exception
  {
    int i = JAMUtil.JAMFindVector(this.vctName, this.cNameActivo);
    if (i == -1) {
      return;
    }
    if (this.nActionActivo == ACTION_INSERT)
    {
      PreparedStatement tmpPrepStam = (PreparedStatement)this.vctInsert.get(i);
      String cClave = (String)this.vctClave.get(i);
      Vector vctCTODtmp = (Vector)this.vctCTOD.get(i);
      try
      {
        tmpPrepStam.executeUpdate();
        if ((vctCTODtmp.size() == 0) || (cClave.trim().equalsIgnoreCase(""))) {
          return;
        }
        String cCTOD = "";
        for (int p = 0; p < vctCTODtmp.size(); p++) {
          cCTOD = cCTOD + (String)vctCTODtmp.get(p) + ",";
        }
        cCTOD = cCTOD.substring(0, cCTOD.length() - 1);
        String cFechaCade = "update " + this.cNameActivo + " set " + cCTOD + " where " + cClave;
        JAMSqlExecute(cFechaCade, 1);
      }
      catch (SQLException g)
      {
        throw new Exception(g.getMessage());
      }
    }
    else if (this.nActionActivo == ACTION_UPDATE)
    {
      String cCadeInto = "UPDATE " + this.cNameActivo + " SET ";
      String cCadeValu = "where ";
      for (int p = 0; p < this.vctClaveActivo.size(); p++)
      {
        String cCampo = (String)this.vctClaveActivo.get(p);
        String cValor = (String)this.vctClaveActivoValor.get(p);
        cCadeValu = cCadeValu + cCampo + " = '" + cValor + "' and ";
      }
      cCadeValu = cCadeValu.trim().substring(0, cCadeValu.trim().length() - 4).trim();
      for (int j = 0; j < this.vctUpdateCampo.size(); j++)
      {
        String cCampo = (String)this.vctUpdateCampo.get(j);
        cCadeInto = cCadeInto + cCampo + " = ?,";
      }
      cCadeInto = cCadeInto.substring(0, cCadeInto.length() - 1);
      String cCadenaFinal = cCadeInto + " " + cCadeValu;
      
      PreparedStatement tmpPrepStam = (PreparedStatement)this.vctUpdate.get(i);
      try
      {
        tmpPrepStam = this.con.prepareStatement(cCadenaFinal);
        this.vctUpdate.set(i, tmpPrepStam);
      }
      catch (SQLException g)
      {
        throw new Exception(g.getMessage());
      }
      for (int j = 0; j < this.vctUpdateCampo.size(); j++)
      {
        String cCampo = (String)this.vctUpdateCampo.get(j);
        String cType = (String)this.vctUpdateType.get(j);
        ResultSet tmpRstUpdate = (ResultSet)this.vctResultSet.get(i);
        if (cType.equalsIgnoreCase("C")) {
          try
          {
            String cValorC = (String)this.vctUpdateValor.get(j);
            tmpPrepStam.setString(getIdColumn(cCampo, tmpRstUpdate), cValorC);
          }
          catch (SQLException es)
          {
            throw new Exception(es.getMessage());
          }
          catch (Exception ee)
          {
            throw new Exception(ee.getMessage());
          }
        } else if (cType.equalsIgnoreCase("D")) {
          try
          {
            Double dValorD = (Double)this.vctUpdateValor.get(j);
            tmpPrepStam.setDouble(getIdColumn(cCampo, tmpRstUpdate), dValorD.doubleValue());
          }
          catch (SQLException es)
          {
            throw new Exception(es.getMessage());
          }
          catch (Exception ee)
          {
            throw new Exception(ee.getMessage());
          }
        } else if (cType.equalsIgnoreCase("F")) {
          try
          {
            java.sql.Date fValorF = (java.sql.Date)this.vctUpdateValor.get(j);
            tmpPrepStam.setDate(getIdColumn(cCampo, tmpRstUpdate), fValorF);
          }
          catch (SQLException es)
          {
            throw new Exception(es.getMessage());
          }
          catch (Exception ee)
          {
            throw new Exception(ee.getMessage());
          }
        } else if (cType.equalsIgnoreCase("I")) {
          try
          {
            Integer iValorI = (Integer)this.vctUpdateValor.get(j);
            tmpPrepStam.setInt(getIdColumn(cCampo, tmpRstUpdate), iValorI.intValue());
          }
          catch (SQLException es)
          {
            throw new Exception(es.getMessage());
          }
          catch (Exception ee)
          {
            throw new Exception(ee.getMessage());
          }
        } else if (cType.equalsIgnoreCase("L")) {
          try
          {
            Long iValorI = (Long)this.vctUpdateValor.get(j);
            tmpPrepStam.setLong(getIdColumn(cCampo, tmpRstUpdate), iValorI.longValue());
          }
          catch (SQLException es)
          {
            throw new Exception(es.getMessage());
          }
          catch (Exception ee)
          {
            throw new Exception(ee.getMessage());
          }
        }
      }
      try
      {
        tmpPrepStam.executeUpdate();
      }
      catch (SQLException g)
      {
        throw new Exception(g.getMessage());
      }
    }
  }
  
  public ResultSet getRowSet()
  {
    int p = JAMUtil.JAMFindVector(this.vctName, this.cNameActivo);
    return (ResultSet)this.vctResultSet.get(p);
  }
  
  public void setString(String argCampo, String argValor)
    throws Exception
  {
    int i = JAMUtil.JAMFindVector(this.vctName, this.cNameActivo);
    if (i == -1) {
      return;
    }
    if (this.nActionActivo == ACTION_INSERT) {
      rstInsertActualizaCampo(i, argCampo, argValor);
    } else if (this.nActionActivo == ACTION_UPDATE) {
      rstUpdateActualizaCampo(argCampo, argValor);
    }
  }
  
  public void setMemo(String argCampo, String argValor)
    throws Exception
  {
    int i = JAMUtil.JAMFindVector(this.vctName, this.cNameActivo);
    if (i == -1) {
      return;
    }
    if (this.nActionActivo == ACTION_INSERT) {
      rstInsertActualizaCampo(i, argCampo, argValor);
    } else if (this.nActionActivo == ACTION_UPDATE) {
      rstUpdateActualizaCampo(argCampo, argValor);
    }
  }
  
  public void setDouble(String argCampo, double argValor)
    throws Exception
  {
    int i = JAMUtil.JAMFindVector(this.vctName, this.cNameActivo);
    if (i == -1) {
      return;
    }
    if (this.nActionActivo == ACTION_INSERT) {
      rstInsertActualizaCampo(i, argCampo, argValor);
    } else if (this.nActionActivo == ACTION_UPDATE) {
      rstUpdateActualizaCampo(argCampo, argValor);
    }
  }
  
  public void setDate(String argCampo, java.sql.Date argValor)
    throws Exception
  {
    int i = JAMUtil.JAMFindVector(this.vctName, this.cNameActivo);
    if (i == -1) {
      return;
    }
    if (this.nActionActivo == ACTION_INSERT)
    {
      rstInsertActualizaCampo(i, argCampo, argValor);
      
      Vector vctCTODtmp = new Vector();
      vctCTODtmp = (Vector)this.vctCTOD.get(i);
      String cCadena = argCampo.trim().toUpperCase() + " = " + CTOD;
      int iFindVector = JAMUtil.JAMFindVector(vctCTODtmp, cCadena);
      if (iFindVector != -1)
      {
        vctCTODtmp.remove(iFindVector);
        this.vctCTOD.set(i, vctCTODtmp);
      }
    }
    else if (this.nActionActivo == ACTION_UPDATE)
    {
      rstUpdateActualizaCampo(argCampo, argValor);
    }
  }
  
  public void setInt(String argCampo, int argValor)
    throws Exception
  {
    int i = JAMUtil.JAMFindVector(this.vctName, this.cNameActivo);
    if (i == -1) {
      return;
    }
    if (this.nActionActivo == ACTION_INSERT) {
      rstInsertActualizaCampo(i, argCampo, argValor);
    } else if (this.nActionActivo == ACTION_UPDATE) {
      rstUpdateActualizaCampo(argCampo, argValor);
    }
  }
  
  public void setLong(String argCampo, long argValor)
    throws Exception
  {
    int i = JAMUtil.JAMFindVector(this.vctName, this.cNameActivo);
    if (i == -1) {
      return;
    }
    if (this.nActionActivo == ACTION_INSERT) {
      rstInsertActualizaCampo(i, argCampo, new Long(argValor).intValue());
    } else if (this.nActionActivo == ACTION_UPDATE) {
      rstUpdateActualizaCampo(argCampo, new Long(argValor).intValue());
    }
  }
  
  public String getString(String argCampo, String argName)
    throws Exception
  {
    argName = argName.toUpperCase().trim();
    int i = JAMUtil.JAMFindVector(this.vctName, argName);
    if (i == -1) {
      return null;
    }
    ResultSet rstTemp = (ResultSet)this.vctResultSet.get(i);
    try
    {
      return rstTemp.getString(getIdColumnEnGets(argCampo, rstTemp));
    }
    catch (SQLException eq)
    {
      throw new Exception(eq.getMessage());
    }
    catch (Exception ee)
    {
      throw new Exception(ee.getMessage());
    }
  }
  
  private int getIdColumn(String argColumn, ResultSet argRst)
    throws Exception
  {
    int iRetor = 0;
    if (this.nActionActivo == ACTION_INSERT) {
      for (int j = 1; j <= argRst.getMetaData().getColumnCount(); j++) {
        if (argRst.getMetaData().getColumnName(j).toUpperCase().trim().equalsIgnoreCase(argColumn.toUpperCase().trim()))
        {
          iRetor = j;
          break;
        }
      }
    } else if (this.nActionActivo == ACTION_UPDATE) {
      for (int j = 0; j < this.vctUpdateCampo.size(); j++)
      {
        String cCampo = (String)this.vctUpdateCampo.get(j);
        cCampo = cCampo.trim().toUpperCase();
        if (cCampo.contains(argColumn.toUpperCase().trim())) {
          return j + 1;
        }
      }
    }
    return iRetor;
  }
  
  public int getIdColumnEnGets(String argColumn, ResultSet argRst)
    throws Exception
  {
    int iRetor = 0;
    for (int j = 1; j <= argRst.getMetaData().getColumnCount(); j++) {
      if (argRst.getMetaData().getColumnName(j).toUpperCase().trim().equalsIgnoreCase(argColumn.toUpperCase().trim()))
      {
        iRetor = j;
        break;
      }
    }
    return iRetor;
  }
  
  private boolean findCampo(String argCampo)
  {
    int u = JAMUtil.JAMFindVector(this.vctName, this.cNameActivo);
    if (u == -1) {
      return false;
    }
    String[] arrCampos = (String[])this.vctCampos.get(u);
    
    int v = JAMUtil.JAMFindArray(arrCampos, argCampo);
    if (v == -1) {
      return false;
    }
    return true;
  }
  
  private void rstInsertActualizaCampo(int iIdxVector, String argCampo, String argValor)
    throws Exception
  {
    PreparedStatement tmpPrepStam = (PreparedStatement)this.vctInsert.get(iIdxVector);
    ResultSet tmpRst = (ResultSet)this.vctResultSet.get(iIdxVector);
    try
    {
      tmpPrepStam.setString(getIdColumn(argCampo, tmpRst), argValor);
    }
    catch (SQLException es)
    {
      throw new Exception(es.getMessage());
    }
    catch (Exception ee)
    {
      throw new Exception(ee.getMessage());
    }
  }
  
  private void rstInsertActualizaCampo(int iIdxVector, String argCampo, double argValor)
    throws Exception
  {
    PreparedStatement tmpPrepStam = (PreparedStatement)this.vctInsert.get(iIdxVector);
    ResultSet tmpRst = (ResultSet)this.vctResultSet.get(iIdxVector);
    try
    {
      tmpPrepStam.setDouble(getIdColumn(argCampo, tmpRst), argValor);
    }
    catch (SQLException es)
    {
      throw new Exception(es.getMessage());
    }
    catch (Exception ee)
    {
      throw new Exception(ee.getMessage());
    }
  }
  
  private void rstInsertActualizaCampo(int iIdxVector, String argCampo, java.sql.Date argValor)
    throws Exception
  {
    PreparedStatement tmpPrepStam = (PreparedStatement)this.vctInsert.get(iIdxVector);
    ResultSet tmpRst = (ResultSet)this.vctResultSet.get(iIdxVector);
    try
    {
      tmpPrepStam.setDate(getIdColumn(argCampo, tmpRst), argValor);
    }
    catch (SQLException es)
    {
      throw new Exception(es.getMessage());
    }
    catch (Exception ee)
    {
      throw new Exception(ee.getMessage());
    }
  }
  
  private void rstInsertActualizaCampo(int iIdxVector, String argCampo, int argValor)
    throws Exception
  {
    PreparedStatement tmpPrepStam = (PreparedStatement)this.vctInsert.get(iIdxVector);
    ResultSet tmpRst = (ResultSet)this.vctResultSet.get(iIdxVector);
    try
    {
      tmpPrepStam.setInt(getIdColumn(argCampo, tmpRst), argValor);
    }
    catch (SQLException es)
    {
      throw new Exception(es.getMessage());
    }
    catch (Exception ee)
    {
      throw new Exception(ee.getMessage());
    }
  }
  
  private void rstUpdateActualizaCampo(String argCampo, String argValor)
  {
    if (!findCampo(argCampo)) {
      return;
    }
    int p = JAMUtil.JAMFindVector(this.vctUpdateCampo, argCampo);
    if (p == -1)
    {
      this.vctUpdateCampo.add(argCampo);
      this.vctUpdateValor.add(argValor);
      this.vctUpdateType.add("C");
    }
    else
    {
      this.vctUpdateCampo.set(p, argCampo);
      this.vctUpdateValor.set(p, argValor);
      this.vctUpdateType.add("C");
    }
  }
  
  private void rstUpdateActualizaCampo(String argCampo, double argValor)
  {
    if (!findCampo(argCampo)) {
      return;
    }
    int p = JAMUtil.JAMFindVector(this.vctUpdateCampo, argCampo);
    if (p == -1)
    {
      this.vctUpdateCampo.add(argCampo);
      this.vctUpdateValor.add(new Double(argValor));
      this.vctUpdateType.add("D");
    }
    else
    {
      this.vctUpdateCampo.set(p, argCampo);
      this.vctUpdateValor.set(p, new Double(argValor));
      this.vctUpdateType.add("D");
    }
  }
  
  private void rstUpdateActualizaCampo(String argCampo, java.sql.Date argValor)
  {
    if (!findCampo(argCampo)) {
      return;
    }
    int p = JAMUtil.JAMFindVector(this.vctUpdateCampo, argCampo);
    if (p == -1)
    {
      this.vctUpdateCampo.add(argCampo);
      this.vctUpdateValor.add(argValor);
      this.vctUpdateType.add("F");
    }
    else
    {
      this.vctUpdateCampo.set(p, argCampo);
      this.vctUpdateValor.set(p, argValor);
      this.vctUpdateType.add("F");
    }
  }
  
  private void rstUpdateActualizaCampo(String argCampo, int argValor)
  {
    if (!findCampo(argCampo)) {
      return;
    }
    int p = JAMUtil.JAMFindVector(this.vctUpdateCampo, argCampo);
    if (p == -1)
    {
      this.vctUpdateCampo.add(argCampo);
      this.vctUpdateValor.add(new Integer(argValor));
      this.vctUpdateType.add("I");
    }
    else
    {
      this.vctUpdateCampo.set(p, argCampo);
      this.vctUpdateValor.set(p, new Integer(argValor));
      this.vctUpdateType.add("I");
    }
  }
}
