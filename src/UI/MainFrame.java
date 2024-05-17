package UI;

import Logic.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MainFrame extends JFrame {
  private static MainFrame instance;
  private CardLayout cardLayout;
  private JPanel mainPanel;
  private JPanel loadingPanel;
  private final Map<String, Supplier<BaseUI>> panelSuppliers = new HashMap<>();
  private final Map<String, BaseUI> initializedPanels = new HashMap<>();
  private String expectedPanelToShow = null;


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
    initializeLoadingPanel();
    loadLoginPanels();
    showSignInPanel();
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

  private void initializeLoadingPanel() {
    loadingPanel = new LoadingPanelUI();
    mainPanel.add(loadingPanel, "LoadingPanel");
  }

  public void initializeSuppliers() {
    panelSuppliers.put("SignIn", SignInUI::new);
    panelSuppliers.put("SignUp", SignUpUI::new);
    panelSuppliers.put("Home", HomeUI::new);
    panelSuppliers.put("Explore", ExploreUI::new);
    panelSuppliers.put("Upload", UploadUI::new);
    panelSuppliers.put("Notifications", NotificationsUI::new);
    panelSuppliers.put("Profile", OwnProfileUI::new);
  }

  public void loadLoginPanels() {
    preloadPanel("SignIn");
    preloadPanel("SignUp");
  }

  public void loadUserPanels() {
    loadPanelsInThreads("Home", "Explore", "Upload", "Notifications");
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
            BaseUI panel = get();
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
      worker.execute();
    }
  }

  private BaseUI preloadPanel(String name) {
    System.out.println("Preloading or reloading panel: " + name);
    Supplier<BaseUI> supplier = panelSuppliers.get(name);
    if (supplier != null) {
      BaseUI existingPanel = initializedPanels.get(name);
      if (existingPanel != null) {
        mainPanel.remove(existingPanel);
        System.out.println("Existing panel removed: " + name);
      }
      BaseUI newPanel = supplier.get();
      mainPanel.add(newPanel, name);
      initializedPanels.put(name, newPanel);
      return newPanel;
    } else {
      System.out.println("Error: No supplier found for panel " + name);
      return null;
    }
  }

  private boolean isPanelLoaded(String name) {
    return initializedPanels.containsKey(name);
  }

  private void switchPanel(String name) {
    System.out.println("Switching to panel: " + name);
    cardLayout.show(mainPanel, name);
  }

  private void showLoadingPanel() {
    SwingUtilities.invokeLater(() -> {
      System.out.println("Showing loading panel.");
      cardLayout.show(mainPanel, "LoadingPanel");
    });
  }

  public void showPanel(String name) {
    expectedPanelToShow = name;  // Set the currently expected panel

    if (!isPanelLoaded(name)) {
      showLoadingPanel(); // Show the loading panel
      // Load the panel asynchronously
      SwingWorker<Void, Void> worker = new SwingWorker<>() {
        @Override
        protected Void doInBackground() throws Exception {
          preloadPanel(name); // Load panel in background
          return null;
        }

        @Override
        protected void done() {
          // Once loading is complete, check if this panel is still expected
          SwingUtilities.invokeLater(() -> {
            if (name.equals(expectedPanelToShow)) {
              switchPanel(name);
            }
          });
        }
      };
      worker.execute();
    } else {
      switchPanel(name); // If the panel is already loaded, simply display it
    }
  }

  // Load the Profile panel, typically called after successful sign-in or sign-up
  public void loadProfilePanel() {
    preloadPanel("Profile");
  }

  public void showSignInPanel() {
    showPanel("SignIn");
  }

  public void showSignUpPanel() {
    showPanel("SignUp");
  }

  public void showHomePanel() {
    showPanel("Home");
  }

  public void showExplorePanel() {
    showPanel("Explore");
  }

  public void showUploadPanel() {
    showPanel("Upload");
  }

  public void showNotificationsPanel() {
    showPanel("Notifications");
  }

  public void showProfilePanel() {
    showPanel("Profile");
  }

  public void showOtherProfilePanel(User user) {
    OtherUserProfileUI otherProfileUI = new OtherUserProfileUI(user);
    String panelKey = "UserProfile_" + user.getId();
    mainPanel.add(otherProfileUI, panelKey);
    showPanel(panelKey);
  }

  public void clearUI() {
    for (BaseUI panel : initializedPanels.values()) {
      mainPanel.remove(panel);
      panel.setVisible(false);
      panel.removeAll();
    }
    initializedPanels.clear();
    mainPanel.revalidate();
    mainPanel.repaint();
    expectedPanelToShow = null; // Reset expected panel
    System.out.println("All UI panels have been cleared.");
  }
}
