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
      DatabaseReset.resetDatabase();
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
    addFollowers();
    addLikes();
    addComments();
  }

  private static void addUsers() {
    UserDAO.registerUser("admin", "admin", "Main chief developer", "img/storage/profile/1.png");
    UserDAO.registerUser("mordula", "admin", "Eva Mordface, Inhabitant of Mordor", "img/storage/profile/2.png");
    UserDAO.registerUser("swiniar", "admin", "Jack Swinehead, Mechanic engineer", "img/storage/profile/3.png");
    UserDAO.registerUser("pluta", "admin", "Vojtech Pluton, Soft ferromagnetics in rotating fields",
        "img/storage/profile/4.png");
    UserDAO.registerUser("gala", "admin", "Alex Galla-Yaskier, Gala, gala - E!", "img/storage/profile/5.png");
    UserDAO.registerUser("baska", "admin", "Barb McWitch, Desing(e)", "img/storage/profile/6.png");
  }

  private static void addPosts() {
    // Admin
    PostDAO.postSomething(1, "Debugging the night away. #CodeLife", "img/storage/posts/1_1.png");
    PostDAO.postSomething(1, "Pushing new features at 2 AM. Who needs sleep?", "img/storage/posts/1_2.png");
    PostDAO.postSomething(1, "When you finally fix that bug that's been bugging you. #Victory",
        "img/storage/posts/1_3.png");
    PostDAO.postSomething(1, "Hack the planet! Or at least this small part of it. 🌐", "img/storage/posts/1_4.png");
    PostDAO.postSomething(1, "The code's compiling... time for a break!", "img/storage/posts/1_5.png");

    // Mordula
    PostDAO.postSomething(2, "Exploring the quantum weirdness of everyday physics.", "img/storage/posts/2_1.png");
    PostDAO.postSomething(2, "Helllooooooo! Diving deep into Einstein's spacetime.", "img/storage/posts/2_2.png");
    PostDAO.postSomething(2, "Creating the new gravitational field. Or just messing with magnets.",
        "img/storage/posts/2_3.png");
    PostDAO.postSomething(2, "Distorting reality, one physics equation at a time.", "img/storage/posts/2_4.png");
    PostDAO.postSomething(2, "If only my potions were as stable as my theories!", "img/storage/posts/2_5.png");

    // Swiniar
    PostDAO.postSomething(3, "Just another day in the workshop, forging realities.", "img/storage/posts/3_1.png");
    PostDAO.postSomething(3, "Cranking gears and taking names. #MechanicLife", "img/storage/posts/3_2.png");
    PostDAO.postSomething(3, "Nothing like a bit of grease and a lot of gears.", "img/storage/posts/3_3.png");
    PostDAO.postSomething(3, "Teaching levers and pulleys, the building blocks of innovation.",
        "img/storage/posts/3_4.png");
    PostDAO.postSomething(3, "When your machine works on the first try, you know it's going to be a good day.",
        "img/storage/posts/3_5.png");

    // Pluta
    PostDAO.postSomething(4, "Wired for success. 🎚️", "img/storage/posts/4_1.png");
    PostDAO.postSomething(4, "When your circuit works and it’s not a shock. #Electronics", "img/storage/posts/4_2.png");
    PostDAO.postSomething(4, "Soldering my way through problems. Quite literally.", "img/storage/posts/4_3.png");
    PostDAO.postSomething(4, "Barking up the right tree with these new electronic modules!",
        "img/storage/posts/4_4.png");
    PostDAO.postSomething(4, "Sometimes I think in code and dream in circuit diagrams.", "img/storage/posts/4_5.png");

    // Gala
    PostDAO.postSomething(5, "Math: where we find the problems fun and the solutions even funner!",
        "img/storage/posts/5_1.png");
    PostDAO.postSomething(5, "Numbers are the language of nature and I'm just composing poetry.",
        "img/storage/posts/5_2.png");
    PostDAO.postSomething(5, "Integrating my thoughts one equation at a time.", "img/storage/posts/5_3.png");
    PostDAO.postSomething(5, "Where there's a cosine, there's a way!", "img/storage/posts/5_4.png");
    PostDAO.postSomething(5, "Solving for X, Y, and Z. Because why not?", "img/storage/posts/5_5.png");

    // Baska
    PostDAO.postSomething(6, "Painting pixels and sketching dreams.", "img/storage/posts/6_1.png");
    PostDAO.postSomething(6, "Designing a more colorful tomorrow.", "img/storage/posts/6_2.png");
    PostDAO.postSomething(6, "Art is where work meets love.", "img/storage/posts/6_3.png");
    PostDAO.postSomething(6, "Bringing my visions to life, one design at a time.", "img/storage/posts/6_4.png");
    PostDAO.postSomething(6, "If it's not bold, it's not worth designing.", "img/storage/posts/6_5.png");
  }

  private static void addFollowers() {
    // Admin follows everyone
    for (int i = 2; i < 7; i++) {
      FollowDAO.followUser(1, i);
    }
    // Everyone follows admin
    for (int i = 2; i < 7; i++) {
      FollowDAO.followUser(i, 1);
    }
  }

  private static void addLikes() {
    for (int i = 1; i <= 30; i++) {
      PostDAO.likePost(i, 1);
    }
  }

  private static void addComments() {
    CommentsDAO.postComment(1, 2, "Great job!");
    CommentsDAO.postComment(2, 3, "Nice work!");
    CommentsDAO.postComment(3, 4, "Great job!");
    CommentsDAO.postComment(4, 5, "Keep it up!");
    CommentsDAO.postComment(5, 6, "Awesome!");
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
