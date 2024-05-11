package UI;

import javax.swing.*;

import Database.DAO.UserDAO;
import Logic.SessionManager;
import Logic.User;

import java.awt.*;

public class EditProfileDialog extends JDialog {
  Frame owner;
  private JTextField nameField;
  private JPasswordField passwordField;
  private JTextField bioField;
  private User currentUser;

  public EditProfileDialog(Frame owner) {
    super(owner, "Edit Profile", true);
    this.owner = owner;
    this.currentUser = SessionManager.getCurrentUser(); // Assuming this method returns the currently logged in user
    initializeUI();
    setDefaultValues(); // Set the default values after initializing the UI
  }

  private void initializeUI() {
    setSize(300, 300);
    setLayout(new BorderLayout());
    add(createContentPanel(), BorderLayout.CENTER);
    setLocationRelativeTo(getOwner());
  }

  private JPanel createContentPanel() {
    JPanel contentPanel = new JPanel(new GridLayout(4, 1));
    contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    contentPanel.add(createNamePanel());
    contentPanel.add(createPasswordPanel());
    contentPanel.add(createBioPanel());
    contentPanel.add(createButtonPanel());
    return contentPanel;
  }

  private JPanel createNamePanel() {
    JPanel namePanel = new JPanel(new BorderLayout());
    namePanel.add(new JLabel("Name:"), BorderLayout.WEST);
    nameField = new JTextField();
    namePanel.add(nameField, BorderLayout.CENTER);
    return namePanel;
  }

  private JPanel createPasswordPanel() {
    JPanel passwordPanel = new JPanel(new BorderLayout());
    passwordPanel.add(new JLabel("Password:"), BorderLayout.WEST);
    passwordField = new JPasswordField();
    passwordPanel.add(passwordField, BorderLayout.CENTER);
    return passwordPanel;
  }

  private JPanel createBioPanel() {
    JPanel bioPanel = new JPanel(new BorderLayout());
    bioPanel.add(new JLabel("Bio:"), BorderLayout.WEST);
    bioField = new JTextField();
    bioPanel.add(bioField, BorderLayout.CENTER);
    return bioPanel;
  }

  private JPanel createButtonPanel() {
    JPanel buttonPanel = new JPanel();
    JButton saveButton = new JButton("Save");
    saveButton.addActionListener(e -> saveProfile());
    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(e -> dispose());
    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);
    return buttonPanel;
  }

  private void setDefaultValues() {
    if (currentUser != null) {
      nameField.setText(currentUser.getUsername());
      passwordField.setText(""); // Passwords are not retrieved for security
      bioField.setText(currentUser.getBio());
    }
  }

  private void saveProfile() {
    String name = nameField.getText();
    String password = new String(passwordField.getPassword());
    String bio = bioField.getText();

    try {
      UserDAO.updateProfile(currentUser.getId(), name, password, bio);
      JOptionPane.showMessageDialog(this, "Profile updated successfully!");
      dispose(); // Close the dialog
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Failed to update profile: " + e.getMessage(), "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }
}
