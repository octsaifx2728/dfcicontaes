package mx.com.jammexico.jamlogon;

import javax.swing.JPanel;
import mx.com.jammexico.jamcomponents.jamform.JAMFormModal;
import mx.com.jammexico.jamsrv.JAMUtil;

public class JAM100Musica
  extends JAMFormModal
{
  private JPanel jContentPane = null;
  
  public JAM100Musica()
  {
    initialize();
  }
  
  public JAM100Musica(String title)
  {
    super(title);
    
    initialize();
  }
  
  public JAM100Musica(String title, boolean resizable)
  {
    super(title, resizable);
    
    initialize();
  }
  
  public JAM100Musica(String title, boolean resizable, boolean closable)
  {
    super(title, resizable, closable);
    
    initialize();
  }
  
  public JAM100Musica(String title, boolean resizable, boolean closable, boolean maximizable)
  {
    super(title, resizable, closable, maximizable);
    
    initialize();
  }
  
  public JAM100Musica(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable)
  {
    super(title, resizable, closable, maximizable, iconifiable);
    
    initialize();
  }
  
  public static void main(String[] args) {}
  
  private void initialize()
  {
    setSize(690, 340);
    setLocation(200, 200);
    setContentPane(getJContentPane());
    setTitle("Tunes Player");
  }
  
  private JPanel getJContentPane()
  {
    if (this.jContentPane == null)
    {
      this.jContentPane = new JPanel();
      this.jContentPane.setLayout(JAMUtil.JAMBorderLayout(5, 5));
    }
    return this.jContentPane;
  }
}
