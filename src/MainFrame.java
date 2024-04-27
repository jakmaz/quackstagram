import java.awt.CardLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.QuadCurve2D;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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

  private MainFrame() {
    super("Quackstagram Application");
    initializeFrame();
    initializePanels();
  }

  private void initializeFrame() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(300, 500);
    setLocationRelativeTo(null);
    cardLayout = new CardLayout();
    mainPanel = new JPanel(cardLayout);
    add(mainPanel);

    // Adding key listener to the main panel
    mainPanel.setFocusable(true);
    mainPanel.requestFocusInWindow();
    mainPanel.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
          System.out.println("Escape pressed, closing application.");
          dispose(); // Closes the JFrame
          System.exit(0); // Ensure JVM stops completely
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
    panelSuppliers.put("InstagramProfile", InstagramProfileUI::new);
    panelSuppliers.put("Home", QuakstagramHomeUI::new);
    // panelSuppliers.put("Explore", ExploreUI::new);
    // panelSuppliers.put("Upload", ImageUploadUI::new);
    // panelSuppliers.put("Notifications", NotificationsUI::new);

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
      setTitle(panel.getTitle());
    } else {
      System.out.println("Error: Panel not initialized - " + name);
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      MainFrame frame = MainFrame.getInstance();
      frame.setVisible(true);
    });
  }
}
