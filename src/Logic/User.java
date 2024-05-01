package Logic;

import Database.DatabaseUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class User {
  private final Integer id;  // Changed from int to Integer
  private String username;
  private String bio;

  private int postsCount;
  private int followersCount;
  private int followingCount;
  private List<Picture> pictures = new ArrayList<>();

  public User(int id) {
    this.id = id;
    readUserDetails();
    readFollowingDetails();
    readPostsDetails();
  }

  private void readUserDetails() {
    UserDetails userDetails = DatabaseUtils.getUserDetails(this.id);
    if (userDetails != null) {
      this.username = userDetails.getUsername();
      this.bio = userDetails.getBio();
    }
  }

  private void readFollowingDetails() {
    int followersCount = 0;
    int followingCount = 0;
//    DatabaseUtils.getFollowingDetails(this.id);
    this.followersCount = followersCount;
    this.followingCount = followingCount;
  }

  private void readPostsDetails() {
//    int imageCount = 0;
//    Path imageDetailsFilePath = Paths.get("img", "image_details.txt");
//
//    try (BufferedReader imageDetailsReader = Files.newBufferedReader(imageDetailsFilePath)) {
//      String line;
//      while ((line = imageDetailsReader.readLine()) != null) {
//        if (line.contains("Username: " + this.username)) {
//          imageCount++;
//        }
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    this.postsCount = imageCount;
    this.postsCount = 0;
  }

  // Getter methods for user details
  public String getUsername() {
    return username;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public int getPostsCount() {
    return postsCount;
  }

  public int getFollowersCount() {
    return followersCount;
  }

  public int getFollowingCount() {
    return followingCount;
  }

  public List<Picture> getPictures() {
    return pictures;
  }

  // Setter methods for followers and following counts
  public void setFollowersCount(int followersCount) {
    this.followersCount = followersCount;
  }

  public void setFollowingCount(int followingCount) {
    this.followingCount = followingCount;
  }

  public void setPostCount(int postCount) {
    this.postsCount = postCount;
  }

  // Implement the toString method for saving user information
  @Override
  public String toString() {
    return username + ":" + bio + ":"; // Format as needed
  }

}
