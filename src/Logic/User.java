package Logic;

import Database.DatabaseUtils;

import java.util.List;

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

  private void loadUserDetails() {
    UserDetails userDetails = DatabaseUtils.getUserDetails(this.id);
    if (userDetails != null) {
      this.username = userDetails.getUsername();
      this.bio = userDetails.getBio();
    }
  }

  private void loadFollowingDetails() {
    this.followersCount = DatabaseUtils.getFollowersAmount(id);
    this.followingCount = DatabaseUtils.getFollowingAmount(id);
  }

  private void loadPosts() {
    this.posts = DatabaseUtils.getPosts(id);
  }
}
