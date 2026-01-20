package application;

import application.controller.SceneManager;
import db.DatabaseSetup;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // ✅ Initialize database once
            DatabaseSetup.initialize();

            // ✅ Load initial Login view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login_view.fxml"));
            Parent root = loader.load();

            // ✅ Create one shared scene
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());

            // ✅ Configure stage
            primaryStage.setTitle("VisionForge - Login");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
            primaryStage.show();

            // ✅ Register this stage globally
            SceneManager.setStage(primaryStage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
