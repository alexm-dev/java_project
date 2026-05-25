package app.model;

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
