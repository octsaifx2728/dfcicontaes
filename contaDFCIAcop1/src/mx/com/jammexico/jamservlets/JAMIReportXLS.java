package mx.com.jammexico.jamservlets;

import java.sql.Connection;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

class JAMIReportXLS
{
  private String pathFileJasper;
  private String pathFileXls;
  private HashMap<String, Object> parametersXls;
  private Connection conXls;
  private int flagFinalizo = 0;
  private String cMensajeError = null;
  
  public JAMIReportXLS(String argPathJasper, String argPathXls, HashMap<String, Object> argParameters, Connection argCon)
  {
    this.conXls = argCon;
    this.parametersXls = argParameters;
    this.pathFileJasper = argPathJasper;
    this.pathFileXls = argPathXls;
  }
  
  public void start()
  {
    run();
  }
  
  public int getActividad()
  {
    return this.flagFinalizo;
  }
  
  public String getMensajeError()
  {
    return this.cMensajeError;
  }
  
  private void run()
  {
    JasperPrint print = null;
    try
    {
      print = JasperFillManager.fillReport(this.pathFileJasper, this.parametersXls, this.conXls);
    }
    catch (JRException eX)
    {
      this.flagFinalizo = eX.hashCode();
      this.cMensajeError = ("producido por el reporteador " + eX.getMessage());
      System.gc();
      return;
    }
    catch (Exception eZ)
    {
      this.flagFinalizo = eZ.hashCode();
      this.cMensajeError = ("producido por java " + eZ.getMessage());
      System.gc();
      return;
    }
    try
    {
      JRExporter exporter = new JRXlsExporter();
      exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, print);
      exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, this.pathFileXls);
      exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
      exporter.exportReport();
    }
    catch (JRException eX)
    {
      this.flagFinalizo = eX.hashCode();
      this.cMensajeError = ("producido por el reporteador " + eX.getMessage());
      System.gc();
      return;
    }
    catch (Exception eZ)
    {
      this.flagFinalizo = eZ.hashCode();
      this.cMensajeError = ("producido por java " + eZ.getMessage());
      System.gc();
      return;
    }
    this.flagFinalizo = -1;
    System.gc();
  }
}
