package app.model;

public class SubCategory {
    private int id;
    private String name;
    private int categoryID;

    public SubCategory(int id, String name, int categoryID) {
        this.id = id;
        this.name = name;
        this.categoryID = categoryID;
    }

    public SubCategory(String name, int categoryID) {
        this.name = name;
        this.categoryID = categoryID;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getCategoryID() { return categoryID; }
}
