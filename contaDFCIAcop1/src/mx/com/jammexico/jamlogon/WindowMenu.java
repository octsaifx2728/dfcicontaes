package mx.com.jammexico.jamlogon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import mx.com.jammexico.jamcomponents.JAMLibKernel;

class WindowMenu
  extends JMenu
{
  private MDIDesktopPane desktop;
  private JAM030Mdi Jam030Mdi;
  private JMenuItem cascade = new JMenuItem("Cascada", new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/menumdi/cascada.png")));
  private JMenuItem tile = new JMenuItem("Distribuido", new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/menumdi/distribuido.png")));
  private JMenuItem logon = new JMenuItem("Conectarse...", new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/menumain/seguridad.png")));
  private JMenuItem cierra = new JMenuItem("Salir de ON2", new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/menumdi/SalirON2.png")));
  
  public WindowMenu(MDIDesktopPane desktop, JAM030Mdi argJam030Mdi)
  {
    this.Jam030Mdi = argJam030Mdi;
    this.desktop = desktop;
    setText("Ventanas");
    this.cascade.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent ae)
      {
        WindowMenu.this.desktop.cascadeFrames();
      }
    });
    this.tile.setVisible(true);
    this.tile.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent ae)
      {
        WindowMenu.this.desktop.tileFrames();
      }
    });
    this.cierra.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent ae)
      {
        JAMLibKernel.doEndSystemFull(true);
      }
    });
    this.logon.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent ae)
      {
        JAM010RunTime objJamRunTime = new JAM010RunTime(WindowMenu.this.Jam030Mdi);
        if (objJamRunTime.getLogonAcepta()) {
          WindowMenu.this.Jam030Mdi.setImageDeskTop();
        }
      }
    });
    addMenuListener(new MenuListener()
    {
      public void menuCanceled(MenuEvent e) {}
      
      public void menuDeselected(MenuEvent e)
      {
        WindowMenu.this.removeAll();
      }
      
      public void menuSelected(MenuEvent e)
      {
        WindowMenu.this.buildChildMenus();
      }
    });
  }
  
  private void doLogon() {}
  
  private void buildChildMenus()
  {
    JInternalFrame[] array = this.desktop.getAllFrames();
    
    add(this.cascade);
    add(this.tile);
    add(this.logon);
    add(this.cierra);
    if (array.length > 0) {
      addSeparator();
    }
    this.cascade.setEnabled(array.length > 0);
    this.tile.setEnabled(array.length > 0);
    for (int i = 0; i < array.length; i++)
    {
      ChildMenuItem menu = new ChildMenuItem(array[i]);
      menu.setState(i == 0);
      
      menu.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent ae)
        {
          JInternalFrame frame = ((WindowMenu.ChildMenuItem)ae.getSource()).getFrame();
          frame.moveToFront();
          try
          {
            frame.setSelected(true);
            frame.setIcon(false);
          }
          catch (PropertyVetoException e)
          {
            e.printStackTrace();
          }
        }
      });
      menu.setIcon(array[i].getFrameIcon());
      add(menu);
    }
  }
  
  class ChildMenuItem
    extends JCheckBoxMenuItem
  {
    private JInternalFrame frame;
    
    public ChildMenuItem(JInternalFrame frame)
    {
      super();
      this.frame = frame;
    }
    
    public JInternalFrame getFrame()
    {
      return this.frame;
    }
  }
}
