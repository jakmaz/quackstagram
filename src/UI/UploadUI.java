package UI;

import Logic.SessionManager;
import Logic.FileUploadHandler;

import javax.swing.*;

import Database.DAO.PostDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Path;

public class UploadUI extends BaseUI {

  private static final int WIDTH = 300;
  private static final int HEIGHT = 500;
  private JLabel imagePreviewLabel;
  private JTextArea bioTextArea;
  private JButton chooseImageButton;
  private JButton uploadPostButton;

  private final FileUploadHandler fileHandler;
  private Path selectedImagePath;

  public UploadUI() {
    this.fileHandler = new FileUploadHandler("img/storage/posts");

    setSize(WIDTH, HEIGHT);
    setMinimumSize(new Dimension(WIDTH, HEIGHT));
    setLayout(new BorderLayout());
    initializeUI();
  }

  public void initializeUI() {
    JPanel headerPanel = createHeaderPanel();
    JPanel navigationPanel = createNavigationPanel();
    JPanel contentPanel = createContentPanel();

    add(headerPanel, BorderLayout.NORTH);
    add(contentPanel, BorderLayout.CENTER);
    add(navigationPanel, BorderLayout.SOUTH);
  }

  private JPanel createContentPanel() {
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

    // Image preview label
    imagePreviewLabel = new JLabel(new ImageIcon());
    imagePreviewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    imagePreviewLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT / 3));
    contentPanel.add(imagePreviewLabel);

    // Choose image button
    chooseImageButton = new JButton("Choose Image");
    chooseImageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    chooseImageButton.addActionListener(this::chooseImageAction);
    contentPanel.add(chooseImageButton);

    // Bio text area setup
    bioTextArea = new JTextArea("Enter a caption");
    bioTextArea.setLineWrap(true);
    bioTextArea.setWrapStyleWord(true);
    JScrollPane bioScrollPane = new JScrollPane(bioTextArea);
    bioScrollPane.setPreferredSize(new Dimension(WIDTH - 50, 60)); // Reduced height for bio input
    contentPanel.add(bioScrollPane);

    // Upload post button
    uploadPostButton = new JButton("Upload Post");
    uploadPostButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    uploadPostButton.addActionListener(this::uploadPostAction);
    contentPanel.add(uploadPostButton);

    return contentPanel;
  }

  private void chooseImageAction(ActionEvent event) {
    JFileChooser fileChooser = fileHandler.createImageFileChooser();
    int returnValue = fileChooser.showOpenDialog(null);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      selectedImagePath = fileChooser.getSelectedFile().toPath();
      ImageIcon icon = new ImageIcon(selectedImagePath.toString());
      Image image = icon.getImage().getScaledInstance(imagePreviewLabel.getWidth(), imagePreviewLabel.getHeight(),
          Image.SCALE_SMOOTH);
      imagePreviewLabel.setIcon(new ImageIcon(image));
    }
  }

  private void uploadPostAction(ActionEvent event) {
    if (selectedImagePath != null && bioTextArea.getText().length() > 0) {
      try {
        int userId = SessionManager.getCurrentUser().getId();
        String caption = bioTextArea.getText();
        String storedImagePath = fileHandler.saveFileToDisk(userId, selectedImagePath);
        PostDAO.postSomething(userId, caption, storedImagePath);
        JOptionPane.showMessageDialog(this, "Post uploaded successfully!");
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error uploading post: " + ex.getMessage(), "Error",
            JOptionPane.ERROR_MESSAGE);
      }
    } else {
      JOptionPane.showMessageDialog(this, "Please select an image and write a caption before uploading.", "Warning",
          JOptionPane.WARNING_MESSAGE);
    }
  }

  private JPanel createHeaderPanel() {
    JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    headerPanel.setBackground(new Color(51, 51, 51));
    JLabel lblRegister = new JLabel("Upload Image 🐥");
    lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
    lblRegister.setForeground(Color.WHITE);
    headerPanel.add(lblRegister);
    headerPanel.setPreferredSize(new Dimension(WIDTH, 40));
    return headerPanel;
  }
}
