package app.model;

/**
 * UserRole class represents the association between a user and a role in the system.
 */
public class UserRole {
    private int userId;
    private int roleId;

    public UserRole(int userId, int roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public int getUserId() { return userId; }
    public int getRoleId() { return roleId; }
}
