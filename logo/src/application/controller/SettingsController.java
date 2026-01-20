package application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;

import java.util.prefs.Preferences;

/**
 * Handles application settings for VisionForge.
 * Acts as the main container for all sub-setting categories (General, Appearance, Storage, Security, Advanced, UX)
 * Also preserves classic settings (theme, font, color, startup, etc.).
 */
public class SettingsController {

    // ---------- Original UI Components ----------
    @FXML private CheckBox themeToggle;
    @FXML private Slider fontSizeSlider;
    @FXML private Label fontSizeValueLabel;
    @FXML private ColorPicker accentColorPicker;
    @FXML private ChoiceBox<String> startupScreenChoice;
    @FXML private CheckBox restoreSessionToggle;
    @FXML private Spinner<Integer> recentProjectsSpinner;
    @FXML private Button guestModeBtn;
    @FXML private Label statusLabel;

    // ---------- New Hub Components ----------
    @FXML private TabPane settingsTabPane;     // Tab container for sub-pages
    @FXML private Button resetDefaultsBtn;     // Reset all preferences
    @FXML private Button backBtn;              // Back to Dashboard

    // ---------- Internal State ----------
    private boolean guestMode = false;
    private double lastFontSize = 16;
    private Color lastAccent = Color.web("#38bdf8");

    private final Preferences prefs = Preferences.userRoot().node("visionforge/settings");

    // ------------------ INITIALIZATION ------------------
    @FXML
    public void initialize() {
        try {
            // ---------- Load Category Tabs ----------
            if (settingsTabPane != null) {
                loadTab("/fxml/settings_general.fxml", "General");
                loadTab("/fxml/settings_appearance.fxml", "Appearance");
                loadTab("/fxml/settings_storage.fxml", "Storage");
                loadTab("/fxml/settings_security.fxml", "Security");
                loadTab("/fxml/settings_advanced.fxml", "Advanced");
                loadTab("/fxml/settings_ux.fxml", "UX");
            }

            // ---------- Original Component Setup ----------
            if (fontSizeSlider != null && fontSizeValueLabel != null) {
                fontSizeValueLabel.setText((int) fontSizeSlider.getValue() + "px");
                fontSizeSlider.valueProperty().addListener((_, _, newVal) -> {
                    fontSizeValueLabel.setText(newVal.intValue() + "px");
                    if (!guestMode) applyFontSize(newVal.intValue());
                });
            }

            if (accentColorPicker != null)
                accentColorPicker.setValue(lastAccent);

            if (startupScreenChoice != null) {
                startupScreenChoice.setItems(FXCollections.observableArrayList(
                        "Dashboard", "Photo Editor", "Video Player", "Profile", "Templates", "Settings"
                ));
                startupScreenChoice.setValue("Dashboard");
            }

            if (recentProjectsSpinner != null) {
                SpinnerValueFactory<Integer> valueFactory =
                        new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 20, 5);
                recentProjectsSpinner.setValueFactory(valueFactory);
            }

            if (restoreSessionToggle != null)
                restoreSessionToggle.setSelected(false);
            if (themeToggle != null)
                themeToggle.setSelected(false);

            if (statusLabel != null)
                statusLabel.setText("Settings initialized successfully.");

            System.out.println("SettingsController initialized successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            if (statusLabel != null)
                statusLabel.setText("Error during initialization: " + e.getMessage());
        }
    }

