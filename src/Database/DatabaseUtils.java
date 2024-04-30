package Database;

import Logic.User;

import java.sql.*;

public class DatabaseUtils {
  private static Connection getConnection() throws SQLException {
    return DatabaseConnection.getConnection();
  }

  public static boolean usernameExists(String username) {
    String query = "SELECT COUNT(*) FROM users WHERE username = ?";
    try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setString(1, username);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getInt(1) > 0;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static void registerUser(String username, String password, String bio) {
    String sql = "INSERT INTO users (username, password, bio) VALUES (?, ?, ?)";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, username);
      ps.setString(2, password);
      ps.setString(3, bio);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static boolean verifyCredentials(String username, String password) {
    String sql = "SELECT password FROM users WHERE username = ?";
    try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        String storedPassword = rs.getString("password");
        return storedPassword.equals(password);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static User getUserDetails(String username) {
    String sql = "SELECT username, password, bio FROM users WHERE username = ?";
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return new User(rs.getString("username"), rs.getString("password"), rs.getString("bio"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void followUser(int followerId, int followingId) {
    String sql = "INSERT INTO follow (follower_id, following_id) VALUES (?, ?)";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, followerId);
      ps.setInt(2, followingId);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void postSomething(int userId, String caption, String imagePath) {
    String sql = "INSERT INTO posts (user_id, caption, image, timestamp) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, userId);
      pstmt.setString(2, caption);
      pstmt.setString(3, imagePath);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
