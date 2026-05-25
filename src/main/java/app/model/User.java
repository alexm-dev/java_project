package app.model;

/**
 * User model representing the User table in the database
 * Fields:
 * - id: int (primary key, auto-increment)
 * - username: String (unique, not null)
 * - email: String (unique, not null)
 * - passwordHash: String (not null)
 * - createdTime: String (timestamp, not null)
 * - status: String (e.g., "active", "inactive")
 */
public class User {
    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private String createdTime;
    private String status;

    // Constructor to load from DB
    public User(int id, String username, String email, String passwordHash, String createdTime, String status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdTime = createdTime;
        this.status = status;
    }

    // Constructor to create a new User in the DB (generates id, createdTime)
    public User(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getCreatedTime() { return createdTime; }
    public String getStatus() { return status; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setStatus(String status) { this.status = status; }
}
