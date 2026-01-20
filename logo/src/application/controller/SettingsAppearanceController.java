package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;

import java.time.LocalTime;
import java.util.prefs.Preferences;

/**
 * Handles "Appearance" tab settings for VisionForge.
 * Manages auto theme, font family, UI density, accent presets, and live preview.
 */
public class SettingsAppearanceController {

    @FXML private CheckBox autoThemeToggle;
    @FXML private Label autoThemeStatus;
    @FXML private ChoiceBox<String> fontFamilyChoice;
    @FXML private RadioButton compactMode;
    @FXML private RadioButton comfortableMode;
    @FXML private ColorPicker accentPresetPicker;
    @FXML private ToggleButton livePreviewToggle;
    @FXML private Label statusLabel;

    private final Preferences prefs = Preferences.userRoot().node("visionforge/settings/appearance");
    private final ToggleGroup densityGroup = new ToggleGroup();

    @FXML
    public void initialize() {
        try {
            // Initialize Density Group
            compactMode.setToggleGroup(densityGroup);
            comfortableMode.setToggleGroup(densityGroup);

            // Load stored prefs
            autoThemeToggle.setSelected(prefs.getBoolean("autoTheme", false));
            fontFamilyChoice.setItems(FXCollections.observableArrayList("Inter", "Roboto", "Segoe UI", "Arial", "System Default"));
            fontFamilyChoice.setValue(prefs.get("fontFamily", "System Default"));

            String density = prefs.get("uiDensity", "Comfortable");
            if (density.equals("Compact")) compactMode.setSelected(true);
            else comfortableMode.setSelected(true);

            String accentHex = prefs.get("accentPreset", "#38bdf8");
            accentPresetPicker.setValue(Color.web(accentHex));

            if (statusLabel != null) statusLabel.setText("Appearance loaded.");

            // If auto theme enabled, run immediately
            if (autoThemeToggle.isSelected()) applyAutoTheme();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- AUTO THEME ----------
    @FXML
    private void handleAutoThemeToggle(ActionEvent event) {
        boolean enabled = autoThemeToggle.isSelected();
        prefs.putBoolean("autoTheme", enabled);

        if (enabled) {
            applyAutoTheme();
            if (autoThemeStatus != null)
                autoThemeStatus.setText("Auto Theme: Enabled");
        } else {
            if (autoThemeStatus != null)
                autoThemeStatus.setText("Auto Theme: Disabled");
        }
    }

    private void applyAutoTheme() {
        LocalTime now = LocalTime.now();
        boolean night = now.isAfter(LocalTime.of(19, 0)) || now.isBefore(LocalTime.of(6, 0));

        try {
            Scene scene = autoThemeToggle.getScene();
            if (scene == null) return;

            String lightCss = getClass().getResource("/application/application.css").toExternalForm();
            String darkCss = getClass().getResource("/application/dark.css").toExternalForm();

            if (night) {
                scene.getStylesheets().remove(lightCss);
                if (!scene.getStylesheets().contains(darkCss))
                    scene.getStylesheets().add(darkCss);
                prefs.putBoolean("darkMode", true);
            } else {
                scene.getStylesheets().remove(darkCss);
                if (!scene.getStylesheets().contains(lightCss))
                    scene.getStylesheets().add(lightCss);
                prefs.putBoolean("darkMode", false);
            }

            if (statusLabel != null)
                statusLabel.setText("Theme auto-applied for current time.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- FONT FAMILY ----------
    @FXML
    private void handleApplyFont(ActionEvent event) {
        String selectedFont = fontFamilyChoice.getValue();
        prefs.put("fontFamily", selectedFont);

        Scene scene = ((Node) event.getSource()).getScene();
        if (scene != null)
            scene.getRoot().setStyle("-fx-font-family: '" + selectedFont + "';");

        if (statusLabel != null)
            statusLabel.setText("Font applied: " + selectedFont);
    }

    // ---------- UI DENSITY ----------
    @FXML
    private void handleSave(ActionEvent event) {
        try {
            String density = compactMode.isSelected() ? "Compact" : "Comfortable";
            prefs.put("uiDensity", density);
            prefs.put("accentPreset", toHex(accentPresetPicker.getValue()));
            prefs.putBoolean("autoTheme", autoThemeToggle.isSelected());
            prefs.putBoolean("livePreview", livePreviewToggle.isSelected());
            prefs.flush();

            if (statusLabel != null)
                statusLabel.setText("Appearance settings saved.");
            System.out.println("[Appearance] Saved preferences.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- ACCENT PRESETS ----------
    @FXML
    private void handleAccentPresetChange(ActionEvent event) {
        String hex = toHex(accentPresetPicker.getValue());
        Scene scene = ((Node) event.getSource()).getScene();
        if (scene != null)
            scene.getRoot().setStyle("-fx-accent: " + hex + ";");
        prefs.put("accentPreset", hex);
        if (statusLabel != null)
            statusLabel.setText("Accent changed to " + hex);
    }

    @FXML
    private void handleResetAccent(ActionEvent event) {
        accentPresetPicker.setValue(Color.web("#38bdf8"));
        handleAccentPresetChange(event);
        if (statusLabel != null)
            statusLabel.setText("Accent reset to default.");
    }

    // ---------- LIVE PREVIEW ----------
    @FXML
    private void handleLivePreviewToggle(ActionEvent event) {
        boolean live = livePreviewToggle.isSelected();
        prefs.putBoolean("livePreview", live);
        if (statusLabel != null)
            statusLabel.setText(live ? "Live preview on." : "Live preview off.");
    }

    // ---------- UTIL ----------
    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
