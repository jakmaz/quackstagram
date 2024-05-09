package UI;

import Logic.Comment;
import Logic.Post;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class PostPanel extends JPanel {
  private Post post;
  private JLabel imageLabel;
  private JLabel dateLabel;
  private JLabel likesLabel;
  private JTextArea commentsArea;
  private JButton backButton;

  public PostPanel(Post post) {
    this.post = post;
    initializeUI();
  }

  private void initializeUI() {
    setLayout(new BorderLayout());

    // Image display
    ImageIcon imageIcon = new ImageIcon(new ImageIcon(post.getImagePath()).getImage().getScaledInstance(100,
        100, Image.SCALE_SMOOTH));
    imageLabel = new JLabel(imageIcon);
    imageLabel.setHorizontalAlignment(JLabel.CENTER);
    add(imageLabel, BorderLayout.CENTER);

    // Info panel at the bottom
    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

    // Date
    dateLabel = new JLabel("Date: " + formatDate(post.getTimestamp()));
    infoPanel.add(dateLabel);

    // Likes
    likesLabel = new JLabel("Likes: " + post.getLikesCount());
    infoPanel.add(likesLabel);

    // Comments
    commentsArea = new JTextArea(5, 20);
    commentsArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(commentsArea);
    infoPanel.add(scrollPane);
    displayComments(post.getComments());

    add(infoPanel, BorderLayout.SOUTH);

    // Back button
    backButton = new JButton("Back");
    add(backButton, BorderLayout.NORTH);
  }

  private String formatDate(Timestamp timestamp) {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
  }

  private void displayComments(List<Comment> comments) {
    StringBuilder commentsText = new StringBuilder();
    for (Comment comment : comments) {
      commentsText.append(comment.getUser().getUsername()).append(": ").append(comment.getText()).append("\n");
    }
    commentsArea.setText(commentsText.toString());
  }

  public JButton getBackButton() {
    return backButton;
  }
}
