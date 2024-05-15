package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

import Database.DAO.UserDAO;
import Logic.SessionManager;
import Logic.User;

public class SignInUI extends BaseUI {
  private JTextField usernameField;
  private JPasswordField passwordField;
  private JButton btnSignIn;

  public SignInUI() {
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
    initializeUI();
  }

  @Override
  protected void initializeUI() {

    JPanel headerPanel = createHeaderPanel("Sign In");
    JPanel fieldsPanel = createFieldsPanel();
    JPanel buttonPanel = createButtonPanel();

    setLayout(new BorderLayout());
    add(headerPanel, BorderLayout.NORTH);
    add(fieldsPanel, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);

    addKeymap();
  }

  private void addKeymap() {
    // Setup key binding for Enter key on username and password input fields
    KeyStroke enterKeyStroke = KeyStroke.getKeyStroke("ENTER");
    Action performSignIn = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        onSignInClicked(e);
      }
    };

    // Assign the Enter key action to both text fields
    usernameField.getInputMap(JComponent.WHEN_FOCUSED).put(enterKeyStroke, "enterPressed");
    usernameField.getActionMap().put("enterPressed", performSignIn);

    passwordField.getInputMap(JComponent.WHEN_FOCUSED).put(enterKeyStroke, "enterPressed");
    passwordField.getActionMap().put("enterPressed", performSignIn);
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

    usernameField = new JTextField("admin");
    passwordField = new JPasswordField("admin");

    SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());

    fieldsPanel.add(Box.createVerticalStrut(20));
    fieldsPanel.add(photoPanel);
    fieldsPanel.add(Box.createVerticalStrut(30));
    fieldsPanel.add(usernameField);
    fieldsPanel.add(Box.createVerticalStrut(10));
    fieldsPanel.add(passwordField);
    fieldsPanel.add(Box.createVerticalStrut(50));

    return fieldsPanel;
  }

  private JPanel createButtonPanel() {
    // Create a JPanel with GridLayout
    JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
    buttonPanel.setBackground(Color.white);

    // Create padding border: top, left, bottom, right
    Border padding = BorderFactory.createEmptyBorder(10, 20, 10, 20);
    buttonPanel.setBorder(padding);

    // Create and configure the Sign-In button
    btnSignIn = new JButton("Sign In");
    btnSignIn.addActionListener(this::onSignInClicked);
    btnSignIn.setFocusPainted(false);
    btnSignIn.setBorderPainted(false);
    btnSignIn.setFont(new Font("Arial", Font.BOLD, 14));

    // Create and configure the Register button
    JButton btnRegisterNow = new JButton("No Account? Register Now");
    btnRegisterNow.addActionListener(this::onRegisterNowClicked);
    btnRegisterNow.setBackground(Color.WHITE);
    btnRegisterNow.setForeground(Color.BLACK);
    btnRegisterNow.setFocusPainted(false);
    btnRegisterNow.setBorderPainted(false);

    // Add buttons to the panel
    buttonPanel.add(btnSignIn);
    buttonPanel.add(btnRegisterNow);

    return buttonPanel;
  }

  private void onSignInClicked(ActionEvent event) {
    String enteredUsername = usernameField.getText().trim();
    String enteredPassword = passwordField.getText().trim();

    try {
      Integer userId = UserDAO.verifyCredentials(enteredUsername, enteredPassword);
      if (userId != null) {
        User loggedInUser = new User(userId);
        saveCurrentUserInformation(loggedInUser);
        btnSignIn.setEnabled(false); // Prevent multiple sign-in attempts
        btnSignIn.setText("Opening your profile...");
        MainFrame.getInstance().loadProfilePanel(); // Load and display the profile panel immediately
        MainFrame.getInstance().showProfilePanel();

        // Load other user-specific panels in a background thread
        new SwingWorker<Void, Void>() {
          @Override
          protected Void doInBackground() throws Exception {
            MainFrame.getInstance().loadUserPanels();
            return null;
          }
        }.execute();

      } else {
        JOptionPane.showMessageDialog(null, "Incorrect username or password.", "Login Failed",
            JOptionPane.ERROR_MESSAGE);
        usernameField.requestFocus();
      }
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, "An error occurred while attempting to log in. Please try again.",
          "Login Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void onRegisterNowClicked(ActionEvent event) {
    MainFrame.getInstance().showSignUpPanel();
  }

  private void saveCurrentUserInformation(User user) {
    SessionManager.setCurrentUser(user);
  }

}
