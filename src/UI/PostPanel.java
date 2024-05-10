package UI;

import Database.DAO.LikesDAO;
import Database.DAO.CommentsDAO;
import Logic.Comment;
import Logic.Post;
import Logic.SessionManager;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PostPanel extends JPanel {
  private final Post post;
  private final int pictureSize;
  private JButton backButton; // For navigation
  private JLabel likesLabel; // For displaying the likes count
  private JButton likeButton; // For liking the post
  private JPanel commentsPanel;

  public PostPanel(Post post, int pictureSize) {
    this.post = post;
    this.pictureSize = pictureSize;
    initializeUI();
  }

  private void initializeUI() {
    setLayout(new BorderLayout());
    add(createNavPanel(), BorderLayout.NORTH);
    add(createScrollableContent(), BorderLayout.CENTER);
  }

  private JScrollPane createScrollableContent() {
    JPanel scrollPanel = new JPanel();
    scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
    scrollPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    scrollPanel.add(createTopPanel());
    scrollPanel.add(createImagePanel());
    scrollPanel.add(createLikePanel());
    scrollPanel.add(createCaptionPanel());
    commentsPanel = createCommentsPanel();
    scrollPanel.add(commentsPanel);
    JScrollPane scrollPane = new JScrollPane(scrollPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    return scrollPane;
  }

  private JPanel createNavPanel() {
    JPanel navPanel = new JPanel(new BorderLayout());
    backButton = new JButton("Back");
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

    JScrollPane captionScrollPane = new JScrollPane(captionTextArea);
    captionScrollPane.setBorder(null); // Optional: remove border
    captionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    captionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    captionPanel.add(captionScrollPane, BorderLayout.CENTER);
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
    commentField.setPreferredSize(new Dimension(commentField.getPreferredSize().width, 20)); // Ustawienie wysokości na 30 pikseli
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
    String result = LikesDAO.likePost(post.getId(), SessionManager.getCurrentUser().getId());
    JOptionPane.showMessageDialog(this, result, "Like Status", JOptionPane.INFORMATION_MESSAGE);
    if (result.equals("Post successfully liked!")) {
      int newLikesCount = post.getLikesCount() + 1;
      likesLabel.setText("Likes: " + newLikesCount);
      post.setLikesCount(newLikesCount);
    }
  }

  private void handleCommentAction(String commentContent){
    boolean result = CommentsDAO.postComment(post.getId(), SessionManager.getCurrentUser().getId(), commentContent);
    if (result) {
      post.reloadComments(); // Refresh the comments list

      remove(commentsPanel); // Remove the old comments panel

      commentsPanel = createCommentsPanel(); // Refresh the comments panel
      add(commentsPanel, BorderLayout.SOUTH); // Add the new comments panel

      revalidate();
      repaint();
    }
  }

  public JButton getBackButton() {
    return backButton;
  }
}
