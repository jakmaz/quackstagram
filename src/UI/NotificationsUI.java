package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Database.DAO.NotificationsDAO;
import Logic.Notification;
import Logic.SessionManager;

public class NotificationsUI extends BaseUI {

  private final int currentUserId;

  public NotificationsUI() {
    this.currentUserId = SessionManager.getCurrentUser().getId();
    setSize(WIDTH, HEIGHT);
    setMinimumSize(new Dimension(WIDTH, HEIGHT));
    setLayout(new BorderLayout());
    initializeUI();
  }

  public void initializeUI() {
    JPanel headerPanel = createHeaderPanel("Notifications");
    JPanel navigationPanel = createNavigationPanel();
    JPanel contentPanel = createContentPanel();

    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    add(headerPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);
    add(navigationPanel, BorderLayout.SOUTH);
  }

  private JPanel createContentPanel() {
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    List<Notification> notifications = NotificationsDAO.getNotificationsByUserId(currentUserId);
    notifications.forEach(notification -> contentPanel.add(createNotificationPanel(notification)));
    return contentPanel;
  }

  private JPanel createNotificationPanel(Notification notification) {
    JPanel notificationPanel = new JPanel();
    notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));
    notificationPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

    JLabel notificationLabel = new JLabel(notification.getMessage());
    notificationLabel.setFont(new Font("Arial", Font.BOLD, 14));

    JLabel timeLabel = new JLabel(getElapsedTime(notification.getTimestamp().toLocalDateTime()));
    timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    timeLabel.setForeground(new Color(130, 130, 130)); // Dark gray text for time

    notificationPanel.add(notificationLabel);
    notificationPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Adds space between text and time
    notificationPanel.add(timeLabel);

    return notificationPanel;
  }

  private String getElapsedTime(LocalDateTime notificationTimestamp) {
    LocalDateTime now = LocalDateTime.now();
    long days = ChronoUnit.DAYS.between(notificationTimestamp, now);
    long hours = ChronoUnit.HOURS.between(notificationTimestamp, now) % 24;
    long minutes = ChronoUnit.MINUTES.between(notificationTimestamp, now) % 60;

    StringBuilder timeSince = new StringBuilder();
    if (days > 0) {
      timeSince.append(days).append(" days");
    }
    if (hours > 0) {
      if (timeSince.length() > 0)
        timeSince.append(", ");
      timeSince.append(hours).append(" hours");
    }
    if (minutes > 0 && days == 0) { // Only show minutes if there are no days counted
      if (timeSince.length() > 0)
        timeSince.append(", ");
      timeSince.append(minutes).append(" minutes");
    }

    if (timeSince.length() == 0) {
      return "Just now";
    } else {
      return timeSince.append(" ago").toString();
    }
  }
}
