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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class TemplatesController {

    @FXML private Label templateStatusLabel;
    @FXML private TilePane templateTilePane;

    @FXML
    public void initialize() {
        addTemplateCard("Portfolio Template", "/images/template1.png");
        addTemplateCard("Resume Template", "/images/template2.png");
        addTemplateCard("Newsletter", "/images/template3.png");
    }

    private void addTemplateCard(String title, String imagePath) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-border-radius: 10; -fx-background-radius: 10; "
                + "-fx-effect: dropshadow(gaussian, #cbd5e1, 4, 0, 0, 2); -fx-padding: 16; -fx-alignment: center;");
        card.setPrefWidth(220);

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

        // ✅ Preview Button
        Button previewBtn = new Button("Preview");
        previewBtn.setStyle("-fx-background-radius: 8; -fx-background-color: #64748b; -fx-text-fill: white;");
        previewBtn.setOnAction(e -> showTemplatePreview(title, imagePath));

        // ✅ Use Template Button
        Button openBtn = new Button("Use Template");
        openBtn.setStyle("-fx-background-radius: 8; -fx-background-color: #38bdf8; -fx-text-fill: white;");
        openBtn.setOnAction(_ -> {
            System.out.println("Opening template: " + title);
            if (templateStatusLabel != null)
                templateStatusLabel.setText("Selected: " + title);
        });

        HBox buttonBox = new HBox(10, previewBtn, openBtn);
        buttonBox.setStyle("-fx-alignment: center;");

        card.getChildren().addAll(imageView, label, buttonBox);
        templateTilePane.getChildren().add(card);
    }

    private void showTemplatePreview(String title, String imagePath) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Template Preview");

        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: #f8fafc; -fx-padding: 32; -fx-alignment: center;");

        ImageView largeView;
        try {
            largeView = new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));
        } catch (Exception e) {
            largeView = new ImageView();
        }
        largeView.setFitWidth(350);
        largeView.setFitHeight(200);
        largeView.setPreserveRatio(true);

        Label nameLabel = new Label(title);
        nameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #22223b;");

        Label desc = new Label("This is a preview for " + title + ".\nAdd real descriptions later from DB/config.");
        desc.setStyle("-fx-font-size: 14px; -fx-text-fill: #475569;");
        desc.setWrapText(true);

        HBox buttonBox = new HBox(16);
        buttonBox.setStyle("-fx-alignment: center;");

        Button useBtn = new Button("Use Template");
        useBtn.setStyle("-fx-background-radius: 8; -fx-background-color: #38bdf8; -fx-text-fill: white;");
        useBtn.setOnAction(_ -> {
            popup.close();
            System.out.println("Template used: " + title);
        });

        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(_ -> popup.close());

        buttonBox.getChildren().addAll(useBtn, closeBtn);
        root.getChildren().addAll(largeView, nameLabel, desc, buttonBox);

        popup.setScene(new Scene(root, 400, 370));
        popup.showAndWait();
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
        }
    }
}
