package application.controller;

import java.io.File;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurface;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class VideoController {

    @FXML private AnchorPane videoPane;
    @FXML private ImageView videoImageView;
    @FXML private Slider volumeSlider;
    @FXML private Slider seekBar;
    @FXML private Text fileNameText;

    private EmbeddedMediaPlayer mediaPlayer;
    private Timeline seekUpdater;

    @FXML
    public void initialize() {
        System.out.println("JARs loaded from: " + System.getProperty("java.class.path"));

        System.setProperty("jna.library.path", "C:/Program Files/VideoLAN/VLC");
        System.setProperty("VLC_PLUGIN_PATH", "C:/Program Files/VideoLAN/VLC/plugins");

        try {
            Class.forName("uk.co.caprica.vlcj.binding.RuntimeUtil");
            System.out.println("VLCJ core class is available!");
        } catch (ClassNotFoundException e) {
            System.out.println("VLCJ core class is missing! Check your .jar files.");
            e.printStackTrace();
            return;
        }

        try {
            MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
            mediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();

            System.out.println("VLC version: " + mediaPlayerFactory.application().version());
            System.out.println("MediaPlayer created: " + (mediaPlayer != null));

            ImageViewVideoSurface videoSurface = new ImageViewVideoSurface(videoImageView);
            mediaPlayer.videoSurface().set(videoSurface);

            // Initialize volume
            if (volumeSlider != null) {
                volumeSlider.setValue(50);
                mediaPlayer.audio().setVolume(50);
            }

            // Set up seek bar updater
            if (seekBar != null) {
                seekUpdater = new Timeline(new KeyFrame(Duration.millis(500), _ -> updateSeekBar()));
                seekUpdater.setCycleCount(Timeline.INDEFINITE);
            }
        } catch (Exception ex) {
            System.out.println("Failed to initialize VLCJ media player.");
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleLoadVideo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Video File");
        fileChooser.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.mkv", "*.avi", "*.mov", "*.flv"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null && mediaPlayer != null) {
            System.out.println("Attempting to play: " + file.getAbsolutePath());
            boolean started = mediaPlayer.media().play(file.getAbsolutePath());
            System.out.println("Play started: " + started);

            // Update file name in UI
            if (fileNameText != null) {
                fileNameText.setText(file.getName());
            }

            // Start seek bar updater
            if (seekUpdater != null) {
                seekUpdater.play();
            }
        } else {
            System.out.println("File was null or mediaPlayer was not initialized!");
        }
    }

    @FXML
    private void handlePlay() {
        if (mediaPlayer != null) {
            System.out.println("Play called");
            mediaPlayer.controls().play();
            if (seekUpdater != null) seekUpdater.play();
        }
    }

    @FXML
    private void handlePause() {
        if (mediaPlayer != null) {
            System.out.println("Pause called");
            mediaPlayer.controls().pause();
            if (seekUpdater != null) seekUpdater.pause();
        }
    }

    @FXML
    private void handleVolumeChange() {
        if (mediaPlayer != null && volumeSlider != null) {
            mediaPlayer.audio().setVolume((int) volumeSlider.getValue());
        }
    }

    @FXML
    private void pauseForSeek() {
        if (mediaPlayer != null) {
            mediaPlayer.controls().pause();
        }
    }

    @FXML
    private void seekToPosition() {
        if (mediaPlayer != null && seekBar != null) {
            long length = mediaPlayer.status().length();
            mediaPlayer.controls().setTime((long) (seekBar.getValue() / 100 * length));
            mediaPlayer.controls().play();
        }
    }

    private void updateSeekBar() {
        if (mediaPlayer != null && seekBar != null && mediaPlayer.status().isPlaying()) {
            long time = mediaPlayer.status().time();
            long length = mediaPlayer.status().length();
            if (length > 0) {
                double pos = (double) time / length * 100;
                seekBar.setValue(pos);
            }
        }
    }

    @FXML
    private void toggleFullScreen(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    private void handleBack(ActionEvent event) throws java.io.IOException {
        Parent mainView = FXMLLoader.load(getClass().getResource("/fxml/main_view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(mainView);

        if (mediaPlayer != null) {
            mediaPlayer.controls().stop();
        }
        if (seekUpdater != null) {
            seekUpdater.stop();
        }
    }
}
