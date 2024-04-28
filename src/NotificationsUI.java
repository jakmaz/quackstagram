import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class NotificationsUI extends BaseUI {

  private static final int WIDTH = 300;
  private static final int HEIGHT = 500;

  public NotificationsUI() {
    setSize(WIDTH, HEIGHT);
    setMinimumSize(new Dimension(WIDTH, HEIGHT));
    setLayout(new BorderLayout());
    initializeUI();
  }

  public void initializeUI() {
    // Reuse the header and navigation panel creation methods from the
    // InstagramProfileUI class
    JPanel headerPanel = createHeaderPanel();
    JPanel navigationPanel = createNavigationPanel();

    // Content Panel for notifications
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    // Read the current username from users.txt
    String currentUsername = "";
    try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
      String line = reader.readLine();
      if (line != null) {
        currentUsername = line.split(":")[0].trim();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "notifications.txt"))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(";");
        if (parts[0].trim().equals(currentUsername)) {
          // Format the notification message
          String userWhoLiked = parts[1].trim();
          String imageId = parts[2].trim();
          String timestamp = parts[3].trim();
          String notificationMessage = userWhoLiked + " liked your picture - " + getElapsedTime(timestamp) + " ago";

          // Add the notification to the panel
          JPanel notificationPanel = new JPanel(new BorderLayout());
          notificationPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

          JLabel notificationLabel = new JLabel(notificationMessage);
          notificationPanel.add(notificationLabel, BorderLayout.CENTER);

          // Add profile icon (if available) and timestamp
          // ... (Additional UI components if needed)

          contentPanel.add(notificationPanel);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    // Add panels to frame
    add(headerPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);
    add(navigationPanel, BorderLayout.SOUTH);
  }

  private String getElapsedTime(String timestamp) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime timeOfNotification = LocalDateTime.parse(timestamp, formatter);
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

    // Header Panel (reuse from InstagramProfileUI or customize for home page)
    // Header with the Register label
    JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
    JLabel lblRegister = new JLabel(" Notifications 🐥");
    lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
    lblRegister.setForeground(Color.WHITE); // Set the text color to white
    headerPanel.add(lblRegister);
    headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
    return headerPanel;
  }

}
