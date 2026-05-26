package app.scenario;

import app.dao.AssetDAO;
import app.dao.BookingDAO;
import app.dao.LocationDAO;
import app.dao.RatingDAO;
import app.dao.RoleDAO;
import app.dao.SubCategoryDAO;
import app.dao.UserDAO;
import app.dao.UserRoleDAO;
import app.database.Database;
import app.model.Asset;
import app.model.Booking;
import app.model.Location;
import app.model.Rating;
import app.model.SubCategory;
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
 * Scenario: a renter books an asset, the lender confirms, then both parties leave reviews.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingFlowTest {

    private static final String ANNA_EMAIL = "anna.booking.scenario@sharespace.test";
    private static final String BOB_EMAIL  = "bob.booking.scenario@sharespace.test";

    private UserDAO     userDAO;
    private LocationDAO locDAO;
    private AssetDAO    assetDAO;
    private BookingDAO  bookingDAO;
    private RatingDAO   ratingDAO;
    private UserRoleDAO urDAO;
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

        RoleDAO roleDAO = new RoleDAO();
        subCat = new SubCategoryDAO().findAll().get(0);

        // store roles so scenario methods can look them up by name if needed
        roleDAO.findByName("lender");
        roleDAO.findByName("renter");
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

    // ----- helpers to set up scenario state -----

    private User createAnna() {
        User anna = new User("Anna Lender", ANNA_EMAIL, "hash");
        anna.setStatus("active");
        userDAO.create(anna);
        urDAO.create(new UserRole(anna.getId(), new RoleDAO().findByName("lender").getId()));
        return anna;
    }

    private User createBob() {
        User bob = new User("Bob Renter", BOB_EMAIL, "hash");
        bob.setStatus("active");
        userDAO.create(bob);
        urDAO.create(new UserRole(bob.getId(), new RoleDAO().findByName("renter").getId()));
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

    // ----- tests -----

    @Test
    void renter_canBookAvailableAsset() {
        User anna = createAnna();
        User bob  = createBob();
        Asset cam = createAnnaAsset(anna);

        Booking booking = new Booking(cam.getId(), bob.getId(),
            "2026-09-01", "2026-09-03", "pending", 100.0);

        assertTrue(bookingDAO.create(booking));
        assertTrue(booking.getId() > 0);
    }

    @Test
    void newBooking_hasPendingStatus() {
        User anna = createAnna();
        User bob  = createBob();
        Asset cam = createAnnaAsset(anna);

        Booking booking = new Booking(cam.getId(), bob.getId(),
            "2026-09-01", "2026-09-03", "pending", 100.0);
        bookingDAO.create(booking);

        assertEquals("pending", bookingDAO.findById(booking.getId()).getStatus());
    }

    @Test
    void lender_canConfirmBooking() {
        User anna = createAnna();
        User bob  = createBob();
        Asset cam = createAnnaAsset(anna);

        Booking booking = new Booking(cam.getId(), bob.getId(),
            "2026-09-01", "2026-09-03", "pending", 100.0);
        bookingDAO.create(booking);

        booking.setStatus("confirmed");
        booking.setUpdatedTime("2026-05-26 10:00:00");
        assertTrue(bookingDAO.update(booking));
        assertEquals("confirmed", bookingDAO.findById(booking.getId()).getStatus());
    }

    @Test
    void renter_canSeeTheirBookings() {
        User anna = createAnna();
        User bob  = createBob();
        Asset cam = createAnnaAsset(anna);

        Booking booking = new Booking(cam.getId(), bob.getId(),
            "2026-09-01", "2026-09-03", "pending", 100.0);
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
            "2026-09-01", "2026-09-03", "completed", 100.0);
        bookingDAO.create(booking);
        booking.setStatus("completed");
        booking.setUpdatedTime("2026-09-04 09:00:00");
        bookingDAO.update(booking);

        Rating review = new Rating(booking.getId(), bob.getId(), anna.getId(), 5, "Excellent lender!");
        assertTrue(ratingDAO.create(review));

        List<Rating> annaRatings = ratingDAO.findByRatedUserId(anna.getId());
        assertEquals(1, annaRatings.size());
        assertEquals(5, annaRatings.get(0).getRatingValue());
    }

    @Test
    void afterBooking_lenderCanRateTheAssetExperience() {
        User anna = createAnna();
        User bob  = createBob();
        Asset cam = createAnnaAsset(anna);

        Booking booking = new Booking(cam.getId(), bob.getId(),
            "2026-09-01", "2026-09-03", "completed", 100.0);
        bookingDAO.create(booking);

        // asset rating: ratedUserId is null, rating is about the asset/experience
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
            "2026-09-01", "2026-09-03", "completed", 100.0);
        bookingDAO.create(booking);

        Rating review = new Rating(booking.getId(), bob.getId(), anna.getId(), 5, "Great!");
        ratingDAO.create(review);

        assertThrows(UnsupportedOperationException.class, () -> ratingDAO.update(review));
    }
}
