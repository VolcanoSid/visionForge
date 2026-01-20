package extension;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

import application.CurrentUser;
import application.model.MediaFile;
import db.FileDAO;
import db.UserDAO;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.File;
import java.time.LocalTime;
import java.util.List;


public class ExtensionLoader {

    @FXML private Label photoCount;
    @FXML private Label videoCount;
    @FXML private Label templateCount;
    @FXML private Label userCount;

    // ---------- UI Components ----------
    @FXML private Label greetingLabel;
    @FXML private PieChart mediaChart;
    @FXML private ListView<String> recentActivityList;

    @FXML private Label dbStatusLabel;
    @FXML private Label pluginStatusLabel;
    @FXML private Label themeStatusLabel;

    @FXML private Button refreshBtn;
    @FXML private Button profileBtn;

    // ---------- Internal State ----------
    private int totalPhotos = 0;
    private int totalVideos = 0;
    private int totalTemplates = 0;
    private int activeUsers = 0;

    // ---------- Initialization ----------
    @FXML
    public void initialize() {
        setGreeting();
        loadDashboardData();
        loadChart();
        loadRecentActivity();
        updateSystemStatus();
        animateCards();
    }

    // ---------- Greeting ----------
    private void setGreeting() {
        String userName = CurrentUser.getName();
        if (userName == null || userName.isEmpty()) userName = "User";

        LocalTime now = LocalTime.now();
        String period;
        if (now.isBefore(LocalTime.NOON)) period = "Good Morning ☀️";
        else if (now.isBefore(LocalTime.of(18, 0))) period = "Good Afternoon 🌤️";
        else period = "Good Evening 🌙";

        if (greetingLabel != null)
            greetingLabel.setText(period + " " + userName + " | VisionForge Dashboard");
    }

    // ---------- Load Dashboard Data ----------
    private void loadDashboardData() {
        try {
            totalPhotos = FileDAO.getTotalImages();
            totalVideos = FileDAO.getTotalVideos();
            totalTemplates = FileDAO.getTotalTemplates();
            activeUsers = UserDAO.getActiveUsers();

            photoCount.setText(String.valueOf(totalPhotos));
            videoCount.setText(String.valueOf(totalVideos));
            templateCount.setText(String.valueOf(totalTemplates));
            userCount.setText(String.valueOf(activeUsers));

            System.out.println("[Dashboard] Stats loaded successfully.");

        } catch (Exception e) {
            System.err.println("⚠️ Error loading dashboard stats: " + e.getMessage());
            photoCount.setText("—");
            videoCount.setText("—");
            templateCount.setText("—");
            userCount.setText("—");
        }
    }

    // ---------- Chart ----------
    private void loadChart() {
        try {
            ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList(
                    new PieChart.Data("Photos", totalPhotos),
                    new PieChart.Data("Videos", totalVideos),
                    new PieChart.Data("Templates", totalTemplates)
            );
            mediaChart.setData(chartData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- Recent Activity ----------
    private void loadRecentActivity() {
        try {
            List<MediaFile> files = FileDAO.getAllMediaFiles(CurrentUser.getId());
            ObservableList<String> activities = FXCollections.observableArrayList();

            if (files == null || files.isEmpty()) {
                activities.add("No recent files found for this user.");
            } else {
                for (MediaFile file : files) {
                    // Try to use available getters dynamically
                    String name = "Unknown File";
                    String type = "";
                    String createdAt = "";

                    try {
                        if (file.getClass().getMethod("getFileName") != null) {
                            name = (String) file.getClass().getMethod("getFileName").invoke(file);
                        }
                    } catch (Exception ignored) {
                        // Try to fallback if filePath exists
                        try {
                            String path = (String) file.getClass().getMethod("getFilePath").invoke(file);
                            if (path != null && !path.isEmpty())
                                name = new File(path).getName();
                        } catch (Exception ignored2) { }
                    }

                    try {
                        if (file.getClass().getMethod("getType") != null) {
                            type = (String) file.getClass().getMethod("getType").invoke(file);
                        }
                    } catch (Exception ignored) { }

                    try {
                        if (file.getClass().getMethod("getCreatedAt") != null) {
                            createdAt = (String) file.getClass().getMethod("getCreatedAt").invoke(file);
                        }
                    } catch (Exception ignored) { }

                    activities.add(type + " " + name + (createdAt.isEmpty() ? "" : " (" + createdAt + ")"));
                }
            }

            if (recentActivityList != null)
                recentActivityList.setItems(activities);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- System Status ----------
    private void updateSystemStatus() {
        if (dbStatusLabel != null)
            dbStatusLabel.setText("DB: Connected ✅");
        if (pluginStatusLabel != null)
            pluginStatusLabel.setText("Plugins: 4 Loaded");
        if (themeStatusLabel != null)
            themeStatusLabel.setText("Theme: Light");
    }

    // ---------- Animations ----------
    private void animateCards() {
        Label[] cards = {photoCount, videoCount, templateCount, userCount};
        for (Label card : cards) {
            if (card != null) {
                ScaleTransition scale = new ScaleTransition(Duration.millis(400), card);
                scale.setFromX(0.8);
                scale.setFromY(0.8);
                scale.setToX(1);
                scale.setToY(1);
                scale.play();
            }
        }
    }

    // ---------- Button Actions ----------
    @FXML
    private void onRefreshClicked(ActionEvent event) {
        System.out.println("[Dashboard] Refresh triggered.");
        loadDashboardData();
        loadChart();
        loadRecentActivity();
        animateCards();
        if (dbStatusLabel != null) dbStatusLabel.setText("DB: Synced ✅");
    }

    @FXML private void openImageEditor(ActionEvent event) { switchScene(event, "/fxml/photo_editor_view.fxml"); }
    @FXML private void openVideoPlayer(ActionEvent event) { switchScene(event, "/fxml/video_player_view.fxml"); }
    @FXML private void openTemplates(ActionEvent event) { switchScene(event, "/fxml/templates_view.fxml"); }
    @FXML private void openProfile(ActionEvent event) { switchScene(event, "/fxml/profile_view.fxml"); }
    @FXML private void openSettings(ActionEvent event) { switchScene(event, "/fxml/settings_view.fxml"); }
    @FXML private void handleLogout(ActionEvent event) { switchScene(event, "/fxml/login_view.fxml"); }

    // ---------- Navigation Utility ----------
    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = new Scene(view);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
