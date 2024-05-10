package Logic;

import java.sql.Timestamp;

public class Notification {
    private final Integer id;
    private final User user;
    private final String message;
    private final Timestamp timestamp;

    public Notification(Integer id, User user, String message, Timestamp timestamp) {
        this.id = id;
        this.user = user;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}