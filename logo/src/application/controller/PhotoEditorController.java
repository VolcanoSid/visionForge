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

public class PhotoEditorController {

    @FXML private ImageView imageView;
    @FXML private Slider brightnessSlider, contrastSlider;
    @FXML private ComboBox<String> effectSelector;

    private Image originalImage;
    private Stack<Image> undoStack = new Stack<>();
    private Stack<Image> redoStack = new Stack<>();

    @FXML
    private void initialize() {
        effectSelector.getItems().addAll(
                "Grayscale", "Invert", "Sepia", "RGB Filter", "Blur", "Sharpen",
                "Edge Detect", "Posterize", "Emboss",
                "Vignette", "Warm Tone", "Cool Tone", "Noise",
                "Pixelate", "Tint Red", "Tint Blue",
                "Desaturate", "Highlight Boost", "Fade"
        );
    }

    // ---------- IMAGE LOADING ----------
    @FXML
    private void handleOpenImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            originalImage = new Image(file.toURI().toString());
            imageView.setImage(originalImage);
            undoStack.clear();
            redoStack.clear();
        }
    }

    // ---------- IMAGE SAVE ----------
    @FXML
    private void handleSaveImage() {
        if (imageView.getImage() == null) return;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG Image", "*.png")
        );
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                BufferedImage bImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
                javax.imageio.ImageIO.write(bImage, "png", file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ---------- APPLY FILTER ----------
    @FXML
    private void handleApplyFilter() {
        if (imageView.getImage() == null) return;
        String effect = effectSelector.getValue();
        if (effect == null) return;

        pushUndo();

        Image img = imageView.getImage();
        Image out = switch (effect) {
            case "Grayscale" -> grayscale(img);
            case "Invert" -> invert(img);
            case "Sepia" -> sepia(img);
            case "RGB Filter" -> rgbFilter(img);
            case "Blur" -> blur(img);
            case "Sharpen" -> sharpen(img);
            case "Edge Detect" -> edgeDetect(img);
            case "Posterize" -> posterize(img);
            case "Emboss" -> emboss(img);
            case "Vignette" -> vignette(img);
            case "Warm Tone" -> warmTone(img);
            case "Cool Tone" -> coolTone(img);
            case "Noise" -> noise(img);
            case "Pixelate" -> pixelate(img);
            case "Tint Red" -> tintRed(img);
            case "Tint Blue" -> tintBlue(img);
            case "Desaturate" -> desaturate(img);
            case "Highlight Boost" -> highlightBoost(img);
            case "Fade" -> fade(img);
            default -> img;
        };

        imageView.setImage(out);
    }

    // ---------- UNDO / REDO ----------
    @FXML private void handleUndo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(imageView.getImage());
            imageView.setImage(undoStack.pop());
        }
    }

    @FXML private void handleRedo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(imageView.getImage());
            imageView.setImage(redoStack.pop());
        }
    }

    private void pushUndo() {
        if (imageView.getImage() != null)
            undoStack.push(imageView.getImage());
        redoStack.clear();
    }

    // ---------- RESET ----------
    @FXML
    private void handleReset() {
        if (originalImage != null) {
            imageView.setImage(originalImage);
            undoStack.clear();
            redoStack.clear();
        }
        brightnessSlider.setValue(1);
        contrastSlider.setValue(1);
        effectSelector.setValue(null);
    }

    // ---------- NAVIGATION ----------
    @FXML
    private void handleBack(ActionEvent event) throws java.io.IOException {
        javafx.scene.Parent mainView = javafx.fxml.FXMLLoader.load(getClass().getResource("/fxml/dashboard_view.fxml"));
        javafx.stage.Stage stage = (javafx.stage.Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(mainView);
    }

    // ============================================================
    // FILTER IMPLEMENTATIONS
    // ============================================================

    private Image grayscale(Image img) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader r = img.getPixelReader();
        PixelWriter wtr = out.getPixelWriter();
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
        PixelReader r = img.getPixelReader();
        PixelWriter wtr = out.getPixelWriter();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                javafx.scene.paint.Color c = r.getColor(x, y);
                wtr.setColor(x, y, new javafx.scene.paint.Color(1-c.getRed(), 1-c.getGreen(), 1-c.getBlue(), c.getOpacity()));
            }
        return out;
    }

    private Image sepia(Image img) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader r = img.getPixelReader();
        PixelWriter wtr = out.getPixelWriter();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                javafx.scene.paint.Color c = r.getColor(x, y);
                double tr = Math.min(0.393*c.getRed() + 0.769*c.getGreen() + 0.189*c.getBlue(), 1.0);
                double tg = Math.min(0.349*c.getRed() + 0.686*c.getGreen() + 0.168*c.getBlue(), 1.0);
                double tb = Math.min(0.272*c.getRed() + 0.534*c.getGreen() + 0.131*c.getBlue(), 1.0);
                wtr.setColor(x, y, new javafx.scene.paint.Color(tr, tg, tb, c.getOpacity()));
            }
        return out;
    }

    private Image rgbFilter(Image img) {
        return tint(img, 1.2, 0.8, 0.8);
    }

    private Image blur(Image img) {
        return convolve(img, new double[][]{
                {1/9f,1/9f,1/9f},
                {1/9f,1/9f,1/9f},
                {1/9f,1/9f,1/9f}
        });
    }

    private Image sharpen(Image img) {
        return convolve(img, new double[][]{
                {0,-1,0},
                {-1,5,-1},
                {0,-1,0}
        });
    }

    private Image edgeDetect(Image img) {
        return convolve(img, new double[][]{
                {-1,-1,-1},
                {-1,8,-1},
                {-1,-1,-1}
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

                // ✅ Safe calculation (never goes above 1.0)
                double rVal = Math.floor(c.getRed() * levels) / levels;
                double gVal = Math.floor(c.getGreen() * levels) / levels;
                double bVal = Math.floor(c.getBlue() * levels) / levels;

                // ✅ Clamp to avoid floating-point rounding overflow
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
                {-2,-1,0},
                {-1,1,1},
                {0,1,2}
        });
    }

    private Image vignette(Image img) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader pr = img.getPixelReader(); PixelWriter pw = out.getPixelWriter();
        double cx = w / 2.0, cy = h / 2.0, maxDist = Math.sqrt(cx*cx + cy*cy);
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                javafx.scene.paint.Color c = pr.getColor(x, y);
                double dist = Math.sqrt(Math.pow(x - cx, 2) + Math.pow(y - cy, 2)) / maxDist;
                double factor = 1 - Math.pow(dist, 2.2);
                pw.setColor(x, y, c.darker().interpolate(c, factor));
            }
        return out;
    }

    private Image warmTone(Image img) { return tint(img, 1.1, 1.0, 0.9); }
    private Image coolTone(Image img) { return tint(img, 0.9, 1.0, 1.1); }

    private Image noise(Image img) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader pr = img.getPixelReader(); PixelWriter pw = out.getPixelWriter();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                javafx.scene.paint.Color c = pr.getColor(x, y);
                double n = (Math.random() - 0.5) * 0.08;
                pw.setColor(x, y, new javafx.scene.paint.Color(
                        clamp(c.getRed() + n),
                        clamp(c.getGreen() + n),
                        clamp(c.getBlue() + n),
                        c.getOpacity()));
            }
        return out;
    }

    private Image pixelate(Image img) {
        int block = 8;
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader pr = img.getPixelReader(); PixelWriter pw = out.getPixelWriter();
        for (int by = 0; by < h; by += block)
            for (int bx = 0; bx < w; bx += block) {
                double r=0,g=0,b=0; int count=0;
                for (int y = by; y < by+block && y<h; y++)
                    for (int x = bx; x < bx+block && x<w; x++) {
                        javafx.scene.paint.Color c = pr.getColor(x, y);
                        r+=c.getRed(); g+=c.getGreen(); b+=c.getBlue(); count++;
                    }
                javafx.scene.paint.Color avg = new javafx.scene.paint.Color(r/count, g/count, b/count, 1);
                for (int y = by; y < by+block && y<h; y++)
                    for (int x = bx; x < bx+block && x<w; x++)
                        pw.setColor(x, y, avg);
            }
        return out;
    }

    private Image tintRed(Image img) { return tint(img, 1.1, 0.9, 0.9); }
    private Image tintBlue(Image img){ return tint(img, 0.9, 0.9, 1.1); }

    private Image desaturate(Image img) {
        int w = (int)img.getWidth(), h = (int)img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader pr = img.getPixelReader(); PixelWriter pw = out.getPixelWriter();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                javafx.scene.paint.Color c = pr.getColor(x, y);
                double gray = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                javafx.scene.paint.Color outC = c.interpolate(new javafx.scene.paint.Color(gray, gray, gray, 1), 0.5);
                pw.setColor(x, y, outC);
            }
        return out;
    }

    private Image highlightBoost(Image img) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader pr = img.getPixelReader(); PixelWriter pw = out.getPixelWriter();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                javafx.scene.paint.Color c = pr.getColor(x, y);
                if (c.getBrightness() > 0.6)
                    c = c.brighter();
                pw.setColor(x, y, c);
            }
        return out;
    }

    private Image fade(Image img) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader pr = img.getPixelReader(); PixelWriter pw = out.getPixelWriter();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                javafx.scene.paint.Color c = pr.getColor(x, y);
                pw.setColor(x, y, c.interpolate(javafx.scene.paint.Color.WHITE, 0.2));
            }
        return out;
    }

    private Image tint(Image img, double rFac, double gFac, double bFac) {
        int w = (int) img.getWidth(), h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader pr = img.getPixelReader(); PixelWriter pw = out.getPixelWriter();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                javafx.scene.paint.Color c = pr.getColor(x, y);
                pw.setColor(x, y, new javafx.scene.paint.Color(
                        clamp(c.getRed()*rFac),
                        clamp(c.getGreen()*gFac),
                        clamp(c.getBlue()*bFac),
                        c.getOpacity()));
            }
        return out;
    }

    private Image convolve(Image img, double[][] kernel) {
        int kw = kernel[0].length, kh = kernel.length;
        int kHalfW = kw/2, kHalfH = kh/2;
        int w = (int)img.getWidth(), h = (int)img.getHeight();
        WritableImage out = new WritableImage(w, h);
        PixelReader r = img.getPixelReader();
        PixelWriter wtr = out.getPixelWriter();

        for (int y = 0; y < h; y++) for (int x = 0; x < w; x++) {
            double rr=0,gg=0,bb=0;
            for (int ky = 0; ky < kh; ky++) {
                int iy = y + ky - kHalfH; if (iy < 0 || iy >= h) continue;
                for (int kx = 0; kx < kw; kx++) {
                    int ix = x + kx - kHalfW; if (ix < 0 || ix >= w) continue;
                    double kval = kernel[ky][kx];
                    javafx.scene.paint.Color c = r.getColor(ix, iy);
                    rr += c.getRed()*kval;
                    gg += c.getGreen()*kval;
                    bb += c.getBlue()*kval;
                }
            }
            rr = clamp(rr); gg = clamp(gg); bb = clamp(bb);
            wtr.setColor(x, y, new javafx.scene.paint.Color(rr, gg, bb, 1.0));
        }
        return out;
    }

    private double clamp(double v) {
        return Math.max(0, Math.min(v, 1));
    }
}
