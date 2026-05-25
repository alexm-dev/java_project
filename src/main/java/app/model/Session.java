package app.model;

public class Session {
    private int id;
    private int userId;
    private String createdAt;

    public Session(int id, int userId, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getCreatedAt() { return createdAt; }
}
