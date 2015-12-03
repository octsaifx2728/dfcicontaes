package mx.com.jammexico.jamdrivers;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAMLeeXLS
{
  private String cArchivo = null;
  private String[] arrNomCampos = null;
  private int intRowIni = 0;
  private int intCampoDescri = 0;
  private Workbook workbook = null;
  private Sheet sheet = null;
  private int intHojaActiva = 0;
  
  public JAMLeeXLS(String cArgArchivo)
  {
    this.cArchivo = cArgArchivo;
    initialize();
  }
  
  public JAMLeeXLS(String cArgArchivo, int intRow, int argCampoDescri)
  {
    this.cArchivo = cArgArchivo;
    this.intRowIni = intRow;
    this.intCampoDescri = argCampoDescri;
    initialize();
  }
  
  private void initialize()
  {
    defineNombresCampos();
  }
  
  public int getHojas()
  {
    return this.workbook.getSheets().length;
  }
  
  private void defineNombresCampos()
  {
    try
    {
      this.workbook = Workbook.getWorkbook(new File(this.cArchivo));
      
      this.sheet = this.workbook.getSheet(this.intHojaActiva);
      
      int intColumns = this.sheet.getColumns();
      this.arrNomCampos = new String[intColumns];
      for (int i = 0; i < intColumns; i++) {
        if (this.intRowIni == 3)
        {
          this.arrNomCampos[i] = rstHardCodeParaUnArchivoEspecial(i);
        }
        else
        {
          Cell a1 = this.sheet.getCell(i, this.intCampoDescri);
          String cCampoNombre = "";
          if ((a1.getType() != CellType.EMPTY) && (a1.getType() == CellType.LABEL))
          {
            LabelCell lc1 = (LabelCell)a1;
            cCampoNombre = lc1.getString().trim().toUpperCase();
          }
          if (cCampoNombre.trim().equalsIgnoreCase("")) {
            cCampoNombre = "CAMPO_NRO_" + i;
          }
          this.arrNomCampos[i] = cCampoNombre.trim();
        }
      }
    }
    catch (BiffException exx)
    {
      System.out.print(exx.getMessage());
    }
    catch (IOException exx)
    {
      System.out.print(exx.getMessage());
    }
  }
  
  public Integer JAMGetInt(String argCampo, int argRow)
  {
    return JAMGetInt(JAMUtil.JAMFindArray(this.arrNomCampos, argCampo), argRow);
  }
  
  public Integer JAMGetInt(int argCampo, int argRow)
  {
    Integer reto = null;
    try
    {
      if (argCampo != -1)
      {
        Cell aCampo = this.sheet.getCell(argCampo, argRow + this.intRowIni);
        if (aCampo.getType() == CellType.NUMBER)
        {
          NumberCell nc = (NumberCell)aCampo;
          return new Integer(new Double(nc.getValue()).intValue());
        }
      }
    }
    catch (Exception localException) {}
    return reto;
  }
  
  public Long JAMGetLong(String argCampo, int argRow)
  {
    return JAMGetLong(JAMUtil.JAMFindArray(this.arrNomCampos, argCampo), argRow);
  }
  
  public Long JAMGetLong(int argCampo, int argRow)
  {
    Long reto = null;
    try
    {
      if (argCampo != -1)
      {
        Cell aCampo = this.sheet.getCell(argCampo, argRow + this.intRowIni);
        if (aCampo.getType() == CellType.NUMBER)
        {
          NumberCell nc = (NumberCell)aCampo;
          return new Long(new Double(nc.getValue()).longValue());
        }
      }
    }
    catch (Exception localException) {}
    return reto;
  }
  
  public Double JAMGetDouble(String argCampo, int argRow)
  {
    return JAMGetDouble(JAMUtil.JAMFindArray(this.arrNomCampos, argCampo), argRow);
  }
  
  public Double JAMGetDouble(int argCampo, int argRow)
  {
    Double reto = null;
    try
    {
      if (argCampo != -1)
      {
        Cell aCampo = this.sheet.getCell(argCampo, argRow + this.intRowIni);
        if (aCampo.getType() == CellType.NUMBER)
        {
          NumberCell nc = (NumberCell)aCampo;
          return new Double(nc.getValue());
        }
      }
    }
    catch (Exception localException) {}
    return reto;
  }
  
  public String JAMGetString(String argCampo, int argRow)
  {
    return JAMGetString(JAMUtil.JAMFindArray(this.arrNomCampos, argCampo), argRow);
  }
  
  public String JAMGetString(int argCampo, int argRow)
  {
    String reto = null;
    try
    {
      if (argCampo != -1)
      {
        Cell aCampo = this.sheet.getCell(argCampo, argRow + this.intRowIni);
        if (aCampo.getType() == CellType.LABEL)
        {
          LabelCell nc = (LabelCell)aCampo;
          return nc.getString();
        }
      }
    }
    catch (Exception localException) {}
    return reto;
  }
  
  public Date JAMGetDate(String argCampo, int argRow)
  {
    return JAMGetDate(JAMUtil.JAMFindArray(this.arrNomCampos, argCampo), argRow);
  }
  
  public Date JAMGetDate(int argCampo, int argRow)
  {
    Date reto = null;
    try
    {
      if (argCampo != -1)
      {
        Cell aCampo = this.sheet.getCell(argCampo, argRow + this.intRowIni);
        if (aCampo.getType() == CellType.DATE)
        {
          DateCell nc = (DateCell)aCampo;
          return nc.getDate();
        }
      }
    }
    catch (Exception localException) {}
    return reto;
  }
  
  public int JAMGetRows()
  {
    return this.sheet.getRows() - this.intRowIni;
  }
  
  private String rstHardCodeParaUnArchivoEspecial(int i)
  {
    Cell a1 = this.sheet.getCell(i, 1);
    Cell a2 = this.sheet.getCell(i, 2);
    
    String ca1 = "";
    String ca2 = "";
    if ((a1.getType() != CellType.EMPTY) && (a1.getType() == CellType.LABEL))
    {
      LabelCell lc1 = (LabelCell)a1;
      ca1 = lc1.getString().trim().toUpperCase();
    }
    if ((a2.getType() != CellType.EMPTY) && (a2.getType() == CellType.LABEL))
    {
      LabelCell lc2 = (LabelCell)a2;
      ca2 = lc2.getString().trim().toUpperCase();
    }
    String cCampoNombre = ca1 + " " + ca2;
    if (cCampoNombre.trim().equalsIgnoreCase("")) {
      cCampoNombre = "CAMPO_NRO_" + i;
    }
    return this.arrNomCampos[i] = cCampoNombre.trim();
  }
  
  public int JAMGetColumns()
  {
    return this.arrNomCampos.length;
  }
  
  public void setActivaHoja(int argActiva, int argIniRow)
  {
    this.intHojaActiva = argActiva;
    this.intRowIni = argIniRow;
    defineNombresCampos();
  }
  
  public String getNameHoja()
  {
    return this.sheet.getName();
  }
}
