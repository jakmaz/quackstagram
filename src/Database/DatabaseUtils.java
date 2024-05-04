package Database;

import Logic.Post;
import Logic.UserDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {
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
    int userId = -1; // Default or error case, assuming no valid user has an ID of -1

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, username);
      ps.setString(2, password);
      ps.setString(3, bio);
      ps.setString(4, imagePath);
      int affectedRows = ps.executeUpdate();

      if (affectedRows > 0) {
        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            userId = generatedKeys.getInt(1); // Retrieve the first field in the generated keys (the ID)
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return userId; // Return the new user's ID or -1 if there was an error
  }

  public static Integer verifyCredentials(String username, String password) {
    String sql = "SELECT id, password FROM users WHERE username = ?";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        String storedPassword = rs.getString("password");
        if (storedPassword.equals(password)) {
          return rs.getInt("id"); // Return user ID if the password matches
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null; // Return null if the credentials are incorrect or any exception occurs
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
                rs.getString("bio")
        );
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void followUser(int followerId, int followingId) {
    String sql = "INSERT INTO followers (follower_id, following_id) VALUES (?, ?)";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, followerId);
      ps.setInt(2, followingId);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void postSomething(int userId, String caption, String imagePath) {
    String sql = "INSERT INTO posts (user_id, caption, image_path, timestamp) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ps.setString(2, caption);
      ps.setString(3, imagePath);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static Integer getPostAmount(int userId) {
    String sql = "SELECT COUNT(*) FROM posts p JOIN users u ON p.user_id = u.id WHERE u.id = ?";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, userId);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static List<Post> getPosts(int userId) {
    List<Post> posts = new ArrayList<>();
    String sql = "SELECT id, user_id, caption, image_path, timestamp FROM posts WHERE user_id = ?";

    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        Post post = new Post(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("caption"),
                rs.getString("image_path"),
                rs.getTimestamp("timestamp")
        );
        posts.add(post);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return posts;
  }

  public static void likePost(int postId, int userId) {
    String sql = "INSERT INTO likes (post_id, user_id) VALUES (?, ?)";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, postId);
      ps.setInt(2, userId);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
