package db;

import db.Model.User;
import java.sql.*;

public class UserDAO {

    // Register new user (returns true on success, false on duplicate username/email)
    public static boolean addUser(User user) {
        String sql = "INSERT INTO users (username, name, password, email, profilePicUrl) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = H2Connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getProfilePicUrl());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            // 23505 is H2's code for unique constraint violation (duplicate username/email)
            if ("23505".equals(e.getSQLState())) {
                System.err.println("Duplicate entry: Username or email already exists.");
            } else {
                System.err.println("Error adding user: " + e.getMessage());
            }
            return false;
        }
    }

    // Get user by username (returns full user object)
    public static User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = H2Connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("profilePicUrl")
                );
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Error fetching user by username: " + username + " | " + e.getMessage());
        }
        return null;
    }

    // Validate login and return the User if credentials match, else null
    public static User authenticate(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // Get user by email
    public static User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = H2Connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("profilePicUrl")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by email: " + e.getMessage());
        }
        return null;
    }

    public static boolean updateUserProfile(int userId, String newName, String newProfilePicUrl) {
        String sql = "UPDATE users SET name = ?, profilePicUrl = ? WHERE id = ?";
        try (Connection conn = H2Connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newName);
            ps.setString(2, newProfilePicUrl);
            ps.setInt(3, userId);
            int updated = ps.executeUpdate();
            return updated == 1;
        } catch (SQLException e) {
            System.err.println("Error updating user profile: " + e.getMessage());
            return false;
        }
    }
 // --- Dashboard Stats Method --- //
    public static int getActiveUsers() {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection conn = H2Connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error counting users: " + e.getMessage());
        }
        return 0;
    }

}
