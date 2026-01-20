package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.util.prefs.Preferences;

/**
 * Controller for the "General" tab of VisionForge Settings.
 * Handles Theme, Font, Accent, Startup, Session, and Guest Mode.
 * Fully persistent using java.util.prefs.Preferences.
 */
public class SettingsGeneralController {

    // ---------- FXML Elements ----------
    @FXML private CheckBox themeToggle;
    @FXML private Slider fontSizeSlider;
    @FXML private Label fontSizeValueLabel;
    @FXML private ColorPicker accentColorPicker;
    @FXML private ChoiceBox<String> startupScreenChoice;
    @FXML private CheckBox restoreSessionToggle;
    @FXML private Spinner<Integer> recentProjectsSpinner;
    @FXML private Button guestModeBtn;
    @FXML private Label statusLabel;

    // ---------- Internal State ----------
    private boolean guestMode = false;
    private double lastFontSize = 16;
    private Color lastAccent = Color.web("#38bdf8");

    private final Preferences prefs = Preferences.userRoot().node("visionforge/settings/general");

    // ---------- Initialization ----------
    @FXML
    public void initialize() {
        try {
            // Load stored preferences
            boolean darkMode = prefs.getBoolean("darkMode", false);
            double fontSize = prefs.getDouble("fontSize", 16);
            String accentHex = prefs.get("accentColor", "#38bdf8");

            if (themeToggle != null)
                themeToggle.setSelected(darkMode);

            if (fontSizeSlider != null) {
                fontSizeSlider.setValue(fontSize);
                if (fontSizeValueLabel != null)
                    fontSizeValueLabel.setText((int) fontSize + "px");
                fontSizeSlider.valueProperty().addListener((_, _, newVal) -> {
                    fontSizeValueLabel.setText(newVal.intValue() + "px");
                    if (!guestMode) applyFontSize(newVal.intValue());
                });
            }

            if (accentColorPicker != null)
                accentColorPicker.setValue(Color.web(accentHex));

            if (startupScreenChoice != null) {
                startupScreenChoice.setItems(FXCollections.observableArrayList(
                        "Dashboard", "Photo Editor", "Video Player", "Profile", "Templates", "Settings"
                ));
                startupScreenChoice.setValue(prefs.get("startupScreen", "Dashboard"));
            }

            if (restoreSessionToggle != null)
                restoreSessionToggle.setSelected(prefs.getBoolean("restoreSession", false));

            if (recentProjectsSpinner != null) {
                SpinnerValueFactory<Integer> valueFactory =
                        new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 20,
                                prefs.getInt("recentProjects", 5));
                recentProjectsSpinner.setValueFactory(valueFactory);
            }

            if (statusLabel != null)
                statusLabel.setText("General settings loaded.");

            System.out.println("[General Settings] Initialized successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            if (statusLabel != null)
                statusLabel.setText("Initialization error: " + e.getMessage());
        }
    }

    // ---------- THEME ----------
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
                prefs.putBoolean("darkMode", true);
                System.out.println("Dark Mode Enabled");
            } else {
                scene.getStylesheets().remove(darkCss);
                if (!scene.getStylesheets().contains(lightCss))
                    scene.getStylesheets().add(lightCss);
                prefs.putBoolean("darkMode", false);
                System.out.println("Light Mode Enabled");
            }

            if (statusLabel != null && !guestMode)
                statusLabel.setText(themeToggle.isSelected() ? "Dark Mode On" : "Light Mode On");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- FONT SIZE ----------
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
            prefs.putDouble("fontSize", fontSize);
            lastFontSize = fontSize;
            System.out.println("Font size set to: " + fontSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- ACCENT COLOR ----------
    @FXML
    private void handleAccentColor(ActionEvent event) {
        try {
            if (accentColorPicker == null) return;
            Color color = accentColorPicker.getValue();
            String hex = toHexString(color);
            Scene scene = ((Node) event.getSource()).getScene();
            if (scene != null)
                scene.getRoot().setStyle("-fx-accent: " + hex + ";");
            prefs.put("accentColor", hex);
            lastAccent = color;

            if (statusLabel != null && !guestMode)
                statusLabel.setText("Accent color set to " + hex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String toHexString(Color color) {
        if (color == null) return "#FFFFFF";
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    // ---------- GUEST MODE ----------
    @FXML
    private void handleGuestMode(ActionEvent event) {
        guestMode = true;
        if (statusLabel != null)
            statusLabel.setText("Guest Mode enabled (no changes saved).");
        System.out.println("[General Settings] Guest Mode enabled.");
    }

    // ---------- SAVE ----------
    @FXML
    private void handleSave(ActionEvent event) {
        if (guestMode) {
            if (statusLabel != null)
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
            prefs.flush();

            if (statusLabel != null)
                statusLabel.setText("Settings saved successfully.");
            System.out.println("[General Settings] Preferences saved.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- BACK ----------
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
