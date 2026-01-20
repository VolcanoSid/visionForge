package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class TemplatePreviewController {
    @FXML private Label templateNameLabel;
    @FXML private Label templateDescLabel;
    @FXML private ImageView templateImageView;

    private String templateName;

    // Call this from TemplatesController to set preview data
    public void setTemplate(String templateName) {
        this.templateName = templateName;
        templateNameLabel.setText(templateName);

        // You can use a switch or map for real preview images and descriptions
        switch (templateName) {
            case "Portfolio Template":
                templateDescLabel.setText("A modern, professional portfolio layout for creative work.");
                templateImageView.setImage(new Image(getClass().getResourceAsStream("/images/portfolio_preview.png")));
                break;
            case "Resume Template":
                templateDescLabel.setText("A clean resume format with easy-to-edit sections.");
                templateImageView.setImage(new Image(getClass().getResourceAsStream("/images/resume_preview.png")));
                break;
            case "Newsletter":
                templateDescLabel.setText("A colorful newsletter template for updates and promotions.");
                templateImageView.setImage(new Image(getClass().getResourceAsStream("/images/newsletter_preview.png")));
                break;
            case "Invoice Template":
                templateDescLabel.setText("A simple invoice template for businesses and freelancers.");
                templateImageView.setImage(new Image(getClass().getResourceAsStream("/images/invoice_preview.png")));
                break;
            default:
                templateDescLabel.setText("A sample template preview. (Replace with real preview!)");
                templateImageView.setImage(null);
        }
    }

    @FXML
    private void handleUseTemplate(ActionEvent event) {
        // Your logic for using the template can go here
        ((Stage) templateNameLabel.getScene().getWindow()).close();
    }

    @FXML
    private void handleClose(ActionEvent event) {
        ((Stage) templateNameLabel.getScene().getWindow()).close();
    }
}
