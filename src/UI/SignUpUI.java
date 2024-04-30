package UI;

import Database.DatabaseUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SignUpUI extends BaseUI {

  private static final int WIDTH = 300;
  private static final int HEIGHT = 500;

  private JTextField usernameInput;
  private JTextField passwordInput;
  private JTextField bioInput;
  private boolean isProfilePictureUploaded = false;

  private final String profilePhotoStoragePath = "img/storage/profile/";

  public SignUpUI() {
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
    setLayout(new BorderLayout(10, 10));
    initializeUI();
  }

  @Override
  public void initializeUI() {
    add(createHeaderPanel(), BorderLayout.NORTH);
    add(createFieldsPanel(), BorderLayout.CENTER);
    add(createRegisterPanel(), BorderLayout.SOUTH);
  }

  private JPanel createHeaderPanel() {
    JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    headerPanel.setBackground(new Color(51, 51, 51));
    JLabel lblRegister = new JLabel("Quackstagram 🐥");
    lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
    lblRegister.setForeground(Color.WHITE);
    headerPanel.add(lblRegister);
    headerPanel.setPreferredSize(new Dimension(WIDTH, 40));
    return headerPanel;
  }

  private JPanel createFieldsPanel() {
    JPanel fieldsPanel = new JPanel();
    fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
    fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

    JLabel lblPhoto = new JLabel();
    lblPhoto.setPreferredSize(new Dimension(80, 80));
    lblPhoto.setHorizontalAlignment(JLabel.CENTER);
    lblPhoto.setVerticalAlignment(JLabel.CENTER);
    lblPhoto.setIcon(
        new ImageIcon(new ImageIcon("img/logos/DACS.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));

    JPanel photoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    photoPanel.add(lblPhoto);

    usernameInput = new JTextField("Username");
    passwordInput = new JTextField("Password");
    bioInput = new JTextField("Bio");

    bioInput.setForeground(Color.GRAY);
    usernameInput.setForeground(Color.GRAY);
    passwordInput.setForeground(Color.GRAY);

    fieldsPanel.add(Box.createVerticalStrut(10));
    fieldsPanel.add(photoPanel);
    fieldsPanel.add(Box.createVerticalStrut(10));
    fieldsPanel.add(usernameInput);
    fieldsPanel.add(Box.createVerticalStrut(10));
    fieldsPanel.add(passwordInput);
    fieldsPanel.add(Box.createVerticalStrut(10));
    fieldsPanel.add(bioInput);

    JButton btnUploadPhoto = new JButton("Upload Photo");
    btnUploadPhoto.addActionListener(this::handleProfilePictureUpload);

    JPanel photoUploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    photoUploadPanel.add(btnUploadPhoto);
    fieldsPanel.add(photoUploadPanel);

    return fieldsPanel;
  }

  private JPanel createRegisterPanel() {
    JButton btnRegister = new JButton("Register");
    btnRegister.addActionListener(this::onRegisterClicked);
    btnRegister.setBackground(new Color(255, 90, 95));
    btnRegister.setForeground(Color.BLACK);
    btnRegister.setFocusPainted(false);
    btnRegister.setBorderPainted(false);
    btnRegister.setFont(new Font("Arial", Font.BOLD, 14));

    JPanel registerPanel = new JPanel(new BorderLayout());
    registerPanel.setBackground(Color.WHITE);
    registerPanel.add(btnRegister, BorderLayout.CENTER);

    JButton btnSignIn = new JButton("Already have an account? Sign In");
    btnSignIn.addActionListener(this::onSignInClicked);
    registerPanel.add(btnSignIn, BorderLayout.SOUTH);

    return registerPanel;
  }

  private void onRegisterClicked(ActionEvent event) {
    String username = usernameInput.getText().trim();
    String password = passwordInput.getText().trim();
    String bio = bioInput.getText().trim();

    if (username.isEmpty() || password.isEmpty() || !isProfilePictureUploaded) {
      JOptionPane.showMessageDialog(this, "Please ensure all fields are filled and a photo is selected.", "Registration Incomplete", JOptionPane.ERROR_MESSAGE);
      return;
    }

    if (DatabaseUtils.usernameExists(username)) {
      JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different username.", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    DatabaseUtils.registerUser(username, password, bio);
    JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
    MainFrame.getInstance().switchPanel("Profile");
  }

  // Method to handle profile picture upload
  private void handleProfilePictureUpload(ActionEvent e) {
    JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
    fileChooser.setFileFilter(filter);
    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      saveProfilePicture(selectedFile, usernameInput.getText());
      isProfilePictureUploaded = true;
    }
  }

  private void saveProfilePicture(File file, String username) {
    try {
      BufferedImage image = ImageIO.read(file);
      File outputFile = new File(profilePhotoStoragePath + username + ".png");
      ImageIO.write(image, "png", outputFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void onSignInClicked(ActionEvent e) {
    MainFrame.getInstance().switchPanel("SignIn");
  }

}
