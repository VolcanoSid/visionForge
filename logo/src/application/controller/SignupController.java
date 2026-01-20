package application.controller;

import db.Model.User;
import db.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailField;
    @FXML private Label statusLabel;

    @FXML
    private void handleSignup() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String email = emailField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            statusLabel.setText("Please fill in all fields.");
            return;
        }

        // Optionally add basic email validation
        if (!email.contains("@") || !email.contains(".")) {
            statusLabel.setText("Please enter a valid email address.");
            return;
        }

        User user = new User(
        	    0,
        	    username,
        	    "",         // name
        	    email,
        	    password,
        	    ""          // profilePicUrl
        	);

        boolean success = UserDAO.addUser(user);
        if (success) {
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Signup successful! You can now log in.");
            usernameField.clear();
            passwordField.clear();
            emailField.clear();
        } else {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Signup failed! Username or email may already exist.");
        }
        
    }
    
    @FXML
    private void handleBackToLogin(javafx.event.ActionEvent event) throws Exception {
        javafx.scene.Parent loginView = javafx.fxml.FXMLLoader.load(getClass().getResource("/fxml/login_view.fxml"));
        javafx.stage.Stage stage = (javafx.stage.Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(loginView);
    }

}
