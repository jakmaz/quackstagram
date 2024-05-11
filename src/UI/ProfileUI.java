package UI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.*;

import Logic.Post;
import Logic.User;

public abstract class ProfileUI extends BaseUI {

  public static final int PROFILE_IMAGE_SIZE = 80; // Adjusted size for the profile image to match UI
  public static final int GRID_IMAGE_SIZE = (WIDTH / 3) - 9; // Static size for grid images
  private JPanel contentPanel;
  public User currentUser; // User object to store the current user's information

  public ProfileUI() {
    setSize(WIDTH, HEIGHT);
    setLayout(new BorderLayout()); // Main layout of the JFrame
  }

  @Override
  public void initializeUI() {
    JPanel headerPanel = createHeaderPanel();
    contentPanel = new JPanel(new CardLayout()); // Panel to switch between different views
    JPanel navigationPanel = createNavigationPanel();

    // Create a JScrollPane that wraps around the contentPanel
    JScrollPane scrollPane = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    // Add header and navigation directly to the main JFrame's BorderLayout, not in the contentPanel
    scrollPane.getVerticalScrollBar().setUnitIncrement(3);
    add(headerPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER); // Add the scroll pane (which contains contentPanel) to the center of the JFrame
    add(navigationPanel, BorderLayout.SOUTH);

    // Add the grid panel to the contentPanel which has CardLayout
    JPanel gridPanel = createImageGrid();
    contentPanel.add(gridPanel, "Grid");
    ((CardLayout) contentPanel.getLayout()).show(contentPanel, "Grid");
    setVisible(true);
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
    if (osName.contains("win")) {
      statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
    } else {
      statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
    }
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

  private JPanel createImageGrid() {
    // Main panel with FlowLayout to align items from top-left to bottom-right
    JPanel gridPanel;
    if (osName.contains("win")) {
      gridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    } else {
      gridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
    }

    // Panel that will actually hold the images
    JPanel imagesPanel = new JPanel(new GridLayout(0, 3, 5, 5)); // GridLayout with 3 columns
    imagesPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align panel to the left

    List<Post> posts = currentUser.getPosts();
    for (Post post : posts) {
      System.out.println(post.getImagePath());

      ImageIcon icon = new ImageIcon(new ImageIcon(post.getImagePath()).getImage().getScaledInstance(GRID_IMAGE_SIZE,
          GRID_IMAGE_SIZE, Image.SCALE_SMOOTH));
      JLabel label = new JLabel(icon);
      label.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          displayPost(post);
        }
      });
      imagesPanel.add(label);
    }

    // Ensure the imagesPanel doesn't grow beyond its necessary size
    if (osName.contains("win")) {
      imagesPanel.setPreferredSize(new Dimension((GRID_IMAGE_SIZE ) * 3 + 11, (GRID_IMAGE_SIZE + 5) * ((posts.size() + 2) / 3)));
    } else {
      imagesPanel.setPreferredSize(new Dimension((GRID_IMAGE_SIZE + 5) * 3, (GRID_IMAGE_SIZE + 5) * ((posts.size() + 2) / 3)));
    }

    gridPanel.add(imagesPanel);

    return gridPanel;
  }

  private void displayPost(Post post) {
    PostPanel profilePostPanel = new PostPanel(post, 100, true);
    contentPanel.add(profilePostPanel, "Post");
    CardLayout cl = (CardLayout) (contentPanel.getLayout());
    cl.show(contentPanel, "Post");

    profilePostPanel.getBackButton().addActionListener(e -> cl.show(contentPanel, "Grid"));
  }

  private JLabel createStatLabel(String number, String text) {
    JLabel label = new JLabel("<html><div style='text-align: center;'>" + number + "<br/>" + text + "</div></html>",
        SwingConstants.CENTER);
    label.setFont(new Font("Arial", Font.BOLD, 12));
    label.setForeground(Color.BLACK);
    return label;
  }

}
