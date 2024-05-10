package Database.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
  private static final String URL = "jdbc:mysql://localhost:3306/quackstagram";
  private static final String USER = "BCS1510";
  private static final String PASSWORD = "BCS1510";

  static {
    try {
      // Explicitly load the MySQL JDBC driver
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static Connection getConnection() {
    try {
      return DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}
