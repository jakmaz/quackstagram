import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
  private static final String URL = "jdbc:yourdb://localhost:3306/yourdatabase";
  private static final String USER = "username";
  private static final String PASSWORD = "password";

  public static Connection connect() {
    try {
      // Ensure the JDBC driver is loaded
      Class.forName("com.yourdb.Driver");

      // Create a connection to the database
      Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
      System.out.println("Connected to the database successfully");
      return conn;
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}
