package UI;

import Database.DatabaseUtils;
import Logic.SessionManager;
import Logic.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SignInUI extends BaseUI {

  private static final int WIDTH = 300;
  private static final int HEIGHT = 500;

  private JTextField usernameInput;
  private JTextField passwordInput;

  public SignInUI() {
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
    initializeUI();
  }

  @Override
  protected void initializeUI() {
    add(createHeaderPanel(), BorderLayout.NORTH);
    add(createFieldsPanel(), BorderLayout.CENTER);
    add(createButtonPanel(), BorderLayout.SOUTH);
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

    JLabel lblPhoto = new JLabel(
        new ImageIcon(new ImageIcon("img/logos/DACS.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
    lblPhoto.setPreferredSize(new Dimension(80, 80));
    lblPhoto.setHorizontalAlignment(JLabel.CENTER);
    lblPhoto.setVerticalAlignment(JLabel.CENTER);

    JPanel photoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    photoPanel.add(lblPhoto);

    usernameInput = new JTextField("Username");
    passwordInput = new JTextField("Password");
    usernameInput.setForeground(Color.GRAY);
    passwordInput.setForeground(Color.GRAY);

    fieldsPanel.add(Box.createVerticalStrut(10));
    fieldsPanel.add(photoPanel);
    fieldsPanel.add(Box.createVerticalStrut(10));
    fieldsPanel.add(usernameInput);
    fieldsPanel.add(Box.createVerticalStrut(10));
    fieldsPanel.add(passwordInput);
    fieldsPanel.add(Box.createVerticalStrut(10));

    return fieldsPanel;
  }

  private JPanel createButtonPanel() {
    JButton btnSignIn = new JButton("Sign-In");
    btnSignIn.addActionListener(this::onSignInClicked);
    btnSignIn.setBackground(new Color(255, 90, 95));
    btnSignIn.setForeground(Color.BLACK);
    btnSignIn.setFocusPainted(false);
    btnSignIn.setBorderPainted(false);
    btnSignIn.setFont(new Font("Arial", Font.BOLD, 14));

    JButton btnRegisterNow = new JButton("No Account? Register Now");
    btnRegisterNow.addActionListener(this::onRegisterNowClicked);
    btnRegisterNow.setBackground(Color.WHITE);
    btnRegisterNow.setForeground(Color.BLACK);
    btnRegisterNow.setFocusPainted(false);
    btnRegisterNow.setBorderPainted(false);

    JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
    buttonPanel.setBackground(Color.white);
    buttonPanel.add(btnSignIn);
    buttonPanel.add(btnRegisterNow);

    return buttonPanel;
  }

  private void onSignInClicked(ActionEvent event) {
    String enteredUsername = usernameInput.getText().trim();
    String enteredPassword = passwordInput.getText().trim();

    try {
      Integer userId = DatabaseUtils.verifyCredentials(enteredUsername, enteredPassword);
      if (userId != null) {
        JOptionPane.showMessageDialog(null, "Login successful", "Success", JOptionPane.INFORMATION_MESSAGE);
        User loggedInUser = new User(userId);
        saveCurrentUserInformation(loggedInUser);
        MainFrame.getInstance().switchPanel("Profile");
      } else {
        JOptionPane.showMessageDialog(null, "Incorrect username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        usernameInput.requestFocus();
      }
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, "An error occurred while attempting to log in. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void onRegisterNowClicked(ActionEvent event) {
    MainFrame.getInstance().switchPanel("SignUp");
  }


  private void saveCurrentUserInformation(User user) {
    SessionManager.setCurrentUser(user);
  }

}
