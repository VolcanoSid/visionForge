package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {
    public static void initialize() {
        try (Connection conn = H2Connector.getConnection();
             Statement stmt = conn.createStatement()) {

            // Updated User Table with all profile fields
            String userTable = "CREATE TABLE IF NOT EXISTS users ("
                    + "id IDENTITY PRIMARY KEY, "
                    + "username VARCHAR(255) UNIQUE NOT NULL, "
                    + "name VARCHAR(255), "
                    + "password VARCHAR(255) NOT NULL, "
                    + "email VARCHAR(255) UNIQUE NOT NULL, "
                    + "profilePicUrl VARCHAR(512)"
                    + ")";

            // Recent Files Table
            String fileTable = "CREATE TABLE IF NOT EXISTS recent_files ("
                    + "id IDENTITY PRIMARY KEY, "
                    + "user_id INT, "
                    + "file_path VARCHAR(500) NOT NULL, "
                    + "last_opened BIGINT, "
                    + "FOREIGN KEY (user_id) REFERENCES users(id)"
                    + ")";

            stmt.execute(userTable);
            stmt.execute(fileTable);

            System.out.println("Tables created or already exist.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
