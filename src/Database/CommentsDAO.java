package Database;

import Logic.Comment;
import Logic.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentsDAO {
  private static Connection getConnection() throws SQLException {
    return DatabaseConnection.getConnection();
  }

  public static void postComment(int postId, int userId, String text) {
    String sql = "INSERT INTO comments (post_id, user_id, text, timestamp) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, postId);
      ps.setInt(2, userId);
      ps.setString(3, text);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static List<Comment> getCommentsForPost(int postId) {
    List<Comment> comments = new ArrayList<>();
    String sql = "SELECT * FROM comments WHERE post_id = ?";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, postId);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        comments.add(new Comment(rs.getInt("id"), new User(rs.getInt("user_id")), rs.getString("text"),
            rs.getTimestamp("timestamp")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return comments;
  }
}
