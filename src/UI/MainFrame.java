package UI;

import Logic.SessionManager;
import Logic.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class MainFrame extends JFrame {
  private static MainFrame instance;
  private CardLayout cardLayout;
  private JPanel mainPanel;
  private JPanel loadingPanel;
  private final Map<PanelKey, Supplier<BaseUI>> panelSuppliers = new HashMap<>();
  private final Map<PanelKey, BaseUI> initializedPanels = new HashMap<>();
  private final Map<String, JPanel> dynamicUserPanels = new HashMap<>();
  private PanelKey expectedPanelToShow = null;


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
    mainPanel.add(loadingPanel, "LOADING");
  }

  private void initializeSuppliers() {
    panelSuppliers.put(PanelKey.SIGN_IN, SignInUI::new);
    panelSuppliers.put(PanelKey.SIGN_UP, SignUpUI::new);
    panelSuppliers.put(PanelKey.HOME, HomeUI::new);
    panelSuppliers.put(PanelKey.EXPLORE, ExploreUI::new);
    panelSuppliers.put(PanelKey.UPLOAD, UploadUI::new);
    panelSuppliers.put(PanelKey.NOTIFICATIONS, NotificationsUI::new);
    panelSuppliers.put(PanelKey.PROFILE, OwnProfileUI::new);
    panelSuppliers.put(PanelKey.LOADING, LoadingPanelUI::new);
  }

  public void loadLoginPanels() {
    preloadPanel(PanelKey.SIGN_IN);
    preloadPanel(PanelKey.SIGN_UP);
  }

  public void loadUserPanels() {
    loadPanelsInThreads(PanelKey.HOME, PanelKey.EXPLORE, PanelKey.UPLOAD, PanelKey.NOTIFICATIONS);
  }

  private void loadPanelsInThreads(PanelKey... panelKeys) {
    for (PanelKey key : panelKeys) {
      SwingWorker<BaseUI, Void> worker = new SwingWorker<>() {
        @Override
        protected BaseUI doInBackground() throws Exception {
          if (SessionManager.getCurrentUser() == null) {
            throw new IllegalStateException("Operation aborted: no user is currently logged in.");
          }
          try {
            return preloadPanel(key);
          } catch (NullPointerException e) {
            throw new Exception("Required data is missing, which caused a Null Pointer Exception.", e);
          }
        }

        @Override
        protected void done() {
          try {
            BaseUI panel = get(); // May throw ExecutionException if doInBackground() throws an exception
            SwingUtilities.invokeLater(() -> {
              if (key == expectedPanelToShow && panel != null) {
                switchPanel(key);
              }
            });
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Restore interrupted status
            System.err.println("Thread was interrupted: " + e.getMessage());
          } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IllegalStateException) {
              System.err.println("Error loading panel " + key + ": " + cause.getMessage());
            } else if (cause.getMessage().contains("Null Pointer")) {
              System.err.println("Error loading panel " + key + " : Missing data - " + cause.getMessage());
            } else if (cause.getMessage().contains("IOException")) {
              System.err.println("Error loading panel " + key + ": Resource load failure - " + cause.getMessage());
            } else {
              System.err.println("Error loading panel " + key + ": Unexpected error occurred - " + cause.getMessage());
              cause.printStackTrace();  // For detailed diagnostics
            }
          } catch (Exception e) {
            System.err.println("Unhandled exception in SwingWorker: " + e.getMessage());
          }
        }
      };
      worker.execute();
    }
  }

  private BaseUI preloadPanel(PanelKey key) {
    System.out.println("Preloading or reloading panel: " + key);
    Supplier<BaseUI> supplier = panelSuppliers.get(key);
    if (supplier != null) {
      BaseUI existingPanel = initializedPanels.get(key);
      if (existingPanel != null) {
        mainPanel.remove(existingPanel);
        System.out.println("Existing panel removed: " + key);
      }
      BaseUI newPanel = supplier.get();
      mainPanel.add(newPanel, key.name());
      initializedPanels.put(key, newPanel);
      return newPanel;
    } else {
      System.out.println("Error: No supplier found for panel " + key);
      return null;
    }
  }

  private boolean isPanelLoaded(PanelKey key) {
    return initializedPanels.containsKey(key);
  }

  private void switchPanel(PanelKey panel) {
    System.out.println("Switching to panel: " + panel);
    cardLayout.show(mainPanel, panel.name());
  }

  private void showLoadingPanel() {
    System.out.println("Attempting to show loading panel.");
    SwingUtilities.invokeLater(() -> {
      System.out.println("Showing loading panel now.");
      cardLayout.show(mainPanel, PanelKey.LOADING.name());
    });
  }

  public void showPanel(PanelKey key) {
    expectedPanelToShow = key;  // Update the expected panel
    System.out.println("Requested to show panel: " + key);

    if (!isPanelLoaded(key)) {
      System.out.println("Panel " + key + " not loaded, showing loading panel.");
      showLoadingPanel(); // Show the loading panel
      SwingWorker<Void, Void> worker = new SwingWorker<>() {
        @Override
        protected Void doInBackground() throws Exception {
          System.out.println("Loading panel " + key + " in background.");
          preloadPanel(key); // Load the panel in the background
          return null;
        }

        @Override
        protected void done() {
          SwingUtilities.invokeLater(() -> {
            System.out.println("Background loading done for " + key);
            if (key == expectedPanelToShow && SessionManager.getCurrentUser() != null) {
              System.out.println("Switching to panel " + key);
              switchPanel(key);  // Show only if it's still the expected panel
            }
          });
        }
      };
      worker.execute();
    } else {
      System.out.println("Panel " + key + " already loaded, switching directly.");
      switchPanel(key); // If the panel is already loaded, just display it
    }
  }


  // Load the Profile panel, typically called after successful sign-in or sign-up
  public void loadProfilePanel() {
    preloadPanel(PanelKey.PROFILE);
  }

  public void showSignInPanel() {
    showPanel(PanelKey.SIGN_IN);
  }

  public void showSignUpPanel() {
    showPanel(PanelKey.SIGN_UP);
  }

  public void showProfilePanel() {
    showPanel(PanelKey.PROFILE);
  }

  public void showOtherProfilePanel(User user) {
    String panelKey = "UserProfile_" + user.getId(); // Create a unique key for each user profile
    if (!dynamicUserPanels.containsKey(panelKey)) { // Check if the panel is not already loaded
      OtherUserProfileUI otherProfileUI = new OtherUserProfileUI(user);
      mainPanel.add(otherProfileUI, panelKey);
      dynamicUserPanels.put(panelKey, otherProfileUI); // Store it in the dynamic panels map
    }
    cardLayout.show(mainPanel, panelKey); // Use CardLayout to show the panel
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
