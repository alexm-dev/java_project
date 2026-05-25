package app.model;

/**
 * Session represents a user session on sharespace.
 * This class stores the session ID, the user ID associated with the session,
 * and the timestamp of when the session was created.
 * Is used to manage authentication of users on sharespace.
 */
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
