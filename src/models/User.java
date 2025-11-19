package models;

import java.time.LocalDateTime;

/**
 * Base class representing a generic campus user (driver, passenger, or both).
 * Demonstrates: Encapsulation, Inheritance (superclass), Polymorphism
 */
public class User {
    // Private fields - Encapsulation
    private int id;
    private String name;
    private String rollNumber;
    private String email;
    private String password;
    private String role; // "DRIVER", "PASSENGER", or "BOTH"
    private int warnings;
    private LocalDateTime blacklistUntil;
    private double rating; // Average rating
    private int totalRatings; // Count of ratings received

    // Constructors

    /**
     * Default constructor
     */
    public User() {
        this.warnings = 0;
        this.blacklistUntil = null;
        this.rating = 0.0;
        this.totalRatings = 0;
    }

    /**
     * Constructor with essential fields
     * Constructor Overloading
     */
    public User(String name, String rollNumber, String email, String password, String role) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.email = email;
        this.password = password;
        this.role = role;
        this.warnings = 0;
        this.blacklistUntil = null;
        this.rating = 0.0;
        this.totalRatings = 0;
    }

    /**
     * Full constructor including ID (for database retrieval)
     * Constructor Overloading
     */
    public User(int id, String name, String rollNumber, String email, String password, 
                String role, int warnings, LocalDateTime blacklistUntil, double rating, int totalRatings) {
        this.id = id;
        this.name = name;
        this.rollNumber = rollNumber;
        this.email = email;
        this.password = password;
        this.role = role;
        this.warnings = warnings;
        this.blacklistUntil = blacklistUntil;
        this.rating = rating;
        this.totalRatings = totalRatings;
    }

    // Getters and Setters - Encapsulation

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getWarnings() {
        return warnings;
    }

    public void setWarnings(int warnings) {
        this.warnings = warnings;
    }

    public LocalDateTime getBlacklistUntil() {
        return blacklistUntil;
    }

    public void setBlacklistUntil(LocalDateTime blacklistUntil) {
        this.blacklistUntil = blacklistUntil;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(int totalRatings) {
        this.totalRatings = totalRatings;
    }

    // Business Logic Methods

    /**
     * Checks if the user is currently blacklisted
     * @return true if user is blacklisted, false otherwise
     */
    public boolean isBlacklisted() {
        if (blacklistUntil == null) {
            return false;
        }
        // Check if current time is before blacklist expiration
        return LocalDateTime.now().isBefore(blacklistUntil);
    }

    /**
     * Adds a warning to the user and triggers blacklist if threshold reached
     * Business rule: After 3 warnings, user gets blacklisted for 7 days
     */
    public void addWarning() {
        this.warnings++;
        
        // Check if warnings exceed threshold (e.g., 3 warnings)
        final int WARNING_THRESHOLD = 3;
        if (this.warnings >= WARNING_THRESHOLD) {
            triggerBlacklist();
        }
    }

    /**
     * Triggers temporary blacklist for the user
     * Default: 7 days from current time
     */
    private void triggerBlacklist() {
        final int BLACKLIST_DAYS = 7;
        this.blacklistUntil = LocalDateTime.now().plusDays(BLACKLIST_DAYS);
    }

    /**
     * Updates the user's average rating
     * @param newRating the new rating to add (1-5)
     */
    public void updateRating(double newRating) {
        // Validate rating range
        if (newRating < 1.0 || newRating > 5.0) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        
        // Calculate new average
        double totalScore = this.rating * this.totalRatings;
        this.totalRatings++;
        this.rating = (totalScore + newRating) / this.totalRatings;
    }

    /**
     * Clears warnings and blacklist (admin or after blacklist period expires)
     */
    public void clearWarnings() {
        this.warnings = 0;
        this.blacklistUntil = null;
    }

    /**
     * Checks if user can perform actions (not blacklisted)
     * @return true if user can act, false if blacklisted
     */
    public boolean canPerformAction() {
        return !isBlacklisted();
    }

    /**
     * Gets user's role as readable string
     * Method Overriding candidate
     */
    public String getRoleDescription() {
        switch (this.role) {
            case "DRIVER":
                return "Driver";
            case "PASSENGER":
                return "Passenger";
            case "BOTH":
                return "Driver & Passenger";
            default:
                return "Unknown";
        }
    }

    // Override toString() for debugging and display
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rollNumber='" + rollNumber + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", warnings=" + warnings +
                ", rating=" + String.format("%.2f", rating) +
                ", totalRatings=" + totalRatings +
                ", blacklisted=" + isBlacklisted() +
                '}';
    }

    // Override equals() for comparing users
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id == user.id && email.equals(user.email);
    }

    // Override hashCode() for collections
    @Override
    public int hashCode() {
        return id * 31 + email.hashCode();
    }
}
