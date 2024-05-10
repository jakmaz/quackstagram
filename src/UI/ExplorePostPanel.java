package UI;

import Logic.Post;
import Logic.SessionManager;

import javax.swing.*;

import Database.DAO.LikesDAO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.imageio.ImageIO;

public class ExplorePostPanel extends JPanel {
  private Post post;
  private JButton backButton; // For navigation
  private JLabel likesLabel; // For displaying the likes count
  private JButton likeButton; // For liking the post

  public ExplorePostPanel(Post post) {
    this.post = post;
    initializeUI();
  }

  private void initializeUI() {
    setLayout(new BorderLayout());
    add(createMainPanel(), BorderLayout.CENTER);
  }

  private JPanel createMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    mainPanel.add(createNavPanel());
    mainPanel.add(createTopPanel());
    mainPanel.add(createImagePanel());
    mainPanel.add(createBottomPanel());

    return mainPanel;
  }

  private JPanel createNavPanel() {
    JPanel navPanel = new JPanel(new BorderLayout());
    backButton = new JButton("Back");
    backButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
    navPanel.add(backButton, BorderLayout.NORTH);
    return navPanel;
  }

  private JPanel createTopPanel() {
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    JButton usernameButton = new JButton(post.getUser().getUsername());
    usernameButton.addActionListener(e -> MainFrame.getInstance().switchToUserProfile(post.getUser()));
    LocalDateTime now = LocalDateTime.now();
    String timeSincePosting = ChronoUnit.DAYS.between(post.getTimestamp().toLocalDateTime(), now) + " days ago";
    JLabel timeLabel = new JLabel(timeSincePosting);
    topPanel.add(usernameButton);
    topPanel.add(timeLabel);
    return topPanel;
  }

  private JPanel createImagePanel() {
    JPanel imagePanel = new JPanel(new GridBagLayout());
    JLabel imageLabel = new JLabel();
    try {
      BufferedImage img = ImageIO.read(new File(post.getImagePath()));
      ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
      imageLabel.setIcon(imageIcon);
    } catch (IOException ex) {
      imageLabel.setText("Image not found");
      ex.printStackTrace();
    }
    imagePanel.add(imageLabel);
    return imagePanel;
  }

  private JPanel createBottomPanel() {
    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

    JLabel captionTextArea = new JLabel(post.getCaption());
    JScrollPane captionScroll = new JScrollPane(captionTextArea);
    captionScroll.setPreferredSize(new Dimension(Integer.MAX_VALUE, 60));

    // Create a panel to hold likes label and like button side by side
    JPanel likePanel = new JPanel();
    likePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0)); // Small horizontal gap, no vertical gap

    likesLabel = new JLabel("Likes: " + post.getLikesCount());
    likeButton = new JButton("Like");
    likeButton.addActionListener(e -> handleLikeAction());

    likePanel.add(likesLabel);
    likePanel.add(likeButton);

    bottomPanel.add(captionScroll);
    bottomPanel.add(likePanel); // Add the likePanel to the bottomPanel
    return bottomPanel;
  }

  private void handleLikeAction() {
    String result = LikesDAO.likePost(post.getId(), SessionManager.getCurrentUser().getId());
    JOptionPane.showMessageDialog(this, result, "Like Status", JOptionPane.INFORMATION_MESSAGE);

    // Optionally, update the likes label if liked successfully
    if (result.equals("Post successfully liked!")) {
      int newLikesCount = post.getLikesCount() + 1;
      likesLabel.setText("Likes: " + newLikesCount);
      post.setLikesCount(newLikesCount); // Update the post object too
    }
  }

  public JButton getBackButton() {
    return backButton;
  }
}
