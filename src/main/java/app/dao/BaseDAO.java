package app.dao;

import app.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for Data Access Objects, following the Template Method pattern.
 *
 * Concrete subclasses provide three hooks (tableName, selectColumns, mapRow)
 * and the standard findById, findAll, and delete operations are implemented
 * here generically.
 *
 * Write operations (create and update) remain abstract because their SQL and
 * parameter bindings vary per table.
 *
 * @param <T>  the entity type this DAO manages
 * @param <ID> the entity's primary key type
 */
public abstract class BaseDAO<T, ID> {

    /** The database connection shared by all DAO operations. */
    protected final Connection conn;

    public BaseDAO() {
        this.conn = Database.getConnection();
    }

    /**
     * Returns the name of the database table this DAO manages.
     *
     * @return the database table name, e.g. "users"
     */
    protected abstract String tableName();

    /**
     * Returns the columns to select for findById and findAll.
     *
     * @return the columns to select, in the order mapRow expects them
     */
    protected abstract String[] selectColumns();

    /**
     * Maps the current row of the result set to a new entity instance. *
     *
     * @param rs the result set positioned at the current row
     * @return a new entity instance populated from the current row
     */
    protected abstract T mapRow(ResultSet rs) throws SQLException;

    /**
     * Crates a new entity in the database.
     * Subclasses must set the generated id back on the
     * entity (via its setter) when applicable.
     *
     * @param entity the entity to create (id may be null if auto-generated)
     * @return true if the entity was created, false otherwise
     */
    public abstract boolean create(T entity);

    /**
     * Updates the entity identified by its id.
     *
     * @param entity the entity to update (must have its id set)
     * @return true if the entity was updated, false if not found
     * */
    public abstract boolean update(T entity);

    /**
     * Generic method to find all entities of type T by their ID.
     *
     * @param id the primary key of the entity to find
     * @return the entity with the given ID, or null if not found
     */
    public T findById(ID id) {
        String cols = String.join(", ", selectColumns());
        String sql  = "SELECT " + cols + " FROM " + tableName() + " WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find " + tableName() + " by id", e);
        }
        return null;
    }

    /**
     * Generic method to find all entities of type T.
     *
     * @return a list of all entities in the database
     */
    public List<T> findAll() {
        String cols = String.join(", ", selectColumns());
        String sql  = "SELECT " + cols + " FROM " + tableName();
        List<T> list = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all " + tableName(), e);
        }

        return list;
    }

    /**
     * Generic method to delete an entity by its ID.
     *
     * @param id the primary key of the entity to delete
     * @return true if the entity was deleted, false if not found
     */
    public boolean delete(ID id) {
        String sql = "DELETE FROM " + tableName() + " WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete from " + tableName(), e);
        }
    }
}
