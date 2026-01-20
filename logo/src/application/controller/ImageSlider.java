package application.controller;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageSlider {
    private final List<String> imagePaths = new ArrayList<>();
    private int currentIndex = 0;

    public void addImages(List<String> paths) {
        imagePaths.addAll(paths);
        if (imagePaths.size() > 0 && currentIndex >= imagePaths.size()) {
            currentIndex = 0;
        }
    }

    public void addImage(String path) {
        imagePaths.add(path);
        if (imagePaths.size() > 0 && currentIndex >= imagePaths.size()) {
            currentIndex = 0;
        }
    }

    public void clearImages() {
        imagePaths.clear();
        currentIndex = 0;
    }

    public boolean hasImages() {
        return !imagePaths.isEmpty();
    }

    public int getImageCount() {
        return imagePaths.size();
    }

    public Image getCurrentImage() {
        if (imagePaths.isEmpty()) return null;
        try (FileInputStream fis = new FileInputStream(imagePaths.get(currentIndex))) {
            return new Image(fis);
        } catch (Exception e) {
            return null;
        }
    }

    public String getCurrentImagePath() {
        if (imagePaths.isEmpty()) return "";
        return imagePaths.get(currentIndex);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void next() {
        if (!imagePaths.isEmpty()) {
            currentIndex = (currentIndex + 1) % imagePaths.size();
        }
    }

    public void prev() {
        if (!imagePaths.isEmpty()) {
            currentIndex = (currentIndex - 1 + imagePaths.size()) % imagePaths.size();
        }
    }
}
