package app.dao;

import app.model.SubCategory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the SubCategory entity.
 *
 * Sub-categories are reference data, each belonging to a parent Category.
 * The update and delete methods are overridden to return false since the
 * application never mutates sub-categories at runtime.
 */
public class SubCategoryDAO extends BaseDAO<SubCategory, Integer> {

    /**
     * The columns to select for findById and findAll, in mapRow order.
     */
    private static final String[] COLUMNS = { "id", "category_id", "name"};

    /**
     * The name of the database table this DAO manages.
     */
    @Override
    protected String tableName() {
        return "sub_categories";
    }

    /**
     * The columns to select for findById and findAll, in mapRow order.
     */
    @Override
    protected String[] selectColumns() {
        return COLUMNS;
    }

    /**
     * Maps a ResultSet row to a SubCategory object.
     *
     * @param rs The ResultSet to map.
     * @return A SubCategory object representing the current row of the ResultSet.
     */
    @Override
    protected SubCategory mapRow(ResultSet rs) throws SQLException {
        return new SubCategory(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("category_id")
        );
    }

    /**
     * Crates a new subcategory in the database.
     *
     * @param subCategory The subcategory to create.
     * @return true if the subcategory was created successfully, false otherwise.
     */
    @Override
    public boolean create(SubCategory subCategory) {
        String sql = "INSERT INTO sub_categories (name, category_id) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, subCategory.getName());
            stmt.setInt(2, subCategory.getCategoryId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create subcategory", e);
        }
    }

    /**
     * Not supported. Subcategories are not updated by the application.
     */
    @Override
    public boolean update(SubCategory subCategory) {
        return false;
    }

    /**
     * Not supported. Subcategories are not deleted by the application.
     */
    @Override
    public boolean delete(Integer id) {
        return false;
    }

    /**
     * Finds all subcategories that belong to a specific category.
     */
    public List<SubCategory> findByCategoryId(int categoryId) {
        List<SubCategory> list = new ArrayList<>();
        String sql = "SELECT id, name, category_id FROM sub_categories WHERE category_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);

            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find subcategories by category ID", e);
        }

        return list;
    }
}
