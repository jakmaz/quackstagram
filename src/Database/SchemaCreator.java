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
          id INT AUTO_INCREMENT PRIMARY KEY,
          username VARCHAR(255) UNIQUE NOT NULL,
          password VARCHAR(255) NOT NULL,
          bio VARCHAR(255),
          image_path VARCHAR(255)
        );""";
    executeUpdate(sql);
  }

  private static void createPostsTable() throws SQLException {
    String sql = """
        CREATE TABLE IF NOT EXISTS posts (
          id INT AUTO_INCREMENT PRIMARY KEY,
          user_id INT NOT NULL,
          caption VARCHAR(255),
          image_path VARCHAR(255),
          timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          FOREIGN KEY (user_id) REFERENCES users(id)
        );""";
    executeUpdate(sql);
  }

  private static void createCommentsTable() throws SQLException {
    String sql = """
        CREATE TABLE IF NOT EXISTS comments (
          id INT AUTO_INCREMENT PRIMARY KEY,
          post_id INT NOT NULL,
          user_id INT NOT NULL,
          text VARCHAR(255),
          timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          FOREIGN KEY (post_id) REFERENCES posts(id),
          FOREIGN KEY (user_id) REFERENCES users(id)
        );""";
    executeUpdate(sql);
  }

  private static void createLikesTable() throws SQLException {
    String sql = """
        CREATE TABLE IF NOT EXISTS likes (
          post_id INT,
          user_id INT,
          timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          PRIMARY KEY (post_id, user_id),
          FOREIGN KEY (post_id) REFERENCES posts(id),
          FOREIGN KEY (user_id) REFERENCES users(id)
        );""";
    executeUpdate(sql);
  }

  private static void createFollowersTable() throws SQLException {
    String sql = """
        CREATE TABLE IF NOT EXISTS followers (
          follower_id INT,
          following_id INT,
          timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          PRIMARY KEY (follower_id, following_id),
          FOREIGN KEY (follower_id) REFERENCES users(id),
          FOREIGN KEY (following_id) REFERENCES users(id)
        );
        """;
    executeUpdate(sql);
  }

  private static void createNotificationsTable() throws SQLException {
    String sql = """
        CREATE TABLE IF NOT EXISTS notifications (
          id INT AUTO_INCREMENT PRIMARY KEY,
          user_id INT NOT NULL,
          post_id INT,
          message VARCHAR(255),
          timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          FOREIGN KEY (user_id) REFERENCES users(id),
          FOREIGN KEY (post_id) REFERENCES posts(id)
        );""";
    executeUpdate(sql);
  }

  private static void addDummyData() {
    addUsers();
    addPosts();
  }

  private static void addUsers() {
    DatabaseUtils.registerUser("admin", "admin", "Main chief developer", "img/storage/profile/admin.png");
    DatabaseUtils.registerUser("mordula", "admin", "Eva Mordface, Inhabitant of Mordor", "img/storage/profile/mordula.png");
    DatabaseUtils.registerUser("swiniar", "admin", "Jack Swinehead, Mechanic engineer", "img/storage/profile/swiniar.png");
    DatabaseUtils.registerUser("pluta", "admin", "Vojtech Pluton, Soft ferromagnetics in rotating fields", "img/storage/profile/pluta.png");
    DatabaseUtils.registerUser("gala", "admin", "Alex Galla-Yaskier, Gala, gala - E!", "img/storage/profile/gala.png");
    DatabaseUtils.registerUser("baska", "admin", "Barb McWitch, Desing(e)", "img/storage/profile/baska.png");
  }

  private static void addPosts() {
    DatabaseUtils.postSomething(1, "Working on the quackstagram", "img/storage/uploaded/admin_1.png");
    DatabaseUtils.postSomething(1, "Working on the quackstagram", "img/storage/uploaded/admin_2.png");
    DatabaseUtils.postSomething(1, "Working on the quackstagram", "img/storage/uploaded/admin_3.png");
    DatabaseUtils.postSomething(1, "Working on the quackstagram", "img/storage/uploaded/admin_4.png");
    DatabaseUtils.postSomething(1, "Working on the quackstagram", "img/storage/uploaded/admin_5.png");
    DatabaseUtils.postSomething(2, "Grey soap?! on the tongue?", "img/storage/uploaded/mordula_1.png");
    DatabaseUtils.postSomething(2, "Helllooooooo!", "img/storage/uploaded/mordula_2.png");
    DatabaseUtils.postSomething(2, "Creating the new gravitational field", "img/storage/uploaded/mordula_3.png");
    DatabaseUtils.postSomething(2, "Working on the quackstagram", "img/storage/uploaded/mordula_4.png");
    DatabaseUtils.postSomething(2, "Working on the quackstagram", "img/storage/uploaded/mordula_5.png");
    DatabaseUtils.postSomething(3, "Working on the quackstagram", "img/storage/uploaded/swiniar_1.png");
    DatabaseUtils.postSomething(3, "Working on the quackstagram", "img/storage/uploaded/swiniar_2.png");
    DatabaseUtils.postSomething(3, "Working on the quackstagram", "img/storage/uploaded/swiniar_3.png");
    DatabaseUtils.postSomething(3, "Working on the quackstagram", "img/storage/uploaded/swiniar_4.png");
    DatabaseUtils.postSomething(3, "Working on the quackstagram", "img/storage/uploaded/swiniar_5.png");
    DatabaseUtils.postSomething(4, "Working on the quackstagram", "img/storage/uploaded/pluta_1.png");
    DatabaseUtils.postSomething(4, "Working on the quackstagram", "img/storage/uploaded/pluta_2.png");
    DatabaseUtils.postSomething(4, "Working on the quackstagram", "img/storage/uploaded/pluta_3.png");
    DatabaseUtils.postSomething(4, "Working on the quackstagram", "img/storage/uploaded/pluta_4.png");
    DatabaseUtils.postSomething(4, "Working on the quackstagram", "img/storage/uploaded/pluta_5.png");
    DatabaseUtils.postSomething(5, "Working on the quackstagram", "img/storage/uploaded/gala_1.png");
    DatabaseUtils.postSomething(5, "Working on the quackstagram", "img/storage/uploaded/gala_2.png");
    DatabaseUtils.postSomething(5, "Working on the quackstagram", "img/storage/uploaded/gala_3.png");
    DatabaseUtils.postSomething(5, "Working on the quackstagram", "img/storage/uploaded/gala_4.png");
    DatabaseUtils.postSomething(5, "Working on the quackstagram", "img/storage/uploaded/gala_5.png");
    DatabaseUtils.postSomething(6, "Working on the quackstagram", "img/storage/uploaded/baska_1.png");
    DatabaseUtils.postSomething(6, "Working on the quackstagram", "img/storage/uploaded/baska_2.png");
    DatabaseUtils.postSomething(6, "Working on the quackstagram", "img/storage/uploaded/baska_3.png");
    DatabaseUtils.postSomething(6, "Working on the quackstagram", "img/storage/uploaded/baska_4.png");
    DatabaseUtils.postSomething(6, "Working on the quackstagram", "img/storage/uploaded/baska_5.png");
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
