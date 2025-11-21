package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.File;

/**
 * Database connection utility class
 * Handles SQLite database connection and initialization
 */
public class DBConnection {

    private static Connection conn = null;
    private static final String DB_URL = "jdbc:sqlite:campuscarpool.db";

    /**
     * Create or return existing DB connection
     * @return Connection object
     */
    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                // Load SQLite JDBC driver (optional in modern Java, but good practice)
                Class.forName("org.sqlite.JDBC");
                
                conn = DriverManager.getConnection(DB_URL);
                System.out.println("Database connected successfully!");
                
                // Enable foreign keys (IMPORTANT for SQLite!)
                enableForeignKeys();
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Enable foreign key constraints in SQLite
     * SQLite has foreign keys disabled by default!
     */
    private static void enableForeignKeys() {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON;");
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Failed to enable foreign keys: " + e.getMessage());
        }
    }

    /**
     * Execute SELECT queries
     * @param sql the SQL SELECT query
     * @return ResultSet containing query results
     */
    public static ResultSet executeQuery(String sql) {
        try {
            Statement stmt = getConnection().createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Query failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Execute INSERT/UPDATE/DELETE queries
     * @param sql the SQL modification query
     * @return number of rows affected, or 0 if failed
     */
    public static int executeUpdate(String sql) {
        try {
            Statement stmt = getConnection().createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Update failed: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Initialize database by running schema.sql
     * Call this on first launch to create all tables
     */
    public static void initializeDatabase() {
        try {
            // Check if database file exists
            File dbFile = new File("campuscarpool.db");
            boolean isFirstRun = !dbFile.exists();
            
            // Get connection (creates file if doesn't exist)
            Connection conn = getConnection();
            
            if (isFirstRun) {
                System.out.println("First run detected. Initializing database...");
                runSchemaScript();
            } else {
                System.out.println("Database already exists.");
            }
            
        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Run the schema.sql script to create tables
     * You can read from file or hardcode the SQL here
     */
    private static void runSchemaScript() {
        try {
            Statement stmt = conn.createStatement();
            
            // Enable foreign keys first
            stmt.execute("PRAGMA foreign_keys = ON;");
            
            // Create users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "roll_number TEXT UNIQUE NOT NULL CHECK(roll_number GLOB '[1-2][0-9][0-9][0-9]A7[PT]S[0-9][0-9][0-9][0-9]U'), " +
                "email TEXT UNIQUE NOT NULL CHECK(email GLOB 'f[1-2][0-9][0-9][0-9]03[0-9][0-9][0-9]@dubai.bits-pilani.ac.in'), " +
                "password TEXT NOT NULL, " +
                "role TEXT NOT NULL CHECK(role IN ('DRIVER', 'PASSENGER', 'BOTH')), " +
                "warnings INTEGER DEFAULT 0, " +
                "blacklist_until TEXT, " +
                "rating REAL DEFAULT 0.0, " +
                "total_ratings INTEGER DEFAULT 0, " +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP)");
            
            // Create drivers table
            stmt.execute("CREATE TABLE IF NOT EXISTS drivers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER UNIQUE NOT NULL, " +
                "license_number TEXT NOT NULL, " +
                "vehicle_model TEXT NOT NULL, " +
                "vehicle_number TEXT NOT NULL, " +
                "seats_available INTEGER NOT NULL CHECK(seats_available > 0), " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)");
            
            // Create passengers table
            stmt.execute("CREATE TABLE IF NOT EXISTS passengers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER UNIQUE NOT NULL, " +
                "preferred_destination TEXT, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)");
            
            // Create destinations table
            stmt.execute("CREATE TABLE IF NOT EXISTS destinations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT UNIQUE NOT NULL, " +
                "description TEXT, " +
                "latitude REAL, " +
                "longitude REAL, " +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP)");
            
            // Create rides table
            stmt.execute("CREATE TABLE IF NOT EXISTS rides (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "driver_id INTEGER NOT NULL, " +
                "driver_name TEXT NOT NULL, " +
                "origin TEXT NOT NULL, " +
                "destination TEXT NOT NULL, " +
                "departure_date TEXT NOT NULL, " +
                "departure_time TEXT NOT NULL, " +
                "seats_available INTEGER NOT NULL CHECK(seats_available >= 0), " +
                "seats_total INTEGER NOT NULL CHECK(seats_total > 0), " +
                "price_per_seat REAL DEFAULT 0.0, " +
                "is_women_only INTEGER DEFAULT 0 CHECK(is_women_only IN (0, 1)), " +
                "status TEXT DEFAULT 'ACTIVE' CHECK(status IN ('ACTIVE', 'COMPLETED', 'CANCELLED')), " +
                "vehicle_info TEXT, " +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (driver_id) REFERENCES drivers(user_id) ON DELETE CASCADE)");
            
            // Create ride_requests table
            stmt.execute("CREATE TABLE IF NOT EXISTS ride_requests (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "passenger_id INTEGER NOT NULL, " +
                "passenger_name TEXT NOT NULL, " +
                "origin TEXT NOT NULL, " +
                "destination TEXT NOT NULL, " +
                "preferred_date TEXT NOT NULL, " +
                "preferred_time TEXT NOT NULL, " +
                "seats_requested INTEGER NOT NULL CHECK(seats_requested > 0), " +
                "status TEXT DEFAULT 'PENDING' CHECK(status IN ('PENDING', 'MATCHED', 'CANCELLED')), " +
                "notes TEXT, " +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (passenger_id) REFERENCES passengers(user_id) ON DELETE CASCADE)");
            
            // Create bookings table
            stmt.execute("CREATE TABLE IF NOT EXISTS bookings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ride_id INTEGER, " +
                "passenger_id INTEGER NOT NULL, " +
                "origin TEXT, " +
                "destination TEXT, " +
                "status TEXT DEFAULT 'REQUESTED' CHECK(status IN ('REQUESTED', 'CONFIRMED', 'CANCELLED')), " +
                "seats_booked INTEGER DEFAULT 1, " +
                "timestamp TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (ride_id) REFERENCES rides(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (passenger_id) REFERENCES passengers(user_id) ON DELETE CASCADE, " +
                "UNIQUE(ride_id, passenger_id))");
            
            // Create ratings table
            stmt.execute("CREATE TABLE IF NOT EXISTS ratings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "from_user_id INTEGER NOT NULL, " +
                "to_user_id INTEGER NOT NULL, " +
                "ride_id INTEGER NOT NULL, " +
                "score INTEGER NOT NULL CHECK(score >= 1 AND score <= 5), " +
                "comment TEXT, " +
                "timestamp TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (from_user_id) REFERENCES users(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (to_user_id) REFERENCES users(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (ride_id) REFERENCES rides(id) ON DELETE CASCADE, " +
                "UNIQUE(from_user_id, to_user_id, ride_id))");
            
            // Create reports table
            stmt.execute("CREATE TABLE IF NOT EXISTS reports (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "reported_by INTEGER NOT NULL, " +
                "reported_user INTEGER NOT NULL, " +
                "ride_id INTEGER, " +
                "reason TEXT NOT NULL, " +
                "status TEXT DEFAULT 'PENDING' CHECK(status IN ('PENDING', 'REVIEWED', 'RESOLVED')), " +
                "timestamp TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (reported_by) REFERENCES users(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (reported_user) REFERENCES users(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (ride_id) REFERENCES rides(id) ON DELETE SET NULL)");
            
            // Create indexes
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_users_email ON users(email)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_users_roll_number ON users(roll_number)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_rides_driver ON rides(driver_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_rides_destination ON rides(destination)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_rides_date ON rides(departure_date)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_bookings_ride ON bookings(ride_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_bookings_passenger ON bookings(passenger_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_ratings_to_user ON ratings(to_user_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_reports_reported_user ON reports(reported_user)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_ride_requests_passenger ON ride_requests(passenger_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_ride_requests_status ON ride_requests(status)");
            
            stmt.close();
            
            System.out.println("Database schema created successfully!");
            
        } catch (SQLException e) {
            System.err.println("Failed to create database schema: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Close DB connection safely
     */
    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Close connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Test database connection
     */
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        initializeDatabase();
        closeConnection();
    }
}

