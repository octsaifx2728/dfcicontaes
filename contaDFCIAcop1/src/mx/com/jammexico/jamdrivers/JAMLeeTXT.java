package mx.com.jammexico.jamdrivers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;
import mx.com.jammexico.jamcomponents.JAMDate;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAMLeeTXT
{
  private String cArchivo = null;
  private String[] arrNomCampos = null;
  private String cDelimitador = null;
  private String cDelimitadorText = null;
  private int intSaltoLinea = 0;
  private int intRowIni = 0;
  private int intRows = 0;
  private Vector<String> vctFilas = new Vector();
  private Vector<String> vctRegistro = new Vector();
  private Date fechahoy = new Date(new Date().getTime());
  
  public JAMLeeTXT(String cArgArchivo, String cArgDelimitador)
  {
    this.cArchivo = cArgArchivo;
    this.cDelimitador = cArgDelimitador;
    initialize(cArgArchivo, cArgDelimitador, 0);
  }
  
  public JAMLeeTXT(String cArgArchivo, String cArgDelimitador, int argSaltoLinea)
  {
    this.cArchivo = cArgArchivo;
    this.intSaltoLinea = argSaltoLinea;
    this.cDelimitador = cArgDelimitador;
    initialize(cArgArchivo, cArgDelimitador, argSaltoLinea);
  }
  
  public JAMLeeTXT(String cArgArchivo, String cArgDelimitador, String cArgDelimitadorText)
  {
    this.cArchivo = cArgArchivo;
    this.cDelimitador = cArgDelimitador;
    this.cDelimitadorText = cArgDelimitadorText;
    initialize(cArgArchivo, cArgDelimitador, 0);
  }
  
  public int JAMGetRows()
  {
    return this.vctFilas.size() - this.intSaltoLinea;
  }
  
  private void initialize(String cArgArchivo, String cArgDelimitador, int argSaltoLinea)
  {
    try
    {
      File f = new File(this.cArchivo);
      BufferedReader fleArchivo = new BufferedReader(new FileReader(f));
      String cLinea = fleArchivo.readLine();
      int intFila = 1;
      while (cLinea != null)
      {
        if (!cLinea.trim().equalsIgnoreCase(""))
        {
          if (argSaltoLinea == 0) {
            this.vctFilas.add(cLinea);
          } else if (intFila > argSaltoLinea) {
            this.vctFilas.add(cLinea);
          }
          intFila++;
        }
        cLinea = fleArchivo.readLine();
      }
      fleArchivo.close();
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
  }
  
  public JAMLeeTextRst getRegistro(int intRegistro)
  {
    try
    {
      this.vctRegistro = new Vector();
      String cLinea = (String)this.vctFilas.get(intRegistro);
      
      int intPosi = 0;
      while (intPosi >= 0)
      {
        intPosi = cLinea.substring(2).indexOf(this.cDelimitador + this.cDelimitador);
        if (intPosi > 0) {
          cLinea = cLinea.substring(0, intPosi + 2) + this.cDelimitador + " " + this.cDelimitador + cLinea.substring(intPosi + 4);
        }
      }
      StringTokenizer strCampos = new StringTokenizer(cLinea, this.cDelimitador);
      String cCampo = "";
      while (strCampos.hasMoreElements())
      {
        cCampo = (String)strCampos.nextElement();
        this.vctRegistro.add(cCampo);
      }
      return new JAMLeeTextRst(this.vctRegistro);
    }
    catch (Exception e)
    {
      JAMUtil.showDialog("Error Al Leer Archivo " + e.getMessage());
    }
    return null;
  }
  
  public JAMLeeTextRst getRegistroOld(int intRegistro)
  {
    try
    {
      this.vctRegistro = new Vector();
      int nContaRegistrosReales = 0;
      
      File f = new File(this.cArchivo);
      BufferedReader fleArchivo = new BufferedReader(new FileReader(f));
      String cLinea = fleArchivo.readLine();
      while (cLinea != null)
      {
        if (nContaRegistrosReales >= intRegistro + this.intRowIni)
        {
          String cCampo = "";
          for (int i = 0; i < cLinea.length(); i++) {
            if (cLinea.substring(i, i + 1).equalsIgnoreCase(this.cDelimitador))
            {
              if (this.cDelimitadorText == null)
              {
                this.vctRegistro.add(cCampo);
              }
              else if ((cCampo.startsWith(this.cDelimitadorText)) && (cCampo.substring(cCampo.length() - 1).equalsIgnoreCase(this.cDelimitadorText)))
              {
                if (cCampo.trim().equalsIgnoreCase(this.cDelimitadorText + this.cDelimitadorText)) {
                  this.vctRegistro.add("");
                } else {
                  this.vctRegistro.add(cCampo.substring(1).substring(0, cCampo.length() - 2).trim());
                }
              }
              else if (!cCampo.startsWith(this.cDelimitadorText))
              {
                this.vctRegistro.add(cCampo.trim());
              }
              else
              {
                String cLineaExt = cLinea.substring(cLinea.indexOf(cCampo) + cCampo.length());
                cLineaExt = cLineaExt.substring(0, cLineaExt.indexOf(this.cDelimitadorText));
                String cLineaRes = cLineaExt;
                cLineaExt = cCampo.substring(1) + cLineaExt;
                this.vctRegistro.add(cLineaExt.trim());
                i += cLineaRes.length() + 1;
              }
              cCampo = "";
            }
            else
            {
              cCampo = cCampo + cLinea.substring(i, i + 1);
            }
          }
          if (cCampo.equalsIgnoreCase("")) {
            break;
          }
          if (cCampo.trim().equalsIgnoreCase(this.cDelimitadorText + this.cDelimitadorText))
          {
            this.vctRegistro.add(""); break;
          }
          this.vctRegistro.add(cCampo);
          
          break;
        }
        if ((this.intSaltoLinea != 0) && (nContaRegistrosReales != 0)) {
          for (int p = 0; p < this.intSaltoLinea; p++)
          {
            cLinea = fleArchivo.readLine();
            if (cLinea == null) {
              break;
            }
          }
        }
        nContaRegistrosReales++;
        cLinea = fleArchivo.readLine();
      }
      return new JAMLeeTextRst(this.vctRegistro);
    }
    catch (FileNotFoundException e)
    {
      JAMUtil.showDialog("Error Al Leer Archivo " + e.getMessage());
      return null;
    }
    catch (Exception e)
    {
      JAMUtil.showDialog("Error Al Leer Archivo " + e.getMessage());
    }
    return null;
  }
  
  public void setMargenSuperior(int argMargenSuperior)
  {
    this.intRowIni = argMargenSuperior;
    this.intRows -= this.intRowIni;
  }
  
  public class JAMLeeTextRst
  {
    private int intColumnas = 0;
    private Vector rstRegistro = null;
    
    public JAMLeeTextRst(Vector argRegistro)
    {
      this.rstRegistro = argRegistro;
    }
    
    public String JAMGetString(int intPosition)
    {
      String tmpRetro = "";
      try
      {
        String ctmpCampo = (String)this.rstRegistro.get(intPosition - 2);
        tmpRetro = ctmpCampo.trim();
      }
      catch (Exception localException) {}
      return tmpRetro;
    }
    
    public Integer JAMGetInt(int intPosition)
    {
      Integer tmpRetro = new Integer(0);
      try
      {
        String ctmpCampo = (String)this.rstRegistro.get(intPosition - 2);
        tmpRetro = new Integer(ctmpCampo.trim());
      }
      catch (Exception localException) {}
      return tmpRetro;
    }
    
    public Double JAMGetDouble(int intPosition)
    {
      Double tmpRetro = new Double(0.0D);
      try
      {
        String ctmpCampo = (String)this.rstRegistro.get(intPosition - 2);
        tmpRetro = new Double(ctmpCampo.trim());
      }
      catch (Exception localException) {}
      return tmpRetro;
    }
    
    public Date JAMGetDate(int intPosition)
    {
      Date tmpRetro = JAMLeeTXT.this.fechahoy;
      try
      {
        String ctmpCampo = (String)this.rstRegistro.get(intPosition - 2);
        tmpRetro = new Date(ctmpCampo.trim());
      }
      catch (Exception localException) {}
      return tmpRetro;
    }
    
    public Date JAMGetDateStr(int intPosition)
    {
      Date tmpRetro = JAMLeeTXT.this.fechahoy;
      try
      {
        JAMDate objDate = new JAMDate();
        String ctmpCampo = (String)this.rstRegistro.get(intPosition - 2);
        
        objDate.setYear(new Integer(ctmpCampo.substring(0, 4)).intValue());
        objDate.setMonth(new Integer(ctmpCampo.substring(4, 6)).intValue());
        objDate.setDay(new Integer(ctmpCampo.substring(6, 8)).intValue());
        
        tmpRetro = objDate.getFetch();
      }
      catch (Exception localException) {}
      return tmpRetro;
    }
    
    public int JAMGetFields()
    {
      return JAMLeeTXT.this.vctRegistro.size();
    }
  }
}
