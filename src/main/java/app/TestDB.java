package app;

import app.database.Database;
import app.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class TestDB {
    private TestDB() {
    }

    public static void run() throws Exception {
        User user = new User("max mustermann", "max.mustermann@sharespace.com", "passwordHash");
        user.setStatus("active");

        Connection conn = Database.getConnection();

        try (PreparedStatement deleteStmt = conn.prepareStatement(
                "DELETE FROM users WHERE email = ?")) {
            deleteStmt.setString(1, user.getEmail());
            deleteStmt.executeUpdate();
        }

        try (PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO users (username, email, password_hash, status) VALUES (?, ?, ?, ?)")) {
            insertStmt.setString(1, user.getUsername());
            insertStmt.setString(2, user.getEmail());
            insertStmt.setString(3, user.getPasswordHash());
            insertStmt.setString(4, user.getStatus());
            insertStmt.executeUpdate();
        }

        try (PreparedStatement readStmt = conn.prepareStatement(
                "SELECT id, username, email, password_hash, created_time, status FROM users WHERE email = ?")) {
            readStmt.setString(1, user.getEmail());

            try (ResultSet rs = readStmt.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalStateException("Inserted test user was not found");
                }

                User loaded = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password_hash"),
                    rs.getString("created_time"),
                    rs.getString("status")
                );

                System.out.println("=== TestDB: User model round-trip ===");
                System.out.println("id:           " + loaded.getId());
                System.out.println("username:     " + loaded.getUsername());
                System.out.println("email:        " + loaded.getEmail());
                System.out.println("status:       " + loaded.getStatus());
                System.out.println("created_time: " + loaded.getCreatedTime());
            }
        }
    }
}
