package Database.IndexesTests;

import Database.Connection.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryPerformanceTester {

    private static final int NUM_RUNS = 10000;

    public static void main(String[] args) {
        String[] queries = {
                "SELECT SQL_NO_CACHE * FROM UserActivity;",
                "SELECT SQL_NO_CACHE * FROM PopularPosts;"
        };

        for (String query : queries) {
            measureQueryPerformance(query, NUM_RUNS);
        }
    }

    private static void measureQueryPerformance(String query, int numRuns) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        long totalDuration = 0;

        try {
            // Get the connection
            connection = DatabaseConnection.getConnection();
            statement = connection.createStatement();

            for (int i = 0; i < numRuns; i++) {
                // Measure the start time
                long startTime = System.nanoTime();

                // Execute the query
                resultSet = statement.executeQuery(query);

                // Measure the end time
                long endTime = System.nanoTime();

                // Calculate the duration for this run
                long duration = (endTime - startTime);
                totalDuration += duration;

                // Close the resultSet to avoid memory issues
                resultSet.close();
            }

            // Calculate the average duration
            long averageDuration = totalDuration / numRuns / 1_000_000; // Convert to milliseconds
            double totalDurationInSeconds = totalDuration / 1_000_000_000.0; // Convert to seconds

            System.out.println("Query: " + query);
            System.out.println("Total Execution Time over " + numRuns + " runs: " + totalDurationInSeconds + " seconds");
            System.out.println("Average Execution Time per run: " + averageDuration + " ms\n");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}