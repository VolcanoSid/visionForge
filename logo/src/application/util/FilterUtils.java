package application.util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

/**
 * Utility class for applying basic image filters.
 * Used by PhotoEditorController and Effect classes.
 */
public class FilterUtils {

    /**
     * Applies a grayscale filter to the given image.
     * @param input Original image
     * @return New grayscale image
     */
    public static Image applyGrayscale(Image input) {
        int width = (int) input.getWidth();
        int height = (int) input.getHeight();

        WritableImage output = new WritableImage(width, height);
        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                double gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3.0;
                writer.setColor(x, y, new Color(gray, gray, gray, color.getOpacity()));
            }
        }

        return output;
    }

    /**
     * Adjusts brightness of the image.
     * @param input Original image
     * @param factor 1.0 = original, >1.0 = brighter, <1.0 = darker
     * @return New image with adjusted brightness
     */
    public static Image adjustBrightness(Image input, double factor) {
        int width = (int) input.getWidth();
        int height = (int) input.getHeight();

        WritableImage output = new WritableImage(width, height);
        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                Color adjusted = color.deriveColor(0, 1, factor, 1);
                writer.setColor(x, y, adjusted);
            }
        }

        return output;
    }

    /**
     * Inverts colors of the given image.
     */
    public static Image invertColors(Image input) {
        int width = (int) input.getWidth();
        int height = (int) input.getHeight();

        WritableImage output = new WritableImage(width, height);
        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                Color inverted = new Color(1 - color.getRed(), 1 - color.getGreen(), 1 - color.getBlue(), color.getOpacity());
                writer.setColor(x, y, inverted);
            }
        }

        return output;
    }
}
