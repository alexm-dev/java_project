package app.dao;

import app.model.Role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Role entity.
 * Extends BaseDAO
 * Provides methods to create, find, and list roles in the database.
 */
public class RoleDAO extends BaseDAO<Role, Integer> {

    /**
     * Creates a new role in the database.
     *
     * @param role The role to be created.
     * @return true if the role was created successfully, false otherwise.
     */
    @Override
    public boolean create(Role role) {
        String sql = "INSERT INTO roles (name) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role.getName());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create role", e);
        }
    }

    /**
     * Finds a role by its ID.
     *
     * @param id The id of the role to be found.
     * @return The role if found, null otherwise.
     */
    @Override
    public Role findById(Integer id) {
        String sql = "SELECT id, name FROM roles WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Role(rs.getInt("id"), rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find role by id", e);
        }
        return null;
    }

    /**
     * Finds all roles in the database.
     *
     * @return A list of all roles.
     */
    @Override
    public List<Role> findAll() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT id, name FROM roles";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                roles.add(new Role(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all roles", e);
        }
        return roles;
    }

    /**
     * Role does not implement update functionality.
     * Roles are never updated.
     */
    @Override
    public boolean update(Role role) {
        return false;
    }

    /**
     * Role does not implement delete functionality.
     * Roles are never deleted.
     */
    @Override
    public boolean delete(Integer id) {
        return false;
    }

    /**
     * Finds a role by its name.
     *
     * @param name The name of the role to be found.
     * @return The role if found, null otherwise.
     */
    public Role findByName(String name) {
        String sql = "SELECT id, name FROM roles WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Role(rs.getInt("id"), rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find role by name", e);
        }
        return null;
    }
}
