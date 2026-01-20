package application.controller;

import javafx.scene.image.Image;

import java.util.Stack;

public class PhotoEditorContext {
    private Image currentImage;
    private final Stack<Image> undoStack = new Stack<>();
    private final Stack<Image> redoStack = new Stack<>();

    // Set a new image and clear redo stack (for normal edits)
    public void setImage(Image image) {
        if (currentImage != null) {
            undoStack.push(currentImage);
            redoStack.clear();
        }
        this.currentImage = image;
    }

    public Image getCurrentImage() {
        return currentImage;
    }

    // Undo/Redo support (call from editor UI)
    public Image undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(currentImage);
            currentImage = undoStack.pop();
        }
        return currentImage;
    }

    public Image redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(currentImage);
            currentImage = redoStack.pop();
        }
        return currentImage;
    }

    public void reset() {
        currentImage = null;
        undoStack.clear();
        redoStack.clear();
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}
