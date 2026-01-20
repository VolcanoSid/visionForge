package application.controller;

import application.CurrentUser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class HeaderBarController {
    @FXML private ImageView logo;
    @FXML private Label appName;
    @FXML private TextField searchBar;
    @FXML private Label profileName;
    @FXML private ImageView profilePic;

    @FXML
    public void initialize() {
        // Logo and app name
        appName.setText("VisionForge");
        // Set logo if you have one
        // logo.setImage(new Image("/logo/logo.png"));

        // User info (uses CurrentUser)
        profileName.setText(CurrentUser.getName());
        // If you have a profile pic path
        // profilePic.setImage(new Image(CurrentUser.getProfilePicUrl()));

        // Search event (optional)
        searchBar.setOnAction(_ -> handleSearch());
    }

    private void handleSearch() {
        String query = searchBar.getText();
        System.out.println("Search: " + query);
    }
}
