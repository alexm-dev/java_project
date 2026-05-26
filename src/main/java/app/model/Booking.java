package app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a booking made by a renter for an asset.
 */
public class Booking {
    private int id;
    private int assetId;
    private int renterId;
    private LocalDate startTime;
    private LocalDate endTime;
    private String status;
    private double totalCost;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    /**
     * Constructor to load from the DB.
     */
    public Booking(int id, int assetId, int renterId, LocalDate startTime, LocalDate endTime,
                   String status, double totalCost, LocalDateTime createdTime, LocalDateTime updatedTime) {
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
     * Constructor to create a new Booking (id and createdTime are set by the DB).
     */
    public Booking(int assetId, int renterId, LocalDate startTime, LocalDate endTime,
                   String status, double totalCost) {
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
    public LocalDate getStartTime() { return startTime; }
    public LocalDate getEndTime() { return endTime; }
    public String getStatus() { return status; }
    public double getTotalCost() { return totalCost; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public LocalDateTime getUpdatedTime() { return updatedTime; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setStatus(String status) { this.status = status; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
}
