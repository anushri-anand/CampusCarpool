package models;

/**
 * Destination class representing ride endpoints.
 * Demonstrates: Encapsulation, Clean Modeling, Future Scalability
 */
public class Destination {

    private int destinationId;
    private String name;
    private String description;
    private double latitude;
    private double longitude;

    // Constructors

    public Destination() {}

    public Destination(int destinationId, String name, String description) {
        this.destinationId = destinationId;
        this.name = name;
        this.description = description;
    }

    public Destination(int destinationId, String name, String description,
                       double latitude, double longitude) {
        this.destinationId = destinationId;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // Overrides

    @Override
    public String toString() {
        return "Destination{" +
                "id=" + destinationId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", lat=" + latitude +
                ", long=" + longitude +
                '}';
    }
}
