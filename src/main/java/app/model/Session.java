package app.model;

import java.time.LocalDateTime;

/**
 * Session represents a user session on sharespace.
 */
public class Session {
    private int id;
    private int userId;
    private LocalDateTime createdAt;

    public Session(int id, int userId, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
