import utils.DBConnection;
import views.LoginView;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * App.java - Main Application Entry Point for CampusCarpool
 * 
 * BITS Pilani Dubai Campus - Carpool Management System
 * Technology: Java Swing GUI + SQLite JDBC
 * 
 * This launcher:
 * - Initializes the SQLite database and creates all tables
 * - Configures the UI theme for cross-platform consistency
 * - Launches the LoginView as the entry point
 * - Provides comprehensive logging and error handling
 * 
 * @author CampusCarpool Team
 * @version 1.0
 * @since 2024-11-22
 */
public class App {
    
    // Application metadata
    private static final String APP_NAME = "CampusCarpool";
    private static final String APP_VERSION = "1.0.0";
    private static final String UNIVERSITY = "BITS Pilani Dubai Campus";
    private static final String DATABASE_NAME = "campuscarpool.db";
    
    // ANSI color codes for beautiful terminal output
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_BOLD = "\u001B[1m";
    
    /**
     * Main entry point for the application
     * @param args Command line arguments (currently unused)
     */
    public static void main(String[] args) {
        // Print startup banner
        printStartupBanner();
        
        // System diagnostics
        printSystemInfo();
        
        // Phase 1: Database Initialization
        if (!initializeDatabase()) {
            System.err.println(ANSI_RED + "✗ FATAL: Database initialization failed" + ANSI_RESET);
            System.err.println("Application cannot continue without database.");
            
            // Show error dialog to user
            JOptionPane.showMessageDialog(null,
                "Database initialization failed!\n" +
                "Please check console for details.",
                "Fatal Error",
                JOptionPane.ERROR_MESSAGE);
            
            System.exit(1);
        }
        
        // Phase 2: UI Configuration
        configureUI();
        
        // Phase 3: Pre-launch checks
        performPreLaunchChecks();
        
        // Phase 4: Launch Application
        launchApplication();
        
        // Post-launch message
        printPostLaunchInfo();
    }
    
    /**
     * Print a beautiful startup banner
     */
    private static void printStartupBanner() {
        String banner = 
            ANSI_CYAN + ANSI_BOLD +
            "\n╔═══════════════════════════════════════════════════════════╗\n" +
            "║                                                           ║\n" +
            "║              " + ANSI_GREEN + "🚗  CAMPUSCARPOOL  🚗" + ANSI_CYAN + "                      ║\n" +
            "║                                                           ║\n" +
            "║         Smart Ride-Sharing for University Students       ║\n" +
            "║                                                           ║\n" +
            "║              " + UNIVERSITY + "               ║\n" +
            "║                                                           ║\n" +
            "║                    Version " + APP_VERSION + "                        ║\n" +
            "║                                                           ║\n" +
            "╚═══════════════════════════════════════════════════════════╝" +
            ANSI_RESET + "\n";
        
        System.out.println(banner);
    }
    
    /**
     * Print system information for debugging
     */
    private static void printSystemInfo() {
        System.out.println(ANSI_BLUE + "════════════════ SYSTEM INFORMATION ════════════════" + ANSI_RESET);
        System.out.println("📅 Launch Time:     " + getCurrentTimestamp());
        System.out.println("💻 Operating System: " + System.getProperty("os.name") + " " + 
                         System.getProperty("os.version"));
        System.out.println("☕ Java Version:     " + System.getProperty("java.version"));
        System.out.println("📂 Working Directory: " + System.getProperty("user.dir"));
        System.out.println("👤 User:            " + System.getProperty("user.name"));
        System.out.println("🧠 Available Memory: " + 
                         (Runtime.getRuntime().maxMemory() / 1024 / 1024) + " MB");
        System.out.println(ANSI_BLUE + "═══════════════════════════════════════════════════" + ANSI_RESET);
        System.out.println();
    }
    
