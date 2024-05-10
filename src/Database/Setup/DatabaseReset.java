package Database.Setup;

import Database.Connection.DatabaseConnection;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseReset {

    private static Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public static void resetDatabase() {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            disableForeignKeyChecks(conn);
            dropTables(conn);
            enableForeignKeyChecks(conn);

            conn.commit();
            System.out.println("Database has been reset successfully");
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();  // Rollback changes in case of an error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            System.out.println("Failed to reset the database.");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void disableForeignKeyChecks(Connection conn) throws SQLException {
        executeUpdate(conn, "SET FOREIGN_KEY_CHECKS = 0;");
    }

    private static void enableForeignKeyChecks(Connection conn) throws SQLException {
        executeUpdate(conn, "SET FOREIGN_KEY_CHECKS = 1;");
    }

    private static void dropTables(Connection conn) throws SQLException {
        String fetchTablesSql = """
        SELECT CONCAT('DROP TABLE IF EXISTS ', table_name, ';') as dropCommand
        FROM information_schema.tables
        WHERE table_schema = 'quackstagram';
    """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(fetchTablesSql)) {
            while (rs.next()) {
                String dropTableSql = rs.getString("dropCommand");
                executeUpdate(conn, dropTableSql);
            }
        }
    }

    private static void executeUpdate(Connection conn, String sql) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    public static void main(String[] args) {
        resetDatabase();
    }
}
