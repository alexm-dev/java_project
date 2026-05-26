package app.util;

import java.util.List;
import java.util.Map;

/**
 * Convention for which metadata keys each sub-category expects.
 */
public final class MetadataSchema {

    private MetadataSchema() {}

    private static final Map<String, List<String>> SCHEMA = Map.of(
        "Smartphone", List.of("color", "storage", "batteryHealth"),
        "TV", List.of("screenSize", "resolution", "panelType", "smart"),
        "Laptop", List.of("cpu", "ram", "storage", "screenSize"),
        "Tablet", List.of("storage", "screenSize", "cellular"),
        "Camera", List.of("megapixels", "lens", "shutterCount"),
        "Drill", List.of("voltage", "batteryIncluded", "chuckSize"),
        "Saw", List.of("type", "bladeSize", "powered"),
        "Bicycle", List.of("type", "frameSize", "gears"),
        "Car", List.of("year", "transmission", "mileage", "fuel")
    );

    /**
     * Returns the metadata keys expected for the given sub-category name,
     * or an empty list if the sub-category has no schema defined.
     *
     * @param subCategoryName the sub-category's name (case-sensitive, matches the seed)
     * @return ordered list of metadata keys
     */
    public static List<String> keysFor(String subCategoryName) {
        return SCHEMA.getOrDefault(subCategoryName, List.of());
    }
}
