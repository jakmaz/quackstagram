package UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import Database.DAO.PostDAO;
import Logic.Post;
import Logic.SessionManager;

public class HomeUI extends BaseUI {
  private JPanel contentPanel;

  public HomeUI() {
    initializeUI();
  }

  public void initializeUI() {
    setMaximumSize(new Dimension(WIDTH, HEIGHT));
    setLayout(new BorderLayout());

    // Create and add components to the layout
    JPanel headerPanel = createHeaderPanel("Home");
    JScrollPane contentScrollPane = createContentScrollPane();
    JPanel navigationPanel = createNavigationPanel(PanelKey.HOME);

    add(headerPanel, BorderLayout.NORTH);
    add(contentScrollPane, BorderLayout.CENTER);
    add(navigationPanel, BorderLayout.SOUTH);
  }

  private JScrollPane createContentScrollPane() {
    contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    loadUserPosts(); // Load and display posts before returning the scroll pane
    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    // set speed
    scrollPane.getVerticalScrollBar().setUnitIncrement(6);
    return scrollPane;
  }

  private void loadUserPosts() {
    List<Post> posts = fetchPostsForCurrentUser();
    boolean isFirstPost = true; // Flag to handle separator addition correctly
    for (Post post : posts) {
      if (!isFirstPost) {
        // Add a separator before each post except the first
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        contentPanel.add(separator);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Optional: Additional spacing after the separator
      } else {
        isFirstPost = false;
      }

      PostPanel postPanel = new PostPanel(post, 250, false);

      // Add padding around each PostPanel
      Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10); // top, left, bottom, right padding
      postPanel.setBorder(padding);

      contentPanel.add(postPanel);
    }
    contentPanel.revalidate();
    contentPanel.repaint();
  }

  private List<Post> fetchPostsForCurrentUser() {
    // return PostDAO.getAllPosts();
    return PostDAO.getFollowingPosts(SessionManager.getCurrentUser().getId());
  }
}
