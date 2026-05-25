package app.dao;

import app.model.Role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for the Role entity.
 *
 * Roles are seeded reference data ('lender', 'renter') and are never updated
 * or deleted by the application. The inherited update and delete methods
 * are overridden to return false.
 */
public class RoleDAO extends BaseDAO<Role, Integer> {

    /** The columns to select for findById and findAll, in mapRow order. */
    private static final String[] COLUMNS = { "id", "name" };

    /** The name of the database table this DAO manages. */
    @Override
    protected String tableName() {
        return "roles";
    }

    /** The columns to select for findById and findAll, in mapRow order. */
    @Override
    protected String[] selectColumns() {
        return COLUMNS;
    }


    /**
     * Maps a ResultSet row to a Role object.
     *
     * @param rs The ResultSet to map.
     * @return A Role object representing the current row of the ResultSet.
     */
    @Override
    protected Role mapRow(ResultSet rs) throws SQLException {
        return new Role(rs.getInt("id"), rs.getString("name"));
    }

    /**
     * Crates a new role in the database.
     *
     * @param role The role to create. The generated id will be set back on this object.
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
     * Not implemented. Roles are seeded data and cannot be updated by the application.
     */
    @Override
    public boolean update(Role role) {
        return false;
    }

    /**
     * Not implemented. Roles are seeded data and cannot be deleted by the application.
     */
    @Override
    public boolean delete(Integer id) {
        return false;
    }

    /**
     * Finds a role by its name.
      *
      * @param name the name of the role to find
      * @return the Role with the given name, or null if not found
     */
    public Role findByName(String name) {
        String sql = "SELECT id, name FROM roles WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find role by name", e);
        }
        return null;
    }
}
