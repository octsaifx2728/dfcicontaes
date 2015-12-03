package mx.com.jammexico.jamsrv;

import com.sun.rowset.CachedRowSetImpl;
import com.sun.rowset.internal.BaseRow;
import com.sun.rowset.internal.CachedRowSetReader;
import com.sun.rowset.internal.CachedRowSetWriter;
import com.sun.rowset.internal.InsertRow;
import com.sun.rowset.internal.Row;
import com.sun.rowset.providers.RIOptimisticProvider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StreamCorruptedException;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import javax.sql.RowSet;
import javax.sql.RowSetEvent;
import javax.sql.RowSetMetaData;
import javax.sql.RowSetReader;
import javax.sql.RowSetWriter;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.RowSetWarning;
import javax.sql.rowset.serial.SQLInputImpl;
import javax.sql.rowset.serial.SerialArray;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialRef;
import javax.sql.rowset.serial.SerialStruct;
import javax.sql.rowset.spi.SyncFactory;
import javax.sql.rowset.spi.SyncProvider;
import javax.sql.rowset.spi.SyncProviderException;
import javax.sql.rowset.spi.TransactionalWriter;

public class JAMRowSet
  extends CachedRowSetImpl
{
  private static final long serialVersionUID = 4604576161145677218L;
  private String rowsetName = null;
  private String idFieldName = null;
  private String selectCmd;
  private String updateCmd;
  private String updateWhere;
  private String deleteCmd;
  private String deleteWhere;
  private String insertCmd;
  private Object[] params;
  private int columnCount;
  private ResultSetMetaData metaData;
  private Connection prvCon;
  private int lastgeneratedid = 0;
  private SyncProvider provider;
  public String cUsuario;
  public static final int DB_MAIN = -1;
  public static final int DB_EXTEND = 0;
  public static final int DB_SECURITY = 1;
  public static final int DB_RESERV01 = 2;
  public static final int DB_RESERV02 = 3;
  public static final int DB_RESERV03 = 4;
  public static final int DB_RESERV04 = 5;
  public static final int DB_RESERV05 = 6;
  public static final int DB_RESERV06 = 7;
  public static final int DB_RESERV07 = 8;
  public static final int DB_RESERV08 = 9;
  public static boolean logEnDesarrollo;
  private RowSetReader rowSetReader;
  private RowSetWriter rowSetWriter;
  private transient Connection conn;
  private transient ResultSetMetaData RSMD;
  private RowSetMetaDataImpl RowSetMD;
  private int[] keyCols;
  private String tableName;
  private Vector rvh;
  private int cursorPos;
  private int absolutePos;
  private int numDeleted;
  private int numRows;
  private InsertRow insertRow;
  private boolean onInsertRow;
  private int currentRow;
  private boolean lastValueNull;
  private SQLWarning sqlwarn;
  private String strMatchColumn = "";
  private int iMatchColumn = -1;
  private RowSetWarning rowsetWarning;
  private String DEFAULT_SYNC_PROVIDER = "com.sun.rowset.providers.RIOptimisticProvider";
  private boolean dbmslocatorsUpdateCopy;
  private ResultSet resultSet;
  private int endPos;
  private int prevEndPos;
  private int startPos;
  private int startPrev;
  private int pageSize;
  private int maxRowsreached;
  private boolean pagenotend = true;
  private boolean onFirstPage;
  private boolean onLastPage;
  private int populatecallcount;
  private int totalRows;
  private boolean callWithCon;
  private CachedRowSetReader crsReader;
  private Vector iMatchColumns;
  private Vector strMatchColumns;
  private boolean tXWriter = false;
  private TransactionalWriter tWriter = null;
  private Vector childrens = null;
  private JAMDataActionsGroup rwGrp = null;
  
  public void setGroup(JAMDataActionsGroup pGrp)
  {
    this.rwGrp = pGrp;
  }
  
  public void addChildren(String pRowswetName, String relationFld)
  {
    if (this.childrens == null) {
      this.childrens = new Vector();
    }
    JAMRowsetChild pc = new JAMRowsetChild(pRowswetName, relationFld);
    this.childrens.add(pc);
  }
  
  public JAMRowSet RefreshData()
  {
    JAMRowSet ret = null;
    if (getCommand() != null) {
      try
      {
        ret = mx.com.jammexico.jamdb.JAMClienteDB.getRowSets(new String[] { getCommand() })[0];
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return ret;
  }
  
  public long getRowid()
    throws Exception
  {
    if (this.idFieldName != null) {
      return getLong(this.idFieldName);
    }
    throw new Exception("No esta definido el nombre del campo de ID.(Usar el metodo setIdFieldName)");
  }
  
  public JAMRowSet()
    throws SQLException
  {
    initConstruct();
  }
  
  public JAMRowSet(String name)
    throws SQLException
  {
    initConstruct();
    this.rowsetName = name;
  }
  
  public void setConn(Connection pconn)
  {
    this.prvCon = pconn;
  }
  
  public void cancelIDModifications()
  {
    try
    {
      beforeFirst();
      while (next()) {
        if ((rowInserted()) || (rowUpdated()) || (rowDeleted())) {
          cancelRowUpdates();
        }
      }
    }
    catch (Exception localException) {}
  }
  
  public void submitDeleted()
    throws SQLException
  {
    initSQLStatements();
    
    boolean saveDeletedStatus = getShowDeleted();
    setShowDeleted(true);
    beforeFirst();
    while (next()) {
      if (rowDeleted()) {
        deleteOriginalRow();
      }
    }
    setShowDeleted(saveDeletedStatus);
  }
  
  public void submitInsertedUpdated()
    throws SQLException
  {
    initSQLStatements();
    
    PreparedStatement pstmt = this.prvCon.prepareStatement(this.insertCmd);
    
    boolean saveDeletedStatus = getShowDeleted();
    setShowDeleted(false);
    beforeFirst();
    while (next()) {
      if (rowInserted()) {
        insertNewRow();
      } else if (rowUpdated()) {
        updateOriginalRow();
      }
    }
    setShowDeleted(saveDeletedStatus);
  }
  
  private void updateOriginalRow()
    throws SQLException
  {
    PreparedStatement pstmt = null;
    int i = 0;
    int idx = 0;
    
    ResultSet origVals = getOriginalRow();
    origVals.next();
    
    this.updateWhere = buildWhereClause(this.updateWhere, origVals);
    
    int colsNotChanged = 0;
    Vector cols = new Vector();
    String updateExec = new String(this.updateCmd);
    
    boolean boolNull = true;
    Object objVal = null;
    
    boolean first = true;
    for (i = 1; i <= this.metaData.getColumnCount(); i++) {
      if (columnUpdated(i))
      {
        if (!first) {
          updateExec = updateExec + ", ";
        }
        updateExec = updateExec + this.metaData.getColumnName(i);
        cols.add(new Integer(i));
        updateExec = updateExec + " = ? ";
        first = false;
      }
    }
    updateExec = updateExec + this.updateWhere;
    
    pstmt = this.prvCon.prepareStatement(updateExec);
    for (i = 0; i < cols.size(); i++)
    {
      Object obj = getObject(((Integer)cols.get(i)).intValue());
      if (obj != null) {
        pstmt.setObject(i + 1, obj);
      } else {
        pstmt.setNull(i + 1, this.metaData.getColumnType(i + 1));
      }
    }
    idx = i;
    for (i = 0; i < this.keyCols.length; i++) {
      if (this.params[i] != null) {
        pstmt.setObject(++idx, this.params[i]);
      }
    }
    if (pstmt.executeUpdate() != 1) {
      throw new SQLException("se esta  updateando mas de una filea ");
    }
    pstmt.close();
  }
  
  private void insertNewRow()
    throws SQLException
  {
    PreparedStatement pinsstmt = this.prvCon.prepareStatement(this.insertCmd);
    
    int i = 0;
    int icolCount = this.metaData.getColumnCount();
    for (i = 1; i <= icolCount; i++)
    {
      Object obj = getObject(i);
      if (obj != null) {
        pinsstmt.setObject(i, obj);
      } else {
        pinsstmt.setNull(i, this.metaData.getColumnType(i));
      }
    }
    if (pinsstmt.executeUpdate() != 1) {
      throw new SQLException("se esta insertando mas de una fila ");
    }
    pinsstmt.close();
    
    long OldId = getLong(this.idFieldName);
    try
    {
      if (this.idFieldName != null)
      {
        updateLong(this.idFieldName, getLastId(getLong(this.idFieldName)));
        updateRow();
      }
    }
    catch (Exception e)
    {
      throw new SQLException("Error Obteniendo el ANIBAL id real : " + e.getMessage());
    }
    if (this.childrens != null)
    {
      JAMRowsetChild chld = null;
      JAMRowSet rwchild = null;
      try
      {
        for (int ch = 0; ch < this.childrens.size(); ch++)
        {
          chld = (JAMRowsetChild)this.childrens.get(ch);
          rwchild = this.rwGrp.getAction(chld.getRowsetName()).getRowSet();
          rwchild.beforeFirst();
          while (rwchild.next()) {
            if (rwchild.getLong(chld.getRelatedFiled()) == OldId)
            {
              rwchild.updateLong(chld.getRelatedFiled(), getRowid());
              rwchild.updateRow();
            }
          }
        }
      }
      catch (Exception e)
      {
        throw new SQLException("Error actualizando el id real  : " + e.getMessage());
      }
    }
  }
  
  public void setidFieldName(String argId)
  {
    this.idFieldName = argId;
  }
  
  public String getidFieldName()
    throws SQLException
  {
    if ((this.idFieldName != null) && (!this.idFieldName.equalsIgnoreCase(""))) {
      return this.idFieldName;
    }
    for (int iw = 1; iw <= getMetaData().getColumnCount(); iw++)
    {
      String cCampoRead = getMetaData().getColumnName(iw);
      if (cCampoRead.startsWith("ID_")) {
        this.idFieldName = cCampoRead;
      }
    }
    return this.idFieldName;
  }
  
  public void setName(String pname)
  {
    this.rowsetName = pname;
  }
  
  public String getName()
  {
    return this.rowsetName;
  }
  
  public int getLastGeneratedId()
  {
    return this.lastgeneratedid;
  }
  
  public long getLastId(long pid)
    throws Exception
  {
    long retval = 0L;
    try
    {
      PreparedStatement pstmt;
      PreparedStatement pstmt;
      if (this.prvCon.getMetaData().getDatabaseProductName().toLowerCase().contains("oracle"))
      {
        pstmt = this.prvCon.prepareStatement("Select Id_Value From lastId");
      }
      else if (this.prvCon.getMetaData().getDatabaseProductName().toLowerCase().contains("firebird"))
      {
        PreparedStatement pstmt = this.prvCon.prepareStatement("Select SOCUTIL02_ULTID From SOCUTIL02_TBL_ULTIDS WHERE SOCUTIL02_USUARIO='" + this.cUsuario + "'");
        System.out.println("SABER EL USUARIO: " + this.cUsuario);
      }
      else
      {
        pstmt = this.prvCon.prepareStatement("select LAST_INSERT_ID()");
      }
      ResultSet rst = pstmt.executeQuery();
      rst.next();
      retval = rst.getLong(1);
      rst.close();
      pstmt.close();
      this.lastgeneratedid = ((int)retval);
      return retval;
    }
    catch (Exception e)
    {
      System.out.println(e.getMessage());
      throw new Exception(e.getMessage());
    }
  }
  
  private void deleteOriginalRow()
    throws SQLException
  {
    int idx = 0;
    
    ResultSet origVals = getOriginalRow();
    origVals.next();
    
    this.deleteWhere = buildWhereClause(this.deleteWhere, origVals);
    String cmd = this.deleteCmd + this.deleteWhere;
    PreparedStatement pstmt = this.prvCon.prepareStatement(cmd);
    
    idx = 0;
    for (int i = 0; i < this.keyCols.length; i++) {
      if (this.params[i] != null) {
        pstmt.setObject(++idx, this.params[i]);
      }
    }
    if (pstmt.executeUpdate() != 1) {
      throw new SQLException("Se esta borrando mas de una fila");
    }
    pstmt.close();
  }
  
  public int[] getkeys()
  {
    return this.keyCols;
  }
  
  public int getColumnIndex(String cName)
    throws Exception
  {
    int retval = -1;
    for (int i = 1; i <= getMetaData().getColumnCount(); i++) {
      if (getMetaData().getColumnName(i).compareToIgnoreCase(cName) == 0)
      {
        retval = i;
        break;
      }
    }
    return retval;
  }
  
  public void DefineKeys()
  {
    if (this.keyCols == null) {
      try
      {
        ResultSet pk = this.prvCon.getMetaData().getPrimaryKeys(null, null, getTableName().toUpperCase());
        
        Vector p = new Vector();
        int keycont = 0;
        while (pk.next())
        {
          Integer sa = new Integer(getColumnIndex(pk.getString(4)));
          p.add(sa);
          keycont++;
        }
        this.keyCols = new int[keycont];
        for (keycont = 0; keycont < this.keyCols.length; keycont++)
        {
          Integer ui = (Integer)p.get(keycont);
          this.keyCols[keycont] = ui.intValue();
        }
      }
      catch (Exception ex)
      {
        System.out.println("no encontro el pk " + ex.getMessage());
      }
    }
  }
  
  private void initSQLStatements()
    throws SQLException
  {
    if (this.insertCmd != null) {
      return;
    }
    this.metaData = getMetaData();
    
    DefineKeys();
    
    this.columnCount = this.metaData.getColumnCount();
    if (this.columnCount < 1) {
      return;
    }
    String table = getTableName();
    if (table == null)
    {
      table = this.metaData.getTableName(1);
      if ((table == null) || (table.length() == 0)) {
        throw new SQLException("initSQLStatements cannot determine the table name.");
      }
    }
    String catalog = this.metaData.getCatalogName(1);
    
    String schema = this.metaData.getSchemaName(1);
    DatabaseMetaData dbmd = this.prvCon.getMetaData();
    
    this.selectCmd = "SELECT ";
    for (int i = 1; i <= this.columnCount; i++)
    {
      this.selectCmd += this.metaData.getColumnName(i);
      if (i < this.columnCount) {
        this.selectCmd += ", ";
      } else {
        this.selectCmd += " ";
      }
    }
    this.selectCmd = (this.selectCmd + "FROM " + buildTableName(dbmd, catalog, schema, table));
    
    this.updateCmd = ("UPDATE " + buildTableName(dbmd, catalog, schema, table));
    
    String tempupdCmd = this.updateCmd.toLowerCase();
    int idxupWhere = tempupdCmd.indexOf(" where");
    if (idxupWhere != -1) {
      this.updateCmd = this.updateCmd.substring(0, idxupWhere);
    }
    this.updateCmd += "SET ";
    
    this.insertCmd = ("INSERT INTO " + buildTableName(dbmd, catalog, schema, table));
    
    this.insertCmd += "(";
    for (i = 1; i <= this.columnCount; i++)
    {
      this.insertCmd += this.metaData.getColumnName(i);
      if (i < this.metaData.getColumnCount()) {
        this.insertCmd += ", ";
      } else {
        this.insertCmd += ") VALUES (";
      }
    }
    for (i = 1; i <= this.columnCount; i++)
    {
      this.insertCmd += "?";
      if (i < this.columnCount) {
        this.insertCmd += ", ";
      } else {
        this.insertCmd += ")";
      }
    }
    this.deleteCmd = ("DELETE FROM " + buildTableName(dbmd, catalog, schema, table));
    
    buildKeyDesc();
  }
  
  private String buildTableName(DatabaseMetaData dbmd, String catalog, String schema, String table)
    throws SQLException
  {
    String cmd = new String();
    
    catalog = catalog.trim();
    schema = schema.trim();
    table = table.trim();
    if (dbmd.isCatalogAtStart())
    {
      if ((catalog != null) && (catalog.length() > 0)) {
        cmd = cmd + catalog + dbmd.getCatalogSeparator();
      }
      if ((schema != null) && (schema.length() > 0)) {
        cmd = cmd + schema + ".";
      }
      cmd = cmd + table;
    }
    else
    {
      if ((schema != null) && (schema.length() > 0)) {
        cmd = cmd + schema + ".";
      }
      cmd = cmd + table;
      if ((catalog != null) && (catalog.length() > 0)) {
        cmd = cmd + dbmd.getCatalogSeparator() + catalog;
      }
    }
    cmd = cmd + " ";
    return cmd;
  }
  
  public long getRowcount()
    throws Exception
  {
    return toCollection().size();
  }
  
  private void buildKeyDesc()
    throws SQLException
  {
    if ((this.keyCols == null) || (this.keyCols.length == 0))
    {
      this.keyCols = new int[this.columnCount];
      for (int i = 0; i < this.keyCols.length;) {
        this.keyCols[(i++)] = i;
      }
    }
    this.params = new Object[this.keyCols.length];
  }
  
  private String buildWhereClause(String whereClause, ResultSet rs)
    throws SQLException
  {
    whereClause = "WHERE ";
    for (int i = 0; i < this.keyCols.length; i++)
    {
      if (i > 0) {
        whereClause = whereClause + "AND ";
      }
      whereClause = whereClause + this.metaData.getColumnName(this.keyCols[i]);
      this.params[i] = rs.getObject(this.keyCols[i]);
      if (rs.wasNull()) {
        whereClause = whereClause + " IS NULL ";
      } else {
        whereClause = whereClause + " = ? ";
      }
    }
    return whereClause;
  }
  
  public void checkForPending()
    throws Exception
  {
    if (getRow() != 0) {
      if (insertPending())
      {
        insertRow();
        moveToCurrentRow();
      }
      else
      {
        updateRow();
      }
    }
  }
  
  public boolean find(String colname, java.util.Date fieldvalue)
    throws Exception
  {
    boolean retval = false;
    
    beforeFirst();
    while (next()) {
      if (getDateTime(colname).compareTo(fieldvalue) == 0)
      {
        retval = true;
        break;
      }
    }
    return retval;
  }
  
  public boolean find(String colname, int fieldvalue)
    throws Exception
  {
    boolean retval = false;
    
    beforeFirst();
    while (next()) {
      if (getInt(colname) == fieldvalue)
      {
        retval = true;
        break;
      }
    }
    return retval;
  }
  
  public JAMRowSet findRst(String colname, int fieldvalue)
    throws Exception
  {
    JAMRowSet retval = null;
    
    beforeFirst();
    while (next()) {
      if (getInt(colname) == fieldvalue)
      {
        retval = this;
        break;
      }
    }
    return retval;
  }
  
  public JAMRowSet findRst(String colname, String fieldvalue)
    throws Exception
  {
    JAMRowSet retval = null;
    
    beforeFirst();
    while (next()) {
      if (JAMUtil.JAMConvNullStr(getString(colname)).trim().equalsIgnoreCase(fieldvalue))
      {
        retval = this;
        break;
      }
    }
    return retval;
  }
  
  public boolean findExclude(String colname, int fieldvalue, String colnameid, int fieldvalueid)
    throws Exception
  {
    boolean retval = false;
    
    beforeFirst();
    while (next()) {
      if ((getInt(colname) == fieldvalue) && 
        (getInt(colnameid) != fieldvalueid))
      {
        retval = true;
        break;
      }
    }
    return retval;
  }
  
  public boolean findExclude(String colname, String fieldvalue, String colnameid, int fieldvalueid)
    throws Exception
  {
    boolean retval = false;
    
    beforeFirst();
    while (next())
    {
      String cPaso = getString(colname);
      
      cPaso = cPaso == null ? "" : cPaso.trim();
      if ((cPaso.equalsIgnoreCase(fieldvalue.trim())) && 
        (getInt(colnameid) != fieldvalueid))
      {
        retval = true;
        break;
      }
    }
    return retval;
  }
  
  public boolean findExcludeAll(String colname, String fieldvalue, String colnameid, int fieldvalueid)
    throws Exception
  {
    boolean retval = false;
    
    beforeFirst();
    while (next())
    {
      String cPaso = getString(colname);
      cPaso = cPaso == null ? "" : cPaso.trim();
      if ((cPaso.toLowerCase().indexOf(fieldvalue.trim().toLowerCase()) != -1) && 
        (getInt(colnameid) != fieldvalueid))
      {
        retval = true;
        break;
      }
    }
    return retval;
  }
  
  public boolean find(String colname, double fieldvalue)
    throws Exception
  {
    boolean retval = false;
    
    beforeFirst();
    while (next()) {
      if (getDouble(colname) == fieldvalue)
      {
        retval = true;
        break;
      }
    }
    return retval;
  }
  
  public boolean find(String colname, String fieldvalue)
    throws Exception
  {
    boolean retval = false;
    beforeFirst();
    while (next()) {
      try
      {
        String cPaso = JAMUtil.JAMConvNullStr(getString(colname));
        if (cPaso.equalsIgnoreCase(fieldvalue.trim())) {
          retval = true;
        }
      }
      catch (Exception localException) {}
    }
    return retval;
  }
  
  public boolean findAll(String colname, String fieldvalue, boolean logModalidad)
    throws Exception
  {
    boolean retval = false;
    beforeFirst();
    while (next()) {
      try
      {
        String cPaso = getString(colname);
        cPaso = cPaso.trim();
        if ((cPaso.startsWith(fieldvalue.trim())) || (cPaso.endsWith(fieldvalue.trim()))) {
          retval = true;
        } else if (logModalidad)
        {
          if (cPaso.toUpperCase().indexOf(" " + fieldvalue.trim().toUpperCase() + " ") != -1)
          {
            retval = true;
            break;
          }
        }
        else if (cPaso.toUpperCase().indexOf(fieldvalue.trim().toUpperCase()) != -1) {
          retval = true;
        }
      }
      catch (Exception localException) {}
    }
    return retval;
  }
  
  public int findID(String colname, String fieldvalue, String fieldID)
    throws Exception
  {
    int retval = -1;
    
    beforeFirst();
    while (next()) {
      if ((getString(colname) != null) && 
        (getString(colname).trim().equalsIgnoreCase(fieldvalue.trim())))
      {
        retval = getInt(fieldID);
        break;
      }
    }
    return retval;
  }
  
  public int findID(String colname, Double fieldvalue, String fieldID)
    throws Exception
  {
    int retval = -1;
    
    beforeFirst();
    while (next()) {
      if ((getString(colname) != null) && 
        (getDouble(colname) == fieldvalue.doubleValue()))
      {
        retval = getInt(fieldID);
        break;
      }
    }
    return retval;
  }
  
  public int findIDFiltro(String colname, String fieldvalue, String fieldID, String colnamefiltro, int valorfiltro)
    throws Exception
  {
    int retval = -1;
    
    beforeFirst();
    while (next()) {
      if ((getString(colname) != null) && 
        (getString(colname).trim().equalsIgnoreCase(fieldvalue.trim())) && 
        (getInt(colnamefiltro) == valorfiltro))
      {
        retval = getInt(fieldID);
        break;
      }
    }
    return retval;
  }
  
  public int findID(String colname, int fieldvalue, String fieldID)
    throws Exception
  {
    int retval = -1;
    
    beforeFirst();
    while (next()) {
      if (getInt(colname) == fieldvalue)
      {
        retval = getInt(fieldID);
        break;
      }
    }
    return retval;
  }
  
  private void initConstruct()
    throws SQLException
  {
    this.provider = 
      SyncFactory.getInstance(this.DEFAULT_SYNC_PROVIDER);
    if (!(this.provider instanceof RIOptimisticProvider)) {
      throw new SQLException("Invalid persistence provider generated");
    }
    this.rowSetReader = ((CachedRowSetReader)this.provider.getRowSetReader());
    this.rowSetWriter = ((CachedRowSetWriter)this.provider.getRowSetWriter());
    
    initParams();
    
    initContainer();
    
    initProperties();
    
    this.onInsertRow = false;
    this.insertRow = null;
    
    this.sqlwarn = new SQLWarning();
    this.rowsetWarning = new RowSetWarning();
  }
  
  private void initContainer()
  {
    this.rvh = new Vector(100);
    this.cursorPos = 0;
    this.absolutePos = 0;
    this.numRows = 0;
    this.numDeleted = 0;
  }
  
  private void initProperties()
    throws SQLException
  {
    setShowDeleted(false);
    setQueryTimeout(0);
    setMaxRows(0);
    setMaxFieldSize(0);
    setType(1004);
    setConcurrency(1008);
    setReadOnly(true);
    setTransactionIsolation(2);
    setEscapeProcessing(true);
    setTypeMap(null);
    checkTransactionalWriter();
    
    this.iMatchColumns = new Vector(10);
    for (int i = 0; i < 10; i++) {
      this.iMatchColumns.add(i, new Integer(-1));
    }
    this.strMatchColumns = new Vector(10);
    for (int j = 0; j < 10; j++) {
      this.strMatchColumns.add(j, null);
    }
  }
  
  private void checkTransactionalWriter()
  {
    if (this.rowSetWriter != null)
    {
      Class c = this.rowSetWriter.getClass();
      if (c != null)
      {
        Class[] theInterfaces = c.getInterfaces();
        for (int i = 0; i < theInterfaces.length; i++) {
          if (theInterfaces[i].getName().indexOf("TransactionalWriter") > 0)
          {
            this.tXWriter = true;
            establishTransactionalWriter();
          }
        }
      }
    }
  }
  
  private void establishTransactionalWriter()
  {
    this.tWriter = ((TransactionalWriter)this.provider.getRowSetWriter());
  }
  
  public void setCommand(String cmd)
    throws SQLException
  {
    super.setCommand(cmd);
    if (!buildTableName(cmd).equals("")) {
      setTableName(buildTableName(cmd));
    }
  }
  
  public void populate(ResultSet data)
    throws SQLException
  {
    Map map = getTypeMap();
    if (data == null) {
      throw new SQLException("Invalid ResultSet object supplied to populate method");
    }
    this.RSMD = data.getMetaData();
    
    this.RowSetMD = new RowSetMetaDataImpl();
    initMetaData(this.RowSetMD, this.RSMD);
    
    this.RSMD = null;
    int numCols = this.RowSetMD.getColumnCount();
    int mRows = getMaxRows();
    int rowsFetched = 0;
    Row currentRow = null;
    while (data.next())
    {
      currentRow = new Row(numCols);
      if ((rowsFetched > mRows) && (mRows > 0)) {
        this.rowsetWarning.setNextWarning(new RowSetWarning("Populating rows setting has exceeded max row setting"));
      }
      for (int i = 1; i <= numCols; i++)
      {
        Object obj;
        Object obj;
        if (map == null)
        {
          Object obj;
          if (data.getMetaData().getColumnClassName(i).compareToIgnoreCase("java.sql.Timestamp") == 0) {
            obj = data.getTimestamp(i);
          } else {
            obj = data.getObject(i);
          }
        }
        else
        {
          obj = data.getObject(i, map);
        }
        if ((obj instanceof Struct)) {
          obj = new SerialStruct((Struct)obj, map);
        } else if ((obj instanceof SQLData)) {
          obj = new SerialStruct((SQLData)obj, map);
        } else if ((obj instanceof Blob)) {
          obj = new SerialBlob((Blob)obj);
        } else if ((obj instanceof Clob)) {
          obj = new SerialClob((Clob)obj);
        } else if ((obj instanceof Array)) {
          obj = new SerialArray((Array)obj, map);
        }
        currentRow.initColumnObject(i, obj);
      }
      rowsFetched++;
      this.rvh.add(currentRow);
    }
    this.numRows = rowsFetched;
    
    notifyRowSetChanged();
  }
  
  private void initMetaData(RowSetMetaDataImpl md, ResultSetMetaData rsmd)
    throws SQLException
  {
    int numCols = rsmd.getColumnCount();
    
    md.setColumnCount(numCols);
    for (int col = 1; col <= numCols; col++)
    {
      md.setAutoIncrement(col, rsmd.isAutoIncrement(col));
      md.setCaseSensitive(col, rsmd.isCaseSensitive(col));
      md.setCurrency(col, rsmd.isCurrency(col));
      md.setNullable(col, rsmd.isNullable(col));
      md.setSigned(col, rsmd.isSigned(col));
      md.setSearchable(col, rsmd.isSearchable(col));
      md.setColumnDisplaySize(col, rsmd.getColumnDisplaySize(col));
      md.setColumnLabel(col, rsmd.getColumnLabel(col));
      md.setColumnName(col, rsmd.getColumnName(col));
      md.setSchemaName(col, rsmd.getSchemaName(col));
      md.setPrecision(col, rsmd.getPrecision(col));
      md.setScale(col, rsmd.getScale(col));
      md.setTableName(col, rsmd.getTableName(col));
      md.setCatalogName(col, rsmd.getCatalogName(col));
      if (rsmd.getColumnClassName(col).compareToIgnoreCase("java.sql.Timestamp") == 0)
      {
        md.setColumnType(col, 93);
        md.setColumnTypeName(col, "TIMESTAMP");
      }
      else
      {
        md.setColumnType(col, rsmd.getColumnType(col));
        md.setColumnTypeName(col, rsmd.getColumnTypeName(col));
      }
      if (this.conn != null) {
        try
        {
          this.dbmslocatorsUpdateCopy = this.conn.getMetaData().locatorsUpdateCopy();
        }
        catch (SQLException localSQLException) {}
      }
    }
  }
  
  public void execute(Connection conn)
    throws SQLException
  {
    setConnection(conn);
    if (getPageSize() != 0)
    {
      this.crsReader = ((CachedRowSetReader)this.provider.getRowSetReader());
      this.crsReader.setStartPosition(1);
      this.callWithCon = true;
      this.crsReader.readData(this);
    }
    else
    {
      this.rowSetReader.readData(this);
    }
    this.RowSetMD = ((RowSetMetaDataImpl)getMetaData());
    if (conn != null)
    {
      try
      {
        this.dbmslocatorsUpdateCopy = conn.getMetaData().locatorsUpdateCopy();
      }
      catch (SQLException localSQLException) {}
    }
    else
    {
      CachedRowSetReader crsTempReader = (CachedRowSetReader)this.rowSetReader;
      Connection tempCon = crsTempReader.connect(this);
      try
      {
        this.dbmslocatorsUpdateCopy = tempCon.getMetaData().locatorsUpdateCopy();
      }
      catch (SQLException localSQLException1) {}
      tempCon = null;
    }
  }
  
  private void setConnection(Connection connection)
  {
    this.conn = connection;
  }
  
  public void acceptChanges()
    throws SyncProviderException
  {
    if (this.onInsertRow) {
      throw new SyncProviderException("Invalid operation while on insert row");
    }
    int saveCursorPos = this.cursorPos;
    boolean success = false;
    boolean conflict = false;
    try
    {
      if (this.rowSetWriter != null)
      {
        saveCursorPos = this.cursorPos;
        conflict = this.rowSetWriter.writeData(this);
        this.cursorPos = saveCursorPos;
      }
      if (this.tXWriter) {
        if (!conflict)
        {
          this.tWriter = ((TransactionalWriter)this.rowSetWriter);
          this.tWriter.rollback();
          success = false;
        }
        else
        {
          this.tWriter = ((TransactionalWriter)this.rowSetWriter);
          this.tWriter.commit();
          success = true;
        }
      }
      if (success) {
        setOriginal();
      } else if (!success) {}
      return;
    }
    catch (SyncProviderException spe)
    {
      throw spe;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      throw new SyncProviderException(e.getMessage());
    }
    catch (SecurityException e)
    {
      throw new SyncProviderException(e.getMessage());
    }
  }
  
  public void acceptChanges(Connection con)
    throws SyncProviderException
  {
    try
    {
      setConnection(con);
      acceptChanges();
    }
    catch (SyncProviderException spe)
    {
      throw spe;
    }
    catch (SQLException sqle)
    {
      throw new SyncProviderException(sqle.getMessage());
    }
  }
  
  public void restoreOriginal()
    throws SQLException
  {
    for (Iterator i = this.rvh.iterator(); i.hasNext();)
    {
      Row currentRow = (Row)i.next();
      if (currentRow.getInserted())
      {
        i.remove();
        this.numRows -= 1;
      }
      else
      {
        if (currentRow.getDeleted()) {
          currentRow.clearDeleted();
        }
        if (currentRow.getUpdated()) {
          currentRow.clearUpdated();
        }
      }
    }
    this.cursorPos = 0;
    
    notifyRowSetChanged();
  }
  
  public void release()
    throws SQLException
  {
    initContainer();
    notifyRowSetChanged();
  }
  
  public void undoDelete()
    throws SQLException
  {
    if (!getShowDeleted()) {
      return;
    }
    checkCursor();
    if (this.onInsertRow) {
      throw new SQLException("Invalid cursor position.");
    }
    Row currentRow = (Row)getCurrentRow();
    if (currentRow.getDeleted())
    {
      currentRow.clearDeleted();
      this.numDeleted -= 1;
      notifyRowChanged();
    }
  }
  
  public void undoInsert()
    throws SQLException
  {
    checkCursor();
    if (this.onInsertRow) {
      throw new SQLException("Invalid cursor position.");
    }
    Row currentRow = (Row)getCurrentRow();
    if (currentRow.getInserted())
    {
      this.rvh.remove(this.cursorPos);
      this.numRows -= 1;
      notifyRowChanged();
    }
    else
    {
      throw new SQLException("Illegal operation on non-inserted row");
    }
  }
  
  public void undoUpdate()
    throws SQLException
  {
    moveToCurrentRow();
    
    undoDelete();
    
    undoInsert();
  }
  
  public RowSet createShared()
    throws SQLException
  {
    try
    {
      clone = (RowSet)clone();
    }
    catch (CloneNotSupportedException ex)
    {
      RowSet clone;
      throw new SQLException(ex.getMessage());
    }
    RowSet clone;
    return clone;
  }
  
  protected Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
  }
  
  public CachedRowSet createCopy()
    throws SQLException
  {
    ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    try
    {
      ObjectOutputStream out = new ObjectOutputStream(bOut);
      out.writeObject(this);
    }
    catch (IOException ex)
    {
      throw new SQLException("Clone failed: " + ex.getMessage());
    }
    try
    {
      ObjectOutputStream out;
      ByteArrayInputStream bIn = new ByteArrayInputStream(bOut.toByteArray());
      in = new ObjectInputStream(bIn);
    }
    catch (StreamCorruptedException ex)
    {
      ObjectInputStream in;
      throw new SQLException("Clone failed: " + ex.getMessage());
    }
    catch (IOException ex)
    {
      throw new SQLException("Clone failed: " + ex.getMessage());
    }
    try
    {
      ObjectInputStream in;
      return (CachedRowSet)in.readObject();
    }
    catch (ClassNotFoundException ex)
    {
      throw new SQLException("Clone failed: " + ex.getMessage());
    }
    catch (OptionalDataException ex)
    {
      throw new SQLException("Clone failed: " + ex.getMessage());
    }
    catch (IOException ex)
    {
      throw new SQLException("Clone failed; " + ex.getMessage());
    }
  }
  
  public CachedRowSet createCopySchema()
    throws SQLException
  {
    int nRows = this.numRows;
    this.numRows = 0;
    
    CachedRowSet crs = createCopy();
    
    this.numRows = nRows;
    
    return crs;
  }
  
  public Collection toCollection()
    throws SQLException
  {
    int count = 0;
    
    int colCount = this.RowSetMD.getColumnCount();
    TreeMap tMap = new TreeMap();
    for (int i = 0; i < this.numRows; i++) {
      tMap.put(new Integer(i), this.rvh.get(i));
    }
    return tMap.values();
  }
  
  public Collection toCollection(int column)
    throws SQLException
  {
    int nRows = this.numRows;
    Vector vec = new Vector(nRows);
    
    CachedRowSetImpl crsTemp = (CachedRowSetImpl)createCopy();
    while (nRows != 0)
    {
      crsTemp.next();
      vec.add(crsTemp.getObject(column));
      nRows--;
    }
    return vec;
  }
  
  public Collection toCollection(String column)
    throws SQLException
  {
    return toCollection(getColIdxByName(column));
  }
  
  public SyncProvider getSyncProvider()
    throws SQLException
  {
    return this.provider;
  }
  
  public void setSyncProvider(String providerStr)
    throws SQLException
  {
    this.provider = 
      SyncFactory.getInstance(providerStr);
    
    this.rowSetReader = this.provider.getRowSetReader();
    this.rowSetWriter = ((TransactionalWriter)this.provider.getRowSetWriter());
  }
  
  public void execute()
    throws SQLException
  {
    execute(null);
  }
  
  public boolean next()
    throws SQLException
  {
    if ((this.cursorPos < 0) || (this.cursorPos >= this.numRows + 1)) {
      throw new SQLException("Invalid Cursor position");
    }
    boolean ret = internalNext();
    notifyCursorMoved();
    
    return ret;
  }
  
  protected boolean internalNext()
    throws SQLException
  {
    boolean ret = false;
    do
    {
      if (this.cursorPos < this.numRows)
      {
        this.cursorPos += 1;
        ret = true;
      }
      else if (this.cursorPos == this.numRows)
      {
        this.cursorPos += 1;
        ret = false;
        break;
      }
    } while ((!getShowDeleted()) && (rowDeleted()));
    if (ret) {
      this.absolutePos += 1;
    } else {
      this.absolutePos = 0;
    }
    return ret;
  }
  
  public void close()
    throws SQLException
  {
    this.cursorPos = 0;
    this.absolutePos = 0;
    this.numRows = 0;
    this.numDeleted = 0;
    
    initProperties();
    
    this.rvh.clear();
  }
  
  public boolean wasNull()
    throws SQLException
  {
    return this.lastValueNull;
  }
  
  private void setLastValueNull(boolean value)
  {
    this.lastValueNull = value;
  }
  
  private void checkIndex(int idx)
    throws SQLException
  {
    if ((idx < 1) || (idx > this.RowSetMD.getColumnCount())) {
      throw new SQLException("Invalid column index");
    }
  }
  
  private void checkCursor()
    throws SQLException
  {
    if ((isAfterLast()) || (isBeforeFirst())) {
      throw new SQLException("Invalid cursor position");
    }
  }
  
  private int getColIdxByName(String name)
    throws SQLException
  {
    int cols = this.RowSetMD.getColumnCount();
    for (int i = 1; i <= cols; i++)
    {
      String colName = this.RowSetMD.getColumnName(i);
      if ((colName != null) && 
        (name.equalsIgnoreCase(colName))) {
        return i;
      }
    }
    throw new SQLException("Invalid column name");
  }
  
  protected BaseRow getCurrentRow()
  {
    if (this.onInsertRow) {
      return this.insertRow;
    }
    return (BaseRow)this.rvh.get(this.cursorPos - 1);
  }
  
  protected void removeCurrentRow()
  {
    ((Row)getCurrentRow()).setDeleted();
    this.rvh.remove(this.cursorPos);
    this.numRows -= 1;
  }
  
  public String getString(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    setLastValueNull(false);
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return null;
    }
    if (this.RowSetMD.getColumnType(columnIndex) == 93) {
      return JAMUtil.getFullDateFormatter().format(value);
    }
    return value.toString();
  }
  
  public boolean getBoolean(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    setLastValueNull(false);
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return false;
    }
    if ((value instanceof Boolean)) {
      return ((Boolean)value).booleanValue();
    }
    try
    {
      Double d = new Double(value.toString());
      if (d.compareTo(new Double(0.0D)) == 0) {
        return false;
      }
      return true;
    }
    catch (NumberFormatException ex)
    {
      throw new SQLException("getBoolen Failed on value (" + 
        value.toString().trim() + 
        ") in column " + columnIndex);
    }
  }
  
  public byte getByte(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    setLastValueNull(false);
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return 0;
    }
    try
    {
      return new Byte(value.toString()).byteValue();
    }
    catch (NumberFormatException ex)
    {
      throw new SQLException("getByte Failed on value (" + 
        value.toString() + ") in column " + 
        columnIndex);
    }
  }
  
  public short getShort(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    setLastValueNull(false);
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return 0;
    }
    try
    {
      return new Short(value.toString().trim()).shortValue();
    }
    catch (NumberFormatException ex)
    {
      throw new SQLException("getShort Failed on value (" + 
        value.toString() + ") in column " + 
        columnIndex);
    }
  }
  
  public int getInt(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    setLastValueNull(false);
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return 0;
    }
    try
    {
      return new Integer(value.toString().trim()).intValue();
    }
    catch (NumberFormatException ex)
    {
      throw new SQLException("getInt Failed on value (" + 
        value.toString() + ") in column " + 
        columnIndex);
    }
  }
  
  public long getLong(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    setLastValueNull(false);
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return 0L;
    }
    try
    {
      return new Long(value.toString().trim()).longValue();
    }
    catch (NumberFormatException ex)
    {
      throw new SQLException("getLong Failed on value (" + 
        value.toString().trim() + 
        ") in column " + columnIndex);
    }
  }
  
  public float getFloat(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    setLastValueNull(false);
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return 0.0F;
    }
    try
    {
      return new Float(value.toString()).floatValue();
    }
    catch (NumberFormatException ex)
    {
      throw new SQLException("getfloat Failed on value (" + 
        value.toString().trim() + 
        ") in column " + columnIndex);
    }
  }
  
  public double getDouble(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    setLastValueNull(false);
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return 0.0D;
    }
    try
    {
      return new Double(value.toString().trim()).doubleValue();
    }
    catch (NumberFormatException ex)
    {
      throw new SQLException("getDouble Failed on value (" + 
        value.toString().trim() + 
        ") in column " + columnIndex);
    }
  }
  
  /**
   * @deprecated
   */
  public BigDecimal getBigDecimal(int columnIndex, int scale)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    setLastValueNull(false);
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return new BigDecimal(0);
    }
    BigDecimal bDecimal = getBigDecimal(columnIndex);
    
    BigDecimal retVal = bDecimal.setScale(scale);
    
    return retVal;
  }
  
  public byte[] getBytes(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    if (!isBinary(this.RowSetMD.getColumnType(columnIndex))) {
      throw new SQLException("Data Type Mismatch");
    }
    return (byte[])getCurrentRow().getColumnObject(columnIndex);
  }
  
  public java.sql.Date getDate(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    setLastValueNull(false);
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return null;
    }
    switch (this.RowSetMD.getColumnType(columnIndex))
    {
    case 91: 
      long sec = ((java.sql.Date)value).getTime();
      return new java.sql.Date(sec);
    case 93: 
      long sec = ((Timestamp)value).getTime();
      return new java.sql.Date(sec);
    case -1: 
    case 1: 
    case 12: 
      try
      {
        DateFormat df = DateFormat.getDateInstance();
        return (java.sql.Date)df.parse(value.toString());
      }
      catch (ParseException ex)
      {
        throw new SQLException("getDate Failed on value (" + 
          value.toString().trim() + 
          ") in column " + columnIndex);
      }
    }
    throw new SQLException("getDate Failed on value (" + 
      value.toString().trim() + 
      ") in column " + columnIndex + 
      "no conversion available");
  }
  
  public java.util.Date getDateTime(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    setLastValueNull(false);
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return null;
    }
    switch (this.RowSetMD.getColumnType(columnIndex))
    {
    case 91: 
      long sec = ((java.util.Date)value).getTime();
      return new java.util.Date(sec);
    case 93: 
      long sec = ((Timestamp)value).getTime();
      return new java.util.Date(sec);
    case -1: 
    case 1: 
    case 12: 
      try
      {
        DateFormat df = DateFormat.getDateInstance();
        return df.parse(value.toString());
      }
      catch (ParseException ex)
      {
        throw new SQLException("getDateTime Failed on value (" + 
          value.toString().trim() + 
          ") in column " + columnIndex);
      }
    }
    throw new SQLException("getDateTime Failed on value (" + 
      value.toString().trim() + 
      ") in column " + columnIndex + 
      "no conversion available");
  }
  
  public java.sql.Date getDateTime(String columnName)
    throws SQLException
  {
    return getDate(getColIdxByName(columnName));
  }
  
  public Time getTime(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    setLastValueNull(false);
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return null;
    }
    switch (this.RowSetMD.getColumnType(columnIndex))
    {
    case 92: 
      return (Time)value;
    case 93: 
      long sec = ((Timestamp)value).getTime();
      return new Time(sec);
    case -1: 
    case 1: 
    case 12: 
      try
      {
        DateFormat tf = DateFormat.getTimeInstance();
        return (Time)tf.parse(value.toString());
      }
      catch (ParseException ex)
      {
        throw new SQLException("getTime Failed on value (" + 
          value.toString().trim() + 
          ") in column " + columnIndex);
      }
    }
    throw new SQLException("getTime Failed on value (" + 
      value.toString().trim() + 
      ") in column " + columnIndex + 
      "no conversion available");
  }
  
  public Timestamp getTimestamp(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    setLastValueNull(false);
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return null;
    }
    switch (this.RowSetMD.getColumnType(columnIndex))
    {
    case 93: 
      return (Timestamp)value;
    case 92: 
      long sec = ((Time)value).getTime();
      return new Timestamp(sec);
    case 91: 
      long sec = ((java.sql.Date)value).getTime();
      return new Timestamp(sec);
    case -1: 
    case 1: 
    case 12: 
      try
      {
        DateFormat tf = DateFormat.getTimeInstance();
        return (Timestamp)tf.parse(value.toString());
      }
      catch (ParseException ex)
      {
        throw new SQLException("getTime Failed on value (" + 
          value.toString().trim() + 
          ") in column " + columnIndex);
      }
    }
    throw new SQLException("getTime Failed on value (" + 
      value.toString().trim() + 
      ") in column " + columnIndex + 
      "no conversion available");
  }
  
  public InputStream getAsciiStream(int columnIndex)
    throws SQLException
  {
    this.asciiStream = null;
    
    checkIndex(columnIndex);
    
    checkCursor();
    
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      this.lastValueNull = true;
      return null;
    }
    try
    {
      if (isString(this.RowSetMD.getColumnType(columnIndex))) {
        this.asciiStream = new ByteArrayInputStream(((String)value).getBytes("ASCII"));
      } else {
        throw new SQLException("Data type mismatch");
      }
    }
    catch (UnsupportedEncodingException ex)
    {
      throw new SQLException(ex.getMessage());
    }
    return this.asciiStream;
  }
  
  /**
   * @deprecated
   */
  public InputStream getUnicodeStream(int columnIndex)
    throws SQLException
  {
    this.unicodeStream = null;
    
    checkIndex(columnIndex);
    
    checkCursor();
    if ((!isBinary(this.RowSetMD.getColumnType(columnIndex))) && 
      (!isString(this.RowSetMD.getColumnType(columnIndex)))) {
      throw new SQLException("Data Type Mismatch");
    }
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      this.lastValueNull = true;
      return null;
    }
    this.unicodeStream = new StringBufferInputStream(value.toString());
    
    return this.unicodeStream;
  }
  
  public InputStream getBinaryStream(int columnIndex)
    throws SQLException
  {
    this.binaryStream = null;
    
    checkIndex(columnIndex);
    
    checkCursor();
    if (!isBinary(this.RowSetMD.getColumnType(columnIndex))) {
      throw new SQLException("Data Type Mismatch");
    }
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      this.lastValueNull = true;
      return null;
    }
    this.binaryStream = new ByteArrayInputStream((byte[])value);
    
    return this.binaryStream;
  }
  
  public String getString(String columnName)
    throws SQLException
  {
    return getString(getColIdxByName(columnName));
  }
  
  public boolean getBoolean(String columnName)
    throws SQLException
  {
    return getBoolean(getColIdxByName(columnName));
  }
  
  public byte getByte(String columnName)
    throws SQLException
  {
    return getByte(getColIdxByName(columnName));
  }
  
  public short getShort(String columnName)
    throws SQLException
  {
    return getShort(getColIdxByName(columnName));
  }
  
  public int getInt(String columnName)
    throws SQLException
  {
    return getInt(getColIdxByName(columnName));
  }
  
  public int getIntCombo(String columnName)
    throws SQLException
  {
    int intValor = getInt(getColIdxByName(columnName));
    if (intValor == 0) {
      return -1;
    }
    return intValor;
  }
  
  public long getLong(String columnName)
    throws SQLException
  {
    return getLong(getColIdxByName(columnName));
  }
  
  public float getFloat(String columnName)
    throws SQLException
  {
    return getFloat(getColIdxByName(columnName));
  }
  
  public double getDouble(String columnName)
    throws SQLException
  {
    return getDouble(getColIdxByName(columnName));
  }
  
  /**
   * @deprecated
   */
  public BigDecimal getBigDecimal(String columnName, int scale)
    throws SQLException
  {
    return getBigDecimal(getColIdxByName(columnName), scale);
  }
  
  public byte[] getBytes(String columnName)
    throws SQLException
  {
    return getBytes(getColIdxByName(columnName));
  }
  
  public java.sql.Date getDate(String columnName)
    throws SQLException
  {
    return getDate(getColIdxByName(columnName));
  }
  
  public Time getTime(String columnName)
    throws SQLException
  {
    return getTime(getColIdxByName(columnName));
  }
  
  public Timestamp getTimestamp(String columnName)
    throws SQLException
  {
    return getTimestamp(getColIdxByName(columnName));
  }
  
  public InputStream getAsciiStream(String columnName)
    throws SQLException
  {
    return getAsciiStream(getColIdxByName(columnName));
  }
  
  /**
   * @deprecated
   */
  public InputStream getUnicodeStream(String columnName)
    throws SQLException
  {
    return getUnicodeStream(getColIdxByName(columnName));
  }
  
  public InputStream getBinaryStream(String columnName)
    throws SQLException
  {
    return getBinaryStream(getColIdxByName(columnName));
  }
  
  public SQLWarning getWarnings()
  {
    return this.sqlwarn;
  }
  
  public void clearWarnings()
  {
    this.sqlwarn = null;
  }
  
  public String getCursorName()
    throws SQLException
  {
    throw new SQLException("Positioned updates not supported");
  }
  
  public ResultSetMetaData getMetaData()
    throws SQLException
  {
    return this.RowSetMD;
  }
  
  public Object getObject(String columnName)
    throws SQLException
  {
    return getObject(getColIdxByName(columnName));
  }
  
  public Object getObject(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    setLastValueNull(false);
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return null;
    }
    if ((value instanceof Struct))
    {
      Struct s = (Struct)value;
      Map map = getTypeMap();
      
      Class c = (Class)map.get(s.getSQLTypeName());
      if (c != null)
      {
        SQLData obj = null;
        try
        {
          obj = (SQLData)c.newInstance();
        }
        catch (InstantiationException ex)
        {
          throw new SQLException("Unable to instantiate: " + 
            ex.getMessage());
        }
        catch (IllegalAccessException ex)
        {
          throw new SQLException("Unable to instantiate: " + 
            ex.getMessage());
        }
        Object[] attribs = s.getAttributes(map);
        
        SQLInputImpl sqlInput = new SQLInputImpl(attribs, map);
        
        obj.readSQL(sqlInput, s.getSQLTypeName());
        return obj;
      }
    }
    return value;
  }
  
  public int findColumn(String columnName)
    throws SQLException
  {
    return getColIdxByName(columnName);
  }
  
  public Reader getCharacterStream(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    if (isBinary(this.RowSetMD.getColumnType(columnIndex)))
    {
      Object value = getCurrentRow().getColumnObject(columnIndex);
      if (value == null)
      {
        this.lastValueNull = true;
        return null;
      }
      this.charStream = new InputStreamReader(
        new ByteArrayInputStream((byte[])value));
    }
    else if (isString(this.RowSetMD.getColumnType(columnIndex)))
    {
      Object value = getCurrentRow().getColumnObject(columnIndex);
      if (value == null)
      {
        this.lastValueNull = true;
        return null;
      }
      this.charStream = new StringReader(value.toString());
    }
    else
    {
      throw new SQLException("Datatye mismatch");
    }
    return this.charStream;
  }
  
  public Reader getCharacterStream(String columnName)
    throws SQLException
  {
    return getCharacterStream(getColIdxByName(columnName));
  }
  
  public BigDecimal getBigDecimal(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    setLastValueNull(false);
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return null;
    }
    try
    {
      return new BigDecimal(value.toString().trim());
    }
    catch (NumberFormatException ex)
    {
      throw new SQLException("getDouble Failed on value (" + 
        value.toString().trim() + 
        ") in column " + columnIndex);
    }
  }
  
  public BigDecimal getBigDecimal(String columnName)
    throws SQLException
  {
    return getBigDecimal(getColIdxByName(columnName));
  }
  
  public int size()
  {
    return this.numRows;
  }
  
  public boolean isBeforeFirst()
    throws SQLException
  {
    if ((this.cursorPos == 0) && (this.numRows > 0)) {
      return true;
    }
    return false;
  }
  
  public boolean isAfterLast()
    throws SQLException
  {
    if ((this.cursorPos == this.numRows + 1) && (this.numRows > 0)) {
      return true;
    }
    return false;
  }
  
  public boolean isFirst()
    throws SQLException
  {
    int saveCursorPos = this.cursorPos;
    int saveAbsoluteCursorPos = this.absolutePos;
    internalFirst();
    if (this.cursorPos == saveCursorPos) {
      return true;
    }
    this.cursorPos = saveCursorPos;
    this.absolutePos = saveAbsoluteCursorPos;
    return false;
  }
  
  public boolean isLast()
    throws SQLException
  {
    int saveCursorPos = this.cursorPos;
    int saveAbsoluteCursorPos = this.absolutePos;
    boolean saveShowDeleted = getShowDeleted();
    setShowDeleted(true);
    internalLast();
    if (this.cursorPos == saveCursorPos)
    {
      setShowDeleted(saveShowDeleted);
      return true;
    }
    setShowDeleted(saveShowDeleted);
    this.cursorPos = saveCursorPos;
    this.absolutePos = saveAbsoluteCursorPos;
    return false;
  }
  
  public void beforeFirst()
    throws SQLException
  {
    if (getType() == 1003) {
      throw new SQLException("beforeFirst: Invalid cursor operation.");
    }
    this.cursorPos = 0;
    this.absolutePos = 0;
    notifyCursorMoved();
  }
  
  public void afterLast()
    throws SQLException
  {
    if (this.numRows > 0)
    {
      this.cursorPos = (this.numRows + 1);
      this.absolutePos = 0;
      notifyCursorMoved();
    }
  }
  
  public boolean first()
    throws SQLException
  {
    if (getType() == 1003) {
      throw new SQLException("First: Invalid cursor operation.");
    }
    boolean ret = internalFirst();
    notifyCursorMoved();
    
    return ret;
  }
  
  protected boolean internalFirst()
    throws SQLException
  {
    boolean ret = false;
    if (this.numRows > 0)
    {
      this.cursorPos = 1;
      if ((!getShowDeleted()) && (rowDeleted())) {
        ret = internalNext();
      } else {
        ret = true;
      }
    }
    if (ret) {
      this.absolutePos = 1;
    } else {
      this.absolutePos = 0;
    }
    return ret;
  }
  
  public boolean last()
    throws SQLException
  {
    if (getType() == 1003) {
      throw new SQLException("last: TYPE_FORWARD_ONLY");
    }
    boolean ret = internalLast();
    notifyCursorMoved();
    
    return ret;
  }
  
  protected boolean internalLast()
    throws SQLException
  {
    boolean ret = false;
    if (this.numRows > 0)
    {
      this.cursorPos = this.numRows;
      if ((!getShowDeleted()) && (rowDeleted())) {
        ret = internalPrevious();
      } else {
        ret = true;
      }
    }
    if (ret) {
      this.absolutePos = (this.numRows - this.numDeleted);
    } else {
      this.absolutePos = 0;
    }
    return ret;
  }
  
  public int getRow()
    throws SQLException
  {
    if ((this.numRows > 0) && 
      (this.cursorPos > 0) && 
      (this.cursorPos < this.numRows + 1) && 
      (!getShowDeleted()) && (!rowDeleted())) {
      return this.absolutePos;
    }
    if (getShowDeleted()) {
      return this.cursorPos;
    }
    return 0;
  }
  
  public boolean absolute(int row)
    throws SQLException
  {
    if ((row == 0) || (getType() == 1003)) {
      throw new SQLException("absolute: Invalid cursor operation.");
    }
    if (row > 0)
    {
      if (row > this.numRows)
      {
        afterLast();
        return false;
      }
      if (this.absolutePos <= 0) {
        internalFirst();
      }
    }
    else
    {
      if (this.cursorPos + row < 0)
      {
        beforeFirst();
        return false;
      }
      if (this.absolutePos >= 0) {
        internalLast();
      }
    }
    while (this.absolutePos != row) {
      if (this.absolutePos < row ? 
        !internalNext() : 
        
        !internalPrevious()) {
        break;
      }
    }
    notifyCursorMoved();
    if ((isAfterLast()) || (isBeforeFirst())) {
      return false;
    }
    return true;
  }
  
  public boolean relative(int rows)
    throws SQLException
  {
    if ((this.numRows == 0) || (isBeforeFirst()) || 
      (isAfterLast()) || (getType() == 1003)) {
      throw new SQLException("relative: Invalid cursor operation");
    }
    if (rows == 0) {
      return true;
    }
    if (rows > 0)
    {
      if (this.cursorPos + rows > this.numRows) {
        afterLast();
      } else {
        for (int i = 0; i < rows; i++) {
          if (!internalNext()) {
            break;
          }
        }
      }
    }
    else if (this.cursorPos + rows < 0) {
      beforeFirst();
    } else {
      for (int i = rows; i < 0; i++) {
        if (!internalPrevious()) {
          break;
        }
      }
    }
    notifyCursorMoved();
    if ((isAfterLast()) || (isBeforeFirst())) {
      return false;
    }
    return true;
  }
  
  public boolean previous()
    throws SQLException
  {
    if (getType() == 1003) {
      throw new SQLException("last: TYPE_FORWARD_ONLY");
    }
    if ((this.cursorPos < 0) || (this.cursorPos > this.numRows + 1)) {
      throw new SQLException("Invalid Cursor position");
    }
    boolean ret = internalPrevious();
    notifyCursorMoved();
    
    return ret;
  }
  
  protected boolean internalPrevious()
    throws SQLException
  {
    boolean ret = false;
    do
    {
      if (this.cursorPos > 1)
      {
        this.cursorPos -= 1;
        ret = true;
      }
      else if (this.cursorPos == 1)
      {
        this.cursorPos -= 1;
        ret = false;
        break;
      }
    } while ((!getShowDeleted()) && (rowDeleted()));
    if (ret) {
      this.absolutePos -= 1;
    } else {
      this.absolutePos = 0;
    }
    return ret;
  }
  
  public boolean rowUpdated()
    throws SQLException
  {
    checkCursor();
    if (this.onInsertRow) {
      throw new SQLException("Operation invalid on insert row");
    }
    return ((Row)getCurrentRow()).getUpdated();
  }
  
  public boolean columnUpdated(int idx)
    throws SQLException
  {
    checkCursor();
    if (this.onInsertRow) {
      throw new SQLException("Operation invalid on insert row");
    }
    return ((Row)getCurrentRow()).getColUpdated(idx - 1);
  }
  
  public boolean columnUpdated(String columnName)
    throws SQLException
  {
    return columnUpdated(getColIdxByName(columnName));
  }
  
  public boolean rowInserted()
    throws SQLException
  {
    checkCursor();
    if (this.onInsertRow) {
      throw new SQLException("Operation invalid on insert row");
    }
    return ((Row)getCurrentRow()).getInserted();
  }
  
  public boolean rowDeleted()
    throws SQLException
  {
    if ((isAfterLast()) || 
      (isBeforeFirst()) || 
      (this.onInsertRow)) {
      throw new SQLException("Invalid cursor position");
    }
    return ((Row)getCurrentRow()).getDeleted();
  }
  
  private boolean isNumeric(int type)
  {
    switch (type)
    {
    case -7: 
    case -6: 
    case -5: 
    case 2: 
    case 3: 
    case 4: 
    case 5: 
    case 6: 
    case 7: 
    case 8: 
      return true;
    }
    return false;
  }
  
  private boolean isString(int type)
  {
    switch (type)
    {
    case -1: 
    case 1: 
    case 12: 
      return true;
    }
    return false;
  }
  
  private boolean isBinary(int type)
  {
    switch (type)
    {
    case -4: 
    case -3: 
    case -2: 
      return true;
    }
    return false;
  }
  
  private boolean isTemporal(int type)
  {
    switch (type)
    {
    case 91: 
    case 92: 
    case 93: 
      return true;
    }
    return false;
  }
  
  private Object convertNumeric(Object srcObj, int srcType, int trgType)
    throws SQLException
  {
    if (srcType == trgType) {
      return srcObj;
    }
    if ((!isNumeric(trgType)) && (!isString(trgType))) {
      throw new SQLException("1.Datatype Mismatch: " + trgType);
    }
    try
    {
      switch (trgType)
      {
      case -7: 
        Integer i = new Integer(srcObj.toString().trim());
        return i.equals(new Integer(0)) ? 
          new Boolean(false) : 
          new Boolean(true);
      case -6: 
        return new Byte(srcObj.toString().trim());
      case 5: 
        return new Short(srcObj.toString().trim());
      case 4: 
        return new Integer(srcObj.toString().trim());
      case -5: 
        return new Long(srcObj.toString().trim());
      case 2: 
      case 3: 
        return new BigDecimal(srcObj.toString().trim());
      case 6: 
      case 7: 
        return new Float(srcObj.toString().trim());
      case 8: 
        return new Double(srcObj.toString().trim());
      case -1: 
      case 1: 
      case 12: 
        return new String(srcObj.toString());
      }
      throw new SQLException("2.Data Type Mismatch: " + trgType);
    }
    catch (NumberFormatException ex)
    {
      throw new SQLException("3.Data Type Mismatch: " + trgType);
    }
  }
  
  private Object convertTemporal(Object srcObj, int srcType, int trgType)
    throws SQLException
  {
    if (srcType == trgType) {
      return srcObj;
    }
    if ((isNumeric(trgType)) || (
      (!isString(trgType)) && (!isTemporal(trgType)))) {
      throw new SQLException("Datatype Mismatch");
    }
    try
    {
      switch (trgType)
      {
      case 91: 
        if (srcType == 93) {
          return new java.sql.Date(((Timestamp)srcObj).getTime());
        }
        throw new SQLException("Data Type Mismatch");
      case 93: 
        if (srcType == 92) {
          return new Timestamp(((Time)srcObj).getTime());
        }
        return new Timestamp(((java.sql.Date)srcObj).getTime());
      case 92: 
        if (srcType == 93) {
          return new Time(((Timestamp)srcObj).getTime());
        }
        throw new SQLException("Data Type Mismatch");
      case -1: 
      case 1: 
      case 12: 
        return new String(srcObj.toString());
      }
      throw new SQLException("Data Type Mismatch");
    }
    catch (NumberFormatException ex)
    {
      throw new SQLException("Data Type Mismatch");
    }
  }
  
  public void updateNull(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    BaseRow row = getCurrentRow();
    row.setColumnObject(columnIndex, null);
  }
  
  public void updateBoolean(int columnIndex, boolean x)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    Object obj = convertNumeric(new Boolean(x), 
      -7, 
      this.RowSetMD.getColumnType(columnIndex));
    
    getCurrentRow().setColumnObject(columnIndex, obj);
  }
  
  public void updateByte(int columnIndex, byte x)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    Object obj = convertNumeric(new Byte(x), 
      -6, 
      this.RowSetMD.getColumnType(columnIndex));
    
    getCurrentRow().setColumnObject(columnIndex, obj);
  }
  
  public void updateShort(int columnIndex, short x)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    Object obj = convertNumeric(new Short(x), 
      5, 
      this.RowSetMD.getColumnType(columnIndex));
    
    getCurrentRow().setColumnObject(columnIndex, obj);
  }
  
  public void updateInt(int columnIndex, int x)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    Object obj = convertNumeric(new Integer(x), 
      4, 
      this.RowSetMD.getColumnType(columnIndex));
    
    getCurrentRow().setColumnObject(columnIndex, obj);
  }
  
  public void updateLong(int columnIndex, long x)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    Object obj = convertNumeric(new Long(x), 
      -5, 
      this.RowSetMD.getColumnType(columnIndex));
    
    getCurrentRow().setColumnObject(columnIndex, obj);
  }
  
  public void updateFloat(int columnIndex, float x)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    Object obj = convertNumeric(new Float(x), 
      7, 
      this.RowSetMD.getColumnType(columnIndex));
    
    getCurrentRow().setColumnObject(columnIndex, obj);
  }
  
  public void updateDouble(int columnIndex, double x)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    Object obj = convertNumeric(new Double(x), 
      8, 
      this.RowSetMD.getColumnType(columnIndex));
    
    getCurrentRow().setColumnObject(columnIndex, obj);
  }
  
  public void updateBigDecimal(int columnIndex, BigDecimal x)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    Object obj = convertNumeric(x, 
      2, 
      this.RowSetMD.getColumnType(columnIndex));
    
    getCurrentRow().setColumnObject(columnIndex, obj);
  }
  
  public void updateString(int columnIndex, String x)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    getCurrentRow().setColumnObject(columnIndex, x);
  }
  
  public void updateBytes(int columnIndex, byte[] x)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    if (!isBinary(this.RowSetMD.getColumnType(columnIndex))) {
      throw new SQLException("Data Type Mismatch");
    }
    getCurrentRow().setColumnObject(columnIndex, x);
  }
  
  public void updateDate(int columnIndex, java.sql.Date x)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    Object obj = convertTemporal(x, 
      91, 
      this.RowSetMD.getColumnType(columnIndex));
    
    getCurrentRow().setColumnObject(columnIndex, obj);
  }
  
  public void updateTime(int columnIndex, Time x)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    Object obj = convertTemporal(x, 
      92, 
      this.RowSetMD.getColumnType(columnIndex));
    
    getCurrentRow().setColumnObject(columnIndex, obj);
  }
  
  public void updateTimestamp(int columnIndex, Timestamp x)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    Object obj = convertTemporal(x, 
      93, 
      this.RowSetMD.getColumnType(columnIndex));
    
    getCurrentRow().setColumnObject(columnIndex, obj);
  }
  
  public void updateDateTime(String columnName, String x)
    throws Exception
  {
    updateDateTime(getColIdxByName(columnName), x);
  }
  
  public void updateDateTime(String columnName, java.util.Date x)
    throws SQLException
  {
    if (x == null) {
      updateNull(getColIdxByName(columnName));
    } else {
      updateDateTime(getColIdxByName(columnName), x);
    }
  }
  
  public void updateDateTime(int columnIndex, java.util.Date y)
    throws SQLException
  {
    Timestamp t = new Timestamp(y.getTime());
    updateTimestamp(columnIndex, t);
  }
  
  public void updateDateTime(int columnIndex, String y)
    throws Exception
  {
    try
    {
      Timestamp t = new Timestamp(JAMUtil.getFullDateFormatter().parse(y).getTime());
      if (JAMUtil.getFullDateFormatter().format(t).compareToIgnoreCase(y) != 0) {
        throw new Exception("La fecha debe estar en el formato : " + JAMUtil.getFullDateFormat());
      }
    }
    catch (Exception e)
    {
      Timestamp t = new Timestamp(JAMUtil.getShortDateFormatter().parse(y).getTime());
      if (JAMUtil.getShortDateFormatter().format(t).compareToIgnoreCase(y) != 0) {
        throw new Exception("La fecha debe estar en el formato : " + JAMUtil.getShortDateFormat());
      }
      updateTimestamp(columnIndex, t);
    }
  }
  
  public void updateAsciiStream(int columnIndex, InputStream x, int length)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    if ((!isString(this.RowSetMD.getColumnType(columnIndex))) && 
      (!isBinary(this.RowSetMD.getColumnType(columnIndex)))) {
      throw new SQLException("Data Type Mismatch");
    }
    byte[] buf = new byte[length];
    try
    {
      int charsRead = 0;
      do
      {
        charsRead += x.read(buf, charsRead, length - charsRead);
      } while (charsRead != length);
    }
    catch (IOException ex)
    {
      throw new SQLException("read failed for aciiStream");
    }
    String str = new String(buf);
    
    getCurrentRow().setColumnObject(columnIndex, str);
  }
  
  public void updateBinaryStream(int columnIndex, InputStream x, int length)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    if (!isBinary(this.RowSetMD.getColumnType(columnIndex))) {
      throw new SQLException("Data Type Mismatch");
    }
    byte[] buf = new byte[length];
    try
    {
      int bytesRead = 0;
      do
      {
        bytesRead += x.read(buf, bytesRead, length - bytesRead);
      } while (bytesRead != -1);
    }
    catch (IOException ex)
    {
      throw new SQLException("read failed for binaryStream");
    }
    getCurrentRow().setColumnObject(columnIndex, buf);
  }
  
  public void updateCharacterStream(int columnIndex, Reader x, int length)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    if ((!isString(this.RowSetMD.getColumnType(columnIndex))) && 
      (!isBinary(this.RowSetMD.getColumnType(columnIndex)))) {
      throw new SQLException("Data Type Mismatch");
    }
    char[] buf = new char[length];
    try
    {
      int charsRead = 0;
      do
      {
        charsRead += x.read(buf, charsRead, length - charsRead);
      } while (charsRead != length);
    }
    catch (IOException ex)
    {
      throw new SQLException("read failed for binaryStream");
    }
    String str = new String(buf);
    
    getCurrentRow().setColumnObject(columnIndex, str);
  }
  
  public void updateObject(int columnIndex, Object x, int scale)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    int type = this.RowSetMD.getColumnType(columnIndex);
    if ((type == 3) || (type == 2)) {
      ((BigDecimal)x).setScale(scale);
    }
    getCurrentRow().setColumnObject(columnIndex, x);
  }
  
  public void updateObject(int columnIndex, Object x)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    getCurrentRow().setColumnObject(columnIndex, x);
  }
  
  public void updateNull(String columnName)
    throws SQLException
  {
    updateNull(getColIdxByName(columnName));
  }
  
  public void updateBoolean(String columnName, boolean x)
    throws SQLException
  {
    updateBoolean(getColIdxByName(columnName), x);
  }
  
  public void updateByte(String columnName, byte x)
    throws SQLException
  {
    updateByte(getColIdxByName(columnName), x);
  }
  
  public void updateShort(String columnName, short x)
    throws SQLException
  {
    updateShort(getColIdxByName(columnName), x);
  }
  
  public void updateInt(String columnName, int x)
    throws SQLException
  {
    updateInt(getColIdxByName(columnName), x);
  }
  
  public void updateIntCombo(String columnName, int x)
    throws SQLException
  {
    if (x <= 0) {
      updateNull(getColIdxByName(columnName));
    } else {
      updateInt(getColIdxByName(columnName), x);
    }
  }
  
  public void updateIntCombo(String columnName, int x, int intdefault)
    throws SQLException
  {
    if (x <= 0) {
      updateInt(getColIdxByName(columnName), intdefault);
    } else {
      updateInt(getColIdxByName(columnName), x);
    }
  }
  
  public void updateLong(String columnName, long x)
    throws SQLException
  {
    updateLong(getColIdxByName(columnName), x);
  }
  
  public void updateFloat(String columnName, float x)
    throws SQLException
  {
    updateFloat(getColIdxByName(columnName), x);
  }
  
  public void updateDouble(String columnName, double x)
    throws SQLException
  {
    updateDouble(getColIdxByName(columnName), x);
  }
  
  public void updateBigDecimal(String columnName, BigDecimal x)
    throws SQLException
  {
    updateBigDecimal(getColIdxByName(columnName), x);
  }
  
  public void updateString(String columnName, String x)
    throws SQLException
  {
    updateString(getColIdxByName(columnName), x);
  }
  
  public void updateStringCombo(String columnName, String x)
    throws SQLException
  {
    if (x == null)
    {
      updateNull(getColIdxByName(columnName));
      return;
    }
    if (x.trim().equalsIgnoreCase("")) {
      updateNull(getColIdxByName(columnName));
    } else {
      updateString(getColIdxByName(columnName), x);
    }
  }
  
  public void updateStringCombo(String columnName, String x, String cdefault)
    throws SQLException
  {
    if (x == null)
    {
      updateString(getColIdxByName(columnName), cdefault);
      return;
    }
    if (x.trim().equalsIgnoreCase("")) {
      updateString(getColIdxByName(columnName), cdefault);
    } else {
      updateString(getColIdxByName(columnName), x);
    }
  }
  
  public void updateBytes(String columnName, byte[] x)
    throws SQLException
  {
    updateBytes(getColIdxByName(columnName), x);
  }
  
  public void updateDate(String columnName, java.sql.Date x)
    throws SQLException
  {
    updateDate(getColIdxByName(columnName), x);
  }
  
  public void updateTime(String columnName, Time x)
    throws SQLException
  {
    updateTime(getColIdxByName(columnName), x);
  }
  
  public void updateTimestamp(String columnName, Timestamp x)
    throws SQLException
  {
    updateTimestamp(getColIdxByName(columnName), x);
  }
  
  public void updateAsciiStream(String columnName, InputStream x, int length)
    throws SQLException
  {
    updateAsciiStream(getColIdxByName(columnName), x, length);
  }
  
  public void updateBinaryStream(String columnName, InputStream x, int length)
    throws SQLException
  {
    updateBinaryStream(getColIdxByName(columnName), x, length);
  }
  
  public void updateCharacterStream(String columnName, Reader reader, int length)
    throws SQLException
  {
    updateCharacterStream(getColIdxByName(columnName), reader, length);
  }
  
  public void updateObject(String columnName, Object x, int scale)
    throws SQLException
  {
    updateObject(getColIdxByName(columnName), x, scale);
  }
  
  public void updateObject(String columnName, Object x)
    throws SQLException
  {
    updateObject(getColIdxByName(columnName), x);
  }
  
  public void insertRow()
    throws SQLException
  {
    if ((!this.onInsertRow) || 
      (!this.insertRow.isCompleteRow(this.RowSetMD))) {
      throw new SQLException("Failed to insert Row");
    }
    Object[] toInsert = getParams();
    for (int i = 0; i < toInsert.length; i++) {
      this.insertRow.setColumnObject(i + 1, toInsert[i]);
    }
    Row insRow = new Row(this.RowSetMD.getColumnCount(), 
      this.insertRow.getOrigRow());
    insRow.setInserted();
    int pos;
    int pos;
    if ((this.currentRow >= this.numRows) || (this.currentRow < 0)) {
      pos = this.numRows;
    } else {
      pos = this.currentRow;
    }
    this.rvh.add(pos, insRow);
    this.numRows += 1;
    
    notifyRowChanged();
  }
  
  public void updateRow()
    throws SQLException
  {
    if (this.onInsertRow) {
      throw new SQLException("updateRow called while on insert row");
    }
    ((Row)getCurrentRow()).setUpdated();
    
    notifyRowChanged();
  }
  
  public void deleteRow()
    throws SQLException
  {
    checkCursor();
    
    ((Row)getCurrentRow()).setDeleted();
    this.numDeleted += 1;
    
    notifyRowChanged();
  }
  
  public void refreshRow()
    throws SQLException
  {
    checkCursor();
    if (this.onInsertRow) {
      throw new SQLException("Invalid cursor position.");
    }
    Row currentRow = (Row)getCurrentRow();
    
    currentRow.clearUpdated();
  }
  
  public void cancelRowUpdates()
    throws SQLException
  {
    checkCursor();
    if (this.onInsertRow) {
      throw new SQLException("Invalid cursor position.");
    }
    Row currentRow = (Row)getCurrentRow();
    if (currentRow.getUpdated())
    {
      currentRow.clearUpdated();
      notifyRowChanged();
    }
  }
  
  public void moveToInsertRow()
    throws SQLException
  {
    if (getConcurrency() == 1007) {
      throw new SQLException("moveToInsertRow: CONCUR_READ_ONLY");
    }
    if (this.insertRow == null)
    {
      if (this.RowSetMD == null) {
        throw new SQLException("moveToInsertRow: no meta data");
      }
      int numCols = this.RowSetMD.getColumnCount();
      if (numCols > 0) {
        this.insertRow = new InsertRow(numCols);
      } else {
        throw new SQLException("moveToInsertRow: invalid number of columns");
      }
    }
    this.onInsertRow = true;
    
    this.currentRow = this.cursorPos;
    this.cursorPos = -1;
    
    this.insertRow.initInsertRow();
  }
  
  public void moveToCurrentRow()
    throws SQLException
  {
    if (!this.onInsertRow) {
      return;
    }
    this.cursorPos = this.currentRow;
    this.onInsertRow = false;
  }
  
  public Statement getStatement()
    throws SQLException
  {
    return null;
  }
  
  public Object getObject(int columnIndex, Map map)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    setLastValueNull(false);
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return null;
    }
    if ((value instanceof Struct))
    {
      Struct s = (Struct)value;
      
      Class c = (Class)map.get(s.getSQLTypeName());
      if (c != null)
      {
        SQLData obj = null;
        try
        {
          obj = (SQLData)c.newInstance();
        }
        catch (InstantiationException ex)
        {
          throw new SQLException("Unable to instantiate: " + 
            ex.getMessage());
        }
        catch (IllegalAccessException ex)
        {
          throw new SQLException("Unable to instantiate: " + 
            ex.getMessage());
        }
        Object[] attribs = s.getAttributes(map);
        
        SQLInputImpl sqlInput = new SQLInputImpl(attribs, map);
        
        obj.readSQL(sqlInput, s.getSQLTypeName());
        return obj;
      }
    }
    return value;
  }
  
  public Ref getRef(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    if (this.RowSetMD.getColumnType(columnIndex) != 2006) {
      throw new SQLException("Datatype Mismatch");
    }
    setLastValueNull(false);
    Ref value = (Ref)getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return null;
    }
    return value;
  }
  
  public Blob getBlob(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    if (this.RowSetMD.getColumnType(columnIndex) != 2004) {
      throw new SQLException("Datatype Mismatch");
    }
    setLastValueNull(false);
    Blob value = (Blob)getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return null;
    }
    return value;
  }
  
  public Clob getClob(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    if (this.RowSetMD.getColumnType(columnIndex) != 2005)
    {
      System.out.println("Type is: " + this.RowSetMD.getColumnType(columnIndex));
      throw new SQLException("Datatype Mismatch");
    }
    setLastValueNull(false);
    Clob value = (Clob)getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return null;
    }
    return value;
  }
  
  public Array getArray(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    if (this.RowSetMD.getColumnType(columnIndex) != 2003) {
      throw new SQLException("Datatype Mismatch");
    }
    setLastValueNull(false);
    Array value = (Array)getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return null;
    }
    return value;
  }
  
  public Object getObject(String columnName, Map map)
    throws SQLException
  {
    return getObject(getColIdxByName(columnName), map);
  }
  
  public Ref getRef(String colName)
    throws SQLException
  {
    return getRef(getColIdxByName(colName));
  }
  
  public Blob getBlob(String colName)
    throws SQLException
  {
    return getBlob(getColIdxByName(colName));
  }
  
  public Clob getClob(String colName)
    throws SQLException
  {
    return getClob(getColIdxByName(colName));
  }
  
  public Array getArray(String colName)
    throws SQLException
  {
    return getArray(getColIdxByName(colName));
  }
  
  public java.sql.Date getDate(int columnIndex, Calendar cal)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    setLastValueNull(false);
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return null;
    }
    value = convertTemporal(value, 
      this.RowSetMD.getColumnType(columnIndex), 
      91);
    
    Calendar defaultCal = Calendar.getInstance();
    
    defaultCal.setTime((java.util.Date)value);
    
    cal.set(1, defaultCal.get(1));
    cal.set(2, defaultCal.get(2));
    cal.set(5, defaultCal.get(5));
    
    return new java.sql.Date(cal.getTime().getTime());
  }
  
  public java.sql.Date getDate(String columnName, Calendar cal)
    throws SQLException
  {
    return getDate(getColIdxByName(columnName), cal);
  }
  
  public Time getTime(int columnIndex, Calendar cal)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    setLastValueNull(false);
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return null;
    }
    value = convertTemporal(value, 
      this.RowSetMD.getColumnType(columnIndex), 
      92);
    
    Calendar defaultCal = Calendar.getInstance();
    
    defaultCal.setTime((java.util.Date)value);
    
    cal.set(11, defaultCal.get(11));
    cal.set(12, defaultCal.get(12));
    cal.set(13, defaultCal.get(13));
    
    return new Time(cal.getTime().getTime());
  }
  
  public Time getTime(String columnName, Calendar cal)
    throws SQLException
  {
    return getTime(getColIdxByName(columnName), cal);
  }
  
  public Timestamp getTimestamp(int columnIndex, Calendar cal)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    setLastValueNull(false);
    Object value = getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return null;
    }
    value = convertTemporal(value, 
      this.RowSetMD.getColumnType(columnIndex), 
      93);
    
    Calendar defaultCal = Calendar.getInstance();
    
    defaultCal.setTime((java.util.Date)value);
    
    cal.set(1, defaultCal.get(1));
    cal.set(2, defaultCal.get(2));
    cal.set(5, defaultCal.get(5));
    cal.set(11, defaultCal.get(11));
    cal.set(12, defaultCal.get(12));
    cal.set(13, defaultCal.get(13));
    
    return new Timestamp(cal.getTime().getTime());
  }
  
  public Timestamp getTimestamp(String columnName, Calendar cal)
    throws SQLException
  {
    return getTimestamp(getColIdxByName(columnName), cal);
  }
  
  public Connection getConnection()
    throws SQLException
  {
    return this.conn;
  }
  
  public void setMetaData(RowSetMetaData md)
    throws SQLException
  {
    this.RowSetMD = ((RowSetMetaDataImpl)md);
  }
  
  public ResultSet getOriginal()
    throws SQLException
  {
    JAMRowSet crs = new JAMRowSet();
    crs.RowSetMD = this.RowSetMD;
    crs.numRows = this.numRows;
    crs.cursorPos = 0;
    
    int colCount = this.RowSetMD.getColumnCount();
    for (Iterator i = this.rvh.iterator(); i.hasNext();)
    {
      Row orig = new Row(colCount, ((Row)i.next()).getOrigRow());
      crs.rvh.add(orig);
    }
    return crs;
  }
  
  public ResultSet getOriginalRow()
    throws SQLException
  {
    JAMRowSet crs = new JAMRowSet();
    crs.RowSetMD = this.RowSetMD;
    crs.numRows = 1;
    crs.cursorPos = 0;
    
    Row orig = new Row(this.RowSetMD.getColumnCount(), 
      getCurrentRow().getOrigRow());
    
    crs.rvh.add(orig);
    
    return crs;
  }
  
  public void setOriginalRow()
    throws SQLException
  {
    if (this.onInsertRow) {
      throw new SQLException("Invalid operation on Insert Row");
    }
    Row row = (Row)getCurrentRow();
    makeRowOriginal(row);
    if (row.getDeleted())
    {
      removeCurrentRow();
      this.numRows -= 1;
    }
  }
  
  private void makeRowOriginal(Row row)
  {
    if (row.getInserted()) {
      row.clearInserted();
    }
    if (row.getUpdated()) {
      row.moveCurrentToOrig();
    }
  }
  
  public void setOriginal()
    throws SQLException
  {
    for (Iterator i = this.rvh.iterator(); i.hasNext();)
    {
      Row row = (Row)i.next();
      makeRowOriginal(row);
      if (row.getDeleted())
      {
        i.remove();
        this.numRows -= 1;
      }
    }
    this.numDeleted = 0;
    
    notifyRowSetChanged();
  }
  
  public String getTableName()
    throws SQLException
  {
    return this.tableName;
  }
  
  public void setTableName(String tabName)
    throws SQLException
  {
    if (tabName == null) {
      throw new SQLException("TableName cannot be null");
    }
    this.tableName = new String(tabName);
  }
  
  public int[] getKeyColumns()
    throws SQLException
  {
    return this.keyCols;
  }
  
  public void setKeyColumns(int[] keys)
    throws SQLException
  {
    int numCols = 0;
    if (this.RowSetMD != null)
    {
      numCols = this.RowSetMD.getColumnCount();
      if (keys.length > numCols) {
        throw new SQLException("Invalid key columns");
      }
    }
    this.keyCols = new int[keys.length];
    for (int i = 0; i < keys.length; i++)
    {
      if ((this.RowSetMD != null) && ((keys[i] <= 0) || 
        (keys[i] > numCols))) {
        throw new SQLException("Invalid column index: " + 
          keys[i]);
      }
      this.keyCols[i] = keys[i];
    }
  }
  
  public void updateRef(int columnIndex, Ref ref)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    getCurrentRow().setColumnObject(columnIndex, new SerialRef(ref));
  }
  
  public void updateRef(String columnName, Ref ref)
    throws SQLException
  {
    updateRef(getColIdxByName(columnName), ref);
  }
  
  public void updateClob(int columnIndex, Clob c)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    if (this.dbmslocatorsUpdateCopy) {
      getCurrentRow().setColumnObject(columnIndex, new SerialClob(c));
    } else {
      throw new SQLException("Operation not supported by database");
    }
  }
  
  public void updateClob(String columnName, Clob c)
    throws SQLException
  {
    updateClob(getColIdxByName(columnName), c);
  }
  
  public void updateBlob(int columnIndex, Blob b)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    if (this.dbmslocatorsUpdateCopy) {
      getCurrentRow().setColumnObject(columnIndex, new SerialBlob(b));
    } else {
      throw new SQLException("Operation not supported by database");
    }
  }
  
  public void updateBlob(String columnName, Blob b)
    throws SQLException
  {
    updateBlob(getColIdxByName(columnName), b);
  }
  
  public void updateArray(int columnIndex, Array a)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    
    getCurrentRow().setColumnObject(columnIndex, new SerialArray(a));
  }
  
  public void updateArray(String columnName, Array a)
    throws SQLException
  {
    updateArray(getColIdxByName(columnName), a);
  }
  
  public URL getURL(int columnIndex)
    throws SQLException
  {
    checkIndex(columnIndex);
    
    checkCursor();
    if (this.RowSetMD.getColumnType(columnIndex) != 70) {
      throw new SQLException("Datatype Mismatch");
    }
    setLastValueNull(false);
    URL value = (URL)getCurrentRow().getColumnObject(columnIndex);
    if (value == null)
    {
      setLastValueNull(true);
      return null;
    }
    return value;
  }
  
  public URL getURL(String columnName)
    throws SQLException
  {
    return getURL(getColIdxByName(columnName));
  }
  
  public RowSetWarning getRowSetWarnings()
  {
    try
    {
      notifyCursorMoved();
    }
    catch (SQLException localSQLException) {}
    return this.rowsetWarning;
  }
  
  private String buildTableName(String command)
    throws SQLException
  {
    String strTablename = "";
    command = command.trim();
    if (command.toLowerCase().startsWith("select"))
    {
      int indexFrom = command.toLowerCase().indexOf("from");
      int indexComma = command.indexOf(",", indexFrom);
      if (indexComma == -1)
      {
        strTablename = command.substring(indexFrom + "from".length(), command.length()).trim();
        
        String tabName = strTablename;
        
        int idxWhere = tabName.toLowerCase().indexOf(" where");
        if (idxWhere != -1) {
          tabName = tabName.substring(0, idxWhere).trim();
        }
        strTablename = tabName;
      }
    }
    else if (!command.toLowerCase().startsWith("insert"))
    {
      command.toLowerCase().startsWith("update");
    }
    return strTablename;
  }
  
  public void commit()
    throws SQLException
  {
    this.conn.commit();
  }
  
  public void rollback()
    throws SQLException
  {
    this.conn.rollback();
  }
  
  public void rollback(Savepoint s)
    throws SQLException
  {
    this.conn.rollback(s);
  }
  
  public void unsetMatchColumn(int[] columnIdxes)
    throws SQLException
  {
    for (int j = 0; j < columnIdxes.length; j++)
    {
      int i_val = Integer.parseInt(this.iMatchColumns.get(j).toString());
      if (columnIdxes[j] != i_val) {
        throw new SQLException("Match Columns are not the same as those set");
      }
    }
    for (int i = 0; i < columnIdxes.length; i++) {
      this.iMatchColumns.set(i, new Integer(-1));
    }
  }
  
  public void unsetMatchColumn(String[] columnIdxes)
    throws SQLException
  {
    for (int j = 0; j < columnIdxes.length; j++) {
      if (!columnIdxes[j].equals(this.strMatchColumns.get(j))) {
        throw new SQLException("Match Columns are not the same that were set");
      }
    }
    for (int i = 0; i < columnIdxes.length; i++) {
      this.strMatchColumns.set(i, null);
    }
  }
  
  public String[] getMatchColumnNames()
    throws SQLException
  {
    String[] str_temp = new String[this.strMatchColumns.size()];
    if (this.strMatchColumns.get(0) == null) {
      throw new SQLException("Set match Columns before getting them");
    }
    this.strMatchColumns.copyInto(str_temp);
    return str_temp;
  }
  
  public int[] getMatchColumnIndexes()
    throws SQLException
  {
    Integer[] int_temp = new Integer[this.iMatchColumns.size()];
    int[] i_temp = new int[this.iMatchColumns.size()];
    
    int i_val = ((Integer)this.iMatchColumns.get(0)).intValue();
    if (i_val == -1) {
      throw new SQLException("Set the match columns before getting them");
    }
    this.iMatchColumns.copyInto(int_temp);
    for (int i = 0; i < int_temp.length; i++) {
      i_temp[i] = int_temp[i].intValue();
    }
    return i_temp;
  }
  
  public void setMatchColumn(int[] columnIdxes)
    throws SQLException
  {
    for (int j = 0; j < columnIdxes.length; j++) {
      if (columnIdxes[j] < 0) {
        throw new SQLException("Match Column should be greater than 0");
      }
    }
    for (int i = 0; i < columnIdxes.length; i++) {
      this.iMatchColumns.add(i, new Integer(columnIdxes[i]));
    }
  }
  
  public void setMatchColumn(String[] columnNames)
    throws SQLException
  {
    for (int j = 0; j < columnNames.length; j++) {
      if ((columnNames[j] == null) || (columnNames[j].equals(""))) {
        throw new SQLException("Match Column cannot be null or empty string");
      }
    }
    for (int i = 0; i < columnNames.length; i++) {
      this.strMatchColumns.add(i, columnNames[i]);
    }
  }
  
  public void setMatchColumn(int columnIdx)
    throws SQLException
  {
    if (columnIdx < 0) {
      throw new SQLException("Column ID has to be >0");
    }
    this.iMatchColumns.set(0, new Integer(columnIdx));
  }
  
  public void setMatchColumn(String columnName)
    throws SQLException
  {
    columnName = columnName.trim();
    if ((columnName == "") || (columnName.equals(null))) {
      throw new SQLException("Column ID has to be a non null value");
    }
    this.strMatchColumns.set(0, columnName);
  }
  
  public void unsetMatchColumn(int columnIdx)
    throws SQLException
  {
    if (!this.iMatchColumns.get(0).equals(new Integer(columnIdx))) {
      throw new SQLException("Column being unset is not same as set");
    }
    if (this.strMatchColumns.get(0) != null) {
      throw new SQLException("Use column name as argument to unsetMatchColumn");
    }
    this.iMatchColumns.set(0, new Integer(-1));
  }
  
  public void unsetMatchColumn(String columnName)
    throws SQLException
  {
    columnName = columnName.trim();
    if (!this.strMatchColumns.get(0).equals(columnName)) {
      throw new SQLException("Column being unset is not same as set");
    }
    if (((Integer)this.iMatchColumns.get(0)).intValue() > 0) {
      throw new SQLException("Use column id as argument to unsetMatchColumn");
    }
    this.strMatchColumns.set(0, null);
  }
  
  public void rowSetPopulated(RowSetEvent event, int numRows)
    throws SQLException
  {
    if ((numRows < 0) || (numRows < getFetchSize())) {
      throw new SQLException("Number of rows is less than zero or less than fetchSize");
    }
    if (size() % numRows == 0)
    {
      RowSetEvent event_temp = new RowSetEvent(this);
      event = event_temp;
      notifyRowSetChanged();
    }
  }
  
  public void populate(ResultSet data, int start)
    throws SQLException
  {
    Map map = getTypeMap();
    
    this.cursorPos = 0;
    if (this.populatecallcount == 0)
    {
      if (start < 0) {
        throw new SQLException("Start posiiton cannot be negative");
      }
      if (getMaxRows() == 0)
      {
        data.absolute(start);
        while (data.next()) {
          this.totalRows += 1;
        }
        this.totalRows += 1;
      }
      this.startPos = start;
    }
    this.populatecallcount += 1;
    this.resultSet = data;
    if ((this.endPos - this.startPos >= getMaxRows()) && (getMaxRows() > 0))
    {
      this.endPos = this.prevEndPos;
      this.pagenotend = false;
      return;
    }
    if (((this.maxRowsreached != getMaxRows()) || (this.maxRowsreached != this.totalRows)) && (this.pagenotend)) {
      this.startPrev = (start - getPageSize());
    }
    if (this.pageSize == 0)
    {
      this.prevEndPos = this.endPos;
      this.endPos = (start + getMaxRows());
    }
    else
    {
      this.prevEndPos = this.endPos;
      this.endPos = (start + getPageSize());
    }
    if (start == 1) {
      this.resultSet.beforeFirst();
    } else {
      this.resultSet.absolute(start - 1);
    }
    if (this.pageSize == 0) {
      this.rvh = new Vector(getMaxRows());
    } else {
      this.rvh = new Vector(getPageSize());
    }
    if (data == null) {
      throw new SQLException("Invalid ResultSet object supplied to populate method");
    }
    this.RSMD = data.getMetaData();
    
    this.RowSetMD = new RowSetMetaDataImpl();
    initMetaData(this.RowSetMD, this.RSMD);
    
    this.RSMD = null;
    int numCols = this.RowSetMD.getColumnCount();
    int mRows = getMaxRows();
    int rowsFetched = 0;
    Row currentRow = null;
    if ((!data.next()) && (mRows == 0))
    {
      this.endPos = this.prevEndPos;
      this.pagenotend = false;
      return;
    }
    data.previous();
    while (data.next())
    {
      currentRow = new Row(numCols);
      if (this.pageSize == 0)
      {
        if ((rowsFetched >= mRows) && (mRows > 0))
        {
          this.rowsetWarning.setNextException(new SQLException("Populating rows setting has exceeded max row setting"));
          
          break;
        }
      }
      else if ((rowsFetched >= this.pageSize) || ((this.maxRowsreached >= mRows) && (mRows > 0)))
      {
        this.rowsetWarning.setNextException(new SQLException("Populating rows setting has exceeded max row setting"));
        
        break;
      }
      for (int i = 1; i <= numCols; i++)
      {
        Object obj;
        Object obj;
        if (map == null) {
          obj = data.getObject(i);
        } else {
          obj = data.getObject(i, map);
        }
        if ((obj instanceof Struct)) {
          obj = new SerialStruct((Struct)obj, map);
        } else if ((obj instanceof SQLData)) {
          obj = new SerialStruct((SQLData)obj, map);
        } else if ((obj instanceof Blob)) {
          obj = new SerialBlob((Blob)obj);
        } else if ((obj instanceof Clob)) {
          obj = new SerialClob((Clob)obj);
        } else if ((obj instanceof Array)) {
          obj = new SerialArray((Array)obj, map);
        }
        currentRow.initColumnObject(i, obj);
      }
      rowsFetched++;
      this.maxRowsreached += 1;
      this.rvh.add(currentRow);
    }
    this.numRows = rowsFetched;
    
    notifyRowSetChanged();
  }
  
  public boolean nextPage()
    throws SQLException
  {
    if (this.populatecallcount == 0) {
      throw new SQLException("Populate the data before calling nextPage");
    }
    if (this.populatecallcount == 1)
    {
      this.populatecallcount += 1;
      return this.pagenotend;
    }
    this.onFirstPage = false;
    if (this.callWithCon)
    {
      this.crsReader.setStartPosition(this.endPos);
      this.crsReader.readData(this);
      this.resultSet = null;
    }
    else
    {
      populate(this.resultSet, this.endPos);
    }
    return this.pagenotend;
  }
  
  public void setPageSize(int size)
    throws SQLException
  {
    if (size < 0) {
      throw new SQLException("Page size cannot be less than zero");
    }
    if ((size > getMaxRows()) && (getMaxRows() != 0)) {
      throw new SQLException("Page size cannot be greater than Max Rows");
    }
    this.pageSize = size;
  }
  
  public int getPageSize()
  {
    return this.pageSize;
  }
  
  public boolean previousPage()
    throws SQLException
  {
    int pS = getPageSize();
    int mR = this.maxRowsreached;
    if (this.populatecallcount == 0) {
      throw new SQLException("Populate the data before calling");
    }
    if ((!this.callWithCon) && 
      (this.resultSet.getType() == 1003)) {
      throw new SQLException("ResultSet is forward only");
    }
    this.pagenotend = true;
    if (this.startPrev < this.startPos)
    {
      this.onFirstPage = true;
      return false;
    }
    if (this.onFirstPage) {
      return false;
    }
    int rem = mR % pS;
    if (rem == 0)
    {
      this.maxRowsreached -= 2 * pS;
      if (this.callWithCon)
      {
        this.crsReader.setStartPosition(this.startPrev);
        this.crsReader.readData(this);
        this.resultSet = null;
      }
      else
      {
        populate(this.resultSet, this.startPrev);
      }
      return true;
    }
    this.maxRowsreached -= pS + rem;
    if (this.callWithCon)
    {
      this.crsReader.setStartPosition(this.startPrev);
      this.crsReader.readData(this);
      this.resultSet = null;
    }
    else
    {
      populate(this.resultSet, this.startPrev);
    }
    return true;
  }
  
  public boolean insertPending()
  {
    return this.onInsertRow;
  }
  
  public boolean absolutePage(int page)
    throws SQLException
  {
    boolean isAbs = true;boolean retVal = true;
    if (page <= 0) {
      throw new SQLException("Absolute positoin is invalid");
    }
    int counter = 0;
    
    firstPage();
    counter++;
    while ((counter < page) && (isAbs))
    {
      isAbs = nextPage();
      counter++;
    }
    if ((!isAbs) && (counter < page)) {
      retVal = false;
    } else if (counter == page) {
      retVal = true;
    }
    return retVal;
  }
  
  public boolean relativePage(int page)
    throws SQLException
  {
    boolean isRel = true;boolean retVal = true;
    if (page > 0)
    {
      int counter = 0;
      while ((counter < page) && (isRel))
      {
        isRel = nextPage();
        counter++;
      }
      if ((!isRel) && (counter < page)) {
        retVal = false;
      } else if (counter == page) {
        retVal = true;
      }
      return retVal;
    }
    int counter = page;
    isRel = true;
    while ((counter < 0) && (isRel))
    {
      isRel = previousPage();
      counter++;
    }
    if ((!isRel) && (counter < 0)) {
      retVal = false;
    } else if (counter == 0) {
      retVal = true;
    }
    return retVal;
  }
  
  public boolean firstPage()
    throws SQLException
  {
    if (this.populatecallcount == 0) {
      throw new SQLException("Populate the data before calling ");
    }
    if ((!this.callWithCon) && 
      (this.resultSet.getType() == 1003)) {
      throw new SQLException("Result of type forward only");
    }
    this.endPos = 0;
    this.maxRowsreached = 0;
    this.pagenotend = true;
    if (this.callWithCon)
    {
      this.crsReader.setStartPosition(this.startPos);
      this.crsReader.readData(this);
      this.resultSet = null;
    }
    else
    {
      populate(this.resultSet, this.startPos);
    }
    this.onFirstPage = true;
    return this.onFirstPage;
  }
  
  public boolean lastPage()
    throws SQLException
  {
    int pS = getPageSize();
    int mR = getMaxRows();
    if (pS == 0)
    {
      this.onLastPage = true;
      return this.onLastPage;
    }
    if (getMaxRows() == 0) {
      mR = this.totalRows;
    }
    if (this.populatecallcount == 0) {
      throw new SQLException("Populate the data before calling ");
    }
    this.onFirstPage = false;
    if (mR % pS == 0)
    {
      int quo = mR / pS;
      int start = this.startPos + pS * (quo - 1);
      this.maxRowsreached = (mR - pS);
      if (this.callWithCon)
      {
        this.crsReader.setStartPosition(start);
        this.crsReader.readData(this);
        this.resultSet = null;
      }
      else
      {
        populate(this.resultSet, start);
      }
      this.onLastPage = true;
      return this.onLastPage;
    }
    int quo = mR / pS;
    int rem = mR % pS;
    int start = this.startPos + pS * quo;
    this.maxRowsreached = (mR - rem);
    if (this.callWithCon)
    {
      this.crsReader.setStartPosition(start);
      this.crsReader.readData(this);
      this.resultSet = null;
    }
    else
    {
      populate(this.resultSet, start);
    }
    this.onLastPage = true;
    return this.onLastPage;
  }
}
