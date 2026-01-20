package application.controller;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class ImageSliderView {

    @FXML private ImageView sliderImage;
    @FXML private Label imageInfoLabel;

    private final ImageSlider imageSlider = new ImageSlider();

    @FXML
    public void initialize() {
        updateSlider();
    }

    @FXML
    private void handleAddImages(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Images");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Stage stage = (Stage) sliderImage.getScene().getWindow();
        List<File> files = fileChooser.showOpenMultipleDialog(stage);
        if (files != null && !files.isEmpty()) {
            List<String> paths = files.stream().map(File::getAbsolutePath).collect(Collectors.toList());
            imageSlider.addImages(paths);
            updateSlider();
        }
    }

    @FXML
    private void showPrev(ActionEvent event) {
        imageSlider.prev();
        updateSlider();
    }

    @FXML
    private void showNext(ActionEvent event) {
        imageSlider.next();
        updateSlider();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard_view.fxml"));
            Parent dashboard = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
            BorderPane mainRoot = (BorderPane) stage.getScene().getRoot();
            mainRoot.setCenter(dashboard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSlider() {
        if (!imageSlider.hasImages()) {
            sliderImage.setImage(null);
            imageInfoLabel.setText("No images loaded. Click 'Add Images' to begin.");
        } else {
            sliderImage.setImage(imageSlider.getCurrentImage());
            imageInfoLabel.setText("Image " + (imageSlider.getCurrentIndex() + 1)
                    + " of " + imageSlider.getImageCount());
        }
    }
}
