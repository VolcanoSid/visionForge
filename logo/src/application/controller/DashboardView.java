package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class DashboardView {

    @FXML
    private Label welcomeLabel;
    @FXML
    private ListView<String> recentProjectsList;

    @FXML
    public void initialize() {
        // Example: set a welcome message or dynamic stats
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome to your Dashboard! 🚀");
        }
        // TODO: Load real recent projects for the current user
    }

    @FXML
    private void handleExplore(ActionEvent event) {
        switchCenter("/fxml/explore_view.fxml", event);
    }

    @FXML
    private void handlePhotoEditor(ActionEvent event) {
        switchCenter("/fxml/photo_editor_view.fxml", event);
    }

    @FXML
    private void handleProfile(ActionEvent event) {
        switchCenter("/fxml/profile_view.fxml", event);
    }

    @FXML
    private void handleImageSlider(ActionEvent event) {
        switchCenter("/fxml/image_slider_view.fxml", event);
    }

    @FXML
    private void handleLabelManager(ActionEvent event) {
        switchCenter("/fxml/label_view.fxml", event);
    }

    @FXML
    private void handleVideoPlayer(ActionEvent event) {
        switchCenter("/fxml/video_player_view.fxml", event);
    }

    @FXML
    private void handleImageViewer(ActionEvent event) {
        switchCenter("/fxml/image_view_pane.fxml", event);
    }

    // --- Utility: swap center of main BorderPane ---
    private void switchCenter(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
            BorderPane mainRoot = (BorderPane) stage.getScene().getRoot();
            mainRoot.setCenter(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
