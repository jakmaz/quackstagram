package UI;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

import Database.DAO.CommentsDAO;
import Database.DAO.LikesDAO;
import Logic.Comment;
import Logic.Post;
import Logic.SessionManager;
import Logic.TimeElapsedCalculator;

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


    JButton ownerBtn = getOwnerBtn();
    LocalDateTime postTimestamp = post.getTimestamp().toLocalDateTime();
    String timeSincePosting = TimeElapsedCalculator.getElapsedTime(postTimestamp);

    JLabel timeLabel = new JLabel(timeSincePosting);
    topPanel.add(ownerBtn, BorderLayout.WEST);
    topPanel.add(timeLabel, BorderLayout.EAST);
    return topPanel;
  }

  private JButton getOwnerBtn() {
    ImageIcon avatarIcon = new ImageIcon(post.getUser().getProfilePicturePath());
    Image roundedAvatarIcon = makeRoundedImage(avatarIcon.getImage());
    ImageIcon scaledRoundedAvatar = new ImageIcon(roundedAvatarIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH));

    JButton ownerBtn = new JButton(post.getUser().getUsername(), scaledRoundedAvatar);
    ownerBtn.setBorderPainted(false);
    ownerBtn.setOpaque(false);
    ownerBtn.setContentAreaFilled(false);
    ownerBtn.setFocusPainted(false);

    // Adding padding around the image inside the button
    ownerBtn.setMargin(new Insets(10, 10, 10, 10)); // Adds 10 pixels of padding on all sides

    ownerBtn.addActionListener(e -> {
      if (post.getUser().equals(SessionManager.getCurrentUser())) {
        MainFrame.getInstance().showProfilePanel();
      } else {
        MainFrame.getInstance().showOtherProfilePanel(post.getUser());
      }
    });
    return ownerBtn;
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
    JPanel likePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    boolean isLiked = LikesDAO.checkLike(post.getId(), SessionManager.getCurrentUser().getId());
    likesLabel = new JLabel("Likes: " + post.getLikesCount());
    likeButton = new JButton(isLiked ? "Unlike" : "Like");
    likeButton.addActionListener(e -> handleLikeAction(likeButton));
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
    commentField.setPreferredSize(new Dimension(commentField.getPreferredSize().width, 20));

    ImageIcon scaledIcon = new ImageIcon(icon.getScaledInstance(15, 15, Image.SCALE_SMOOTH));
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

  private void handleLikeAction(JButton likeButton) {
    boolean isLiked = LikesDAO.checkLike(post.getId(), SessionManager.getCurrentUser().getId());
    updatePostLikes(isLiked, likeButton);
    likeButton.revalidate();
    likeButton.repaint();
  }

  private void updatePostLikes(boolean isLiked, JButton likeButton) {
    if (isLiked && LikesDAO.unlikePost(post.getId(), SessionManager.getCurrentUser().getId())) {
      updateLikesCount(-1, "Like", likeButton);
    } else if (!isLiked && LikesDAO.likePost(post.getId(), SessionManager.getCurrentUser().getId())) {
      updateLikesCount(1, "Unlike", likeButton);
    }
  }

  private void updateLikesCount(int delta, String buttonText, JButton likeButton) {
    int newLikesCount = post.getLikesCount() + delta;
    likesLabel.setText("Likes: " + newLikesCount);
    post.setLikesCount(newLikesCount);
    likeButton.setText(buttonText);
  }

  private void handleCommentAction(String commentContent) {
    if(commentContent.isEmpty()) {
      return;
    }
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

  private Image makeRoundedImage(Image img) {
    int width = img.getWidth(null);
    int height = img.getHeight(null);
    BufferedImage circleBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = circleBuffer.createGraphics();

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    Ellipse2D shape = new Ellipse2D.Float(0, 0, width, height);
    g2.setClip(shape);

    g2.drawImage(img, 0, 0, width, height, null);

    g2.setClip(null);
    g2.setStroke(new BasicStroke(50));
    g2.setColor(Color.BLACK);
    g2.draw(shape);

    g2.dispose();

    return circleBuffer;
  }
}
