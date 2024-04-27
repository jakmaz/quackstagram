import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class BaseUI extends JPanel {
  private static final int NAV_ICON_SIZE = 20; // Corrected static size for bottom icons

  public BaseUI() {
    setLayout(new BorderLayout());
  }

  protected abstract void initializeUI();

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

  public JButton createIconButton(String iconPath, String buttonType) {
    ImageIcon iconOriginal = new ImageIcon(iconPath);
    Image iconScaled = iconOriginal.getImage().getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);
    JButton button = new JButton(new ImageIcon(iconScaled));
    button.setBorder(BorderFactory.createEmptyBorder());
    button.setContentAreaFilled(false);

    // Define actions based on button type
    if ("home".equals(buttonType)) {
      button.addActionListener(e -> MainFrame.getInstance().switchPanel("Home"));
    } else if ("profile".equals(buttonType)) {
      button.addActionListener(e -> MainFrame.getInstance().switchPanel("InstagramProfile"));
    } else if ("notification".equals(buttonType)) {
      button.addActionListener(e -> MainFrame.getInstance().switchPanel("Notifications"));
    } else if ("explore".equals(buttonType)) {
      button.addActionListener(e -> MainFrame.getInstance().switchPanel("Explore"));
    } else if ("add".equals(buttonType)) {
      button.addActionListener(e -> MainFrame.getInstance().switchPanel("Upload"));
    }
    return button;

  }
}
