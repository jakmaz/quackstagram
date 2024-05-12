package Database.DAO;

import Database.Connection.DatabaseConnection;

import java.sql.*;

public class FollowDAO {
  private static Connection getConnection() throws SQLException {
    return DatabaseConnection.getConnection();
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

  public static void followUser(int followerId, int followingId, Timestamp timestamp) {
    String sql = "INSERT INTO followers (follower_id, following_id, timestamp) VALUES (?, ?, ?)";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, followerId);
      ps.setInt(2, followingId);
      ps.setTimestamp(3, timestamp);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static Integer getFollowersAmount(int userId) {
    String sql = "SELECT COUNT(*) AS count FROM followers WHERE following_id = ?";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getInt("count");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Integer getFollowingAmount(int userId) {
    String sql = "SELECT COUNT(*) AS count FROM followers WHERE follower_id = ?";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getInt("count");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}
