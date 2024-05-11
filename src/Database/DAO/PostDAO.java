package Database.DAO;

import Database.Connection.DatabaseConnection;
import Logic.Post;
import Logic.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {
  private static Connection getConnection() throws SQLException {
    return DatabaseConnection.getConnection();
  }

  /**
   * Inserts a new post with the current timestamp.
   *
   * @param userId    The ID of the user making the post.
   * @param caption   The caption of the post.
   * @param imagePath The path to the image associated with the post.
   */
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

  /**
   * Inserts a new post with a specified timestamp.
   *
   * @param userId    The ID of the user making the post.
   * @param caption   The caption of the post.
   * @param imagePath The path to the image associated with the post.
   * @param timestamp The timestamp when the post should be recorded.
   */
  public static void postSomething(int userId, String caption, String imagePath, Timestamp timestamp) {
    String sql = "INSERT INTO posts (user_id, caption, image_path, timestamp) VALUES (?, ?, ?, ?)";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ps.setString(2, caption);
      ps.setString(3, imagePath);
      ps.setTimestamp(4, timestamp);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Retrieves all posts made by a specific user.
   *
   * @param userId The ID of the user whose posts are to be retrieved.
   * @return A list of Post objects representing all posts made by the specified
   *         user.
   */
  public static List<Post> getPostsByUserId(int userId) {
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

  public static List<Post> getPostsByUsername(String username) {
    List<Post> posts = new ArrayList<>();
    String sql = "SELECT p.id, p.user_id, p.caption, p.image_path, p.timestamp FROM posts p " +
        "JOIN users u ON p.user_id = u.id " +
        "WHERE u.username = ?";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, username);
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

  /**
   * Retrieves all posts in the database.
   *
   * @return A list of all posts available in the database.
   */
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

  /**
   * Retrieves posts from all users whom a specific user follows.
   *
   * @param userId The user ID of the follower.
   * @return A list of posts from the users being followed by the specified user.
   */
  public static List<Post> getFollowingPosts(int userId) {
    List<Post> posts = new ArrayList<>();
    String sql = "SELECT p.id, p.user_id, p.caption, p.image_path, p.timestamp FROM posts p " +
        "JOIN followers f ON p.user_id = f.following_id " +
        "WHERE f.follower_id = ?";
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

  /**
   * Registers a like on a post by a specific user.
   *
   * @param postId The ID of the post that is being liked.
   * @param userId The ID of the user who likes the post.
   */
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
