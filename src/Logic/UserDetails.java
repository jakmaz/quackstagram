package Logic;

public class UserDetails {
    private int id;
    private String username;
    private String bio;

    public UserDetails(int id, String username, String bio) {
        this.id = id;
        this.username = username;
        this.bio = bio;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }
}