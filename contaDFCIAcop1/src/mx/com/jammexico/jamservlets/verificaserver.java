package mx.com.jammexico.jamservlets;

import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPFeatureSetFactory;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.DPFPTemplateFactory;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationFactory;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

class verificaserver
{
  private String strPath = null;
  private String strSource = null;
  private int farRequested = 21474;
  private DPFPTemplate template = null;
  
  public verificaserver(String argSource, String argPath)
  {
    this.strSource = argSource;
    this.strPath = argPath;
    try
    {
      this.template = DPFPGlobal.getTemplateFactory().createTemplate();
      this.template.deserialize(getByteFiles(this.strPath + this.strSource));
    }
    catch (IllegalArgumentException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public byte[] getByteFiles(String argPath)
    throws IOException
  {
    InputStream in = new FileInputStream(argPath);
    int size = in.available();
    byte[] buf = new byte[size];
    in.read(buf, 0, size);
    return buf;
  }
  
  public File[] getFiles()
  {
    File dir = new File(this.strPath);
    File[] files = dir.listFiles(new FilenameFilter()
    {
      public boolean accept(File dir, String name)
      {
        if ((name.startsWith("DI")) && (name.endsWith(".fpt"))) {
          return true;
        }
        if ((name.startsWith("DM")) && (name.endsWith(".fpt"))) {
          return true;
        }
        if ((name.startsWith("DA")) && (name.endsWith(".fpt"))) {
          return true;
        }
        if ((name.startsWith("DP")) && (name.endsWith(".fpt"))) {
          return true;
        }
        if ((name.startsWith("DG")) && (name.endsWith(".fpt"))) {
          return true;
        }
        if ((name.startsWith("II")) && (name.endsWith(".fpt"))) {
          return true;
        }
        if ((name.startsWith("IM")) && (name.endsWith(".fpt"))) {
          return true;
        }
        if ((name.startsWith("IA")) && (name.endsWith(".fpt"))) {
          return true;
        }
        if ((name.startsWith("IP")) && (name.endsWith(".fpt"))) {
          return true;
        }
        if ((name.startsWith("IG")) && (name.endsWith(".fpt"))) {
          return true;
        }
        return false;
      }
    });
    return files;
  }
  
  public String doverifica()
    throws IllegalArgumentException, IOException
  {
    File[] files = getFiles();
    for (int p = 0; p < files.length; p++)
    {
      DPFPFeatureSetFactory featureSetFactory = DPFPGlobal.getFeatureSetFactory();
      DPFPVerification verification = DPFPGlobal.getVerificationFactory().createVerification(this.farRequested);
      verification.setFARRequested(214748);
      
      DPFPFeatureSet features = featureSetFactory.createFeatureSet(getByteFiles(files[p].getAbsolutePath()));
      DPFPVerificationResult result = verification.verify(features, this.template);
      int far = result.getFalseAcceptRate();
      if (result.isVerified()) {
        return "true";
      }
    }
    return "false";
  }
  
  public boolean performVerification(byte[] fingerScanTemplate, byte[] sampleFeatures)
  {
    DPFPTemplateFactory templateFactory = DPFPGlobal.getTemplateFactory();
    DPFPFeatureSetFactory featureSetFactory = DPFPGlobal.getFeatureSetFactory();
    DPFPVerification verifier = DPFPGlobal.getVerificationFactory().createVerification();
    
    DPFPTemplate deserializedTemplate = templateFactory.createTemplate(fingerScanTemplate);
    DPFPFeatureSet features = featureSetFactory.createFeatureSet(sampleFeatures);
    
    DPFPVerificationResult result = null;
    result = verifier.verify(features, deserializedTemplate);
    
    return (result != null) && (result.isVerified());
  }
}
