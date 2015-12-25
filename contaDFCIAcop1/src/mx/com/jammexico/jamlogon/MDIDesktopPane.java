package mx.com.jammexico.jamlogon;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

class MDIDesktopPane
  extends JDesktopPane
{
  private static int FRAME_OFFSET = 20;
  private MDIDesktopManager manager;
  
  public MDIDesktopPane()
  {
    this.manager = new MDIDesktopManager(this);
    setDesktopManager(this.manager);
    setDragMode(1);
  }
  
  public void setBounds(int x, int y, int w, int h)
  {
    super.setBounds(x, y, w, h);
    checkDesktopSize();
  }
  
  public Component add(JInternalFrame frame)
  {
    JInternalFrame[] array = getAllFrames();
    
    Component retval = super.add(frame);
    checkDesktopSize();
    Point p;
    if (array.length > 0)
    {
      p = array[0].getLocation();
      p.x += FRAME_OFFSET;
      p.y += FRAME_OFFSET;
    }
    else
    {
      p = new Point(0, 0);
    }
    frame.setLocation(p.x, p.y);
    if (frame.isResizable())
    {
      int w = getWidth() - getWidth() / 3;
      int h = getHeight() - getHeight() / 3;
      if (w < frame.getMinimumSize().getWidth()) {
        w = (int)frame.getMinimumSize().getWidth();
      }
      if (h < frame.getMinimumSize().getHeight()) {
        h = (int)frame.getMinimumSize().getHeight();
      }
      frame.setSize(w, h);
    }
    moveToFront(frame);
    frame.setVisible(true);
    try
    {
      frame.setSelected(true);
    }
    catch (PropertyVetoException e)
    {
      frame.toBack();
    }
    return retval;
  }
  
  public void remove(Component c)
  {
    super.remove(c);
    checkDesktopSize();
  }
  
  public void cascadeFrames()
  {
    int x = 0;
    int y = 0;
    JInternalFrame[] allFrames = getAllFrames();
    
    this.manager.setNormalSize();
    int frameHeight = getBounds().height - 5 - allFrames.length * FRAME_OFFSET;
    int frameWidth = getBounds().width - 5 - allFrames.length * FRAME_OFFSET;
    for (int i = allFrames.length - 1; i >= 0; i--)
    {
      allFrames[i].setLocation(x, y);
      x += FRAME_OFFSET;
      y += FRAME_OFFSET;
    }
  }
  
  public void tileFrames()
  {
    Component[] allFrames = getAllFrames();
    this.manager.setNormalSize();
    int frameHeight = getBounds().height / allFrames.length;
    int y = 0;
    for (int i = 0; i < allFrames.length; i++)
    {
      allFrames[i].setSize(getBounds().width, frameHeight);
      allFrames[i].setLocation(0, y);
      y += frameHeight;
    }
  }
  
  public void setAllSize(Dimension d)
  {
    setMinimumSize(d);
    setMaximumSize(d);
    setPreferredSize(d);
  }
  
  public void setAllSize(int width, int height)
  {
    setAllSize(new Dimension(width, height));
  }
  
  private void checkDesktopSize()
  {
    if ((getParent() != null) && (isVisible())) {
      this.manager.resizeDesktop();
    }
  }
}
