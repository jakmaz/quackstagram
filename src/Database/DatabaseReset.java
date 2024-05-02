package Database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseReset {

    private static Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public static void resetDatabase() {
        try {
            disableForeignKeyChecks();
            dropTables();
            enableForeignKeyChecks();
            System.out.println("Database has been reset successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to reset the database.");
        }
    }

    private static void dropTables() throws SQLException {
        String fetchTablesSql = """
            SELECT CONCAT('DROP TABLE IF EXISTS ', table_name, ';') as dropCommand
            FROM information_schema.tables
            WHERE table_schema = 'quackstagram';
        """;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(fetchTablesSql)) {
            while (rs.next()) {
                String dropTableSql = rs.getString("dropCommand");
                executeUpdate(dropTableSql);
            }
        }
    }

    private static void disableForeignKeyChecks() throws SQLException {
        String sql = "SET FOREIGN_KEY_CHECKS = 0;";
        executeUpdate(sql);
    }

    private static void enableForeignKeyChecks() throws SQLException {
        String sql = "SET FOREIGN_KEY_CHECKS = 1;";
        executeUpdate(sql);
    }

    private static void executeUpdate(String sql) throws SQLException {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    public static void main(String[] args) {
        resetDatabase();
    }
}
