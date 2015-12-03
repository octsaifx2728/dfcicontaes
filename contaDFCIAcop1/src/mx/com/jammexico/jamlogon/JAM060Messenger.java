package mx.com.jammexico.jamlogon;

import JAMMessenger.Browser.JAMChatPanel;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import mx.com.jammexico.jamcomponents.JAMCursor;
import mx.com.jammexico.jamcomponents.JAMLibKernel;
import mx.com.jammexico.jamcomponents.events.JAMGridEditablePressedEvent;
import mx.com.jammexico.jamcomponents.events.JAMGridEditablePressedListener;
import mx.com.jammexico.jamcomponents.jambuttons.JAMButtonAccion;
import mx.com.jammexico.jamcomponents.jambuttons.JAMButtonAceptar;
import mx.com.jammexico.jamcomponents.jambuttons.JAMButtonAlta;
import mx.com.jammexico.jamcomponents.jambuttons.JAMButtonCancelar;
import mx.com.jammexico.jamcomponents.jambuttons.JAMButtonModifica;
import mx.com.jammexico.jamcomponents.jambuttons.JAMButtonVe;
import mx.com.jammexico.jamcomponents.jamcombo.JAMCombo;
import mx.com.jammexico.jamcomponents.jamcombo.JAMComboTree;
import mx.com.jammexico.jamcomponents.jamcombo.JAMComboView;
import mx.com.jammexico.jamcomponents.jamcombo.JAMHierarchicalView;
import mx.com.jammexico.jamcomponents.jamcomm.JAMComm;
import mx.com.jammexico.jamcomponents.jamcomm.SerialParameters;
import mx.com.jammexico.jamcomponents.jamform.JAMFormModal;
import mx.com.jammexico.jamcomponents.jamgrid.JAMGrid;
import mx.com.jammexico.jamcomponents.jamgrid.JAMGridColumn;
import mx.com.jammexico.jamcomponents.jamgrid.JAMGridEditable;
import mx.com.jammexico.jamcomponents.jamgrid.JAMSortedView;
import mx.com.jammexico.jamcomponents.jamtabpanel.JAMTab;
import mx.com.jammexico.jamcomponents.visual.JAMAreaTexto;
import mx.com.jammexico.jamcomponents.visual.JAMInputNumber;
import mx.com.jammexico.jamcomponents.visual.JAMInputText;
import mx.com.jammexico.jamcomponents.visual.JAMLabel;
import mx.com.jammexico.jamdb.JAMClienteDB;
import mx.com.jammexico.jamdb.JAMDataConexionesInfo;
import mx.com.jammexico.jamsrv.JAMDataActionsGroup;
import mx.com.jammexico.jamsrv.JAMRowSet;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAM060Messenger
  extends JAMFormModal
{
  public static int ORGANIZACION = 0;
  public static int CARGO = 1;
  public static int USUARIO = 2;
  public static int NINGUNO = -1;
  private JPanel pnlContainer1 = null;
  private JPanel pnlMail = null;
  private JPanel pnlBoletines = null;
  private JPanel pnlMenuButtons = null;
  private JAMButtonAceptar btoAceptar = null;
  private JAMButtonCancelar btoCancelar = null;
  private boolean flgAceptar = false;
  private JAMTab tabMenu = null;
  private JPanel pnlMessenger = null;
  private JAMGrid grdServidoresCorreos = null;
  private JAMLabel lblHost = null;
  private JAMLabel lblTtls = null;
  private JAMLabel lblPort = null;
  private JAMLabel lblAuth = null;
  private JCheckBox chkTtls = null;
  private JCheckBox chkAuth = null;
  private JAMInputNumber txtPuertoSMTP = null;
  private JAMInputText txtServidorSMTP = null;
  private JAMButtonAlta btoAlta = null;
  private JAMButtonModifica btoAlta1 = null;
  private int intIdFalso = 0;
  private JAMLabel lblDNS = null;
  private JAMInputText txtDns = null;
  private JAMLabel lblHostPop3 = null;
  private JAMInputText txtServidorPOP3 = null;
  private JAMLabel lblPort1 = null;
  private JAMInputNumber txtPuertoPOP3 = null;
  private JPanel pnlUsuarios = null;
  private JAMCombo cboUsuarios = null;
  private JAMLabel lblUsuarios = null;
  private JPanel pnlCargos = null;
  private JAMLabel lblCargos = null;
  private JAMCombo cboCargos = null;
  private JPanel pnlOrga = null;
  private JAMLabel lblOrga = null;
  private JAMComboTree cboTreeOrga = null;
  private JCheckBox chkUsuarios = null;
  private JCheckBox chkCargos = null;
  private JCheckBox chkOrga = null;
  private JAMGridEditable grdSeguimientos = null;
  private JPanel pnlDatosBoletines = null;
  private JPanel pnlTopBoletines = null;
  private JPanel pnlNotas1 = null;
  private JAMLabel lblNotas = null;
  private JAMAreaTexto txtNotas = null;
  private JPanel pnlTitulo = null;
  private JAMLabel lblTitulo = null;
  private JAMInputText txtTitulo = null;
  private int intIdFalsoBoletin = -1;
  private JPanel pnlPuertos = null;
  private JPanel pnlPortName = null;
  private JAMLabel lblPortName = null;
  private JPanel pnlDispositivo = null;
  private JAMLabel lblDispositivo = null;
  private JPanel pnlBaud = null;
  private JAMLabel lblBaud = null;
  private JAMCombo cboPortName = null;
  private JAMCombo cboDispositivo = null;
  private JAMCombo cboBaud = null;
  private JAMCombo cboFlowControlIn = null;
  private JAMCombo cboFlowControlOut = null;
  private JAMCombo cboParity = null;
  private JAMCombo cboDataBits = null;
  private JAMCombo cboStopBits = null;
  private JPanel pnlFlowControlIn = null;
  private JAMLabel lblFlowControlIn = null;
  private JPanel pnlFlowControlOut = null;
  private JAMLabel lblFlowControlOut = null;
  private JPanel pnlParity = null;
  private JAMLabel lblParity = null;
  private JPanel pnlDataBits = null;
  private JAMLabel lblDataBits = null;
  private JPanel pnlStopBits = null;
  private JAMLabel lblStopBits = null;
  private JAMRowSet rstDispositivos = null;
  private SerialParameters parameters;
  private boolean logAltaDispositivos = false;
  private JCheckBox chkBloquea = null;
  private JPanel pnlTabConexiones = null;
  private JAMGrid grdConexiones = null;
  private JPanel pnlCerrarConexiones = null;
  private JCheckBox chkSeleccionaConexiones = null;
  private JAMButtonAccion btoCierraConexiones = null;
  private JAMButtonVe btoRefrescaConexiones = null;
  private JAMGrid grdConexionesMessenger = null;
  private JPanel chkPie = null;
  private JCheckBox chkBloqueaConexiones = null;
  private JAMChatPanel objMessenger = null;
  
  public JAM060Messenger()
    throws HeadlessException
  {
    initialize();
  }
  
  public boolean getButtonAceptar()
  {
    return this.flgAceptar;
  }
  
  public JAM060Messenger(int argIdUsuario)
    throws HeadlessException
  {
    initialize();
  }
  
  private void initialize()
  {
    setTitle("Messenger");
    setContentPane(getPnlContainer1());
    setSize(656, 483);
    setContentPane(getPnlContainer1());
    setResizable(true);
    JAMUtil.setCentraDialog(this);
    
    addInternalFrameListener(new InternalFrameAdapter()
    {
      public void internalFrameClosed(InternalFrameEvent e)
      {
        JAM060Messenger.this.flgAceptar = false;
      }
    });
    start();
  }
  
  private void start()
  {
    this.tabMenu.setSelectedIndex(1);
  }
  
  private JPanel getPnlContainer1()
  {
    if (this.pnlContainer1 == null)
    {
      this.pnlContainer1 = new JPanel();
      this.pnlContainer1.setLayout(new BorderLayout());
      
      this.pnlContainer1.add(getPnlMenuButtons(), "South");
      this.pnlContainer1.add(getTabMenu(), "Center");
    }
    return this.pnlContainer1;
  }
  
  private JPanel getPnlMessenger()
  {
    if (this.pnlMessenger == null)
    {
      this.pnlMessenger = new JPanel();
      this.pnlMessenger.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlMessenger.add(getSplMessenger(), "Center");
    }
    return this.pnlMessenger;
  }
  
  private JPanel getPnlMail()
  {
    if (this.pnlMail == null)
    {
      this.lblAuth = new JAMLabel(1);
      this.lblAuth.setBounds(new Rectangle(190, 80, 136, 21));
      this.lblAuth.setText("Autentificacion :");
      this.lblAuth.setIconInput(1);
      this.lblPort = new JAMLabel(1);
      this.lblPort.setBounds(new Rectangle(475, 5, 91, 21));
      this.lblPort.setText("Puerto :");
      this.lblPort.setIconInput(1);
      this.lblTtls = new JAMLabel(1);
      this.lblTtls.setBounds(new Rectangle(5, 80, 136, 21));
      this.lblTtls.setText("Seguridad TTLS :");
      this.lblTtls.setIconInput(1);
      this.lblHost = new JAMLabel(1);
      this.lblHost.setBounds(new Rectangle(5, 5, 136, 21));
      this.lblHost.setText("Servidor SMTP :");
      this.lblHost.setIconInput(1);
      this.pnlMail = new JPanel();
      this.pnlMail.setLayout(null);
      this.pnlMail.add(getGrdServidoresCorreos(), null);
      this.pnlMail.add(this.lblHost, null);
      this.pnlMail.add(this.lblTtls, null);
      this.pnlMail.add(this.lblPort, null);
      this.pnlMail.add(this.lblAuth, null);
      this.pnlMail.add(getChkTtls(), null);
      this.pnlMail.add(getChkAuth(), null);
      this.pnlMail.add(getTxtPuertoSMTP(), null);
      this.pnlMail.add(getTxtServidorSMTP(), null);
      this.pnlMail.add(getBtoAlta1(), null);
      this.pnlMail.add(getBtoAlta(), null);
      this.pnlMail.add(getLblDNS(), null);
      this.pnlMail.add(getTxtDns(), null);
      this.pnlMail.add(getLblHostPop3(), null);
      this.pnlMail.add(getTxtServidorPOP3(), null);
      this.pnlMail.add(getLblPort1(), null);
      this.pnlMail.add(getTxtPuertoPOP3(), null);
    }
    return this.pnlMail;
  }
  
  private JPanel getPnlBoletines()
  {
    if (this.pnlBoletines == null)
    {
      this.pnlBoletines = new JPanel();
      this.pnlBoletines.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlBoletines.add(getGrdSeguimientos(), "Center");
    }
    return this.pnlBoletines;
  }
  
  private JAMButtonAceptar getBtoAceptar()
  {
    if (this.btoAceptar == null)
    {
      this.btoAceptar = new JAMButtonAceptar();
      this.btoAceptar.setHorizontalAlignment(0);
      this.btoAceptar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAM060Messenger.this.flgAceptar = true;
          try
          {
            boolean logGrabaPuertos = false;
            if ((JAM060Messenger.this.tabMenu.getSelectedIndex() == 3) && (JAM060Messenger.this.rstDispositivos != null)) {
              if ((JAM060Messenger.this.cboPortName.getSelectedIndex() != 0) && (JAM060Messenger.this.cboDispositivo.getId() != -1))
              {
                logGrabaPuertos = true;
                if (JAM060Messenger.this.logAltaDispositivos)
                {
                  JAM060Messenger.this.rstDispositivos.moveToInsertRow();
                  JAM060Messenger.this.rstDispositivos.updateInt("ID_SOCSYST45", -1);
                }
                else
                {
                  JAM060Messenger.this.rstDispositivos.moveToCurrentRow();
                }
                JAM060Messenger.this.parameters.setPortName((String)JAM060Messenger.this.cboPortName.getSelectedItem());
                JAM060Messenger.this.parameters.setBaudRate((String)JAM060Messenger.this.cboBaud.getSelectedItem());
                JAM060Messenger.this.parameters.setFlowControlIn((String)JAM060Messenger.this.cboFlowControlIn.getSelectedItem());
                JAM060Messenger.this.parameters.setFlowControlOut((String)JAM060Messenger.this.cboFlowControlOut.getSelectedItem());
                JAM060Messenger.this.parameters.setDatabits((String)JAM060Messenger.this.cboDataBits.getSelectedItem());
                JAM060Messenger.this.parameters.setStopbits((String)JAM060Messenger.this.cboStopBits.getSelectedItem());
                JAM060Messenger.this.parameters.setParity((String)JAM060Messenger.this.cboParity.getSelectedItem());
                
                JAM060Messenger.this.rstDispositivos.updateInt("RELA_SOCSYST10", JAM060Messenger.this.cboDispositivo.getId());
                JAM060Messenger.this.rstDispositivos.updateString("SOCSYST45_PORTNAME", JAM060Messenger.this.parameters.getPortName());
                JAM060Messenger.this.rstDispositivos.updateInt("SOCSYST45_BAUDRATE", JAM060Messenger.this.parameters.getBaudRate());
                JAM060Messenger.this.rstDispositivos.updateInt("SOCSYST45_FLOWCONTROLIN", JAM060Messenger.this.parameters.getFlowControlIn());
                JAM060Messenger.this.rstDispositivos.updateInt("SOCSYST45_FLOWCONTROLOUT", JAM060Messenger.this.parameters.getFlowControlOut());
                JAM060Messenger.this.rstDispositivos.updateInt("SOCSYST45_PARITY", JAM060Messenger.this.parameters.getParity());
                JAM060Messenger.this.rstDispositivos.updateInt("SOCSYST45_DATABITS", JAM060Messenger.this.parameters.getDatabits());
                JAM060Messenger.this.rstDispositivos.updateInt("SOCSYST45_STOPBITS", JAM060Messenger.this.parameters.getStopbits());
                JAM060Messenger.this.rstDispositivos.updateDateTime("SOCSYST45_FAPL", JAMLibKernel.getFecha());
                if (JAM060Messenger.this.logAltaDispositivos)
                {
                  JAM060Messenger.this.rstDispositivos.insertRow();
                  JAM060Messenger.this.rstDispositivos.moveToCurrentRow();
                }
                else
                {
                  JAM060Messenger.this.rstDispositivos.updateRow();
                }
              }
              else if (!JAM060Messenger.this.logAltaDispositivos)
              {
                logGrabaPuertos = true;
                JAM060Messenger.this.rstDispositivos.deleteRow();
              }
            }
            JAMCursor.setCursorOn(JAM060Messenger.this.btoAceptar);
            JAMDataActionsGroup write = new JAMDataActionsGroup();
            if (JAM060Messenger.this.grdServidoresCorreos.getRowset() != null) {
              write.addAction(JAM060Messenger.this.grdServidoresCorreos.getRowSetWrite(), new String[] { "NAME=SOCSYST43;ID_SOCSYST43" });
            }
            if (logGrabaPuertos) {
              write.addAction(JAM060Messenger.this.rstDispositivos, new String[] { "NAME=SOCSYST45;ID_SOCSYST45" });
            }
            write.addAction(JAM060Messenger.this.grdSeguimientos.getRowSetWrite(), new String[] { "NAME=SOCSYST50;ID_SOCSYST50" });
            
            JAMClienteDB.setTransaction(write);
          }
          catch (Exception eg)
          {
            JAMCursor.setCursorOff(JAM060Messenger.this.btoAceptar);
            JAMUtil.showDialog("No se pudieron grabar los cambios " + eg.getMessage());
          }
          JAMCursor.setCursorOff(JAM060Messenger.this.btoAceptar);
          JAM060Messenger.this.dispose();
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
      this.btoCancelar.setHorizontalAlignment(0);
      this.btoCancelar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAM060Messenger.this.flgAceptar = false;
          JAM060Messenger.this.dispose();
        }
      });
    }
    return this.btoCancelar;
  }
  
  private JAMTab getTabMenu()
  {
    if (this.tabMenu == null)
    {
      this.tabMenu = new JAMTab();
      this.tabMenu.setCursor(new Cursor(0));
      this.tabMenu.setTabPlacement(1);
      this.tabMenu.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
      this.tabMenu.addTab("Boletines", null, getPnlBoletines(), null);
      this.tabMenu.addTab("Messenger", null, getPnlMessenger(), null);
      this.tabMenu.addTab("Configura Servidores de Correo", null, getPnlMail(), null);
      this.tabMenu.addTab("Puertos", null, getPnlPuertos(), null);
      this.tabMenu.addTab("Conexiones", null, getPnlTabConexiones(), null);
      
      this.tabMenu.addChangeListener(new ChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          if (JAM060Messenger.this.tabMenu.getSelectedIndex() == 2) {
            try
            {
              if (JAM060Messenger.this.grdServidoresCorreos.getRowset() != null) {
                return;
              }
              JAMCursor.setCursorOn(JAM060Messenger.this.tabMenu);
              String[] arrSql = new String[1];
              arrSql[0] = "Select * from GRID_SOCSYST43";
              
              JAM060Messenger.this.grdServidoresCorreos.setAllowDelete(true);
              JAM060Messenger.this.grdServidoresCorreos.setSortedView(new JAMSortedView(JAMClienteDB.getRowSets(arrSql)[0], "SOCSYST43_TBL_SERVMAILS"));
              
              JAM060Messenger.this.grdServidoresCorreos.setAllColumnsVisible(false);
              
              JAM060Messenger.this.grdServidoresCorreos.getColumn("SOCSYST43_HOST").setVisible(true);
              JAM060Messenger.this.grdServidoresCorreos.getColumn("SOCSYST43_HOST_POP3").setVisible(true);
              JAM060Messenger.this.grdServidoresCorreos.getColumn("TTLS").setVisible(true);
              JAM060Messenger.this.grdServidoresCorreos.getColumn("SOCSYST43_PORT").setVisible(true);
              JAM060Messenger.this.grdServidoresCorreos.getColumn("SOCSYST43_PORT_POP3").setVisible(true);
              JAM060Messenger.this.grdServidoresCorreos.getColumn("AUTH").setVisible(true);
              
              JAM060Messenger.this.grdServidoresCorreos.getColumn("SOCSYST43_HOST").setHeader("Smtp");
              JAM060Messenger.this.grdServidoresCorreos.getColumn("SOCSYST43_HOST_POP3").setHeader("Pop3");
              JAM060Messenger.this.grdServidoresCorreos.getColumn("TTLS").setHeader("Seguridad");
              JAM060Messenger.this.grdServidoresCorreos.getColumn("SOCSYST43_PORT").setHeader("Pto. Smtp");
              JAM060Messenger.this.grdServidoresCorreos.getColumn("SOCSYST43_PORT_POP3").setHeader("Pto. Pop3");
              JAM060Messenger.this.grdServidoresCorreos.getColumn("AUTH").setHeader("Autorizaciones");
              
              JAM060Messenger.this.grdServidoresCorreos.adjustcolumnWidht("SOCSYST43_HOST", 300);
              JAM060Messenger.this.grdServidoresCorreos.adjustcolumnWidht("SOCSYST43_HOST_POP3", 300);
              JAM060Messenger.this.grdServidoresCorreos.adjustcolumnWidht("TTLS", 120);
              JAM060Messenger.this.grdServidoresCorreos.adjustcolumnWidht("SOCSYST43_PORT", 90);
              JAM060Messenger.this.grdServidoresCorreos.adjustcolumnWidht("SOCSYST43_PORT_POP3", 90);
              JAM060Messenger.this.grdServidoresCorreos.adjustcolumnWidht("AUTH", 120);
              
              JAM060Messenger.this.grdServidoresCorreos.repaint();
              JAMCursor.setCursorOff(JAM060Messenger.this.tabMenu);
              
              JAM060Messenger.this.doLimpiaGets();
            }
            catch (Exception eg)
            {
              JAMCursor.setCursorOff(JAM060Messenger.this.tabMenu);
              JAMUtil.showDialog("No se pudo recuperar la grilla de Servidores de Correo" + JAMUtil.getCrlf() + 
                eg.getMessage());
            }
          }
          if (JAM060Messenger.this.tabMenu.getSelectedIndex() == 0)
          {
            try
            {
              JAMCursor.setCursorOn(JAM060Messenger.this.tabMenu);
              JAM060Messenger.this.cboCargos.setEnabled(false);
              JAM060Messenger.this.cboTreeOrga.setEnabled(false);
              JAM060Messenger.this.cboUsuarios.setEnabled(false);
              
              String[] arrSql = new String[4];
              arrSql[0] = "select id_socusua02, '(' || socusua02_usuario || ') ' || entidad entidad from VR_ENTIDADES_ALL";
              arrSql[1] = "Select * from SOCWFLO02_TBL_CARGOS";
              arrSql[2] = "SELECT * FROM VW_SOCWFLO01_ORDER('-1')";
              arrSql[3] = "SELECT * FROM GRID_SOCSYST50";
              
              JAMRowSet[] arrRst = JAMClienteDB.getRowSets(arrSql);
              
              JAM060Messenger.this.cboUsuarios.setModel(new JAMComboView(arrRst[0], "ID_SOCUSUA02", "ENTIDAD", true));
              JAM060Messenger.this.cboCargos.setModel(new JAMComboView(arrRst[1], "ID_SOCWFLO02", "SOCWFLO02_DESCRI", true));
              JAM060Messenger.this.cboTreeOrga.SetModel(new JAMHierarchicalView("Organizacion", "ID_SOCWFLO01", "RELA_SOCWFLO01", "SOCWFLO01_DESCRI", arrRst[2]));
              
              JAM060Messenger.this.grdSeguimientos.StartUp(arrRst[3], "socsyst50_mov_boletines");
              
              JAM060Messenger.this.grdSeguimientos.getJAMGrid().addSortKey("SOCSYST50_FAPL");
              JAM060Messenger.this.grdSeguimientos.getJAMGrid().getColumn("TIPO").setHeader("Tipo");
              JAM060Messenger.this.grdSeguimientos.getJAMGrid().getColumn("ENTIDAD").setHeader("Entidad");
              JAM060Messenger.this.grdSeguimientos.getJAMGrid().getColumn("SOCSYST50_TITULO").setHeader("Titulo");
              JAM060Messenger.this.grdSeguimientos.getJAMGrid().getColumn("SOCSYST50_FAPL").setHeader("Fecha");
              
              JAM060Messenger.this.grdSeguimientos.getJAMGrid().getColumn("TIPO").setVisible(true);
              JAM060Messenger.this.grdSeguimientos.getJAMGrid().getColumn("ENTIDAD").setVisible(true);
              JAM060Messenger.this.grdSeguimientos.getJAMGrid().getColumn("SOCSYST50_TITULO").setVisible(true);
              JAM060Messenger.this.grdSeguimientos.getJAMGrid().getColumn("SOCSYST50_FAPL").setVisible(true);
              
              JAM060Messenger.this.grdSeguimientos.getJAMGrid().adjustcolumnWidht();
              JAM060Messenger.this.grdSeguimientos.getJAMGrid().repaint();
              JAMCursor.setCursorOff(JAM060Messenger.this.tabMenu);
            }
            catch (Exception e1)
            {
              JAMCursor.setCursorOff(JAM060Messenger.this.tabMenu);
              JAMUtil.showDialog("No se pudo recuperar los datos de los boletines" + JAMUtil.getCrlf() + 
                e1.getMessage());
            }
          }
          else if (JAM060Messenger.this.tabMenu.getSelectedIndex() == 3)
          {
            try
            {
              JAMCursor.setCursorOn(JAM060Messenger.this.tabMenu);
              JAM060Messenger.this.parameters = new SerialParameters();
              JAM060Messenger.this.cboBaud.setSelectedItem(JAM060Messenger.this.parameters.getBaudRateString());
              JAM060Messenger.this.cboDataBits.setSelectedItem(JAM060Messenger.this.parameters.getDatabitsString());
              JAM060Messenger.this.cboFlowControlIn.setSelectedItem(JAM060Messenger.this.parameters.getFlowControlInString());
              JAM060Messenger.this.cboFlowControlOut.setSelectedItem(JAM060Messenger.this.parameters.getFlowControlOutString());
              JAM060Messenger.this.cboParity.setSelectedItem(JAM060Messenger.this.parameters.getParityString());
              JAM060Messenger.this.cboStopBits.setSelectedItem(JAM060Messenger.this.parameters.getStopbitsString());
              
              String[] arrSql = new String[2];
              arrSql[0] = "Select * from CBO_DISPOSITIVOS";
              JAMRowSet[] arrRst = JAMClienteDB.getRowSets(arrSql);
              JAM060Messenger.this.cboDispositivo.setModel(new JAMComboView(arrRst[0], "ID_SOCSYST10", "SOCSYST10_FILTRO_DES", true));
              
              JAMComm.copydllorsh();
              
              JAMComm objCom = new JAMComm();
              
              JAM060Messenger.this.cboPortName.removeAllItems();
              String[] arrPorts = objCom.getPorts();
              if (arrPorts != null) {
                for (int i = 0; i < arrPorts.length; i++) {
                  JAM060Messenger.this.cboPortName.addItem(arrPorts[i]);
                }
              }
              JAMCursor.setCursorOff(JAM060Messenger.this.tabMenu);
            }
            catch (Exception e1)
            {
              JAMCursor.setCursorOff(JAM060Messenger.this.tabMenu);
              
              e1.printStackTrace();
            }
          }
          else if (JAM060Messenger.this.tabMenu.getSelectedIndex() == 4)
          {
            JAM060Messenger.this.chkSeleccionaConexiones.setSelected(false);
            JAM060Messenger.this.chkBloqueaConexiones.setSelected(false);
            JAM060Messenger.this.setChkSelecciona();
            JAM060Messenger.doConexionesActivas(JAM060Messenger.this.grdConexiones, false);
          }
          else if (JAM060Messenger.this.tabMenu.getSelectedIndex() == 1)
          {
            JAM060Messenger.this.objMessenger.start();
          }
        }
      });
    }
    return this.tabMenu;
  }
  
  public static void doConexionesActivas(JAMGrid argGrid, boolean logHistorico)
  {
    try
    {
      String[] strSql = new String[1];
      strSql[0] = "Select first 0 0 ORDEN, 0 MARCA, '' INSTANCIAS, '' USUARIO, CAST(0 AS INTEGER) TIEMPO, '' STATUS, '' NOVEDAD, '' BLOQUEADO, CAST('' AS TIMESTAMP) DIA, CAST(0 AS INTEGER) ESBLOQUEADO, '' CLAVE from socutil04_tbl_dual";
      
      argGrid.setTitulo("Corriendo en Modo : " + 
        JAMClienteDB.getModoConexionDescripcion() + 
        " en Servidor : " + 
        JAMLibKernel.JAMURL_ROOT);
      
      argGrid.setAllowDelete(false);
      argGrid.setMarcador(true);
      argGrid.setSortedView(new JAMSortedView(JAMClienteDB.getRowSets(strSql)[0]));
      argGrid.setAllColumnsVisible(true);
      if (logHistorico)
      {
        argGrid.addSortKey("USUARIO");
        argGrid.setSemaforo(true);
      }
      else
      {
        argGrid.addSortKeyDesc("TIEMPO");
      }
      argGrid.getColumn("ORDEN").setHeader(" ");
      argGrid.getColumn("MARCA").setHeader("Borra");
      argGrid.getColumn("INSTANCIAS").setHeader("Instancias");
      argGrid.getColumn("USUARIO").setHeader("Usuarios");
      argGrid.getColumn("TIEMPO").setHeader("Ult. Acceso");
      argGrid.getColumn("STATUS").setHeader("Status");
      argGrid.getColumn("NOVEDAD").setHeader("Conexion");
      argGrid.getColumn("BLOQUEADO").setHeader("Bloqueos");
      argGrid.getColumn("DIA").setHeader("Fecha");
      if (logHistorico)
      {
        argGrid.getColumn("ORDEN").setVisible(true);
        argGrid.getColumn("MARCA").setVisible(false);
        argGrid.getColumn("TIEMPO").setVisible(false);
        argGrid.getColumn("NOVEDAD").setVisible(false);
        argGrid.getColumn("BLOQUEADO").setVisible(false);
      }
      else
      {
        argGrid.getColumn("ORDEN").setVisible(false);
      }
      argGrid.getColumn("ESBLOQUEADO").setVisible(false);
      argGrid.getColumn("CLAVE").setVisible(false);
      
      argGrid.adjustcolumnWidht();
      argGrid.repaint();
      
      ArrayList<JAMDataConexionesInfo> arrConexiones = null;
      if (logHistorico) {
        arrConexiones = JAMClienteDB.getConexionesHistoricas();
      } else {
        arrConexiones = JAMClienteDB.getConexionesActivas();
      }
      JAMRowSet rstConexiones = argGrid.getRowset();
      for (JAMDataConexionesInfo conn : arrConexiones)
      {
        rstConexiones.moveToInsertRow();
        rstConexiones.updateInt("ORDEN", 5);
        rstConexiones.updateInt("MARCA", 0);
        rstConexiones.updateString("INSTANCIAS", conn.getInstancia());
        rstConexiones.updateString("USUARIO", conn.getUsuario());
        rstConexiones.updateInt("TIEMPO", conn.getTime());
        rstConexiones.updateString("STATUS", conn.getStatus());
        rstConexiones.updateString("NOVEDAD", conn.getNovedades());
        rstConexiones.updateString("CLAVE", conn.getClave());
        rstConexiones.updateString("BLOQUEADO", conn.getBloqueado());
        rstConexiones.updateDateTime("DIA", conn.getDiaApertura());
        rstConexiones.updateInt("ESBLOQUEADO", JAMUtil.JAMConvBoolean(conn.isBloqueado()));
        rstConexiones.insertRow();
        rstConexiones.moveToCurrentRow();
      }
      argGrid.SearchForNewRecords();
    }
    catch (Exception eg)
    {
      JAMUtil.showDialog(eg.getMessage());
    }
  }
  
  private JPanel getPnlMenuButtons()
  {
    if (this.pnlMenuButtons == null)
    {
      this.pnlMenuButtons = new JPanel();
      this.pnlMenuButtons.setLayout(new BorderLayout());
      this.pnlMenuButtons.add(getBtoAceptar(), "Center");
      this.pnlMenuButtons.add(getCancelar(), "West");
    }
    return this.pnlMenuButtons;
  }
  
  private JAMGrid getGrdServidoresCorreos()
  {
    if (this.grdServidoresCorreos == null)
    {
      this.grdServidoresCorreos = new JAMGrid();
      this.grdServidoresCorreos.setBounds(new Rectangle(0, 105, 641, 286));
      JTable tmpGrd = this.grdServidoresCorreos.retornaGrilla();
      tmpGrd.addMouseListener(new MouseAdapter()
      {
        public void mouseClicked(MouseEvent e)
        {
          try
          {
            JAM060Messenger.this.doEditaGrilla();
          }
          catch (Exception eg)
          {
            JAMUtil.showDialog("Error al Recuperar datos : " + eg.getMessage());
          }
        }
      });
    }
    return this.grdServidoresCorreos;
  }
  
  private JCheckBox getChkTtls()
  {
    if (this.chkTtls == null)
    {
      this.chkTtls = new JCheckBox();
      this.chkTtls.setBounds(new Rectangle(145, 80, 41, 21));
      this.chkTtls.setText("Si");
      this.chkTtls.setSelected(true);
      this.chkTtls.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAM060Messenger.this.doEventChkTtls(null);
        }
      });
    }
    return this.chkTtls;
  }
  
  private JCheckBox getChkAuth()
  {
    if (this.chkAuth == null)
    {
      this.chkAuth = new JCheckBox();
      this.chkAuth.setBounds(new Rectangle(330, 80, 41, 21));
      this.chkAuth.setText("Si");
      this.chkAuth.setSelected(true);
      this.chkAuth.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAM060Messenger.this.doEventChkAuth(null);
        }
      });
    }
    return this.chkAuth;
  }
  
  private JAMInputNumber getTxtPuertoSMTP()
  {
    if (this.txtPuertoSMTP == null)
    {
      this.txtPuertoSMTP = new JAMInputNumber(4, JAMInputNumber.JAMINT);
      this.txtPuertoSMTP.setBounds(new Rectangle(570, 5, 66, 21));
    }
    return this.txtPuertoSMTP;
  }
  
  private JAMInputText getTxtServidorSMTP()
  {
    if (this.txtServidorSMTP == null)
    {
      this.txtServidorSMTP = new JAMInputText(100);
      this.txtServidorSMTP.setBounds(new Rectangle(145, 5, 326, 21));
    }
    return this.txtServidorSMTP;
  }
  
  private JAMButtonAlta getBtoAlta()
  {
    if (this.btoAlta == null)
    {
      this.btoAlta = new JAMButtonAlta();
      this.btoAlta.setBounds(new Rectangle(375, 80, 126, 21));
      this.btoAlta.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          try
          {
            JAM060Messenger.this.doEventAltaModi(true);
          }
          catch (Exception eg)
          {
            JAMUtil.showDialog("No se pudo grabar los datos : " + eg.getMessage());
          }
        }
      });
    }
    return this.btoAlta;
  }
  
  private JAMButtonModifica getBtoAlta1()
  {
    if (this.btoAlta1 == null)
    {
      this.btoAlta1 = new JAMButtonModifica();
      this.btoAlta1.setBounds(new Rectangle(505, 80, 131, 21));
      this.btoAlta1.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          try
          {
            JAM060Messenger.this.doEventAltaModi(false);
          }
          catch (Exception eg)
          {
            JAMUtil.showDialog("No se pudo grabar los datos : " + eg.getMessage());
          }
        }
      });
    }
    return this.btoAlta1;
  }
  
  private void doEventAltaModi(boolean logAlta)
    throws Exception
  {
    if (this.txtServidorSMTP.getText().equalsIgnoreCase(""))
    {
      JAMUtil.showDialog("Debe Ingresar Servidor SMTP");
      return;
    }
    if (this.txtServidorPOP3.getText().equalsIgnoreCase(""))
    {
      JAMUtil.showDialog("Debe Ingresar Servidor POP3");
      return;
    }
    if (this.txtDns.getText().equalsIgnoreCase(""))
    {
      JAMUtil.showDialog("Debe Ingresar DNS");
      return;
    }
    if (this.txtPuertoSMTP.getNumber().intValue() <= 0)
    {
      JAMUtil.showDialog("Debe Ingresar Puerto SMTP");
      return;
    }
    if (this.txtPuertoPOP3.getNumber().intValue() <= 0)
    {
      JAMUtil.showDialog("Debe Ingresar Puerto POP3");
      return;
    }
    if (this.txtServidorSMTP.getText().indexOf(" ") != -1)
    {
      JAMUtil.showDialog("No puede haber espacios en blanco en el servidor");
      return;
    }
    if (this.txtDns.getText().indexOf(" ") != -1)
    {
      JAMUtil.showDialog("No puede haber espacios en blanco en el DNS");
      return;
    }
    JAMRowSet rstTmp = this.grdServidoresCorreos.getRowset();
    if (logAlta)
    {
      rstTmp.moveToInsertRow();
      rstTmp.updateInt("id_socsyst43", --this.intIdFalso);
    }
    else
    {
      rstTmp.moveToCurrentRow();
    }
    rstTmp.updateString("socsyst43_host", this.txtServidorSMTP.getText());
    rstTmp.updateString("socsyst43_host_pop3", this.txtServidorPOP3.getText());
    rstTmp.updateString("socsyst43_dns", this.txtDns.getText());
    rstTmp.updateInt("socsyst43_port", this.txtPuertoSMTP.getNumber().intValue());
    rstTmp.updateInt("socsyst43_port_pop3", this.txtPuertoPOP3.getNumber().intValue());
    rstTmp.updateInt("socsyst43_ttls", JAMUtil.JAMConvBoolean(this.chkTtls.getSelectedObjects()));
    rstTmp.updateInt("socsyst43_auth", JAMUtil.JAMConvBoolean(this.chkAuth.getSelectedObjects()));
    rstTmp.updateDateTime("socsyst43_fapl", JAMLibKernel.getFecha());
    
    rstTmp.updateString("ttls", this.chkTtls.getText());
    rstTmp.updateString("auth", this.chkAuth.getText());
    if (logAlta)
    {
      rstTmp.insertRow();
      rstTmp.moveToCurrentRow();
    }
    else
    {
      rstTmp.updateRow();
    }
    this.grdServidoresCorreos.SearchForNewRecords();
    doLimpiaGets();
  }
  
  private void doEventsChecks(int argIndex)
  {
    if ((!this.chkOrga.isSelected()) && (argIndex == ORGANIZACION))
    {
      this.chkOrga.setSelected(true);
      return;
    }
    if ((!this.chkCargos.isSelected()) && (argIndex == CARGO))
    {
      this.chkCargos.setSelected(true);
      return;
    }
    if ((!this.chkUsuarios.isSelected()) && (argIndex == USUARIO))
    {
      this.chkUsuarios.setSelected(true);
      return;
    }
    this.cboCargos.setEnabled(false);
    this.cboTreeOrga.setEnabled(false);
    this.cboUsuarios.setEnabled(false);
    
    this.cboCargos.positionAt(-1);
    this.cboTreeOrga.positionById(-1);
    this.cboUsuarios.positionAt(-1);
    if (argIndex == ORGANIZACION)
    {
      this.chkCargos.setSelected(false);
      this.chkUsuarios.setSelected(false);
      this.cboTreeOrga.setEnabled(this.chkOrga.isSelected());
      this.chkOrga.setSelected(true);
    }
    else if (argIndex == CARGO)
    {
      this.chkOrga.setSelected(false);
      this.chkUsuarios.setSelected(false);
      this.cboCargos.setEnabled(this.chkCargos.isSelected());
      this.chkCargos.setSelected(true);
    }
    else if (argIndex == USUARIO)
    {
      this.chkOrga.setSelected(false);
      this.chkCargos.setSelected(false);
      this.cboUsuarios.setEnabled(this.chkUsuarios.isSelected());
      this.chkUsuarios.setSelected(true);
    }
  }
  
  private void doEventChkTtls(Boolean logAccion)
  {
    if (logAccion != null) {
      this.chkTtls.setSelected(logAccion.booleanValue());
    }
    if (JAMUtil.JAMConvBoolean(this.chkTtls.getSelectedObjects()) == 0) {
      this.chkTtls.setText("No");
    } else {
      this.chkTtls.setText("Si");
    }
  }
  
  private void doEventChkAuth(Boolean logAccion)
  {
    if (logAccion != null) {
      this.chkAuth.setSelected(logAccion.booleanValue());
    }
    if (JAMUtil.JAMConvBoolean(this.chkAuth.getSelectedObjects()) == 0) {
      this.chkAuth.setText("No");
    } else {
      this.chkAuth.setText("Si");
    }
  }
  
  private void doLimpiaGets()
    throws Exception
  {
    this.txtPuertoSMTP.setNumber(0);
    this.txtPuertoPOP3.setNumber(0);
    this.txtServidorSMTP.setText("");
    this.txtServidorPOP3.setText("");
    this.txtDns.setText("");
    doEventChkAuth(new Boolean(false));
    doEventChkTtls(new Boolean(false));
    if ((this.grdServidoresCorreos.getRowset() != null) && (this.grdServidoresCorreos.getRowset().getRowcount() > 0L)) {
      this.grdServidoresCorreos.setSelectionRow(-1, -1);
    }
  }
  
  private void doEditaGrilla()
    throws Exception
  {
    if ((this.grdServidoresCorreos.getRowset() == null) || 
      (this.grdServidoresCorreos.getRowset().getRowcount() == 0L)) {
      return;
    }
    this.txtServidorSMTP.setText(this.grdServidoresCorreos.getRowset().getString("socsyst43_host"));
    this.txtServidorPOP3.setText(this.grdServidoresCorreos.getRowset().getString("socsyst43_host_pop3"));
    this.txtDns.setText(this.grdServidoresCorreos.getRowset().getString("socsyst43_dns"));
    this.txtPuertoSMTP.setNumber(this.grdServidoresCorreos.getRowset().getInt("socsyst43_port"));
    this.txtPuertoPOP3.setNumber(this.grdServidoresCorreos.getRowset().getInt("socsyst43_port_pop3"));
    
    doEventChkTtls(new Boolean(JAMUtil.JAMConvBoolean(this.grdServidoresCorreos.getRowset().getInt("socsyst43_ttls"))));
    doEventChkAuth(new Boolean(JAMUtil.JAMConvBoolean(this.grdServidoresCorreos.getRowset().getInt("socsyst43_auth"))));
  }
  
  private JAMLabel getLblDNS()
  {
    if (this.lblDNS == null)
    {
      this.lblDNS = new JAMLabel(1);
      this.lblDNS.setBounds(new Rectangle(5, 55, 136, 21));
      this.lblDNS.setIconInput(1);
      this.lblDNS.setText("DNS :");
    }
    return this.lblDNS;
  }
  
  private JAMInputText getTxtDns()
  {
    if (this.txtDns == null)
    {
      this.txtDns = new JAMInputText(100);
      this.txtDns.setBounds(new Rectangle(145, 55, 326, 21));
    }
    return this.txtDns;
  }
  
  private JAMLabel getLblHostPop3()
  {
    if (this.lblHostPop3 == null)
    {
      this.lblHostPop3 = new JAMLabel(1);
      this.lblHostPop3.setBounds(new Rectangle(5, 30, 136, 21));
      this.lblHostPop3.setIconInput(1);
      this.lblHostPop3.setText("Servidor POP3 :");
    }
    return this.lblHostPop3;
  }
  
  private JAMInputText getTxtServidorPOP3()
  {
    if (this.txtServidorPOP3 == null)
    {
      this.txtServidorPOP3 = new JAMInputText(100);
      this.txtServidorPOP3.setBounds(new Rectangle(145, 30, 326, 21));
    }
    return this.txtServidorPOP3;
  }
  
  private JAMLabel getLblPort1()
  {
    if (this.lblPort1 == null)
    {
      this.lblPort1 = new JAMLabel(1);
      this.lblPort1.setBounds(new Rectangle(475, 30, 91, 21));
      this.lblPort1.setIconInput(1);
      this.lblPort1.setText("Puerto :");
    }
    return this.lblPort1;
  }
  
  private JAMInputNumber getTxtPuertoPOP3()
  {
    if (this.txtPuertoPOP3 == null)
    {
      this.txtPuertoPOP3 = new JAMInputNumber(4, JAMInputNumber.JAMINT);
      this.txtPuertoPOP3.setBounds(new Rectangle(570, 30, 66, 21));
    }
    return this.txtPuertoPOP3;
  }
  
  private JPanel getPnlUsuarios()
  {
    if (this.pnlUsuarios == null)
    {
      this.lblUsuarios = new JAMLabel(1);
      this.lblUsuarios.setIconInput(0);
      this.lblUsuarios.setText("Usuario :");
      this.lblUsuarios.setPreferredSize(new Dimension(120, 21));
      this.pnlUsuarios = new JPanel();
      this.pnlUsuarios.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlUsuarios.add(this.lblUsuarios, "West");
      this.pnlUsuarios.add(getChkUsuarios(), "East");
      this.pnlUsuarios.add(getCboUsuarios(), "Center");
    }
    return this.pnlUsuarios;
  }
  
  private JAMCombo getCboUsuarios()
  {
    if (this.cboUsuarios == null) {
      this.cboUsuarios = new JAMCombo();
    }
    return this.cboUsuarios;
  }
  
  private JPanel getPnlCargos()
  {
    if (this.pnlCargos == null)
    {
      this.pnlCargos = new JPanel();
      this.pnlCargos.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlCargos.add(getLblCargos(), "West");
      this.pnlCargos.add(getChkCargos(), "East");
      this.pnlCargos.add(getCboCargos(), "Center");
    }
    return this.pnlCargos;
  }
  
  private JAMLabel getLblCargos()
  {
    if (this.lblCargos == null)
    {
      this.lblCargos = new JAMLabel(1);
      this.lblCargos.setPreferredSize(new Dimension(120, 21));
      this.lblCargos.setIconInput(0);
      this.lblCargos.setText("Cargo :");
    }
    return this.lblCargos;
  }
  
  private JAMCombo getCboCargos()
  {
    if (this.cboCargos == null) {
      this.cboCargos = new JAMCombo();
    }
    return this.cboCargos;
  }
  
  private JPanel getPnlOrga()
  {
    if (this.pnlOrga == null)
    {
      this.pnlOrga = new JPanel();
      this.pnlOrga.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlOrga.add(getLblOrga(), "West");
      this.pnlOrga.add(getChkOrga(), "East");
      this.pnlOrga.add(getCboTreeOrga(), "Center");
    }
    return this.pnlOrga;
  }
  
  private JAMLabel getLblOrga()
  {
    if (this.lblOrga == null)
    {
      this.lblOrga = new JAMLabel(1);
      this.lblOrga.setPreferredSize(new Dimension(120, 21));
      this.lblOrga.setIconInput(0);
      this.lblOrga.setText("Organizacion :");
    }
    return this.lblOrga;
  }
  
  private JAMComboTree getCboTreeOrga()
  {
    if (this.cboTreeOrga == null) {
      this.cboTreeOrga = new JAMComboTree();
    }
    return this.cboTreeOrga;
  }
  
  private JCheckBox getChkUsuarios()
  {
    if (this.chkUsuarios == null)
    {
      this.chkUsuarios = new JCheckBox();
      this.chkUsuarios.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAM060Messenger.this.doEventsChecks(JAM060Messenger.USUARIO);
        }
      });
    }
    return this.chkUsuarios;
  }
  
  private JCheckBox getChkCargos()
  {
    if (this.chkCargos == null)
    {
      this.chkCargos = new JCheckBox();
      this.chkCargos.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAM060Messenger.this.doEventsChecks(JAM060Messenger.CARGO);
        }
      });
    }
    return this.chkCargos;
  }
  
  private JCheckBox getChkOrga()
  {
    if (this.chkOrga == null)
    {
      this.chkOrga = new JCheckBox();
      this.chkOrga.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAM060Messenger.this.doEventsChecks(JAM060Messenger.ORGANIZACION);
        }
      });
    }
    return this.chkOrga;
  }
  
  private JAMGridEditable getGrdSeguimientos()
  {
    if (this.grdSeguimientos == null)
    {
      if (this == null) {
        this.grdSeguimientos = new JAMGridEditable();
      } else {
        this.grdSeguimientos = new JAMGridEditable(getPnlDatosBoletines(), null);
      }
      this.grdSeguimientos.addJAMGridEditableListener(new JAMGridEditablePressedListener()
      {
        public void eventJAMGridEditable(JAMGridEditablePressedEvent e)
        {
          switch (e.getTipoEvent())
          {
          case 2: 
            if ((JAM060Messenger.this.chkCargos.isSelected()) && (JAM060Messenger.this.cboCargos.getId() == -1))
            {
              JAMUtil.showDialog("Debe Seleccionar un CARGO para enviar un boletin");
              e.setCancela(true);
              return;
            }
            if ((JAM060Messenger.this.chkUsuarios.isSelected()) && (JAM060Messenger.this.cboUsuarios.getId() == -1))
            {
              JAMUtil.showDialog("Debe Seleccionar un USUARIO para enviar un boletin");
              e.setCancela(true);
              return;
            }
            if ((JAM060Messenger.this.chkOrga.isSelected()) && (JAM060Messenger.this.cboTreeOrga.getId() == -1))
            {
              JAMUtil.showDialog("Debe Seleccionar una ORGANIZACION para enviar un boletin");
              e.setCancela(true);
              return;
            }
            if ((!JAM060Messenger.this.chkCargos.isSelected()) && (!JAM060Messenger.this.chkUsuarios.isSelected()) && (!JAM060Messenger.this.chkOrga.isSelected()))
            {
              JAMUtil.showDialog("Debe Seleccionar una DESTINO para enviar un boletin");
              e.setCancela(true);
              return;
            }
            if (JAM060Messenger.this.txtTitulo.getText().trim().equalsIgnoreCase(""))
            {
              JAMUtil.showDialog("Debe ingresar una TITULO para identificar el boletin");
              e.setCancela(true);
              return;
            }
            if (JAM060Messenger.this.txtNotas.getText().trim().equalsIgnoreCase(""))
            {
              JAMUtil.showDialog("Debe ingresar una NOTA que describa el boletin");
              e.setCancela(true);
              return;
            }
            try
            {
              JAMRowSet rstWrite = JAM060Messenger.this.grdSeguimientos.getJAMGrid().getRowset();
              int intIdPosi = -1;
              if (!e.getAlta()) {
                intIdPosi = rstWrite.getInt("ID_SOCSYST50");
              }
              if (e.getAlta())
              {
                if ((JAM060Messenger.this.cboTreeOrga.getId() != -1) && (rstWrite.find("RELA_SOCWFLO01", JAM060Messenger.this.cboTreeOrga.getId())))
                {
                  JAMUtil.showDialog("El boletin para esa ORGANIZACION YA EXISTE");
                  e.setCancela(true);
                  return;
                }
                if ((JAM060Messenger.this.cboCargos.getId() != -1) && (rstWrite.find("RELA_SOCWFLO02", JAM060Messenger.this.cboCargos.getId())))
                {
                  JAMUtil.showDialog("El boletin para ese CARGO YA EXISTE");
                  e.setCancela(true);
                  return;
                }
                if ((JAM060Messenger.this.cboUsuarios.getId() != -1) && (rstWrite.find("RELA_SOCUSUA02", JAM060Messenger.this.cboUsuarios.getId())))
                {
                  JAMUtil.showDialog("El boletin para ese USUARIO YA EXISTE");
                  e.setCancela(true);
                  return;
                }
                rstWrite.moveToInsertRow();
                
                rstWrite.updateInt("ID_SOCSYST50", --JAM060Messenger.this.intIdFalsoBoletin);
                rstWrite.updateDateTime("SOCSYST50_FAPL", JAMLibKernel.getFecha());
              }
              else
              {
                if ((JAM060Messenger.this.cboTreeOrga.getId() != -1) && (rstWrite.findExclude("RELA_SOCWFLO01", JAM060Messenger.this.cboTreeOrga.getId(), "ID_SOCSYST50", intIdPosi)))
                {
                  JAMUtil.showDialog("El boletin para esa ORGANIZACION YA EXISTE");
                  e.setCancela(true);
                  return;
                }
                if ((JAM060Messenger.this.cboCargos.getId() != -1) && (rstWrite.findExclude("RELA_SOCWFLO02", JAM060Messenger.this.cboCargos.getId(), "ID_SOCSYST50", intIdPosi)))
                {
                  JAMUtil.showDialog("El boletin para ese CARGO YA EXISTE");
                  e.setCancela(true);
                  return;
                }
                if ((JAM060Messenger.this.cboUsuarios.getId() != -1) && (rstWrite.findExclude("RELA_SOCUSUA02", JAM060Messenger.this.cboUsuarios.getId(), "ID_SOCSYST50", intIdPosi)))
                {
                  JAMUtil.showDialog("El boletin para ese USUARIO YA EXISTE");
                  e.setCancela(true);
                  return;
                }
                rstWrite.find("ID_SOCSYST50", intIdPosi);
                rstWrite.moveToCurrentRow();
              }
              rstWrite.updateIntCombo("RELA_SOCWFLO02", JAM060Messenger.this.cboCargos.getId());
              rstWrite.updateIntCombo("RELA_SOCUSUA02", JAM060Messenger.this.cboUsuarios.getId());
              rstWrite.updateIntCombo("RELA_SOCWFLO01", JAM060Messenger.this.cboTreeOrga.getId());
              if (JAM060Messenger.this.chkCargos.isSelected())
              {
                rstWrite.updateString("TIPO", "CARGO");
                rstWrite.updateString("ENTIDAD", JAM060Messenger.this.cboCargos.getDescription());
              }
              else if (JAM060Messenger.this.chkUsuarios.isSelected())
              {
                rstWrite.updateString("TIPO", "USUARIO");
                rstWrite.updateString("ENTIDAD", JAM060Messenger.this.cboUsuarios.getDescription());
              }
              else if (JAM060Messenger.this.chkOrga.isSelected())
              {
                rstWrite.updateString("TIPO", "ORGANIZACION");
                rstWrite.updateString("ENTIDAD", JAM060Messenger.this.cboTreeOrga.GetDescription());
              }
              rstWrite.updateString("SOCSYST50_TITULO", JAM060Messenger.this.txtTitulo.getText());
              rstWrite.updateString("SOCSYST50_BOLETIN", JAM060Messenger.this.txtNotas.getText());
              rstWrite.updateInt("SOCSYST50_BLOQUEA", JAMUtil.JAMConvBoolean(JAM060Messenger.this.chkBloquea.isSelected()));
              if (e.getAlta())
              {
                rstWrite.insertRow();
                rstWrite.moveToCurrentRow();
              }
              else
              {
                rstWrite.updateRow();
              }
              JAM060Messenger.this.grdSeguimientos.getJAMGrid().SearchForNewRecords();
            }
            catch (Exception e1)
            {
              JAMUtil.showDialog("ERROR al grabar la grilla : " + e1.getMessage());
            }
          case 3: 
            break;
          case 0: 
            JAM060Messenger.this.doEventsChecks(JAM060Messenger.NINGUNO);
            JAM060Messenger.this.grdSeguimientos.clearGetsonly();
            break;
          case 1: 
            try
            {
              if (JAM060Messenger.this.grdSeguimientos.getJAMGrid().getRowset().getInt("RELA_SOCWFLO01") > 0)
              {
                JAM060Messenger.this.doEventsChecks(JAM060Messenger.ORGANIZACION);
                JAM060Messenger.this.cboTreeOrga.positionById(JAM060Messenger.this.grdSeguimientos.getJAMGrid().getRowset().getInt("RELA_SOCWFLO01"));
              }
              else if (JAM060Messenger.this.grdSeguimientos.getJAMGrid().getRowset().getInt("RELA_SOCWFLO02") > 0)
              {
                JAM060Messenger.this.doEventsChecks(JAM060Messenger.CARGO);
                JAM060Messenger.this.cboCargos.positionAt(JAM060Messenger.this.grdSeguimientos.getJAMGrid().getRowset().getInt("RELA_SOCWFLO02"));
              }
              else if (JAM060Messenger.this.grdSeguimientos.getJAMGrid().getRowset().getInt("RELA_SOCUSUA02") > 0)
              {
                JAM060Messenger.this.doEventsChecks(JAM060Messenger.USUARIO);
                JAM060Messenger.this.cboUsuarios.positionAt(JAM060Messenger.this.grdSeguimientos.getJAMGrid().getRowset().getInt("RELA_SOCUSUA02"));
              }
              JAM060Messenger.this.txtTitulo.setText(JAM060Messenger.this.grdSeguimientos.getJAMGrid().getRowset().getString("SOCSYST50_TITULO"));
              JAM060Messenger.this.txtNotas.setText(JAM060Messenger.this.grdSeguimientos.getJAMGrid().getRowset().getString("SOCSYST50_BOLETIN"));
              JAM060Messenger.this.chkBloquea.setSelected(JAMUtil.JAMConvBoolean(JAM060Messenger.this.grdSeguimientos.getJAMGrid().getRowset().getInt("socsyst50_bloquea")));
            }
            catch (Exception e1)
            {
              JAMUtil.showDialog("ERROR al Actualizar boletin : " + e1.getMessage());
            }
          }
        }
      });
    }
    return this.grdSeguimientos;
  }
  
  private JPanel getPnlDatosBoletines()
  {
    if (this.pnlDatosBoletines == null)
    {
      this.pnlDatosBoletines = new JPanel();
      this.pnlDatosBoletines.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlDatosBoletines.setSize(new Dimension(494, 226));
      this.pnlDatosBoletines.add(getPnlTopBoletines(), "North");
      this.pnlDatosBoletines.add(getPnlNotas1(), "Center");
    }
    return this.pnlDatosBoletines;
  }
  
  private JPanel getPnlTopBoletines()
  {
    if (this.pnlTopBoletines == null)
    {
      this.pnlTopBoletines = new JPanel();
      this.pnlTopBoletines.setLayout(JAMUtil.JAMGridLayout(4, 1, 5, 5));
      this.pnlTopBoletines.setPreferredSize(new Dimension(100, 99));
      this.pnlTopBoletines.add(getPnlOrga(), null);
      this.pnlTopBoletines.add(getPnlCargos(), null);
      this.pnlTopBoletines.add(getPnlUsuarios(), null);
      this.pnlTopBoletines.add(getPnlTitulo(), null);
    }
    return this.pnlTopBoletines;
  }
  
  private JPanel getPnlNotas1()
  {
    if (this.pnlNotas1 == null)
    {
      this.pnlNotas1 = new JPanel();
      this.pnlNotas1.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlNotas1.add(getLblNotas(), "North");
      this.pnlNotas1.add(getTxtNotas(), "Center");
    }
    return this.pnlNotas1;
  }
  
  private JAMLabel getLblNotas()
  {
    if (this.lblNotas == null)
    {
      this.lblNotas = new JAMLabel(1);
      this.lblNotas.setPreferredSize(new Dimension(120, 21));
      this.lblNotas.setText("Escriba aqu������ el boletion que desea enviar......");
      this.lblNotas.setIconInput(0);
      this.lblNotas.setHorizontalAlignment(0);
      this.lblNotas.add(getChkBloquea(), "East");
    }
    return this.lblNotas;
  }
  
  private JAMAreaTexto getTxtNotas()
  {
    if (this.txtNotas == null) {
      this.txtNotas = new JAMAreaTexto();
    }
    return this.txtNotas;
  }
  
  private JPanel getPnlTitulo()
  {
    if (this.pnlTitulo == null)
    {
      this.pnlTitulo = new JPanel();
      this.pnlTitulo.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlTitulo.add(getLblTitulo(), "West");
      this.pnlTitulo.add(getTxtTitulo(), "Center");
    }
    return this.pnlTitulo;
  }
  
  private JAMLabel getLblTitulo()
  {
    if (this.lblTitulo == null)
    {
      this.lblTitulo = new JAMLabel(1);
      this.lblTitulo.setText("T������tulo :");
      this.lblTitulo.setIconInput(1);
      this.lblTitulo.setPreferredSize(new Dimension(120, 21));
    }
    return this.lblTitulo;
  }
  
  private JAMInputText getTxtTitulo()
  {
    if (this.txtTitulo == null) {
      this.txtTitulo = new JAMInputText(100);
    }
    return this.txtTitulo;
  }
  
  private JPanel getPnlPuertos()
  {
    if (this.pnlPuertos == null)
    {
      this.pnlPuertos = new JPanel();
      this.pnlPuertos.setLayout(null);
      this.pnlPuertos.add(getPnlPortName(), null);
      this.pnlPuertos.add(getPnlDispositivo(), null);
      this.pnlPuertos.add(getPnlBaud(), null);
      this.pnlPuertos.add(getPnlFlowControlIn(), null);
      this.pnlPuertos.add(getPnlFlowControlOut(), null);
      this.pnlPuertos.add(getPnlParity(), null);
      this.pnlPuertos.add(getPnlDataBits(), null);
      this.pnlPuertos.add(getPnlStopBits(), null);
    }
    return this.pnlPuertos;
  }
  
  private JPanel getPnlPortName()
  {
    if (this.pnlPortName == null)
    {
      this.pnlPortName = new JPanel();
      this.pnlPortName.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlPortName.setBounds(new Rectangle(5, 30, 471, 21));
      this.pnlPortName.add(getLblPortName(), "West");
      this.pnlPortName.add(getCboPortName(), "Center");
    }
    return this.pnlPortName;
  }
  
  private JAMLabel getLblPortName()
  {
    if (this.lblPortName == null)
    {
      this.lblPortName = new JAMLabel(1);
      this.lblPortName.setPreferredSize(new Dimension(150, 21));
      this.lblPortName.setIconInput(1);
      this.lblPortName.setText("Puerto Nombre :");
    }
    return this.lblPortName;
  }
  
  private JAMCombo getCboPortName()
  {
    if (this.cboPortName == null) {
      this.cboPortName = new JAMCombo();
    }
    return this.cboPortName;
  }
  
  private JPanel getPnlDispositivo()
  {
    if (this.pnlDispositivo == null)
    {
      this.pnlDispositivo = new JPanel();
      this.pnlDispositivo.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlDispositivo.setBounds(new Rectangle(5, 5, 471, 21));
      this.pnlDispositivo.add(getLblDispositivo(), "West");
      this.pnlDispositivo.add(getCboDispositivo(), "Center");
    }
    return this.pnlDispositivo;
  }
  
  private JAMLabel getLblDispositivo()
  {
    if (this.lblDispositivo == null)
    {
      this.lblDispositivo = new JAMLabel(1);
      this.lblDispositivo.setPreferredSize(new Dimension(150, 21));
      this.lblDispositivo.setIconInput(1);
      this.lblDispositivo.setText("Dispositivo :");
    }
    return this.lblDispositivo;
  }
  
  private JAMCombo getCboDispositivo()
  {
    if (this.cboDispositivo == null)
    {
      this.cboDispositivo = new JAMCombo();
      this.cboDispositivo.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          try
          {
            JAM060Messenger.this.logAltaDispositivos = true;
            String[] arrSql = new String[1];
            arrSql[0] = ("Select * from SOCSYST45_TBL_PUERTOS where RELA_SOCSYST10 = " + JAM060Messenger.this.cboDispositivo.getId());
            JAM060Messenger.this.rstDispositivos = JAMClienteDB.getRowSets(arrSql)[0];
            JAM060Messenger.this.cboPortName.setSelectedIndex(0);
            if (JAM060Messenger.this.rstDispositivos.getRowcount() == 1L)
            {
              JAM060Messenger.this.logAltaDispositivos = false;
              JAM060Messenger.this.rstDispositivos.first();
              JAM060Messenger.this.parameters.setPortName(JAM060Messenger.this.rstDispositivos.getString("SOCSYST45_PORTNAME"));
              JAM060Messenger.this.parameters.setBaudRate(JAM060Messenger.this.rstDispositivos.getInt("SOCSYST45_BAUDRATE"));
              JAM060Messenger.this.parameters.setFlowControlIn(JAM060Messenger.this.rstDispositivos.getInt("SOCSYST45_FLOWCONTROLIN"));
              JAM060Messenger.this.parameters.setFlowControlOut(JAM060Messenger.this.rstDispositivos.getInt("SOCSYST45_FLOWCONTROLOUT"));
              JAM060Messenger.this.parameters.setParity(JAM060Messenger.this.rstDispositivos.getInt("SOCSYST45_PARITY"));
              JAM060Messenger.this.parameters.setDatabits(JAM060Messenger.this.rstDispositivos.getInt("SOCSYST45_DATABITS"));
              JAM060Messenger.this.parameters.setStopbits(JAM060Messenger.this.rstDispositivos.getInt("SOCSYST45_STOPBITS"));
              
              JAM060Messenger.this.cboPortName.setSelectedItem(JAM060Messenger.this.parameters.getPortName());
            }
            JAM060Messenger.this.cboBaud.setSelectedItem(JAM060Messenger.this.parameters.getBaudRateString());
            JAM060Messenger.this.cboDataBits.setSelectedItem(JAM060Messenger.this.parameters.getDatabitsString());
            JAM060Messenger.this.cboFlowControlIn.setSelectedItem(JAM060Messenger.this.parameters.getFlowControlInString());
            JAM060Messenger.this.cboFlowControlOut.setSelectedItem(JAM060Messenger.this.parameters.getFlowControlOutString());
            JAM060Messenger.this.cboParity.setSelectedItem(JAM060Messenger.this.parameters.getParityString());
            JAM060Messenger.this.cboStopBits.setSelectedItem(JAM060Messenger.this.parameters.getStopbitsString());
          }
          catch (Exception e1)
          {
            e1.printStackTrace();
          }
        }
      });
    }
    return this.cboDispositivo;
  }
  
  private JPanel getPnlBaud()
  {
    if (this.pnlBaud == null)
    {
      this.pnlBaud = new JPanel();
      this.pnlBaud.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlBaud.setBounds(new Rectangle(5, 55, 471, 21));
      this.pnlBaud.add(getLblBaud(), "West");
      this.pnlBaud.add(getCboBaud(), "Center");
    }
    return this.pnlBaud;
  }
  
  private JAMLabel getLblBaud()
  {
    if (this.lblBaud == null)
    {
      this.lblBaud = new JAMLabel(1);
      this.lblBaud.setPreferredSize(new Dimension(150, 21));
      this.lblBaud.setIconInput(1);
      this.lblBaud.setText("Velocidad :");
    }
    return this.lblBaud;
  }
  
  private JAMCombo getCboBaud()
  {
    if (this.cboBaud == null)
    {
      this.cboBaud = new JAMCombo();
      this.cboBaud.addItem("300");
      this.cboBaud.addItem("2400");
      this.cboBaud.addItem("9600");
      this.cboBaud.addItem("14400");
      this.cboBaud.addItem("28800");
      this.cboBaud.addItem("38400");
      this.cboBaud.addItem("57600");
      this.cboBaud.addItem("152000");
    }
    return this.cboBaud;
  }
  
  private JPanel getPnlFlowControlIn()
  {
    if (this.pnlFlowControlIn == null)
    {
      this.pnlFlowControlIn = new JPanel();
      this.pnlFlowControlIn.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlFlowControlIn.setBounds(new Rectangle(5, 80, 471, 21));
      this.pnlFlowControlIn.add(getLblFlowControlIn(), "West");
      this.pnlFlowControlIn.add(getCboFlowControlIn(), "Center");
    }
    return this.pnlFlowControlIn;
  }
  
  private JAMLabel getLblFlowControlIn()
  {
    if (this.lblFlowControlIn == null)
    {
      this.lblFlowControlIn = new JAMLabel(1);
      this.lblFlowControlIn.setPreferredSize(new Dimension(150, 21));
      this.lblFlowControlIn.setIconInput(1);
      this.lblFlowControlIn.setText("Control de Ingreso :");
    }
    return this.lblFlowControlIn;
  }
  
  private JAMCombo getCboFlowControlIn()
  {
    if (this.cboFlowControlIn == null)
    {
      this.cboFlowControlIn = new JAMCombo();
      this.cboFlowControlIn.addItem("None");
      this.cboFlowControlIn.addItem("Xon/Xoff In");
      this.cboFlowControlIn.addItem("RTS/CTS In");
    }
    return this.cboFlowControlIn;
  }
  
  private JPanel getPnlFlowControlOut()
  {
    if (this.pnlFlowControlOut == null)
    {
      this.pnlFlowControlOut = new JPanel();
      this.pnlFlowControlOut.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlFlowControlOut.setBounds(new Rectangle(5, 105, 471, 21));
      this.pnlFlowControlOut.add(getLblFlowControlOut(), "West");
      this.pnlFlowControlOut.add(getCboFlowControlOut(), "Center");
    }
    return this.pnlFlowControlOut;
  }
  
  private JAMLabel getLblFlowControlOut()
  {
    if (this.lblFlowControlOut == null)
    {
      this.lblFlowControlOut = new JAMLabel(1);
      this.lblFlowControlOut.setPreferredSize(new Dimension(150, 21));
      this.lblFlowControlOut.setIconInput(1);
      this.lblFlowControlOut.setText("Control de Salida :");
    }
    return this.lblFlowControlOut;
  }
  
  private JAMCombo getCboFlowControlOut()
  {
    if (this.cboFlowControlOut == null)
    {
      this.cboFlowControlOut = new JAMCombo();
      this.cboFlowControlOut.addItem("None");
      this.cboFlowControlOut.addItem("Xon/Xoff Out");
      this.cboFlowControlOut.addItem("RTS/CTS Out");
    }
    return this.cboFlowControlOut;
  }
  
  private JPanel getPnlParity()
  {
    if (this.pnlParity == null)
    {
      this.pnlParity = new JPanel();
      this.pnlParity.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlParity.setBounds(new Rectangle(5, 180, 471, 21));
      this.pnlParity.add(getLblParity(), "West");
      this.pnlParity.add(getCboParity(), "Center");
    }
    return this.pnlParity;
  }
  
  private JAMLabel getLblParity()
  {
    if (this.lblParity == null)
    {
      this.lblParity = new JAMLabel(1);
      this.lblParity.setPreferredSize(new Dimension(150, 21));
      this.lblParity.setIconInput(1);
      this.lblParity.setText("Pariedad :");
    }
    return this.lblParity;
  }
  
  private JAMCombo getCboParity()
  {
    if (this.cboParity == null)
    {
      this.cboParity = new JAMCombo();
      this.cboParity.addItem("None");
      this.cboParity.addItem("Even");
      this.cboParity.addItem("Odd");
    }
    return this.cboParity;
  }
  
  private JPanel getPnlDataBits()
  {
    if (this.pnlDataBits == null)
    {
      this.pnlDataBits = new JPanel();
      this.pnlDataBits.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlDataBits.setBounds(new Rectangle(5, 130, 471, 21));
      this.pnlDataBits.add(getLblDataBits(), "West");
      this.pnlDataBits.add(getCboDataBits(), "Center");
    }
    return this.pnlDataBits;
  }
  
  private JAMLabel getLblDataBits()
  {
    if (this.lblDataBits == null)
    {
      this.lblDataBits = new JAMLabel(1);
      this.lblDataBits.setPreferredSize(new Dimension(150, 21));
      this.lblDataBits.setIconInput(1);
      this.lblDataBits.setText("Data Bits  :");
    }
    return this.lblDataBits;
  }
  
  private JAMCombo getCboDataBits()
  {
    if (this.cboDataBits == null)
    {
      this.cboDataBits = new JAMCombo();
      this.cboDataBits.addItem("5");
      this.cboDataBits.addItem("6");
      this.cboDataBits.addItem("7");
      this.cboDataBits.addItem("8");
    }
    return this.cboDataBits;
  }
  
  private JPanel getPnlStopBits()
  {
    if (this.pnlStopBits == null)
    {
      this.pnlStopBits = new JPanel();
      this.pnlStopBits.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlStopBits.setBounds(new Rectangle(5, 155, 471, 21));
      this.pnlStopBits.add(getLblStopBits(), "West");
      this.pnlStopBits.add(getCboStopBits(), "Center");
    }
    return this.pnlStopBits;
  }
  
  private JAMLabel getLblStopBits()
  {
    if (this.lblStopBits == null)
    {
      this.lblStopBits = new JAMLabel(1);
      this.lblStopBits.setPreferredSize(new Dimension(150, 21));
      this.lblStopBits.setIconInput(1);
      this.lblStopBits.setText("Stop Bits :");
    }
    return this.lblStopBits;
  }
  
  private JAMCombo getCboStopBits()
  {
    if (this.cboStopBits == null)
    {
      this.cboStopBits = new JAMCombo();
      this.cboStopBits.addItem("1");
      this.cboStopBits.addItem("1.5");
      this.cboStopBits.addItem("2");
    }
    return this.cboStopBits;
  }
  
  private JCheckBox getChkBloquea()
  {
    if (this.chkBloquea == null)
    {
      this.chkBloquea = new JCheckBox();
      this.chkBloquea.setText("Bloquea Ingreso");
    }
    return this.chkBloquea;
  }
  
  private JPanel getPnlTabConexiones()
  {
    if (this.pnlTabConexiones == null)
    {
      this.pnlTabConexiones = new JPanel();
      this.pnlTabConexiones.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlTabConexiones.add(getPnlCerrarConexiones(), "North");
      this.pnlTabConexiones.add(getChkPie(), "South");
      this.pnlTabConexiones.add(getGrdConexiones(), "Center");
    }
    return this.pnlTabConexiones;
  }
  
  private JAMGrid getGrdConexiones()
  {
    if (this.grdConexiones == null) {
      this.grdConexiones = new JAMGrid();
    }
    return this.grdConexiones;
  }
  
  private JPanel getPnlCerrarConexiones()
  {
    if (this.pnlCerrarConexiones == null)
    {
      this.pnlCerrarConexiones = new JPanel();
      this.pnlCerrarConexiones.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlCerrarConexiones.setPreferredSize(new Dimension(100, 21));
      this.pnlCerrarConexiones.add(getBtoRefrescaConexiones(), "East");
      this.pnlCerrarConexiones.add(getBtoCierraConexiones(), "Center");
    }
    return this.pnlCerrarConexiones;
  }
  
  private JCheckBox getChkSeleccionaConexiones()
  {
    if (this.chkSeleccionaConexiones == null)
    {
      this.chkSeleccionaConexiones = new JCheckBox();
      this.chkSeleccionaConexiones.setText("Seleccionar Todas las Conexiones");
      this.chkSeleccionaConexiones.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAM060Messenger.this.setChkSelecciona();
        }
      });
    }
    return this.chkSeleccionaConexiones;
  }
  
  private void setChkSelecciona()
  {
    if (this.chkSeleccionaConexiones.isSelected())
    {
      this.chkSeleccionaConexiones.setText("Quitar Selecci��n");
      this.grdConexiones.setAllMarcadores(true);
    }
    else
    {
      this.chkSeleccionaConexiones.setText("Seleccionar Todas las Conexiones");
      this.grdConexiones.setAllMarcadores(false);
    }
  }
  
  private JAMButtonAccion getBtoCierraConexiones()
  {
    if (this.btoCierraConexiones == null)
    {
      this.btoCierraConexiones = new JAMButtonAccion();
      this.btoCierraConexiones.setText("Ejecuta Liberar Conexiones Seleccionadas");
      this.btoCierraConexiones.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          try
          {
            if ((JAM060Messenger.this.chkBloqueaConexiones.isSelected()) && (JAM060Messenger.this.grdConexiones.getCountMarcador() > 1L))
            {
              JAMUtil.showDialog("No puede por SEGURIDAD bloquear m��s de Una Instancia por vez", JAM060Messenger.this);
              return;
            }
            JAMCursor.setCursorOn(JAM060Messenger.this.btoCierraConexiones);
            JAMRowSet rstConexionesCierre = JAM060Messenger.this.grdConexiones.getRowset();
            
            rstConexionesCierre.beforeFirst();
            while (rstConexionesCierre.next()) {
              if (rstConexionesCierre.getInt("marca") == 1)
              {
                if ((JAM060Messenger.this.chkBloqueaConexiones.isSelected()) && 
                  (rstConexionesCierre.getString("USUARIO").equalsIgnoreCase(JAMLibKernel.ParamJAMUsername)) && 
                  (rstConexionesCierre.getString("INSTANCIAS").equalsIgnoreCase(JAMLibKernel.ParamJAMInstname)))
                {
                  JAMCursor.setCursorOff(JAM060Messenger.this.btoCierraConexiones);
                  JAMUtil.showDialog("No puede BLOQUEAR la INSTANCIA que esta Conectado", JAM060Messenger.this);
                  return;
                }
                String strStatus = JAMClienteDB.setConexionCerrar(rstConexionesCierre.getString("clave"), JAM060Messenger.this.chkBloqueaConexiones.isSelected());
                rstConexionesCierre.moveToCurrentRow();
                rstConexionesCierre.updateString("STATUS", strStatus);
                rstConexionesCierre.updateString("NOVEDAD", "");
                rstConexionesCierre.updateRow();
              }
            }
            JAM060Messenger.this.grdConexiones.SearchForNewRecords();
            JAM060Messenger.this.chkSeleccionaConexiones.setSelected(false);
            JAM060Messenger.this.chkBloqueaConexiones.setSelected(false);
            JAM060Messenger.this.setChkSelecciona();
          }
          catch (Exception eg)
          {
            JAMUtil.showDialog(eg.getMessage(), JAM060Messenger.this);
          }
          JAMCursor.setCursorOff(JAM060Messenger.this.btoCierraConexiones);
        }
      });
    }
    return this.btoCierraConexiones;
  }
  
  private JAMButtonVe getBtoRefrescaConexiones()
  {
    if (this.btoRefrescaConexiones == null)
    {
      this.btoRefrescaConexiones = new JAMButtonVe();
      this.btoRefrescaConexiones.setText("Refresca");
      this.btoRefrescaConexiones.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAMCursor.setCursorOn(JAM060Messenger.this.btoRefrescaConexiones);
          JAM060Messenger.this.chkSeleccionaConexiones.setSelected(false);
          JAM060Messenger.this.chkBloqueaConexiones.setSelected(false);
          JAM060Messenger.this.setChkSelecciona();
          JAM060Messenger.doConexionesActivas(JAM060Messenger.this.grdConexiones, false);
          JAMCursor.setCursorOff(JAM060Messenger.this.btoRefrescaConexiones);
        }
      });
    }
    return this.btoRefrescaConexiones;
  }
  
  private JAMGrid getGrdConexionesMessenger()
  {
    if (this.grdConexionesMessenger == null) {
      this.grdConexionesMessenger = new JAMGrid();
    }
    return this.grdConexionesMessenger;
  }
  
  private JPanel getChkPie()
  {
    if (this.chkPie == null)
    {
      this.chkPie = new JPanel();
      this.chkPie.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.chkPie.add(getChkSeleccionaConexiones(), "West");
      this.chkPie.add(getChkBloqueaConexiones(), "East");
    }
    return this.chkPie;
  }
  
  private JCheckBox getChkBloqueaConexiones()
  {
    if (this.chkBloqueaConexiones == null)
    {
      this.chkBloqueaConexiones = new JCheckBox();
      this.chkBloqueaConexiones.setText("Bloquea Conexi��n");
    }
    return this.chkBloqueaConexiones;
  }
  
  private JAMChatPanel getSplMessenger()
  {
    if (this.objMessenger == null) {
      this.objMessenger = new JAMChatPanel();
    }
    return this.objMessenger;
  }
}
