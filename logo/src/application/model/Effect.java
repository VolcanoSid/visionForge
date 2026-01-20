package application.model;

/**
 * Represents a single visual effect or filter that can be applied to images or videos.
 * Used by the Photo Editor and future plugin system.
 */
public class Effect {

    private String name;
    private String category; // e.g., "Color", "Blur", "Lighting"
    private double intensity; // 0.0 to 1.0
    private boolean enabled;

    public Effect(String name, String category, double intensity) {
        this.name = name;
        this.category = category;
        this.intensity = intensity;
        this.enabled = true;
    }

    // --- Getters and Setters --- //
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getIntensity() { return intensity; }
    public void setIntensity(double intensity) { this.intensity = intensity; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    @Override
    public String toString() {
        return String.format("%s (%s) - %.2f%%", name, category, intensity * 100);
    }
}
