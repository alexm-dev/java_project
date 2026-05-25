package app.model;

/**
 * Represents a booking made by a renter for an asset.
 *
 * Each booking has an associated asset, renter, start and end time, status, total cost,
 * and timestamps for when the booking was created and last updated.
 *
 * The Booking class provides constructors for creating new bookings and getters/setters
 */
public class Booking {
    private int id;
    private int assetId;
    private int renterId;
    private String startTime;
    private String endTime;
    private String status;
    private double totalCost;
    private String createdTime;
    private String updatedTime;

    /**
     * Constructor for creating a Booking with an ID (used when retrieving from the database).
     */
    public Booking(int id, int assetId, int renterId, String startTime, String endTime, String status, double totalCost, String createdTime, String updatedTime) {
        this.id = id;
        this.assetId = assetId;
        this.renterId = renterId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.totalCost = totalCost;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    /**
     * Constructor for creating a new Booking without an ID (used when inserting into the database).
     */
    public Booking(int assetId, int renterId, String startTime, String endTime, String status, double totalCost) {
        this.assetId = assetId;
        this.renterId = renterId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.totalCost = totalCost;
    }

    // Getters
    public int getId() { return id; }
    public int getAssetId() { return assetId; }
    public int getRenterId() { return renterId; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getStatus() { return status; }
    public double getTotalCost() { return totalCost; }
    public String getCreatedTime() { return createdTime; }
    public String getUpdatedTime() { return updatedTime; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setStatus(String status) { this.status = status; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    public void setUpdatedTime(String updatedTime) { this.updatedTime = updatedTime; }
}
