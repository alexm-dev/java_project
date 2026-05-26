package app.scenario;

import app.dao.RoleDAO;
import app.dao.UserDAO;
import app.dao.UserRoleDAO;
import app.database.Database;
import app.model.Role;
import app.model.User;
import app.model.UserRole;
import app.service.UserService;
import app.util.PasswordHasher;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Scenario: a new user registers on ShareSpace and is assigned a role.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRegistrationTest {

    private static final String ANNA_EMAIL = "anna.lender.scenario@sharespace.test";
    private static final String ANNA_USERNAME = "Anna Lender";
    private static final String PASSWORD = "s3cretPassw0rd!";

    private UserService userService;
    private UserDAO userDAO;
    private RoleDAO roleDAO;
    private UserRoleDAO urDAO;
    private Role lenderRole;

    @BeforeAll
    void init() {
        Database.initialize();
        userService = new UserService();
        userDAO = new UserDAO();
        roleDAO = new RoleDAO();
        urDAO = new UserRoleDAO();
        lenderRole = roleDAO.findByName("lender");
    }

    @BeforeEach
    void setUp() { cleanup(); }

    @AfterEach
    void tearDown() { cleanup(); }

    private void cleanup() {
        User existing = userDAO.findByEmail(ANNA_EMAIL);
        if (existing != null) {
            urDAO.findByUserId(existing.getId())
                .forEach(ur -> urDAO.delete(ur.getUserId(), ur.getRoleId()));
            userDAO.delete(existing.getId());
        }
    }

    @Test
    void newUser_canRegister() {
        User anna = userService.register(ANNA_USERNAME, ANNA_EMAIL, PASSWORD.toCharArray());

        assertNotNull(anna);
        assertTrue(anna.getId() > 0);
        assertEquals("active", anna.getStatus());
    }

    @Test
    void registering_withDuplicateEmail_returnsNull() {
        userService.register(ANNA_USERNAME, ANNA_EMAIL, PASSWORD.toCharArray());

        User second = userService.register("Someone Else", ANNA_EMAIL, "otherPwd".toCharArray());
        assertNull(second);
    }

    @Test
    void storedPassword_isHashed_notPlaintext() {
        userService.register(ANNA_USERNAME, ANNA_EMAIL, PASSWORD.toCharArray());

        User stored = userDAO.findByEmail(ANNA_EMAIL);
        assertNotEquals(PASSWORD, stored.getPasswordHash(), "password must not be stored as plain text");
        assertTrue(PasswordHasher.verify(PASSWORD.toCharArray(), stored.getPasswordHash()));
    }

    @Test
    void registeredUser_canBeFoundByEmail() {
        userService.register(ANNA_USERNAME, ANNA_EMAIL, PASSWORD.toCharArray());

        User found = userService.findByEmail(ANNA_EMAIL);
        assertNotNull(found);
        assertEquals(ANNA_USERNAME, found.getUsername());
        assertEquals("active", found.getStatus());
    }

    @Test
    void registeredUser_canBeAssignedLenderRole() {
        User anna = userService.register(ANNA_USERNAME, ANNA_EMAIL, PASSWORD.toCharArray());

        assertTrue(userService.assignRoleToUser(anna.getId(), lenderRole.getId()));

        List<UserRole> roles = urDAO.findByUserId(anna.getId());
        assertEquals(1, roles.size());
        assertEquals(lenderRole.getId(), roles.get(0).getRoleId());
    }

    @Test
    void registeredUser_appearsInUserList() {
        userService.register(ANNA_USERNAME, ANNA_EMAIL, PASSWORD.toCharArray());

        boolean found = userDAO.findAll().stream()
            .anyMatch(u -> u.getEmail().equals(ANNA_EMAIL));
        assertTrue(found);
    }

    @Test
    void user_canUpdateStatus() {
        User anna = userService.register(ANNA_USERNAME, ANNA_EMAIL, PASSWORD.toCharArray());

        assertTrue(userService.updateStatus(anna.getId(), "inactive"));
        assertEquals("inactive", userService.findById(anna.getId()).getStatus());
    }

    @Test
    void user_canUpdateEmail() {
        User anna = userService.register(ANNA_USERNAME, ANNA_EMAIL, PASSWORD.toCharArray());

        String newEmail = "anna.new@sharespace.test";
        try {
            assertTrue(userService.updateEmail(anna.getId(), newEmail));
            assertEquals(newEmail, userService.findById(anna.getId()).getEmail());
        } finally {
            // cleanup the renamed user since the standard cleanup() looks for ANNA_EMAIL
            User renamed = userDAO.findByEmail(newEmail);
            if (renamed != null) userDAO.delete(renamed.getId());
        }
    }

    @Test
    void user_canUpdatePassword() {
        User anna = userService.register(ANNA_USERNAME, ANNA_EMAIL, PASSWORD.toCharArray());

        String newPassword = "evenM0reSecure!";
        assertTrue(userService.updatePassword(anna.getId(), newPassword.toCharArray()));

        User updated = userDAO.findByEmail(ANNA_EMAIL);
        assertTrue(PasswordHasher.verify(newPassword.toCharArray(), updated.getPasswordHash()));
        assertFalse(PasswordHasher.verify(PASSWORD.toCharArray(), updated.getPasswordHash()));
    }
}
