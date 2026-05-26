package app.model.attributes;

/**
 * Category-specific attributes for mechanical/tool assets (drills, saws, etc.).
 */
public class MechanicalAttributes extends AssetAttributes {

    private String maintenanceLog;
    private String lastServiceDate;

    public MechanicalAttributes(String maintenanceLog, String lastServiceDate) {
        this.maintenanceLog = maintenanceLog;
        this.lastServiceDate = lastServiceDate;
    }

    @Override
    public String getType() { return "mechanical"; }

    public String getMaintenanceLog() { return maintenanceLog; }
    public String getLastServiceDate() { return lastServiceDate; }

    public void setMaintenanceLog(String maintenanceLog) { this.maintenanceLog = maintenanceLog; }
    public void setLastServiceDate(String lastServiceDate) { this.lastServiceDate = lastServiceDate; }
}
