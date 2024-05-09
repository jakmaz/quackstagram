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

    // Composite panel that includes both nav and top panels
    JPanel compositeTopPanel = new JPanel(new BorderLayout());

    // Navigation panel for the back button
    JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    backButton = new JButton("Back");
    navPanel.add(backButton);

    // Top panel for user and timestamp
    JPanel topPanel = new JPanel(new BorderLayout());
    JButton usernameButton = new JButton(post.getUser().getUsername());
    usernameButton.addActionListener(e -> {
      MainFrame.getInstance().switchToUserProfile(post.getUser());
    });
    LocalDateTime now = LocalDateTime.now();
    String timeSincePosting = ChronoUnit.DAYS.between(post.getTimestamp().toLocalDateTime(), now) + " days ago";
    JLabel timeLabel = new JLabel(timeSincePosting);
    timeLabel.setHorizontalAlignment(JLabel.RIGHT);

    topPanel.add(usernameButton, BorderLayout.WEST);
    topPanel.add(timeLabel, BorderLayout.EAST);

    // Adding navPanel and topPanel to the composite panel
    compositeTopPanel.add(navPanel, BorderLayout.NORTH);
    compositeTopPanel.add(topPanel, BorderLayout.CENTER);

    // Image display
    JLabel imageLabel = new JLabel();
    imageLabel.setHorizontalAlignment(JLabel.CENTER);
    try {
      BufferedImage img = ImageIO.read(new File(post.getImagePath()));
      ImageIcon imageIcon = new ImageIcon(img);
      imageLabel.setIcon(imageIcon);
    } catch (IOException ex) {
      imageLabel.setText("Image not found");
      ex.printStackTrace();
    }

    // Bottom panel for likes and caption
    JPanel bottomPanel = new JPanel(new BorderLayout());
    JTextArea captionTextArea = new JTextArea(post.getCaption());
    captionTextArea.setEditable(false);
    JLabel likesLabel = new JLabel("Likes: " + post.getLikesCount());
    bottomPanel.add(captionTextArea, BorderLayout.CENTER);
    bottomPanel.add(likesLabel, BorderLayout.SOUTH);

    // Add all components to main panel
    add(compositeTopPanel, BorderLayout.NORTH);
    add(imageLabel, BorderLayout.CENTER);
    add(bottomPanel, BorderLayout.SOUTH);
  }

  public JButton getBackButton() {
    return backButton;
  }
}
