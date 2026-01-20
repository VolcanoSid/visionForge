package application.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

/**
 * Utility class for handling video-related operations.
 * Used by VideoController and other modules that manage media playback.
 */
public class VideoUtils {

    /**
     * Creates a MediaPlayer from a video file.
     * @param videoFile File object pointing to the video
     * @return MediaPlayer instance or null if the file is invalid
     */
    public static MediaPlayer createMediaPlayer(File videoFile) {
        if (videoFile == null || !videoFile.exists()) {
            System.err.println("[VideoUtils] Invalid video file.");
            return null;
        }

        try {
            Media media = new Media(videoFile.toURI().toString());
            return new MediaPlayer(media);
        } catch (Exception e) {
            System.err.println("[VideoUtils] Error creating MediaPlayer: " + e.getMessage());
            return null;
        }
    }

    /**
     * Converts video duration in seconds to HH:MM:SS format.
     * @param totalSeconds duration in seconds
     * @return formatted time string
     */
    public static String formatDuration(double totalSeconds) {
        int hours = (int) totalSeconds / 3600;
        int minutes = ((int) totalSeconds % 3600) / 60;
        int seconds = (int) totalSeconds % 60;
        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    /**
     * Prints basic metadata about a video (for debugging/logging).
     * @param player MediaPlayer instance
     */
    public static void logVideoInfo(MediaPlayer player) {
        if (player == null || player.getMedia() == null) {
            System.err.println("[VideoUtils] No media loaded.");
            return;
        }

        System.out.println("=== Video Metadata ===");
        System.out.println("Source: " + player.getMedia().getSource());
        System.out.println("Duration: " + formatDuration(player.getTotalDuration().toSeconds()));
    }
}
