package UI;

import Logic.User;

import javax.swing.*;
import java.awt.*;

public class UserProfileUI extends ProfileUI {
  User user;

  public void setUser(User user) {
    this.user = user;
    initializeUI();
  }

  @Override
  JPanel createHeaderActionButtons() {
    // Create a panel to hold buttons
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS)); // Use X_AXIS for horizontal alignment

    // Configure the Follow button
    JButton followButton = new JButton("Follow");
    followButton.setFont(new Font("Arial", Font.BOLD, 12));
    followButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, followButton.getMinimumSize().height));
    followButton.setBackground(new Color(225, 228, 232)); // A soft, appealing color
    followButton.setForeground(Color.BLACK);
    followButton.setOpaque(true);
    followButton.setBorderPainted(false);
    followButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10)); // Add some vertical padding
    buttonPanel.add(followButton);
    return buttonPanel;
  }
}
