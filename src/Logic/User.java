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
  private final String username;
  private String bio;
  private String password;
  private int postsCount;
  private int followersCount;
  private int followingCount;
  private List<Picture> pictures = new ArrayList<>();

  public User(String username) {
    this.username = username;
    readUserDetails();
    readFollowingDetails();
    readPostsDetails();
  }

  public User(String username, String password, String bio) {
    this.username = username;
    this.bio = bio;
    this.password = password;
    readFollowingDetails();
  }

  // Add a picture to the user's profile
  public void addPicture(Picture picture) {
    pictures.add(picture);
    postsCount++;
  }

  private void readUserDetails() {
    DatabaseUtils.getUserDetails(this.username);
  }

  private void readFollowingDetails() {
    int followersCount = 0;
    int followingCount = 0;
    Path followingFilePath = Paths.get("data", "following.txt");

    try (BufferedReader followingReader = Files.newBufferedReader(followingFilePath)) {
      String line;
      while ((line = followingReader.readLine()) != null) {
        String[] parts = line.split(":");
        if (parts.length == 2) {
          String username = parts[0].trim();
          String[] followingUsers = parts[1].split(";");
          if (username.equals(this.username)) {
            followingCount = followingUsers.length;
          } else {
            for (String followingUser : followingUsers) {
              if (followingUser.trim().equals(this.username)) {
                followersCount++;
              }
            }
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.followersCount = followersCount;
    this.followingCount = followingCount;
  }

  private void readPostsDetails() {
    int imageCount = 0;
    Path imageDetailsFilePath = Paths.get("img", "image_details.txt");

    try (BufferedReader imageDetailsReader = Files.newBufferedReader(imageDetailsFilePath)) {
      String line;
      while ((line = imageDetailsReader.readLine()) != null) {
        if (line.contains("Username: " + this.username)) {
          imageCount++;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.postsCount = imageCount;
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
    return username + ":" + bio + ":" + password; // Format as needed
  }

}