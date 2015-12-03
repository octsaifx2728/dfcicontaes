package mx.com.jammexico.jamdrivers;

import ch.ubique.inieditor.IniEditor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Vector;
import jcifs.Config;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import mx.com.jammexico.jamdb.JAMDataBase;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAMCopiaInstancias
{
  private static final String FIREBIRD_JAMSOCDB = "JAMSOCDB";
  private static final String JAM_ARCHIVO_INI_CONNECT = "conections.ini";
  private static final String JAM_ARCHIVO_INI_PRINTER = "jamsrvprint.ini";
  private static final String FIREBIRD_ARCHIVO_CONFIG = "aliases.conf";
  private static final String JAM_PATH_DBS_PREFIJO = "DB_";
  private static final String JAM_WEB_CONFIG = "C:\\JAMWebService\\JAMDBConfig\\";
  private static final String ROOT_PICTURE = "RootPicture = C:\\\\JAMWebService\\\\Tomcat 5.5\\\\webapps\\\\JAMMexico\\\\Images\\\\dbpictures\\\\";
  private static final String ROOT_FILES = "RootFiles = C:\\\\JAMWebService\\\\Tomcat 5.5\\\\webapps\\\\JAMMexico\\\\tempfiles\\\\dbfiles\\\\";
  private static final String ROOT_FILES_XML = "RootFilesXml = C:\\\\JAMWebService\\\\Tomcat 5.5\\\\webapps\\\\JAMMexico\\\\tempfiles\\\\dbxml\\\\";
  private static final String PATH_REPORTES = "PATH_REPORTES = C:\\\\JAMWebService\\\\Tomcat 5.5\\\\webapps\\\\JAMMexico\\\\Reportes\\\\";
  private static final String PATH_IMAGES = "PATH_IMAGES = C:\\\\JAMWebService\\\\Tomcat 5.5\\\\webapps\\\\JAMMexico\\\\Reportes\\\\";
  private static final String PATH_PICTURES = "PATH_PICTURES = C:\\\\JAMWebService\\\\Tomcat 5.5\\\\webapps\\\\JAMMexico\\\\Images\\dbpictures\\\\";
  private static final String DB_JAM_USER = "DB_JAM_USER = JAM$_";
  private static final String DB_JAM_ROLE = "DB_JAM_ROLE = JAM_SOC";
  private static final String DB_SYS_USER = "DB_SYS_USER = SYSDBA";
  private static final String DB_SYS_PASS = "DB_SYS_PASS = ChkMts5";
  private static final String P_VALOR_REPORAUTO = "P_VALOR_REPORAUTO = JAMAutoPrint.jasper";
  private static final String INSTANCIA_FILES_SRC = "src\\";
  private static final String INSTANCIA_REPORTES_IMAGES = "Images\\";
  private static final String SERVIDORAPL = "servidor = http://72.55.137.77:8024";
  private static final String USRMASTER = "usrmaster = SYSDBA";
  private static final String PWDMASTER = "pwdmaster = ChkMts5";
  private String USUARIO_DEFAULT = "JAMADM";
  private String PASSWORD_DEFAULT = "JAMADM";
  private static String FIREBIRD_PATH = "E:\\Program Files\\Firebird\\Firebird_1_5\\";
  private static String JAM_PATH_DBS = "C:\\JAMDataBase\\";
  private static String FIREBIRD_VACIO = "C:\\JAMDataBase\\DB_VAC\\VAC.KIT";
  private static String ROOT_WEB = "C:\\JAMWebService\\Tomcat 5.5\\webapps\\JAMMexico\\";
  private static String DRIVER_NAME_FIREBIRD = "org.firebirdsql.jdbc.FBDriver";
  private static String CONNECT_STRING_FIREBIRD = "jdbc:firebirdsql:localhost/3050:";
  private static String DRIVER_NAME = "DriverName = " + DRIVER_NAME_FIREBIRD;
  private static String CONNECT_STRING = "ConnectString = " + CONNECT_STRING_FIREBIRD;
  private static String DB_DRIVER_FIREBIRD = "DB_DRIVER_FIREBIRD = " + DRIVER_NAME_FIREBIRD;
  private static String DB_CONECCION = "DB_CONECCION = " + CONNECT_STRING_FIREBIRD;
  private static String INSTANCIA_FILES = ROOT_WEB + "tempfiles\\dbfiles\\";
  private static String INSTANCIA_FILES_XML = ROOT_WEB + "tempfiles\\dbxml\\";
  private static String INSTANCIA_IMAGES = ROOT_WEB + "Images\\dbpictures\\";
  private static String INSTANCIA_REPORTES = ROOT_WEB + "Reportes\\";
  private static String INSTANCIA_FILES_SOURCE = ROOT_WEB + "tempfiles\\dbfiles\\jamvac\\";
  private static String INSTANCIA_FILES_SRC_SOURCE = ROOT_WEB + "tempfiles\\dbfiles\\jamvac\\src\\";
  private static String INSTANCIA_IMAGES_SOURCE = ROOT_WEB + "Images\\dbpictures\\jamvac\\";
  private static String INSTANCIA_REPORTES_SOURCE = ROOT_WEB + "Reportes\\jamvac\\";
  private static String INSTANCIA_REPORTES_IMAGES_SOURCE = ROOT_WEB + "Reportes\\jamvac\\Images\\";
  private static String SERVIDOR = "SERVIDOR01";
  public static final int INSTANCIA = 0;
  public static final int USUARIO = 1;
  public static final int CLAVE = 2;
  public static final int CLIENTE = 3;
  public static final int REGFISCAL = 4;
  public static final int SUCURSAL = 5;
  
  public JAMCopiaInstancias(String argServidorSucursal)
  {
    SERVIDOR = argServidorSucursal;
    getTomaArchivoDeCopiaInstancias();
  }
  
  public JAMCopiaInstancias()
  {
    getTomaArchivoDeCopiaInstancias();
  }
  
  private void getTomaArchivoDeCopiaInstancias()
  {
    try
    {
      FileInputStream file = new FileInputStream("C:\\JAMWebService\\JAMDBConfig\\autoinstancias.ini");
      IniEditor e = new IniEditor();
      e.load(file);
      
      this.USUARIO_DEFAULT = e.get(SERVIDOR, "USUARIO_DEFAULT");
      this.PASSWORD_DEFAULT = e.get(SERVIDOR, "PASSWORD_DEFAULT");
      FIREBIRD_PATH = e.get(SERVIDOR, "FIREBIRD_PATH");
      JAM_PATH_DBS = e.get(SERVIDOR, "JAM_PATH_DBS");
      FIREBIRD_VACIO = e.get(SERVIDOR, "FIREBIRD_VACIO");
      ROOT_WEB = e.get(SERVIDOR, "ROOT_WEB");
      DRIVER_NAME_FIREBIRD = e.get(SERVIDOR, "DRIVER_NAME_FIREBIRD");
      CONNECT_STRING_FIREBIRD = e.get(SERVIDOR, "CONNECT_STRING_FIREBIRD");
      
      DRIVER_NAME = "DriverName = " + DRIVER_NAME_FIREBIRD;
      CONNECT_STRING = "ConnectString = " + CONNECT_STRING_FIREBIRD;
      DB_DRIVER_FIREBIRD = "DB_DRIVER_FIREBIRD = " + DRIVER_NAME_FIREBIRD;
      DB_CONECCION = "DB_CONECCION = " + CONNECT_STRING_FIREBIRD;
      INSTANCIA_FILES = ROOT_WEB + "tempfiles\\dbfiles\\";
      INSTANCIA_FILES_XML = ROOT_WEB + "tempfiles\\dbxml\\";
      INSTANCIA_IMAGES = ROOT_WEB + "Images\\dbpictures\\";
      INSTANCIA_REPORTES = ROOT_WEB + "Reportes\\";
      INSTANCIA_FILES_SOURCE = ROOT_WEB + "tempfiles\\dbfiles\\jamvac\\";
      INSTANCIA_FILES_SRC_SOURCE = ROOT_WEB + "tempfiles\\dbfiles\\jamvac\\src\\";
      INSTANCIA_IMAGES_SOURCE = ROOT_WEB + "Images\\dbpictures\\jamvac\\";
      INSTANCIA_REPORTES_SOURCE = ROOT_WEB + "Reportes\\jamvac\\";
      INSTANCIA_REPORTES_IMAGES_SOURCE = ROOT_WEB + "Reportes\\jamvac\\Images\\";
      
      file.close();
    }
    catch (Exception e)
    {
      System.out.println("Error al Eliminar Etiquetas Antiguas : " + e.getMessage());
    }
  }
  
  public void JAMStartUp(String[] strArg)
    throws Exception
  {
    do00EliminaINIs(strArg[0]);
    do01AgregaDBInstancia(strArg[0]);
    do02AgregaINIs(strArg[0]);
    do03CreaDirectorios(strArg[0]);
    do04CopiaArchivos(strArg[0]);
    do05RenombraAdministradorDB(strArg);
  }
  
  public static void main(String[] args)
  {
    JAMCopiaInstancias obj = new JAMCopiaInstancias();
    
    String[] arrString = new String[4];
    
    arrString[0] = "PRUEBAJAM";
    arrString[1] = "ANIBALPEPE";
    arrString[2] = "ANIBALPEPE";
    arrString[3] = "Cliente de pruebas";
    try
    {
      obj.JAMStartUp(arrString);
    }
    catch (Exception e)
    {
      System.out.println(e.getMessage());
    }
  }
  
  private void do00EliminaINIs(String argInstancia)
    throws Exception
  {
    FileInputStream file = null;
    FileOutputStream filex = null;
    IniEditor e = null;
    try
    {
      file = new FileInputStream("C:\\JAMWebService\\JAMDBConfig\\conections.ini");
      e = new IniEditor();
      e.load(file);
      e.removeSection(argInstancia);
      filex = new FileOutputStream("C:\\JAMWebService\\JAMDBConfig\\conections.ini");
      e.save(filex);
      file.close();
      
      file = new FileInputStream("C:\\JAMWebService\\JAMDBConfig\\jamsrvprint.ini");
      e = new IniEditor();
      e.load(file);
      e.removeSection(argInstancia);
      filex = new FileOutputStream("C:\\JAMWebService\\JAMDBConfig\\jamsrvprint.ini");
      e.save(filex);
      file.close();
      if (FIREBIRD_PATH.toUpperCase().startsWith("C:"))
      {
        file = new FileInputStream(FIREBIRD_PATH + "aliases.conf");
        e = new IniEditor();
        e.load(file);
        e.remove("JAMSOCDB", argInstancia);
        
        filex = new FileOutputStream(FIREBIRD_PATH + "aliases.conf");
        e.save(filex);
        file.close();
      }
      else
      {
        try
        {
          Config.setProperty("jcifs.netbios.wins", "10.7.110.1");
          NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, "amohr", "annasophia2425..");
          
          SmbFile smbFile = new SmbFile(FIREBIRD_PATH + "aliases.conf", auth);
          synchronized (smbFile)
          {
            SmbFileInputStream fileredIn = new SmbFileInputStream(smbFile);
            
            OutputStream out = new FileOutputStream("C:\\JAMWebService\\JAMDBConfig\\" + argInstancia + ".conf");
            byte[] b = new byte[10];
              int n= 0;
            while ((n = fileredIn.read(b)) > 0)
            {
            
              out.write(b, 0, n);
              out.flush();
            }
            fileredIn.close();
            out.close();
            
            file = new FileInputStream("C:\\JAMWebService\\JAMDBConfig\\" + argInstancia + ".conf");
            e = new IniEditor();
            e.load(file);
            e.remove("JAMSOCDB", argInstancia);
            
            filex = new FileOutputStream("C:\\JAMWebService\\JAMDBConfig\\" + argInstancia + ".conf");
            e.save(filex);
            file.close();
            
            smbFile = new SmbFile(FIREBIRD_PATH + argInstancia + ".conf", auth);
            SmbFileOutputStream sfos = new SmbFileOutputStream(smbFile);
            
            FileInputStream inp = new FileInputStream("C:\\JAMWebService\\JAMDBConfig\\" + argInstancia + ".conf");
            b = new byte[10];
            
            while ((n = inp.read(b)) > 0)
            {
              sfos.write(b, 0, n);
              sfos.flush();
            }
            inp.close();
            sfos.close();
            
            SmbFile from = new SmbFile(FIREBIRD_PATH + "aliases.conf", auth);
            SmbFile to = new SmbFile(FIREBIRD_PATH + "aliases.conf" + ".bak", auth);
            if (to.exists()) {
              to.delete();
            }
            from.renameTo(to);
            
            from = new SmbFile(FIREBIRD_PATH + argInstancia + ".conf", auth);
            to = new SmbFile(FIREBIRD_PATH + "aliases.conf", auth);
            from.renameTo(to);
          }
        }
        catch (Exception e1)
        {
          System.out.println(e1.getMessage());
        }
      }
      return;
    }
    catch (Exception eg)
    {
      throw new Exception("Error al Eliminar Etiquetas Antiguas : " + eg.getMessage());
    }
  }
  
  private void do01AgregaDBInstancia(String argInstancia)
    throws Exception
  {
    String strTargetDBArchive = JAM_PATH_DBS + "DB_" + argInstancia + "\\\\" + argInstancia + ".KIT";
    try
    {
      if (FIREBIRD_PATH.toUpperCase().startsWith("C:"))
      {
        FileWriter fw = new FileWriter(FIREBIRD_PATH + "aliases.conf", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter salida = new PrintWriter(bw);
        salida.append(argInstancia + " = " + strTargetDBArchive + JAMUtil.getCrlf());
        salida.close();
      }
      else
      {
        Config.setProperty("jcifs.netbios.wins", "10.7.110.1");
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, "amohr", "annasophia2425..");
        
        SmbFile smbFile = new SmbFile(FIREBIRD_PATH + "aliases.conf", auth);
        synchronized (smbFile)
        {
          strTargetDBArchive = strTargetDBArchive.substring(strTargetDBArchive.indexOf("JAMDataBase")).trim();
          strTargetDBArchive = "C:\\\\" + strTargetDBArchive;
          strTargetDBArchive = strTargetDBArchive.replace("//", "\\\\");
          
          SmbFileOutputStream sfos = new SmbFileOutputStream(smbFile, true);
          String strAppend = argInstancia + " = " + strTargetDBArchive + JAMUtil.getCrlf();
          sfos.write(strAppend.getBytes());
          sfos.close();
        }
      }
    }
    catch (IOException ioex)
    {
      throw new Exception("error Agregar Instancias en los INI de la Base de datos : " + ioex.toString());
    }
    catch (Exception ioex)
    {
      throw new Exception("error Agregar Instancias en los INI de la Base de datos : " + ioex.toString());
    }
  }
  
  private void do02AgregaINIs(String argInstancia)
    throws Exception
  {
    FileWriter fw = null;
    BufferedWriter bw = null;
    PrintWriter salida = null;
    try
    {
      fw = new FileWriter("C:\\JAMWebService\\JAMDBConfig\\conections.ini", true);
      bw = new BufferedWriter(fw);
      salida = new PrintWriter(bw);
      
      salida.append("[" + argInstancia + "]" + JAMUtil.getCrlf());
      salida.append(DRIVER_NAME + JAMUtil.getCrlf());
      salida.append(CONNECT_STRING + argInstancia + JAMUtil.getCrlf());
      salida.append("RootPicture = C:\\\\JAMWebService\\\\Tomcat 5.5\\\\webapps\\\\JAMMexico\\\\Images\\\\dbpictures\\\\" + JAMUtil.getCrlf());
      salida.append("RootFiles = C:\\\\JAMWebService\\\\Tomcat 5.5\\\\webapps\\\\JAMMexico\\\\tempfiles\\\\dbfiles\\\\" + JAMUtil.getCrlf());
      salida.append("RootFilesXml = C:\\\\JAMWebService\\\\Tomcat 5.5\\\\webapps\\\\JAMMexico\\\\tempfiles\\\\dbxml\\\\" + JAMUtil.getCrlf());
      salida.append("servidor = http://72.55.137.77:8024" + JAMUtil.getCrlf());
      salida.append("usrmaster = SYSDBA" + JAMUtil.getCrlf());
      salida.append("pwdmaster = ChkMts5" + JAMUtil.getCrlf());
      salida.append(JAMUtil.getCrlf());
      salida.close();
      
      fw = new FileWriter("C:\\JAMWebService\\JAMDBConfig\\jamsrvprint.ini", true);
      bw = new BufferedWriter(fw);
      salida = new PrintWriter(bw);
      
      salida.append("[" + argInstancia + "]" + JAMUtil.getCrlf());
      salida.append("PATH_REPORTES = C:\\\\JAMWebService\\\\Tomcat 5.5\\\\webapps\\\\JAMMexico\\\\Reportes\\\\" + argInstancia.toLowerCase() + "\\\\" + JAMUtil.getCrlf());
      salida.append("PATH_IMAGES = C:\\\\JAMWebService\\\\Tomcat 5.5\\\\webapps\\\\JAMMexico\\\\Reportes\\\\" + argInstancia.toLowerCase() + "\\\\Images\\\\" + JAMUtil.getCrlf());
      salida.append("PATH_PICTURES = C:\\\\JAMWebService\\\\Tomcat 5.5\\\\webapps\\\\JAMMexico\\\\Images\\dbpictures\\\\" + JAMUtil.getCrlf());
      salida.append(DB_DRIVER_FIREBIRD + JAMUtil.getCrlf());
      salida.append(DB_CONECCION + argInstancia + JAMUtil.getCrlf());
      salida.append("DB_JAM_USER = JAM$_" + JAMUtil.getCrlf());
      salida.append("DB_JAM_ROLE = JAM_SOC" + JAMUtil.getCrlf());
      salida.append("DB_SYS_USER = SYSDBA" + JAMUtil.getCrlf());
      salida.append("DB_SYS_PASS = ChkMts5" + JAMUtil.getCrlf());
      salida.append("P_VALOR_REPORAUTO = JAMAutoPrint.jasper" + JAMUtil.getCrlf());
      salida.append(JAMUtil.getCrlf());
      salida.close();
    }
    catch (IOException ioex)
    {
      throw new Exception("error al agregar INSTANCIA en los INIs del Tomcat : " + ioex.toString());
    }
    catch (Exception ioex)
    {
      throw new Exception("error al agregar INSTANCIA en los INIs del Tomcat : " + ioex.toString());
    }
  }
  
  private void do03CreaDirectorios(String argInstancia)
    throws Exception
  {
    try
    {
      File file = null;
      if (JAM_PATH_DBS.toUpperCase().startsWith("C:"))
      {
        file = new File(JAM_PATH_DBS + "DB_" + argInstancia);
        file.mkdirs();
      }
      else
      {
        Config.setProperty("jcifs.netbios.wins", "10.7.110.1");
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, "amohr", "annasophia2425..");
        SmbFile smbFile = new SmbFile(JAM_PATH_DBS + "DB_" + argInstancia + "//", auth);
        if (smbFile.exists()) {
          smbFile.delete();
        }
        smbFile.mkdir();
      }
      file = new File(INSTANCIA_FILES + argInstancia.toLowerCase());
      file.mkdirs();
      
      file = new File(INSTANCIA_FILES + argInstancia.toLowerCase() + "\\" + "src\\");
      file.mkdirs();
      
      file = new File(INSTANCIA_FILES_XML + argInstancia.toLowerCase());
      file.mkdirs();
      
      file = new File(INSTANCIA_IMAGES + argInstancia.toLowerCase());
      file.mkdirs();
      
      file = new File(INSTANCIA_REPORTES + argInstancia.toLowerCase());
      file.mkdirs();
      
      file = new File(INSTANCIA_REPORTES + argInstancia.toLowerCase() + "\\" + "Images\\");
      file.mkdirs();
    }
    catch (Exception e)
    {
      throw new Exception("error al Crear Directorios : " + e.getMessage());
    }
  }
  
  private void do04CopiaArchivos(String argInstancia)
    throws Exception
  {
    Vector vctArchivos = null;
    String cFile = null;
    String strSource = FIREBIRD_VACIO;
    String strTarget = JAM_PATH_DBS + "DB_" + argInstancia + "\\" + argInstancia + ".KIT";
    try
    {
      if (strSource.toUpperCase().startsWith("C:"))
      {
        JAMUtil.copyFile(new File(strSource), new File(strTarget));
      }
      else
      {
        Config.setProperty("jcifs.netbios.wins", "10.7.110.1");
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, "amohr", "annasophia2425..");
        
        SmbFile from = new SmbFile(strSource, auth);
        SmbFile to = new SmbFile(strTarget, auth);
        if (to.exists()) {
          to.delete();
        }
        from.copyTo(to);
      }
      vctArchivos = JAMUtil.listDirectoryVector(INSTANCIA_FILES_SRC_SOURCE, null, 0);
      for (int i = 0; i < vctArchivos.size(); i++)
      {
        cFile = (String)vctArchivos.get(i);
        strSource = INSTANCIA_FILES_SRC_SOURCE + cFile;
        strTarget = INSTANCIA_FILES + argInstancia.toLowerCase() + "\\" + "src\\" + cFile;
        
        JAMUtil.copyFile(new File(strSource), new File(strTarget));
      }
      vctArchivos = JAMUtil.listDirectoryVector(INSTANCIA_FILES_SOURCE, null, 0);
      for (int i = 0; i < vctArchivos.size(); i++)
      {
        cFile = (String)vctArchivos.get(i);
        strSource = INSTANCIA_FILES_SOURCE + cFile;
        strTarget = INSTANCIA_FILES + argInstancia.toLowerCase() + "\\" + cFile;
        
        JAMUtil.copyFile(new File(strSource), new File(strTarget));
      }
      vctArchivos = JAMUtil.listDirectoryVector(INSTANCIA_IMAGES_SOURCE, null, 0);
      for (int i = 0; i < vctArchivos.size(); i++)
      {
        cFile = (String)vctArchivos.get(i);
        strSource = INSTANCIA_IMAGES_SOURCE + cFile;
        strTarget = INSTANCIA_IMAGES + argInstancia.toLowerCase() + "\\" + cFile;
        
        JAMUtil.copyFile(new File(strSource), new File(strTarget));
      }
      vctArchivos = JAMUtil.listDirectoryVector(INSTANCIA_REPORTES_IMAGES_SOURCE, null, 0);
      for (int i = 0; i < vctArchivos.size(); i++)
      {
        cFile = (String)vctArchivos.get(i);
        strSource = INSTANCIA_REPORTES_IMAGES_SOURCE + cFile;
        strTarget = INSTANCIA_REPORTES + argInstancia.toLowerCase() + "\\" + "Images\\" + cFile;
        
        JAMUtil.copyFile(new File(strSource), new File(strTarget));
      }
      vctArchivos = JAMUtil.listDirectoryVector(INSTANCIA_REPORTES_SOURCE, null, 0);
      for (int i = 0; i < vctArchivos.size(); i++)
      {
        cFile = (String)vctArchivos.get(i);
        strSource = INSTANCIA_REPORTES_SOURCE + cFile;
        strTarget = INSTANCIA_REPORTES + argInstancia.toLowerCase() + "\\" + cFile;
        
        JAMUtil.copyFile(new File(strSource), new File(strTarget));
      }
    }
    catch (IOException ioex)
    {
      throw new Exception("Error al copiar archivos del Tomcar : " + ioex.toString());
    }
    catch (Exception ioex)
    {
      throw new Exception("Error al copiar archivos del Tomcar : " + ioex.toString());
    }
  }
  
  private void do05RenombraAdministradorDB(String[] strArg)
    throws Exception
  {
    try
    {
      JAMDataBase dbLee = new JAMDataBase(strArg[0], this.USUARIO_DEFAULT, this.PASSWORD_DEFAULT);
      
      String[] arrSqls = new String[1];
      
      arrSqls[0] = 
      
        ("Select * from JAM$_CREAINSTANCIANEW('" + strArg[0] + "','" + strArg[3] + "','" + strArg[1] + "','" + strArg[2] + "','JAMADM','" + strArg[4] + "')");
      try
      {
        dbLee.getRowSet(arrSqls);
      }
      catch (Exception e)
      {
        throw new Exception(e.getMessage());
      }
    }
    catch (Exception e)
    {
      throw new Exception("Error al actualizar la base de datos : " + e.getMessage());
    }
  }
}