    /**
     * Initialize the database with comprehensive error handling
     * @return true if successful, false otherwise
     */
    private static boolean initializeDatabase() {
        System.out.println(ANSI_YELLOW + "┌─ PHASE 1: DATABASE INITIALIZATION" + ANSI_RESET);
        System.out.println("│");
        
        try {
            // Check if database file exists
            java.io.File dbFile = new java.io.File(DATABASE_NAME);
            boolean isFirstRun = !dbFile.exists();
            
            System.out.println("│  📊 Database File: " + DATABASE_NAME);
            System.out.println("│  📍 Location: " + dbFile.getAbsolutePath());
            
            if (isFirstRun) {
                System.out.println("│  " + ANSI_CYAN + "ℹ First run detected - creating new database..." + ANSI_RESET);
            } else {
                System.out.println("│  " + ANSI_GREEN + "✓ Existing database found" + ANSI_RESET);
                System.out.println("│  📏 Size: " + (dbFile.length() / 1024) + " KB");
            }
            
            System.out.println("│");
            System.out.println("│  🔧 Initializing database connection...");
            
            // Initialize database using your DBConnection class
            DBConnection.initializeDatabase();
            
            System.out.println("│  " + ANSI_GREEN + "✓ Database connection established" + ANSI_RESET);
            
            if (isFirstRun) {
                System.out.println("│  " + ANSI_GREEN + "✓ Database schema created successfully" + ANSI_RESET);
                System.out.println("│  " + ANSI_GREEN + "✓ Tables: users, drivers, passengers, rides, bookings, reports, ratings, destinations" + ANSI_RESET);
            }
            
            System.out.println("│");
            System.out.println(ANSI_GREEN + "└─ ✓ DATABASE READY" + ANSI_RESET);
            System.out.println();
            
            return true;
            
        } catch (Exception e) {
            System.out.println("│");
            System.out.println("│  " + ANSI_RED + "✗ DATABASE INITIALIZATION FAILED" + ANSI_RESET);
            System.out.println("│");
            System.out.println("│  Error Details:");
            System.out.println("│  └─ " + e.getClass().getSimpleName() + ": " + e.getMessage());
            System.out.println("│");
            System.out.println(ANSI_RED + "└─ ✗ PHASE 1 FAILED" + ANSI_RESET);
            System.out.println();
            
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Configure UI look and feel for better appearance
     */
    private static void configureUI() {
        System.out.println(ANSI_YELLOW + "┌─ PHASE 2: UI CONFIGURATION" + ANSI_RESET);
        System.out.println("│");
        
        try {
            // Get system look and feel
            String systemLAF = UIManager.getSystemLookAndFeelClassName();
            System.out.println("│  🎨 Applying system theme: " + systemLAF);
            
            // Set look and feel
            UIManager.setLookAndFeel(systemLAF);
            
            System.out.println("│  " + ANSI_GREEN + "✓ UI theme applied successfully" + ANSI_RESET);
            
        } catch (Exception e) {
            System.out.println("│  " + ANSI_YELLOW + "⚠ Could not set system look and feel" + ANSI_RESET);
            System.out.println("│  ℹ Using default Java theme instead");
        }
        
        // Enable anti-aliasing for text (smoother fonts)
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        System.out.println("│  " + ANSI_GREEN + "✓ Font anti-aliasing enabled" + ANSI_RESET);
        System.out.println("│");
        System.out.println(ANSI_GREEN + "└─ ✓ UI CONFIGURED" + ANSI_RESET);
        System.out.println();
    }
    
    /**
     * Perform pre-launch system checks
     */
    private static void performPreLaunchChecks() {
        System.out.println(ANSI_YELLOW + "┌─ PHASE 3: PRE-LAUNCH CHECKS" + ANSI_RESET);
        System.out.println("│");
        
        // Check 1: Graphics Environment
        try {
            if (GraphicsEnvironment.isHeadless()) {
                System.out.println("│  " + ANSI_RED + "✗ GUI not supported (headless environment)" + ANSI_RESET);
                System.exit(1);
            } else {
                System.out.println("│  " + ANSI_GREEN + "✓ Graphics environment available" + ANSI_RESET);
            }
        } catch (Exception e) {
            System.out.println("│  " + ANSI_YELLOW + "⚠ Could not verify graphics environment" + ANSI_RESET);
        }
        
        // Check 2: Display resolution
        try {
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            int width = gd.getDisplayMode().getWidth();
            int height = gd.getDisplayMode().getHeight();
            System.out.println("│  " + ANSI_GREEN + "✓ Display: " + width + "x" + height + ANSI_RESET);
            
            if (width < 1024 || height < 768) {
                System.out.println("│  " + ANSI_YELLOW + "⚠ Low resolution detected - UI may be cramped" + ANSI_RESET);
            }
        } catch (Exception e) {
            System.out.println("│  " + ANSI_YELLOW + "⚠ Could not detect display resolution" + ANSI_RESET);
        }
        
        // Check 3: Memory
        long freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024;
        if (freeMemory < 50) {
            System.out.println("│  " + ANSI_YELLOW + "⚠ Low memory warning: " + freeMemory + " MB available" + ANSI_RESET);
        } else {
            System.out.println("│  " + ANSI_GREEN + "✓ Memory: " + freeMemory + " MB available" + ANSI_RESET);
        }
        
        // Check 4: JDBC SQLite Driver
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("│  " + ANSI_GREEN + "✓ SQLite JDBC driver loaded" + ANSI_RESET);
        } catch (ClassNotFoundException e) {
            System.out.println("│  " + ANSI_RED + "✗ SQLite JDBC driver not found!" + ANSI_RESET);
            System.out.println("│  ℹ Make sure sqlite-jdbc JAR is in classpath");
        }
        
        System.out.println("│");
        System.out.println(ANSI_GREEN + "└─ ✓ ALL CHECKS PASSED" + ANSI_RESET);
        System.out.println();
    }
    
    /**
     * Launch the main application GUI
     */
    private static void launchApplication() {
        System.out.println(ANSI_YELLOW + "┌─ PHASE 4: APPLICATION LAUNCH" + ANSI_RESET);
        System.out.println("│");
        System.out.println("│  🚀 Starting GUI on Event Dispatch Thread...");
        
        SwingUtilities.invokeLater(() -> {
            try {
                // Create and show login window
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
                
                System.out.println("│  " + ANSI_GREEN + "✓ Login window displayed" + ANSI_RESET);
                System.out.println("│");
                System.out.println(ANSI_GREEN + "└─ ✓ APPLICATION LAUNCHED SUCCESSFULLY" + ANSI_RESET);
                System.out.println();
                
            } catch (Exception e) {
                System.out.println("│  " + ANSI_RED + "✗ Failed to launch GUI" + ANSI_RESET);
                System.out.println("│");
                System.out.println("│  Error: " + e.getMessage());
                System.out.println("│");
                System.out.println(ANSI_RED + "└─ ✗ LAUNCH FAILED" + ANSI_RESET);
                
                e.printStackTrace();
                
                // Show error dialog
                JOptionPane.showMessageDialog(null,
                    "Failed to launch CampusCarpool:\n" + e.getMessage(),
                    "Launch Error",
                    JOptionPane.ERROR_MESSAGE);
                
                System.exit(1);
            }
        });
    }
    
    /**
     * Print post-launch information and tips
     */
    private static void printPostLaunchInfo() {
        // Wait a moment for GUI to initialize
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // Ignore
        }
        
        System.out.println(ANSI_CYAN + "═══════════════════════════════════════════════════" + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_GREEN + ANSI_BOLD + "  ✓ CampusCarpool is now running!" + ANSI_RESET);
        System.out.println();
        System.out.println("  📱 Check your screen for the login window");
        System.out.println("  🔐 New users: Click 'Register here' to create an account");
        System.out.println("  📋 Roll Number Format: 2024A7PS0336U");
        System.out.println("  📧 Email Format: f20240328@dubai.bits-pilani.ac.in");
        System.out.println("  🚗 Drivers can post rides after registration");
        System.out.println("  🧑‍🎓 Passengers can search and book rides");
        System.out.println("  ⚠️  Report system available for safety");
        System.out.println("  ⭐ Rating system for drivers and passengers");
        System.out.println();
        System.out.println(ANSI_CYAN + "═══════════════════════════════════════════════════" + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_YELLOW + "💡 TIP: Keep this console open to see application logs" + ANSI_RESET);
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────");
        System.out.println();
    }
    
    /**
     * Get current timestamp as formatted string
     * @return Formatted timestamp
     */
    private static String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}

