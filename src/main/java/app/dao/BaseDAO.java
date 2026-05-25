package app.dao;

import app.database.Database;

import java.sql.Connection;
import java.util.List;

/**
 * Abstract base class for Data Access Objects (DAOs)
 * Provides common CRUD operations for entities.
 *
 * @param <T>  The type of the entity.
 * @param <ID> The type of the entity's identifier.
 */
public abstract class BaseDAO<T, ID> {
    protected Connection conn;

    /**
     * Constructor that initializes the database connection.
     */
    public BaseDAO() {
        this.conn = Database.getConnection();
    }

    /**
     * Creates a new entity in the database.
     *
     * @param entity Generic entity to be created.
     * @return true if the entity was created successfully, false otherwise.
     */
    public abstract boolean create(T entity);

    /**
     * Generic method to find an entity by its ID.
     *
     * @param id The id of the entity to be found.
     * @return The entity if found, null otherwise.
     */
    public abstract T findById(ID id);

    /**
     * Generic method to find all entities of type T.
     *
     * @return A list of all entities of type T in the database.
     */
    public abstract List<T> findAll();

    /**
     * Generic method to update an existing entity in the database.
     *
     * @param entity The generic entity with updated information.
     * @return true if the entity was updated successfully, false otherwise.
     */
    public abstract boolean update(T entity);

    /**
     * Method to delete an entity by its ID.
     *
     * @param id The id of the entity to be deleted.
     * @return true if the entity was deleted successfully, false otherwise.
     */
    public abstract boolean delete(ID id);
}
