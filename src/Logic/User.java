package Logic;

import Database.DAO.FollowDAO;
import Database.DAO.PostDAO;
import Database.DAO.UserDAO;

import java.util.List;
import java.util.Objects;

public class User {
  private final Integer id;
  private String username;
  private String bio;
  private Integer followersCount;
  private Integer followingCount;
  private List<Post> posts;

  public User(int id) {
    this.id = id;
  }

  public String getUsername() {
    if (username == null) {
      loadUserDetails();
    }
    return username;
  }

  public String getBio() {
    if (bio == null) {
      loadUserDetails();
    }
    return bio;
  }

  public int getPostsCount() {
    if (posts == null) {
      loadPosts();
    }
    return posts.size();
  }

  public int getFollowersCount() {
    if (followersCount == null) {
      loadFollowingDetails();
    }
    return followersCount != null ? followersCount : 0;
  }

  public int getFollowingCount() {
    if (followingCount == null) {
      loadFollowingDetails();
    }
    return followingCount != null ? followingCount : 0;
  }

  public List<Post> getPosts() {
    if (posts == null) {
      loadPosts();
    }
    return posts;
  }

  public String getProfilePicturePath() {
    return UserDAO.getProfilePicturePath(this.id);
  }

  private void loadUserDetails() {
    UserDetails userDetails = UserDAO.getUserDetails(this.id);
    if (userDetails != null) {
      this.username = userDetails.getUsername();
      this.bio = userDetails.getBio();
    }
  }

  private void loadFollowingDetails() {
    this.followersCount = FollowDAO.getFollowersAmount(id);
    this.followingCount = FollowDAO.getFollowingAmount(id);
  }

  private void loadPosts() {
    this.posts = PostDAO.getPostsByUserId(id);
  }

  public Integer getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
