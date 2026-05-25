package app.model;

/**
 * Role class representing the roles table from the DB.
 * */
public class Role {
    private int id;
    private String name;

    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role(String name) {
        this.name = name;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
}
