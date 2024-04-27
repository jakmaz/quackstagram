import java.awt.BorderLayout;
import javax.swing.JPanel;

public abstract class BaseUI extends JPanel {
  public BaseUI() {
    setLayout(new BorderLayout());
    initializeUI();
  }

  protected abstract void initializeUI();

  public abstract String getTitle();
}
