import java.util.List;
import java.util.ArrayList;

// Represents a picture on Quackstagram
class Picture {
    private String imagePath;
    private String caption;
    private int likesCount;
    private List<String> comments;

    public Picture(String imagePath, String caption) {
        this.imagePath = imagePath;
        this.caption = caption;
        this.likesCount = 0;
        this.comments = new ArrayList<>();
    }

    // Add a comment to the picture
    public void addComment(String comment) {
        comments.add(comment);
    }

    // Increment likes count
    public void like() {
        likesCount++;
    }

    // Getter methods for picture details
    public String getImagePath() { return imagePath; }
    public String getCaption() { return caption; }
    public int getLikesCount() { return likesCount; }
    public List<String> getComments() { return comments; }
}
