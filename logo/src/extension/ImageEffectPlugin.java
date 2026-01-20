package extension;

import javafx.scene.image.Image;

/**
 * Interface for creating custom image effect plugins.
 * Implement this interface in external JARs or internal modules
 * to add new filters dynamically into VisionForge.
 *
 * Example:
 * public class BlurEffect implements ImageEffectPlugin {
 *     public String getName() { return "Gaussian Blur"; }
 *     public Image applyEffect(Image input) { ... }
 * }
 */
public interface ImageEffectPlugin {

    /**
     * Returns the display name of the effect (e.g. "Grayscale", "Sepia").
     */
    String getName();

    /**
     * Applies the effect to the given image and returns the transformed version.
     * @param input Original JavaFX Image
     * @return Modified Image with the effect applied
     */
    Image applyEffect(Image input);
}
