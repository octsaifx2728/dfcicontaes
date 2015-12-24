package mx.com.jammexico.jamlogon;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.ResultSetMetaData;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import mx.com.jammexico.jamcomponents.JAMCursor;
import mx.com.jammexico.jamcomponents.JAMLibKernel;
import mx.com.jammexico.jamcomponents.JAMTrayIcon;
import mx.com.jammexico.jamcomponents.files.JAMAlmacenPropiedades;
import mx.com.jammexico.jamdb.JAMClienteDB;
import mx.com.jammexico.jamsrv.JAMRowSet;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAM010RunTime
{
  private static String WEB_ROOT_IMAGES = "/mx/com/jammexico/jamcomponents/jamimages/";
  private boolean logCancelaLogon = false;
  private boolean logConParametros = false;
  private String strUserName = "";
  private String strUserPass = "";
  private String strUserInst = "";
  private String strUserServ = "";
  private String strUserServSocket = "";
  private String strUserPort = "";
  private String strUserPortSocket = "";
  private String strUserIDbs = "";
  private String strClassCall = "?";
  private String strModeCall = "1";
  private String strUserChat = "";
  private String[] arrStrServs = null;
  private String[] arrAliasDBS = new String[10];
  private JAMRowSet rstMenu = null;
  private JAMRowSet rstTrace = null;
  private JAM030Mdi Jam030Mdi = null;
  private JAM070VisorPanel Jam070Visor = null;
  private JFrame frmVisor = new JFrame();
  
  public static void main(String[] args)
  {
    if (args.length == 0) {
      new JAM010RunTime();
    } else {
      new JAM010RunTime(args, false);
    }
  }
  
  public JAM010RunTime()
  {
    JAMLibKernel.setActivo(true, false);
    setLookAndFeel();
    JAMStarUp();
  }
  
  public JAM010RunTime(String[] args, boolean argApplet)
  {
    JAMLibKernel.ParamJAMStartUp = args;
    JAMLibKernel.setActivo(true, argApplet);
    setLookAndFeel();
    JAMStarUp();
  }
  
  public JAM010RunTime(JAM030Mdi argJam030Mdi)
  {
    this.Jam030Mdi = argJam030Mdi;
    setLookAndFeel();
    JAMStarUp();
  }
  
  private void JAMStarUp()
  {
    this.logConParametros = (JAMLibKernel.ParamJAMStartUp != null);
    if (this.logConParametros)
    {
      this.strClassCall = JAMLibKernel.ParamJAMStartUp[8];
      this.strModeCall = JAMLibKernel.ParamJAMStartUp[9];
    }
    doSetVisor();
    while (doCallLogin()) {
      if (doDBLogin())
      {
        doOKLogin();
        doEnd(true);
        return;
      }
    }
    if (this.Jam030Mdi == null) {
      doEnd(false);
    }
    JAMCursor.setCursorOff();
  }
  
  private void doSetVisor()
  {
    this.Jam070Visor = new JAM070VisorPanel(WEB_ROOT_IMAGES);
    this.Jam070Visor.setMaximo(130);
    
    this.frmVisor = new JFrame();
    this.frmVisor.setContentPane(this.Jam070Visor);
    this.frmVisor.setSize(new Dimension(this.Jam070Visor.getIconWidth(), this.Jam070Visor.getIconHeight()));
    this.frmVisor.setLocationRelativeTo(null);
    this.frmVisor.setResizable(false);
    this.frmVisor.setAlwaysOnTop(false);
    this.frmVisor.setVisible(true);
    this.frmVisor.setTitle("VOS v1.05 - Visor de Actividades");
    
    this.frmVisor.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        if (JAM010RunTime.this.Jam070Visor != null) {
          JAM010RunTime.this.Jam070Visor.stopMusic();
        }
        JAMLibKernel.doEndSystemFull(true);
      }
    });
  }
  
  private void doJam070Visor(JAM070VisorMensajes argParametros)
  {
    if (this.Jam070Visor != null) {
      this.Jam070Visor.setValue(argParametros);
    }
  }
  
  private void setLookAndFeel()
  {
    try
    {
      String sSistemaOperativo = System.getProperty("os.name");
      sSistemaOperativo = sSistemaOperativo.toUpperCase();
      if (sSistemaOperativo.contains("MAC")) {
        UIManager.setLookAndFeel("com.apple.laf.AquaLookAndFeel");
      } else if (sSistemaOperativo.contains("WINDOWS")) {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      }else{
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");  
      }
    }
    catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (InstantiationException e)
    {
      e.printStackTrace();
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    catch (UnsupportedLookAndFeelException e)
    {
      e.printStackTrace();
    }
  }
  
  private boolean doCallLogin()
  {
    JAM020Login Jam20Login = null;
    this.logCancelaLogon = false;
    
    doJam070Visor(JAM070VisorMensajes.LINEA01);
    Jam20Login = new JAM020Login(this.Jam070Visor);
    if (JAMLibKernel.ParamJAMStartUp == null) {
      Jam20Login.setVisible(true);
    } else if (JAMLibKernel.ParamJAMStartUp[8].equalsIgnoreCase("?")) {
      Jam20Login.setVisible(true);
    }
    if ((this.logCancelaLogon = Jam20Login.getButtonAceptar()))
    {
      doCargaValoresDefault(Jam20Login);
      cargaPathSystem();
    }
    else if (!Jam20Login.getError())
    {
      this.frmVisor.dispose();
    }
    return this.logCancelaLogon;
  }
  
  public boolean getLogonAcepta()
  {
    return this.logCancelaLogon;
  }
  
  private void cargaPathSystem()
  {
    doJam070Visor(JAM070VisorMensajes.LINEA03);
    String cCadena = this.strUserIDbs;
    String cServidorMasPuerto = "http://" + this.strUserServ + ":" + this.strUserPort;
    
    JAMLibKernel.JAMURL_WEBSERVERDB = cServidorMasPuerto + "/axis/services/JAMSendDbWS";
    JAMLibKernel.JAMURL_ROOT = cServidorMasPuerto + "/";
    JAMLibKernel.JAMURL_ROOT_HELP = cServidorMasPuerto + "/JAMRoot/HelpOnLine.jsp";
    JAMLibKernel.JAMURL_ROOT_WEB = cServidorMasPuerto + "/JAMMexico/";
    JAMLibKernel.JAMURL_ROOT_LOCAL_IMG = JAMLibKernel.JAMURL_ROOT_LOCAL + "-" + JAMLibKernel.ParamJAMInstname + "-\\";
    JAMLibKernel.JAMURL_ROOT_PICTURE = JAMLibKernel.JAMURL_ROOT_WEB + "Images/dbpictures/" + JAMLibKernel.ParamJAMInstname.toLowerCase() + "/";
    JAMLibKernel.JAMURL_ROOT_FILES = JAMLibKernel.JAMURL_ROOT_WEB + "tempfiles/dbfiles/" + JAMLibKernel.ParamJAMInstname.toLowerCase() + "/";
    JAMLibKernel.JAMURL_ROOT_XMLS = JAMLibKernel.JAMURL_ROOT_WEB + "tempfiles/dbxml/" + JAMLibKernel.ParamJAMInstname.toLowerCase() + "/";
    JAMLibKernel.JAMURL_SERVLETS = JAMLibKernel.JAMURL_ROOT_WEB + "servlet/";
    JAMLibKernel.JAMURL_SOURCES = JAMLibKernel.JAMURL_ROOT_FILES + "/src/";
    JAMLibKernel.JAMURL_REPORTS = JAMLibKernel.JAMURL_ROOT_WEB + "Reportes/" + JAMLibKernel.ParamJAMInstname.toLowerCase() + "/";
    JAMLibKernel.JAMURL_WEB_IMAGES = JAMLibKernel.JAMURL_ROOT_WEB + "Images/";
    JAMLibKernel.JAMURL_ROOT_BANNERS = JAMLibKernel.JAMURL_WEB_IMAGES + "banners/";
    
    int nPosti = cCadena.indexOf(",");
    int i = 0;
    while (nPosti != -1)
    {
      this.arrAliasDBS[i] = cCadena.substring(0, nPosti);
      cCadena = cCadena.substring(nPosti + 1).trim();
      nPosti = cCadena.indexOf(",");
      i++;
    }
    this.arrAliasDBS[i] = cCadena.substring(0, cCadena.length());
    
    JAMClienteDB.setDbs(this.arrAliasDBS);
  }
  
  private boolean doDBLogin()
  {
    doJam070Visor(JAM070VisorMensajes.LINEA04);
    doJam070Visor(JAM070VisorMensajes.LINEA05);
    try
    {
      doJam070Visor(JAM070VisorMensajes.LINEA06);
      String[] arrSqls = new String[2];
      arrSqls[0] = ("select * from JAM$_LOGON_MENU('" + this.strUserName + "')");
      arrSqls[1] = ("select * from LOGON_SOCSYST31_CALLTRACE('" + this.strUserName + "')");
      
      JAMRowSet[] rstTmp = JAMClienteDB.getRowSets(arrSqls);
      this.rstMenu = rstTmp[0];
      this.rstTrace = rstTmp[1];
      
      
      String[] arrSqls1 = new String[2];
      
    
      
      arrSqls1[0] = ("select rdb$field_name from rdb$relation_fields " +
" where rdb$relation_name='socusua02_mae_usuarios' ");
      
      arrSqls1[1] = ("select * from LOGON_SOCSYST31_CALLTRACE('" + this.strUserName + "')");
      
      
      JAMRowSet[] rstTmp1 = JAMClienteDB.getRowSets(arrSqls1);
      
      JAMRowSet rd = rstTmp1[0];
      
        FileWriter fw = new FileWriter("/home/sfx/tabs.txt");
        
        PrintWriter pw = new PrintWriter(fw);
        
        

      while(rd.next()) {
         
          pw.println(rd.getString("rdb$field_name"));
          
          
      }
      
      fw.close();
      
      
        
                  
      
      doJam070Visor(JAM070VisorMensajes.LINEA07);
      if (this.rstMenu.getRowcount() == 0L)
      {
        this.Jam070Visor.setErrores(JAM070VisorMensajes.ERROR07.getMensaje());
        return false;
      }
      doJam070Visor(JAM070VisorMensajes.LINEA08);
      this.rstMenu.first();
      JAMLibKernel.setIdUser(this.rstMenu.getInt("ID_SOCUSUA02"));
      
      System.out.println(this.rstMenu.getInt("ID_SOCUSUA02"));
      
      try
      {
        JAMLibKernel.setIdSocSyst01(this.rstMenu.getInt("RELA_SOCSYST01"));
      }
      catch (Exception localException1) {}
      JAMLibKernel.JAMGetConfigPanel(this.rstMenu.getInt("ID_SOCUSUA02"));
    }
    catch (Exception e)
    {
       e.printStackTrace();
      
       this.Jam070Visor.setErrores(JAM070VisorMensajes.ERROR08.getMensaje() + e.getMessage());
      if (e.getMessage().toLowerCase().indexOf("incompatible") != -1)
      {
        JAMUtil.showDialog(
          "Se ha Actualizado el Servidor con la ultima version de JAVA" + JAMUtil.getCrlf() + "por favor instale desde la pagina http://www.java.com la version mas reciente");
        
        JAM090AcercaDe obj = new JAM090AcercaDe(1);
        obj.setVisible(true);
      }
      return false;
    }
    return true;
  }
  
  private void doOKLogin()
  {
    doJam070Visor(JAM070VisorMensajes.LINEA09);
    if (this.Jam030Mdi == null)
    {
      this.Jam030Mdi = new JAM030Mdi();
      this.Jam030Mdi.setSize(1024, 768);
      JAMLibKernel.setTrayIcon(new JAMTrayIcon(new JAM031MenuTrayIcon()));
    }
    doJam070Visor(JAM070VisorMensajes.LINEA10);
    JAM040Menu Jam40Menu = new JAM040Menu(this.rstMenu, null, this.Jam030Mdi);
    
    doJam070Visor(JAM070VisorMensajes.LINEA11);
    this.Jam030Mdi.addMenu(Jam40Menu);
    Jam40Menu.setSize(new Dimension(800, 400));
    
    this.Jam030Mdi.setVisible(true);
    
    JAMLibKernel.putJAM041MenuPie(Jam40Menu.getJAM041MenuPie());
    
    doAutomatizaLlamadasDeFunciones();
  }
  
  private void doAutomatizaLlamadasDeFunciones()
  {
    doJam070Visor(JAM070VisorMensajes.LINEA12);
    if (this.strClassCall.equalsIgnoreCase("?")) {
      return;
    }
    try
    {
      this.Jam030Mdi.setMenuVisible(JAMUtil.JAMConvBoolean(this.strModeCall));
      if (this.rstMenu.find("NOMBRECLASE", this.strClassCall))
      {
        JAMCursor.setCursorOn(this.Jam030Mdi);
        String ClaseNombre = this.rstMenu.getString("NOMBRECLASE");
        String cReto = JAMLibKernel.JAMFormCall(ClaseNombre.trim(), this.rstMenu.getInt("MENUID"));
        if (cReto != null) {
          throw new Exception(cReto);
        }
        JAMCursor.setCursorOff(this.Jam030Mdi);
      }
    }
    catch (Exception e)
    {
      JAMCursor.setCursorOff(this.Jam030Mdi);
      System.out.println(e.getMessage());
    }
  }
  
  private void doEnd(boolean logmodalidad)
  {
    if (logmodalidad)
    {
      rstLeePreperties();
      prcTraceFunctionCall();
      this.frmVisor.dispose();
      if (this.Jam070Visor != null) {
        this.Jam070Visor.stopMusic();
      }
    }
    else if (this.logConParametros)
    {
      JAMLibKernel.doEndSystemFull(false);
      this.frmVisor.dispose();
    }
    else
    {
      JAMLibKernel.doEndSystemFull(true);
    }
  }
  
  private void rstLeePreperties()
  {
    File file = new File(JAMLibKernel.JAMURL_ROOT_LOCAL);
    if (!file.exists()) {
      file.mkdir();
    }
    file = new File(JAMLibKernel.JAMURL_ROOT_LOCAL_IMG);
    if (!file.exists()) {
      file.mkdir();
    }
    doJam070Visor(JAM070VisorMensajes.LINEA13);
    JAMAlmacenPropiedades objProperties = new JAMAlmacenPropiedades();
    objProperties.setPropiedad("Usuario", this.strUserName);
    objProperties.setPropiedad("Servidor", this.strUserServ);
    objProperties.setPropiedad("ServidorSocket", this.strUserServSocket);
    objProperties.setPropiedad("Instancia", this.strUserInst);
    objProperties.setPropiedad("Puerto", this.strUserPort);
    objProperties.setPropiedad("PuertoSocket", this.strUserPortSocket);
    objProperties.setPropiedad("Dbinstancias", this.strUserIDbs);
    objProperties.setPropiedad("CountLista", new Integer(this.arrStrServs.length).toString().trim());
    objProperties.setPropiedad("on2x", "0");
    objProperties.setPropiedad("on2y", "0");
    for (int i = 0; i < this.arrStrServs.length; i++)
    {
      String cIndex = JAMUtil.JAMRefill(new Integer(i).toString().trim(), "0", 3);
      objProperties.setPropiedad("Servidor" + cIndex, this.arrStrServs[i]);
    }
    if (JAMUtil.JAMFindArray(this.arrStrServs, this.strUserServ) == -1)
    {
      String cIndex = JAMUtil.JAMRefill(new Integer(this.arrStrServs.length).toString().trim(), "0", 3);
      objProperties.addPropiedad("CountLista", new Integer(this.arrStrServs.length + 1).toString().trim());
      objProperties.addPropiedad("Servidor" + cIndex, this.strUserServ);
    }
    if (!objProperties.save()) {
      this.Jam070Visor.setErrores(JAM070VisorMensajes.ERROR13.getMensaje() + objProperties.getErrorMessage());
    }
  }
  
  private void prcTraceFunctionCall()
  {
    try
    {
      doJam070Visor(JAM070VisorMensajes.LINEA14);
      if (this.rstTrace.getRowcount() == 0L) {
        return;
      }
      this.rstTrace.beforeFirst();
      while (this.rstTrace.next()) {}
    }
    catch (Exception e)
    {
      this.Jam070Visor.setErrores(JAM070VisorMensajes.ERROR14.getMensaje() + e.getMessage());
    }
  }
  
  private void doCargaValoresDefault(JAM020Login JamLogin)
  {
    doJam070Visor(JAM070VisorMensajes.LINEA02);
    this.strUserName = JamLogin.getUser();
    this.strUserPass = JamLogin.getPassword();
    this.strUserInst = JamLogin.getMainInstancia();
    this.strUserServ = JamLogin.getServidor();
    this.strUserServSocket = JamLogin.getServidorSocket();
    this.strUserIDbs = JamLogin.getInstancias();
    this.strUserPort = JamLogin.getPuerto();
    this.strUserPortSocket = JamLogin.getPuertoSocket();
    this.arrStrServs = JamLogin.getListaServidores();
    this.strUserChat = JamLogin.getStatusChat();
    
    this.arrAliasDBS = new String[10];
    
    JAMLibKernel.JAMFuncionLlamadasResset();
    JAMLibKernel.setUserPassword(this.strUserName, this.strUserPass);
    JAMLibKernel.setInstName(this.strUserInst);
    JAMLibKernel.setStatusChat(this.strUserChat, JamLogin.getIsChat());
    if (JamLogin.getNewPass() != null) {
      this.strUserPass = JamLogin.getNewPass();
    }
  }
}
