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
}
