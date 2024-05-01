package Database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class SchemaCreator {

  private static Connection getConnection() throws SQLException {
    return DatabaseConnection.getConnection();
  }

  public static void createDatabaseSchema() {
    try {
      createUsersTable();
      createPostsTable();
      createCommentsTable();
      createLikesTable();
      createFollowersTable();
      createNotificationsTable();
      setupForeignKeys();
      addDummyData();
      System.out.println("Database schema created successfully.");
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Failed to create database schema.");
    }
  }

  private static void createUsersTable() throws SQLException {
    String sql = """
        CREATE TABLE IF NOT EXISTS users (
            id int PRIMARY KEY AUTO_INCREMENT,
            username varchar(255),
            password varchar(255),
            bio varchar(255),
            profile_photo blob
        )""";
    executeUpdate(sql);
  }

  private static void createPostsTable() throws SQLException {
    String sql = """
        CREATE TABLE IF NOT EXISTS posts (
            id int PRIMARY KEY AUTO_INCREMENT,
            user_id int,
            caption varchar(255),
            image blob,
            timestamp timestamp
        )""";
    executeUpdate(sql);
  }

  private static void createCommentsTable() throws SQLException {
    String sql = """
        CREATE TABLE IF NOT EXISTS comments (
            id int PRIMARY KEY AUTO_INCREMENT,
            post_id int,
            user_id int,
            text varchar(255),
            timestamp timestamp
        )""";
    executeUpdate(sql);
  }

  private static void createLikesTable() throws SQLException {
    String sql = """
        CREATE TABLE IF NOT EXISTS likes (
            post_id int,
            user_id int,
            timestamp timestamp,
            PRIMARY KEY (`post_id`, `user_id`)
        )""";
    executeUpdate(sql);
  }

  private static void createFollowersTable() throws SQLException {
    String sql = """
        CREATE TABLE IF NOT EXISTS followers (
          follower_id int,
          following_id int,
          timestamp timestamp,
          PRIMARY KEY (`follower_id`, `following_id`)
        );""";
    executeUpdate(sql);
  }

  private static void createNotificationsTable() throws SQLException {
    String sql = """
        CREATE TABLE IF NOT EXISTS notifications (
            id int PRIMARY KEY AUTO_INCREMENT,
            user_id int,
            post_id int,
            message varchar(255),
            timestamp timestamp
        )""";
    executeUpdate(sql);
  }

  private static void setupForeignKeys() throws SQLException {
    String[] sqlStatements = {
        "ALTER TABLE posts ADD FOREIGN KEY (user_id) REFERENCES users (id)",
        "ALTER TABLE comments ADD FOREIGN KEY (post_id) REFERENCES posts (id)",
        "ALTER TABLE comments ADD FOREIGN KEY (user_id) REFERENCES users (id)",
        "ALTER TABLE likes ADD FOREIGN KEY (post_id) REFERENCES posts (id)",
        "ALTER TABLE likes ADD FOREIGN KEY (user_id) REFERENCES users (id)",
        "ALTER TABLE followers ADD FOREIGN KEY (follower_id) REFERENCES users (id)",
        "ALTER TABLE followers ADD FOREIGN KEY (following_id) REFERENCES users (id)",
        "ALTER TABLE notifications ADD FOREIGN KEY (user_id) REFERENCES users (id)",
        "ALTER TABLE notifications ADD FOREIGN KEY (post_id) REFERENCES posts (id)"
    };
    for (String sql : sqlStatements) {
      executeUpdate(sql);
    }
  }

  private static void addDummyData() {
    addUsers();
  }

  private static void addUsers() {
    DatabaseUtils.registerUser("admin", "admin", "admin");
  }

  private static void executeUpdate(String sql) throws SQLException {
    try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(sql);
    }
  }

  public static void main(String[] args) {
    createDatabaseSchema();
  }
}
