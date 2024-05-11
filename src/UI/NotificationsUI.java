package UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Database.DAO.NotificationsDAO;
import Logic.Notification;
import Logic.SessionManager;

public class NotificationsUI extends BaseUI {

  private final int currentUserId; // This should be set based on user authentication

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
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    // Fetch notifications from the database
    List<Notification> notifications = NotificationsDAO.getNotificationsByUserId(currentUserId);
    for (Notification notification : notifications) {
      String notificationMessage = notification.getMessage() + " "
          + getElapsedTime(notification.getTimestamp().toLocalDateTime());

      JPanel notificationPanel = new JPanel(new BorderLayout());
      notificationPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
      JLabel notificationLabel = new JLabel(notificationMessage);
      notificationPanel.add(notificationLabel, BorderLayout.CENTER);
      contentPanel.add(notificationPanel);
    }

    add(headerPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);
    add(navigationPanel, BorderLayout.SOUTH);
  }

  private String getElapsedTime(LocalDateTime timeOfNotification) {
    LocalDateTime currentTime = LocalDateTime.now();
    long daysBetween = ChronoUnit.DAYS.between(timeOfNotification, currentTime);
    long minutesBetween = ChronoUnit.MINUTES.between(timeOfNotification, currentTime) % 60;

    StringBuilder timeElapsed = new StringBuilder();
    if (daysBetween > 0) {
      timeElapsed.append(daysBetween).append(" day").append(daysBetween > 1 ? "s" : "");
    }
    if (minutesBetween > 0) {
      if (daysBetween > 0) {
        timeElapsed.append(" and ");
      }
      timeElapsed.append(minutesBetween).append(" minute").append(minutesBetween > 1 ? "s" : "");
    }
    return timeElapsed.toString();
  }
}
