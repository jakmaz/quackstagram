import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExploreUI extends JFrame {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    private static final int NAV_ICON_SIZE = 20; // Size for navigation icons
    private static final int IMAGE_SIZE = WIDTH / 3; // Size for each image in the grid

    public ExploreUI() {
        setTitle("Explore");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        
        getContentPane().removeAll(); // Clear existing components
        setLayout(new BorderLayout()); // Reset the layout manager

        JPanel headerPanel = createHeaderPanel(); // Method from your InstagramProfileUI class
        JPanel navigationPanel = createNavigationPanel(); // Method from your InstagramProfileUI class
        JPanel mainContentPanel = createMainContentPanel();

        // Add panels to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();

        
    }
    
    private JPanel createMainContentPanel() {
        // Create the main content panel with search and image grid
      // Search bar at the top
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField(" Search Users");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height)); // Limit the height
    
       // Image Grid
    JPanel imageGridPanel = new JPanel(new GridLayout(0, 3, 2, 2)); // 3 columns, auto rows

    // Load images from the uploaded folder
    File imageDir = new File("img/uploaded");
    if (imageDir.exists() && imageDir.isDirectory()) {
        File[] imageFiles = imageDir.listFiles((dir, name) -> name.matches(".*\\.(png|jpg|jpeg)"));
        if (imageFiles != null) {
            for (File imageFile : imageFiles) {
                ImageIcon imageIcon = new ImageIcon(new ImageIcon(imageFile.getPath()).getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH));
                JLabel imageLabel = new JLabel(imageIcon);
                imageLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        displayImage(imageFile.getPath()); // Call method to display the clicked image
                    }
                });
                imageGridPanel.add(imageLabel);
            }
        }
    }

    JScrollPane scrollPane = new JScrollPane(imageGridPanel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    // Main content panel that holds both the search bar and the image grid
    JPanel mainContentPanel = new JPanel();
    mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
    mainContentPanel.add(searchPanel);
    mainContentPanel.add(scrollPane); // This will stretch to take up remaining space
    return mainContentPanel;
}

   
    private JPanel createHeaderPanel() {
       
        // Header Panel (reuse from InstagramProfileUI or customize for home page)
         // Header with the Register label
         JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
         headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
         JLabel lblRegister = new JLabel(" Explore 🐥");
         lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
         lblRegister.setForeground(Color.WHITE); // Set the text color to white
         headerPanel.add(lblRegister);
         headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
         return headerPanel;
   }

   private JPanel createNavigationPanel() {
       // Create and return the navigation panel
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

   private void displayImage(String imagePath) {
    getContentPane().removeAll();
    setLayout(new BorderLayout());

    // Add the header and navigation panels back
    add(createHeaderPanel(), BorderLayout.NORTH);
    add(createNavigationPanel(), BorderLayout.SOUTH);

    JPanel imageViewerPanel = new JPanel(new BorderLayout());

    // Extract image ID from the imagePath
    String imageId = new File(imagePath).getName().split("\\.")[0];
    
    // Read image details
    String username = "";
    String bio = "";
    String timestampString = "";
    int likes = 0;
    Path detailsPath = Paths.get("img", "image_details.txt");
    try (Stream<String> lines = Files.lines(detailsPath)) {
        String details = lines.filter(line -> line.contains("ImageID: " + imageId)).findFirst().orElse("");
        if (!details.isEmpty()) {
            String[] parts = details.split(", ");
            username = parts[1].split(": ")[1];
            bio = parts[2].split(": ")[1];
            System.out.println(bio+"this is where you get an error "+parts[3]);
            timestampString = parts[3].split(": ")[1];
            likes = Integer.parseInt(parts[4].split(": ")[1]);
        }
    } catch (IOException ex) {
        ex.printStackTrace();
        // Handle exception
    }

    // Calculate time since posting
    String timeSincePosting = "Unknown";
    if (!timestampString.isEmpty()) {
        LocalDateTime timestamp = LocalDateTime.parse(timestampString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime now = LocalDateTime.now();
        long days = ChronoUnit.DAYS.between(timestamp, now);
        timeSincePosting = days + " day" + (days != 1 ? "s" : "") + " ago";
    }

// Top panel for username and time since posting
JPanel topPanel = new JPanel(new BorderLayout());
JButton usernameLabel = new JButton(username);
JLabel timeLabel = new JLabel(timeSincePosting);
timeLabel.setHorizontalAlignment(JLabel.RIGHT);
topPanel.add(usernameLabel, BorderLayout.WEST);
topPanel.add(timeLabel, BorderLayout.EAST);


    // Prepare the image for display
    JLabel imageLabel = new JLabel();
    imageLabel.setHorizontalAlignment(JLabel.CENTER);
    try {
        BufferedImage originalImage = ImageIO.read(new File(imagePath));
        ImageIcon imageIcon = new ImageIcon(originalImage);
        imageLabel.setIcon(imageIcon);
    } catch (IOException ex) {
        imageLabel.setText("Image not found");
    }

    // Bottom panel for bio and likes
    JPanel bottomPanel = new JPanel(new BorderLayout());
    JTextArea bioTextArea = new JTextArea(bio);
    bioTextArea.setEditable(false);
    JLabel likesLabel = new JLabel("Likes: " + likes);
    bottomPanel.add(bioTextArea, BorderLayout.CENTER);
    bottomPanel.add(likesLabel, BorderLayout.SOUTH);

    // Adding the components to the frame
    add(topPanel, BorderLayout.NORTH);
    add(imageLabel, BorderLayout.CENTER);
    add(bottomPanel, BorderLayout.SOUTH);

    // Re-add the header and navigation panels
    add(createHeaderPanel(), BorderLayout.NORTH);
    add(createNavigationPanel(), BorderLayout.SOUTH);

 // Panel for the back button
 JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
 JButton backButton = new JButton("Back");

 // Make the button take up the full width
 backButton.setPreferredSize(new Dimension(WIDTH-20, backButton.getPreferredSize().height));

 backButtonPanel.add(backButton);

 backButton.addActionListener(e -> {
     getContentPane().removeAll();
     add(createHeaderPanel(), BorderLayout.NORTH);
     add(createMainContentPanel(), BorderLayout.CENTER);
     add(createNavigationPanel(), BorderLayout.SOUTH);
     revalidate();
     repaint();
 });
 final String finalUsername = username;

 usernameLabel.addActionListener(e -> {
    User user = new User(finalUsername); // Assuming User class has a constructor that takes a username
    InstagramProfileUI profileUI = new InstagramProfileUI(user);
    profileUI.setVisible(true);
    dispose(); // Close the current frame
});

 // Container panel for image and details
 JPanel containerPanel = new JPanel(new BorderLayout());

 containerPanel.add(topPanel, BorderLayout.NORTH);
 containerPanel.add(imageLabel, BorderLayout.CENTER);
 containerPanel.add(bottomPanel, BorderLayout.SOUTH);

 // Add the container panel and back button panel to the frame
 add(backButtonPanel, BorderLayout.NORTH);
 add(containerPanel, BorderLayout.CENTER);

 revalidate();
 repaint();
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
        button.addActionListener(e -> openProfileUI());
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
       String loggedInUsername = "";

        // Read the logged-in user's username from users.txt
    try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
        String line = reader.readLine();
        if (line != null) {
            loggedInUsername = line.split(":")[0].trim();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
     User user = new User(loggedInUsername);
       InstagramProfileUI profileUI = new InstagramProfileUI(user);
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
