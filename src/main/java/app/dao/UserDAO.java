package app.dao;

import app.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User entity.
 * Provides methods to create, find, update, and delete users in the database.
 * Also includes methods to find users by email and username.
 * Maps ResultSet rows to User objects
 */
public class UserDAO extends BaseDAO<User, Integer> {

    /**
     * Creates a new user in the database.
     * The generated id is written back onto the user via its setter.
     *
     * @param user The user to be created.
     * @return true if the user was created successfully, false otherwise.
     */
    @Override
    public boolean create(User user) {
        String sql = "INSERT INTO users (username, email, password_hash, status) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());
            stmt.setString(4, user.getStatus());
            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) user.setId(keys.getInt(1));
                }
            }
            return success;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create user", e);
        }
    }

    /**
     * Finds a user by their ID.
     *
     * @param id The id of the user to be found.
     * @return The user if found, null otherwise.
     * */
    @Override
    public User findById(Integer id) {
        String sql = "SELECT id, username, email, password_hash, created_time, status FROM users WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by id", e);
        }
        return null;
    }

    /**
     * Finds all users in the database.
     *
     * @return A list of all users in the database.
     */
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, email, password_hash, created_time, status FROM users";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) users.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all users", e);
        }
        return users;
    }

    /**
     * Updates an existing user in the database.
     *
     * @param user The user object with updated information.
     * @return true if the user was updated successfully, false otherwise.
     */
    @Override
    public boolean update(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, password_hash = ?, status = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());
            stmt.setString(4, user.getStatus());
            stmt.setInt(5, user.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The id of the user to be deleted.
     * @return true if the user was deleted successfully, false otherwise.
     */
    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    /**
     * Finds a user by their set email.
     *
     * @param email The email of the user to be found.
     * @return The user if found, null otherwise.
     */
    public User findByEmail(String email) {
        String sql = "SELECT id, username, email, password_hash, created_time, status FROM users WHERE email = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by email", e);
        }
        return null;
    }

    /**
     * Finds a user by their set username.
     *
     * @param username The username of the user to be found.
     * @return The user if found, null otherwise.
     */
    public User findByUsername(String username) {
        String sql = "SELECT id, username, email, password_hash, created_time, status FROM users WHERE username = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by username", e);
        }
        return null;
    }

    /**
     * Maps a ResultSet row to a User object.
     *
     * @param rs The ResultSet containing the user data.
     * @return A User object populated with data from the ResultSet.
     * @throws SQLException If an SQL error occurs while accessing the ResultSet.
     */
    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("password_hash"),
            rs.getString("created_time"),
            rs.getString("status")
        );
    }
}
