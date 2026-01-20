package application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class ExploreController {

    @FXML private TilePane exploreTilePane;
    @FXML private Label exploreStatusLabel;

    @FXML
    public void initialize() {
        // ✅ Load sample items — replace with DB/content feed later
        addExploreCard("Modern Brochure", "/images/explore1.png");
        addExploreCard("Startup Landing Page", "/images/explore2.png");
        addExploreCard("Social Media Post", "/images/explore3.png");
        addExploreCard("Minimal Portfolio", "/images/explore4.png");
    }

    private void addExploreCard(String title, String imagePath) {
        VBox card = new VBox(10);
        card.setStyle(
                "-fx-background-color: white; " +
                "-fx-border-radius: 12; " +
                "-fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, #cbd5e1, 4, 0, 0, 2); " +
                "-fx-padding: 16; " +
                "-fx-alignment: center;"
        );
        card.setPrefWidth(220);

        // ✅ Load image safely
        ImageView imageView;
        try {
            imageView = new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));
        } catch (Exception e) {
            imageView = new ImageView();
        }
        imageView.setFitWidth(180);
        imageView.setFitHeight(110);
        imageView.setPreserveRatio(true);

        Label label = new Label(title);
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        Button viewBtn = new Button("Preview");
        viewBtn.setStyle("-fx-background-radius: 8; -fx-background-color: #64748b; -fx-text-fill: white;");
        viewBtn.setOnAction(_ -> {
            System.out.println("Previewing: " + title);
            exploreStatusLabel.setText("Preview opened for: " + title);
        });

        Button openBtn = new Button("Open");
        openBtn.setStyle("-fx-background-radius: 8; -fx-background-color: #38bdf8; -fx-text-fill: white;");
        openBtn.setOnAction(_ -> {
            System.out.println("Opened project: " + title);
            exploreStatusLabel.setText("Opened: " + title);
        });

        HBox buttonBox = new HBox(10, viewBtn, openBtn);
        buttonBox.setStyle("-fx-alignment: center;");

        card.getChildren().addAll(imageView, label, buttonBox);
        exploreTilePane.getChildren().add(card);
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard_view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            exploreStatusLabel.setText("Failed to go back to Dashboard.");
        }
    }
}
