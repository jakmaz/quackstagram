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

    public Post(Integer id, Integer userId, String imagePath, String caption, Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.imagePath = imagePath;
        this.caption = caption;
        this.timestamp = timestamp;
    }
}