    // ------------------ LOAD TAB METHOD ------------------
    private void loadTab(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent content = loader.load();
            Tab tab = new Tab(title);
            tab.setContent(content);
            settingsTabPane.getTabs().add(tab);
        } catch (Exception e) {
            System.err.println("[SettingsController] Failed to load tab: " + title + " -> " + e.getMessage());
        }
    }

    // ------------------ THEME ------------------
    @FXML
    private void handleThemeSwitch(ActionEvent event) {
        try {
            Scene scene = ((Node) event.getSource()).getScene();
            if (scene == null || themeToggle == null) return;

            String lightCss = getClass().getResource("/application/application.css").toExternalForm();
            String darkCss = getClass().getResource("/application/dark.css").toExternalForm();

            if (themeToggle.isSelected()) {
                scene.getStylesheets().remove(lightCss);
                if (!scene.getStylesheets().contains(darkCss))
                    scene.getStylesheets().add(darkCss);
                System.out.println("Dark Mode Enabled");
            } else {
                scene.getStylesheets().remove(darkCss);
                if (!scene.getStylesheets().contains(lightCss))
                    scene.getStylesheets().add(lightCss);
                System.out.println("Light Mode Enabled");
            }

            if (statusLabel != null && !guestMode)
                statusLabel.setText(themeToggle.isSelected() ? "Dark Mode On" : "Light Mode On");

            prefs.putBoolean("darkMode", themeToggle.isSelected());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ------------------ FONT SIZE ------------------
    @FXML
    private void handleFontSize() {
        if (fontSizeSlider == null || fontSizeValueLabel == null) return;

        int fontSize = (int) fontSizeSlider.getValue();
        fontSizeValueLabel.setText(fontSize + "px");
        if (!guestMode) applyFontSize(fontSize);
    }

    private void applyFontSize(int fontSize) {
        try {
            if (fontSizeSlider == null) return;
            Scene scene = fontSizeSlider.getScene();
            if (scene != null)
                scene.getRoot().setStyle("-fx-font-size: " + fontSize + "px;");
            System.out.println("Font size set to: " + fontSize);
            lastFontSize = fontSize;
            prefs.putDouble("fontSize", fontSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ------------------ ACCENT COLOR ------------------
    @FXML
    private void handleAccentColor(ActionEvent event) {
        try {
            if (accentColorPicker == null) return;
            Color color = accentColorPicker.getValue();
            String hex = toHexString(color);

            Scene scene = ((Node) event.getSource()).getScene();
            if (scene != null)
                scene.getRoot().setStyle("-fx-accent: " + hex + ";");

            System.out.println("Accent color: " + hex);
            lastAccent = color;
            prefs.put("accentColor", hex);

            if (statusLabel != null && !guestMode)
                statusLabel.setText("Accent set!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ------------------ UTIL ------------------
    private String toHexString(Color color) {
        if (color == null) return "#FFFFFF";
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    // ------------------ GUEST MODE ------------------
    @FXML
    private void handleGuestMode(ActionEvent event) {
        guestMode = true;
        if (statusLabel != null)
            statusLabel.setText("Guest Mode enabled (no changes saved).");
        System.out.println("Guest Mode enabled (no changes saved).");
    }

    // ------------------ RESET DEFAULTS ------------------
    @FXML
    private void handleResetDefaults(ActionEvent event) {
        try {
            prefs.clear();
            themeToggle.setSelected(false);
            fontSizeSlider.setValue(16);
            accentColorPicker.setValue(Color.web("#38bdf8"));
            startupScreenChoice.setValue("Dashboard");
            restoreSessionToggle.setSelected(false);

            if (statusLabel != null)
                statusLabel.setText("Settings reset to defaults.");
            System.out.println("Settings reset to defaults.");

        } catch (Exception e) {
            e.printStackTrace();
            if (statusLabel != null)
                statusLabel.setText("Error resetting defaults: " + e.getMessage());
        }
    }

    // ------------------ SAVE ------------------
    @FXML
    private void handleSave(ActionEvent event) {
        if (statusLabel == null) return;

        if (guestMode) {
            statusLabel.setText("Guest Mode: changes not saved.");
            return;
        }

        try {
            prefs.putBoolean("darkMode", themeToggle.isSelected());
            prefs.putDouble("fontSize", fontSizeSlider.getValue());
            prefs.put("accentColor", toHexString(accentColorPicker.getValue()));
            prefs.put("startupScreen", startupScreenChoice.getValue());
            prefs.putBoolean("restoreSession", restoreSessionToggle.isSelected());
            prefs.putInt("recentProjects", recentProjectsSpinner.getValue());

            prefs.flush(); // instantly save

            statusLabel.setText("Settings saved successfully.");
            System.out.println("Settings saved successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ------------------ BACK ------------------
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/fxml/dashboard_view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = stage.getScene();

            if (scene != null)
                scene.setRoot(view);
            else
                stage.setScene(new Scene(view));

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
