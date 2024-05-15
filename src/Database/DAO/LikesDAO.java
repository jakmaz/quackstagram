package Database.DAO;

import Database.Connection.DatabaseConnection;

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

  public static boolean checkLike(int postId, int userId) {
    // Check if the like already exists
    try (Connection conn = getConnection();
        PreparedStatement checkStmt = conn
            .prepareStatement("SELECT COUNT(*) FROM likes WHERE post_id = ? AND user_id = ?")) {
      checkStmt.setInt(1, postId);
      checkStmt.setInt(2, userId);
      ResultSet rs = checkStmt.executeQuery();
      return rs.next() && rs.getInt(1) > 0;
    } catch (SQLException ex) {
      ex.printStackTrace();
      return false; // Assume not liked on error to allow retry
    }
  }

  public static boolean likePost(int postId, int userId) {
    // Add a new like
    try (Connection conn = getConnection();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO likes (post_id, user_id) VALUES (?, ?)")) {
      insertStmt.setInt(1, postId);
      insertStmt.setInt(2, userId);
      insertStmt.executeUpdate();
      return true; // Like was successfully added
    } catch (SQLException ex) {
      ex.printStackTrace();
      return false; // Error occurred while adding like
    }
  }

  public static boolean likePost(int postId, int userId, Timestamp timestamp) {
    // Add a new like with a timestamp
    try (Connection conn = getConnection();
        PreparedStatement insertStmt = conn
            .prepareStatement("INSERT INTO likes (post_id, user_id, created_at) VALUES (?, ?, ?)")) {
      insertStmt.setInt(1, postId);
      insertStmt.setInt(2, userId);
      insertStmt.setTimestamp(3, timestamp);
      insertStmt.executeUpdate();
      return true; // Like was successfully added
    } catch (SQLException ex) {
      ex.printStackTrace();
      return false; // Error occurred while adding like
    }
  }

  public static boolean unlikePost(int postId, int userId) {
    // Delete an existing like
    try (Connection conn = getConnection();
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM likes WHERE post_id = ? AND user_id = ?")) {
      deleteStmt.setInt(1, postId);
      deleteStmt.setInt(2, userId);
      deleteStmt.executeUpdate();
      return true; // Like was successfully removed
    } catch (SQLException ex) {
      ex.printStackTrace();
      return false; // Error occurred while removing like
    }
  }
}
