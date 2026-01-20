package db.Model;

public class RecentFile {
    private int id;
    private int userId;
    private String filePath;
    private long lastOpened;  // Epoch millis

    public RecentFile(int id, int userId, String filePath, long lastOpened) {
        this.id = id;
        this.userId = userId;
        this.filePath = filePath;
        this.lastOpened = lastOpened;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getFilePath() { return filePath; }
    public long getLastOpened() { return lastOpened; }

    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setLastOpened(long lastOpened) { this.lastOpened = lastOpened; }
}
