package app.dao;

import app.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for the single-row Session table.
 *
 * Unlike the other DAOs, SessionDAO does not extend BaseDAO because the
 * sessions table holds at most one row (id = 1) and standard CRUD does not
 * apply. The methods here let SessionService persist who was last logged in
 * so the app can restore the session on the next startup.
 */
// TODO: SessionService.restoreSession() at startup will call getActiveUserId() and load the User from UserDAO
public class SessionDAO {

    /** The id used for the single session row. The schema enforces this with CHECK (id = 1). */
    private static final int SESSION_ID = 1;

    private final Connection conn;

    public SessionDAO() {
        this.conn = Database.getConnection();
    }

    /**
     * Saves the active user. If a session row already exists it is replaced.
     *
     * @param userId the id of the user that just logged in
     * @return true if the row was inserted or replaced, false otherwise
     */
    public boolean save(int userId) {
        String sql = "INSERT OR REPLACE INTO sessions (id, user_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, SESSION_ID);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save session", e);
        }
    }

    /**
     * Returns the user id of the active session, or -1 if no session is stored.
     * Used by SessionService.restoreSession at app startup.
     *
     * @return the active user id, or -1 if no session is currently stored
     */
    public int getActiveUserId() {
        String sql = "SELECT user_id FROM sessions WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, SESSION_ID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read active session", e);
        }
        return -1;
    }

    /**
     * Clears the stored session (logout).
     *
     * @return true if a session row existed and was removed, false if none was stored
     */
    public boolean clear() {
        String sql = "DELETE FROM sessions WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, SESSION_ID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear session", e);
        }
    }
}
