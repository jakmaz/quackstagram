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

  private JTextField txtUsername;
  private JTextField txtPassword;
  private JButton btnSignIn, btnRegisterNow;
  private JLabel lblPhoto;
  private User newUser;

  public SignInUI() {
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
    initializeUI();
  }

  @Override
  protected void initializeUI() {
    // Header with the Register label
    JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    headerPanel.setBackground(new Color(51, 51, 51));
    JLabel lblRegister = new JLabel("Quackstagram 🐥");
    lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
    lblRegister.setForeground(Color.WHITE); // Set the text color to white
    headerPanel.add(lblRegister);
    headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height

    // Profile picture placeholder without border
    lblPhoto = new JLabel();
    lblPhoto.setPreferredSize(new Dimension(80, 80));
    lblPhoto.setHorizontalAlignment(JLabel.CENTER);
    lblPhoto.setVerticalAlignment(JLabel.CENTER);
    lblPhoto.setIcon(
        new ImageIcon(new ImageIcon("img/logos/DACS.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
    JPanel photoPanel = new JPanel(); // Use a panel to center the photo label
    photoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    photoPanel.add(lblPhoto);

    // Text fields panel
    JPanel fieldsPanel = new JPanel();
    fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
    fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

    txtUsername = new JTextField("Username");
    txtUsername = new JTextField("Zara");
    txtPassword = new JTextField("Password");
    txtUsername.setForeground(Color.GRAY);
    txtPassword.setForeground(Color.GRAY);

    fieldsPanel.add(Box.createVerticalStrut(10));
    fieldsPanel.add(photoPanel);
    fieldsPanel.add(Box.createVerticalStrut(10));
    fieldsPanel.add(txtUsername);
    fieldsPanel.add(Box.createVerticalStrut(10));
    fieldsPanel.add(txtPassword);
    fieldsPanel.add(Box.createVerticalStrut(10));

    // Register button with black text
    btnSignIn = new JButton("Sign-In");
    btnSignIn.addActionListener(this::onSignInClicked);
    btnSignIn.setBackground(new Color(255, 90, 95)); // Use a red color that matches the mockup
    btnSignIn.setForeground(Color.BLACK); // Set the text color to black
    btnSignIn.setFocusPainted(false);
    btnSignIn.setBorderPainted(false);
    btnSignIn.setFont(new Font("Arial", Font.BOLD, 14));
    JPanel registerPanel = new JPanel(new BorderLayout()); // Panel to contain the register button
    registerPanel.setBackground(Color.WHITE); // Background for the panel
    registerPanel.add(btnSignIn, BorderLayout.CENTER);

    // Adding components to the frame
    add(headerPanel, BorderLayout.NORTH);
    add(fieldsPanel, BorderLayout.CENTER);
    add(registerPanel, BorderLayout.SOUTH);

    // New button for navigating to SignUpUI
    btnRegisterNow = new JButton("No Account? Register Now");
    btnRegisterNow.addActionListener(this::onRegisterNowClicked);
    btnRegisterNow.setBackground(Color.WHITE); // Set a different color for distinction
    btnRegisterNow.setForeground(Color.BLACK);
    btnRegisterNow.setFocusPainted(false);
    btnRegisterNow.setBorderPainted(false);

    // Panel to hold both buttons
    JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10)); // Grid layout with 1 row, 2 columns
    buttonPanel.setBackground(Color.white);
    buttonPanel.add(btnSignIn);
    buttonPanel.add(btnRegisterNow);

    // Adding the button panel to the frame
    add(buttonPanel, BorderLayout.SOUTH);

  }

  private void onSignInClicked(ActionEvent event) {
    String enteredUsername = txtUsername.getText();
    String enteredPassword = txtPassword.getText();

    try {
      if (verifyCredentials(enteredUsername, enteredPassword)) {
        System.out.println("Login successful");
        // Signal MainFrame to switch to the InstagramProfileUI
        MainFrame.getInstance().switchPanel("InstagramProfile");
      } else {
        // Display an error message directly in the GUI
        JOptionPane.showMessageDialog(null, "Incorrect username or password.", "Login Failed",
            JOptionPane.ERROR_MESSAGE);
        // txtUsername.setText(""); // Optionally clear the username field
        // txtPassword.setText(""); // Clear the password field
        // txtUsername.requestFocus(); // Set focus back to the username field
      }
    } catch (Exception e) {
      e.printStackTrace();
      // Display a generic error message for any other unexpected errors
      JOptionPane.showMessageDialog(null, "An error occurred while attempting to log in. Please try again.",
          "Login Error", JOptionPane.ERROR_MESSAGE);
      // Consider logging this error to a file or system log for maintenance purposes
    }
  }

  private void onRegisterNowClicked(ActionEvent event) {
    System.out.println("Register Now clicked");
    // Signal MainFrame to switch to the SignUpUI
    MainFrame.getInstance().switchPanel("SignUp"); // Assuming "SignUpUI" is registered in the MainFrame
  }

  private boolean verifyCredentials(String username, String password) {
    try (BufferedReader reader = new BufferedReader(new FileReader("data/credentials.txt"))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] credentials = line.split(":");
        if (credentials[0].equals(username) && credentials[1].equals(password)) {
          String bio = credentials[2];
          // Create User object and save information
          newUser = new User(username, bio, password); // Assuming User constructor takes these parameters
          saveUserInformation(newUser);

          return true;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  private void saveUserInformation(User user) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/users.txt", false))) {
      writer.write(user.toString()); // Implement a suitable toString method in User class
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getTitle() {
    return "Sign-In";
  }
}
