package app.scenario;

import app.dao.RoleDAO;
import app.dao.UserDAO;
import app.dao.UserRoleDAO;
import app.database.Database;
import app.model.Role;
import app.model.User;
import app.model.UserRole;

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

    private UserDAO     userDAO;
    private RoleDAO     roleDAO;
    private UserRoleDAO urDAO;
    private Role        lenderRole;

    @BeforeAll
    void init() {
        Database.initialize();
        userDAO    = new UserDAO();
        roleDAO    = new RoleDAO();
        urDAO      = new UserRoleDAO();
        lenderRole = roleDAO.findByName("lender");
    }

    @BeforeEach
    void setUp() {
        cleanup();
    }

    @AfterEach
    void tearDown() {
        cleanup();
    }

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
        User anna = new User("Anna Lender", ANNA_EMAIL, "secureHash");
        anna.setStatus("active");

        assertTrue(userDAO.create(anna));
        assertTrue(anna.getId() > 0);
    }

    @Test
    void registeredUser_canBeFoundByEmail() {
        User anna = new User("Anna Lender", ANNA_EMAIL, "secureHash");
        anna.setStatus("active");
        userDAO.create(anna);

        User found = userDAO.findByEmail(ANNA_EMAIL);
        assertNotNull(found);
        assertEquals("Anna Lender", found.getUsername());
        assertEquals("active", found.getStatus());
    }

    @Test
    void registeredUser_canBeAssignedLenderRole() {
        User anna = new User("Anna Lender", ANNA_EMAIL, "secureHash");
        anna.setStatus("active");
        userDAO.create(anna);

        assertTrue(urDAO.create(new UserRole(anna.getId(), lenderRole.getId())));

        List<UserRole> roles = urDAO.findByUserId(anna.getId());
        assertEquals(1, roles.size());
        assertEquals(lenderRole.getId(), roles.get(0).getRoleId());
    }

    @Test
    void registeredUser_appearsInUserList() {
        User anna = new User("Anna Lender", ANNA_EMAIL, "secureHash");
        anna.setStatus("active");
        userDAO.create(anna);

        boolean found = userDAO.findAll().stream()
            .anyMatch(u -> u.getEmail().equals(ANNA_EMAIL));
        assertTrue(found);
    }

    @Test
    void user_canUpdateProfile() {
        User anna = new User("Anna Lender", ANNA_EMAIL, "secureHash");
        anna.setStatus("active");
        userDAO.create(anna);

        anna.setStatus("inactive");
        assertTrue(userDAO.update(anna));
        assertEquals("inactive", userDAO.findById(anna.getId()).getStatus());
    }
}
