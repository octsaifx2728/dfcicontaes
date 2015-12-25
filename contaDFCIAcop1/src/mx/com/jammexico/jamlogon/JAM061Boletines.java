package mx.com.jammexico.jamlogon;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.MenuComponent;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import mx.com.jammexico.jamcomponents.jambuttons.JAMButtonAceptar;
import mx.com.jammexico.jamcomponents.jambuttons.JAMButtonCancelar;
import mx.com.jammexico.jamcomponents.jamform.JAMFormModal;
import mx.com.jammexico.jamcomponents.jamtabpanel.JAMTab;
import mx.com.jammexico.jamcomponents.visual.JAMAreaTexto;
import mx.com.jammexico.jamcomponents.visual.JAMInputText;
import mx.com.jammexico.jamcomponents.visual.JAMLabel;
import mx.com.jammexico.jamsrv.JAMRowSet;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAM061Boletines
  extends JAMFormModal
{
  private JPanel jContentPane = null;
  private JAMTab tabMain = null;
  private JPanel tabOrganizacion = null;
  private JPanel tabCargo = null;
  private JPanel tabUsuario = null;
  private JAMAreaTexto txtNotasOrganizacion = null;
  private JPanel pnlNotasOrganizacion = null;
  private JPanel pnlTituloOrgannizacion = null;
  private JAMLabel lblTituloOrganizacion = null;
  private JAMInputText txtTituloOrganizacion = null;
  private JPanel pnlNotasCargo = null;
  private JPanel pnlTituloCargo = null;
  private JAMLabel lblTituloCargo = null;
  private JAMInputText txtTituloCargo = null;
  private JAMAreaTexto txtNotasCargo = null;
  private JPanel pnlNotasUsuario = null;
  private JPanel pnlTituloUsuario = null;
  private JAMLabel lblTituloUsuario = null;
  private JAMInputText txtTituloUsuario = null;
  private JAMAreaTexto txtNotasUsuario = null;
  private JPanel pnlMenuButtons = null;
  private JAMButtonAceptar btoAceptar = null;
  private JAMButtonCancelar btoCancelar = null;
  private JAMRowSet rstBoletines = null;
  private boolean[] logBloquea = new boolean[3];
  
  public JAM061Boletines(JAMRowSet argRowSet)
  {
    this.rstBoletines = argRowSet;
    initialize();
  }
  
  public JAM061Boletines(String title)
  {
    super(title);
    
    initialize();
  }
  
  public JAM061Boletines(String title, boolean resizable)
  {
    super(title, resizable);
    
    initialize();
  }
  
  public JAM061Boletines(String title, boolean resizable, boolean closable)
  {
    super(title, resizable, closable);
    
    initialize();
  }
  
  public JAM061Boletines(String title, boolean resizable, boolean closable, boolean maximizable)
  {
    super(title, resizable, closable, maximizable);
    
    initialize();
  }
  
  public JAM061Boletines(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable)
  {
    super(title, resizable, closable, maximizable, iconifiable);
    
    initialize();
  }
  
  private JAMTab getTabMain()
  {
    if (this.tabMain == null)
    {
      this.tabMain = new JAMTab();
      this.tabMain.addTab("Organizacion", new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/pymes/datosc.png")), getTabOrganizacion(), null);
      this.tabMain.addTab("Cargo", new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/pymes/cajero.png")), getTabCargo(), null);
      this.tabMain.addTab("Usuario", new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/pymes/clientes.png")), getTabUsuario(), null);
    }
    return this.tabMain;
  }
  
  private JPanel getTabOrganizacion()
  {
    if (this.tabOrganizacion == null)
    {
      this.tabOrganizacion = new JPanel();
      this.tabOrganizacion.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.tabOrganizacion.add(getPnlNotasOrganizacion(), "Center");
    }
    return this.tabOrganizacion;
  }
  
  private JPanel getTabCargo()
  {
    if (this.tabCargo == null)
    {
      this.tabCargo = new JPanel();
      this.tabCargo.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.tabCargo.add(getPnlNotasCargo(), "Center");
    }
    return this.tabCargo;
  }
  
  private JPanel getTabUsuario()
  {
    if (this.tabUsuario == null)
    {
      this.tabUsuario = new JPanel();
      this.tabUsuario.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.tabUsuario.add(getPnlNotasUsuario(), "Center");
    }
    return this.tabUsuario;
  }
  
  private JAMAreaTexto getTxtNotasOrganizacion()
  {
    if (this.txtNotasOrganizacion == null)
    {
      this.txtNotasOrganizacion = new JAMAreaTexto();
      this.txtNotasOrganizacion.setEditable(false);
      this.txtNotasOrganizacion.setJAMFont(new Font("Dialog", 1, 18));
    }
    return this.txtNotasOrganizacion;
  }
  
  private JPanel getPnlNotasOrganizacion()
  {
    if (this.pnlNotasOrganizacion == null)
    {
      this.pnlNotasOrganizacion = new JPanel();
      this.pnlNotasOrganizacion.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlNotasOrganizacion.add(getPnlTituloOrgannizacion(), "North");
      this.pnlNotasOrganizacion.add(getTxtNotasOrganizacion(), "Center");
    }
    return this.pnlNotasOrganizacion;
  }
  
  private JPanel getPnlTituloOrgannizacion()
  {
    if (this.pnlTituloOrgannizacion == null)
    {
      this.pnlTituloOrgannizacion = new JPanel();
      this.pnlTituloOrgannizacion.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlTituloOrgannizacion.add(getLblTituloOrganizacion(), "West");
      this.pnlTituloOrgannizacion.add(getTxtTituloOrganizacion(), "Center");
    }
    return this.pnlTituloOrgannizacion;
  }
  
  private JAMLabel getLblTituloOrganizacion()
  {
    if (this.lblTituloOrganizacion == null)
    {
      this.lblTituloOrganizacion = new JAMLabel(1);
      this.lblTituloOrganizacion.setPreferredSize(new Dimension(120, 21));
      this.lblTituloOrganizacion.setIconInput(1);
      this.lblTituloOrganizacion.setText("Titulo :");
    }
    return this.lblTituloOrganizacion;
  }
  
  private JAMInputText getTxtTituloOrganizacion()
  {
    if (this.txtTituloOrganizacion == null)
    {
      this.txtTituloOrganizacion = new JAMInputText(100);
      this.txtTituloOrganizacion.setEditable(false);
    }
    return this.txtTituloOrganizacion;
  }
  
  private JPanel getPnlNotasCargo()
  {
    if (this.pnlNotasCargo == null)
    {
      this.pnlNotasCargo = new JPanel();
      this.pnlNotasCargo.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlNotasCargo.add(getPnlTituloCargo(), "North");
      this.pnlNotasCargo.add(getTxtNotasCargo(), "Center");
    }
    return this.pnlNotasCargo;
  }
  
  private JPanel getPnlTituloCargo()
  {
    if (this.pnlTituloCargo == null)
    {
      this.pnlTituloCargo = new JPanel();
      this.pnlTituloCargo.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlTituloCargo.add(getLblTituloCargo(), "West");
      this.pnlTituloCargo.add(getTxtTituloCargo(), "Center");
    }
    return this.pnlTituloCargo;
  }
  
  private JAMLabel getLblTituloCargo()
  {
    if (this.lblTituloCargo == null)
    {
      this.lblTituloCargo = new JAMLabel(1);
      this.lblTituloCargo.setPreferredSize(new Dimension(120, 21));
      this.lblTituloCargo.setIconInput(1);
      this.lblTituloCargo.setText("Titulo :");
    }
    return this.lblTituloCargo;
  }
  
  private JAMInputText getTxtTituloCargo()
  {
    if (this.txtTituloCargo == null)
    {
      this.txtTituloCargo = new JAMInputText(100);
      this.txtTituloCargo.setEditable(false);
    }
    return this.txtTituloCargo;
  }
  
  private JAMAreaTexto getTxtNotasCargo()
  {
    if (this.txtNotasCargo == null)
    {
      this.txtNotasCargo = new JAMAreaTexto();
      this.txtNotasCargo.setJAMFont(new Font("Dialog", 1, 18));
      this.txtNotasCargo.setEditable(false);
    }
    return this.txtNotasCargo;
  }
  
  private JPanel getPnlNotasUsuario()
  {
    if (this.pnlNotasUsuario == null)
    {
      this.pnlNotasUsuario = new JPanel();
      this.pnlNotasUsuario.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlNotasUsuario.add(getPnlTituloUsuario(), "North");
      this.pnlNotasUsuario.add(getTxtNotasUsuario(), "Center");
    }
    return this.pnlNotasUsuario;
  }
  
  private JPanel getPnlTituloUsuario()
  {
    if (this.pnlTituloUsuario == null)
    {
      this.pnlTituloUsuario = new JPanel();
      this.pnlTituloUsuario.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlTituloUsuario.add(getLblTituloUsuario(), "West");
      this.pnlTituloUsuario.add(getTxtTituloUsuario(), "Center");
    }
    return this.pnlTituloUsuario;
  }
  
  private JAMLabel getLblTituloUsuario()
  {
    if (this.lblTituloUsuario == null)
    {
      this.lblTituloUsuario = new JAMLabel(1);
      this.lblTituloUsuario.setPreferredSize(new Dimension(120, 21));
      this.lblTituloUsuario.setIconInput(1);
      this.lblTituloUsuario.setText("Titulo :");
    }
    return this.lblTituloUsuario;
  }
  
  private JAMInputText getTxtTituloUsuario()
  {
    if (this.txtTituloUsuario == null)
    {
      this.txtTituloUsuario = new JAMInputText(100);
      this.txtTituloUsuario.setEditable(false);
    }
    return this.txtTituloUsuario;
  }
  
  private JAMAreaTexto getTxtNotasUsuario()
  {
    if (this.txtNotasUsuario == null)
    {
      this.txtNotasUsuario = new JAMAreaTexto();
      this.txtNotasUsuario.setJAMFont(new Font("Dialog", 1, 18));
      this.txtNotasUsuario.setEditable(false);
    }
    return this.txtNotasUsuario;
  }
  
  private JPanel getPnlMenuButtons()
  {
    if (this.pnlMenuButtons == null)
    {
      this.pnlMenuButtons = new JPanel();
      this.pnlMenuButtons.setLayout(new BorderLayout());
      this.pnlMenuButtons.add(getBtoAceptar(), "Center");
      this.pnlMenuButtons.add(getBtoCancelar(), "West");
    }
    return this.pnlMenuButtons;
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
          if (JAM061Boletines.this.isBloquea()) {
            return;
          }
          JAM061Boletines.this.dispose();
        }
      });
    }
    return this.btoAceptar;
  }
  
  private boolean isBloquea()
  {
    for (int i = 0; i < this.logBloquea.length; i++) {
      if (this.logBloquea[i] != false) {
        return this.logBloquea[i];
      }
    }
    return false;
  }
  
  private JAMButtonCancelar getBtoCancelar()
  {
    if (this.btoCancelar == null)
    {
      this.btoCancelar = new JAMButtonCancelar();
      this.btoCancelar.setHorizontalAlignment(0);
      this.btoCancelar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          if (JAM061Boletines.this.isBloquea()) {
            return;
          }
          JAM061Boletines.this.dispose();
        }
      });
    }
    return this.btoCancelar;
  }
  
  public static void main(String[] args) {}
  
  private void initialize()
  {
    setSize(800, 600);
    setLocation(200, 200);
    setContentPane(getJContentPane());
    setTitle("Visor de Boletines");
    
    this.tabMain.setEnabledAt(JAM060Messenger.ORGANIZACION, false);
    this.tabMain.setEnabledAt(JAM060Messenger.CARGO, false);
    this.tabMain.setEnabledAt(JAM060Messenger.USUARIO, false);
    try
    {
      this.rstBoletines.beforeFirst();
      while (this.rstBoletines.next()) {
        if (this.rstBoletines.getInt("TIPO") == JAM060Messenger.ORGANIZACION)
        {
          this.logBloquea[JAM060Messenger.ORGANIZACION] = JAMUtil.JAMConvBoolean(this.rstBoletines.getInt("SOCSYST50_BLOQUEA"));
          this.tabMain.setSelectedIndex(JAM060Messenger.ORGANIZACION);
          this.tabMain.setEnabledAt(JAM060Messenger.ORGANIZACION, true);
          this.txtTituloOrganizacion.setText(this.rstBoletines.getString("socsyst50_titulo"));
          this.txtNotasOrganizacion.setText(this.rstBoletines.getString("socsyst50_boletin"));
        }
        else if (this.rstBoletines.getInt("TIPO") == JAM060Messenger.CARGO)
        {
          this.logBloquea[JAM060Messenger.CARGO] = JAMUtil.JAMConvBoolean(this.rstBoletines.getInt("SOCSYST50_BLOQUEA"));
          this.tabMain.setSelectedIndex(JAM060Messenger.CARGO);
          this.tabMain.setEnabledAt(JAM060Messenger.CARGO, true);
          this.txtTituloCargo.setText(this.rstBoletines.getString("socsyst50_titulo"));
          this.txtNotasCargo.setText(this.rstBoletines.getString("socsyst50_boletin"));
        }
        else if (this.rstBoletines.getInt("TIPO") == JAM060Messenger.USUARIO)
        {
          this.tabMain.setEnabledAt(JAM060Messenger.USUARIO, true);
          this.logBloquea[JAM060Messenger.USUARIO] = JAMUtil.JAMConvBoolean(this.rstBoletines.getInt("SOCSYST50_BLOQUEA"));
          this.tabMain.setSelectedIndex(JAM060Messenger.USUARIO);
          this.txtTituloUsuario.setText(this.rstBoletines.getString("socsyst50_titulo"));
          this.txtNotasUsuario.setText(this.rstBoletines.getString("socsyst50_boletin"));
        }
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
  
  private JPanel getJContentPane()
  {
    if (this.jContentPane == null)
    {
      this.jContentPane = new JPanel();
      this.jContentPane.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.jContentPane.add(getPnlMenuButtons(), "South");
      this.jContentPane.add(getTabMain(), "Center");
      this.jContentPane.addMouseListener(new MouseAdapter()
      {
        public void mouseClicked(MouseEvent e)
        {
          if (e.getClickCount() == 2) {
            JAM061Boletines.this.dispose();
          }
        }
      });
    }
    return this.jContentPane;
  }
  
  private synchronized void startModal()
  {
    try
    {
      if (SwingUtilities.isEventDispatchThread())
      {
          EventQueue theQueue = getToolkit().getSystemEventQueue();
          AWTEvent event = null;
        while (isVisible())
        {
              event = theQueue.getNextEvent();
              Object source = event.getSource();
          if ((event instanceof ActiveEvent)) {
            ((ActiveEvent)event).dispatch();
          } else if ((source instanceof Component)) {
            ((Component)source).dispatchEvent(event);
          } else if ((source instanceof MenuComponent)) {
            ((MenuComponent)source).dispatchEvent(event);
          } else {
            System.err.println("Unable to dispatch: " + event);
          }
        }
      }
      else
      {
        while (isVisible())
        {
          EventQueue theQueue;
          AWTEvent event;
          Object source;
          wait();
        }
      }
    }
    catch (InterruptedException localInterruptedException) {}
  }
  
  private synchronized void stopModal()
  {
    notifyAll();
  }
}
