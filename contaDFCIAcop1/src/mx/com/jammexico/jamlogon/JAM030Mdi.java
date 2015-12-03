package mx.com.jammexico.jamlogon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import mx.com.jammexico.jamcomponents.JAMBrowser;
import mx.com.jammexico.jamcomponents.JAMLibKernel;
import mx.com.jammexico.jamcomponents.image.JAMImage;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAM030Mdi
  extends JFrame
{
  private MDIDesktopPane MDIdesktop = new MDIDesktopPane();
  private JScrollPane scrollPane = new JScrollPane();
  private JMenuBar menuBar = new JMenuBar();
  private JMenu fileMenu = new JMenu("File");
  private JMenuItem newMenu = new JMenuItem("New");
  private JAMImage pctDesktop = null;
  private int intIdxMenu = -1;
  
  public JAM030Mdi()
  {
    JAMLibKernel.setWindowsMain(this);
    
    setDefaultCloseOperation(0);
    setBackground(new Color(102, 102, 0));
    this.MDIdesktop.setBackground(new Color(102, 102, 0));
    this.scrollPane.setBackground(new Color(102, 102, 0));
    
    setTitle("JAM v2.01 - Soportado sobre VOS v1.05 - Corriendo ON2 v2.05 201.175.21.173");
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(this.scrollPane.getViewport().add(this.MDIdesktop), "Center");
    
    setJMenuBar(this.menuBar);
    this.menuBar.add(this.fileMenu);
    this.menuBar.add(new WindowMenu(this.MDIdesktop, this));
    this.menuBar.add(getMenuAyuda());
    
    this.fileMenu.add(this.newMenu);
    this.fileMenu.setVisible(false);
    
    this.MDIdesktop.add(getPctDestktop());
    this.MDIdesktop.addComponentListener(new ComponentAdapter()
    {
      public void componentResized(ComponentEvent e)
      {
        if (JAM030Mdi.this.pctDesktop != null)
        {
          JAM030Mdi.this.pctDesktop.setBounds(new Rectangle(0, 0, JAM030Mdi.this.MDIdesktop.getWidth(), JAM030Mdi.this.MDIdesktop.getHeight()));
          JAM030Mdi.this.pctDesktop.repaint();
        }
      }
    });
    this.pctDesktop.startView();
    
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        super.windowClosing(e);
        JAM030Mdi.this.setVisible(false);
      }
      
      public void windowIconified(WindowEvent e)
      {
        super.windowIconified(e);
      }
    });
  }
  
  public JMenu getMenuAyuda()
  {
    JMenu fileAyuda = new JMenu("Ayuda");
    JMenuItem itmIndice = new JMenuItem("Indice", new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/menumdi/indice.png")));
    JMenuItem itmSoport = new JMenuItem("Soporte ONLINE", new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/menumdi/soporteOnLine.png")));
    JMenuItem itmAcerca = new JMenuItem("Acerca de ON2 v1.05", new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/jamvos/on2.png")));
    JMenuItem itmFundac = new JMenuItem("Fundacion JAM & VOS", new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/menumdi/FudacionJAMVOS.png")));
    
    fileAyuda.add(itmIndice);
    fileAyuda.add(itmAcerca);
    fileAyuda.add(itmSoport);
    fileAyuda.add(itmFundac);
    
    itmIndice.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent ae)
      {
        JAMBrowser.displayURL(JAMLibKernel.JAMURL_ROOT_HELP);
      }
    });
    itmAcerca.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent ae)
      {
        JAM090AcercaDe objJam090AcercaDe = new JAM090AcercaDe();
        int newy = JAM030Mdi.this.getY() + JAM030Mdi.this.getHeight() / 2 - objJam090AcercaDe.getHeight() / 2;
        int newx = JAM030Mdi.this.getX() + JAM030Mdi.this.getWidth() / 2 - objJam090AcercaDe.getWidth() / 2;
        objJam090AcercaDe.setLocation(newx, newy);
        objJam090AcercaDe.setVisible(true);
      }
    });
    itmSoport.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent ae)
      {
        JAMBrowser.displayURL(JAMLibKernel.JAMURL_ROOT_HELP);
      }
    });
    itmFundac.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent ae)
      {
        JAMBrowser.displayURL(JAMLibKernel.JAMURL_ROOT_HELP);
      }
    });
    return fileAyuda;
  }
  
  public void setMenuVisible(boolean argLog)
  {
    this.MDIdesktop.getComponent(this.intIdxMenu).setVisible(argLog);
    JAMLibKernel.logVisibleMenu = argLog;
  }
  
  public void addMenu(JInternalFrame argInternalFrame)
  {
    if (this.intIdxMenu != -1) {
      this.MDIdesktop.remove(this.intIdxMenu);
    }
    JAMLibKernel.Letdesktop(this.MDIdesktop);
    argInternalFrame.setName("menu");
    this.MDIdesktop.add(argInternalFrame);
    argInternalFrame.setLocation(10, 10);
    argInternalFrame.setVisible(true);
    for (int i = 0; i < this.MDIdesktop.getComponentCount(); i++) {
      if ((this.MDIdesktop.getComponent(i).getName() != null) && 
        (this.MDIdesktop.getComponent(i).getName().equalsIgnoreCase("menu")))
      {
        this.intIdxMenu = i;
        break;
      }
    }
  }
  
  private JAMImage getPctDestktop()
  {
    if (this.pctDesktop == null)
    {
      if (!JAMLibKernel.getImgDeskTopAlternativo().equalsIgnoreCase(""))
      {
        this.pctDesktop = new JAMImage(JAMLibKernel.JAMURL_WEB_IMAGES + JAMLibKernel.getImgDeskTopAlternativo());
      }
      else if (JAMLibKernel.getImgDeskTop().equalsIgnoreCase(""))
      {
        this.pctDesktop = new JAMImage(JAMLibKernel.JAMURL_WEB_IMAGES + "JAMDeskTopDefault.jpg");
      }
      else
      {
        String cTipoArchivo = "." + JAMUtil.JAMFindeStr(JAMLibKernel.getImgDeskTop().trim(), true, ".").toUpperCase();
        this.pctDesktop = new JAMImage(JAMLibKernel.JAMURL_ROOT_PICTURE + "IMGDT_" + JAMLibKernel.ParamJAMUsersyst.toUpperCase() + cTipoArchivo);
      }
      this.pctDesktop.JAMSetPictureDefault(JAMLibKernel.JAMURL_WEB_IMAGES + "JAMDeskTopDefault.jpg");
      this.pctDesktop.setDownloadPicture(false);
      this.pctDesktop.setBackground(new Color(102, 102, 0));
      this.pctDesktop.setErrorActive(false);
    }
    return this.pctDesktop;
  }
  
  public void setImageDeskTop()
  {
    String cTipoArchivo = "." + JAMUtil.JAMFindeStr(JAMLibKernel.getImgDeskTop().trim(), true, ".").toUpperCase();
    if (JAMLibKernel.getImgDeskTop().trim().equalsIgnoreCase("")) {
      cTipoArchivo = JAMLibKernel.JAMURL_WEB_IMAGES + "JAMDeskTopDefault.JPG";
    } else {
      cTipoArchivo = JAMLibKernel.JAMURL_ROOT_PICTURE + "IMGDT_" + JAMLibKernel.ParamJAMUsersyst.toUpperCase() + cTipoArchivo;
    }
    this.pctDesktop.startView(cTipoArchivo);
  }
}
