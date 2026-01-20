package application.controller;

import application.CurrentUser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import java.io.File;

public class ProfileController {

    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private Label statusLabel;
    @FXML private ImageView profileImageView;

    // ==========================
    // Initialization
    // ==========================
    @FXML
    public void initialize() {
        try {
            // Load profile information
            nameField.setText(safe(CurrentUser.getName(), "Guest User"));
            usernameField.setText(safe(CurrentUser.getUsername(), "unknown_user"));
            emailField.setText(safe(CurrentUser.getEmail(), "noemail@domain.com"));

            // Load profile picture if available
            loadProfilePicture(CurrentUser.getProfilePicUrl());

            setStatus("Profile loaded successfully.", false);
        } catch (Exception ex) {
            ex.printStackTrace();
            setStatus("Failed to load profile.", true);
        }
    }

    // ==========================
    // Button Handlers
    // ==========================

    @FXML
    private void handleChangePhoto(ActionEvent event) {
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Profile Photo");
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );

            File file = chooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
            if (file != null) {
                String uri = file.toURI().toString();
                profileImageView.setImage(new Image(uri));
                CurrentUser.setProfilePicUrl(uri);
                setStatus("Profile photo updated successfully.", false);
                System.out.println("[Profile] Updated profile picture: " + file.getAbsolutePath());
            } else {
                setStatus("No image selected.", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setStatus("Error updating profile picture.", true);
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        // Currently read-only; future: update DB
        setStatus("Profile information is up to date.", false);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            CurrentUser.clear();
            switchScene(event, "/fxml/login_view.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            setStatus("Failed to logout.", true);
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            switchScene(event, "/fxml/home_view.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            setStatus("Failed to return to Home.", true);
        }
    }

    // ==========================
    // Utility Methods
    // ==========================

    private void loadProfilePicture(String url) {
        try {
            if (url != null && !url.isBlank() && profileImageView != null) {
                profileImageView.setImage(new Image(url, true));
            }
        } catch (Exception e) {
            System.out.println("[Profile] Failed to load image: " + e.getMessage());
        }
    }

    private void switchScene(ActionEvent event, String fxmlPath) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    private void setStatus(String msg, boolean isError) {
        if (statusLabel != null) {
            statusLabel.setText(msg);
            statusLabel.setStyle(isError
                    ? "-fx-text-fill: #e57373; -fx-font-size: 13px;"
                    : "-fx-text-fill: #059669; -fx-font-size: 13px;");
        }
    }

    private String safe(String... vals) {
        for (String v : vals) {
            if (v != null && !v.isBlank()) return v;
        }
        return "";
    }
}
