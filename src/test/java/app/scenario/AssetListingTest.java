package app.scenario;

import app.dao.AssetDAO;
import app.dao.CategoryDAO;
import app.dao.LocationDAO;
import app.dao.SubCategoryDAO;
import app.dao.UserDAO;
import app.dao.UserRoleDAO;
import app.database.Database;
import app.model.Asset;
import app.model.Category;
import app.model.Location;
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
 * Scenario: a registered lender creates a location and lists an asset for rent.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AssetListingTest {

    private static final String ANNA_EMAIL = "anna.listing.scenario@sharespace.test";

    private UserDAO        userDAO;
    private LocationDAO    locDAO;
    private AssetDAO       assetDAO;
    private UserRoleDAO    urDAO;
    private SubCategory    subCat;
    private Category       category;

    @BeforeAll
    void init() {
        Database.initialize();
        userDAO  = new UserDAO();
        locDAO   = new LocationDAO();
        assetDAO = new AssetDAO();
        urDAO    = new UserRoleDAO();

        CategoryDAO catDAO = new CategoryDAO();
        category = catDAO.findAll().get(0);
        subCat   = new SubCategoryDAO().findByCategoryId(category.getId()).get(0);
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
            assetDAO.findByOwnerId(existing.getId()).forEach(a -> {
                Location l = locDAO.findById(a.getAssetLocationId());
                assetDAO.delete(a.getId());
                if (l != null) locDAO.delete(l.getId());
            });
            urDAO.findByUserId(existing.getId())
                .forEach(ur -> urDAO.delete(ur.getUserId(), ur.getRoleId()));
            userDAO.delete(existing.getId());
        }
    }

    @Test
    void lender_canListAsset() {
        User anna = new User("Anna Lender", ANNA_EMAIL, "hash");
        anna.setStatus("active");
        userDAO.create(anna);

        Location loc = new Location("Frankfurt", "60311", "Innenstadt", "Listing-Test-Str. 1", "Germany");
        locDAO.create(loc);

        Asset camera = new Asset(anna.getId(), subCat.getId(),
            "Canon EOS R50", "Great entry-level camera", "good", loc.getId(), 35.0);

        assertTrue(assetDAO.create(camera));
        assertTrue(camera.getId() > 0);
    }

    @Test
    void listedAsset_hasCorrectDetails() {
        User anna = new User("Anna Lender", ANNA_EMAIL, "hash");
        anna.setStatus("active");
        userDAO.create(anna);

        Location loc = new Location("Frankfurt", "60311", "Innenstadt", "Listing-Test-Str. 1", "Germany");
        locDAO.create(loc);

        Asset camera = new Asset(anna.getId(), subCat.getId(),
            "Canon EOS R50", "Great entry-level camera", "good", loc.getId(), 35.0);
        assetDAO.create(camera);

        Asset found = assetDAO.findById(camera.getId());
        assertNotNull(found);
        assertEquals("Canon EOS R50", found.getModel());
        assertEquals("good", found.getCondition());
        assertEquals(35.0, found.getDailyRate());
        assertEquals(loc.getId(), found.getAssetLocationId());
    }

    @Test
    void listedAsset_appearsInOwnerListing() {
        User anna = new User("Anna Lender", ANNA_EMAIL, "hash");
        anna.setStatus("active");
        userDAO.create(anna);

        Location loc = new Location("Frankfurt", "60311", "Innenstadt", "Listing-Test-Str. 1", "Germany");
        locDAO.create(loc);

        Asset camera = new Asset(anna.getId(), subCat.getId(),
            "Canon EOS R50", "Great entry-level camera", "good", loc.getId(), 35.0);
        assetDAO.create(camera);

        List<Asset> annaAssets = assetDAO.findByOwnerId(anna.getId());
        assertEquals(1, annaAssets.size());
        assertEquals(camera.getId(), annaAssets.get(0).getId());
    }

    @Test
    void listedAsset_appearsUnderCorrectSubCategory() {
        User anna = new User("Anna Lender", ANNA_EMAIL, "hash");
        anna.setStatus("active");
        userDAO.create(anna);

        Location loc = new Location("Frankfurt", "60311", "Innenstadt", "Listing-Test-Str. 1", "Germany");
        locDAO.create(loc);

        Asset camera = new Asset(anna.getId(), subCat.getId(),
            "Canon EOS R50", "Great entry-level camera", "good", loc.getId(), 35.0);
        assetDAO.create(camera);

        assertTrue(assetDAO.findBySubCategoryId(subCat.getId()).stream()
            .anyMatch(a -> a.getId() == camera.getId()));
    }

    @Test
    void lender_canUpdateAssetDetails() {
        User anna = new User("Anna Lender", ANNA_EMAIL, "hash");
        anna.setStatus("active");
        userDAO.create(anna);

        Location loc = new Location("Frankfurt", "60311", "Innenstadt", "Listing-Test-Str. 1", "Germany");
        locDAO.create(loc);

        Asset camera = new Asset(anna.getId(), subCat.getId(),
            "Canon EOS R50", "Great entry-level camera", "good", loc.getId(), 35.0);
        assetDAO.create(camera);

        camera.setDailyRate(40.0);
        camera.setCondition("excellent");
        assertTrue(assetDAO.update(camera));

        Asset updated = assetDAO.findById(camera.getId());
        assertEquals(40.0, updated.getDailyRate());
        assertEquals("excellent", updated.getCondition());
    }
}
