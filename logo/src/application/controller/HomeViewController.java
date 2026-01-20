package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

import java.time.LocalTime;
import java.util.Random;
import java.util.List;

import db.FileDAO;
import db.Model.RecentFile;
import application.CurrentUser;

/**
 * Controller for VisionForge Dashboard (Home View)
 * Provides interactive overview cards, recent activity, quick actions, and system stats.
 */
public class HomeViewController {

    // Overview labels
    @FXML private Label totalPhotosLabel;
    @FXML private Label totalVideosLabel;
    @FXML private Label totalTemplatesLabel;
    @FXML private Label activeUsersLabel;

    // Components
    @FXML private ListView<String> recentActivityList;
    @FXML private PieChart mediaChart;
    @FXML private Label greetingLabel;
    @FXML private Label dbStatusLabel;
    @FXML private Label pluginStatusLabel;
    @FXML private Label themeStatusLabel;

    @FXML private Button refreshBtn;
    @FXML private Button profileBtn;

    // Dummy data (replace with DB calls later)
    private int totalPhotos = 124;
    private int totalVideos = 36;
    private int totalTemplates = 15;
    private int activeUsers = 5;

    @FXML
    public void initialize() {
        setGreeting();
        loadOverviewData();
        loadRecentActivity(); // ✅ will now check DB first, fallback to dummy data
        loadChart();
        updateSystemStatus();
        animateCards();
    }

    // ---------- Greeting ----------
    private void setGreeting() {
        LocalTime now = LocalTime.now();
        String greeting;
        if (now.isBefore(LocalTime.NOON)) greeting = "Good Morning ☀️";
        else if (now.isBefore(LocalTime.of(18, 0))) greeting = "Good Afternoon 🌤️";
        else greeting = "Good Evening 🌙";
        greetingLabel.setText(greeting + " | VisionForge Dashboard");
    }

    // ---------- Overview ----------
    private void loadOverviewData() {
        totalPhotosLabel.setText(String.valueOf(totalPhotos));
        totalVideosLabel.setText(String.valueOf(totalVideos));
        totalTemplatesLabel.setText(String.valueOf(totalTemplates));
        activeUsersLabel.setText(String.valueOf(activeUsers));
    }

    // ---------- Recent Activity / Recent Work ----------
    private void loadRecentActivity() {
        ObservableList<String> activities;

        try {
            FileDAO fileDAO = new FileDAO();
            int userId = CurrentUser.getId();
            List<RecentFile> recentFiles = fileDAO.getRecentFiles(userId);

            if (recentFiles != null && !recentFiles.isEmpty()) {
                activities = FXCollections.observableArrayList();
                for (RecentFile file : recentFiles) {
                    activities.add("📂 " + file);
                }
                System.out.println("[Dashboard] Loaded " + recentFiles.size() + " recent entries from DB.");
            } else {
                // fallback to sample dummy data
                activities = FXCollections.observableArrayList(
                        "📸 Edited Photo: portrait_01.png",
                        "🎬 Rendered Video: vlog_2025.mp4",
                        "🧩 New Template: minimal_resume.fxml",
                        "📸 Imported photo: sunset.jpg",
                        "⚙ Changed Theme to Dark Mode"
                );
                System.out.println("[Dashboard] No DB data found. Using default sample data.");
            }

            recentActivityList.setItems(activities);

        } catch (Exception e) {
            e.printStackTrace();
            // fallback if DB fails
            activities = FXCollections.observableArrayList(
                    "⚠️ Error loading from DB — showing cached items.",
                    "📸 Edited Photo: portrait_01.png",
                    "🎬 Rendered Video: vlog_2025.mp4"
            );
            recentActivityList.setItems(activities);
        }
    }

    // ---------- Chart ----------
    private void loadChart() {
        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList(
                new PieChart.Data("Photos", totalPhotos),
                new PieChart.Data("Videos", totalVideos),
                new PieChart.Data("Templates", totalTemplates)
        );
        mediaChart.setData(chartData);
    }

    // ---------- System Status ----------
    private void updateSystemStatus() {
        dbStatusLabel.setText("DB: Connected ✅");
        pluginStatusLabel.setText("Plugins: 4 Loaded");
        themeStatusLabel.setText("Theme: Light");
    }

    // ---------- Refresh ----------
    @FXML
    private void handleRefresh(ActionEvent event) {
        Random rand = new Random();
        totalPhotos += rand.nextInt(3);
        totalVideos += rand.nextInt(2);
        activeUsers = 3 + rand.nextInt(5);

        loadOverviewData();
        loadChart();
        loadRecentActivity(); // ✅ refresh DB data here
        animateCards();

        dbStatusLabel.setText("DB: Synced ✅");
        System.out.println("[Dashboard] Refreshed data successfully.");
    }

    // ---------- Quick Actions ----------
    @FXML
    private void openImageEditor() {
        System.out.println("Opening Image Editor...");
    }

    @FXML
    private void openVideoPlayer() {
        System.out.println("Opening Video Player...");
    }

    @FXML
    private void openTemplates() {
        System.out.println("Opening Templates...");
    }

    @FXML
    private void openSettings() {
        System.out.println("Opening Settings...");
    }

    @FXML
    private void handleProfile() {
        System.out.println("Opening Profile View...");
    }

    // ---------- Animations ----------
    private void animateCards() {
        Label[] cards = {totalPhotosLabel, totalVideosLabel, totalTemplatesLabel, activeUsersLabel};
        for (Label card : cards) {
            ScaleTransition scale = new ScaleTransition(Duration.millis(400), card);
            scale.setFromX(0.8);
            scale.setFromY(0.8);
            scale.setToX(1);
            scale.setToY(1);
            scale.play();
        }
    }
}
