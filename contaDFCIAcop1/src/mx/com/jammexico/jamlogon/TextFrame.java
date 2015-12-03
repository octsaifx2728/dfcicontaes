package mx.com.jammexico.jamlogon;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;

class TextFrame
  extends JInternalFrame
{
  private JTextArea textArea = new JTextArea();
  private JScrollPane scrollPane = new JScrollPane();
  
  public TextFrame()
  {
    setSize(200, 300);
    setTitle("Edit Text");
    setMaximizable(true);
    setIconifiable(true);
    setClosable(true);
    setResizable(true);
    this.scrollPane.getViewport().add(this.textArea);
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(this.scrollPane, "Center");
  }
}
