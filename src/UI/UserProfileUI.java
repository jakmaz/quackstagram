package UI;

import Logic.User;

import javax.swing.*;
import java.awt.*;

public class UserProfileUI extends ProfileUI {
    User user;

    public UserProfileUI() {
        super();
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    JButton createHeaderActionButton() {
        JButton followButton = new JButton("Follow");
        followButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        followButton.setFont(new Font("Arial", Font.BOLD, 12));
        followButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, followButton.getMinimumSize().height)); // Make the button fill the horizontal space
        followButton.setBackground(new Color(225, 228, 232)); // A soft, appealing color that complements the UI
        followButton.setForeground(Color.BLACK);
        followButton.setOpaque(true);
        followButton.setBorderPainted(false);
        followButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10)); // Add some vertical padding
        return followButton;
    }
}