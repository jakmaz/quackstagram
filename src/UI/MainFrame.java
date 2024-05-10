package UI;

import java.awt.CardLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Logic.SessionManager;
import Logic.User;

public class MainFrame extends JFrame {
  private static MainFrame instance;
  private CardLayout cardLayout;
  private JPanel mainPanel;
  private Map<String, Supplier<BaseUI>> panelSuppliers = new HashMap<>();
  private Map<String, BaseUI> initializedPanels = new HashMap<>();

  public static MainFrame getInstance() {
    if (instance == null) {
      instance = new MainFrame();
    }
    return instance;
  }

  private MainFrame() {
    super("Quackstagram Application");
    initializeFrame();
    initializeSuppliers();
    loadLoginPanels(); // Initially load only login-related panels
    showSignInPanel(); // Display the Sign In panel initially
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

  // Initializes the suppliers for all panel types
  public void initializeSuppliers() {
    panelSuppliers.put("SignIn", SignInUI::new);
    panelSuppliers.put("SignUp", SignUpUI::new);
    panelSuppliers.put("Home", HomeUI::new);
    panelSuppliers.put("Explore", ExploreUI::new);
    panelSuppliers.put("Upload", UploadUI::new);
    panelSuppliers.put("Notifications", NotificationsUI::new);
    panelSuppliers.put("Profile", OwnProfileUI::new);
    panelSuppliers.put("OtherProfile", UserProfileUI::new);
  }

  // Load panels necessary for login
  public void loadLoginPanels() {
    preloadPanel("SignIn");
    preloadPanel("SignUp");
  }

  // Load the Profile panel, typically called after successful sign-in or sign-up
  public void loadProfilePanel() {
    preloadPanel("Profile");
  }

  // Load other panels that require user information, called after showing the
  // Profile panel
  public void loadUserPanels() {
    preloadPanel("Home");
    preloadPanel("Explore");
    preloadPanel("Upload");
    preloadPanel("Notifications");
  }

  // General method to preload a panel based on the name
  private void preloadPanel(String name) {
    System.out.println("Preloading panel: " + name);
    Supplier<BaseUI> supplier = panelSuppliers.get(name);
    if (supplier != null) {
      BaseUI panel = supplier.get();
      mainPanel.add(panel, name);
      initializedPanels.put(name, panel);
    } else {
      System.out.println("Error: No supplier found for panel " + name);
    }
  }

  // Switch to a specific panel by name
  private void switchPanel(String name) {
    System.out.println("Switching to panel: " + name);
    cardLayout.show(mainPanel, name);
    setTitle(name);
  }

  // Individual methods for displaying each panel
  public void showSignInPanel() {
    switchPanel("SignIn");
  }

  public void showSignUpPanel() {
    switchPanel("SignUp");
  }

  public void showHomePanel() {
    switchPanel("Home");
  }

  public void showExplorePanel() {
    switchPanel("Explore");
  }

  public void showUploadPanel() {
    switchPanel("Upload");
  }

  public void showNotificationsPanel() {
    switchPanel("Notifications");
  }

  public void showProfilePanel() {
    switchPanel("Profile");
  }

  public void showOtherProfilePanel(User user) {
    UserProfileUI otherProfileUI = (UserProfileUI) initializedPanels.get("OtherProfile");
    otherProfileUI.setUser(user);
    switchPanel("OtherProfile");
  }

  // Clears all UI panels, typically used during log out
  public void clearUI() {
    for (BaseUI panel : initializedPanels.values()) {
      mainPanel.remove(panel);
      panel.setVisible(false);
      panel.removeAll();
    }
    initializedPanels.clear();
    mainPanel.revalidate();
    mainPanel.repaint();
    System.out.println("All UI panels have been cleared.");
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      MainFrame frame = MainFrame.getInstance();
      frame.setVisible(true);
    });
  }
}
