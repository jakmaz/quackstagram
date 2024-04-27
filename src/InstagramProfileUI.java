import javax.swing.*;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.awt.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class InstagramProfileUI extends JFrame {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    private static final int PROFILE_IMAGE_SIZE = 80; // Adjusted size for the profile image to match UI
    private static final int GRID_IMAGE_SIZE = WIDTH / 3; // Static size for grid images
    private static final int NAV_ICON_SIZE = 20; // Corrected static size for bottom icons
    private JPanel contentPanel; // Panel to display the image grid or the clicked image
    private JPanel headerPanel;   // Panel for the header
    private JPanel navigationPanel; // Panel for the navigation
    private User currentUser; // User object to store the current user's information

    public InstagramProfileUI(User user) {
        this.currentUser = user;
         // Initialize counts
        int imageCount = 0;
        int followersCount = 0;
        int followingCount = 0;
       
        // Step 1: Read image_details.txt to count the number of images posted by the user
    Path imageDetailsFilePath = Paths.get("img", "image_details.txt");
    try (BufferedReader imageDetailsReader = Files.newBufferedReader(imageDetailsFilePath)) {
        String line;
        while ((line = imageDetailsReader.readLine()) != null) {
            if (line.contains("Username: " + currentUser.getUsername())) {
                imageCount++;
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Step 2: Read following.txt to calculate followers and following
    Path followingFilePath = Paths.get("data", "following.txt");
    try (BufferedReader followingReader = Files.newBufferedReader(followingFilePath)) {
        String line;
        while ((line = followingReader.readLine()) != null) {
            String[] parts = line.split(":");
            if (parts.length == 2) {
                String username = parts[0].trim();
                String[] followingUsers = parts[1].split(";");
                if (username.equals(currentUser.getUsername())) {
                    followingCount = followingUsers.length;
                } else {
                    for (String followingUser : followingUsers) {
                        if (followingUser.trim().equals(currentUser.getUsername())) {
                            followersCount++;
                        }
                    }
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    String bio = "";

    Path bioDetailsFilePath = Paths.get("data", "credentials.txt");
    try (BufferedReader bioDetailsReader = Files.newBufferedReader(bioDetailsFilePath)) {
        String line;
        while ((line = bioDetailsReader.readLine()) != null) {
            String[] parts = line.split(":");
            if (parts[0].equals(currentUser.getUsername()) && parts.length >= 3) {
                bio = parts[2];
                break; // Exit the loop once the matching bio is found
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    System.out.println("Bio for " + currentUser.getUsername() + ": " + bio);
    currentUser.setBio(bio);
    

    currentUser.setFollowersCount(followersCount);
    currentUser.setFollowingCount(followingCount);
    currentUser.setPostCount(imageCount);

    System.out.println(currentUser.getPostsCount());

     setTitle("DACS Profile");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        contentPanel = new JPanel();
        headerPanel = createHeaderPanel();       // Initialize header panel
        navigationPanel = createNavigationPanel(); // Initialize navigation panel

        initializeUI();
    }


      public InstagramProfileUI() {

        setTitle("DACS Profile");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        contentPanel = new JPanel();
        headerPanel = createHeaderPanel();       // Initialize header panel
        navigationPanel = createNavigationPanel(); // Initialize navigation panel
        initializeUI();
    }

    private void initializeUI() {
        getContentPane().removeAll(); // Clear existing components
        
        // Re-add the header and navigation panels
        add(headerPanel, BorderLayout.NORTH);
        add(navigationPanel, BorderLayout.SOUTH);

        // Initialize the image grid
        initializeImageGrid();

        revalidate();
        repaint();
    }

    private JPanel createHeaderPanel() {
        boolean isCurrentUser = false;
        String loggedInUsername = "";

        // Read the logged-in user's username from users.txt
    try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
        String line = reader.readLine();
        if (line != null) {
            loggedInUsername = line.split(":")[0].trim();
            isCurrentUser = loggedInUsername.equals(currentUser.getUsername());
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    
       // Header Panel
        JPanel headerPanel = new JPanel();
        try (Stream<String> lines = Files.lines(Paths.get("data", "users.txt"))) {
            isCurrentUser = lines.anyMatch(line -> line.startsWith(currentUser.getUsername() + ":"));
        } catch (IOException e) {
            e.printStackTrace();  // Log or handle the exception as appropriate
        }

        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.GRAY);
        
        // Top Part of the Header (Profile Image, Stats, Follow Button)
        JPanel topHeaderPanel = new JPanel(new BorderLayout(10, 0));
        topHeaderPanel.setBackground(new Color(249, 249, 249));

        // Profile image
        ImageIcon profileIcon = new ImageIcon(new ImageIcon("img/storage/profile/"+currentUser.getUsername()+".png").getImage().getScaledInstance(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Image.SCALE_SMOOTH));
        JLabel profileImage = new JLabel(profileIcon);
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topHeaderPanel.add(profileImage, BorderLayout.WEST);

        // Stats Panel
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        statsPanel.setBackground(new Color(249, 249, 249));
        System.out.println("Number of posts for this user"+currentUser.getPostsCount());
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getPostsCount()) , "Posts"));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getFollowersCount()), "Followers"));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getFollowingCount()), "Following"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0)); // Add some vertical padding

        
// Follow Button
// Follow or Edit Profile Button
// followButton.addActionListener(e -> handleFollowAction(currentUser.getUsername()));
JButton followButton;
    if (isCurrentUser) {
        followButton = new JButton("Edit Profile");
    } else {
        followButton = new JButton("Follow");

        // Check if the current user is already being followed by the logged-in user
        Path followingFilePath = Paths.get("data", "following.txt");
        try (BufferedReader reader = Files.newBufferedReader(followingFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].trim().equals(loggedInUsername)) {
                    String[] followedUsers = parts[1].split(";");
                    for (String followedUser : followedUsers) {
                        if (followedUser.trim().equals(currentUser.getUsername())) {
                            followButton.setText("Following");
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        followButton.addActionListener(e -> {
            handleFollowAction(currentUser.getUsername());
            followButton.setText("Following");
        });
    }
    
followButton.setAlignmentX(Component.CENTER_ALIGNMENT);
followButton.setFont(new Font("Arial", Font.BOLD, 12));
followButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, followButton.getMinimumSize().height)); // Make the button fill the horizontal space
followButton.setBackground(new Color(225, 228, 232)); // A soft, appealing color that complements the UI
followButton.setForeground(Color.BLACK);
followButton.setOpaque(true);
followButton.setBorderPainted(false);
followButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some vertical padding

        
        // Add Stats and Follow Button to a combined Panel
        JPanel statsFollowPanel = new JPanel();
        statsFollowPanel.setLayout(new BoxLayout(statsFollowPanel, BoxLayout.Y_AXIS));
        statsFollowPanel.add(statsPanel);
        statsFollowPanel.add(followButton);
        topHeaderPanel.add(statsFollowPanel, BorderLayout.CENTER);

        headerPanel.add(topHeaderPanel);

     // Profile Name and Bio Panel
JPanel profileNameAndBioPanel = new JPanel();
profileNameAndBioPanel.setLayout(new BorderLayout());
profileNameAndBioPanel.setBackground(new Color(249, 249, 249));

JLabel profileNameLabel = new JLabel(currentUser.getUsername());
profileNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
profileNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Padding on the sides

JTextArea profileBio = new JTextArea(currentUser.getBio());
System.out.println("This is the bio "+currentUser.getUsername());
profileBio.setEditable(false);
profileBio.setFont(new Font("Arial", Font.PLAIN, 12));
profileBio.setBackground(new Color(249, 249, 249));
profileBio.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // Padding on the sides

profileNameAndBioPanel.add(profileNameLabel, BorderLayout.NORTH);
profileNameAndBioPanel.add(profileBio, BorderLayout.CENTER);

headerPanel.add(profileNameAndBioPanel);


        
        return headerPanel;

    }


   private void handleFollowAction(String usernameToFollow) {
    Path followingFilePath = Paths.get("data", "following.txt");
    Path usersFilePath = Paths.get("data", "users.txt");
    String currentUserUsername = "";

    try {
        // Read the current user's username from users.txt
        try (BufferedReader reader = Files.newBufferedReader(usersFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
               currentUserUsername = parts[0];
            }
        }

        System.out.println("Real user is "+currentUserUsername);
        // If currentUserUsername is not empty, process following.txt
        if (!currentUserUsername.isEmpty()) {
            boolean found = false;
            StringBuilder newContent = new StringBuilder();

            // Read and process following.txt
            if (Files.exists(followingFilePath)) {
                try (BufferedReader reader = Files.newBufferedReader(followingFilePath)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(":");
                        if (parts[0].trim().equals(currentUserUsername)) {
                            found = true;
                            if (!line.contains(usernameToFollow)) {
                                line = line.concat(line.endsWith(":") ? "" : "; ").concat(usernameToFollow);
                            }
                        }
                        newContent.append(line).append("\n");
                    }
                }
            }

            // If the current user was not found in following.txt, add them
            if (!found) {
                newContent.append(currentUserUsername).append(": ").append(usernameToFollow).append("\n");
            }

            // Write the updated content back to following.txt
            try (BufferedWriter writer = Files.newBufferedWriter(followingFilePath)) {
                writer.write(newContent.toString());
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    

    
    
    private JPanel createNavigationPanel() {
        // Navigation Bar
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(new Color(249, 249, 249));
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        navigationPanel.add(createIconButton("img/icons/home.png", "home"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton("img/icons/search.png","explore"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton("img/icons/add.png","add"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton("img/icons/heart.png","notification"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton("img/icons/profile.png", "profile"));

        return navigationPanel;

    }

private void initializeImageGrid() {
    contentPanel.removeAll(); // Clear existing content
    contentPanel.setLayout(new GridLayout(0, 3, 5, 5)); // Grid layout for image grid

    Path imageDir = Paths.get("img", "uploaded");
    try (Stream<Path> paths = Files.list(imageDir)) {
        paths.filter(path -> path.getFileName().toString().startsWith(currentUser.getUsername() + "_"))
             .forEach(path -> {
                 ImageIcon imageIcon = new ImageIcon(new ImageIcon(path.toString()).getImage().getScaledInstance(GRID_IMAGE_SIZE, GRID_IMAGE_SIZE, Image.SCALE_SMOOTH));
                 JLabel imageLabel = new JLabel(imageIcon);
                 imageLabel.addMouseListener(new MouseAdapter() {
                     @Override
                     public void mouseClicked(MouseEvent e) {
                         displayImage(imageIcon); // Call method to display the clicked image
                     }
                 });
                 contentPanel.add(imageLabel);
             });
    } catch (IOException ex) {
        ex.printStackTrace();
        // Handle exception (e.g., show a message or log)
    }

    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center

    revalidate();
    repaint();
}



    private void displayImage(ImageIcon imageIcon) {
        contentPanel.removeAll(); // Remove existing content
        contentPanel.setLayout(new BorderLayout()); // Change layout for image display

        JLabel fullSizeImageLabel = new JLabel(imageIcon);
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(fullSizeImageLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            getContentPane().removeAll(); // Remove all components from the frame
            initializeUI(); // Re-initialize the UI
        });
        contentPanel.add(backButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }



    private JLabel createStatLabel(String number, String text) {
        JLabel label = new JLabel("<html><div style='text-align: center;'>" + number + "<br/>" + text + "</div></html>", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.BLACK);
        return label;
    }

    private JButton createIconButton(String iconPath, String buttonType) {
        ImageIcon iconOriginal = new ImageIcon(iconPath);
        Image iconScaled = iconOriginal.getImage().getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(iconScaled));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
    
        // Define actions based on button type
        if ("home".equals(buttonType)) {
            button.addActionListener(e -> openHomeUI());
        } else if ("profile".equals(buttonType)) {
            //
        } else if ("notification".equals(buttonType)) {
            button.addActionListener(e -> notificationsUI());
        } else if ("explore".equals(buttonType)) {
            button.addActionListener(e -> exploreUI());
        } else if ("add".equals(buttonType)) {
            button.addActionListener(e -> ImageUploadUI());
        }
        return button;
    
        
    }
 
    private void ImageUploadUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        ImageUploadUI upload = new ImageUploadUI();
        upload.setVisible(true);
    }

    private void openProfileUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        InstagramProfileUI profileUI = new InstagramProfileUI();
        profileUI.setVisible(true);
    }
 
     private void notificationsUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        NotificationsUI notificationsUI = new NotificationsUI();
        notificationsUI.setVisible(true);
    }
 
    private void openHomeUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        QuakstagramHomeUI homeUI = new QuakstagramHomeUI();
        homeUI.setVisible(true);
    }
 
    private void exploreUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        ExploreUI explore = new ExploreUI();
        explore.setVisible(true);
    }   

    
}
