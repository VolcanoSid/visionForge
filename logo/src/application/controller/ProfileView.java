package application.controller;

import application.CurrentUser;
import db.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;

public class ProfileView {

    @FXML private ImageView profileImage;
    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private Label statusLabel;

    private String profilePicUrl = "";

    @FXML
    public void initialize() {
        nameField.setText(CurrentUser.getName());
        usernameField.setText(CurrentUser.getUsername());
        emailField.setText(CurrentUser.getEmail());
        profilePicUrl = CurrentUser.getProfilePicUrl();

        boolean loaded = false;
        // Try user-set image path (local file)
        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
            try (FileInputStream fis = new FileInputStream(profilePicUrl)) {
                profileImage.setImage(new Image(fis));
                loaded = true;
            } catch (Exception e) {
                // Do nothing yet, will fall back to default
            }
        }
        // Fallback: bundled resource
        if (!loaded) {
            try {
                profileImage.setImage(
                    new Image(getClass().getResource("/images/default_profile.png").toExternalForm())
                );
            } catch (Exception e) {
                // Fallback to nothing if the image is missing (won't crash)
                profileImage.setImage(null);
            }
        }
    }

    @FXML
    private void handleChangePhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Photo");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Stage stage = (Stage) profileImage.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try (FileInputStream fis = new FileInputStream(file)) {
                profileImage.setImage(new Image(fis));
                profilePicUrl = file.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
                statusLabel.setText("Failed to set profile image.");
            }
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        String newName = nameField.getText();
        if (UserDAO.updateUserProfile(CurrentUser.getId(), newName, profilePicUrl)) {
            statusLabel.setText("Profile updated!");
            CurrentUser.setName(newName);
            CurrentUser.setProfilePicUrl(profilePicUrl);
        } else {
            statusLabel.setText("Failed to update profile.");
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login_view.fxml"));
            Parent login = loader.load();
            Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(login));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
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
