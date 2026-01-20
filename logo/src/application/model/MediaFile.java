package application.model;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a generic media file (photo, video, or template).
 * Used by FileDAO, Dashboard, and Editor modules.
 */
public class MediaFile {

    private int id;
    private String filePath;       // Full path of the media file
    private String type;           // "image", "video", or "template"
    private long sizeBytes;
    private LocalDateTime createdAt;

    // ---------- Constructor ----------
    public MediaFile(int id, String filePath, String type, long sizeBytes, LocalDateTime createdAt) {
        this.id = id;
        this.filePath = filePath;
        this.type = type;
        this.sizeBytes = sizeBytes;
        this.createdAt = createdAt;
    }

    // ---------- Getters and Setters ----------
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public long getSizeBytes() { return sizeBytes; }
    public void setSizeBytes(long sizeBytes) { this.sizeBytes = sizeBytes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // ---------- Derived / Utility Methods ----------

    /**
     * Extracts and returns just the file name (without directory path).
     */
    public String getFileName() {
        if (filePath == null || filePath.isEmpty()) return "Unknown";
        return new File(filePath).getName();
    }

    /**
     * Returns the file type icon as a Unicode emoji or short label.
     * Helps in visualizing recent activity logs.
     */
    public String getTypeIcon() {
        if (type == null) return "📁";
        return switch (type.toLowerCase()) {
            case "image", "photo", "picture" -> "📸";
            case "video" -> "🎬";
            case "template" -> "🧩";
            default -> "📁";
        };
    }

    /**
     * Returns file size in KB or MB (human-readable).
     */
    public String getReadableSize() {
        double kb = sizeBytes / 1024.0;
        double mb = kb / 1024.0;
        return mb >= 1 ? String.format("%.2f MB", mb) : String.format("%.1f KB", kb);
    }

    /**
     * Returns creation timestamp in readable format.
     */
    public String getFormattedDate() {
        if (createdAt == null) return "Unknown Date";
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    // ---------- Object Overrides ----------
    @Override
    public String toString() {
        return String.format("[%s] %s (%s, %s)",
                type != null ? type.toUpperCase() : "UNKNOWN",
                getFileName(),
                getReadableSize(),
                getFormattedDate());
    }
}
