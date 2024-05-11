package UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import Database.DAO.PostDAO;
import Logic.Post;
import Logic.SessionManager;

public class HomeUI extends BaseUI {
  private JPanel contentPanel;

  public HomeUI() {
    super(); // Ensure the superclass constructor is called if needed
    initializeUI();
  }

  public void initializeUI() {
    setSize(WIDTH, HEIGHT);
    setMinimumSize(new Dimension(WIDTH, HEIGHT));
    setLayout(new BorderLayout());

    // Create and add components to the layout
    JPanel headerPanel = createHeaderPanel("Home");
    JScrollPane contentScrollPane = createContentScrollPane();
    JPanel navigationPanel = createNavigationPanel(); // Use the inherited method if applicable

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
    for (Post post : posts) {
      PostPanel postPanel = new PostPanel(post, 250, false);
      contentPanel.add(postPanel);
      contentPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing between posts
    }
    contentPanel.revalidate();
    contentPanel.repaint();
  }

  private List<Post> fetchPostsForCurrentUser() {
    // return PostDAO.getAllPosts();
    return PostDAO.getFollowingPosts(SessionManager.getCurrentUser().getId());
  }
}
