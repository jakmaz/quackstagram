# Final Report for Quackstagram Project

## Table of Contents

- [Designing a Relational Database Schema](#designing-a-relational-database-schema)
- [Implementing a MySQL-Compatible Relational Database Schema](#implementing-a-mysql-compatible-relational-database-schema)
- [Integrating and Functionally Developing the Application](#integrating-and-functionally-developing-the-application)
- [Maximizing Profits Through SQL Queries](#maximizing-profits-through-sql-queries)
- [Conclusion](#conclusion)
- [Appendices](#appendices)

### Project Overview

Quackstagram is a dynamic social media platform designed for sharing visual content and engaging with others through posts, likes, comments, and follows. The project's main objective is to enhance the platform by transitioning from a basic system using text files to a robust, scalable relational database. This upgrade aims to improve performance, enable detailed analytics for strategic decision-making, and monetize the platform through targeted advertising. By integrating advanced database features, Quackstagram seeks to enrich user interaction and establish itself as a leading social network in the digital era.

### Team Members

- **Students**:
  - Jakub Mazur - I6349651
  - Tomasz Mizera - I6357148
- **Colaboration**:
  - Both members collaborated equally to the develompent of the project in all fields.

## Designing a Relational Database Schema

### Entity Analysis

This section provides an abstracted overview of the core entities identified within the Quackstagram application framework. Each entity is crucial for the relational dynamics of the platform, supporting a wide range of features from user interactions to content management and notifications.

#### Entities and Their Attributes

1. **User**

   - **Description:** Represents individuals registered on the platform, encapsulating all necessary personal information.
   - **Attributes:**
     - **Authentication Details:** Ensures secure access to the user's account.
     - **Profile Information:** Includes biographical details and visual identifiers.

2. **Post**

   - **Description:** Constitutes the primary content created by users, showcasing media and textual descriptions.
   - **Attributes:**
     - **Ownership Link:** Connects the post to the user who created it.
     - **Content Details:** Encompasses the media path and accompanying caption.
     - **Creation Timestamp:** Records the time at which the post was made.

3. **Comment**

   - **Description:** Allows users to engage in discussions by commenting on posts, fostering community interaction.
   - **Attributes:**
     - **Post Link:** Associates the comment with a specific post.
     - **User Link:** Indicates the user who made the comment.
     - **Text Content:** Contains the actual comment text.
     - **Comment Timestamp:** Marks the moment the comment was posted.

4. **Like**

   - **Description:** Represents a user's approval of a post, an integral part of social feedback mechanisms.
   - **Attributes:**
     - **Post Link:** Associates the like with a specific post.
     - **User Link:** Identifies the user who liked the post.
     - **Like Timestamp:** Records when the like was registered.

5. **Follower**

   - **Description:** Tracks relationships between users, specifically who follows whom, vital for network growth and content dissemination.
   - **Attributes:**
     - **Follower Link:** Identifies the user who is following.
     - **Following Link:** Identifies the user being followed.
     - **Follow Timestamp:** Records the initiation of the following relationship.

6. **Notification**
   - **Description:** Serves to inform users about relevant activities within their network, such as new followers, likes, or comments.
   - **Attributes:**
     - **Identifier:** Uniquely identifies each notification.
     - **Recipient Link:** Connects the notification to the user it targets.
     - **Relevant Post Link:** Optionally links to related content.
     - **Message Content:** Describes the reason for the notification.
     - **Notification Timestamp:** Timestamps when the notification was generated.

This abstracted analysis underscores the functional roles these entities play within the Quackstagram ecosystem, emphasizing their interrelationships and the attributes that facilitate the platform's operational requirements. This perspective helps in understanding how data is structured to support user interactions and content dynamics effectively.

### Relationship Mapping

This section explores the relational dynamics between the core entities within the Quackstagram platform. The relationships are fundamental to structuring the database and defining how entities interact with one another, which is crucial for queries, data integrity, and maintaining logical consistency throughout the application.

#### Entity Relationships

1. **User to Post**

   - **Type:** One-to-Many
   - **Description:** A single user can create multiple posts, but each post is created by only one user. This relationship facilitates user content generation and management.

2. **Post to Comment**

   - **Type:** One-to-Many
   - **Description:** Each post can have multiple comments, but each comment is associated with only one post. This structure supports community engagement and interaction on individual pieces of content.

3. **User to Comment**

   - **Type:** One-to-Many
   - **Description:** A user can make several comments on various posts, but each comment is made by a single user, linking users to their interactive contributions across the platform.

4. **Post to Like**

   - **Type:** Many-to-Many (realized via a junction table with unique constraints)
   - **Description:** Users can like many posts, and each post can be liked by multiple users. This many-to-many relationship is implemented through a Likes table that records each like uniquely to a post by a user, capturing user preferences and engagement.

5. **User to Follower**

   - **Type:** Many-to-Many (implemented using a bridge table)
   - **Description:** Users can follow multiple other users, and each user can be followed by multiple other users. This relationship is crucial for forming the social network and allows for the dissemination of content and updates within interconnected user circles.

6. **User to Notification**
   - **Type:** One-to-Many
   - **Description:** A single user can receive multiple notifications, but each notification is targeted at a single user. This relationship ensures that users are informed about relevant activities impacting their profile or content.

These relationships are meticulously designed to support the functional requirements of the Quackstagram platform, ensuring that users can interact, connect, and engage with content and each other in a meaningful way. The mapping highlights not only the relational model but also the platform's capacity to scale and manage complex interactions and data flows between users and their content.

### Entity Relationship Diagram (ERD)

The Entity-Relationship Diagram (ERD) presented below visually illustrates the relationships between the core entities within the Quackstagram database. This diagram provides a clear and organized view of the database structure, highlighting how entities such as Users, Posts, Comments, and others are interconnected. It serves as a vital tool for understanding the database's architecture, aiding in both development and discussion of the system's design.

### Normalization

- Documentation of functional dependencies and normalization steps up to 3NF or BCNF.

## Implementing a MySQL-Compatible Relational Database Schema

### Schema.sql

This section details the SQL statements used to create the database schema for the project. Each table is designed to store specific types of data crucial for the platform's functionality.

#### Users Table

```sql
CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  bio VARCHAR(255),
  image_path VARCHAR(255)
);
```

#### Posts Table

```sql
CREATE TABLE IF NOT EXISTS posts (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  caption VARCHAR(255),
  image_path VARCHAR(255),
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

#### Comments Table

```sql
CREATE TABLE IF NOT EXISTS comments (
  id INT AUTO_INCREMENT PRIMARY KEY,
  post_id INT NOT NULL,
  user_id INT NOT NULL,
  text VARCHAR(255),
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (post_id) REFERENCES posts(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

#### Likes Table

```sql
CREATE TABLE IF NOT EXISTS likes (
  post_id INT,
  user_id INT,
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (post_id, user_id),
  FOREIGN KEY (post_id) REFERENCES posts(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

#### Followers Table

```sql
CREATE TABLE IF NOT EXISTS followers (
  follower_id INT,
  following_id INT,
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (follower_id, following_id),
  FOREIGN KEY (follower_id) REFERENCES users(id),
  FOREIGN KEY (following_id) REFERENCES users(id)
);
```

#### Notifications Table

```sql
CREATE TABLE IF NOT EXISTS notifications (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  post_id INT,
  message VARCHAR(255),
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (post_id) REFERENCES posts(id)
);
```

### Views.sql

This section details the SQL statements used to create views in the Quackstagram database. Views are implemented to simplify data access and enhance query performance for recurring data analysis tasks, providing aggregated or specific views of the data tailored for various application needs.

#### User Activity View

This view aggregates the total number of posts, comments, and likes for each user, providing a quick snapshot of user engagement on the platform.

```sql
CREATE VIEW IF NOT EXISTS UserActivity AS
SELECT
    u.id AS user_id,
    u.username,
    COUNT(DISTINCT p.id) AS total_posts,
    COUNT(DISTINCT c.id) AS total_comments,
    COUNT(DISTINCT l.post_id) AS total_likes
FROM users u
LEFT JOIN posts p ON u.id = p.user_id
LEFT JOIN comments c ON u.id = c.user_id
LEFT JOIN likes l ON u.id = l.user_id
GROUP BY u.id, u.username;
```

#### Popular Posts View

This view identifies the most liked posts, displaying the post details along with the count of likes, aiding in content popularity analysis.

```sql
CREATE VIEW IF NOT EXISTS PopularPosts AS
SELECT
    p.id AS post_id,
    p.user_id,
    p.caption,
    COUNT(l.user_id) AS like_count
FROM posts p
JOIN likes l ON p.id = l.post_id
GROUP BY p.id
ORDER BY like_count DESC;
```

#### Recent User Interactions View

This view provides information on recent interactions (likes or comments) by users on different posts, useful for tracking activity and engagement trends.

```sql
CREATE VIEW IF NOT EXISTS RecentInteractions AS
SELECT
    u.username,
    p.id AS post_id,
    p.caption,
    'Like' AS interaction_type,
    l.timestamp AS interaction_time
FROM likes l
JOIN posts p ON l.post_id = p.id
JOIN users u ON l.user_id = u.id
UNION ALL
SELECT
    u.username,
    p.id,
    p.caption,
    'Comment' AS interaction_type,
    c.timestamp
FROM comments c
JOIN posts p ON c.post_id = p.id
JOIN users u ON c.user_id = u.id
ORDER BY interaction_time DESC;
```

Each of these views serves a specific purpose, from providing an overview of individual user activity to highlighting the most engaged-with content on the platform. They are designed to support the platform's reporting and analytical capabilities, making it easier to derive insights from the data without the need to repeatedly query the base tables.

### Triggers.sql

This section outlines the SQL triggers implemented in the Quackstagram database. These triggers automatically generate notifications when users follow each other, like posts, or comment on posts, enhancing user engagement and interaction.

#### Followers Trigger

This trigger creates a notification whenever a user gains a new follower.

```sql
CREATE TRIGGER IF NOT EXISTS trg_after_follow
AFTER INSERT ON followers
FOR EACH ROW
BEGIN
    INSERT INTO notifications (user_id, message, timestamp)
    VALUES (NEW.following_id, CONCAT('You have a new follower: ', (SELECT username FROM users WHERE id = NEW.follower_id)), NEW.timestamp);
END;
```

#### Likes Trigger

This trigger creates a notification when a post is liked, informing the post owner about who liked the post.

```sql
CREATE TRIGGER IF NOT EXISTS trg_after_like
AFTER INSERT ON likes
FOR EACH ROW
BEGIN
    INSERT INTO notifications (user_id, post_id, message, timestamp)
    VALUES ((SELECT user_id FROM posts WHERE id = NEW.post_id), NEW.post_id, CONCAT('Your post was liked by ', (SELECT username FROM users WHERE id = NEW.user_id)), NEW.timestamp);
END;
```

#### Comments Trigger

This trigger sends a notification to the post owner when a new comment is made on their post, including who commented and what was said.

```sql
CREATE TRIGGER IF NOT EXISTS trg_after_comment
AFTER INSERT ON comments
FOR EACH ROW
BEGIN
    INSERT INTO notifications (user_id, post_id, message, timestamp)
    VALUES ((SELECT user_id FROM posts WHERE id = NEW.post_id), NEW.post_id, CONCAT('Your post was commented on ', (SELECT username FROM users WHERE id = NEW.user_id), ': ', NEW.text), NEW.timestamp);
END;
```

Each trigger uses an `AFTER INSERT` event on the respective tables (`followers`, `likes`, `comments`), ensuring that notifications are generated immediately after the actions occur. These triggers are fundamental in keeping the users engaged by promptly updating them about interactions related to their content on the Quackstagram platform.

## Integrating and Functionally Developing the Application

### Tasks Completed

- **Database Connection**: Description of the JDBC setup.
- **Feature Implementation**: How features were implemented using the database.
- **Error Handling and Security**: Discuss the strategies used for handling errors and securing the database interactions.

## Maximizing Profits Through SQL Queries

### Queries and Results

List each SQL query along with a brief description of its purpose and output.

1. **List all users who have more than X followers where X can be any integer value.**

   ```sql
   SELECT ...
   ```

2. **Show the total number of posts made by each user.**

   ```sql
   SELECT ...
   ```

3. **Find all comments made on a particular user’s post.**

   ```sql
   SELECT ...
   ```

4. **Display the top X most liked posts.**

   ```sql
   SELECT ...
   ```

5. **Count the number of posts each user has liked.**

   ```sql
   SELECT ...
   ```

6. **List all users who haven’t made a post yet.**

   ```sql
   SELECT ...
   ```

7. **List users who follow each other.**

   ```sql
   SELECT ...
   ```

8. **Show the user with the highest number of posts.**

   ```sql
   SELECT ...
   ```

9. **List the top X users with the most followers.**

   ```sql
   SELECT ...
   ```

10. **Find posts that have been liked by all users.**

    ```sql
    SELECT ...
    ```

11. **Display the most active user (based on posts, comments, and likes).**

    ```sql
    SELECT ...
    ```

12. **Find the average number of likes per post for each user.**

    ```sql
    SELECT ...
    ```

13. **Show posts that have more comments than likes.**

    ```sql
    SELECT ...
    ```

14. **List the users who have liked every post of a specific user.**

    ```sql
    SELECT ...
    ```

15. **Display the most popular post of each user (based on likes).**

    ```sql
    SELECT ...
    ```

16. **Find the user(s) with the highest ratio of followers to following.**

    ```sql
    SELECT ...
    ```

17. **Show the month with the highest number of posts made.**

    ```sql
    SELECT ...
    ```

18. **Identify users who have not interacted with a specific user’s posts.**

    ```sql
    SELECT ...
    ```

19. **Display the user with the greatest increase in followers in the last X days.**

    ```sql
    SELECT ...
    ```

20. **Find users who are followed by more than X% of the platform users.**
    ```sql
    SELECT ...
    ```

## Conclusion

- Summarize the project outcomes and any lessons learned.

## Appendices

- Include any additional diagrams, code snippets, or important notices here.
