package app.service;

import app.dao.UserDAO;
import app.dao.UserRoleDAO;
import app.model.User;
import app.model.UserRole;
import app.util.EmailValidator;
import app.util.PasswordHasher;
import app.util.PasswordValidator;

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
        if (!EmailValidator.isValid(email)){
            return null;
        }

        if (!PasswordValidator.isValid(plainPassword)) {
            return null;
        }

        if (userDAO.findByEmail(email) != null) {
            return null;
        }

        if (userDAO.findByUsername(username) != null) {
            return null;
        }

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
     * Changes a users email address. Rejects the change if the new email
     * is already taken by another user.
     *
     * @param userId the user id
     * @param newEmail the new email address
     * @return true if updated, false if the user was not found or the email is taken
     */
    public boolean updateEmail(int userId, String newEmail) {
        if (!EmailValidator.isValid(newEmail)) { 
            return false;
        }

        User existing = userDAO.findByEmail(newEmail);
        if (existing != null && existing.getId() != userId) { 
            return false;
        }

        User user = userDAO.findById(userId);
        if (user == null) {
            return false;
        }

        user.setEmail(newEmail);
        return userDAO.update(user);
    }

    /**
     * Changes a users username. Rejects the change if the new username
     * is already taken by another user.
     *
     * @param userId the user id
     * @param newUsername the new username
     * @return true if updated, false if the user was not found or the username is taken
     */
    public boolean updateUsername(int userId, String newUsername) {
        User existing = userDAO.findByUsername(newUsername);
        if (existing != null && existing.getId() != userId) { 
            return false;
        }

        User user = userDAO.findById(userId);
        if (user == null) {
            return false;
        }

        user.setUsername(newUsername);
        return userDAO.update(user);
    }

    /**
     * Updates a user's status (eg. active, inactive, suspended).
     *
     * @param userId the user id
     * @param newStatus the new status string
     * @return true if updated, false if the user was not found
     */
    public boolean updateStatus(int userId, String newStatus) {
        User user = userDAO.findById(userId);
        if (user == null) {
            return false;
        }

        user.setStatus(newStatus);
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
        if (!PasswordValidator.isValid(plainPassword)) { 
            return false;
        }
        User user = userDAO.findById(userId);
        if (user == null) {
            return false;
        }

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
