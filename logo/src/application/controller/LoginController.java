package application.controller;

import db.UserDAO;
import db.Model.User;
import application.CurrentUser;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = UserDAO.authenticate(username, password);

        if (user != null) {
            // Save user info globally
            CurrentUser.setId(user.getId());
            CurrentUser.setUsername(user.getUsername());
            CurrentUser.setName(user.getName());
            CurrentUser.setEmail(user.getEmail());
            CurrentUser.setProfilePicUrl(user.getProfilePicUrl());

            // Load dashboard
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard_view.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);

               
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                errorLabel.setText("Login succeeded but failed to load dashboard.");
            }
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    private void handleSignup(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signup_view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // If using CSS:
            // scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());

            Stage stage = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load signup screen.");
        }
    }
}
