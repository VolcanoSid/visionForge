package application.controller;

import javafx.fxml.FXML;
import javafx.scene.media.*;
import javafx.stage.FileChooser;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;

public class VideoPlayerController {

    @FXML private MediaView mediaView;
    @FXML private Label videoStatusLabel;

    private MediaPlayer mediaPlayer;

    @FXML
    private void handleOpenVideo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Video File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.flv", "*.m4v", "*.mpg", "*.mpeg", "*.avi")
        );
        Stage stage = (Stage) mediaView.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                Media media = new Media(file.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);
                videoStatusLabel.setText("Loaded: " + file.getName());
            } catch (Exception e) {
                e.printStackTrace();
                videoStatusLabel.setText("Failed to load video.");
            }
        }
    }

    @FXML
    private void handlePlay(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.play();
            videoStatusLabel.setText("Playing");
        }
    }

    @FXML
    private void handlePause(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            videoStatusLabel.setText("Paused");
        }
    }

    @FXML
    private void handleStop(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            videoStatusLabel.setText("Stopped");
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
