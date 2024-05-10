package UI;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;

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
    JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
    buttonPanel.setBackground(new Color(249, 249, 249));

    // Create and configure the Edit Profile button
    JButton editProfileButton = new JButton("Edit Profile");
    editProfileButton.addActionListener(e -> {
      Frame owner = JOptionPane.getFrameForComponent(editProfileButton); // Get the parent frame
      EditProfileDialog editDialog = new EditProfileDialog(owner);
      editDialog.setVisible(true);
    });
    buttonPanel.add(editProfileButton);

    // Create and configure the Log-Out button
    JButton logOutButton = new JButton("Log Out");
    logOutButton.addActionListener(e -> {
      MainFrame.getInstance().clearUI();
      MainFrame.getInstance().loadLoginPanels();
      MainFrame.getInstance().showSignInPanel();
      SessionManager.clearCurrentUser();
    });
    buttonPanel.add(logOutButton);

    return buttonPanel;
  }
}
