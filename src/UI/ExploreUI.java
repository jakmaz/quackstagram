package UI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import Database.DAO.PostDAO;
import Logic.Post;

public class ExploreUI extends BaseUI {

  private static final int WIDTH = 300;
  private static final int HEIGHT = 500;
  private static final int IMAGE_SIZE = (WIDTH / 3) - 10;

  private JPanel contentPanel; // Panel to switch between the grid and individual post views

  public ExploreUI() {
    setSize(WIDTH, HEIGHT);
    setMinimumSize(new Dimension(WIDTH, HEIGHT));
    setLayout(new BorderLayout());
    initializeUI();
  }

  public void initializeUI() {
    JPanel headerPanel = createHeaderPanel("Explore");
    JPanel navigationPanel = createNavigationPanel();

    contentPanel = new JPanel(new CardLayout()); // Use CardLayout for the content panel
    JPanel mainContentPanel = createMainContentPanel(); // This is the grid view
    contentPanel.add(mainContentPanel, "Grid");

    add(headerPanel, BorderLayout.NORTH);
    add(contentPanel, BorderLayout.CENTER);
    add(navigationPanel, BorderLayout.SOUTH);
  }

  private JPanel createMainContentPanel() {
    JPanel mainContentPanel = new JPanel(new BorderLayout());
    JTextField searchField = new JTextField(" Search Users");
    mainContentPanel.add(searchField, BorderLayout.NORTH);

    JPanel imageGridPanel = new JPanel(new GridLayout(0, 3, 2, 2));
    List<Post> posts = PostDAO.getAllPosts();
    for (Post post : posts) {
      ImageIcon imageIcon = new ImageIcon(
          new ImageIcon(post.getImagePath()).getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH));
      JLabel imageLabel = new JLabel(imageIcon);
      imageLabel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          showPostDetails(post);
        }
      });
      imageGridPanel.add(imageLabel);
    }

    JScrollPane scrollPane = new JScrollPane(imageGridPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.getVerticalScrollBar().setUnitIncrement(6);
    mainContentPanel.add(scrollPane, BorderLayout.CENTER);

    return mainContentPanel;
  }

  private void showPostDetails(Post post) {
    PostPanel postPanel = new PostPanel(post, 200, true);
    JScrollPane scrollPane = new JScrollPane(postPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.getVerticalScrollBar().setUnitIncrement(3);
    contentPanel.add(scrollPane, "PostDetails");
    CardLayout cl = (CardLayout) (contentPanel.getLayout());
    cl.show(contentPanel, "PostDetails");

    // To add a 'back' functionality to return to the grid view:
    postPanel.getBackButton().addActionListener(e -> cl.show(contentPanel, "Grid"));
  }
}
