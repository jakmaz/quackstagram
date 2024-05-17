package UI;

import java.awt.*;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Logic.SessionManager;

public class OwnProfileUI extends ProfileUI {

  public OwnProfileUI() {
    super();
    this.currentUser = SessionManager.getCurrentUser();
    initializeUI();
  }

  @Override
  JPanel createHeaderActionButtons() {
    // Create a panel to hold buttons
    JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
    buttonPanel.setBackground(new Color(249, 249, 249));

    // Create and configure the Edit Profile button
    JButton editProfileButton = new JButton("Edit Profile");
    editProfileButton.setMargin( new Insets(5, 5, 5, 5) );
    editProfileButton.addActionListener(e -> {
      Frame owner = JOptionPane.getFrameForComponent(editProfileButton); // Get the parent frame
      EditProfileDialog editDialog = new EditProfileDialog(owner);
      editDialog.setVisible(true);
    });
    buttonPanel.add(editProfileButton);

    // Create and configure the Log-Out button
    JButton logOutButton = new JButton("Log Out");
    logOutButton.setMargin( new Insets(5, 5, 5, 5) );
    logOutButton.addActionListener(e -> {
      SessionManager.clearCurrentUser();
      MainFrame.getInstance().clearUI();
      MainFrame.getInstance().loadLoginPanels();
      MainFrame.getInstance().showSignInPanel();
    });

    buttonPanel.add(logOutButton);

    return buttonPanel;
  }
}
