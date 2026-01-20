package db.Model;

public class User {
    private int id;
    private String username;
    private String name;
    private String email;
    private String password;      // Note: in production, never store raw passwords!
    private String profilePicUrl; // Path or URL to profile image

    // --- Constructors ---

    public User() {}

    public User(int id, String username, String name, String email, String password, String profilePicUrl) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.profilePicUrl = profilePicUrl;
    }

    // --- Getters & Setters ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProfilePicUrl() { return profilePicUrl; }
    public void setProfilePicUrl(String profilePicUrl) { this.profilePicUrl = profilePicUrl; }

    // --- toString (optional, for debugging) ---

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", profilePicUrl='" + profilePicUrl + '\'' +
                '}';
    }
}
