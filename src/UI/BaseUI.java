package UI;

import java.awt.*;
import javax.swing.*;

public abstract class BaseUI extends JPanel {
  public static final int WIDTH = 300;
  public static final int HEIGHT = 500;
  private static final int NAV_ICON_SIZE = 20; // Corrected static size for bottom icons
  protected String osName = System.getProperty("os.name").toLowerCase();

  public BaseUI() {
    setLayout(new BorderLayout());
  }

  protected abstract void initializeUI();

  public static JPanel createHeaderPanel(String title) {
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setBackground(new Color(51, 51, 51));

    JLabel titleLabel = new JLabel(title + " 🐥", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    titleLabel.setForeground(Color.WHITE);

    headerPanel.add(titleLabel, BorderLayout.CENTER);
    headerPanel.setPreferredSize(new Dimension(400, 40)); // Replace 400 with your WIDTH constant if you have one.

    return headerPanel;
  }

  public JPanel createNavigationPanel() {
    // Navigation Bar
    JPanel navigationPanel = new JPanel();
    navigationPanel.setBackground(new Color(249, 249, 249));
    navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
    navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    navigationPanel.add(createIconButton("img/icons/home.png", "home"));
    navigationPanel.add(Box.createHorizontalGlue());
    navigationPanel.add(createIconButton("img/icons/search.png", "explore"));
    navigationPanel.add(Box.createHorizontalGlue());
    navigationPanel.add(createIconButton("img/icons/add.png", "add"));
    navigationPanel.add(Box.createHorizontalGlue());
    navigationPanel.add(createIconButton("img/icons/heart.png", "notification"));
    navigationPanel.add(Box.createHorizontalGlue());
    navigationPanel.add(createIconButton("img/icons/profile.png", "profile"));

    return navigationPanel;

  }

  private JButton createIconButton(String iconPath, String buttonType) {
    ImageIcon iconOriginal = new ImageIcon(iconPath);
    Image iconScaled = iconOriginal.getImage().getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);
    JButton button = new JButton(new ImageIcon(iconScaled));
    button.setBorder(BorderFactory.createEmptyBorder());
    button.setContentAreaFilled(false);

    // Define actions based on button type
    if ("home".equals(buttonType)) {
      button.addActionListener(e -> MainFrame.getInstance().showHomePanel());
    } else if ("profile".equals(buttonType)) {
      button.addActionListener(e -> MainFrame.getInstance().showProfilePanel());
    } else if ("notification".equals(buttonType)) {
      button.addActionListener(e -> MainFrame.getInstance().showNotificationsPanel());
    } else if ("explore".equals(buttonType)) {
      button.addActionListener(e -> MainFrame.getInstance().showExplorePanel());
    } else if ("add".equals(buttonType)) {
      button.addActionListener(e -> MainFrame.getInstance().showUploadPanel());
    }
    return button;

  }
}
