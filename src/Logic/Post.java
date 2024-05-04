package Logic;

import java.sql.Timestamp;
import java.util.List;

// Represents a picture on Quackstagram
public class Post {
    private Integer id;
    private Integer userId;
    private String imagePath;
    private String caption;
    private Timestamp timestamp;
    private int likesCount;
    private List<Comment> comments;

    public Post(Integer id, Integer userId, String caption, String imagePath, Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.imagePath = imagePath;
        this.caption = caption;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getCaption() {
        return caption;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public List<Comment> getComments() {
        return comments;
    }
}
