package app.dao;

import app.model.Category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for the Category entity.
 *
 * Categories are reference data; the update and delete methods are
 * overridden to return false since the application never mutates
 * categories at runtime.
 */
public class CategoryDAO extends BaseDAO<Category, Integer> {

    /**
     * The columns to select for findById and findAll, in mapRow order.
     */
    private static final String[] COLUMNS = { "id", "name", "description" };

    /**
     * The name of the database table this DAO manages.
     */
    @Override
    protected String tableName() {
        return "categories";
    }

    /**
     * The columns to select for findById and findAll, in mapRow orderr.
     */
    @Override
    protected String[] selectColumns() {
        return COLUMNS;
    }

    /**
     * Maps a ResultSet row to a Category object.
     *
     * @param rs The ResultSet to map.
     * @return A Category object representing the current row of the ResultSet.
     */
    @Override
    protected Category mapRow(ResultSet rs) throws SQLException {
        return new Category(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("description")
        );
    }

    /**
     * Crates a new category in the database.
     *
     * @param category The category to create. The generated id will be set back on this object.
     * @return true if the category was created successfully, false otherwise.
     */
    @Override
    public boolean create(Category category) {
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create category", e);
        }
    }

    /**
     * Not implemented. Categories are seeded data and cannot be updated at runtime.
     */
    @Override
    public boolean update(Category category) {
        return false;
    }

    /**
     * Not implemented. Categories are seeded data and cannot be deleted at runtime.
     */
    @Override
    public boolean delete(Integer id) {
        return false;
    }

    /**
     * Finds a category by its name.
     *
     * @param name the name of the category to find
     * @return the Category with the given name, or null if not found
     */
    public Category findByName(String name) {
        String sql = "SELECT id, name, description FROM categories WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find category by name", e);
        }
        return null;
    }
}
