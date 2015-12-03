package mx.com.jammexico.jamlogon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import mx.com.jammexico.jamcomponents.JAMLibKernel;

public class JAM031MenuTrayIcon
  extends JPopupMenu
{
  public JAM031MenuTrayIcon()
  {
    JAM030Mdi obj = (JAM030Mdi)JAMLibKernel.getWindowsMain();
    
    add(obj.getMenuAyuda());
    
    JMenuItem maximizaItem = new JMenuItem("Maximiza", new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/menu/acceso.png")));
    maximizaItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        JAMLibKernel.getWindowsMain().setExtendedState(6);
        JAMLibKernel.getWindowsMain().setVisible(true);
      }
    });
    add(maximizaItem);
    
    JMenuItem exitItem = new JMenuItem("Hasta Pronto", new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/menumdi/SalirON2.png")));
    exitItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        JAMLibKernel.doEndSystemFull(true);
      }
    });
    add(exitItem);
  }
}
