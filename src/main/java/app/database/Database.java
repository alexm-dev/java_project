package app.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database utility class for managing DB connection and initialization.
 * Uses SQLite for simplicity and jdbc for DB driver.
 */
public class Database {

    // v1 relative path (dev/submission, run from project root)
    private static final String URL = "jdbc:sqlite:database/sharespace.db";

    // v2 persistent user data directory
    // private static final String DB_DIR = System.getProperty("user.home") + java.io.File.separator + "ShareSpace";
    // private static final String URL = "jdbc:sqlite:" + DB_DIR + java.io.File.separator + "sharespace.db";

    private static Connection connection;

    /**
     * Initialize method to set up databse schema from schema.sql.
     * Is called from Main.java at application startup.
     */
    public static void initialize() {
        Connection conn = getConnection();
        try (InputStream is = Database.class.getResourceAsStream("/schema.sql")) {
            if (is == null) throw new RuntimeException("schema.sql not found in classpath");
            String sql = new String(is.readAllBytes());
            for (String statement : sql.split(";")) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty()) {
                    conn.createStatement().execute(trimmed);
                }
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    /**
     * getConnection method to provide a singleton DB connection.
     */
    public static Connection getConnection() {
        if (connection == null) {
            // v1 ensure the database/ directory exists before SQLite tries to create the file
            new java.io.File("database").mkdirs();
            // v2 create the directory before connecting (only needed with user.home path)
            // new java.io.File(DB_DIR).mkdirs();
            try {
                connection = DriverManager.getConnection(URL);
                connection.createStatement().execute("PRAGMA foreign_keys = ON");
            } catch (SQLException e) {
                throw new RuntimeException("Database connection failed", e);
            }
        }
        return connection;
    }
}
