package UI;

import java.awt.*;
import javax.swing.*;

public abstract class BaseUI extends JPanel {
  public static final int WIDTH = 300;
  public static final int HEIGHT = 500;
  private static final int NAV_ICON_SIZE = 20;
  protected String osName = System.getProperty("os.name").toLowerCase();

  public BaseUI() {
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
  }

  protected abstract void initializeUI();

  public static JPanel createHeaderPanel(String title) {
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setBackground(new Color(51, 51, 51));

    JLabel titleLabel = new JLabel(title, loadScaledIcon("img/icons/chicken.png", 20, 20), SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    titleLabel.setForeground(Color.WHITE);
    titleLabel.setHorizontalTextPosition(SwingConstants.LEFT);
    titleLabel.setIconTextGap(10);

    headerPanel.add(titleLabel, BorderLayout.CENTER);
    headerPanel.setPreferredSize(new Dimension(WIDTH, 40));

    return headerPanel;
  }

  public JPanel createNavigationPanel(PanelKey selectedOption) {
    JPanel navigationPanel = new JPanel();
    navigationPanel.setBackground(new Color(249, 249, 249));
    navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
    navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    for (PanelKey key : PanelKey.values()) {
      navigationPanel.add(createIconButton("img/icons/" + key.name().toLowerCase() + ".png", key, selectedOption == key));
      navigationPanel.add(Box.createHorizontalGlue());
    }

    return navigationPanel;
  }

  private JButton createIconButton(String iconPath, PanelKey buttonType, boolean isSelected) {
    JButton button = new JButton(loadScaledIcon(iconPath, NAV_ICON_SIZE, NAV_ICON_SIZE));
    button.setBorder(BorderFactory.createEmptyBorder());
    button.setContentAreaFilled(false);
    button.setBorder(isSelected ? BorderFactory.createLineBorder(Color.GRAY, 2) : null);
    button.addActionListener(e -> MainFrame.getInstance().showPanel(buttonType));
    return button;
  }

  private static ImageIcon loadScaledIcon(String path, int width, int height) {
    ImageIcon originalIcon = new ImageIcon(path);
    Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    return new ImageIcon(scaledImage);
  }
}