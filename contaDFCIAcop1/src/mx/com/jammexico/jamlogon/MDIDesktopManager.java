package mx.com.jammexico.jamlogon;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.DefaultDesktopManager;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.border.Border;

class MDIDesktopManager
  extends DefaultDesktopManager
{
  private MDIDesktopPane desktop;
  
  public MDIDesktopManager(MDIDesktopPane desktop)
  {
    this.desktop = desktop;
  }
  
  public void endResizingFrame(JComponent f)
  {
    super.endResizingFrame(f);
    resizeDesktop();
  }
  
  public void endDraggingFrame(JComponent f)
  {
    super.endDraggingFrame(f);
    resizeDesktop();
  }
  
  public void setNormalSize()
  {
    JScrollPane scrollPane = getScrollPane();
    int x = 0;
    int y = 0;
    Insets scrollInsets = getScrollPaneInsets();
    if (scrollPane != null)
    {
      Dimension d = scrollPane.getVisibleRect().getSize();
      if (scrollPane.getBorder() != null) {
        d.setSize(d.getWidth() - scrollInsets.left - scrollInsets.right, d.getHeight() - 
          scrollInsets.top - scrollInsets.bottom);
      }
      d.setSize(d.getWidth() - 20.0D, d.getHeight() - 20.0D);
      this.desktop.setAllSize(x, y);
      scrollPane.invalidate();
      scrollPane.validate();
    }
  }
  
  private Insets getScrollPaneInsets()
  {
    JScrollPane scrollPane = getScrollPane();
    if (scrollPane == null) {
      return new Insets(0, 0, 0, 0);
    }
    return getScrollPane().getBorder().getBorderInsets(scrollPane);
  }
  
  private JScrollPane getScrollPane()
  {
    if ((this.desktop.getParent() instanceof JViewport))
    {
      JViewport viewPort = (JViewport)this.desktop.getParent();
      if ((viewPort.getParent() instanceof JScrollPane)) {
        return (JScrollPane)viewPort.getParent();
      }
    }
    return null;
  }
  
  protected void resizeDesktop()
  {
    int x = 0;
    int y = 0;
    JScrollPane scrollPane = getScrollPane();
    Insets scrollInsets = getScrollPaneInsets();
    if (scrollPane != null)
    {
      JInternalFrame[] allFrames = this.desktop.getAllFrames();
      for (int i = 0; i < allFrames.length; i++)
      {
        if (allFrames[i].getX() + allFrames[i].getWidth() > x) {
          x = allFrames[i].getX() + allFrames[i].getWidth();
        }
        if (allFrames[i].getY() + allFrames[i].getHeight() > y) {
          y = allFrames[i].getY() + allFrames[i].getHeight();
        }
      }
      Dimension d = scrollPane.getVisibleRect().getSize();
      if (scrollPane.getBorder() != null) {
        d.setSize(d.getWidth() - scrollInsets.left - scrollInsets.right, d.getHeight() - 
          scrollInsets.top - scrollInsets.bottom);
      }
      if (x <= d.getWidth()) {
        x = (int)d.getWidth() - 20;
      }
      if (y <= d.getHeight()) {
        y = (int)d.getHeight() - 20;
      }
      this.desktop.setAllSize(x, y);
      scrollPane.invalidate();
      scrollPane.validate();
    }
  }
}
