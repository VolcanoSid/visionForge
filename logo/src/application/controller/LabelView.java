package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class LabelView {

    @FXML private TextField labelField;
    @FXML private ListView<String> labelListView;
    @FXML private Label addStatusLabel;

    private final List<String> labels = new ArrayList<>();

    @FXML
    public void initialize() {
        refreshList();
    }

    @FXML
    private void handleAddLabel(ActionEvent event) {
        String text = labelField.getText();
        if (text != null && !text.trim().isEmpty() && !labels.contains(text.trim())) {
            labels.add(text.trim());
            labelField.clear();
            addStatusLabel.setText("Label added.");
            refreshList();
        } else {
            addStatusLabel.setText("Enter a unique, non-empty label.");
        }
    }

    @FXML
    private void handleRemoveLabel(ActionEvent event) {
        String selected = labelListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            labels.remove(selected);
            addStatusLabel.setText("Label removed.");
            refreshList();
        } else {
            addStatusLabel.setText("Select a label to remove.");
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

    private void refreshList() {
        labelListView.getItems().setAll(labels);
    }
}
