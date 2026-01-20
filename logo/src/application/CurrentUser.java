package application;

public class CurrentUser {
    private static int id;
    private static String username;
    private static String name;
    private static String email;
    private static String profilePicUrl;

    // --- SETTERS ---
    public static void setId(int value) { id = value; }
    public static void setUsername(String value) { username = value; }
    public static void setName(String value) { name = value; }
    public static void setEmail(String value) { email = value; }
    public static void setProfilePicUrl(String value) { profilePicUrl = value; }

    // --- GETTERS ---
    public static int getId() { return id; }
    public static String getUsername() { return username; }
    public static String getName() { return name; }
    public static String getEmail() { return email; }
    public static String getProfilePicUrl() { return profilePicUrl; }

    // (Optional) method to clear all info, e.g. on logout
    public static void clear() {
        id = 0;
        username = null;
        name = null;
        email = null;
        profilePicUrl = null;
    }
}
