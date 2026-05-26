package app.service;

import app.dao.AssetDAO;
import app.dao.LocationDAO;
import app.model.Asset;
import app.model.Location;

import java.util.List;

/**
 * Service layer for managing assets.
 * Handles business logic related to asset creation, updating, deletion, and retrieval.
 */
public class AssetService {
    private final AssetDAO assetDAO;
    private final LocationDAO locationDAO;

    public AssetService() {
        this.assetDAO = new AssetDAO();
        this.locationDAO = new LocationDAO();
    }

    /**
     * Creates a new asset.
     *
     * @param asset The asset to create.
     * @param location The location associated with the asset. If it already exists, it will be reused.
     * @return The created asset with its ID, or null if creation failed.
     */
    public Asset createAsset(Asset asset, Location location) {
        Location existing = locationDAO.findMatch(location);
        if (existing != null) {
            location = existing;
        } else if (!locationDAO.create(location)) {
            return null;
        }

        asset.setAssetLocationId(location.getId());
        return assetDAO.create(asset) ? asset : null;
    }

    /**
     * Updates an existing asset.
     *
     * @param asset The asset with updated information. Must have a valid ID.
     * @param requestingUserID The ID of the user requesting the update. Must be the owner of the asset.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateAsset(Asset asset, int requestingUserID) {
        Asset existing = assetDAO.findById(asset.getId());

        if (existing == null) {
            return false;
        }

        if (existing.getOwnerId() != requestingUserID) {
            return false;
        }

        return assetDAO.update(asset);
    }

    /**
     * Deletes an asset by its ID.
     *
     * @param assetId The ID of the asset to delete.
     * @param requestingUserID The ID of the user requesting the deletion. Must be the owner of the asset.
     * @return True if the deletion was successful, false otherwise.
     */
    public boolean deleteAsset(int assetId, int requestingUserID) {
        Asset existing = assetDAO.findById(assetId);

        if (existing == null) {
            return false;
        }

        if (existing.getOwnerId() != requestingUserID) {
            return false;
        }

        return assetDAO.delete(assetId);
    }

    /**
     * Finds an asset by its ID.
     *
     * @param id The ID of the asset to find.
     * @return The asset with the specified ID, or null if not found.
     */
    public Asset findById(int id) {
        return assetDAO.findById(id);
    }

    /**
     * Looks up a location by id.
     *
     * @param locationId the location id (typically from Asset.getAssetLocationId())
     * @return the Location, or null if not found
     */
    public Location findLocationById(int locationId) {
        return locationDAO.findById(locationId);
    }

    /**
     * Finds all assets owned by a specific user.
     *
     * @param ownerId The ID of the owner whose assets to find.
     * @return A list of assets owned by the specified user, or an empty list if none are found.
     */
    public List<Asset> findByOwner(int ownerId) {
        return assetDAO.findByOwnerId(ownerId);
    }

    /**
     * Finds all assets that belong to a specific subcategory.
     *
     * @param subcategoryId The ID of the subcategory to search for.
     * @return A list of assets that belong to the specified subcategory, or an empty list if none are found.
     */
    public List<Asset> findBySubcategory(int subcategoryId) {
        return assetDAO.findBySubCategoryId(subcategoryId);
    }
}
