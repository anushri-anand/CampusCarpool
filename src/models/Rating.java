package models;

import java.time.LocalDateTime;

/**
 * Rating class representing feedback between users.
 * Demonstrates: Encapsulation, Associations, Composition, Validation
 */
public class Rating {

    private int ratingId;
    private User fromUser;
    private User toUser;
    private Ride ride;
    private int score; // 1–5
    private String comment;
    private LocalDateTime timestamp;

    // Constructors

    public Rating() {
        this.timestamp = LocalDateTime.now();
    }

    public Rating(int ratingId, User fromUser, User toUser, Ride ride, int score, String comment) {
        this.ratingId = ratingId;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.ride = ride;
        setScore(score);
        this.comment = comment;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters

    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public int getScore() {
        return score;
    }

    /** Validates rating score between 1 and 5 */
    public void setScore(int score) {
        if (score < 1 || score > 5)
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Business Logic

    /** Allow updating a rating */
    public void updateScore(int newScore) {
        setScore(newScore);
    }

    // Overrides

    @Override
    public String toString() {
        return "Rating{" +
                "ratingId=" + ratingId +
                ", fromUser='" + (fromUser != null ? fromUser.getName() : "N/A") + '\'' +
                ", toUser='" + (toUser != null ? toUser.getName() : "N/A") + '\'' +
                ", rideId=" + (ride != null ? ride.getId() : "N/A") +
                ", score=" + score +
                ", comment='" + comment + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
