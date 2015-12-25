package mx.com.jammexico.jamlogon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import mx.com.jammexico.jamcomponents.jambuttons.JAMButtonAceptar;
import mx.com.jammexico.jamcomponents.visual.JAMAreaTexto;
import mx.com.jammexico.jamcomponents.visual.JAMLabel;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAM090AcercaDe
  extends JDialog
{
  private static final long serialVersionUID = 1L;
  private JPanel jContentPane = null;
  private JLabel lblVosImg = null;
  private JLabel lblOn2Img = null;
  private JLabel lblJamImg = null;
  private JAMAreaTexto lblOn2Txt = null;
  private JAMAreaTexto lblVosTxt = null;
  private JAMAreaTexto lblJamTxt = null;
  private JAMButtonAceptar btoSalir = null;
  private JAMLabel lblMiembros = null;
  private JAMAreaTexto txtMiembros = null;
  private JPanel pnlON2 = null;
  private JPanel pnlVOS = null;
  private JPanel pnlJAM = null;
  private JTabbedPane tabAcercaDe = null;
  private JPanel pnlON2Top = null;
  private JPanel pnlVOSTop = null;
  private JPanel pnlJAMTop = null;
  private JPanel pnlON2Salir = null;
  private JTable jTableProperties = null;
  private JScrollPane jScrollProperties = new JScrollPane();
  private int intTagDefault = -1;
  
  public JAM090AcercaDe()
  {
    initialize();
  }
  
  public JAM090AcercaDe(Frame owner)
  {
    super(owner);
    initialize();
  }
  
  public JAM090AcercaDe(int argTag)
  {
    this.intTagDefault = argTag;
    initialize();
  }
  
  private void initialize()
  {
    setSize(545, 416);
    setTitle("JAM v2.01 - Acerca de - VOS v1.05");
    setContentPane(getJContentPane());
    setResizable(false);
    setModal(true);
    setAlwaysOnTop(true);
    this.lblOn2Txt.grabFocus();
    if (this.intTagDefault != -1) {
      this.tabAcercaDe.setSelectedIndex(this.intTagDefault);
    }
  }
  
  private JPanel getJContentPane()
  {
    if (this.jContentPane == null)
    {
      this.jContentPane = new JPanel();
      this.jContentPane.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.jContentPane.setSize(new Dimension(564, 454));
      this.jContentPane.add(getTabAcercaDe(), "Center");
    }
    return this.jContentPane;
  }
  
  private JAMButtonAceptar getBtoSalir()
  {
    if (this.btoSalir == null)
    {
      this.btoSalir = new JAMButtonAceptar();
      this.btoSalir.setText("Acepta");
      this.btoSalir.setPreferredSize(new Dimension(110, 100));
      this.btoSalir.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          JAM090AcercaDe.this.dispose();
        }
      });
    }
    return this.btoSalir;
  }
  
  private JPanel getPnlON2()
  {
    if (this.pnlON2 == null)
    {
      this.pnlON2 = new JPanel();
      this.pnlON2.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlON2.setSize(new Dimension(485, 79));
      this.pnlON2.add(getPnlON2Top(), "North");
      this.pnlON2.add(getPnlON2Salir(), "Center");
      this.pnlON2.add(getTxtMiembros(), "South");
    }
    return this.pnlON2;
  }
  
  private JPanel getPnlVOS()
  {
    if (this.pnlVOS == null)
    {
      this.pnlVOS = new JPanel();
      this.pnlVOS.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlVOS.setSize(new Dimension(496, 98));
      this.pnlVOS.add(getPnlVOSTop(), "North");
      this.jScrollProperties.setViewportView(getJTableProperties());
      this.pnlVOS.add(this.jScrollProperties, "Center");
    }
    return this.pnlVOS;
  }
  
  private JPanel getPnlJAM()
  {
    if (this.pnlJAM == null)
    {
      this.pnlJAM = new JPanel();
      this.pnlJAM.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlJAM.setSize(new Dimension(478, 81));
      this.pnlJAM.add(getPnlJAMTop(), "North");
    }
    return this.pnlJAM;
  }
  
  private JTabbedPane getTabAcercaDe()
  {
    if (this.tabAcercaDe == null)
    {
      this.tabAcercaDe = new JTabbedPane();
      
      this.tabAcercaDe.addTab("On2", new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/jamvos/on2.png")), getPnlON2(), null);
      this.tabAcercaDe.addTab("VOS", new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/jamvos/vos.png")), getPnlVOS());
      this.tabAcercaDe.addTab("JAM", new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/jamvos/jam.png")), getPnlJAM());
    }
    return this.tabAcercaDe;
  }
  
  private JAMAreaTexto getTxtMiembros()
  {
    if (this.txtMiembros == null)
    {
      this.txtMiembros = new JAMAreaTexto();
      this.txtMiembros.setEditable(false);
      this.txtMiembros.setJAMFont(new Font("SansSerif", 0, 11));
      this.txtMiembros.setBounds(new Rectangle(27, 16, 534, 266));
      this.txtMiembros.setPreferredSize(new Dimension(100, 220));
      this.txtMiembros.setText("Direccion de Proyecto : Ing. Jesus Orestes, jesus_orestes@officenet2.com" + JAMUtil.getCrlf() + 
        "Direccion de Investigacion : Lic. Anibal Mohr, anibal_mohr@officenet2.com" + JAMUtil.getCrlf() + 
        "Direccion de Analisis Funcional : Lic. Raul Luna, raul_luna@officenet2.com" + JAMUtil.getCrlf() + 
        "Direccion Administrativa : Lic. Arnoldo Arreola, arnoldo_arreola@officenet2.com" + JAMUtil.getCrlf() + 
        "Direccion de Hosting y Web : Ing. Luis Almager, luis_almager@officenet2.com" + JAMUtil.getCrlf() + 
        "Desarrolladores :" + JAMUtil.getCrlf() + 
        "Ing. Deissy Izaguirre, deissy_izaguirre@officenet2.com" + JAMUtil.getCrlf() + 
        "Ing. Fernando Vital, fernando_vital@officenet2.com" + JAMUtil.getCrlf() + 
        "Sr. David Contreras, david_contreras@officenet2.com" + JAMUtil.getCrlf() + 
        "Mexico - Tamaulipas CP. 88240. Ocampo 2104. Nuevo Laredo" + JAMUtil.getCrlf() + 
        "Tels. (+52)-867-714-9003");
    }
    return this.txtMiembros;
  }
  
  private JPanel getPnlON2Top()
  {
    if (this.pnlON2Top == null)
    {
      this.pnlON2Top = new JPanel();
      this.pnlON2Top.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlON2Top.setPreferredSize(new Dimension(100, 100));
      
      this.lblOn2Txt = new JAMAreaTexto();
      this.lblOn2Txt.setEditable(false);
      this.lblOn2Txt.setJAMFont(new Font("SansSerif", 0, 11));
      
      this.lblOn2Img = new JLabel();
      this.lblOn2Img.setIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/jamvos/on2_chico.jpg")));
      this.lblOn2Img.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 1));
      
      this.lblOn2Txt.setText("ON2 y Office Net2 son marcas registradas " + JAMUtil.getCrlf() + 
        "por Office Net2 S.A. de C.V. - Mexico. CopyRight (c) 2007" + JAMUtil.getCrlf() + 
        "ultima version liberada v2.05.2008" + JAMUtil.getCrlf() + JAMUtil.getCrlf() + 
        "Idea, Proyecto y Realizacion : Lic. Jose Anibal Mohr Mitoff");
      
      this.pnlON2Top.add(this.lblOn2Img, "West");
      this.pnlON2Top.add(this.lblOn2Txt, "Center");
    }
    return this.pnlON2Top;
  }
  
  private JPanel getPnlVOSTop()
  {
    if (this.pnlVOSTop == null)
    {
      this.pnlVOSTop = new JPanel();
      this.pnlVOSTop.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlVOSTop.setPreferredSize(new Dimension(100, 100));
      
      this.lblVosTxt = new JAMAreaTexto();
      this.lblVosTxt.setEditable(false);
      this.lblVosTxt.setJAMFont(new Font("SansSerif", 0, 11));
      
      this.lblVosImg = new JLabel();
      this.lblVosImg.setIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/jamvos/vos_chico.jpg")));
      this.lblVosImg.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 1));
      this.lblVosImg.setOpaque(false);
      
      this.lblVosTxt.setText("VOS es marca registradas por Office Net2 S.A. de C.V." + JAMUtil.getCrlf() + 
        "Mexico. CopyRight (c) 2007. Version liberada v1.05.2008" + JAMUtil.getCrlf() + JAMUtil.getCrlf() + 
        "Idea, Proyecto y Realizacion : Lic. Jose Anibal Mohr Mitoff");
      
      this.pnlVOSTop.add(this.lblVosImg, "West");
      this.pnlVOSTop.add(this.lblVosTxt, "Center");
    }
    return this.pnlVOSTop;
  }
  
  private JPanel getPnlJAMTop()
  {
    if (this.pnlJAMTop == null)
    {
      this.pnlJAMTop = new JPanel();
      this.pnlJAMTop.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlJAMTop.setPreferredSize(new Dimension(100, 100));
      
      this.lblJamTxt = new JAMAreaTexto();
      this.lblJamTxt.setEditable(false);
      this.lblJamTxt.setJAMFont(new Font("SansSerif", 0, 11));
      
      this.lblJamImg = new JLabel();
      this.lblJamImg.setIcon(new ImageIcon(getClass().getResource("/mx/com/jammexico/jamcomponents/jamimages/jamvos/jam_chico.jpg")));
      this.lblJamImg.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 1));
      
      this.lblJamTxt.setText("JAM es marca registrada por Office Net2 S.A. de C.V." + JAMUtil.getCrlf() + 
        "Mexico. CopyRight (c) 2007. Version liberada v2.01.2008" + JAMUtil.getCrlf() + 
        "JAM & VOS son proyectos bajo normas (Open Source)" + JAMUtil.getCrlf() + 
        "ON2 y todos sus proyectos son propiedad de quien corresponda" + JAMUtil.getCrlf() + JAMUtil.getCrlf() + 
        "Idea, Proyecto y Realizacion : Lic. Jose Anibal Mohr Mitoff");
      
      this.pnlJAMTop.add(this.lblJamImg, "West");
      this.pnlJAMTop.add(this.lblJamTxt, "Center");
    }
    return this.pnlJAMTop;
  }
  
  private JPanel getPnlON2Salir()
  {
    if (this.pnlON2Salir == null)
    {
      this.pnlON2Salir = new JPanel();
      this.pnlON2Salir.setLayout(JAMUtil.JAMBorderLayout(5, 5));
      this.pnlON2Salir.setSize(new Dimension(439, 35));
      
      this.lblMiembros = new JAMLabel(1);
      
      this.lblMiembros.setHorizontalAlignment(0);
      this.lblMiembros.setBounds(new Rectangle(5, 287, 204, 16));
      this.lblMiembros.setText("Creditos (Miembros y Participantes)");
      
      this.lblMiembros.setIconInput(1);
      
      this.pnlON2Salir.add(this.lblMiembros, "Center");
      this.pnlON2Salir.add(getBtoSalir(), "East");
    }
    return this.pnlON2Salir;
  }
  
  private JTable getJTableProperties()
  {
    if (this.jTableProperties == null)
    {
      this.jTableProperties = new JTable();
      this.jTableProperties.setModel(
        new DefaultTableModel(new Object[0][], new String[] { "Property", "Value" })
        {
          boolean[] canEdit = new boolean[2];
          
          public boolean isCellEditable(int rowIndex, int columnIndex)
          {
            return this.canEdit[columnIndex];
          }
        });
      this.jTableProperties.setAutoResizeMode(1);
      
      Properties system_props = System.getProperties();
      Enumeration system_props_enum = system_props.keys();
      while (system_props_enum.hasMoreElements())
      {
        String key = (String)system_props_enum.nextElement();
        if (key.trim().length() != 0) {
          ((DefaultTableModel)this.jTableProperties.getModel()).addRow(new Object[] { key, system_props.getProperty(key, "") });
        }
      }
      this.jTableProperties.updateUI();
    }
    return this.jTableProperties;
  }
}
