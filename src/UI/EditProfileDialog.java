package UI;

import javax.swing.*;
import java.awt.*;

public class EditProfileDialog extends JDialog {
    Frame owner;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JTextField bioField;

    public EditProfileDialog(Frame owner) {
        super(owner, "Edit Profile", true);
        this.owner = owner;
        initializeUI();
    }

    private void initializeUI() {
        setSize(300, 300);
        setLayout(new BorderLayout()); // Change layout to BorderLayout

        // Main content panel with GridLayout
        JPanel contentPanel = new JPanel(new GridLayout(4, 1));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding around all content

        // Name field
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.add(new JLabel("Name:"), BorderLayout.WEST);
        nameField = new JTextField();
        namePanel.add(nameField, BorderLayout.CENTER);
        contentPanel.add(namePanel);

        // Password field
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.add(new JLabel("Password:"), BorderLayout.WEST);
        passwordField = new JPasswordField();
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        contentPanel.add(passwordPanel);

        // Bio field
        JPanel bioPanel = new JPanel(new BorderLayout());
        bioPanel.add(new JLabel("Bio:"), BorderLayout.WEST);
        bioField = new JTextField();
        bioPanel.add(bioField, BorderLayout.CENTER);
        contentPanel.add(bioPanel);

        // Buttons for actions
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveProfile());
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        contentPanel.add(buttonPanel);

        // Add the main content panel to the dialog
        add(contentPanel, BorderLayout.CENTER);

        setLocationRelativeTo(getOwner());
    }


    private void saveProfile() {
        String name = nameField.getText();
        String password = new String(passwordField.getPassword());
        String bio = bioField.getText();

        try {
//            DatabaseUtils.updateProfile(name, password, bio);
            JOptionPane.showMessageDialog(this, "Profile updated successfully!");
            dispose(); // Close the dialog
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to update profile: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
