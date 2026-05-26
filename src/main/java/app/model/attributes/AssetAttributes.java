package app.model.attributes;

/**
 * Base class for category-specific asset attributes.
 * Subclasses add fields relevant to their asset type (e.g. battery health for
 * electronics, maintenance log for tools).
 */
public abstract class AssetAttributes {

    /**
     * Returns the attribute type key.
     */
    public abstract String getType();
}
