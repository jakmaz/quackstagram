package Database.DAO;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

import Database.Connection.DatabaseConnection;
import Logic.User;
import Logic.UserDetails;

/**
 * Handles database operations related to user management.
 */
public class UserDAO {

  /**
   * Retrieves a database connection.
   *
   * @return a {@link Connection} object to the database
   * @throws SQLException if a database access error occurs
   */
  private static Connection getConnection() throws SQLException {
    return DatabaseConnection.getConnection();
  }

  /**
   * Checks if a username already exists in the database.
   *
   * @param username the username to check
   * @return true if the username exists, false otherwise
   */
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

  /**
   * Registers a new user in the database.
   *
   * @param username  the username of the new user
   * @param password  the password of the new user
   * @param bio       the biography of the new user
   * @param imagePath the image path of the new user's profile picture
   * @return the user ID of the newly registered user or null if registration
   *         failed
   */
  public static Integer registerUser(String username, String password, String bio, String imagePath) {
    String sql = "INSERT INTO users (username, password, bio, image_path) VALUES (?, ?, ?, ?)";
    try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, username);
      ps.setString(2, hashPassword(password));
      ps.setString(3, bio);
      ps.setString(4, imagePath);
      int affectedRows = ps.executeUpdate();

      if (affectedRows > 0) {
        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Hashes a password using SHA-256.
   *
   * @param password the password to hash
   * @return the hashed password as a Base64 encoded string
   */
  private static String hashPassword(String password) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] hashedPassword = md.digest(password.getBytes());
      return Base64.getEncoder().encodeToString(hashedPassword);
    } catch (NoSuchAlgorithmException e) {
      System.err.println("Failed to hash password: " + e.getMessage());
      return null;
    }
  }

  /**
   * Verifies user credentials against stored values in the database.
   *
   * @param username the username to verify
   * @param password the password to verify
   * @return the user ID if credentials are correct, null otherwise
   */
  public static Integer verifyCredentials(String username, String password) {
    String sql = "SELECT id, password FROM users WHERE username = ?";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        String storedPassword = rs.getString("password");
        if (storedPassword.equals(hashPassword(password))) {
          return rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Retrieves the user details for a given user ID.
   *
   * @param userId the ID of the user
   * @return a {@link UserDetails} object containing the user details or null if
   *         the user does not exist
   */
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

  /**
   * Finds a user in the database by their username.
   *
   * @param enteredUsername the username of the user to find
   * @return a User object representing the found user, or null if no user was found
   */
  public static User findUserByUsername(String enteredUsername) {
    String sql = "SELECT id FROM users WHERE username = ?";
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, enteredUsername);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return new User(rs.getInt("id"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Updates the profile details of a user.
   *
   * @param userId   the ID of the user
   * @param name     the new name of the user
   * @param password the new password of the user
   * @param bio      the new biography of the user
   * @return true if the profile was updated successfully, false otherwise
   */
  public static boolean updateProfile(int userId, String name, String password, String bio) {
    String sql = "UPDATE users SET username = ?, password = ?, bio = ? WHERE id = ?";
    try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, name);
      ps.setString(2, hashPassword(password));
      ps.setString(3, bio);
      ps.setInt(4, userId);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static String getProfilePicturePath(int userId) {
    String sql = "SELECT image_path FROM users WHERE id = ?";
    try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getString("image_path");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}
