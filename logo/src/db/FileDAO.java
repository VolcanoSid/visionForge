package db;

import db.Model.RecentFile;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import application.model.MediaFile;
import java.time.LocalDateTime;


public class FileDAO {

    public static void addRecentFile(int userId, String filePath) {
        String sql = "INSERT INTO recent_files (user_id, file_path, last_opened) VALUES (?, ?, ?)";
        try (Connection conn = H2Connector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, filePath);
            pstmt.setLong(3, System.currentTimeMillis());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<RecentFile> getRecentFiles(int userId) {
        List<RecentFile> list = new ArrayList<>();
        String sql = "SELECT * FROM recent_files WHERE user_id=? ORDER BY last_opened DESC LIMIT 10";
        try (Connection conn = H2Connector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new RecentFile(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("file_path"),
                        rs.getLong("last_opened")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // NEW: Get only the file/project names as a List<String>
    public static List<String> getRecentProjectNamesByUser(int userId) {
        List<String> names = new ArrayList<>();
        String sql = "SELECT file_path FROM recent_files WHERE user_id=? ORDER BY last_opened DESC LIMIT 10";
        try (Connection conn = H2Connector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // Just the file path (you can extract a project name from the path if needed)
                names.add(rs.getString("file_path"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }
 // --- Dashboard Stats Methods --- //
    public static int getTotalImages() {
        String sql = "SELECT COUNT(*) FROM recent_files WHERE file_path LIKE '%.jpg' OR file_path LIKE '%.png'";
        try (Connection conn = H2Connector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getTotalVideos() {
        String sql = "SELECT COUNT(*) FROM recent_files WHERE file_path LIKE '%.mp4' OR file_path LIKE '%.mkv' OR file_path LIKE '%.avi'";
        try (Connection conn = H2Connector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getTotalTemplates() {
        String sql = "SELECT COUNT(*) FROM recent_files WHERE file_path LIKE '%.template%' OR file_path LIKE '%.tpl'";
        try (Connection conn = H2Connector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
 // --- Retrieve all recent media files as MediaFile objects --- //
    public static List<MediaFile> getAllMediaFiles(int userId) {
        List<MediaFile> mediaFiles = new ArrayList<>();
        String sql = "SELECT id, file_path, last_opened FROM recent_files WHERE user_id=? ORDER BY last_opened DESC";

        try (Connection conn = H2Connector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String path = rs.getString("file_path");
                @SuppressWarnings("unused")
				long time = rs.getLong("last_opened");
                LocalDateTime created = LocalDateTime.now(); // placeholder; can be replaced if DB stores creation time

                // Detect type by extension
                String type = "unknown";
                if (path.endsWith(".jpg") || path.endsWith(".png")) type = "image";
                else if (path.endsWith(".mp4") || path.endsWith(".mkv")) type = "video";
                else if (path.endsWith(".tpl") || path.endsWith(".template")) type = "template";

                // File size (approximation — can fetch from java.io.File if needed)
                long sizeBytes = new java.io.File(path).length();

                mediaFiles.add(new MediaFile(id, path, type, sizeBytes, created));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mediaFiles;
    }

}
