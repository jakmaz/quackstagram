package Database;

import Logic.Post;
import Logic.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {
  private static Connection getConnection() throws SQLException {
    return DatabaseConnection.getConnection();
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

  public static List<Post> getPostsbyUserId(int userId) {
    List<Post> posts = new ArrayList<>();
    String sql = "SELECT id, user_id, caption, image_path, timestamp FROM posts WHERE user_id = ?";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        Post post = new Post(
            rs.getInt("id"),
            new User(rs.getInt("user_id")),
            rs.getString("caption"),
            rs.getString("image_path"),
            rs.getTimestamp("timestamp"));
        posts.add(post);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return posts;
  }

  public static List<Post> getAllPosts() {
    List<Post> posts = new ArrayList<>();
    String sql = "SELECT id, user_id, caption, image_path, timestamp FROM posts";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        Post post = new Post(
            rs.getInt("id"),
            new User(rs.getInt("user_id")),
            rs.getString("caption"),
            rs.getString("image_path"),
            rs.getTimestamp("timestamp"));
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
