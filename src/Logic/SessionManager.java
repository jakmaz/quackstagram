package Logic;

public class SessionManager {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
        System.out.println("Session updated: User " + user.getUsername() + " is now logged in.");
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void clearCurrentUser() {
        System.out.println("Session cleared: User " + (currentUser != null ? currentUser.getUsername() : "none") + " logged out.");
        currentUser = null;
    }
}
