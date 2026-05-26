package app.dao;

import app.model.UserRole;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the UserRole entity.
 * UserRole has a composite primary key consisting of user_id and role_id and no single id.
 *
 * The inherited findById and delete method is not applicable for this entity, 
 * so it should be overridden to throw an UnsupportedOperationException.
 */
public class UserRoleDAO extends BaseDAO<UserRole, Integer> {

    /**
     * The columns of the user_roles tables to select for findById and findAll
     */
    private static final String[] COLUMNS = { "user_id", "role_id" };

    /** The name of the database table this DAO manages. */
    @Override
    protected String tableName() {
        return "user_roles";
    }

    /** The columns to select for findById and findAll, in mapRow order. */
    @Override
    protected String[] selectColumns() {
        return COLUMNS;
    }

    /**
     * Maps a ResultSet row to a UserRole object.
     *
     * @param rs The ResultSet to map.
     * @return A UserRole object representing the current row of the ResultSet.
     * @throws SQLException If an SQL error occurs while mapping the row.
     */
    @Override
    protected UserRole mapRow(ResultSet rs) throws SQLException {
        return new UserRole(rs.getInt("user_id"), rs.getInt("role_id"));
    }

    /**
     * Creates a new UserRole in the database.
     *
     * @param userRole The UserRole to create.
     * @return true if the UserRole was created successfully, false otherwise.
     */
    @Override
    public boolean create(UserRole userRole) {
        String sql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userRole.getUserId());
            stmt.setInt(2, userRole.getRoleId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create UserRole", e);
        }
    }

    /**
     * Unsopported operation. UserRole has a composite primary key and cannot be updated by the application.
     * Delete and recreate instead.
     */
    @Override
    public boolean update(UserRole userRole) {
        throw new UnsupportedOperationException("Update operation is not supported for UserRole");
    }

    /**
     * Unsupported operation. UserRole has a composite primary key and cannot be retrieved by a single id.
     * Use findByUserId(int userId) instead to retrieve all roles for a given user.
     */
    @Override
    public UserRole findById(Integer id) {
        throw new UnsupportedOperationException("findById operation is not supported for UserRole");
    }

    /**
     * Unsupported operation. UserRole has a composite primary key and cannot be deleted by a single id.
     * Use delete(int userId, int roleId) instead.
     */
    @Override
    public boolean delete(Integer id) {
        throw new UnsupportedOperationException("delete operation is not supported for UserRole");
    }

    /**
     * Deletes a UserRole from the database by userId and roleId.
     *
     * @param userId The userId of the UserRole to delete.
     * @param roleId The roleId of the UserRole to delete.
     * @return true if the UserRole was deleted successfully, false otherwise.
     */
    public boolean delete(int userId, int roleId) {
        String sql = "DELETE FROM user_roles WHERE user_id = ? AND role_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, userId);
            stmt.setInt(2, roleId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete UserRole", e);
        }
    }

    /**
     * Finds all UserRoles for a given userId.
     *
     * @param userId The userId to find UserRoles for.
     * @return A list of UserRole objects representing the roles of the specified user.
     */
    public List<UserRole> findByUserId(int userId) {
        List<UserRole> list = new ArrayList<>();
        String sql = "SELECT user_id, role_id FROM user_roles WHERE user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find UserRoles by userId", e);
        }

        return list;
    }
}
