package UI;

import Logic.SessionManager;
import Logic.User;

import javax.swing.*;
import java.awt.CardLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MainFrame extends JFrame {
  private static MainFrame instance;
  private CardLayout cardLayout;
  private JPanel mainPanel;
  private Map<String, Supplier<BaseUI>> panelSuppliers;
  private Map<String, BaseUI> initializedPanels;

  public static MainFrame getInstance() {
    if (instance == null) {
      instance = new MainFrame();
    }
    return instance;
  }

  public static MainFrame createNewInstance() {
    instance = new MainFrame();
    return instance;
  }

  private MainFrame() {
    super("Quackstagram Application");
    initializeFrame();
    initializePanels();
  }

  private void initializeFrame() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    setSize(300, 500);
    setLocationRelativeTo(null);
    cardLayout = new CardLayout();
    mainPanel = new JPanel(cardLayout);
    add(mainPanel);

    mainPanel.setFocusable(true);
    mainPanel.requestFocusInWindow();
    mainPanel.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
          System.out.println("Escape pressed, closing application.");
          dispose();
          System.exit(0);
        }
      }
    });
  }

  private void initializePanels() {
    panelSuppliers = new HashMap<>();
    initializedPanels = new HashMap<>();

    // Register panel suppliers
    panelSuppliers.put("SignIn", SignInUI::new);
    panelSuppliers.put("SignUp", SignUpUI::new);
    panelSuppliers.put("Home", HomeUI::new);
    panelSuppliers.put("Explore", ExploreUI::new);
    panelSuppliers.put("Upload", UploadUI::new);
    panelSuppliers.put("Notifications", NotificationsUI::new);
    panelSuppliers.put("Profile", OwnProfileUI::new);
    panelSuppliers.put("OtherProfile", UserProfileUI::new);

    // Preload initial panel
    preloadPanel("SignIn");
  }

  private void preloadPanel(String name) {
    System.out.println("Preloading panel: " + name);
    Supplier<BaseUI> supplier = panelSuppliers.get(name);
    if (supplier != null) {
      BaseUI panel = supplier.get();
      mainPanel.add(panel, name);
      initializedPanels.put(name, panel);
    } else {
      System.out.println("Error: No supplier found for " + name);
    }
  }

  public void switchPanel(String name) {
    if (!initializedPanels.containsKey(name)) {
      preloadPanel(name);
    }
    System.out.println("Switching to panel: " + name);
    cardLayout.show(mainPanel, name);
    BaseUI panel = initializedPanels.get(name);
    if (panel != null) {
      setTitle(name + " - " + SessionManager.getCurrentUser().getUsername());
    } else {
      System.out.println("Error: Panel not initialized - " + name);
    }
  }

  public void clearUI() {
    for (BaseUI panel : initializedPanels.values()) {
      mainPanel.remove(panel); // Remove the panel from the mainPanel container
      panel.setVisible(false); // Make panel non-visible
      panel.removeAll(); // Remove all components from the panel
    }
    initializedPanels.clear(); // Clear the map to remove all stored references
    mainPanel.revalidate(); // Revalidate the container to update its state
    mainPanel.repaint(); // Repaint the container to refresh the UI display
    System.out.println("All UI panels have been cleared.");
  }

  public void switchToUserProfile(User user) {
    switchPanel("OtherProfile");
    // UserProfileUI otherProfileUI = (UserProfileUI)
    // initializedPanels.computeIfAbsent("OtherProfile", k -> new UserProfileUI());
    UserProfileUI otherProfileUI = (UserProfileUI) initializedPanels.get("OtherProfile");
    otherProfileUI.setUser(user);
    // switchPanel("OtherProfile");
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      MainFrame frame = MainFrame.getInstance();
      frame.setVisible(true);
    });
  }
}
