package app.scenario;

import app.dao.*;
import app.database.Database;
import app.model.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Scenario: a renter books an asset, the lender confirms, then both parties leave reviews.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingFlowTest {

    private static final String ANNA_EMAIL  = "anna.booking.scenario@sharespace.test";
    private static final String BOB_EMAIL   = "bob.booking.scenario@sharespace.test";
    private static final String TEST_CAT    = "Test Category (BookingFlow)";
    private static final String TEST_SUBCAT = "Test SubCategory (BookingFlow)";

    private UserDAO     userDAO;
    private LocationDAO locDAO;
    private AssetDAO    assetDAO;
    private BookingDAO  bookingDAO;
    private RatingDAO   ratingDAO;
    private UserRoleDAO urDAO;
    private RoleDAO     roleDAO;
    private SubCategory subCat;

    @BeforeAll
    void init() {
        Database.initialize();
        userDAO    = new UserDAO();
        locDAO     = new LocationDAO();
        assetDAO   = new AssetDAO();
        bookingDAO = new BookingDAO();
        ratingDAO  = new RatingDAO();
        urDAO      = new UserRoleDAO();
        roleDAO    = new RoleDAO();

        // ensure test category + sub-category exist (create only if missing)
        CategoryDAO    catDAO    = new CategoryDAO();
        SubCategoryDAO subCatDAO = new SubCategoryDAO();

        if (catDAO.findByName(TEST_CAT) == null) {
            catDAO.create(new Category(TEST_CAT, "Fixture category for BookingFlowTest"));
        }
        Category cat = catDAO.findByName(TEST_CAT);

        List<SubCategory> subs = subCatDAO.findByCategoryId(cat.getId());
        if (subs.isEmpty()) {
            subCatDAO.create(new SubCategory(TEST_SUBCAT, cat.getId()));
            subs = subCatDAO.findByCategoryId(cat.getId());
        }
        subCat = subs.get(0);
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
        for (String email : new String[]{ ANNA_EMAIL, BOB_EMAIL }) {
            User u = userDAO.findByEmail(email);
            if (u == null) continue;
            assetDAO.findByOwnerId(u.getId()).forEach(a -> {
                bookingDAO.findByAssetId(a.getId()).forEach(b -> {
                    ratingDAO.findByBookingId(b.getId()).forEach(r -> ratingDAO.delete(r.getId()));
                    bookingDAO.delete(b.getId());
                });
                Location l = locDAO.findById(a.getAssetLocationId());
                assetDAO.delete(a.getId());
                if (l != null) locDAO.delete(l.getId());
            });
            bookingDAO.findByRenterId(u.getId()).forEach(b -> {
                ratingDAO.findByBookingId(b.getId()).forEach(r -> ratingDAO.delete(r.getId()));
                bookingDAO.delete(b.getId());
            });
            urDAO.findByUserId(u.getId()).forEach(ur -> urDAO.delete(ur.getUserId(), ur.getRoleId()));
            userDAO.delete(u.getId());
        }
    }

    private User createAnna() {
        User anna = new User("Anna Lender", ANNA_EMAIL, "hash");
        anna.setStatus("active");
        userDAO.create(anna);
        urDAO.create(new UserRole(anna.getId(), roleDAO.findByName("lender").getId()));
        return anna;
    }

    private User createBob() {
        User bob = new User("Bob Renter", BOB_EMAIL, "hash");
        bob.setStatus("active");
        userDAO.create(bob);
        urDAO.create(new UserRole(bob.getId(), roleDAO.findByName("renter").getId()));
        return bob;
    }

    private Asset createAnnaAsset(User anna) {
        Location loc = new Location("Munich", "80331", null, "Booking-Flow-Str. 1", "Germany");
        locDAO.create(loc);
        Asset cam = new Asset(anna.getId(), subCat.getId(),
            "Sony A7 IV", "Full-frame mirrorless", "excellent", loc.getId(), 50.0);
        assetDAO.create(cam);
        return cam;
    }

    @Test
    void renter_canBookAvailableAsset() {
        User anna = createAnna();
        User bob  = createBob();
        Asset cam = createAnnaAsset(anna);

        Booking booking = new Booking(cam.getId(), bob.getId(),
            LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 3), "pending", 100.0);

        assertTrue(bookingDAO.create(booking));
        assertTrue(booking.getId() > 0);
    }

    @Test
    void newBooking_hasPendingStatus() {
        User anna = createAnna();
        User bob  = createBob();
        Asset cam = createAnnaAsset(anna);

        Booking booking = new Booking(cam.getId(), bob.getId(),
            LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 3), "pending", 100.0);
        bookingDAO.create(booking);

        assertEquals("pending", bookingDAO.findById(booking.getId()).getStatus());
    }

    @Test
    void lender_canConfirmBooking() {
        User anna = createAnna();
        User bob  = createBob();
        Asset cam = createAnnaAsset(anna);

        Booking booking = new Booking(cam.getId(), bob.getId(),
            LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 3), "pending", 100.0);
        bookingDAO.create(booking);

        booking.setStatus("confirmed");
        assertTrue(bookingDAO.update(booking));
        assertEquals("confirmed", bookingDAO.findById(booking.getId()).getStatus());
    }

    @Test
    void renter_canSeeTheirBookings() {
        User anna = createAnna();
        User bob  = createBob();
        Asset cam = createAnnaAsset(anna);

        Booking booking = new Booking(cam.getId(), bob.getId(),
            LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 3), "pending", 100.0);
        bookingDAO.create(booking);

        List<Booking> bobs = bookingDAO.findByRenterId(bob.getId());
        assertEquals(1, bobs.size());
        assertEquals(cam.getId(), bobs.get(0).getAssetId());
    }

    @Test
    void afterBooking_renterCanRateTheLender() {
        User anna = createAnna();
        User bob  = createBob();
        Asset cam = createAnnaAsset(anna);

        Booking booking = new Booking(cam.getId(), bob.getId(),
            LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 3), "completed", 100.0);
        bookingDAO.create(booking);
        booking.setStatus("completed");
        bookingDAO.update(booking);

        Rating review = new Rating(booking.getId(), bob.getId(), anna.getId(), 5, "Excellent lender!");
        assertTrue(ratingDAO.create(review));

        List<Rating> annaRatings = ratingDAO.findByRatedUserId(anna.getId());
        assertEquals(1, annaRatings.size());
        assertEquals(5, annaRatings.get(0).getRatingValue());
    }

    @Test
    void afterBooking_renterCanRateTheAssetExperience() {
        User anna = createAnna();
        User bob  = createBob();
        Asset cam = createAnnaAsset(anna);

        Booking booking = new Booking(cam.getId(), bob.getId(),
            LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 3), "completed", 100.0);
        bookingDAO.create(booking);

        // asset rating: ratedUserId is null (rating is about the asset, not a person)
        Rating assetReview = new Rating(booking.getId(), bob.getId(), null, 4, "Great camera, minor scratches");
        assertTrue(ratingDAO.create(assetReview));
        assertNull(ratingDAO.findById(assetReview.getId()).getRatedUserId());
    }

    @Test
    void booking_reviewsAreImmutable() {
        User anna = createAnna();
        User bob  = createBob();
        Asset cam = createAnnaAsset(anna);

        Booking booking = new Booking(cam.getId(), bob.getId(),
            LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 3), "completed", 100.0);
        bookingDAO.create(booking);

        Rating review = new Rating(booking.getId(), bob.getId(), anna.getId(), 5, "Great!");
        ratingDAO.create(review);

        assertThrows(UnsupportedOperationException.class, () -> ratingDAO.update(review));
    }
}
