package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2Connector {
    private static final String DB_URL = "jdbc:h2:file:C:/Users/Assus/eclipse-workspace/VisionForge/data/visionforge_db";
    private static final String DB_USER = "sa";
    private static final String DB_PASS = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
}
