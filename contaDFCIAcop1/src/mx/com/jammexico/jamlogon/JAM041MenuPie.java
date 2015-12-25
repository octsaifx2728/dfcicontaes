package mx.com.jammexico.jamlogon;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import mx.com.jammexico.jamcomponents.JAMBrowser;
import mx.com.jammexico.jamcomponents.JAMCursor;
import mx.com.jammexico.jamcomponents.JAMLibKernel;
import mx.com.jammexico.jamcomponents.JAMReloj;
import mx.com.jammexico.jamcomponents.events.SecondsPressedEvent;
import mx.com.jammexico.jamcomponents.events.SecondsPressedListener;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAM041MenuPie
  extends JPanel
{
  private static final long serialVersionUID = 1L;
  private JPanel pnlInfoPie = null;
  private JPanel pnlMenuBto = null;
  private JButton btoMensajes = null;
  private JButton btoConfiguracion = null;
  private JButton btoLogon = null;
  private JButton btoAyuda = null;
  private JButton btoMusica = null;
  private JAM030Mdi Jam030Mdi = null;
  private JDateChooser datSystem = null;
  private JAMReloj datReloj = null;
  private boolean logMensajesAviso = false;
  private boolean logMusica = false;
  
  public JAM041MenuPie()
  {
    initialize();
  }
  
  public JAM041MenuPie(JAM030Mdi argJam030Mdi)
  {
    this.Jam030Mdi = argJam030Mdi;
    initialize();
  }
  
  public JAM041MenuPie(LayoutManager layout)
  {
    super(layout);
    
    initialize();
  }
  
  public JAM041MenuPie(boolean isDoubleBuffered)
  {
    super(isDoubleBuffered);
    
    initialize();
  }
  
  public JAM041MenuPie(LayoutManager layout, boolean isDoubleBuffered)
  {
    super(layout, isDoubleBuffered);
    
    initialize();
  }
  
  private void initialize()
  {
    setSize(new Dimension(860, 25));
    setPreferredSize(new Dimension(600, 25));
    setLayout(new BorderLayout());
    add(getPnlMenuBto(), "West");
    add(getPnlInfoPie(), "East");
  }
  
  private JPanel getPnlMenuBto()
  {
    if (this.pnlMenuBto == null)
    {
      this.pnlMenuBto = new JPanel();
      this.pnlMenuBto.setLayout(new GridLayout(0, 5));
      this.pnlMenuBto.setPreferredSize(new Dimension(140, 25));
      this.pnlMenuBto.add(getBtoLogon());
      this.pnlMenuBto.add(getBtoMensajes());
      this.pnlMenuBto.add(getBtoConfiguracion());
      this.pnlMenuBto.add(getBtoAyuda());
      this.pnlMenuBto.add(getBtoMusica());
    }
    return this.pnlMenuBto;
  }
  
  private JPanel getPnlInfoPie()
  {
    if (this.pnlInfoPie == null)
    {
      this.pnlInfoPie = new JPanel();
      this.pnlInfoPie.setLayout(new BorderLayout());
      this.pnlInfoPie.setPreferredSize(new Dimension(325, 25));
      this.pnlInfoPie.add(getDatReloj(), "Center");
      this.pnlInfoPie.add(getDatSystem(), "East");
    }
    return this.pnlInfoPie;
  }
  
  private JButton getBtoMensajes()
  {
    if (this.btoMensajes == null)
    {
      this.btoMensajes = new JButton();
      this.btoMensajes.setIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/menumain/mensajes.png")));
      this.btoMensajes.setToolTipText("Mensajes y Comunicacion");
      this.btoMensajes.setName("mensajes.png");
      this.btoMensajes.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAMCursor.setCursorOn(JAM041MenuPie.this.btoMensajes);
          JAM060Messenger objMsn = new JAM060Messenger(JAMLibKernel.getIdUser());
          JAMLibKernel.JAMFormCall(objMsn, false);
          JAMCursor.setCursorOff(JAM041MenuPie.this.btoMensajes);
        }
      });
    }
    return this.btoMensajes;
  }
  
  public void setActivaMensajes(boolean logModo)
  {
    this.logMensajesAviso = logModo;
    if (!this.logMensajesAviso)
    {
      this.btoMensajes.setIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/menumain/mensajes.png")));
      this.btoMensajes.setName("mensajes.png");
    }
  }
  
  private JButton getBtoConfiguracion()
  {
    if (this.btoConfiguracion == null)
    {
      this.btoConfiguracion = new JButton();
      this.btoConfiguracion.setIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/menumain/setup.png")));
      this.btoConfiguracion.setToolTipText("Configuracion del Escritorio");
      this.btoConfiguracion.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAMCursor.setCursorOn(JAM041MenuPie.this.btoConfiguracion);
          JAM050Config objConfig = new JAM050Config(JAM041MenuPie.this.Jam030Mdi);
          JAMLibKernel.JAMFormCall(objConfig, false);
          JAMCursor.setCursorOff(JAM041MenuPie.this.btoConfiguracion);
        }
      });
    }
    return this.btoConfiguracion;
  }
  
  private JButton getBtoLogon()
  {
    if (this.btoLogon == null)
    {
      this.btoLogon = new JButton();
      this.btoLogon.setIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/menumain/seguridad.png")));
      this.btoLogon.setToolTipText("Seguridad y Control");
      this.btoLogon.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          if (JAMLibKernel.JAMFuncionLlamadasCount() != 0)
          {
            JAMUtil.showDialogInf(
              "Debe cerrar primero todas las Aplicaciones para ingresar como otro Usuario" + JAMUtil.getCrlf() + "o, caso contrario, puede cerrar ON2 completamente y volver a ingresar");
            return;
          }
          JAMCursor.setCursorOn(JAM041MenuPie.this.btoLogon);
          JAM010RunTime objJamRunTime = new JAM010RunTime(JAM041MenuPie.this.Jam030Mdi);
          JAMCursor.setCursorOff(JAM041MenuPie.this.btoLogon);
          if (objJamRunTime.getLogonAcepta()) {
            JAM041MenuPie.this.Jam030Mdi.setImageDeskTop();
          }
        }
      });
    }
    return this.btoLogon;
  }
  
  private JButton getBtoAyuda()
  {
    if (this.btoAyuda == null)
    {
      this.btoAyuda = new JButton();
      this.btoAyuda.setIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/menumain/ayuda.png")));
      this.btoAyuda.setToolTipText("Indice de Ayuda General");
      this.btoAyuda.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAMBrowser.displayURL(JAMLibKernel.JAMURL_ROOT_HELP);
        }
      });
    }
    return this.btoAyuda;
  }
  
  private JAMReloj getDatReloj()
  {
    if (this.datReloj == null)
    {
      this.datReloj = new JAMReloj(1, 1);
      
      JAMLibKernel.setTimer(this.datReloj);
      
      this.datReloj.addSecondsListener(new SecondsPressedListener()
      {
        public void Seconds(SecondsPressedEvent e) {}
      });
    }
    return this.datReloj;
  }
  
  private void doEventRejosChat()
  {
    if (!JAMLibKernel.ParamJAMIsChat) {
      return;
    }
    if (this.logMensajesAviso) {
      if (this.btoMensajes.getName().equalsIgnoreCase("MensajeOn.png"))
      {
        this.btoMensajes.setIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/menumain/MensajeOff.png")));
        this.btoMensajes.setName("MensajeOff.png");
      }
      else
      {
        this.btoMensajes.setIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/menumain/MensajeOn.png")));
        this.btoMensajes.setName("MensajeOn.png");
      }
    }
  }
  
  private boolean logReconectandose = false;
  
  class HiloReconecta
    extends Thread
  {
    HiloReconecta() {}
    
    public void run()
    {
      JAM041MenuPie.this.logReconectandose = true;
      
      JAM041MenuPie.this.logReconectandose = false;
    }
  }
  
  private JDateChooser getDatSystem()
  {
    if (this.datSystem == null)
    {
      this.datSystem = new JDateChooser();
      this.datSystem.setPreferredSize(new Dimension(165, 25));
      
      JAMLibKernel.setFecha(this.datSystem);
      
      this.datSystem.addPropertyChangeListener("date", new PropertyChangeListener()
      {
        public void propertyChange(PropertyChangeEvent e) {}
      });
    }
    return this.datSystem;
  }
  
  private JButton getBtoMusica()
  {
    if (this.btoMusica == null)
    {
      this.btoMusica = new JButton();
      this.btoMusica.setVisible(true);
      this.btoMusica.setIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/MusicOn.png")));
      this.btoMusica.setToolTipText("Edita Mp3 y Mp4");
      this.btoMusica.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAMCursor.setCursorOn(JAM041MenuPie.this.btoMusica);
          JAM100Musica objMusica = new JAM100Musica();
          JAMLibKernel.JAMFormCall(objMusica, true);
          JAMCursor.setCursorOff(JAM041MenuPie.this.btoMusica);
        }
      });
    }
    return this.btoMusica;
  }
}
