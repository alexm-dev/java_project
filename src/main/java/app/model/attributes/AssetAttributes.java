package app.model.attributes;

/**
 * Base class for category-specific asset attributes.
 * Subclasses add fields relevant to their asset type (e.g. battery health for
 * electronics, maintenance log for tools). The service layer serializes these
 * to/from the JSON metadata column on the assets table.
 */
public abstract class AssetAttributes {

    /**
     * Returns the attribute type key, used by the service layer to pick the
     * right subclass when deserializing from JSON (e.g. "electronic", "mechanical").
     */
    public abstract String getType();
}
