package UI;

import java.awt.*;


import javax.swing.JButton;
import javax.swing.JPanel;

import Database.DAO.FollowDAO;
import Logic.SessionManager;
import Logic.User;

public class OtherUserProfileUI extends ProfileUI {
  private JButton followButton;

  public OtherUserProfileUI(User user) {
    this.currentUser = user;
    initializeUI();
  }

  @Override
  JPanel createHeaderActionButtons() {
    JPanel buttonPanel = new JPanel(new GridLayout(1, 1));
    followButton = new JButton();
    updateFollowButton();
    followButton.addActionListener(e -> handleFollowAction());
    buttonPanel.add(followButton);
    return buttonPanel;
  }

  private void handleFollowAction() {
    boolean isFollowing = FollowDAO.isFollowing(SessionManager.getCurrentUser().getId(), currentUser.getId());
    if (isFollowing) {
      FollowDAO.unfollowUser(SessionManager.getCurrentUser().getId(), currentUser.getId());
    } else {
      FollowDAO.followUser(SessionManager.getCurrentUser().getId(), currentUser.getId());
    }
    updateFollowButton();  // Update button text after action
  }

  private void updateFollowButton() {
    boolean isFollowing = FollowDAO.isFollowing(SessionManager.getCurrentUser().getId(), currentUser.getId());
    followButton.setText(isFollowing ? "Followed" : "Follow");
  }
}
