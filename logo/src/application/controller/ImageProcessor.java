package application.controller;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class ImageProcessor {
    public static Image adjustImage(Image src, double brightness, double contrast) {
        int w = (int) src.getWidth();
        int h = (int) src.getHeight();
        WritableImage img = new WritableImage(w, h);
        PixelReader pr = src.getPixelReader();
        PixelWriter pw = img.getPixelWriter();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color c = pr.getColor(x, y);
                // Adjust contrast: c = (c - 0.5) * contrast + 0.5
                double r = ((c.getRed() - 0.5) * contrast + 0.5) * brightness;
                double g = ((c.getGreen() - 0.5) * contrast + 0.5) * brightness;
                double b = ((c.getBlue() - 0.5) * contrast + 0.5) * brightness;
                r = Math.min(Math.max(r, 0), 1);
                g = Math.min(Math.max(g, 0), 1);
                b = Math.min(Math.max(b, 0), 1);
                pw.setColor(x, y, new Color(r, g, b, c.getOpacity()));
            }
        }
        return img;
    }
}
