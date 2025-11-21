package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DBConnection {

    private static Connection conn = null;

    private static final String DB_URL = "jdbc:sqlite:campuscarpool.db";

    // Create or return existing DB connection
    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
        return conn;
    }

    // Execute SELECT queries
    public static ResultSet executeQuery(String sql) {
        try {
            Statement stmt = getConnection().createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    // Execute INSERT/UPDATE/DELETE
    public static int executeUpdate(String sql) {
        try {
            Statement stmt = getConnection().createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return 0;
        }
    }

    // Close DB safely
    public static void closeConnection() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Close connection failed: " + e.getMessage());
        }
    }
}
