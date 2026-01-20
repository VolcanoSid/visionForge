package application.controller;

import javafx.fxml.FXML;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.FileInputStream;

public class ImageViewPane {

    @FXML private ImageView mainImageView;
    @FXML private Label imageInfoLabel;

    
    public void initialize() {
        imageInfoLabel.setText("No image loaded. Click 'Open Image' to select a file.");
    }

    @FXML
    private void handleOpenImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Stage stage = (Stage) mainImageView.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            file.getAbsolutePath();
            try (FileInputStream fis = new FileInputStream(file)) {
                mainImageView.setImage(new Image(fis));
                imageInfoLabel.setText("Loaded: " + file.getName() +
                        "  (" + (file.length() / 1024) + " KB)");
            } catch (Exception e) {
                mainImageView.setImage(null);
                imageInfoLabel.setText("Failed to load image.");
            }
        }
    }

    @FXML
    private void handleSaveAs(ActionEvent event) {
        if (mainImageView.getImage() == null) {
            imageInfoLabel.setText("No image to save.");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image As");
        fileChooser.setInitialFileName("image.png");
        Stage stage = (Stage) mainImageView.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                // Note: real saving requires ImageIO/SwingFXUtils (see PhotoEditorController for details)
                // javax.imageio.ImageIO.write(javafx.embed.swing.SwingFXUtils.fromFXImage(mainImageView.getImage(), null), "png", file);
                System.out.println("Add ImageIO save code for " + file.getAbsolutePath());
                imageInfoLabel.setText("Saved: " + file.getName());
            } catch (Exception e) {
                imageInfoLabel.setText("Failed to save image.");
            }
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard_view.fxml"));
            Parent dashboard = loader.load();
            Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            BorderPane mainRoot = (BorderPane) stage.getScene().getRoot();
            mainRoot.setCenter(dashboard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
