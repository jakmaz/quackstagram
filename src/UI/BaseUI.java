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

    // Load and scale the image icon
    ImageIcon chickenIcon = new ImageIcon(new ImageIcon("img/icons/chicken.png").getImage().getScaledInstance(20, 20, Image.SCALE_AREA_AVERAGING));

    // Create the label with text and an icon positioned to the right
    JLabel titleLabel = new JLabel(title, chickenIcon, SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    titleLabel.setForeground(Color.WHITE);
    titleLabel.setHorizontalTextPosition(SwingConstants.LEFT); // Position text to the left of the icon
    titleLabel.setIconTextGap(10); // Set gap between text and icon

    headerPanel.add(titleLabel, BorderLayout.CENTER);
    headerPanel.setPreferredSize(new Dimension(WIDTH, 40));

    return headerPanel;
  }

  public JPanel createNavigationPanel(PanelKey selectedOption) {
    // Navigation Bar
    JPanel navigationPanel = new JPanel();
    navigationPanel.setBackground(new Color(249, 249, 249));
    navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
    navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    // Adding buttons with selection check
    navigationPanel.add(createIconButton("img/icons/home.png", PanelKey.HOME, selectedOption == PanelKey.HOME));
    navigationPanel.add(Box.createHorizontalGlue());
    navigationPanel.add(createIconButton("img/icons/search.png", PanelKey.EXPLORE, selectedOption == PanelKey.EXPLORE));
    navigationPanel.add(Box.createHorizontalGlue());
    navigationPanel.add(createIconButton("img/icons/add.png", PanelKey.UPLOAD, selectedOption == PanelKey.UPLOAD));
    navigationPanel.add(Box.createHorizontalGlue());
    navigationPanel.add(createIconButton("img/icons/heart.png", PanelKey.NOTIFICATIONS, selectedOption == PanelKey.NOTIFICATIONS));
    navigationPanel.add(Box.createHorizontalGlue());
    navigationPanel.add(createIconButton("img/icons/profile.png", PanelKey.PROFILE, selectedOption == PanelKey.PROFILE));

    return navigationPanel;
  }

  private JButton createIconButton(String iconPath, PanelKey buttonType, boolean isSelected) {
    ImageIcon iconOriginal = new ImageIcon(iconPath);
    Image iconScaled = iconOriginal.getImage().getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);
    JButton button = new JButton(new ImageIcon(iconScaled));
    button.setBorder(BorderFactory.createEmptyBorder());
    button.setContentAreaFilled(false);

    // Optionally highlight the selected button
    if (isSelected) {
      button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
    }

    // Attach action listener to switch panels based on PanelKey
    button.addActionListener(e -> MainFrame.getInstance().showPanel(buttonType));

    return button;
  }
}
