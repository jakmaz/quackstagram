package Database.Setup;

import Database.Connection.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TriggersCreator {

    private static Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public static void createTriggers() {
        try {
            createFollowersTrigger();
            createLikesTrigger();
            createCommentsTrigger();
            System.out.println("Triggers created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to create triggers.");
        }
    }

    private static void createFollowersTrigger() throws SQLException {
        String sql = """
            CREATE TRIGGER IF NOT EXISTS trg_after_follow
            AFTER INSERT ON followers
            FOR EACH ROW
            BEGIN
                INSERT INTO notifications (user_id, message, timestamp)
                VALUES (NEW.following_id, CONCAT('You have a new follower: ', (SELECT username FROM users WHERE id = NEW.follower_id)), CURRENT_TIMESTAMP);
            END;
            """;
        executeUpdate(sql);
    }

    private static void createLikesTrigger() throws SQLException {
        String sql = """
            CREATE TRIGGER IF NOT EXISTS trg_after_like
            AFTER INSERT ON likes
            FOR EACH ROW
            BEGIN
                INSERT INTO notifications (user_id, post_id, message, timestamp)
                VALUES ((SELECT user_id FROM posts WHERE id = NEW.post_id), NEW.post_id, CONCAT('Your post was liked by ', (SELECT username FROM users WHERE id = NEW.user_id)), CURRENT_TIMESTAMP);
            END;
            """;
        executeUpdate(sql);
    }

    private static void createCommentsTrigger() throws SQLException {
        String sql = """
            CREATE TRIGGER IF NOT EXISTS trg_after_comment
            AFTER INSERT ON comments
            FOR EACH ROW
            BEGIN
                INSERT INTO notifications (user_id, post_id, message, timestamp)
                VALUES ((SELECT user_id FROM posts WHERE id = NEW.post_id), NEW.post_id, CONCAT('Your post was commented on ', (SELECT username FROM users WHERE id = NEW.user_id), ': ', NEW.text), CURRENT_TIMESTAMP);
            END;
            """;
        executeUpdate(sql);
    }

    private static void executeUpdate(String sql) throws SQLException {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}
