package UI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import Logic.Post;
import Logic.User;

public abstract class ProfileUI extends BaseUI {

  public static final int PROFILE_IMAGE_SIZE = 80; // Adjusted size for the profile image to match UI
  public static final int GRID_IMAGE_SIZE = WIDTH / 3; // Static size for grid images
  private final JPanel contentPanel;
  public User currentUser; // User object to store the current user's information

  public ProfileUI() {
    setSize(WIDTH, HEIGHT);
    setLayout(new BorderLayout()); // Main layout of the JFrame
    contentPanel = new JPanel(new CardLayout()); // Panel to switch between different views
    add(contentPanel, BorderLayout.CENTER); // Add the content panel to the center of the JFrame
  }

  @Override
  public void initializeUI() {
    JPanel headerPanel = createHeaderPanel();
    JPanel gridPanel = createImageGrid();
    JPanel navigationPanel = createNavigationPanel();

    // Add header and navigation directly to the main JFrame's BorderLayout, not in
    // the contentPanel
    add(headerPanel, BorderLayout.NORTH);
    add(navigationPanel, BorderLayout.SOUTH);

    // Add the grid panel to the contentPanel which has CardLayout
    contentPanel.add(gridPanel, "Grid");
    ((CardLayout) contentPanel.getLayout()).show(contentPanel, "Grid");
  }

  private JPanel createHeaderPanel() {
    JPanel headerPanel = new JPanel();
    headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
    headerPanel.setBackground(Color.GRAY);

    // Add profile image, stats, and follow button
    JPanel topHeaderPanel = createTopHeaderPanel();
    headerPanel.add(topHeaderPanel);

    // Add profile name and bio
    JPanel profileNameAndBioPanel = createProfileNameAndBioPanel();
    headerPanel.add(profileNameAndBioPanel);

    return headerPanel;
  }

  private JPanel createTopHeaderPanel() {
    JPanel topHeaderPanel = new JPanel(new BorderLayout(10, 0));
    topHeaderPanel.setBackground(new Color(249, 249, 249));

    // Add profile image
    JLabel profileImage = createProfileImage();
    topHeaderPanel.add(profileImage, BorderLayout.WEST);

    // Add stats and follow button
    JPanel statsFollowPanel = createStatsFollowPanel();
    topHeaderPanel.add(statsFollowPanel, BorderLayout.CENTER);

    return topHeaderPanel;
  }

  private JLabel createProfileImage() {
    ImageIcon profileIcon = new ImageIcon(new ImageIcon("img/storage/profile/" + currentUser.getUsername() + ".png")
        .getImage().getScaledInstance(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Image.SCALE_SMOOTH));
    JLabel profileImage = new JLabel(profileIcon);
    profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    return profileImage;
  }

  private JPanel createStatsFollowPanel() {
    JPanel statsFollowPanel = new JPanel();
    statsFollowPanel.setLayout(new BoxLayout(statsFollowPanel, BoxLayout.Y_AXIS));
    statsFollowPanel.setBackground(new Color(249, 249, 249));

    // Add stats
    JPanel statsPanel = createStatsPanel();
    statsFollowPanel.add(statsPanel);

    // Add action button
    JPanel headerActionButtons = createHeaderActionButtons();
    statsFollowPanel.add(headerActionButtons);

    // Set a right margin
    statsFollowPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // top, left, bottom, right

    return statsFollowPanel;
  }

  private JPanel createStatsPanel() {
    JPanel statsPanel = new JPanel();
    statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
    statsPanel.setBackground(new Color(249, 249, 249));
    statsPanel.add(createStatLabel(Integer.toString(currentUser.getPostsCount()), "Posts"));
    statsPanel.add(createStatLabel(Integer.toString(currentUser.getFollowersCount()), "Followers"));
    statsPanel.add(createStatLabel(Integer.toString(currentUser.getFollowingCount()), "Following"));
    statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0)); // Add some vertical padding
    return statsPanel;
  }

  abstract JPanel createHeaderActionButtons();

  private JPanel createProfileNameAndBioPanel() {
    JPanel profileNameAndBioPanel = new JPanel();
    profileNameAndBioPanel.setLayout(new BorderLayout());
    profileNameAndBioPanel.setBackground(new Color(249, 249, 249));

    JLabel profileNameLabel = new JLabel(currentUser.getUsername());
    profileNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
    profileNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Padding on the sides

    JTextArea profileBio = new JTextArea(currentUser.getBio());
    profileBio.setEditable(false);
    profileBio.setFont(new Font("Arial", Font.PLAIN, 12));
    profileBio.setBackground(new Color(249, 249, 249));
    profileBio.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // Padding on the sides

    profileNameAndBioPanel.add(profileNameLabel, BorderLayout.NORTH);
    profileNameAndBioPanel.add(profileBio, BorderLayout.CENTER);

    return profileNameAndBioPanel;
  }

  private void handleFollowAction(String usernameToFollow) {
    // TODO: to implement
  }

  private JPanel createImageGrid() {
    JPanel gridPanel = new JPanel(new GridLayout(0, 3, 5, 5)); // Adjusted for the grid layout
    List<Post> posts = currentUser.getPosts();
    for (Post post : posts) {
      ImageIcon icon = new ImageIcon(new ImageIcon(post.getImagePath()).getImage().getScaledInstance(GRID_IMAGE_SIZE,
          GRID_IMAGE_SIZE, Image.SCALE_SMOOTH));
      JLabel label = new JLabel(icon);
      label.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          displayPost(post);
        }
      });
      gridPanel.add(label);
    }
    return gridPanel;
  }

  private void displayPost(Post post) {
    PostPanel postPanel = new PostPanel(post);
    contentPanel.add(postPanel, "Post");
    CardLayout cl = (CardLayout) (contentPanel.getLayout());
    cl.show(contentPanel, "Post");

    postPanel.getBackButton().addActionListener(e -> cl.show(contentPanel, "Grid"));
  }

  private void displayImage(ImageIcon imageIcon) {
    contentPanel.removeAll(); // Remove existing content
    contentPanel.setLayout(new BorderLayout()); // Change layout for image display

    JLabel fullSizeImageLabel = new JLabel(imageIcon);
    fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);
    contentPanel.add(fullSizeImageLabel, BorderLayout.CENTER);

    JButton backButton = new JButton("Back");
    backButton.addActionListener(e -> {
      initializeUI(); // Re-initialize the UI
    });
    contentPanel.add(backButton, BorderLayout.SOUTH);

    revalidate();
    repaint();
  }

  private JLabel createStatLabel(String number, String text) {
    JLabel label = new JLabel("<html><div style='text-align: center;'>" + number + "<br/>" + text + "</div></html>",
        SwingConstants.CENTER);
    label.setFont(new Font("Arial", Font.BOLD, 12));
    label.setForeground(Color.BLACK);
    return label;
  }

}
