package mx.com.jammexico.jamlogon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxEditor;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JToggleButton;
import mx.com.jammexico.jambiometrix.VerificationDialog;
import mx.com.jammexico.jamcomponents.JAMCursor;
import mx.com.jammexico.jamcomponents.JAMLibKernel;
import mx.com.jammexico.jamcomponents.files.JAMAlmacenPropiedades;
import mx.com.jammexico.jamcomponents.jambuttons.JAMButtonAceptar;
import mx.com.jammexico.jamcomponents.jambuttons.JAMButtonCancelar;
import mx.com.jammexico.jamcomponents.visual.JAMInputNumber;
import mx.com.jammexico.jamcomponents.visual.JAMInputText;
import mx.com.jammexico.jamcomponents.visual.JAMLabel;
import mx.com.jammexico.jamdb.JAMClienteDB;
import mx.com.jammexico.jamdrivers.JAMSendLogon;
import mx.com.jammexico.jamsrv.JAMRowSet;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAM020Login
  extends JDialog
{
  private static final String SERVLET_CALL = "/JAMMexico/servlet/JAMServeletLogon";
  private String cServidor = "201.175.21.173";
  private String cServidorStk = "201.175.21.173";
  private int nPuerto = 8024;
  private int nPuertoSoc = 5000;
  private boolean logUserChat = true;
  private String cInstancias = "JAMON2, JAMSEG";
  private String cMainInstan = "DEFAULT";
  private String cUsuario = "ADMIN";
  private String cPassword = "AS";
  private String cNewPassword = null;
  private boolean flgAceptar = false;
  private boolean logFlagClave = false;
  private boolean logForProgram = false;
  private boolean logError = false;
  private JPanel pnlContainer1 = null;
  private JPanel pnlSeguridad = null;
  private JPanel pnlRed = null;
  private JAMLabel lblUsuario = null;
  private JAMLabel lblPassword = null;
  private JAMLabel lblLogo = null;
  private JAMLabel lblInstancia = null;
  private JAMLabel lblNuevaClave = null;
  private JAMLabel lblNuevaClaveRep = null;
  private JAMLabel lblServidor = null;
  private JAMLabel lblPuertoWeb = null;
  private JAMLabel lblInstancias = null;
  private JPasswordField txtPassword = null;
  private JPasswordField txtClaveNueva = null;
  private JPasswordField txtClaveNuevaRep = null;
  private JToggleButton btoCambiaPassword = null;
  private JToggleButton btoRed = null;
  private JAMInputText txtUsuario = null;
  private JAMInputText txtInstancia = null;
  private JComboBox cboServidor = null;
  private JAMInputText txtInstancias = null;
  private JAMInputNumber txtPuerto = null;
  private JAMButtonAceptar btoAceptar = null;
  private JAMButtonCancelar btoCancelar = null;
  private int intWidth = 365;
  private int intHeight;
  private int intHeightSeguridad;
  private int intHeightServidores;
  private JAM070VisorPanel Jam070Visor = null;
  private String strModoChat = "0";
  private JPanel pnlLogon = null;
  private JPanel pnlUsuario = null;
  private JPanel pnlClave = null;
  private JPanel pnlInstancia = null;
  private JPanel pnlBotonesLogon = null;
  private JPanel pnlLogonInput = null;
  private JPanel pnlCentro = null;
  private JPanel pnlClaveNueva = null;
  private JPanel pnlClaveRepite = null;
  private JPanel pnlServidorWeb = null;
  private JPanel pnlServidorSocket = null;
  private JAMLabel lblServidorSocket = null;
  private JComboBox cboServidorSocket = null;
  private JPanel pnlPuertoSocket = null;
  private JAMLabel lblPuertoSocket = null;
  private JAMInputNumber txtPuertoSocket = null;
  private JPanel pnlPuertoWeb = null;
  private JPanel pnlDBs = null;
  private JPanel pnlPuertos = null;
  private JPanel pnlPuertos1 = null;
  private JAMLabel lblPuertoServ = null;
  private JToggleButton chkModo = null;
  
  public JAM020Login()
    throws HeadlessException
  {
    getParametros();
    initialize();
    setAlwaysOnTop(true);
  }
  
  public JAM020Login(JAM070VisorPanel argVisor)
    throws HeadlessException
  {
    this.Jam070Visor = argVisor;
    if (JAMLibKernel.ParamJAMStartUp == null)
    {
      getParametros();
      initialize();
      setAlwaysOnTop(true);
    }
    else
    {
      this.cUsuario = JAMLibKernel.ParamJAMStartUp[0];
      this.cPassword = JAMLibKernel.ParamJAMStartUp[1];
      this.cMainInstan = JAMLibKernel.ParamJAMStartUp[2];
      this.cServidor = JAMLibKernel.ParamJAMStartUp[3];
      this.nPuerto = new Integer(JAMLibKernel.ParamJAMStartUp[4]).intValue();
      this.cInstancias = (JAMLibKernel.ParamJAMStartUp[5] + "," + JAMLibKernel.ParamJAMStartUp[6]);
      this.logUserChat = JAMUtil.JAMConvBoolean(JAMLibKernel.ParamJAMStartUp[7]);
      if ((this.cUsuario.equalsIgnoreCase("?")) && (this.cPassword.equalsIgnoreCase("?")))
      {
        getBtoRed().setEnabled(false);
        getBtoCambiaPassword().setEnabled(false);
        getTxtInstancia().setEditable(false);
        getParametros();
        initialize();
        setAlwaysOnTop(true);
        if (JAMLibKernel.ParamJAMStartUp[2].equalsIgnoreCase("?"))
        {
          getTxtInstancia().setText(this.cMainInstan);
          getTxtInstancia().setEditable(true);
        }
        return;
      }
      getParametros();
      this.logForProgram = true;
      if (LlamaValidacionServidor())
      {
        this.flgAceptar = true;
        dispose();
      }
    }
  }
  
  private void initialize()
  {
    setContentPane(getPnlContainer1());
    setResizable(false);
    setModal(true);
    setTitle("JAM V2.01 - Logon");
    
    setSize(new Dimension(365, 183));
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        JAM020Login.this.logError = false;
        JAM020Login.this.flgAceptar = false;
      }
      
      public void windowOpened(WindowEvent e)
      {
        JAM020Login.this.intHeight = (JAM020Login.this.pnlLogon.getHeight() + 32);
        JAM020Login.this.setSize(new Dimension(JAM020Login.this.intWidth, JAM020Login.this.intHeight));
      }
    });
    getPnlSeguridad();
    getPnlRed();
    
    this.intHeightSeguridad = getPnlSeguridad().getHeight();
    this.intHeightServidores = getPnlRed().getHeight();
  }
  
  public void setVisor(JAM070VisorPanel argVisor)
  {
    this.Jam070Visor = argVisor;
  }
  
  public boolean getButtonAceptar()
  {
    return this.flgAceptar;
  }
  
  public String getServidor()
  {
    return this.cServidor;
  }
  
  public String getServidorSocket()
  {
    return this.cServidorStk;
  }
  
  public String getInstancias()
  {
    return this.cInstancias;
  }
  
  public String getPuerto()
  {
    return new Integer(this.nPuerto).toString();
  }
  
  public String getPuertoSocket()
  {
    return new Integer(this.nPuertoSoc).toString();
  }
  
  public String getUser()
  {
    return this.cUsuario;
  }
  
  public String getPassword()
  {
    return this.cPassword;
  }
  
  public String getMainInstancia()
  {
    return this.cMainInstan;
  }
  
  public String getStatusChat()
  {
    return this.strModoChat;
  }
  
  public String getNewPass()
  {
    return this.cNewPassword;
  }
  
  public boolean getIsChat()
  {
    return this.logUserChat;
  }
  
  public String[] getListaServidores()
  {
    if (this.cboServidor == null) {
      getCboServidor().addItem(JAMUtil.JAMAddCombos(this.cServidor));
    }
    String[] arrServidores = new String[this.cboServidor.getItemCount()];
    for (int i = 0; i < this.cboServidor.getItemCount(); i++) {
      arrServidores[i] = this.cboServidor.getItemAt(i).toString();
    }
    return arrServidores;
  }
  
  private void getParametros()
  {
    JAMAlmacenPropiedades objLee = new JAMAlmacenPropiedades();
    try
    {
      if (objLee.getExists())
      {
        try
        {
          this.cUsuario = objLee.getPropiedad("Usuario");
          this.cServidorStk = objLee.getPropiedad("ServidorSocket");
          if (JAMLibKernel.ParamJAMStartUp == null)
          {
            if (!this.cServidor.equalsIgnoreCase("")) {
              this.cServidor = objLee.getPropiedad("Servidor");
            }
            this.cInstancias = objLee.getPropiedad("Dbinstancias");
            this.cMainInstan = objLee.getPropiedad("Instancia");
            this.nPuerto = new Integer(objLee.getPropiedad("Puerto")).intValue();
          }
          if (this.cMainInstan.equalsIgnoreCase("?")) {
            this.cMainInstan = objLee.getPropiedad("Instancia");
          }
        }
        catch (Exception localException1) {}
        int intCountLista = 0;
        String cCountLista = objLee.getPropiedad("CountLista");
        try
        {
          intCountLista = new Integer(cCountLista).intValue();
        }
        catch (Exception localException2) {}
        String[] arrLista = new String[intCountLista];
        for (int i = 0; i < intCountLista; i++)
        {
          String cClave = "Servidor" + JAMUtil.JAMRefill(new Integer(i).toString(), "0", 3);
          arrLista[i] = objLee.getPropiedad(cClave);
          getCboServidor().addItem(JAMUtil.JAMAddCombos(objLee.getPropiedad(cClave)));
        }
        int intPos = JAMUtil.JAMFindArray(arrLista, this.cServidor);
        if (intPos != -1)
        {
          this.cboServidor.setSelectedIndex(JAMUtil.JAMFindArray(arrLista, this.cServidor));
        }
        else if (JAMLibKernel.ParamJAMStartUp != null)
        {
          getCboServidor().addItem(JAMUtil.JAMAddCombos(this.cServidor));
          this.cboServidor.setSelectedIndex(this.cboServidor.getItemCount() - 1);
        }
        if ((this.cServidorStk.equalsIgnoreCase("")) || (this.cServidorStk.startsWith("ERROR"))) {
          getCboServidorSocket().addItem(this.cServidor);
        } else {
          getCboServidorSocket().addItem(this.cServidorStk);
        }
        getCboServidorSocket().setSelectedIndex(this.cboServidorSocket.getItemCount() - 1);
        getTxtUsuario().setText(this.cUsuario);
      }
      else
      {
        getCboServidor().addItem(JAMUtil.JAMAddCombos(this.cServidor));
        getCboServidorSocket().addItem(JAMUtil.JAMAddCombos(this.cServidor));
        this.cboServidor.setSelectedIndex(0);
        this.cboServidorSocket.setSelectedIndex(0);
      }
    }
    catch (Exception ee)
    {
      File fDel = new File(JAMAlmacenPropiedades.CONFIGURATION_FILE);
      fDel.delete();
    }
  }
  
  private JPanel getPnlContainer1()
  {
    if (this.pnlContainer1 == null)
    {
      this.pnlContainer1 = new JPanel();
      this.pnlContainer1.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlContainer1.add(getPnlLogon(), "North");
    }
    return this.pnlContainer1;
  }
  
  private JAMInputText getTxtUsuario()
  {
    if (this.txtUsuario == null)
    {
      this.txtUsuario = new JAMInputText(15);
      this.txtUsuario.setFont(new Font("Dialog", 3, 14));
      this.txtUsuario.addKeyListener(new KeyAdapter()
      {
        public void keyPressed(KeyEvent e)
        {
          if (e.getKeyCode() == 10) {
            JAM020Login.this.JAMConfiguraParametrosAcepta();
          }
        }
      });
    }
    return this.txtUsuario;
  }
  
  private JPasswordField getTxtPassword()
  {
    if (this.txtPassword == null)
    {
      this.txtPassword = new JPasswordField();
      this.txtPassword.setFont(new Font("Dialog", 3, 14));
      this.txtPassword.addKeyListener(new KeyAdapter()
      {
        public void keyPressed(KeyEvent e)
        {
          if (e.getKeyCode() == 10) {
            JAM020Login.this.JAMConfiguraParametrosAcepta();
          }
        }
      });
    }
    return this.txtPassword;
  }
  
  private JAMLabel getLblLogo()
  {
    if (this.lblLogo == null)
    {
      this.lblLogo = new JAMLabel(1);
      this.lblLogo.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 1));
      this.lblLogo.setPreferredSize(new Dimension(90, 21));
      try
      {
        this.lblLogo.setIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/logo360.png")));
      }
      catch (Exception localException) {}
      this.lblLogo.setHorizontalTextPosition(0);
      this.lblLogo.setHorizontalAlignment(0);
      this.lblLogo.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 1));
    }
    return this.lblLogo;
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
          JAM020Login.this.JAMConfiguraParametrosAcepta();
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
          JAM020Login.this.logError = false;
          JAM020Login.this.flgAceptar = false;
          JAM020Login.this.dispose();
        }
      });
    }
    return this.btoCancelar;
  }
  
  private JAMInputText getTxtInstancia()
  {
    if (this.txtInstancia == null)
    {
      this.txtInstancia = new JAMInputText(16);
      this.txtInstancia.setFont(new Font("Dialog", 3, 14));
      this.txtInstancia.setText(this.cMainInstan);
      this.txtInstancia.addKeyListener(new KeyAdapter()
      {
        public void keyPressed(KeyEvent e)
        {
          if (e.getKeyCode() == 10) {
            JAM020Login.this.JAMConfiguraParametrosAcepta();
          }
        }
      });
    }
    return this.txtInstancia;
  }
  
  private void JAMConfiguraParametrosAcepta()
  {
    String tmpPassReal = new String(this.txtPassword.getPassword());
    String tmpPass = new String(this.txtClaveNueva.getPassword());
    String tmpPassRep = new String(this.txtClaveNuevaRep.getPassword());
    if ((this.txtUsuario.getText().trim().equalsIgnoreCase("")) || 
      (tmpPassReal.trim().equalsIgnoreCase("")))
    {
      setCursor(JAMCursor.setCursorOff());
      doModalMensajes("No puede Ingresar Usuario y/o Clave vacios");
      return;
    }
    if (this.txtInstancia.getText().trim().equalsIgnoreCase(""))
    {
      setCursor(JAMCursor.setCursorOff());
      doModalMensajes("Debe Ingresar una Instancia");
      return;
    }
    if (this.logFlagClave)
    {
      if ((tmpPass.trim().equalsIgnoreCase("")) || 
        (tmpPassRep.trim().equalsIgnoreCase("")))
      {
        setCursor(JAMCursor.setCursorOff());
        doModalMensajes("No puede Ingresar Clave o Repeticion vacios");
        return;
      }
      if (!tmpPass.trim().equals(tmpPassRep.trim()))
      {
        setCursor(JAMCursor.setCursorOff());
        doModalMensajes("La nueva clave y su repiticion no son iguales");
        return;
      }
    }
    if (LlamaValidacionServidor())
    {
      this.cServidor = getStringCombo().trim();
      this.cServidorStk = getStringComboSocket().trim();
      this.nPuerto = this.txtPuerto.getNumber().intValue();
      this.nPuertoSoc = this.txtPuertoSocket.getNumber().intValue();
      this.cInstancias = this.txtInstancias.getText().trim();
      this.cMainInstan = this.txtInstancia.getText().trim();
      this.cUsuario = this.txtUsuario.getText().trim().toUpperCase();
      this.cPassword = new String(this.txtPassword.getPassword()).trim();
      if (this.logFlagClave) {
        this.cNewPassword = new String(this.txtClaveNueva.getPassword()).trim();
      }
      this.flgAceptar = true;
      dispose();
    }
  }
  
  private JToggleButton getBtoCambiaPassword()
  {
    if (this.btoCambiaPassword == null)
    {
      this.btoCambiaPassword = new JToggleButton();
      this.btoCambiaPassword.setIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/logon/segu.png")));
      this.btoCambiaPassword.setToolTipText("Modifica clave");
      this.btoCambiaPassword.setPreferredSize(new Dimension(31, 21));
      this.btoCambiaPassword.addItemListener(new ItemListener()
      {
        public void itemStateChanged(ItemEvent e)
        {
          String tmpPassReal = new String(JAM020Login.this.txtPassword.getPassword());
          
          JAM020Login.this.txtClaveNueva.setText("");
          JAM020Login.this.txtClaveNuevaRep.setText("");
          if (JAMUtil.JAMConvBoolean(JAM020Login.this.btoCambiaPassword.getSelectedObjects()) == 1)
          {
            if ((JAM020Login.this.txtUsuario.getText().trim().equalsIgnoreCase("")) || 
              (tmpPassReal.trim().equalsIgnoreCase("")))
            {
              JAM020Login.this.doModalMensajes("No puede Ingresar Usuario y/o Clave vacios");
              JAM020Login.this.btoCambiaPassword.setSelected(false);
              return;
            }
            if (JAM020Login.this.txtInstancia.getText().trim().equalsIgnoreCase(""))
            {
              JAM020Login.this.doModalMensajes("Debe Ingresar una Instancia");
              JAM020Login.this.btoCambiaPassword.setSelected(false);
              return;
            }
            JAM020Login.this.pnlContainer1.add(JAM020Login.this.getPnlSeguridad(), "Center");
            JAM020Login.this.setSize(new Dimension(JAM020Login.this.intWidth, JAM020Login.this.intHeight + JAM020Login.this.intHeightSeguridad));
            JAM020Login.this.btoRed.setEnabled(false);
          }
          else
          {
            JAM020Login.this.pnlContainer1.remove(JAM020Login.this.getPnlSeguridad());
            JAM020Login.this.setSize(new Dimension(JAM020Login.this.intWidth, JAM020Login.this.intHeight));
            JAM020Login.this.btoRed.setEnabled(true);
          }
        }
      });
      this.btoCambiaPassword.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e) {}
      });
    }
    return this.btoCambiaPassword;
  }
  
  private String getStringCombo()
  {
    String cValidaServidor = this.cboServidor.getEditor().getItem().toString();
    if (cValidaServidor.trim().equalsIgnoreCase("")) {
      return this.cServidor;
    }
    return this.cboServidor.getEditor().getItem().toString();
  }
  
  private String getStringComboSocket()
  {
    String cValidaServidor = this.cboServidorSocket.getEditor().getItem().toString();
    if (cValidaServidor.trim().equalsIgnoreCase("")) {
      return this.cServidorStk;
    }
    return this.cboServidorSocket.getEditor().getItem().toString();
  }
  
  private JPasswordField getTxtClaveNueva()
  {
    if (this.txtClaveNueva == null)
    {
      this.txtClaveNueva = new JPasswordField();
      this.txtClaveNueva.addKeyListener(new KeyAdapter()
      {
        public void keyPressed(KeyEvent e)
        {
          if (e.getKeyCode() == 10) {
            JAM020Login.this.JAMConfiguraParametrosAcepta();
          }
        }
      });
    }
    return this.txtClaveNueva;
  }
  
  private JPasswordField getTxtClaveNuevaRep()
  {
    if (this.txtClaveNuevaRep == null) {
      this.txtClaveNuevaRep = new JPasswordField();
    }
    return this.txtClaveNuevaRep;
  }
  
  private JPanel getPnlSeguridad()
  {
    if (this.pnlSeguridad == null)
    {
      this.pnlSeguridad = new JPanel();
      this.pnlSeguridad.setLayout(JAMUtil.JAMGridLayout(2, 1, 5, 5));
      this.pnlSeguridad.setSize(new Dimension(407, 55));
      this.pnlSeguridad.setPreferredSize(new Dimension(506, 115));
      this.pnlSeguridad.add(getPnlClaveNueva(), null);
      this.pnlSeguridad.add(getPnlClaveRepite(), null);
    }
    return this.pnlSeguridad;
  }
  
  private JPanel getPnlRed()
  {
    if (this.pnlRed == null)
    {
      this.pnlRed = new JPanel();
      this.pnlRed.setLayout(JAMUtil.JAMGridLayout(4, 1, 5, 5));
      this.pnlRed.setSize(new Dimension(405, 115));
      this.pnlRed.setPreferredSize(new Dimension(506, 115));
      this.pnlRed.add(getPnlServidorWeb(), null);
      this.pnlRed.add(getPnlServidorSocket(), null);
      this.pnlRed.add(getPnlDBs(), null);
      this.pnlRed.add(getPnlPuertos(), null);
    }
    return this.pnlRed;
  }
  
  private JAMInputNumber getTxtPuerto()
  {
    if (this.txtPuerto == null)
    {
      this.txtPuerto = new JAMInputNumber(4, JAMInputNumber.JAMINT);
      this.txtPuerto.setFormat(false);
      this.txtPuerto.setFontJam(new Font("Dialog", 3, 14));
      this.txtPuerto.setHorizontalAlignment(0);
      this.txtPuerto.setNumber(this.nPuerto);
      this.txtPuerto.addKeyListener(new KeyAdapter()
      {
        public void keyPressed(KeyEvent e)
        {
          if (e.getKeyCode() == 10) {
            JAM020Login.this.JAMConfiguraParametrosAcepta();
          }
        }
      });
    }
    return this.txtPuerto;
  }
  
  private JAMInputText getTxtInstancias()
  {
    if (this.txtInstancias == null)
    {
      this.txtInstancias = new JAMInputText(100);
      this.txtInstancias.setFont(new Font("Dialog", 3, 14));
      this.txtInstancias.setText(this.cInstancias);
      this.txtInstancias.addKeyListener(new KeyAdapter()
      {
        public void keyPressed(KeyEvent e)
        {
          if (e.getKeyCode() == 10) {
            JAM020Login.this.JAMConfiguraParametrosAcepta();
          }
        }
      });
    }
    return this.txtInstancias;
  }
  
  private JComboBox getCboServidor()
  {
    if (this.cboServidor == null)
    {
      this.cboServidor = new JComboBox();
      this.cboServidor.setEditable(true);
      this.cboServidor.setFont(new Font("Dialog", 3, 14));
      this.cboServidor.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAM020Login.this.getCboServidorSocket().getEditor().setItem(JAM020Login.this.cboServidor.getEditor().getItem().toString());
        }
      });
    }
    return this.cboServidor;
  }
  
  private JToggleButton getBtoRed()
  {
    if (this.btoRed == null)
    {
      this.btoRed = new JToggleButton();
      this.btoRed.setIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/logon/servidor.png")));
      this.btoRed.setToolTipText("Configura Conexion");
      this.btoRed.setPreferredSize(new Dimension(31, 21));
      this.btoRed.addItemListener(new ItemListener()
      {
        public void itemStateChanged(ItemEvent e)
        {
          if (JAMUtil.JAMConvBoolean(JAM020Login.this.btoRed.getSelectedObjects()) == 1)
          {
            JAM020Login.this.pnlContainer1.add(JAM020Login.this.getPnlRed(), "Center");
            JAM020Login.this.setSize(new Dimension(JAM020Login.this.intWidth, JAM020Login.this.intHeight + JAM020Login.this.intHeightServidores));
            JAM020Login.this.btoCambiaPassword.setEnabled(false);
          }
          else
          {
            JAM020Login.this.pnlContainer1.remove(JAM020Login.this.getPnlRed());
            JAM020Login.this.setSize(new Dimension(JAM020Login.this.intWidth, JAM020Login.this.intHeight));
            JAM020Login.this.btoCambiaPassword.setEnabled(true);
          }
        }
      });
      this.btoRed.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e) {}
      });
    }
    return this.btoRed;
  }
  
  private boolean LlamaValidacionServidor()
  {
    try
    {
      setCursor(JAMCursor.setCursorOn(this));
      if (!doLlamaServeLetLogin()) {
        return false;
      }
      setCursor(JAMCursor.setCursorOff(this));
    }
    catch (IOException e)
    {
      setCursor(JAMCursor.setCursorOff(this));
      doModalMensajes("Error al Intentar Validar al Usuario en el Servidor : " + e.getMessage());
      return false;
    }
    catch (Exception e)
    {
      setCursor(JAMCursor.setCursorOff(this));
      doModalMensajes("Error al Intentar Existencia del Usuario : " + e.getMessage());
      return false;
    }
    return true;
  }
  
  public boolean getError()
  {
    return this.logError;
  }
  
  private void doModalMensajes(String argMensaje)
  {
    this.logError = true;
    if (this.Jam070Visor == null) {
      JAMUtil.showDialog(argMensaje);
    } else {
      this.Jam070Visor.setErrores(argMensaje);
    }
  }
  
  private boolean doLlamaWebServiceLogin()
    throws Exception
  {
    String strNewPass = new String(this.txtClaveNueva.getPassword()).trim();
    if (this.logForProgram)
    {
      JAMLibKernel.ParamJAMInstname = this.cMainInstan;
      JAMLibKernel.ParamJAMUsername = this.cUsuario;
      JAMLibKernel.ParamJAMUserpass = this.cPassword;
    }
    else
    {
      JAMLibKernel.ParamJAMInstname = this.txtInstancia.getText();
      JAMLibKernel.ParamJAMUsername = this.txtUsuario.getText().trim().toUpperCase();
      JAMLibKernel.ParamJAMUserpass = new String(this.txtPassword.getPassword()).trim();
    }
    String strSql = "SELECT * FROM SOCUSUA02_MAE_USUARIOS where SOCUSUA02_USUARIO = '" + JAMLibKernel.ParamJAMUsername + 
      "' and SOCUSUA02_PASSWORD = '" + JAMLibKernel.ParamJAMUserpass + "'";
    
    JAMRowSet rstLogon = JAMClienteDB.getRowSets(new String[] { strSql })[0];
    
    return true;
  }
  
  private boolean doLlamaServeLetLogin()
    throws IOException
  {
    String cConnectionUrl = null;
    JAMSendLogon sendLogon = new JAMSendLogon();
    if (this.logForProgram)
    {
      cConnectionUrl = "http://" + this.cServidor + ":" + new Integer(this.nPuerto).toString() + "/JAMMexico/servlet/JAMServeletLogon";
      sendLogon.setConnectionDB(this.cMainInstan);
      sendLogon.setUser(this.cUsuario);
      sendLogon.setPass(this.cPassword);
    }
    else
    {
      cConnectionUrl = "http://" + getStringCombo() + ":" + new Integer(this.txtPuerto.getNumber().intValue()).toString() + "/JAMMexico/servlet/JAMServeletLogon";
      sendLogon.setConnectionDB(this.txtInstancia.getText());
      sendLogon.setUser(this.txtUsuario.getText().trim().toUpperCase());
      sendLogon.setPass(new String(this.txtPassword.getPassword()).trim());
      sendLogon.setNewPass(new String(this.txtClaveNueva.getPassword()).trim());
    }
    URL miurl = new URL(cConnectionUrl);
    URLConnection conexion = miurl.openConnection();
    conexion.setDoInput(true);
    conexion.setDoOutput(true);
    conexion.setUseCaches(false);
    conexion.setDefaultUseCaches(false);
    conexion.setRequestProperty("Content-Type", "java-internal/" + sendLogon.getClass().getName());
    conexion.connect();
    
    ObjectOutputStream output = new ObjectOutputStream(conexion.getOutputStream());
    output.writeObject(sendLogon);
    output.flush();
    
    JAMSendLogon recibe = null;
    try
    {
      ObjectInputStream input = new ObjectInputStream(conexion.getInputStream());
      Object response = input.readObject();
      recibe = (JAMSendLogon)response;
      output.close();
      input.close();
    }
    catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    if (recibe.getError() != null)
    {
      setCursor(JAMCursor.setCursorOff(this));
      doModalMensajes(recibe.getError());
      return false;
    }
    String cServidorName = "servidor01";
    JAMLibKernel.setServerName(cServidorName);
    if (recibe.getServidorname() != null) {
      JAMLibKernel.setServerName(recibe.getServidorname());
    }
    JAMClienteDB.setTimeOut(recibe.getTimeout());
    
    this.strModoChat = recibe.getChat();
    if ((recibe.getNewPass() != null) && (recibe.getNewPass().equalsIgnoreCase("OK"))) {
      doModalMensajes("Cambio de Password realizado con exito");
    }
    if (recibe.getHuelladigital() != 0)
    {
      String strUrlHD = "http://" + this.cboServidor.getItemAt(this.cboServidor.getSelectedIndex()) + ":" + this.txtPuerto.getNumberStr() + "/JAMMexico/tempfiles/dbfiles/" + this.txtInstancia.getText().toLowerCase() + "/";
      VerificationDialog objVeri = new VerificationDialog(recibe.getHuelladigital(), false, strUrlHD, this);
      objVeri.setVisible(true);
      if (!objVeri.getMatched())
      {
        doModalMensajes("Error de SEGURIDAD por huella digital : Tiene activa la restriccion");
        return false;
      }
    }
    setCursor(JAMCursor.setCursorOff(this));
    return true;
  }
  
  private JPanel getPnlLogon()
  {
    if (this.pnlLogon == null)
    {
      this.pnlLogon = new JPanel();
      JPanel pnlTempTop = new JPanel();
      JPanel pnlTempLef = new JPanel();
      pnlTempTop.setPreferredSize(new Dimension(1, 1));
      pnlTempLef.setPreferredSize(new Dimension(1, 1));
      this.pnlLogon.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlLogon.add(getLblLogo(), "West");
      this.pnlLogon.add(getPnlLogonInput(), "Center");
      this.pnlLogon.add(pnlTempLef, "East");
      this.pnlLogon.add(pnlTempTop, "North");
    }
    return this.pnlLogon;
  }
  
  private JPanel getPnlUsuario()
  {
    if (this.pnlUsuario == null)
    {
      this.pnlUsuario = new JPanel();
      this.pnlUsuario.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      
      this.lblUsuario = new JAMLabel(1);
      this.lblUsuario.setFont(new Font("Dialog", 3, 14));
      this.lblUsuario.setIconInput(1);
      this.lblUsuario.setText("Usuario :");
      this.lblUsuario.setPreferredSize(new Dimension(100, 21));
      
      this.pnlUsuario.add(this.lblUsuario, "West");
      this.pnlUsuario.add(getTxtUsuario(), "Center");
    }
    return this.pnlUsuario;
  }
  
  private JPanel getPnlClave()
  {
    if (this.pnlClave == null)
    {
      this.pnlClave = new JPanel();
      this.pnlClave.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      
      this.lblPassword = new JAMLabel(1);
      this.lblPassword.setFont(new Font("Dialog", 3, 14));
      this.lblPassword.setText("Clave :");
      this.lblPassword.setIconInput(1);
      this.lblPassword.setPreferredSize(new Dimension(100, 21));
      
      this.pnlClave.add(this.lblPassword, "West");
      this.pnlClave.add(getBtoCambiaPassword(), "East");
      this.pnlClave.add(getTxtPassword(), "Center");
    }
    return this.pnlClave;
  }
  
  private JPanel getPnlInstancia()
  {
    if (this.pnlInstancia == null)
    {
      this.pnlInstancia = new JPanel();
      this.pnlInstancia.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      
      this.lblInstancia = new JAMLabel(1);
      this.lblInstancia.setFont(new Font("Dialog", 3, 14));
      this.lblInstancia.setText(" Instancia:");
      this.lblInstancia.setIconInput(1);
      this.lblInstancia.setPreferredSize(new Dimension(100, 21));
      
      this.pnlInstancia.add(this.lblInstancia, "West");
      this.pnlInstancia.add(getBtoRed(), "East");
      this.pnlInstancia.add(getTxtInstancia(), "Center");
    }
    return this.pnlInstancia;
  }
  
  private JPanel getPnlBotonesLogon()
  {
    if (this.pnlBotonesLogon == null)
    {
      this.pnlBotonesLogon = new JPanel();
      this.pnlBotonesLogon.setLayout(JAMUtil.JAMGridLayout(1, 2, 5, 5));
      this.pnlBotonesLogon.add(getCancelar(), null);
      this.pnlBotonesLogon.add(getBtoAceptar(), null);
    }
    return this.pnlBotonesLogon;
  }
  
  private JPanel getPnlLogonInput()
  {
    if (this.pnlLogonInput == null)
    {
      this.pnlLogonInput = new JPanel();
      this.pnlLogonInput.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlLogonInput.add(getPnlBotonesLogon(), "South");
      this.pnlLogonInput.add(getPnlCentro(), "Center");
    }
    return this.pnlLogonInput;
  }
  
  private JPanel getPnlCentro()
  {
    if (this.pnlCentro == null)
    {
      this.pnlCentro = new JPanel();
      this.pnlCentro.setLayout(JAMUtil.JAMGridLayout(3, 1, 5, 5));
      this.pnlCentro.add(getPnlUsuario(), null);
      this.pnlCentro.add(getPnlClave(), null);
      this.pnlCentro.add(getPnlInstancia(), null);
    }
    return this.pnlCentro;
  }
  
  private JPanel getPnlClaveNueva()
  {
    if (this.pnlClaveNueva == null)
    {
      this.pnlClaveNueva = new JPanel();
      this.pnlClaveNueva.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      
      this.lblNuevaClave = new JAMLabel(1);
      this.lblNuevaClave.setFont(new Font("Dialog", 3, 14));
      this.lblNuevaClave.setText("Nueva Clave:");
      this.lblNuevaClave.setIconInput(1);
      this.lblNuevaClave.setPreferredSize(new Dimension(120, 21));
      
      this.pnlClaveNueva.add(this.lblNuevaClave, "West");
      this.pnlClaveNueva.add(getTxtClaveNueva(), "Center");
    }
    return this.pnlClaveNueva;
  }
  
  private JPanel getPnlClaveRepite()
  {
    if (this.pnlClaveRepite == null)
    {
      this.pnlClaveRepite = new JPanel();
      this.pnlClaveRepite.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      
      this.lblNuevaClaveRep = new JAMLabel(1);
      this.lblNuevaClaveRep.setFont(new Font("Dialog", 3, 14));
      this.lblNuevaClaveRep.setText("Repita Clave:");
      this.lblNuevaClaveRep.setIconInput(1);
      this.lblNuevaClaveRep.setPreferredSize(new Dimension(120, 21));
      
      this.pnlClaveRepite.add(this.lblNuevaClaveRep, "West");
      this.pnlClaveRepite.add(getTxtClaveNuevaRep(), "Center");
    }
    return this.pnlClaveRepite;
  }
  
  private JPanel getPnlServidorWeb()
  {
    if (this.pnlServidorWeb == null)
    {
      this.pnlServidorWeb = new JPanel();
      this.pnlServidorWeb.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      
      this.lblServidor = new JAMLabel(1);
      this.lblServidor.setText("Servidor :");
      this.lblServidor.setIconInput(1);
      this.lblServidor.setPreferredSize(new Dimension(90, 21));
      
      this.pnlServidorWeb.add(this.lblServidor, "West");
      this.pnlServidorWeb.add(getCboServidor(), "Center");
    }
    return this.pnlServidorWeb;
  }
  
  private JPanel getPnlServidorSocket()
  {
    if (this.pnlServidorSocket == null)
    {
      this.pnlServidorSocket = new JPanel();
      this.pnlServidorSocket.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      
      this.lblServidorSocket = new JAMLabel(1);
      this.lblServidorSocket.setPreferredSize(new Dimension(90, 21));
      this.lblServidorSocket.setText("Enlace :");
      this.lblServidorSocket.setIconInput(1);
      
      this.pnlServidorSocket.add(this.lblServidorSocket, "West");
      this.pnlServidorSocket.add(getCboServidorSocket(), "Center");
    }
    return this.pnlServidorSocket;
  }
  
  private JComboBox getCboServidorSocket()
  {
    if (this.cboServidorSocket == null)
    {
      this.cboServidorSocket = new JComboBox();
      this.cboServidorSocket.setFont(new Font("Dialog", 3, 14));
      this.cboServidorSocket.setEditable(true);
    }
    return this.cboServidorSocket;
  }
  
  private JPanel getPnlPuertoSocket()
  {
    if (this.pnlPuertoSocket == null)
    {
      this.pnlPuertoSocket = new JPanel();
      this.pnlPuertoSocket.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlPuertoSocket.setPreferredSize(new Dimension(125, 21));
      
      this.lblPuertoSocket = new JAMLabel(1);
      this.lblPuertoSocket.setText("Enlace");
      this.lblPuertoSocket.setIconInput(1);
      this.lblPuertoSocket.setPreferredSize(new Dimension(70, 21));
      
      this.pnlPuertoSocket.add(this.lblPuertoSocket, "West");
      this.pnlPuertoSocket.add(getTxtPuertoSocket(), "Center");
    }
    return this.pnlPuertoSocket;
  }
  
  private JAMInputNumber getTxtPuertoSocket()
  {
    if (this.txtPuertoSocket == null)
    {
      this.txtPuertoSocket = new JAMInputNumber(4, JAMInputNumber.JAMINT);
      this.txtPuertoSocket.setFormat(false);
      this.txtPuertoSocket.setFontJam(new Font("Dialog", 3, 14));
      this.txtPuertoSocket.setHorizontalAlignment(0);
      this.txtPuertoSocket.setNumber(this.nPuertoSoc);
      this.txtPuertoSocket.addKeyListener(new KeyAdapter()
      {
        public void keyPressed(KeyEvent e)
        {
          if (e.getKeyCode() == 10) {
            JAM020Login.this.JAMConfiguraParametrosAcepta();
          }
        }
      });
    }
    return this.txtPuertoSocket;
  }
  
  private JPanel getPnlPuertoWeb()
  {
    if (this.pnlPuertoWeb == null)
    {
      this.lblPuertoServ = new JAMLabel(1);
      this.lblPuertoServ.setText("Servidor");
      this.lblPuertoServ.setIconInput(0);
      
      this.pnlPuertoWeb = new JPanel();
      this.pnlPuertoWeb.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlPuertoWeb.setPreferredSize(new Dimension(230, 21));
      
      this.lblPuertoWeb = new JAMLabel(1);
      this.lblPuertoWeb.setText("Puertos :");
      this.lblPuertoWeb.setIconInput(1);
      this.lblPuertoWeb.setPreferredSize(new Dimension(90, 21));
      
      this.pnlPuertoWeb.add(this.lblPuertoWeb, "West");
      this.pnlPuertoWeb.add(this.lblPuertoServ, "Center");
      this.pnlPuertoWeb.add(getTxtPuerto(), "East");
    }
    return this.pnlPuertoWeb;
  }
  
  private JPanel getPnlDBs()
  {
    if (this.pnlDBs == null)
    {
      this.pnlDBs = new JPanel();
      this.pnlDBs.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      
      this.lblInstancias = new JAMLabel(1);
      this.lblInstancias.setText("DBs :");
      this.lblInstancias.setIconInput(1);
      this.lblInstancias.setPreferredSize(new Dimension(90, 21));
      
      this.pnlDBs.add(this.lblInstancias, "West");
      this.pnlDBs.add(getChkModo(), "East");
      this.pnlDBs.add(getTxtInstancias(), "Center");
    }
    return this.pnlDBs;
  }
  
  private JPanel getPnlPuertos()
  {
    if (this.pnlPuertos == null)
    {
      this.pnlPuertos = new JPanel();
      this.pnlPuertos.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlPuertos.setSize(new Dimension(495, 37));
      this.pnlPuertos.add(getPnlPuertoWeb(), "West");
      this.pnlPuertos.add(getPnlPuertos1(), "Center");
    }
    return this.pnlPuertos;
  }
  
  private JPanel getPnlPuertos1()
  {
    if (this.pnlPuertos1 == null)
    {
      this.pnlPuertos1 = new JPanel();
      this.pnlPuertos1.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlPuertos1.add(getPnlPuertoSocket(), "West");
    }
    return this.pnlPuertos1;
  }
  
  private JToggleButton getChkModo()
  {
    if (this.chkModo == null)
    {
      this.chkModo = new JToggleButton();
      this.chkModo.setText("WebService");
      this.chkModo.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          if (JAM020Login.this.chkModo.isSelected())
          {
            JAM020Login.this.chkModo.setText("WebService");
            JAMClienteDB.setModoConexion(1);
          }
          else
          {
            JAM020Login.this.chkModo.setText("ServLet");
            JAMClienteDB.setModoConexion(0);
          }
        }
      });
    }
    return this.chkModo;
  }
}
