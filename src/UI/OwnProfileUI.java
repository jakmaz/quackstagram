package UI;

import Logic.SessionManager;
import Logic.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            MainFrame.getInstance().switchPanel("SignIn");
            SessionManager.clearCurrentUser();
        });
        buttonPanel.add(logOutButton);

        return buttonPanel;
    }
}
