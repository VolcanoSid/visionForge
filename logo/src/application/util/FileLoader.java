package application.util;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;

/**
 * Utility class to simplify file selection and loading.
 * Used across modules like PhotoEditorController, VideoController, etc.
 */
public class FileLoader {

    /**
     * Opens a FileChooser dialog for selecting an image.
     * @param window Parent window (usually the current Stage)
     * @return Selected File or null if canceled
     */
    public static File chooseImageFile(Window window) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Image File");
        chooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.bmp")
        );
        return chooser.showOpenDialog(window);
    }

    /**
     * Opens a FileChooser dialog for selecting a video.
     * @param window Parent window (usually the current Stage)
     * @return Selected File or null if canceled
     */
    public static File chooseVideoFile(Window window) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Video File");
        chooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.mkv", "*.avi", "*.mov")
        );
        return chooser.showOpenDialog(window);
    }

    /**
     * Opens a FileChooser dialog for selecting a template file.
     * @param window Parent window (usually the current Stage)
     * @return Selected File or null if canceled
     */
    public static File chooseTemplateFile(Window window) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Template File");
        chooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Template Files", "*.tpl", "*.template", "*.json")
        );
        return chooser.showOpenDialog(window);
    }

    /**
     * Generic file chooser for any file type.
     */
    public static File chooseAnyFile(Window window) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select File");
        chooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        return chooser.showOpenDialog(window);
    }
}
