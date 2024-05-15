package UI;

import java.awt.CardLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.swing.*;

import Logic.User;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

public class MainFrame extends JFrame {
  private static MainFrame instance;
  private CardLayout cardLayout;
  private JPanel mainPanel;
  private final Map<String, Supplier<BaseUI>> panelSuppliers = new HashMap<>();
  private final Map<String, BaseUI> initializedPanels = new HashMap<>();

  public static MainFrame getInstance() {
    if (instance == null) {
      instance = new MainFrame();
    }
    return instance;
  }

  private MainFrame() {
    super("Quackstagram Application");
    setTitle("Quackstagram Application");
    initializeFrame();
    initializeSuppliers();
    loadLoginPanels(); // Initially load only login-related panels
    showSignInPanel(); // Display the Sign In panel initially
  }

  private void initializeFrame() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

  public void loadUserPanels() {
    loadPanelsInThreads("Notifications", "Upload", "Home", "Explore");
  }

  private void loadPanelsInThreads(String... panelNames) {
    for (String name : panelNames) {
      SwingWorker<BaseUI, Void> worker = new SwingWorker<>() {
        @Override
        protected BaseUI doInBackground() throws Exception {
          return preloadPanel(name);
        }

        @Override
        protected void done() {
          try {
            BaseUI panel = get(); // Retrieve the loaded panel
            if (panel != null) {
              System.out.println("Panel loaded and added to mainPanel: " + name);
              mainPanel.revalidate();
              mainPanel.repaint();
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      };
      worker.execute(); // Start the worker thread
    }
  }

  // General method to preload or reload a panel based on the name
  private BaseUI preloadPanel(String name) {
    System.out.println("Preloading or reloading panel: " + name);
    Supplier<BaseUI> supplier = panelSuppliers.get(name);
    if (supplier != null) {
      // Remove the existing panel if it has already been initialized
      BaseUI existingPanel = initializedPanels.get(name);
      if (existingPanel != null) {
        mainPanel.remove(existingPanel);
        System.out.println("Existing panel removed: " + name);
      }

      // Get a new instance of the panel from the supplier
      BaseUI newPanel = supplier.get();
      mainPanel.add(newPanel, name);
      initializedPanels.put(name, newPanel); // Update the map with the new panel instance
      return newPanel;
    } else {
      System.out.println("Error: No supplier found for panel " + name);
      return null;
    }
  }

  // Switch to a specific panel by name
  private void switchPanel(String name) {
    System.out.println("Switching to panel: " + name);
    cardLayout.show(mainPanel, name);
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
    FlatLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", "#1E1F22"));
    FlatMacLightLaf.setup();
    SwingUtilities.invokeLater(() -> {
      MainFrame frame = MainFrame.getInstance();
      frame.setVisible(true);
    });
  }
}
