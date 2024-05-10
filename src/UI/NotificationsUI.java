package UI;

import javax.swing.*;
import java.awt.*;
import Logic.Notification;
import Database.DAO.NotificationsDAO;
import Logic.SessionManager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
    JPanel headerPanel = createHeaderPanel();
    JPanel navigationPanel = createNavigationPanel();
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    // Fetch notifications from the database
    List<Notification> notifications = NotificationsDAO.getNotificationsByUserId(currentUserId);
    for (Notification notification : notifications) {
      String notificationMessage = notification.getMessage() + " " + getElapsedTime(notification.getTimestamp().toLocalDateTime());

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

  private JPanel createHeaderPanel() {
    JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    headerPanel.setBackground(new Color(51, 51, 51));
    JLabel lblRegister = new JLabel(" Notifications 🐥");
    lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
    lblRegister.setForeground(Color.WHITE);
    headerPanel.add(lblRegister);
    headerPanel.setPreferredSize(new Dimension(WIDTH, 40));
    return headerPanel;
  }
}
