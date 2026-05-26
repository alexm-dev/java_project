package app.service;

import app.dao.UserDAO;
import app.dao.UserRoleDAO;
import app.model.User;
import app.model.UserRole;
import app.util.PasswordHasher;

/**
 * Handles user registration, profile management, password updates and role assignment.
 * All persistence goes through UserDAO and UserRoleDAO, no user state is held in memory here.
 */
public class UserService {

    private final UserDAO userDAO;
    private final UserRoleDAO userRoleDAO;

    public UserService() {
        this.userDAO = new UserDAO();
        this.userRoleDAO = new UserRoleDAO();
    }

    /**
     * Registers a new user. Hashes the plain-text password before storing.
     * Returns null if the username or email is already taken.
     *
     * @param username the desired username
     * @param email the user's email address
     * @param plainPassword the plain-text password (hashed internally)
     * @return the created User with its generated id, or null if registration failed
     */
    public User register(String username, String email, char[] plainPassword) {
        if (userDAO.findByEmail(email) != null) return null;
        if (userDAO.findByUsername(username) != null) return null;

        User user = new User(username, email, PasswordHasher.hash(plainPassword));
        user.setStatus("active");
        return userDAO.create(user) ? user : null;
    }

    /**
     * Looks up a user by their id.
     *
     * @param id the user id
     * @return the User, or null if not found
     */
    public User findById(int id) {
        return userDAO.findById(id);
    }

    /**
     * Looks up a user by their email address.
     *
     * @param email the email to search for
     * @return the User, or null if not found
     */
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    /**
     * Updates a user's profile fields (username, email, status).
     * The user object must have its id set.
     *
     * @param user the user with updated values
     * @return true if updated, false if the user was not found
     */
    public boolean updateProfile(User user) {
        return userDAO.update(user);
    }

    /**
     * Replaces the stored password hash for a user.
     *
     * @param userId the id of the user
     * @param plainPassword the new plain-text password (hashed internally)
     * @return true if updated, false if the user was not found
     */
    public boolean updatePassword(int userId, char[] plainPassword) {
        User user = userDAO.findById(userId);
        if (user == null) return false;
        user.setPasswordHash(PasswordHasher.hash(plainPassword));
        return userDAO.update(user);
    }

    /**
     * Deletes a user account. Cascades to their assets, bookings and roles via FK.
     *
     * @param userId the id of the user to delete
     * @return true if deleted, false if not found
     */
    public boolean deleteAccount(int userId) {
        return userDAO.delete(userId);
    }

    /**
     * Assigns a role to a user (eg. lender or renter).
     *
     * @param userId the user id
     * @param roleId the role id to assign
     * @return true if the role was assigned
     */
    public boolean assignRoleToUser(int userId, int roleId) {
        return userRoleDAO.create(new UserRole(userId, roleId));
    }

    /**
     * Removes a role from a user.
     *
     * @param userId the user id
     * @param roleId the role id to remove
     * @return true if removed, false if the role wasn't assigned
     */
    public boolean removeRoleFromUser(int userId, int roleId) {
        return userRoleDAO.delete(userId, roleId);
    }
}
