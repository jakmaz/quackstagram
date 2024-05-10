package Database;

import java.sql.*;

public class LikesDAO {
  private static Connection getConnection() throws SQLException {
    return DatabaseConnection.getConnection();
  }

  public static int getLikesCountForPost(int postId) {
    String sql = "SELECT COUNT(*) AS count FROM likes WHERE post_id = ?";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, postId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getInt("count");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public static String likePost(int postId, int userId) {
    // Check if the like already exists
    try (Connection conn = getConnection();
        PreparedStatement checkStmt = conn
            .prepareStatement("SELECT COUNT(*) FROM likes WHERE post_id = ? AND user_id = ?")) {
      checkStmt.setInt(1, postId);
      checkStmt.setInt(2, userId);
      ResultSet rs = checkStmt.executeQuery();
      if (rs.next() && rs.getInt(1) > 0) {
        return "You've already liked this post.";
      }

      // Insert the new like
      try (PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO likes (post_id, user_id) VALUES (?, ?)")) {
        insertStmt.setInt(1, postId);
        insertStmt.setInt(2, userId);
        insertStmt.executeUpdate();
        return "Post successfully liked!";
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      return "Error liking post.";
    }
  }
}
