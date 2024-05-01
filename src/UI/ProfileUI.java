package UI;

import Logic.User;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.awt.*;
import java.nio.file.*;
import java.util.stream.Stream;

public abstract class ProfileUI extends BaseUI {

  public static final int PROFILE_IMAGE_SIZE = 80; // Adjusted size for the profile image to match UI
  public static final int GRID_IMAGE_SIZE = WIDTH / 3; // Static size for grid images
  private JPanel contentPanel; //  to display the image grid or the clicked image
  public User currentUser; // User object to store the current user's information

  public ProfileUI() {
    setSize(WIDTH, HEIGHT);
    setMinimumSize(new Dimension(WIDTH, HEIGHT));
    setLayout(new BorderLayout());
  }

  @Override
  public void initializeUI() {
    contentPanel = new JPanel();
    JPanel headerPanel = createHeaderPanel(); // attempt to recreate if null
    JPanel navigationPanel = createNavigationPanel(); // attempt to recreate if null

    add(headerPanel, BorderLayout.NORTH);
    add(navigationPanel, BorderLayout.SOUTH);

    initializeImageGrid();
//    revalidate();
//    repaint();
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
    ImageIcon profileIcon = new ImageIcon(new ImageIcon("img/storage/profile/admin.png")
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
    JButton headerActionButton = createHeaderActionButton();
    statsFollowPanel.add(headerActionButton);

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

  abstract JButton createHeaderActionButton();

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

  private void initializeImageGrid() {
    contentPanel.removeAll(); // Clear existing content
    contentPanel.setLayout(new GridLayout(0, 3, 5, 5)); // Grid layout for image grid

    Path imageDir = Paths.get("img", "uploaded");
    try (Stream<Path> paths = Files.list(imageDir)) {
      paths.filter(path -> path.getFileName().toString().startsWith(currentUser.getUsername() + "_"))
          .forEach(path -> {
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(path.toString()).getImage()
                .getScaledInstance(GRID_IMAGE_SIZE, GRID_IMAGE_SIZE, Image.SCALE_SMOOTH));
            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.addMouseListener(new MouseAdapter() {
              @Override
              public void mouseClicked(MouseEvent e) {
                displayImage(imageIcon); // Call method to display the clicked image
              }
            });
            contentPanel.add(imageLabel);
          });
    } catch (IOException ex) {
      ex.printStackTrace();
      // Handle exception (e.g., show a message or log)
    }

    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center

    revalidate();
    repaint();
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
