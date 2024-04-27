import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// Represents a user on Quackstagram
class User {
  private String username;
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
  }

  // Add a picture to the user's profile
  public void addPicture(Picture picture) {
    pictures.add(picture);
    postsCount++;
  }

  private void readUserDetails() {
    Path userFilePath = Paths.get("data", "users.txt");

    try (BufferedReader userReader = Files.newBufferedReader(userFilePath)) {
      String line;
      while ((line = userReader.readLine()) != null) {
        String[] parts = line.split(":");
        if (parts.length == 3) {
          String username = parts[0].trim();
          if (username.equals(this.username)) {
            this.bio = parts[1].trim();
            this.password = parts[2].trim();
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
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
