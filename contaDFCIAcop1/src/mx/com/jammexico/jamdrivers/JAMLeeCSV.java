package mx.com.jammexico.jamdrivers;

import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAMLeeCSV
{
  private String cArchivo = null;
  private String[] arrNomCampos = null;
  private int intRows = 0;
  private Vector vctRegistro = new Vector();
  private CsvReader cvsReader = null;
  
  public JAMLeeCSV(String cArgArchivo)
  {
    this.cArchivo = cArgArchivo;
    try
    {
      File fichero = new File(this.cArchivo);
      this.cvsReader = new CsvReader(new FileReader(fichero));
      
      this.cvsReader.readHeaders();
      int intColumns = this.cvsReader.getHeaderCount();
      this.arrNomCampos = new String[intColumns];
      for (int i = 0; i < intColumns; i++)
      {
        String cCampoNombre = "";
        try
        {
          cCampoNombre = this.cvsReader.getHeader(i);
        }
        catch (Exception localException1) {}
        if (cCampoNombre.trim().equalsIgnoreCase("")) {
          cCampoNombre = "CAMPO_NRO_" + i;
        }
        this.arrNomCampos[i] = cCampoNombre.trim();
      }
      while (this.cvsReader.readRecord())
      {
        String[] arrCampos = new String[intColumns];
        for (int i = 0; i < intColumns; i++) {
          arrCampos[i] = this.cvsReader.get(i);
        }
        this.vctRegistro.add(arrCampos);
        this.intRows += 1;
      }
      this.cvsReader.close();
    }
    catch (FileNotFoundException e)
    {
      JAMUtil.showDialog("Error Al Leer Archivo");
      return;
    }
    catch (Exception e)
    {
      JAMUtil.showDialog("Error Al Leer Archivo");
      return;
    }
    initialize();
  }
  
  public int JAMGetRows()
  {
    return this.intRows;
  }
  
  private void initialize() {}
  
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
        String[] arrCampos = (String[])this.vctRegistro.get(argRow);
        return new Integer(new Double(arrCampos[argCampo]).intValue());
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
        String[] arrCampos = (String[])this.vctRegistro.get(argRow);
        return new Double(new Double(arrCampos[argCampo]).doubleValue());
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
        String[] arrCampos = (String[])this.vctRegistro.get(argRow);
        return arrCampos[argCampo];
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
        String[] arrCampos = (String[])this.vctRegistro.get(argRow);
        try
        {
          SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
          reto = sdf.parse(arrCampos[argCampo]);
        }
        catch (Exception e)
        {
          System.out.println(e);
        }
        return reto;
      }
    }
    catch (Exception localException1) {}
    return reto;
  }
  
  public Time JAMGetTime(String argCampo, int argRow)
  {
    return JAMGetTime(JAMUtil.JAMFindArray(this.arrNomCampos, argCampo), argRow);
  }
  
  public Time JAMGetTime(int argCampo, int argRow)
  {
    Time reto = null;
    try
    {
      if (argCampo != -1)
      {
        String[] arrCampos = (String[])this.vctRegistro.get(argRow);
        return JAMUtil.SparseToTime(arrCampos[argCampo]);
      }
    }
    catch (Exception localException) {}
    return reto;
  }
}
