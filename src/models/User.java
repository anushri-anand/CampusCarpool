package models;

import java.time.LocalDateTime;


public class User {

    private int id;
    private String name;
    private String rollNumber;
    private String email;
    private String password;
    private String role;
    private int warnings;
    private LocalDateTime blacklistUntil;
    private double rating; 
    private int ratingCount; 
    private int totalRatings; 

    public User() {
        this.warnings = 0;
        this.blacklistUntil = null;
        this.rating = 0.0;
        this.ratingCount = 0;
        this.totalRatings = 0;
    }


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
    public int getRatingCount() {
        return ratingCount;
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


    public boolean isBlacklisted() {
        if (blacklistUntil == null) {
            return false;
        }
        return LocalDateTime.now().isBefore(blacklistUntil);
    }

    public void addWarning() {
        this.warnings++;
        
        final int WARNING_THRESHOLD = 3;
        if (this.warnings >= WARNING_THRESHOLD) {
            triggerBlacklist();
        }
    }

    private void triggerBlacklist() {
        final int BLACKLIST_DAYS = 7;
        this.blacklistUntil = LocalDateTime.now().plusDays(BLACKLIST_DAYS);
    }

    public void updateRating(double newRating) {
 
        if (newRating < 1.0 || newRating > 5.0) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        
    
        double totalScore = this.rating * this.totalRatings;
        this.totalRatings++;
        this.rating = (totalScore + newRating) / this.totalRatings;
    }

    public void clearWarnings() {
        this.warnings = 0;
        this.blacklistUntil = null;
    }


    public boolean canPerformAction() {
        return !isBlacklisted();
    }

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
   public void addRating(double newRating) {
        // Update average rating
        rating = ((rating * ratingCount) + newRating) / (ratingCount + 1);
        ratingCount++;
    }
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


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id == user.id && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return id * 31 + email.hashCode();
    }
    public boolean isDriver() {
    return "DRIVER".equals(role) || "BOTH".equals(role);
}

public boolean isPassenger() {
    return "PASSENGER".equals(role) || "BOTH".equals(role);
}

}
