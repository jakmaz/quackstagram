package UI;

import Database.DAO.LikesDAO;
import Database.DAO.CommentsDAO;
import Logic.Comment;
import Logic.Post;
import Logic.SessionManager;
import Logic.TimeElapsedCalculator;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.time.LocalDateTime;

public class PostPanel extends JPanel {
  private final Post post;
  private final int pictureSize;
  private final boolean displayBackButton;
  private JButton backButton; // For navigation
  private JLabel likesLabel; // For displaying the likes count
  private JButton likeButton; // For liking the post
  private JPanel commentsPanel;
  private JPanel scrollPanel;

  public PostPanel(Post post, int pictureSize, boolean displayBackButton) {
    this.post = post;
    this.pictureSize = pictureSize;
    this.displayBackButton = displayBackButton;
    initializeUI();
  }

  private void initializeUI() {
    setLayout(new BorderLayout());
    add(createNavPanel(), BorderLayout.NORTH);
    add(createScrollableContent(), BorderLayout.CENTER);
  }

  private JPanel createScrollableContent() {
    scrollPanel = new JPanel();
    scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
    scrollPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    scrollPanel.add(createTopPanel());
    scrollPanel.add(createImagePanel());
    scrollPanel.add(createLikePanel());
    scrollPanel.add(createCaptionPanel());
    commentsPanel = createCommentsPanel();
    scrollPanel.add(commentsPanel);

    return scrollPanel;
  }

  private JPanel createNavPanel() {
    JPanel navPanel = new JPanel(new BorderLayout());
    if (displayBackButton) {
      backButton = new JButton("Back");
      navPanel.add(backButton, BorderLayout.CENTER); // This will make the button take the full width
    }
    return navPanel;
  }

  private JPanel createTopPanel() {
    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

    JButton ownerBtn = new JButton(post.getUser().getUsername());
    ownerBtn.setBorderPainted(false);
    ownerBtn.setOpaque(true);
    ownerBtn.addActionListener(e -> MainFrame.getInstance().showOtherProfilePanel(post.getUser()));

    LocalDateTime postTimestamp = post.getTimestamp().toLocalDateTime();
    String timeSincePosting = TimeElapsedCalculator.getElapsedTime(postTimestamp);

    JLabel timeLabel = new JLabel(timeSincePosting);
    topPanel.add(ownerBtn, BorderLayout.WEST);
    topPanel.add(timeLabel, BorderLayout.EAST);
    return topPanel;
  }

  private JPanel createImagePanel() {
    JPanel imagePanel = new JPanel(new GridBagLayout());
    JLabel imageLabel = new JLabel();
    try {
      BufferedImage img = ImageIO.read(new File(post.getImagePath()));
      ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(pictureSize, pictureSize, Image.SCALE_SMOOTH));
      imageLabel.setIcon(imageIcon);
    } catch (IOException ex) {
      imageLabel.setText("Image not found");
      ex.printStackTrace();
    }
    imagePanel.add(imageLabel);
    return imagePanel;
  }

  private JPanel createLikePanel() {
    JPanel likePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    likesLabel = new JLabel("Likes: " + post.getLikesCount());
    likeButton = new JButton("Like");
    likeButton.addActionListener(e -> handleLikeAction());
    likePanel.add(likesLabel);
    likePanel.add(likeButton);
    return likePanel;
  }

  private JPanel createCaptionPanel() {
    JPanel captionPanel = new JPanel(new BorderLayout());
    JTextArea captionTextArea = new JTextArea(post.getCaption());
    captionTextArea.setWrapStyleWord(true);
    captionTextArea.setLineWrap(true);
    captionTextArea.setEditable(false);
    captionTextArea.setBorder(null); // Remove border if not desired

    captionPanel.add(captionTextArea, BorderLayout.CENTER);
    return captionPanel;
  }

  private JPanel createCommentsPanel() {
    JPanel commentsPanel = new JPanel();
    commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));
    commentsPanel.setBorder(BorderFactory.createTitledBorder("Comments"));
    for (Comment comment : post.getComments()) {
      JPanel singleCommentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      JLabel commentLabel = new JLabel(
          "<html><b>" + comment.getUser().getUsername() + ":</b> " + comment.getText() + "</html>");
      singleCommentPanel.add(commentLabel);
      commentsPanel.add(singleCommentPanel);
    }
    // Input panel for new comment
    BufferedImage icon = null;
    try {
      icon = ImageIO.read(new File("img/icons/submit.png")); // Ensure the path is correct
    } catch (IOException e) {
      e.printStackTrace();
    }
    JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JTextField commentField = new JTextField(15); // Adjustable size
    commentField.setPreferredSize(new Dimension(commentField.getPreferredSize().width, 20)); // Ustawienie wysokości na
                                                                                             // 30 pikseli
    ImageIcon scaledIcon = new ImageIcon(icon.getScaledInstance(20, 20, Image.SCALE_AREA_AVERAGING));
    JButton submitButton = new JButton(scaledIcon);
    submitButton.setBorderPainted(false);
    submitButton.setContentAreaFilled(false);
    submitButton.setFocusPainted(false);
    submitButton.setOpaque(false);

    submitButton.addActionListener(e -> {
      handleCommentAction(commentField.getText());
      commentField.setText(""); // Clear the field after submission
    });

    inputPanel.add(commentField);
    inputPanel.add(submitButton);

    // Add the input panel to the main comments panel
    commentsPanel.add(inputPanel);

    return commentsPanel;
  }

  private void handleLikeAction() {
    boolean isLiked = LikesDAO.checkLike(post.getId(), SessionManager.getCurrentUser().getId());
    if (isLiked) {
      if (LikesDAO.unlikePost(post.getId(), SessionManager.getCurrentUser().getId())) {
        int newLikesCount = post.getLikesCount() - 1;
        likesLabel.setText("Likes: " + newLikesCount);
        post.setLikesCount(newLikesCount);
      }
    } else {
      if (LikesDAO.likePost(post.getId(), SessionManager.getCurrentUser().getId())) {
        int newLikesCount = post.getLikesCount() + 1;
        likesLabel.setText("Likes: " + newLikesCount);
        post.setLikesCount(newLikesCount);
      }
    }
  }

  private void handleCommentAction(String commentContent) {
    boolean result = CommentsDAO.postComment(post.getId(), SessionManager.getCurrentUser().getId(), commentContent);
    if (result) {
      post.reloadComments(); // Refresh the comments list

      scrollPanel.remove(commentsPanel); // Remove the old comments panel

      commentsPanel = createCommentsPanel(); // Refresh the comments panel
      scrollPanel.add(commentsPanel, BorderLayout.SOUTH); // Add the new comments panel

      revalidate();
      repaint();
    }
  }

  public JButton getBackButton() {
    return backButton;
  }
}
