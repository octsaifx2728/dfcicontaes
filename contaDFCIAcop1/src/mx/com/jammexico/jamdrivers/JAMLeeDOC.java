package mx.com.jammexico.jamdrivers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Vector;
import mx.com.jammexico.jamsrv.JAMUtil;
import org.apache.poi.hdf.extractor.WordDocument;
import org.apache.poi.hpsf.PropertySet;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;

public class JAMLeeDOC
{
  private String tempF = "";
  private Vector vctRegistro = new Vector();
  private int intIndice = 0;
  private int intMargen = 0;
  
  public JAMLeeDOC(String filename, String[] argDefinidores, int argMargen)
  {
    try
    {
      this.intMargen = argMargen;
      this.tempF = (JAMUtil.JAMFindeStrAll(filename, true, ".") + ".txt");
      
      POIFSReader r = new POIFSReader();
      r.registerListener(new MyPOIFSReaderListener(), "\005SummaryInformation");
      r.read(new FileInputStream(filename));
      
      leeDoc td = new leeDoc(filename, this.tempF);
      td.creaTemporarioTxt();
      
      BufferedReader in = new BufferedReader(new FileReader(this.tempF));
      for (;;)
      {
        String aux = in.readLine();
        if (aux == null) {
          break;
        }
        if ((!aux.trim().equalsIgnoreCase("")) && (argDefinidores == null))
        {
          this.vctRegistro.add(aux);
        }
        else if ((argDefinidores != null) && 
          (aux.trim().indexOf(argDefinidores[this.intIndice]) != -1))
        {
          this.vctRegistro.add(aux);
          this.intIndice += 1;
          if (this.intIndice == argDefinidores.length) {
            this.intIndice = this.intMargen;
          }
        }
      }
      in.close();
    }
    catch (Exception localException) {}
  }
  
  public String getRegistro(int argRegistro)
  {
    return (String)this.vctRegistro.get(argRegistro);
  }
  
  public int JAMGetRows()
  {
    return this.vctRegistro.size();
  }
  
  static class MyPOIFSReaderListener
    implements POIFSReaderListener
  {
    public void processPOIFSReaderEvent(POIFSReaderEvent event)
    {
      SummaryInformation si = null;
      PropertySet ps = null;
      try
      {
        si = (SummaryInformation)PropertySetFactory.create(event.getStream());
      }
      catch (Exception ex)
      {
        throw new RuntimeException("Property set stream \"" + event.getPath() + event.getName() + "\": " + ex);
      }
      String title = si.getTitle();
      if (title != null) {
        System.out.println("Title: \"" + title + "\"");
      } else {
        System.out.println("Document has no title.");
      }
    }
  }
  
  static class leeDoc
  {
    String origFileName;
    String tempFile;
    WordDocument wd;
    
    leeDoc(String origFileName, String tempFile)
    {
      this.tempFile = tempFile;
      this.origFileName = origFileName;
    }
    
    public void creaTemporarioTxt()
    {
      try
      {
        this.wd = new WordDocument(this.origFileName);
        Writer out = new BufferedWriter(new FileWriter(this.tempFile));
        this.wd.writeAllText(out);
        out.flush();
        out.close();
      }
      catch (Exception eN)
      {
        System.out.println("Error reading document:" + this.origFileName + "\n" + eN.toString());
      }
    }
  }
}
