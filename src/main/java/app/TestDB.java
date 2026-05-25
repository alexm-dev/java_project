package app;

import app.dao.RoleDAO;
import app.dao.UserDAO;
import app.model.Role;
import app.model.User;

import java.util.List;

public final class TestDB {
    private TestDB() {
    }

    public static void run() throws Exception {
        UserDAO userDAO = new UserDAO();
        RoleDAO roleDAO = new RoleDAO();

        // clean up any leftover test user from a previous run
        User existing = userDAO.findByEmail("max.mustermann@sharespace.com");
        if (existing != null) userDAO.delete(existing.getId());

        // --- 1. create ---
        User user = new User("max mustermann", "max.mustermann@sharespace.com", "passwordHash");
        user.setStatus("active");

        boolean created = userDAO.create(user);
        System.out.println("=== UserDAO test ===");
        System.out.println("create:        " + created + " (id=" + user.getId() + ")");

        // --- 2. findById ---
        User byId = userDAO.findById(user.getId());
        System.out.println("findById:      " + byId.getUsername() + " <" + byId.getEmail() + ">");

        // --- 3. findByEmail ---
        User byEmail = userDAO.findByEmail("max.mustermann@sharespace.com");
        System.out.println("findByEmail:   id=" + byEmail.getId());

        // --- 4. findByUsername ---
        User byUsername = userDAO.findByUsername("max mustermann");
        System.out.println("findByUsername: id=" + byUsername.getId());

        // --- 5. update ---
        user.setStatus("inactive");
        boolean updated = userDAO.update(user);
        User reloaded = userDAO.findById(user.getId());
        System.out.println("update:        " + updated + " (status now: " + reloaded.getStatus() + ")");

        // --- 6. findAll ---
        List<User> allUsers = userDAO.findAll();
        System.out.println("findAll:       " + allUsers.size() + " user(s) total");

        // --- 7. RoleDAO smoke test (seeded data) ---
        System.out.println("\n=== RoleDAO test ===");
        List<Role> roles = roleDAO.findAll();
        for (Role r : roles) {
            System.out.println("role: id=" + r.getId() + " name=" + r.getName());
        }
        Role lender = roleDAO.findByName("lender");
        System.out.println("findByName(lender): id=" + lender.getId());

        // --- 8. delete (cleanup) ---
        boolean deleted = userDAO.delete(user.getId());
        System.out.println("\ndelete:        " + deleted);
    }
}
