package mx.com.jammexico.jamlogon;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javazoom.jl.player.JAMTunesPlayer;
import mx.com.jammexico.jamcomponents.JAMLibKernel;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAM070VisorPanel
  extends JPanel
{
  private static final long serialVersionUID = 1L;
  private JLabel lblBanner = null;
  private String strUrlImages = "";
  private JAMTunesPlayer objPlayer = null;
  private JPanel pnlDown = null;
  private JLabel lblAmohr = null;
  private JProgressBar prgVisor = null;
  private JLabel lblMensajes = null;
  private JPanel pnlErrores = null;
  private JLabel lblMusica = null;
  private JLabel lblErrores = null;
  private ImageIcon icon = null;
  private boolean logPresiona = true;
  
  public JAM070VisorPanel()
  {
    initialize();
  }
  
  public JAM070VisorPanel(String argWebImages)
  {
    this.strUrlImages = argWebImages;
    initialize();
  }
  
  private void initialize()
  {
    this.lblBanner = new JLabel();
    this.lblBanner.setLayout(JAMUtil.JAMBorderLayout(5, 5));
    this.lblBanner.add(getPnlDown(), "South");
    try
    {
      if (this.strUrlImages.toLowerCase().startsWith("http:"))
      {
        URL imgURL = new URL(this.strUrlImages + "visor360.png");
        this.icon = new ImageIcon(imgURL);
      }
      else
      {
        this.icon = new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/visor360.jpg"));
      }
      
      this.lblBanner.setIcon(this.icon);
    }
    catch (Exception localException1) {}
    setSize(getIconWidth(), getIconHeight());
    setLayout(JAMUtil.JAMBorderLayout(5, 5));
    add(this.lblBanner, "Center");
  }
  
  private JPanel getPnlDown()
  {
    if (this.pnlDown == null)
    {
      this.lblMensajes = new JLabel();
      this.lblMensajes.setBounds(new Rectangle(5, 220, 491, 26));
      this.lblMensajes.setForeground(Color.white);
      this.lblMensajes.setFont(new Font("Dialog", 2, 12));
      this.lblMensajes.setOpaque(false);
      
      this.lblAmohr = new JLabel();
      this.lblAmohr.setBounds(new Rectangle(5, 163, 521, 26));
      this.lblAmohr.setText("Idea, Proyecto y realizacion : Office Net2 S.A de C.V. 2007/2014 Mexico/Argentina");
      this.lblAmohr.setHorizontalAlignment(0);
      this.lblAmohr.setFont(new Font("Dialog", 2, 12));
      this.lblAmohr.setForeground(Color.white);
      this.lblAmohr.setOpaque(false);
      
      this.pnlDown = new JPanel();
      this.pnlDown.setOpaque(false);
      this.pnlDown.setLayout(JAMUtil.JAMGridLayout(4, 1, 5, 5));
      this.pnlDown.setPreferredSize(new Dimension(100, 99));
      this.pnlDown.setSize(new Dimension(326, 100));
      this.pnlDown.add(getPrgVisor(), null);
      this.pnlDown.add(this.lblMensajes, null);
      this.pnlDown.add(this.lblAmohr, null);
      this.pnlDown.add(getPnlErrores(), null);
    }
    return this.pnlDown;
  }
  
  private JProgressBar getPrgVisor()
  {
    if (this.prgVisor == null)
    {
      this.prgVisor = new JProgressBar();
      this.prgVisor.setName("prgBarraProceso");
      this.prgVisor.setStringPainted(true);
      this.prgVisor.setOpaque(false);
      this.prgVisor.setForeground(new Color(153, 0, 0));
      this.prgVisor.setBackground(Color.red);
      this.prgVisor.setMinimum(0);
    }
    return this.prgVisor;
  }
  
  private JPanel getPnlErrores()
  {
    if (this.pnlErrores == null)
    {
      this.lblErrores = new JLabel();
      this.lblErrores.setForeground(Color.BLACK);
      this.lblErrores.setFont(new Font("Dialog", 2, 12));
      
      this.lblMusica = new JLabel();
      this.lblMusica.setPreferredSize(new Dimension(25, 25));
      ImageIcon iconMusica = new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/sonidoon.png"));
      this.lblMusica.setIcon(iconMusica);
      this.lblMusica.setCursor(new Cursor(12));
      this.lblMusica.addMouseListener(new MouseAdapter()
      {
        public void mouseClicked(MouseEvent e)
        {
          try
          {
            if (JAM070VisorPanel.this.logPresiona)
            {
              JAM070VisorPanel.this.objPlayer.TunesPause();
              JAM070VisorPanel.this.logPresiona = false;
              ImageIcon iconMusica = new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/sonidooff.png"));
              JAM070VisorPanel.this.lblMusica.setIcon(iconMusica);
            }
            else
            {
              JAM070VisorPanel.this.objPlayer.TunesResumen();
              JAM070VisorPanel.this.logPresiona = true;
              ImageIcon iconMusica = new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/sonidoon.png"));
              JAM070VisorPanel.this.lblMusica.setIcon(iconMusica);
            }
          }
          catch (Exception eg)
          {
            System.out.println(eg.getMessage());
          }
        }
      });
      this.pnlErrores = new JPanel();
      this.pnlErrores.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlErrores.setOpaque(false);
      this.pnlErrores.add(this.lblMusica, "East");
      this.pnlErrores.add(this.lblErrores, "Center");
    }
    return this.pnlErrores;
  }
  
  public void setMaximo(int argMaximo)
  {
    this.prgVisor.setMaximum(argMaximo);
  }
  
  public void setValue(int argValue)
  {
    this.prgVisor.setValue(argValue);
  }
  
  public void setValue(JAM070VisorMensajes argMensaje)
  {
    if (argMensaje.getWaiting() == 0)
    {
      setValue(argMensaje.getLinea(), argMensaje.getMensaje());
      repaint();
    }
    else
    {
      JAMUtil.JAMTimeNow();
      JAMUtil.JAMTimeInit();
      int intSegundos = 1;
      while (JAMUtil.JAMTimeElapsed() < argMensaje.getWaiting()) {
        if (JAMUtil.JAMTimeElapsed() == intSegundos)
        {
          intSegundos++;
          setValue(argMensaje.getLinea() + intSegundos, argMensaje.getMensaje());
        }
      }
    }
  }
  
  public void setValue(int argValue, String argMensaje)
  {
    this.prgVisor.setValue(argValue);
    this.lblMensajes.setText(argMensaje);
    this.lblErrores.setText("");
  }
  
  public void setErrores(String argMensaje)
  {
    getToolkit().beep();
    this.lblErrores.setText(argMensaje);
  }
  
  public void stopMusic()
  {
    if (this.objPlayer != null) {
      this.objPlayer.TunesStop();
    }
  }
  
  public int getIconWidth()
  {
    return this.icon.getIconWidth();
  }
  
  public int getIconHeight()
  {
    return this.icon.getIconHeight();
  }
}
