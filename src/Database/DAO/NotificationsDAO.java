package Database.DAO;

import Database.Connection.DatabaseConnection;
import Logic.Notification;
import Logic.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationsDAO {
  private static Connection getConnection() throws SQLException {
    return DatabaseConnection.getConnection();
  }

  public static void createNotification(int userId, String message) {
    String sql = "INSERT INTO notifications (user_id, message, timestamp) VALUES (?, ?, CURRENT_TIMESTAMP)";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ps.setString(2, message);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static List<Notification> getNotificationsByUserId(int userId) {
    List<Notification> notifications = new ArrayList<>();
    String sql = "SELECT id, user_id, message, timestamp FROM notifications WHERE user_id = ? ORDER BY timestamp DESC";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        Notification notification = new Notification(
            rs.getInt("id"),
            new User(rs.getInt("user_id")),
            rs.getString("message"),
            rs.getTimestamp("timestamp"));
        notifications.add(notification);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return notifications;
  }
}
