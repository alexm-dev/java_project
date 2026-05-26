package app.cli;

import app.dao.*;
import app.model.*;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Read-only inspector for the DB. Uses DAOs directly since this is a debug tool.
 *
 * TODO: Expand to also create and delete records.
 */
public class AdminMenu {

    private final Scanner scanner;

    private final UserDAO userDAO = new UserDAO();
    private final UserRoleDAO userRoleDAO = new UserRoleDAO();
    private final RoleDAO roleDAO = new RoleDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final SubCategoryDAO subCategoryDAO = new SubCategoryDAO();
    private final LocationDAO locationDAO = new LocationDAO();
    private final AssetDAO assetDAO = new AssetDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final RatingDAO ratingDAO = new RatingDAO();
    private final SessionDAO sessionDAO = new SessionDAO();

    public AdminMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Open the Admin menu loop.
     */
    public void run() {
        while (true) {
            System.out.println();
            System.out.println("[admin]");
            System.out.println("1. users");
            System.out.println("2. roles");
            System.out.println("3. categories");
            System.out.println("4. locations");
            System.out.println("5. assets");
            System.out.println("6. bookings");
            System.out.println("7. ratings");
            System.out.println("8. active session");
            System.out.println("9. row counts");
            System.out.println("0. back");

            switch (prompt()) {
                case "1" -> listUsers();
                case "2" -> listRoles();
                case "3" -> listCategories();
                case "4" -> listLocations();
                case "5" -> listAssets();
                case "6" -> listBookings();
                case "7" -> listRatings();
                case "8" -> showSession();
                case "9" -> showStats();
                case "0" -> { return; }
                default -> System.out.println("unknown option");
            }
        }
    }

    private void listUsers() {
        List<User> users = userDAO.findAll();
        System.out.println("users: " + users.size());
        for (User u : users) {
            String roles = userRoleDAO.findByUserId(u.getId()).stream()
                .map(ur -> roleDAO.findById(ur.getRoleId()))
                .filter(r -> r != null)
                .map(Role::getName)
                .collect(Collectors.joining(","));
            if (roles.isEmpty()) roles = "none";
            System.out.println("  " + u.getId() + " " + u.getUsername() + " " + u.getEmail()
                + " status=" + u.getStatus() + " roles=" + roles);
        }
    }

    private void listRoles() {
        List<Role> roles = roleDAO.findAll();
        System.out.println("roles: " + roles.size());
        for (Role r : roles) {
            System.out.println("  " + r.getId() + " " + r.getName());
        }
    }

    private void listCategories() {
        List<Category> cats = categoryDAO.findAll();
        System.out.println("categories: " + cats.size());
        if (cats.isEmpty()) {
            System.out.println("  (none seeded)");
            return;
        }
        for (Category c : cats) {
            System.out.println("  " + c.getId() + " " + c.getName());
            for (SubCategory s : subCategoryDAO.findByCategoryId(c.getId())) {
                System.out.println("    -> " + s.getId() + " " + s.getName());
            }
        }
    }

    private void listLocations() {
        List<Location> locs = locationDAO.findAll();
        System.out.println("locations: " + locs.size());
        for (Location l : locs) {
            System.out.println("  " + l.getId() + " " + l.getStreetAddress() + ", "
                + l.getPostalCode() + " " + l.getCity() + ", " + l.getCountry());
        }
    }

    private void listAssets() {
        List<Asset> assets = assetDAO.findAll();
        System.out.println("assets: " + assets.size());
        for (Asset a : assets) {
            System.out.println("  " + a.getId() + " " + a.getModel()
                + " owner=" + a.getOwnerId() + " subcat=" + a.getSubCategoryId()
                + " rate=" + a.getDailyRate() + " condition=" + a.getCondition());
            if (a.getMetadata() != null && !a.getMetadata().isBlank()) {
                System.out.println("    metadata: " + a.getMetadata());
            }
        }
    }

    private void listBookings() {
        List<Booking> bookings = bookingDAO.findAll();
        System.out.println("bookings: " + bookings.size());
        for (Booking b : bookings) {
            System.out.println("  " + b.getId() + " asset=" + b.getAssetId()
                + " renter=" + b.getRenterId() + " " + b.getStartTime() + " to " + b.getEndTime()
                + " status=" + b.getStatus() + " cost=" + b.getTotalCost());
        }
    }

    private void listRatings() {
        List<Rating> ratings = ratingDAO.findAll();
        System.out.println("ratings: " + ratings.size());
        for (Rating r : ratings) {
            String target = r.getRatedUserId() == null ? "asset" : "user=" + r.getRatedUserId();
            System.out.println("  " + r.getId() + " booking=" + r.getBookingId()
                + " reviewer=" + r.getReviewerId() + " " + target
                + " rating=" + r.getRatingValue() + "/5 \"" + (r.getComment() == null ? "" : r.getComment()) + "\"");
        }
    }

    private void showSession() {
        int activeId = sessionDAO.getActiveUserId();
        if (activeId == -1) {
            System.out.println("no active session row");
            return;
        }
        User u = userDAO.findById(activeId);
        System.out.println("active session: user_id=" + activeId
            + (u != null ? " (" + u.getUsername() + ")" : " (user no longer exists)"));
    }

    private void showStats() {
        System.out.println("users: " + userDAO.findAll().size());
        System.out.println("roles: " + roleDAO.findAll().size());
        System.out.println("categories: " + categoryDAO.findAll().size());
        System.out.println("sub_categories: " + subCategoryDAO.findAll().size());
        System.out.println("locations: " + locationDAO.findAll().size());
        System.out.println("assets: " + assetDAO.findAll().size());
        System.out.println("bookings: " + bookingDAO.findAll().size());
        System.out.println("ratings: " + ratingDAO.findAll().size());
    }

    private String prompt() {
        System.out.print("> ");
        return scanner.nextLine().trim();
    }
}
