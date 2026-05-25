package app.model;

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
    public void setStatus(String status) { this.status = status; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    public void setUpdatedTime(String updatedTime) { this.updatedTime = updatedTime; }
}
