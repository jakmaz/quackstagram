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
    JButton createHeaderActionButton() {
        JButton editProfileButton = new JButton("Edit Profile");
        editProfileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        editProfileButton.setFont(new Font("Arial", Font.BOLD, 12));
        editProfileButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, editProfileButton.getMinimumSize().height)); // Make the button fill the horizontal space
        editProfileButton.setMargin(new Insets(7, 0, 7, 0)); // top, left, bottom, right padding

        // Add action listener to open the edit profile dialog
        editProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Assuming the frame of your application is accessible, otherwise pass a suitable parent
                Frame owner = JOptionPane.getFrameForComponent(editProfileButton); // Get the parent frame
                EditProfileDialog editDialog = new EditProfileDialog(owner);
                editDialog.setVisible(true);
            }
        });

        return editProfileButton;
    }
}
