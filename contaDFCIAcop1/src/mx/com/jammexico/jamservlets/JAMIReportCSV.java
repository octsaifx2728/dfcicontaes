package mx.com.jammexico.jamservlets;

import java.sql.Connection;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;

class JAMIReportCSV
{
  private String pathFileJasper;
  private String pathFileCsv;
  private HashMap<String, Object> parametersCsv;
  private Connection conCsv;
  private int flagFinalizo = 0;
  private String cMensajeError = null;
  
  public JAMIReportCSV(String argPathJasper, String argPathCsv, HashMap<String, Object> argParameters, Connection argCon)
  {
    this.conCsv = argCon;
    this.parametersCsv = argParameters;
    this.pathFileJasper = argPathJasper;
    this.pathFileCsv = argPathCsv;
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
  
  public void run()
  {
    JasperPrint print = null;
    try
    {
      print = JasperFillManager.fillReport(this.pathFileJasper, this.parametersCsv, this.conCsv);
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
      JRExporter exporter = new JRCsvExporter();
      exporter.setParameter(JRCsvExporterParameter.JASPER_PRINT, print);
      exporter.setParameter(JRCsvExporterParameter.OUTPUT_FILE_NAME, this.pathFileCsv);
      exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, ",");
      exporter.setParameter(JRCsvExporterParameter.RECORD_DELIMITER, System.getProperty("line.separator"));
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
