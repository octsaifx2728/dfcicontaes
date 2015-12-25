package mx.com.jammexico.jamservlets;

import java.sql.Connection;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

class JAMIReportPDF
{
  private String pathFileJasper;
  private String pathFilePdf;
  private HashMap<String, Object> parametersPdf;
  private Connection conPdf;
  private int flagFinalizo = 0;
  private String cMensajeError = null;
  
  public JAMIReportPDF(String argPathJasper, String argPathPdf, HashMap<String, Object> argParameters, Connection argCon)
  {
    this.conPdf = argCon;
    this.parametersPdf = argParameters;
    this.pathFileJasper = argPathJasper;
    this.pathFilePdf = argPathPdf;
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
      print = JasperFillManager.fillReport(this.pathFileJasper, this.parametersPdf, this.conPdf);
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
      JRExporter exporter = new JRPdfExporter();
      exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, this.pathFilePdf);
      exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
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
