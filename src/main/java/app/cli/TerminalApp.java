package app.cli;

import app.model.User;
import app.service.UserService;
import app.util.Logger;
import app.util.PasswordHasher;

import java.util.Scanner;

/**
 * CLI testbed for ShareSpace before the JavaFX UI is built.
 * Lets you exercise UserService and inspect the DB via the admin panel.
 */
public class TerminalApp {

    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService = new UserService();
    private final AdminMenu adminMenu;

    // local session - will move into SessionService once that exists
    private User currentUser = null;

    public TerminalApp() {
        this.adminMenu = new AdminMenu(scanner);
    }

    public void run() {
        System.out.println("ShareSpace CLI");
        while (true) {
            try {
                if (currentUser == null) guestMenu();
                else loggedInMenu();
            } catch (Exception e) {
                Logger.error("menu error", e);
            }
        }
    }

    private void guestMenu() {
        System.out.println();
        System.out.println("[guest menu]");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Browse assets (not yet)");
        System.out.println("4. Admin / Debug");
        System.out.println("9. Toggle debug logs (" + (Logger.isDebug() ? "ON" : "OFF") + ")");
        System.out.println("0. Exit");

        switch (prompt()) {
            case "1" -> register();
            case "2" -> login();
            case "3" -> System.out.println("AssetService not implemented yet.");
            case "4" -> adminMenu.run();
            case "9" -> toggleDebug();
            case "0" -> exit();
            default -> System.out.println("unknown option");
        }
    }

    private void loggedInMenu() {
        System.out.println();
        System.out.println("[" + currentUser.getUsername() + "]");
        System.out.println("1. View profile");
        System.out.println("2. Change username");
        System.out.println("3. Change email");
        System.out.println("4. Change password");
        System.out.println("5. Manage roles (not yet)");
        System.out.println("6. Delete account");
        System.out.println("7. My listings (not yet)");
        System.out.println("8. My bookings (not yet)");
        System.out.println("9. Toggle debug logs (" + (Logger.isDebug() ? "ON" : "OFF") + ")");
        System.out.println("a. Admin / Debug");
        System.out.println("l. Logout");
        System.out.println("0. Exit");

        switch (prompt().toLowerCase()) {
            case "1" -> viewProfile();
            case "2" -> changeUsername();
            case "3" -> changeEmail();
            case "4" -> changePassword();
            case "5" -> System.out.println("not yet integrated, use admin panel");
            case "6" -> deleteAccount();
            case "7" -> System.out.println("AssetService not implemented yet.");
            case "8" -> System.out.println("BookingService not implemented yet.");
            case "9" -> toggleDebug();
            case "a" -> adminMenu.run();
            case "l" -> logout();
            case "0" -> exit();
            default -> System.out.println("unknown option");
        }
    }

    private void register() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        char[] password = readPassword("Password: ");

        Logger.debug("register attempt: " + email);
        User user = userService.register(username, email, password);
        if (user == null) {
            System.out.println("registration failed - username or email taken");
            return;
        }
        System.out.println("registered: " + user.getUsername() + " (id=" + user.getId() + ")");
        Logger.info("user registered: id=" + user.getId());
    }

    private void login() {
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        char[] password = readPassword("Password: ");

        Logger.debug("login attempt: " + email);
        User user = userService.findByEmail(email);
        if (user == null || !PasswordHasher.verify(password, user.getPasswordHash())) {
            System.out.println("invalid email or password");
            Logger.warn("failed login: " + email);
            return;
        }
        currentUser = user;
        System.out.println("welcome " + user.getUsername());
        Logger.info("user logged in: id=" + user.getId());
    }

    private void logout() {
        Logger.info("user logged out: id=" + currentUser.getId());
        currentUser = null;
        System.out.println("logged out");
    }

    private void viewProfile() {
        currentUser = userService.findById(currentUser.getId());
        System.out.println("id: " + currentUser.getId());
        System.out.println("username: " + currentUser.getUsername());
        System.out.println("email: " + currentUser.getEmail());
        System.out.println("status: " + currentUser.getStatus());
        System.out.println("joined: " + currentUser.getCreatedTime());
    }

    private void changeUsername() {
        System.out.print("New username: ");
        String newName = scanner.nextLine().trim();
        if (userService.updateUsername(currentUser.getId(), newName)) {
            System.out.println("ok");
            Logger.info("username changed: id=" + currentUser.getId());
        } else {
            System.out.println("failed - username taken");
        }
    }

    private void changeEmail() {
        System.out.print("New email: ");
        String newEmail = scanner.nextLine().trim();
        if (userService.updateEmail(currentUser.getId(), newEmail)) {
            System.out.println("ok");
            Logger.info("email changed: id=" + currentUser.getId());
        } else {
            System.out.println("failed - email taken");
        }
    }

    private void changePassword() {
        char[] oldPwd = readPassword("Current password: ");
        User fresh = userService.findById(currentUser.getId());
        if (!PasswordHasher.verify(oldPwd, fresh.getPasswordHash())) {
            System.out.println("wrong password");
            Logger.warn("password change failed: id=" + currentUser.getId());
            return;
        }
        char[] newPwd = readPassword("New password: ");
        userService.updatePassword(currentUser.getId(), newPwd);
        System.out.println("password updated");
        Logger.info("password changed: id=" + currentUser.getId());
    }

    private void deleteAccount() {
        System.out.print("Type DELETE to confirm: ");
        if (!"DELETE".equals(scanner.nextLine().trim())) {
            System.out.println("cancelled");
            return;
        }
        int id = currentUser.getId();
        userService.deleteAccount(id);
        currentUser = null;
        System.out.println("account deleted");
        Logger.info("account deleted: id=" + id);
    }

    private void toggleDebug() {
        Logger.setDebug(!Logger.isDebug());
        System.out.println("debug logs " + (Logger.isDebug() ? "ON" : "OFF"));
    }

    private void exit() {
        System.out.println("bye");
        System.exit(0);
    }

    private String prompt() {
        System.out.print("> ");
        return scanner.nextLine().trim();
    }

    // Reads a password without echo if a real Console is attached.
    // When running from an IDE there's no Console so fall back to Scanner.
    private char[] readPassword(String label) {
        System.out.print(label);
        if (System.console() != null) {
            return System.console().readPassword();
        }
        Logger.debug("no Console, password input is visible");
        return scanner.nextLine().toCharArray();
    }
}
