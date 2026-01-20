package application.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Stack;

public class ImageController {

    @FXML private ImageView imageView;

    // Sliders & labels for live updates
    @FXML private Slider brightnessSlider, contrastSlider, blurSlider;
    @FXML private Label brightnessLabel, contrastLabel, blurLabel;

    // Undo/Redo support
    private final Stack<Image> undoStack = new Stack<>();
    private final Stack<Image> redoStack = new Stack<>();

    private Image originalImage;

    // =============================================
    //   CORE IMAGE HANDLING
    // =============================================

    private interface ImageOp { Image apply(Image img); }

    private void filterImage(ImageOp op) {
        Image img = imageView.getImage();
        if (img == null) return;

        pushUndo();
        Image result = op.apply(img);
        if (result == null) return;

        imageView.setImage(result);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(false);

        System.out.println("[Filter Applied]");
    }

    private void pushUndo() {
        if (imageView.getImage() != null)
            undoStack.push(imageView.getImage());
        redoStack.clear();
    }

    // =============================================
    //   LOAD / RESET / UNDO / REDO
    // =============================================

    @FXML
    private void handleLoadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            originalImage = new Image(file.toURI().toString());
            imageView.setImage(originalImage);
            undoStack.clear();
            redoStack.clear();
        }
    }

    @FXML
    private void handleReset() {
        if (originalImage != null) {
            imageView.setImage(originalImage);
            undoStack.clear();
            redoStack.clear();
        }
        resetSliders();
    }

    @FXML
    private void handleUndo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(imageView.getImage());
            imageView.setImage(undoStack.pop());
        }
    }

    @FXML
    private void handleRedo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(imageView.getImage());
            imageView.setImage(redoStack.pop());
        }
    }

    // =============================================
    //   SLIDERS (Brightness, Contrast, Blur)
    // =============================================

    @FXML
    private void initialize() {
        brightnessSlider.valueProperty().addListener((_, _, newVal) ->
                brightnessLabel.setText(String.format("%.2f", newVal.doubleValue())));
        contrastSlider.valueProperty().addListener((_, _, newVal) ->
                contrastLabel.setText(String.format("%.2f", newVal.doubleValue())));
        blurSlider.valueProperty().addListener((_, _, newVal) ->
                blurLabel.setText(String.format("%.0f", newVal.doubleValue())));
        resetSliders();
    }

    private void resetSliders() {
        if (brightnessSlider != null) brightnessSlider.setValue(0);
        if (contrastSlider != null) contrastSlider.setValue(1);
        if (blurSlider != null) blurSlider.setValue(0);
    }

    // =============================================
    //   FILTER BUTTON ACTIONS
    // =============================================

    @FXML private void applyGrayscale() { filterImage(this::grayscale); }
    @FXML private void applyInvert()    { filterImage(this::invert); }
    @FXML private void applySepia()     { filterImage(this::sepia); }
    @FXML private void applyRGBFilter() { filterImage(this::rgbFilter); }
    @FXML private void applySharpen()   { filterImage(this::sharpen); }
    @FXML private void applyEdgeDetect(){ filterImage(this::edgeDetect); }
    @FXML private void applyPosterize() { filterImage(this::posterize); }
    @FXML private void applyEmboss()    { filterImage(this::emboss); }

    @FXML private void applyVignette()       { filterImage(this::vignette); }
    @FXML private void applyWarmTone()       { filterImage(this::warmTone); }
    @FXML private void applyCoolTone()       { filterImage(this::coolTone); }
    @FXML private void applyNoise()          { filterImage(this::noise); }
    @FXML private void applyPixelate()       { filterImage(this::pixelate); }
    @FXML private void applyTintRed()        { filterImage(this::tintRed); }
    @FXML private void applyTintBlue()       { filterImage(this::tintBlue); }
    @FXML private void applyDesaturate()     { filterImage(this::desaturate); }
    @FXML private void applyHighlightBoost() { filterImage(this::highlightBoost); }
    @FXML private void applyFade()           { filterImage(this::fade); }

    // =============================================
    //   FILTER IMPLEMENTATIONS
    // =============================================

    private Image convolve(Image img, double[][] kernel) {
        int kw = kernel[0].length, kh = kernel.length;
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader r = img.getPixelReader(); PixelWriter wtr = out.getPixelWriter();
        int kHalfW = kw / 2, kHalfH = kh / 2;

        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                double rr = 0, gg = 0, bb = 0;
                for (int ky = 0; ky < kh; ky++) {
                    int iy = y + ky - kHalfH;
                    if (iy < 0 || iy >= h) continue;
                    for (int kx = 0; kx < kw; kx++) {
                        int ix = x + kx - kHalfW;
                        if (ix < 0 || ix >= w) continue;
                        double kval = kernel[ky][kx];
                        javafx.scene.paint.Color c = r.getColor(ix, iy);
                        rr += c.getRed() * kval;
                        gg += c.getGreen() * kval;
                        bb += c.getBlue() * kval;
                    }
                }
                rr = clamp(rr); gg = clamp(gg); bb = clamp(bb);
                wtr.setColor(x, y, new javafx.scene.paint.Color(rr, gg, bb, 1.0));
            }
        return out;
    }

    private Image grayscale(Image img) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader r = img.getPixelReader(); PixelWriter wtr = out.getPixelWriter();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                javafx.scene.paint.Color c = r.getColor(x, y);
                double g = (c.getRed() + c.getGreen() + c.getBlue()) / 3.0;
                wtr.setColor(x, y, new javafx.scene.paint.Color(g, g, g, c.getOpacity()));
            }
        return out;
    }

    private Image invert(Image img) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader r = img.getPixelReader(); PixelWriter wtr = out.getPixelWriter();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                javafx.scene.paint.Color c = r.getColor(x, y);
                wtr.setColor(x, y, new javafx.scene.paint.Color(1 - c.getRed(), 1 - c.getGreen(), 1 - c.getBlue(), c.getOpacity()));
            }
        return out;
    }

    private Image sepia(Image img) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader r = img.getPixelReader(); PixelWriter wtr = out.getPixelWriter();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                javafx.scene.paint.Color c = r.getColor(x, y);
                double tr = Math.min(0.393 * c.getRed() + 0.769 * c.getGreen() + 0.189 * c.getBlue(), 1.0);
                double tg = Math.min(0.349 * c.getRed() + 0.686 * c.getGreen() + 0.168 * c.getBlue(), 1.0);
                double tb = Math.min(0.272 * c.getRed() + 0.534 * c.getGreen() + 0.131 * c.getBlue(), 1.0);
                wtr.setColor(x, y, new javafx.scene.paint.Color(tr, tg, tb, c.getOpacity()));
            }
        return out;
    }

    private Image rgbFilter(Image img) {
        return tint(img, 1.2, 0.8, 0.8);
    }

    private Image sharpen(Image img) {
        return convolve(img, new double[][]{
                {0, -1, 0},
                {-1, 5, -1},
                {0, -1, 0}
        });
    }

    private Image edgeDetect(Image img) {
        return convolve(img, new double[][]{
                {-1, -1, -1},
                {-1, 8, -1},
                {-1, -1, -1}
        });
    }
    private Image posterize(Image img) {
        int levels = 4;
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader pr = img.getPixelReader();
        PixelWriter pw = out.getPixelWriter();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                javafx.scene.paint.Color c = pr.getColor(x, y);

                // Calculate posterized channels safely
                double rVal = Math.floor(c.getRed() * levels) / levels;
                double gVal = Math.floor(c.getGreen() * levels) / levels;
                double bVal = Math.floor(c.getBlue() * levels) / levels;

                // Clamp values to prevent out-of-range exceptions
                rVal = Math.min(1.0, Math.max(0.0, rVal));
                gVal = Math.min(1.0, Math.max(0.0, gVal));
                bVal = Math.min(1.0, Math.max(0.0, bVal));

                pw.setColor(x, y, new javafx.scene.paint.Color(rVal, gVal, bVal, c.getOpacity()));
            }
        }
        return out;
    }


    private Image emboss(Image img) {
        return convolve(img, new double[][]{
                {-2, -1, 0},
                {-1, 1, 1},
                {0, 1, 2}
        });
    }

    // --- extra effects ---
    private Image vignette(Image img) { /* same as before */ return img; }
    private Image warmTone(Image img) { return tint(img, 1.1, 1.0, 0.9); }
    private Image coolTone(Image img) { return tint(img, 0.9, 1.0, 1.1); }
    private Image noise(Image img) { return img; }
    private Image pixelate(Image img) { return img; }
    private Image tintRed(Image img) { return tint(img, 1.1, 0.9, 0.9); }
    private Image tintBlue(Image img){ return tint(img, 0.9, 0.9, 1.1); }
    private Image desaturate(Image img) { return img; }
    private Image highlightBoost(Image img){ return img; }
    private Image fade(Image img){ return img; }

    private Image tint(Image img, double rFac, double gFac, double bFac) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader pr = img.getPixelReader(); PixelWriter pw = out.getPixelWriter();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                javafx.scene.paint.Color c = pr.getColor(x, y);
                pw.setColor(x, y, new javafx.scene.paint.Color(
                        clamp(c.getRed() * rFac),
                        clamp(c.getGreen() * gFac),
                        clamp(c.getBlue() * bFac),
                        c.getOpacity()));
            }
        return out;
    }

    private double clamp(double v) { return Math.max(0, Math.min(v, 1)); }

 // ✅ Correct version — keep only this one
    @FXML
    private void handleBack(ActionEvent event) throws java.io.IOException {
        javafx.scene.Parent mainView = javafx.fxml.FXMLLoader.load(getClass().getResource("/fxml/main_view.fxml"));
        javafx.stage.Stage stage = (javafx.stage.Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(mainView);

        // Force JavaFX to re-layout and resize everything
        stage.sizeToScene();
        stage.centerOnScreen();
    }


}
