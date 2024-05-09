package Logic;

import java.sql.Timestamp;

public class Comment {
    private int id;
    private User user;
    private String text;
    private Timestamp timestamp;

    // Constructor for initializing a Comment object
    public Comment(int id, User user, String text, Timestamp timestamp) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.timestamp = timestamp;
    }

    // Getters for accessing the fields
    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    // Setters if modification is required
    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
