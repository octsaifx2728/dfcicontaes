package mx.com.jammexico.jamlogon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.io.PrintStream;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.tree.DefaultTreeCellRenderer;
import mx.com.jammexico.jamcomponents.JAMCursor;
import mx.com.jammexico.jamcomponents.JAMLibKernel;
import mx.com.jammexico.jamcomponents.jamcombo.JAMHierarchicalView;
import mx.com.jammexico.jamcomponents.jamcombo.JAMNode;
import mx.com.jammexico.jamcomponents.jamcombo.JAMTree;
import mx.com.jammexico.jamdb.JAMClienteDB;
import mx.com.jammexico.jamsrv.JAMRowSet;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAM040Menu
  extends JInternalFrame
{
  private JPanel jContentPane = null;
  private JAMTree jamTree = null;
  private JAM041MenuPie JamMenu041Pie = null;
  private JAMRowSet rstMenu = null;
  private JAM030Mdi jam030Mdi = null;
  
  public JAM040Menu()
  {
    initialize();
  }
  
  public JAM040Menu(JAMRowSet argRstMenu, JAMLibKernel argJamKernel, JAM030Mdi argDeskTop)
  {
    this.rstMenu = argRstMenu;
    this.jam030Mdi = argDeskTop;
    initialize();
  }
  
  public JAMRowSet getRstMenu()
  {
    return this.rstMenu;
  }
  
  private void initialize()
  {
    String cOrga = "";
    String cCarg = "";
    setSize(new Dimension(800, 400));
    setPreferredSize(new Dimension(800, 400));
    setMaximizable(true);
    setIconifiable(true);
    setClosable(false);
    setResizable(true);
    setFrameIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/jamvos/jam.png")));
    setLayout(new BorderLayout());
    setContentPane(getJContentPane());
    try
    {
      this.rstMenu.first();
      cOrga = JAMUtil.JAMConvNullStr(this.rstMenu.getString("SOCWFLO01_DESCRI"));
      cCarg = JAMUtil.JAMConvNullStr(this.rstMenu.getString("SOCWFLO02_DESCRI"));
    }
    catch (Exception e)
    {
      JAMUtil.showDialog("Existio un error al recuperar la Organizacion/Cargo. Continua Coriendo...");
    }
    if (cOrga.equals("")) {
      setTitle("JAM v2.01 (Usuario : " + JAMLibKernel.ParamJAMUsersyst + " / Sin asignacion de Oficina y Cargo)");
    } else {
      setTitle("JAM v2.01 (Usuario : " + JAMLibKernel.ParamJAMUsersyst + " / Organizacion : " + cOrga + " / Cargo : " + cCarg + ")");
    }
    addInternalFrameListener(new InternalFrameAdapter()
    {
      public void internalFrameIconified(InternalFrameEvent e)
      {
        try
        {
          JAM040Menu.this.setSelected(true);
          JAM040Menu.this.moveToFront();
        }
        catch (PropertyVetoException eg)
        {
          eg.printStackTrace();
        }
      }
    });
    try
    {
      String[] arrSqls = new String[2];
      arrSqls[0] = 
      
        ("select * from VR_SOCSYST50(" + this.rstMenu.getInt("ID_SOCWFLO01") + "," + this.rstMenu.getInt("ID_SOCWFLO02") + "," + this.rstMenu.getInt("ID_SOCUSUA02") + ")");
      arrSqls[1] = 
        ("insert into SOCUTIL01_MOV_LOGS(socutil01_usuario, socutil01_accion, socutil01_tabla, socutil01_ultid) values('" + JAMLibKernel.ParamJAMUsername + "','L','" + JAMUtil.getIPPublic() + "." + JAMUtil.getIPLocal() + "',1964)");
      
      JAMRowSet[] rstTmp = JAMClienteDB.getRowSets(arrSqls);
      if (rstTmp[0].getRowcount() != 0L)
      {
        JAM061Boletines objbol = new JAM061Boletines(rstTmp[0]);
        JAMLibKernel.JAMFormCall(objbol, false, false);
      }
    }
    catch (Exception e)
    {
      System.out.println("Existio un error al recuperar la Organizacion/Cargo. Continua Coriendo...");
    }
  }
  
  private JPanel getJContentPane()
  {
    if (this.jContentPane == null)
    {
      this.jContentPane = new JPanel();
      this.jContentPane.setLayout(new BorderLayout());
      this.jContentPane.add(getJAMTree(), "Center");
      this.jContentPane.add(getJAM041MenuPie(), "South");
    }
    return this.jContentPane;
  }
  
  private JAMTree getJAMTree()
  {
    if (this.jamTree == null)
    {
      this.jamTree = new JAMTree();
      this.jamTree.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 1));
      JAMHierarchicalView hv = new JAMHierarchicalView("Menu Office Net2 V1.05", "MENUID", "PADREID", "MENUTITULO", this.rstMenu);
      this.jamTree.setHierarchicalView(hv);
      
      JTree zxTree = this.jamTree.RetornaArbol();
      try
      {
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setLeafIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/menu/acceso.png")));
        renderer.setClosedIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/menu/cerrado.png")));
        renderer.setOpenIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/menu/abierto.png")));
        zxTree.setCellRenderer(renderer);
      }
      catch (Exception localException) {}
      zxTree.addMouseListener(new MouseAdapter()
      {
        public void mouseClicked(MouseEvent er)
        {
          JAM040Menu.this.rstLlamaFuncionMenu(er.getClickCount());
        }
      });
    }
    return this.jamTree;
  }
  
  private void rstLlamaFuncionMenu(int intCantidadClickMouse)
  {
    JAMRowSet xdTPaso = this.jamTree.getRowset();
    JAMNode Mnode = this.jamTree.getSelectedNode();
    if (xdTPaso == null) {
      return;
    }
    if (Mnode.getId() == 0) {
      return;
    }
    if (intCantidadClickMouse == 1) {
      return;
    }
    try
    {
      xdTPaso.first();
      xdTPaso.find("MENUID", Mnode.getId());
      int idFuncionUsuario = xdTPaso.getInt("ID_SOCUSUA05");
      String ClaseNombre = xdTPaso.getString("NOMBRECLASE");
      if (idFuncionUsuario != 0)
      {
        setCursor(JAMCursor.setCursorOn(this.jam030Mdi));
        String cReto = JAMLibKernel.JAMFormCall(ClaseNombre.trim(), Mnode.getId());
        if (cReto != null) {
          throw new Exception(cReto);
        }
        setCursor(JAMCursor.setCursorOff(this.jam030Mdi));
      }
    }
    catch (Exception eLlama)
    {
      setCursor(JAMCursor.setCursorOff(this.jam030Mdi));
      if (eLlama.getMessage() != null) {
        JAMUtil.showDialogInf(eLlama.getMessage());
      }
    }
  }
  
  public JAM041MenuPie getJAM041MenuPie()
  {
    if (this.JamMenu041Pie == null) {
      this.JamMenu041Pie = new JAM041MenuPie(this.jam030Mdi);
    }
    return this.JamMenu041Pie;
  }
}
