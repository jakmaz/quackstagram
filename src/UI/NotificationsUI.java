package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Database.DAO.NotificationsDAO;
import Logic.Notification;
import Logic.SessionManager;
import Logic.TimeElapsedCalculator;

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
    JPanel navigationPanel = createNavigationPanel(PanelKey.NOTIFICATIONS);
    JScrollPane scrollPane = createScrollPane();

    add(headerPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);
    add(navigationPanel, BorderLayout.SOUTH);
  }

  private JScrollPane createScrollPane() {
    JPanel contentPanel = createContentPanel();
    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.getVerticalScrollBar().setUnitIncrement(6);
    return scrollPane;
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

    JPanel messagePanel = createMessagePanel(notification);
    JPanel timePanel = createTimePanel(notification);

    notificationPanel.add(messagePanel);
    notificationPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Adds space between text and time
    notificationPanel.add(timePanel);

    return notificationPanel;
  }

  private JPanel createMessagePanel(Notification notification) {
    JPanel messagePanel = new JPanel(new BorderLayout());
    JTextArea notificationTextArea = createNotificationTextArea(notification);
    messagePanel.add(notificationTextArea, BorderLayout.CENTER);
    return messagePanel;
  }

  private JTextArea createNotificationTextArea(Notification notification) {
    JTextArea notificationTextArea = new JTextArea(notification.getMessage());
    notificationTextArea.setFont(new Font("Arial", Font.BOLD, 14));
    notificationTextArea.setLineWrap(true);
    notificationTextArea.setWrapStyleWord(true);
    notificationTextArea.setOpaque(false); // Make the JTextArea background transparent
    notificationTextArea.setEditable(false);
    notificationTextArea.setFocusable(false);
    notificationTextArea
        .setMaximumSize(new Dimension(Integer.MAX_VALUE, notificationTextArea.getPreferredSize().height));
    return notificationTextArea;
  }

  private JPanel createTimePanel(Notification notification) {
    JPanel timePanel = new JPanel(new BorderLayout());
    JLabel timeLabel = createTimeLabel(notification);
    timePanel.add(timeLabel, BorderLayout.WEST);
    return timePanel;
  }

  private JLabel createTimeLabel(Notification notification) {
    JLabel timeLabel = new JLabel(
        "  " + TimeElapsedCalculator.getElapsedTime(notification.getTimestamp().toLocalDateTime()));
    timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    timeLabel.setForeground(new Color(130, 130, 130)); // Dark gray text for time
    return timeLabel;
  }

}
