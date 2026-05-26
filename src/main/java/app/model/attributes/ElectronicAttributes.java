package app.model.attributes;

/**
 * Category-specific attributes for electronic assets (cameras, laptops, etc.).
 */
public class ElectronicAttributes extends AssetAttributes {

    private String batteryHealth;
    private String warrantyInfo;

    public ElectronicAttributes(String batteryHealth, String warrantyInfo) {
        this.batteryHealth = batteryHealth;
        this.warrantyInfo = warrantyInfo;
    }

    @Override
    public String getType() { return "electronic"; }

    public String getBatteryHealth() { return batteryHealth; }
    public String getWarrantyInfo() { return warrantyInfo; }

    public void setBatteryHealth(String batteryHealth) { this.batteryHealth = batteryHealth; }
    public void setWarrantyInfo(String warrantyInfo) { this.warrantyInfo = warrantyInfo; }
}
