package app;

import app.database.Database;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public final class TestDB {
    private TestDB() {
    }

    public static void run() throws Exception {
        String schemaSql = Files.readString(Path.of("database", "schema.sql"));
        String username = "test_user";
        String email = "test_user@sharespace.com";

        try (Connection conn = Database.getConnection();
                Statement stmt = conn.createStatement()) {
            for (String sqlPart : schemaSql.split(";")) {
                String sql = sqlPart.trim();
                if (!sql.isEmpty()) {
                    stmt.execute(sql);
                }
            }

            try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM users WHERE email = ?")) {
                deleteStmt.setString(1, email);
                deleteStmt.executeUpdate();
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO users(username, email, password_hash, status) VALUES (?, ?, ?, ?)")) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, email);
                insertStmt.setString(3, "demo_hash");
                insertStmt.setString(4, "active");
                insertStmt.executeUpdate();
            }

            try (PreparedStatement readStmt = conn
                    .prepareStatement("SELECT username, email, status, created_time FROM users WHERE email = ?")) {
                readStmt.setString(1, email);

                try (ResultSet rs = readStmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalStateException("Inserted test user was not found");
                    }

                    System.out.println("Connection + user create/read SUCCESS");
                    System.out.println("username: " + rs.getString("username"));
                    System.out.println("email: " + rs.getString("email"));
                    System.out.println("status: " + rs.getString("status"));
                    System.out.println("created_time: " + rs.getString("created_time"));
                }
            }

        }
    }
}
