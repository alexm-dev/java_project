package app.service;

import app.dao.SessionDAO;
import app.dao.UserDAO;
import app.model.User;
import app.util.AuthUtil;

import java.util.Arrays;

/**
 * Service for managing user sessions, including login, logout, and session restoration.
 */
public class SessionService {
    private final SessionDAO sessionDAO;
    private final UserDAO userDAO;

    private User activeUser;

    public SessionService() {
        this.sessionDAO = new SessionDAO();
        this.userDAO = new UserDAO();
    }

    /**
     * Restores the user session by checking for an active user ID in the session storage.
     *
     * @return The active user if a session exists, or null if no session is found.
     */
    public User restoreSession() {
        int userId = sessionDAO.getActiveUserId();

        if (userId == -1) {
            return null;
        }

        activeUser = userDAO.findById(userId);
        return activeUser;
    }

    /**
     * Attempts to log in a user with the provided email and plain text password.
     *
     * @param email The email of the user attempting to log in.
     * @param plainPassword The plain text password provided by the user.
     * @return The logged-in user if authentication is successful, or null if authentication fails.
     */
    public User login(String email, char[] plainPassword) {
        User user = userDAO.findByEmail(email);

        if (user == null || !AuthUtil.verifyPassword(plainPassword, user.getPasswordHash())) {
            return null;
        }

        sessionDAO.save(user.getId());
        activeUser = user;
        return activeUser;
    }

    /**
     * Logout the current user
     */
    public void logout() {
        sessionDAO.clear();
        activeUser = null;
    }

    /**
     * Gets the currently active user in the session.
     *
     * @return The active user, or null if no user is logged in.
     */
    public User getActiveUser() {
        return activeUser;
    }

    /**
     * Checks if a user is currently logged in.
     *
     * @return true if a user is logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return activeUser != null;
    }

    /**
     * Re-reads the active user from the DB so the in-memory copy stays in sync
     * after a service mutates the user row (eg. username/email/password change).
     */
    public void refreshActiveUser() {
        if (activeUser == null) return;
        activeUser = userDAO.findById(activeUser.getId());
    }

    /**
     * Checks a plain-text password against the active users stored hash.
     *
     * @param plain the plain-text password attempt
     * @return true if it matches, false otherwise (or if no one is logged in)
     */
    public boolean verifyActivePassword(char[] plain) {
        if (activeUser == null) {
            Arrays.fill(plain, '\0');
            return false;
        }

        User fresh = userDAO.findById(activeUser.getId());
        if (fresh == null) {
            Arrays.fill(plain, '\0');
            return false;
        }

        return AuthUtil.verifyPassword(plain, fresh.getPasswordHash());
    }
}
