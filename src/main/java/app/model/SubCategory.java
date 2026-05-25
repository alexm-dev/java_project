package app.model;

/**
 * SubCategory class representing the subcategories table in the database.
 * This class represents a subcategory of assets that belongs to a specific category.
 */
public class SubCategory {
    private int id;
    private String name;
    private int categoryId;

    public SubCategory(int id, String name, int categoryId) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
    }

    public SubCategory(String name, int categoryId) {
        this.name = name;
        this.categoryId = categoryId;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getCategoryId() { return categoryId; }
}
