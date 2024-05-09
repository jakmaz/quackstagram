package Logic;

import Database.CommentsDAO;
import Database.LikesDAO;

import java.sql.Timestamp;
import java.util.List;

public class Post {
  private Integer id;
  private User user;
  private String imagePath;
  private String caption;
  private Timestamp timestamp;
  private Integer likesCount;
  private List<Comment> comments;

  public Post(Integer id, User user, String caption, String imagePath, Timestamp timestamp) {
    this.id = id;
    this.user = user;
    this.imagePath = imagePath;
    this.caption = caption;
    this.timestamp = timestamp;
  }

  public Integer getId() {
    return id;
  }

  public User getUser() {
    return user;
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
    if (likesCount == null) {
      likesCount = LikesDAO.getLikesCountForPost(id);
    }
    return likesCount;
  }

  public List<Comment> getComments() {
    if (comments == null) {
      comments = CommentsDAO.getCommentsForPost(id);
    }
    return comments;
  }
}
