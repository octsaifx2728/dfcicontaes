package mx.com.jammexico.jamlogon;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javazoom.jl.player.JAMTunesPlayer;
import mx.com.jammexico.jamcomponents.JAMBrowser;
import mx.com.jammexico.jamcomponents.JAMCodeBar.JAMDemoCodeBar;
import mx.com.jammexico.jamcomponents.JAMCursor;
import mx.com.jammexico.jamcomponents.JAMLibKernel;
import mx.com.jammexico.jamcomponents.files.JAMChooserFile;
import mx.com.jammexico.jamcomponents.files.JAMLoadFile;
import mx.com.jammexico.jamcomponents.jambuttons.JAMButtonAccion;
import mx.com.jammexico.jamcomponents.jambuttons.JAMButtonAceptar;
import mx.com.jammexico.jamcomponents.jambuttons.JAMButtonAlta;
import mx.com.jammexico.jamcomponents.jambuttons.JAMButtonCancelar;
import mx.com.jammexico.jamcomponents.jambuttons.JAMButtonMusicaPlay;
import mx.com.jammexico.jamcomponents.jambuttons.JAMButtonVe;
import mx.com.jammexico.jamcomponents.jamcombo.JAMCombo;
import mx.com.jammexico.jamcomponents.jamcombo.JAMComboView;
import mx.com.jammexico.jamcomponents.jamform.JAMFormModal;
import mx.com.jammexico.jamcomponents.jamgrid.JAMGrid;
import mx.com.jammexico.jamcomponents.jamgrid.JAMGridColumn;
import mx.com.jammexico.jamcomponents.jamgrid.JAMSortedView;
import mx.com.jammexico.jamcomponents.visual.JAMInputText;
import mx.com.jammexico.jamcomponents.visual.JAMLabel;
import mx.com.jammexico.jamdb.JAMClienteDB;
import mx.com.jammexico.jamsrv.JAMDataActionsGroup;
import mx.com.jammexico.jamsrv.JAMRowSet;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAM050Config
  extends JAMFormModal
{
  private String SERVLET_PRINT = "";
  private JPanel pnlContainer1 = null;
  private JAMLabel lblLogo = null;
  private JAMLabel lblLogo1 = null;
  private JAMLabel lblLogo12 = null;
  private JAMLabel lblPaisOrigen = null;
  private JAMLabel lblPaisDestino = null;
  private JAMLabel lblIdioma = null;
  private JAMLabel lblSetNum = null;
  private JAMLabel lblCliente = null;
  private JAMLabel lblTimeOut = null;
  private JAMLabel lblVistas = null;
  private JAMLabel lblDescri = null;
  private JAMButtonAceptar btoAceptar = null;
  private JAMButtonCancelar btoCancelar = null;
  private JAMButtonAlta btoAlta = null;
  private JAMRowSet rstConfiguracion = null;
  private JAMCombo cboTimeOut = null;
  private JAMCombo cboIdioma = null;
  private JAMCombo cboSetNum = null;
  private JAMCombo cboPaisOrigen = null;
  private JAMCombo cboPaisDestino = null;
  private JAMCombo cboVistas = null;
  private JAMGrid grdVistas = null;
  private JAMButtonAccion btoPathWord = null;
  private JAMButtonAccion btoPathExcel = null;
  private JAMButtonAccion btoDirectorioDatos = null;
  private JAMButtonAccion btoBarCode = null;
  private JAMButtonAccion btoPDF = null;
  private JAMButtonAccion btoImgDeskTop = null;
  private JAMButtonAccion btoMusica = null;
  private JAMButtonMusicaPlay btoMusicaPlay = null;
  private JAMInputText txtPathWord = null;
  private JAMInputText txtPathExcel = null;
  private JAMInputText txtDirectorioDatos = null;
  private JAMInputText txtBarCode = null;
  private JAMInputText txtPathPdf = null;
  private JAMInputText txtCliente = null;
  private JAMInputText txtDescri = null;
  private JAMInputText txtImgDeskTop = null;
  private JAMInputText txtMusicaDeskTop = null;
  private Vector vctSetNum = new Vector(2);
  private int[] indTimeOut = { 0, 15, 30, 45, 60 };
  private boolean flgAceptar = false;
  private boolean newImageDeskTop = false;
  private int xIDUsuario;
  private String cLastImgDeskTop = null;
  private JAMChooserFile chooser = null;
  private JAMTunesPlayer objPlayer = null;
  private JAM030Mdi objJam030Mdi = null;
  private JAMLabel lblDecimales = null;
  private JAMCombo cboDecimales = null;
  private JAMButtonVe btoVePdf = null;
  private JToggleButton chkModo = null;
  
  public boolean getButtonAceptar()
  {
    return this.flgAceptar;
  }
  
  public JAM050Config(JAM030Mdi argJam030Mdi)
    throws HeadlessException
  {
    this.xIDUsuario = JAMLibKernel.getIdUser();
    this.objJam030Mdi = argJam030Mdi;
    this.SERVLET_PRINT = (JAMLibKernel.JAMURL_SERVLETS + "JAMServletPrint?");
    initialize();
  }
  
  private void initialize()
  {
    setTitle("Configuraci����n");
    setContentPane(getPnlContainer1());
    setSize(705, 515);
    
    addInternalFrameListener(new InternalFrameAdapter()
    {
      public void internalFrameClosed(InternalFrameEvent e)
      {
        JAM050Config.this.flgAceptar = false;
      }
    });
    this.txtDescri.setEnabled(false);
    this.btoAlta.setEnabled(false);
    this.objPlayer = JAMLibKernel.getJAMTunesPLayer();
    if (JAMClienteDB.getModoConexion() == 1) {
      this.chkModo.setSelected(true);
    } else {
      this.chkModo.setSelected(false);
    }
    doEventChkModo();
    start();
  }
  
  private void start()
  {
    String[] arrSql = new String[6];
    arrSql[0] = ("SELECT * FROM VW_SOCSYST17(" + this.xIDUsuario + ")");
    
    arrSql[1] = 
    
      ("SELECT RELA_SOCUSUA50_ORIG, RELA_SOCUSUA50_DEST, RELA_SOCSYST10, ID_SOCSYST17, SOCSYST17_WORD, SOCSYST17_EXCEL, SOCSYST17_SETNUM, SOCSYST17_DATOS_I, SOCSYST17_BARCODE, SOCSYST17_TIMEOUT, SOCSYST17_PDF, SOCSYST17_IMGDESKTOP, SOCSYST17_DECIMALES FROM SOCSYST17_TBL_CONFIGURACION WHERE RELA_SOCUSUA02 = " + this.xIDUsuario);
    
    arrSql[2] = "SELECT * FROM CBO_PAISES";
    
    arrSql[3] = "SELECT ID_SOCSYST01, ENTIDAD FROM GRID_CLIENTE_EMP where SOCSYST01_PROPIETARIO = 1";
    
    arrSql[4] = "SELECT * FROM CBO_REPORTES_ASIGNADOS";
    
    arrSql[5] = "SELECT * FROM CBO_IDIOMAS";
    try
    {
      JAMRowSet[] rstTmp = JAMClienteDB.getRowSets(arrSql);
      
      JAMRowSet rstSoloVe = rstTmp[0];
      this.rstConfiguracion = rstTmp[1];
      JAMRowSet rstPaisOrigen = rstTmp[2];
      JAMRowSet rstCliente = rstTmp[3];
      JAMRowSet rstVistas = rstTmp[4];
      JAMRowSet rstIdioma = rstTmp[5];
      
      this.rstConfiguracion.first();
      rstSoloVe.first();
      
      this.cboPaisOrigen.setModel(new JAMComboView(rstPaisOrigen, "id_socusua50", "socusua50_descri"));
      this.cboPaisDestino.setModel(new JAMComboView(rstPaisOrigen, "id_socusua50", "socusua50_descri"));
      this.cboVistas.setModel(new JAMComboView(rstVistas, "ID_SOCSYST10", "SOCSYST10_FILTRO_DES", true));
      this.cboIdioma.setModel(new JAMComboView(rstIdioma, "ID_SOCSYST10", "SOCSYST10_FILTRO_DES"));
      
      this.cboPaisOrigen.positionById(rstSoloVe.getInt("RELA_SOCUSUA50_ORIG"));
      this.cboPaisDestino.positionById(rstSoloVe.getInt("RELA_SOCUSUA50_DEST"));
      this.cboIdioma.positionById(rstSoloVe.getInt("RELA_SOCSYST10"));
      
      this.cboSetNum.setSelectedIndex(JAMUtil.JAMFindVector(this.vctSetNum, rstSoloVe.getString("SOCSYST17_SETNUM").toString()));
      
      this.txtPathExcel.setText(rstSoloVe.getString("SOCSYST17_EXCEL"));
      this.txtPathWord.setText(rstSoloVe.getString("SOCSYST17_WORD"));
      this.txtPathPdf.setText(rstSoloVe.getString("SOCSYST17_PDF"));
      this.txtDirectorioDatos.setText(rstSoloVe.getString("SOCSYST17_DATOS_I"));
      this.txtBarCode.setText(rstSoloVe.getString("SOCSYST17_BARCODE"));
      if (rstSoloVe.getString("SOCSYST17_IMGDESKTOP") == null)
      {
        this.txtImgDeskTop.setText("JAMDeskTopDefault.JPG");
        this.txtImgDeskTop.setName("");
      }
      else
      {
        this.txtImgDeskTop.setText(rstSoloVe.getString("SOCSYST17_IMGDESKTOP"));
        this.txtImgDeskTop.setName(rstSoloVe.getString("SOCSYST17_IMGDESKTOP"));
      }
      this.cLastImgDeskTop = this.txtImgDeskTop.getName();
      this.cboTimeOut.setSelectedIndex(JAMUtil.JAMFindArray(this.indTimeOut, rstSoloVe.getInt("SOCSYST17_TIMEOUT")));
      this.cboDecimales.setSelectedIndex(rstSoloVe.getInt("SOCSYST17_DECIMALES"));
      
      rstCliente.first();
      this.txtCliente.setText(rstCliente.getString("ENTIDAD").trim());
    }
    catch (Exception er)
    {
      JAMUtil.showDialog("Error al Buscar la configuraci����n" + er.getMessage());
      this.flgAceptar = false;
      dispose();
    }
  }
  
  private void rstLlamaVistas(String argCodigo)
  {
    this.txtDescri.setEnabled(false);
    this.btoAlta.setEnabled(false);
    
    String[] arrSqls = new String[1];
    if (argCodigo.equalsIgnoreCase("HISTO1")) {
      arrSqls[0] = ("Select * FROM SOCUTIL03_MOV_IMPRESIONES WHERE RELA_SOCUSUA02 = " + this.xIDUsuario + " AND SOCUTIL03_DESCRIPCION IS NULL");
    } else if (argCodigo.equalsIgnoreCase("USUA1")) {
      arrSqls[0] = ("SELECT * FROM GRID_SOCSYST13_SOCSYST04(" + this.xIDUsuario + ")");
    } else if (argCodigo.equalsIgnoreCase("PERS1")) {
      arrSqls[0] = ("Select * FROM SOCUTIL03_MOV_IMPRESIONES WHERE RELA_SOCUSUA02 = " + this.xIDUsuario + " AND SOCUTIL03_DESCRIPCION IS NOT NULL");
    }
    try
    {
      JAMRowSet[] arrRowSet = JAMClienteDB.getRowSets(arrSqls);
      if (argCodigo.equalsIgnoreCase("HISTO1"))
      {
        if (arrRowSet[0].getRowcount() != 0L)
        {
          this.txtDescri.setEnabled(true);
          this.btoAlta.setEnabled(true);
        }
        rstVistaHistorico(arrRowSet[0]);
      }
      else if (argCodigo.equalsIgnoreCase("USUA1"))
      {
        rstVistaReportesAsignados(arrRowSet[0]);
      }
      else if (argCodigo.equalsIgnoreCase("PERS1"))
      {
        rstVistaReporPersonal(arrRowSet[0]);
      }
    }
    catch (Exception er)
    {
      JAMUtil.showDialog("Error al Buscar la vista" + er.getMessage());
      this.flgAceptar = false;
      dispose();
    }
  }
  
  private void rstVistaHistorico(JAMRowSet argRowSet)
    throws Exception
  {
    this.grdVistas.setSortedView(new JAMSortedView(argRowSet));
    this.grdVistas.setAllColumnsVisible(false);
    this.grdVistas.addSortKey("SOCUTIL03_FAPL");
    
    this.grdVistas.getColumn("SOCUTIL03_FUNCION").setVisible(true);
    this.grdVistas.getColumn("SOCUTIL03_COMANDO").setVisible(true);
    this.grdVistas.getColumn("SOCUTIL03_FAPL").setVisible(true);
    
    this.grdVistas.getColumn("SOCUTIL03_FUNCION").setHeader("Funci����n");
    this.grdVistas.getColumn("SOCUTIL03_COMANDO").setHeader("Comando Impresi����n");
    this.grdVistas.getColumn("SOCUTIL03_FAPL").setHeader("Fecha");
    this.grdVistas.adjustcolumnWidht("SOCUTIL03_FUNCION", 100);
    this.grdVistas.adjustcolumnWidht("SOCUTIL03_COMANDO", 450);
    this.grdVistas.adjustcolumnWidht("SOCUTIL03_FAPL", 80);
    this.grdVistas.setAllowDelete(true);
  }
  
  private void rstVistaReportesAsignados(JAMRowSet argRowSet)
    throws Exception
  {
    this.grdVistas.setSortedView(new JAMSortedView(argRowSet));
    this.grdVistas.setAllColumnsVisible(false);
    this.grdVistas.addSortKey("SOCSYST13_DESCRI");
    
    this.grdVistas.getColumn("SOCSYST13_DESCRI").setVisible(true);
    this.grdVistas.getColumn("MENUTITULO").setVisible(true);
    
    this.grdVistas.getColumn("SOCSYST13_DESCRI").setHeader("Reportes");
    this.grdVistas.getColumn("MENUTITULO").setHeader("Modulo Asignado");
    
    this.grdVistas.adjustcolumnWidht("SOCSYST13_DESCRI", 300);
    this.grdVistas.adjustcolumnWidht("MENUTITULO", 300);
    this.grdVistas.setAllowDelete(true);
  }
  
  private void rstVistaReporPersonal(JAMRowSet argRowSet)
    throws Exception
  {
    this.grdVistas.setSortedView(new JAMSortedView(argRowSet));
    this.grdVistas.setAllColumnsVisible(false);
    this.grdVistas.addSortKey("SOCUTIL03_FAPL");
    
    this.grdVistas.getColumn("SOCUTIL03_DESCRIPCION").setVisible(true);
    this.grdVistas.getColumn("SOCUTIL03_FUNCION").setVisible(true);
    this.grdVistas.getColumn("SOCUTIL03_COMANDO").setVisible(true);
    this.grdVistas.getColumn("SOCUTIL03_FAPL").setVisible(true);
    
    this.grdVistas.getColumn("SOCUTIL03_DESCRIPCION").setHeader("Descripci����n");
    this.grdVistas.getColumn("SOCUTIL03_FUNCION").setHeader("Funci����n");
    this.grdVistas.getColumn("SOCUTIL03_COMANDO").setHeader("Comando Impresi����n");
    this.grdVistas.getColumn("SOCUTIL03_FAPL").setHeader("Fecha");
    
    this.grdVistas.adjustcolumnWidht("SOCUTIL03_DESCRIPCION", 400);
    this.grdVistas.adjustcolumnWidht("SOCUTIL03_FUNCION", 100);
    this.grdVistas.adjustcolumnWidht("SOCUTIL03_COMANDO", 450);
    this.grdVistas.adjustcolumnWidht("SOCUTIL03_FAPL", 80);
    this.grdVistas.setAllowDelete(true);
  }
  
  public boolean getNewLoadImagenDeskTop()
  {
    return this.newImageDeskTop;
  }
  
  private JPanel getPnlContainer1()
  {
    if (this.pnlContainer1 == null)
    {
      this.lblLogo12 = new JAMLabel(1);
      this.lblLogo12.setBounds(new Rectangle(5, 180, 66, 71));
      this.lblLogo12.setHorizontalAlignment(0);
      this.lblLogo12.setHorizontalTextPosition(0);
      this.lblLogo12.setIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/jamvos/jamtucan.png")));
      this.lblLogo12.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 1));
      this.pnlContainer1 = new JPanel();
      this.pnlContainer1.setLayout(null);
      
      this.lblDescri = new JAMLabel(1);
      this.lblDescri.setBounds(new Rectangle(5, 415, 91, 21));
      this.lblDescri.setText(" Personalizar :");
      
      this.lblVistas = new JAMLabel(1);
      this.lblVistas.setBounds(new Rectangle(5, 255, 176, 21));
      this.lblVistas.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 1));
      this.lblVistas.setText(" Consultas :");
      
      this.lblTimeOut = new JAMLabel(1);
      this.lblTimeOut.setBounds(new Rectangle(530, 180, 81, 22));
      this.lblTimeOut.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 1));
      this.lblTimeOut.setText(" Tiempo Out :");
      
      this.lblCliente = new JAMLabel(1);
      this.lblCliente.setBounds(new Rectangle(5, 5, 161, 21));
      this.lblCliente.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 1));
      this.lblCliente.setText(" Propietario :");
      
      this.lblSetNum = new JAMLabel(1);
      this.lblSetNum.setBounds(new Rectangle(430, 55, 91, 21));
      this.lblSetNum.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 1));
      this.lblSetNum.setText(" Set. Num����rico :");
      
      this.lblIdioma = new JAMLabel(1);
      this.lblIdioma.setBounds(new Rectangle(430, 30, 91, 21));
      this.lblIdioma.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 1));
      this.lblIdioma.setText(" Idioma :");
      
      this.lblPaisDestino = new JAMLabel(1);
      this.lblPaisDestino.setBounds(new Rectangle(75, 55, 91, 21));
      this.lblPaisDestino.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 1));
      this.lblPaisDestino.setText(" Pa����s Destino :");
      
      this.lblPaisOrigen = new JAMLabel(1);
      this.lblPaisOrigen.setBounds(new Rectangle(75, 30, 91, 21));
      this.lblPaisOrigen.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 1));
      this.lblPaisOrigen.setText(" Pa����s Origen :");
      
      this.pnlContainer1.add(this.lblPaisOrigen, null);
      this.pnlContainer1.add(this.lblPaisDestino, null);
      this.pnlContainer1.add(this.lblIdioma, null);
      this.pnlContainer1.add(this.lblSetNum, null);
      this.pnlContainer1.add(this.lblCliente, null);
      this.pnlContainer1.add(this.lblTimeOut, null);
      this.pnlContainer1.add(this.lblVistas, null);
      this.pnlContainer1.add(this.lblDescri, null);
      
      this.pnlContainer1.add(getLblLogo(), null);
      this.pnlContainer1.add(getLblLogo1(), null);
      this.pnlContainer1.add(getCancelar(), null);
      this.pnlContainer1.add(getCboPaisOrigen(), null);
      this.pnlContainer1.add(getCboPaisDestino(), null);
      this.pnlContainer1.add(getCboIdioma(), null);
      this.pnlContainer1.add(getCboSetNum(), null);
      this.pnlContainer1.add(getBtoPathExcel(), null);
      this.pnlContainer1.add(getBtoAceptar(), null);
      this.pnlContainer1.add(getBtoPathWord(), null);
      this.pnlContainer1.add(getTxtPathWord(), null);
      this.pnlContainer1.add(getTxtPathExcel(), null);
      this.pnlContainer1.add(getGrdVistas(), null);
      this.pnlContainer1.add(getBtoDirectorioDatos(), null);
      this.pnlContainer1.add(getTxtDirectorioDatos(), null);
      this.pnlContainer1.add(getBtoBarCode(), null);
      this.pnlContainer1.add(getTxtBarCode(), null);
      this.pnlContainer1.add(getTxtCliente(), null);
      this.pnlContainer1.add(getCboTimeOut(), null);
      this.pnlContainer1.add(getCboVistas(), null);
      this.pnlContainer1.add(getBtoPDF(), null);
      this.pnlContainer1.add(getTxtDescri(), null);
      this.pnlContainer1.add(getBtoAlta(), null);
      this.pnlContainer1.add(getBtoImgDeskTop(), null);
      this.pnlContainer1.add(this.lblLogo12, null);
      this.pnlContainer1.add(getTxtImgDeskTop(), null);
      this.pnlContainer1.add(getBtoMusica(), null);
      this.pnlContainer1.add(getTxtMusicaDeskTop(), null);
      this.pnlContainer1.add(getBtoMusicaPlay(), null);
      this.pnlContainer1.add(getLblDecimales(), null);
      this.pnlContainer1.add(getCboDecimales(), null);
      this.pnlContainer1.add(getBtoVePdf(), null);
      this.pnlContainer1.add(getTxtPathPdf(), null);
      this.pnlContainer1.add(getChkModo(), null);
    }
    return this.pnlContainer1;
  }
  
  private JAMLabel getLblLogo()
  {
    if (this.lblLogo == null)
    {
      this.lblLogo = new JAMLabel(1);
      this.lblLogo.setBounds(new Rectangle(5, 105, 66, 71));
      this.lblLogo.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 1));
      try
      {
        this.lblLogo.setIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/jamvos/vosmari.png")));
      }
      catch (Exception localException) {}
      this.lblLogo.setHorizontalTextPosition(0);
      this.lblLogo.setHorizontalAlignment(0);
      this.lblLogo.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 1));
    }
    return this.lblLogo;
  }
  
  private JAMLabel getLblLogo1()
  {
    if (this.lblLogo1 == null)
    {
      this.lblLogo1 = new JAMLabel(1);
      this.lblLogo1.setBounds(new Rectangle(5, 30, 66, 71));
      this.lblLogo1.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 1));
      try
      {
        this.lblLogo1.setIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/jamvos/on2delfin.png")));
      }
      catch (Exception localException) {}
      this.lblLogo1.setHorizontalTextPosition(0);
      this.lblLogo1.setHorizontalAlignment(0);
      this.lblLogo1.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 1));
    }
    return this.lblLogo1;
  }
  
  private JAMButtonAceptar getBtoAceptar()
  {
    if (this.btoAceptar == null)
    {
      this.btoAceptar = new JAMButtonAceptar();
      this.btoAceptar.setBounds(new Rectangle(565, 440, 111, 26));
      this.btoAceptar.setHorizontalAlignment(0);
      this.btoAceptar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          if ((JAM050Config.this.cboPaisOrigen.getId() == -1) || (JAM050Config.this.cboPaisDestino.getId() == -1))
          {
            JAMUtil.showDialogInf("Debe Seleccionar Pa����s Origen y Destino");
            return;
          }
          if (JAM050Config.this.cboIdioma.getId() == -1)
          {
            JAMUtil.showDialogInf("Debe Seleccionar el Idioma");
            return;
          }
          try
          {
            JAMCursor.setCursorOn(JAM050Config.this.btoAceptar);
            
            JAM050Config.this.rstConfiguracion.moveToCurrentRow();
            JAM050Config.this.rstConfiguracion.updateInt("RELA_SOCUSUA50_ORIG", JAM050Config.this.cboPaisOrigen.getId());
            JAM050Config.this.rstConfiguracion.updateInt("RELA_SOCUSUA50_DEST", JAM050Config.this.cboPaisDestino.getId());
            JAM050Config.this.rstConfiguracion.updateInt("RELA_SOCSYST10", JAM050Config.this.cboIdioma.getId());
            JAM050Config.this.rstConfiguracion.updateString("SOCSYST17_WORD", JAM050Config.this.txtPathWord.getText());
            JAM050Config.this.rstConfiguracion.updateString("SOCSYST17_EXCEL", JAM050Config.this.txtPathExcel.getText());
            JAM050Config.this.rstConfiguracion.updateString("SOCSYST17_PDF", JAM050Config.this.txtPathPdf.getText());
            JAM050Config.this.rstConfiguracion.updateString("SOCSYST17_DATOS_I", JAM050Config.this.txtDirectorioDatos.getText());
            JAM050Config.this.rstConfiguracion.updateString("SOCSYST17_SETNUM", JAM050Config.this.cboSetNum.getSelectedItem().toString());
            JAM050Config.this.rstConfiguracion.updateString("SOCSYST17_BARCODE", JAM050Config.this.txtBarCode.getText());
            if (JAM050Config.this.txtImgDeskTop.getName().equalsIgnoreCase("")) {
              JAM050Config.this.rstConfiguracion.updateNull("SOCSYST17_IMGDESKTOP");
            } else {
              JAM050Config.this.rstConfiguracion.updateString("SOCSYST17_IMGDESKTOP", JAM050Config.this.txtImgDeskTop.getName());
            }
            JAM050Config.this.rstConfiguracion.updateInt("SOCSYST17_TIMEOUT", JAM050Config.this.indTimeOut[JAM050Config.this.cboTimeOut.getSelectedIndex()]);
            JAM050Config.this.rstConfiguracion.updateInt("SOCSYST17_DECIMALES", JAM050Config.this.cboDecimales.getSelectedIndex());
            JAM050Config.this.rstConfiguracion.updateRow();
            
            JAMDataActionsGroup write = new JAMDataActionsGroup();
            
            write.addAction(JAM050Config.this.rstConfiguracion, new String[] { "NAME=CONFIG;ID_SOCSYST17" });
            if (JAM050Config.this.cboVistas.getId() != -1)
            {
              JAMRowSet rstPoolSrv = JAM050Config.this.grdVistas.getInternalRowSet();
              if ((JAM050Config.this.cboVistas.getRowset().getString("socsyst10_codigo").equalsIgnoreCase("HISTO1")) || 
                (JAM050Config.this.cboVistas.getRowset().getString("socsyst10_codigo").equalsIgnoreCase("PERS1"))) {
                write.addAction(rstPoolSrv, new String[] { "NAME=MOVPOOL;ID_SOCUTIL03" });
              }
            }
            JAMClienteDB.setTransaction(write);
            if ((!JAM050Config.this.cLastImgDeskTop.equalsIgnoreCase(JAM050Config.this.txtImgDeskTop.getText())) && 
              (!JAM050Config.this.txtImgDeskTop.getName().equalsIgnoreCase("")))
            {
              JAMLoadFile uploadfile = new JAMLoadFile(JAM050Config.this.txtImgDeskTop.getText());
              uploadfile.JAMSetNewName("IMGDT_" + JAMLibKernel.ParamJAMUsersyst);
              if (!uploadfile.JAMStar())
              {
                JAMCursor.setCursorOff(JAM050Config.this.btoAceptar);
                JAMUtil.showDialog(uploadfile.JAMGetMsgServer());
              }
              else
              {
                String cTipoArchivo = JAMUtil.JAMFindeStr(JAM050Config.this.txtImgDeskTop.getName().trim(), true, ".").toUpperCase();
                JAMLibKernel.setImgDeskTop("IMGDT_" + JAMLibKernel.ParamJAMUsersyst + "." + cTipoArchivo);
                JAM050Config.this.newImageDeskTop = true;
              }
            }
            JAMLibKernel.setDecimalesDefault(JAM050Config.this.cboDecimales.getSelectedIndex() + 2);
            JAMCursor.setCursorOff(JAM050Config.this.btoAceptar);
          }
          catch (Exception er)
          {
            JAMCursor.setCursorOff(JAM050Config.this.btoAceptar);
            JAMUtil.showDialog("Error al Intentar grabar la Configuraci����n " + er.getMessage());
            return;
          }
          JAMLibKernel.setPathPdf(JAM050Config.this.txtPathPdf.getText());
          if (JAM050Config.this.objPlayer != null) {
            JAM050Config.this.objPlayer.TunesStop();
          }
          JAM050Config.this.flgAceptar = true;
          if (JAM050Config.this.getNewLoadImagenDeskTop()) {
            JAM050Config.this.objJam030Mdi.setImageDeskTop();
          }
          JAM050Config.this.dispose();
        }
      });
    }
    return this.btoAceptar;
  }
  
  private JAMButtonCancelar getCancelar()
  {
    if (this.btoCancelar == null)
    {
      this.btoCancelar = new JAMButtonCancelar();
      this.btoCancelar.setBounds(new Rectangle(440, 440, 121, 26));
      this.btoCancelar.setHorizontalAlignment(0);
      this.btoCancelar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAM050Config.this.flgAceptar = false;
          JAM050Config.this.dispose();
        }
      });
    }
    return this.btoCancelar;
  }
  
  private JAMCombo getCboPaisOrigen()
  {
    if (this.cboPaisOrigen == null)
    {
      this.cboPaisOrigen = new JAMCombo();
      this.cboPaisOrigen.setBounds(new Rectangle(170, 30, 256, 21));
    }
    return this.cboPaisOrigen;
  }
  
  private JAMCombo getCboPaisDestino()
  {
    if (this.cboPaisDestino == null)
    {
      this.cboPaisDestino = new JAMCombo();
      this.cboPaisDestino.setBounds(new Rectangle(170, 55, 256, 21));
    }
    return this.cboPaisDestino;
  }
  
  private JAMCombo getCboIdioma()
  {
    if (this.cboIdioma == null)
    {
      this.cboIdioma = new JAMCombo();
      this.cboIdioma.setBounds(new Rectangle(525, 30, 151, 21));
    }
    return this.cboIdioma;
  }
  
  private JAMButtonAccion getBtoPathWord()
  {
    if (this.btoPathWord == null)
    {
      this.btoPathWord = new JAMButtonAccion();
      this.btoPathWord.setBounds(new Rectangle(75, 80, 191, 21));
      this.btoPathWord.setHorizontalAlignment(2);
      this.btoPathWord.setText("Procesador de Texto");
      this.btoPathWord.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAM050Config.this.chooser = new JAMChooserFile();
          JAM050Config.this.chooser.JAMsetFileFilter(new String[] { "exe" }, new String[] { "Programa" });
          JAM050Config.this.chooser.JAMSetPath(JAM050Config.this.txtPathWord.getText());
          JAM050Config.this.chooser.JAMshowOpenDialog(false);
          JAM050Config.this.txtPathWord.setText(JAM050Config.this.chooser.JAMGetAbsolutePath());
        }
      });
    }
    return this.btoPathWord;
  }
  
  private JAMInputText getTxtPathWord()
  {
    if (this.txtPathWord == null)
    {
      this.txtPathWord = new JAMInputText(100);
      this.txtPathWord.setBounds(new Rectangle(270, 80, 406, 21));
    }
    return this.txtPathWord;
  }
  
  private JAMButtonAccion getBtoPathExcel()
  {
    if (this.btoPathExcel == null)
    {
      this.btoPathExcel = new JAMButtonAccion();
      this.btoPathExcel.setBounds(new Rectangle(75, 105, 191, 21));
      this.btoPathExcel.setHorizontalAlignment(2);
      this.btoPathExcel.setText("Hoja de C����lculo");
      this.btoPathExcel.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAM050Config.this.chooser = new JAMChooserFile();
          JAM050Config.this.chooser.JAMsetFileFilter(new String[] { "exe" }, new String[] { "Programa" });
          JAM050Config.this.chooser.JAMSetPath(JAM050Config.this.txtPathExcel.getText());
          JAM050Config.this.chooser.JAMshowOpenDialog(false);
          JAM050Config.this.txtPathExcel.setText(JAM050Config.this.chooser.JAMGetAbsolutePath());
        }
      });
    }
    return this.btoPathExcel;
  }
  
  private JAMInputText getTxtPathExcel()
  {
    if (this.txtPathExcel == null)
    {
      this.txtPathExcel = new JAMInputText(100);
      this.txtPathExcel.setBounds(new Rectangle(270, 105, 406, 21));
    }
    return this.txtPathExcel;
  }
  
  private JAMCombo getCboSetNum()
  {
    if (this.cboSetNum == null)
    {
      this.cboSetNum = new JAMCombo();
      this.cboSetNum.setBounds(new Rectangle(525, 55, 151, 21));
      this.vctSetNum.add("Americano");
      this.vctSetNum.add("Europeo");
      this.cboSetNum.addItem(JAMUtil.JAMAddCombos(this.vctSetNum.get(0).toString()));
      this.cboSetNum.addItem(JAMUtil.JAMAddCombos(this.vctSetNum.get(1).toString()));
    }
    return this.cboSetNum;
  }
  
  private JAMGrid getGrdVistas()
  {
    if (this.grdVistas == null)
    {
      this.grdVistas = new JAMGrid();
      this.grdVistas.setBounds(new Rectangle(5, 280, 671, 131));
      this.grdVistas.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 2));
      JTable zxGrid = this.grdVistas.retornaGrilla();
      zxGrid.addMouseListener(new MouseAdapter()
      {
        public void mouseClicked(MouseEvent e)
        {
          try
          {
            if ((e.getClickCount() == 2) && 
              (!JAM050Config.this.cboVistas.getRowset().getString("socsyst10_codigo").equalsIgnoreCase("USUA1"))) {
              JAMBrowser.displayURL(JAM050Config.this.SERVLET_PRINT + JAM050Config.this.grdVistas.getRowset().getString("SOCUTIL03_COMANDO"));
            }
          }
          catch (Exception r)
          {
            JAMUtil.showDialog("Error al Seleccionar Pool de Impresi����n. " + r.getMessage());
          }
        }
      });
    }
    return this.grdVistas;
  }
  
  private JAMButtonAccion getBtoDirectorioDatos()
  {
    if (this.btoDirectorioDatos == null)
    {
      this.btoDirectorioDatos = new JAMButtonAccion();
      this.btoDirectorioDatos.setBounds(new Rectangle(75, 130, 191, 21));
      this.btoDirectorioDatos.setHorizontalAlignment(2);
      this.btoDirectorioDatos.setText("Directorio de Datos I ");
      this.btoDirectorioDatos.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAM050Config.this.chooser = new JAMChooserFile();
          JAM050Config.this.chooser.JAMsetFileFilter(new String[] { "." }, new String[] { "Directorio" });
          JAM050Config.this.chooser.JAMSetPath(JAM050Config.this.txtDirectorioDatos.getText());
          JAM050Config.this.chooser.JAMshowOpenDialog(false);
          JAM050Config.this.txtDirectorioDatos.setText(JAM050Config.this.chooser.JAMGetAbsolutePath());
        }
      });
    }
    return this.btoDirectorioDatos;
  }
  
  private JAMInputText getTxtDirectorioDatos()
  {
    if (this.txtDirectorioDatos == null)
    {
      this.txtDirectorioDatos = new JAMInputText(100);
      this.txtDirectorioDatos.setBounds(new Rectangle(270, 130, 406, 21));
    }
    return this.txtDirectorioDatos;
  }
  
  private JAMButtonAccion getBtoBarCode()
  {
    if (this.btoBarCode == null)
    {
      this.btoBarCode = new JAMButtonAccion();
      this.btoBarCode.setBounds(new Rectangle(75, 180, 191, 21));
      this.btoBarCode.setHorizontalTextPosition(4);
      this.btoBarCode.setHorizontalAlignment(2);
      this.btoBarCode.setText("JAMBarCode");
      this.btoBarCode.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAMDemoCodeBar objBarCode = new JAMDemoCodeBar();
          objBarCode.setVisible(true);
          JAM050Config.this.txtBarCode.setText(objBarCode.getTipoCodeDescri());
        }
      });
    }
    return this.btoBarCode;
  }
  
  private JAMInputText getTxtBarCode()
  {
    if (this.txtBarCode == null)
    {
      this.txtBarCode = new JAMInputText(50);
      this.txtBarCode.setBounds(new Rectangle(270, 180, 256, 21));
      this.txtBarCode.setEditable(false);
    }
    return this.txtBarCode;
  }
  
  private JAMInputText getTxtCliente()
  {
    if (this.txtCliente == null)
    {
      this.txtCliente = new JAMInputText(100);
      this.txtCliente.setBounds(new Rectangle(170, 5, 506, 21));
      this.txtCliente.setEditable(false);
    }
    return this.txtCliente;
  }
  
  private JAMCombo getCboTimeOut()
  {
    if (this.cboTimeOut == null)
    {
      this.cboTimeOut = new JAMCombo();
      this.cboTimeOut.setBounds(new Rectangle(615, 180, 61, 21));
      this.cboTimeOut.addItem(JAMUtil.JAMAddCombos("00"));
      this.cboTimeOut.addItem(JAMUtil.JAMAddCombos("15"));
      this.cboTimeOut.addItem(JAMUtil.JAMAddCombos("30"));
      this.cboTimeOut.addItem(JAMUtil.JAMAddCombos("45"));
      this.cboTimeOut.addItem(JAMUtil.JAMAddCombos("60"));
      this.cboTimeOut.setSelectedIndex(4);
    }
    return this.cboTimeOut;
  }
  
  private JAMCombo getCboVistas()
  {
    if (this.cboVistas == null)
    {
      this.cboVistas = new JAMCombo();
      this.cboVistas.setBounds(new Rectangle(185, 255, 456, 21));
      this.cboVistas.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          try
          {
            JAM050Config.this.rstLlamaVistas(JAM050Config.this.cboVistas.getRowset().getString("socsyst10_codigo"));
          }
          catch (Exception ed)
          {
            JAMUtil.showDialog("Error Al Consultar Vistas : " + ed.getMessage());
          }
        }
      });
    }
    return this.cboVistas;
  }
  
  private JAMButtonAccion getBtoPDF()
  {
    if (this.btoPDF == null)
    {
      this.btoPDF = new JAMButtonAccion();
      this.btoPDF.setBounds(new Rectangle(75, 155, 191, 21));
      this.btoPDF.setHorizontalAlignment(2);
      this.btoPDF.setText("Adobe Acrobat ");
      this.btoPDF.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAM050Config.this.chooser = new JAMChooserFile();
          JAM050Config.this.chooser.JAMsetFileFilter(new String[] { "exe" }, new String[] { "Programa" });
          JAM050Config.this.chooser.JAMSetPath(JAM050Config.this.txtPathPdf.getText());
          JAM050Config.this.chooser.JAMshowOpenDialog(false);
          JAM050Config.this.txtPathPdf.setText(JAM050Config.this.chooser.JAMGetAbsolutePath());
          JAMLibKernel.setPathPdf(JAM050Config.this.chooser.JAMGetAbsolutePath());
        }
      });
    }
    return this.btoPDF;
  }
  
  private JAMInputText getTxtPathPdf()
  {
    if (this.txtPathPdf == null)
    {
      this.txtPathPdf = new JAMInputText(100);
      this.txtPathPdf.setBounds(new Rectangle(270, 155, 366, 21));
    }
    return this.txtPathPdf;
  }
  
  private JAMInputText getTxtDescri()
  {
    if (this.txtDescri == null)
    {
      this.txtDescri = new JAMInputText(100);
      this.txtDescri.setBounds(new Rectangle(100, 415, 531, 21));
    }
    return this.txtDescri;
  }
  
  private JAMButtonAlta getBtoAlta()
  {
    if (this.btoAlta == null)
    {
      this.btoAlta = new JAMButtonAlta();
      this.btoAlta.setBounds(new Rectangle(635, 415, 41, 21));
      this.btoAlta.setText("");
      this.btoAlta.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          if (JAM050Config.this.txtDescri.getText().trim().equalsIgnoreCase(""))
          {
            JAMUtil.showDialog("No puede Ingresar una Descripci����n Vacia");
            return;
          }
          if (JAM050Config.this.grdVistas.getSelectedRows().length != 1)
          {
            JAMUtil.showDialog("Debe Seleccionar un Reporte Historico");
            return;
          }
          try
          {
            JAMRowSet rstPoolSrv = JAM050Config.this.grdVistas.getInternalRowSet();
            rstPoolSrv.moveToCurrentRow();
            rstPoolSrv.updateString("SOCUTIL03_DESCRIPCION", JAM050Config.this.txtDescri.getText());
            rstPoolSrv.updateRow();
            JAM050Config.this.txtDescri.setText("");
          }
          catch (Exception localException) {}
        }
      });
    }
    return this.btoAlta;
  }
  
  private JAMButtonAccion getBtoImgDeskTop()
  {
    if (this.btoImgDeskTop == null)
    {
      this.btoImgDeskTop = new JAMButtonAccion();
      this.btoImgDeskTop.setBounds(new Rectangle(75, 205, 191, 21));
      this.btoImgDeskTop.setHorizontalTextPosition(4);
      this.btoImgDeskTop.setHorizontalAlignment(2);
      this.btoImgDeskTop.setText("Image DeskTop");
      this.btoImgDeskTop.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAM050Config.this.chooser = new JAMChooserFile();
          
          JAM050Config.this.chooser.JAMsetFileFilter(new String[] { "png", "tif", "gif", "jpg" }, 
            new String[] { "Formato Compacto", "Formato Scanner", "Intercambio de Gr����ficos", "Formato JPEG" });
          
          JAM050Config.this.chooser.JAMSetModalidad(JAM050Config.this.chooser.ACT_ARCHIVO);
          JAM050Config.this.chooser.JAMshowOpenDialog(false);
          if (!JAM050Config.this.chooser.JAMGetCancela())
          {
            JAM050Config.this.txtImgDeskTop.setText(JAM050Config.this.chooser.JAMGetAbsolutePath());
            JAM050Config.this.txtImgDeskTop.setName(JAM050Config.this.chooser.JAMGetJAMArchivo());
          }
        }
      });
    }
    return this.btoImgDeskTop;
  }
  
  private JAMInputText getTxtImgDeskTop()
  {
    if (this.txtImgDeskTop == null)
    {
      this.txtImgDeskTop = new JAMInputText();
      this.txtImgDeskTop.setBounds(new Rectangle(270, 205, 256, 21));
      this.txtImgDeskTop.setEditable(false);
    }
    return this.txtImgDeskTop;
  }
  
  private JAMButtonAccion getBtoMusica()
  {
    if (this.btoMusica == null)
    {
      this.btoMusica = new JAMButtonAccion();
      this.btoMusica.setBounds(new Rectangle(75, 230, 191, 21));
      this.btoMusica.setText("Musica de Ingreso");
      this.btoMusica.setHorizontalTextPosition(4);
      this.btoMusica.setHorizontalAlignment(2);
      this.btoMusica.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAM050Config.this.chooser = new JAMChooserFile();
          JAM050Config.this.chooser.JAMsetFileFilter(new String[] { "mp4", "mp3" }, 
            new String[] { "Formato Mp4", "Formato Mp3" });
          
          JAM050Config.this.chooser.JAMSetModalidad(JAM050Config.this.chooser.ACT_ARCHIVO);
          JAM050Config.this.chooser.JAMshowOpenDialog(false);
          if (!JAM050Config.this.chooser.JAMGetCancela())
          {
            JAM050Config.this.txtMusicaDeskTop.setText(JAM050Config.this.chooser.JAMGetAbsolutePath());
            JAM050Config.this.txtMusicaDeskTop.setName(JAM050Config.this.chooser.JAMGetJAMArchivo());
          }
          if (JAM050Config.this.objPlayer != null) {
            JAM050Config.this.objPlayer.TunesStop();
          }
          JAM050Config.this.objPlayer.play(JAM050Config.this.txtMusicaDeskTop.getText());
          JAM050Config.this.btoMusicaPlay.setEstado(true);
        }
      });
    }
    return this.btoMusica;
  }
  
  private JAMInputText getTxtMusicaDeskTop()
  {
    if (this.txtMusicaDeskTop == null)
    {
      this.txtMusicaDeskTop = new JAMInputText();
      this.txtMusicaDeskTop.setBounds(new Rectangle(270, 230, 371, 21));
      this.txtMusicaDeskTop.setEditable(false);
    }
    return this.txtMusicaDeskTop;
  }
  
  private JAMButtonMusicaPlay getBtoMusicaPlay()
  {
    if (this.btoMusicaPlay == null)
    {
      this.btoMusicaPlay = new JAMButtonMusicaPlay();
      this.btoMusicaPlay.setBounds(new Rectangle(645, 230, 31, 46));
      this.btoMusicaPlay.setText("");
      this.btoMusicaPlay.setEstado(false);
      this.btoMusicaPlay.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          if (JAM050Config.this.objPlayer != null) {
            if (JAM050Config.this.btoMusicaPlay.getEstado()) {
              JAM050Config.this.objPlayer.TunesPause();
            } else {
              JAM050Config.this.objPlayer.TunesResumen();
            }
          }
        }
      });
    }
    return this.btoMusicaPlay;
  }
  
  private JAMLabel getLblDecimales()
  {
    if (this.lblDecimales == null)
    {
      this.lblDecimales = new JAMLabel(1);
      this.lblDecimales.setBounds(new Rectangle(530, 205, 81, 21));
      this.lblDecimales.setText("Decimales :");
    }
    return this.lblDecimales;
  }
  
  private JAMCombo getCboDecimales()
  {
    if (this.cboDecimales == null)
    {
      this.cboDecimales = new JAMCombo();
      this.cboDecimales.setBounds(new Rectangle(615, 205, 61, 21));
      this.cboDecimales.addItem(JAMUtil.JAMAddCombos("2"));
      this.cboDecimales.addItem(JAMUtil.JAMAddCombos("3"));
      this.cboDecimales.addItem(JAMUtil.JAMAddCombos("4"));
      this.cboDecimales.addItem(JAMUtil.JAMAddCombos("5"));
      this.cboDecimales.addItem(JAMUtil.JAMAddCombos("6"));
    }
    return this.cboDecimales;
  }
  
  private JAMButtonVe getBtoVePdf()
  {
    if (this.btoVePdf == null)
    {
      this.btoVePdf = new JAMButtonVe();
      this.btoVePdf.setBounds(new Rectangle(640, 155, 34, 21));
      this.btoVePdf.setText("");
      this.btoVePdf.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          try
          {
            JAMUtil.JAMRuntime(new String[] { JAMLibKernel.getPathPdf() });
          }
          catch (Exception localException) {}
        }
      });
    }
    return this.btoVePdf;
  }
  
  private JToggleButton getChkModo()
  {
    if (this.chkModo == null)
    {
      this.chkModo = new JToggleButton();
      this.chkModo.setText("Modo WebService (Estable pero Lento)");
      this.chkModo.setBounds(new Rectangle(5, 440, 431, 26));
      this.chkModo.setSelected(true);
      this.chkModo.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAM050Config.this.doEventChkModo();
        }
      });
    }
    return this.chkModo;
  }
  
  private void doEventChkModo()
  {
    if (this.chkModo.isSelected())
    {
      JAMClienteDB.setModoConexion(1);
      this.chkModo.setText("Modo WebService (Estable pero Lento)");
    }
    else
    {
      JAMClienteDB.setModoConexion(0);
      this.chkModo.setText("Modo ServLet (R��pido pero Inestable)");
    }
  }
}
