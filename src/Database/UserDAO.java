package Database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

import Logic.UserDetails;

public class UserDAO {
  private static Connection getConnection() throws SQLException {
    return DatabaseConnection.getConnection();
  }

  public static boolean usernameExists(String username) {
    String query = "SELECT COUNT(*) FROM users WHERE username = ?";
    try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getInt(1) > 0;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static int registerUser(String username, String password, String bio, String imagePath) {
    String sql = "INSERT INTO users (username, password, bio, image_path) VALUES (?, ?, ?, ?)";
    int userId = -1;
    try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, username);
      ps.setString(2, hashPassword(password)); // Hash the password before storing it
      ps.setString(3, bio);
      ps.setString(4, imagePath);
      int affectedRows = ps.executeUpdate();

      if (affectedRows > 0) {
        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            userId = generatedKeys.getInt(1);
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return userId;
  }

  private static String hashPassword(String password) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] hashedPassword = md.digest(password.getBytes());
      return Base64.getEncoder().encodeToString(hashedPassword);
    } catch (NoSuchAlgorithmException e) {
      System.err.println("Failed to hash password: " + e.getMessage());
      return null; // Returning null or alternatively, you could handle it differently based on
                   // your application's needs.
    }
  }

  public static Integer verifyCredentials(String username, String password) {
    String sql = "SELECT id, password FROM users WHERE username = ?";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        String storedPassword = rs.getString("password");
        String hashedInputPassword = hashPassword(password); // Hash the input password to compare
        if (storedPassword.equals(hashedInputPassword)) {
          return rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static UserDetails getUserDetails(int userId) {
    String sql = "SELECT id, username, bio FROM users WHERE id = ?";
    try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return new UserDetails(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("bio"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}
