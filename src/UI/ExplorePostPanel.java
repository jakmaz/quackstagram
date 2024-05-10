package UI;

import Logic.Post;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.imageio.ImageIO;

public class ExplorePostPanel extends JPanel {
  private Post post;
  private JButton backButton; // Declare backButton at the class level for accessibility

  public ExplorePostPanel(Post post) {
    this.post = post;
    initializeUI();
  }

  private void initializeUI() {
    setLayout(new BorderLayout());

    // Main vertical panel with BoxLayout to simulate Flexbox column
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add padding to the main panel

    // Navigation panel for the back button, with reduced height
    JPanel navPanel = new JPanel(new BorderLayout());
    backButton = new JButton("Back");
    backButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // Control the height more precisely
    navPanel.add(backButton, BorderLayout.NORTH);
    navPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, backButton.getPreferredSize().height));

    // Top panel for user and timestamp, with reduced spacing
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); // Reduce the horizontal and vertical gap
    JButton usernameButton = new JButton(post.getUser().getUsername());
    usernameButton.addActionListener(e -> MainFrame.getInstance().switchToUserProfile(post.getUser()));
    LocalDateTime now = LocalDateTime.now();
    String timeSincePosting = ChronoUnit.DAYS.between(post.getTimestamp().toLocalDateTime(), now) + " days ago";
    JLabel timeLabel = new JLabel(timeSincePosting);
    topPanel.add(usernameButton);
    topPanel.add(timeLabel);

    // Image display centered
    JPanel imagePanel = new JPanel(new GridBagLayout()); // Use GridBagLayout for better centering
    JLabel imageLabel = new JLabel();
    try {
      BufferedImage img = ImageIO.read(new File(post.getImagePath()));
      ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
      imageLabel.setIcon(imageIcon);
    } catch (IOException ex) {
      imageLabel.setText("Image not found");
      ex.printStackTrace();
    }
    imagePanel.add(imageLabel); // This will center the image

    // Bottom panel for likes and caption, with adjusted scroll pane dimensions
    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
    JTextArea captionTextArea = new JTextArea(post.getCaption(), 2, 20); // Set rows to 2 to reduce area
    captionTextArea.setEditable(false);
    captionTextArea.setWrapStyleWord(true);
    captionTextArea.setLineWrap(true);
    JScrollPane captionScroll = new JScrollPane(captionTextArea);
    captionScroll.setPreferredSize(new Dimension(Integer.MAX_VALUE, 60)); // Control the preferred height
    JLabel likesLabel = new JLabel("Likes: " + post.getLikesCount());
    bottomPanel.add(captionScroll);
    bottomPanel.add(likesLabel);

    // Add subpanels to main panel
    mainPanel.add(navPanel);
    mainPanel.add(topPanel);
    mainPanel.add(imagePanel);
    mainPanel.add(bottomPanel);

    // Add main panel to ExplorePostPanel
    add(mainPanel, BorderLayout.CENTER);
  }

  public JButton getBackButton() {
    return backButton;
  }
}
