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
    String insert = "INSERT INTO users (username, password, bio) VALUES (?, ?, ?)";
    try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(insert)) {
      stmt.setString(1, username);
      stmt.setString(2, password);
      stmt.setString(3, bio);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static boolean verifyCredentials(String username, String password) {
    String sql = "SELECT password FROM users WHERE username = ?";
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, username);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        String storedPassword = rs.getString("password");
        // Assuming passwords are stored securely (e.g., hashed with a salt)
        return storedPassword.equals(password); // For simplicity; in practice, use password hashing+salt
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static User getUserDetails(String username) {
    String sql = "SELECT username, password, bio FROM users WHERE username = ?";
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, username);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return new User(rs.getString("username"), rs.getString("password"), rs.getString("bio"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}
