import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QuakstagramHomeUI extends JFrame {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    private static final int NAV_ICON_SIZE = 20; // Corrected static size for bottom icons
    private static final int IMAGE_WIDTH = WIDTH - 100; // Width for the image posts
    private static final int IMAGE_HEIGHT = 150; // Height for the image posts
    private static final Color LIKE_BUTTON_COLOR = new Color(255, 90, 95); // Color for the like button
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel homePanel;
    private JPanel imageViewPanel;
    

    public QuakstagramHomeUI() {
        setTitle("Quakstagram Home");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        homePanel = new JPanel(new BorderLayout());
        imageViewPanel = new JPanel(new BorderLayout());

        initializeUI();

        cardPanel.add(homePanel, "Home");
        cardPanel.add(imageViewPanel, "ImageView");

        add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "Home"); // Start with the home view
        
         // Header Panel (reuse from InstagramProfileUI or customize for home page)
          // Header with the Register label
          JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
          headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
          JLabel lblRegister = new JLabel("🐥 Quackstagram 🐥");
          lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
          lblRegister.setForeground(Color.WHITE); // Set the text color to white
          headerPanel.add(lblRegister);
          headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
        
          add(headerPanel, BorderLayout.NORTH);


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


        add(navigationPanel, BorderLayout.SOUTH);
    }

    private void initializeUI() {
       

        // Content Scroll Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Vertical box layout
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Never allow horizontal scrolling
         String[][] sampleData = createSampleData(); 
        populateContentPanel(contentPanel, sampleData);
        add(scrollPane, BorderLayout.CENTER);


        

         // Set up the home panel

         contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        
      

         homePanel.add(scrollPane, BorderLayout.CENTER);
       

    }

    private void populateContentPanel(JPanel panel, String[][] sampleData) {

         for (String[] postData : sampleData) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
            itemPanel.setBackground(Color.WHITE); // Set the background color for the item panel
            itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            itemPanel.setAlignmentX(CENTER_ALIGNMENT);
            JLabel nameLabel = new JLabel(postData[0]);
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Crop the image to the fixed size
            JLabel imageLabel = new JLabel();
            imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
            imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to image label
            String imageId = new File(postData[3]).getName().split("\\.")[0];
            try {
                BufferedImage originalImage = ImageIO.read(new File(postData[3]));
                BufferedImage croppedImage = originalImage.getSubimage(0, 0, Math.min(originalImage.getWidth(), IMAGE_WIDTH), Math.min(originalImage.getHeight(), IMAGE_HEIGHT));
                ImageIcon imageIcon = new ImageIcon(croppedImage);
                imageLabel.setIcon(imageIcon);
            } catch (IOException ex) {
                // Handle exception: Image file not found or reading error
                imageLabel.setText("Image not found");
            }

            JLabel descriptionLabel = new JLabel(postData[1]);
            descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel likesLabel = new JLabel(postData[2]);
            likesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JButton likeButton = new JButton("❤");
            likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            likeButton.setBackground(LIKE_BUTTON_COLOR); // Set the background color for the like button
            likeButton.setOpaque(true);
            likeButton.setBorderPainted(false); // Remove border
            likeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleLikeAction(imageId, likesLabel);
                }
            });

            itemPanel.add(nameLabel);
            itemPanel.add(imageLabel);
            itemPanel.add(descriptionLabel);
            itemPanel.add(likesLabel);
            itemPanel.add(likeButton);

            panel.add(itemPanel);

              // Make the image clickable
              imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    displayImage(postData); // Call a method to switch to the image view
                }
            });
        

            // Grey spacing panel
            JPanel spacingPanel = new JPanel();
            spacingPanel.setPreferredSize(new Dimension(WIDTH-10, 5)); // Set the height for spacing
            spacingPanel.setBackground(new Color(230, 230, 230)); // Grey color for spacing
            panel.add(spacingPanel);
        }
    }

private void handleLikeAction(String imageId, JLabel likesLabel) {
    Path detailsPath = Paths.get("img", "image_details.txt");
    StringBuilder newContent = new StringBuilder();
    boolean updated = false;
    String currentUser = "";
    String imageOwner = "";
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    // Retrieve the current user from users.txt
    try (BufferedReader userReader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
        String line = userReader.readLine();
        if (line != null) {
            currentUser = line.split(":")[0].trim();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Read and update image_details.txt
    try (BufferedReader reader = Files.newBufferedReader(detailsPath)) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("ImageID: " + imageId)) {
                String[] parts = line.split(", ");
                imageOwner = parts[1].split(": ")[1];
                int likes = Integer.parseInt(parts[4].split(": ")[1]);
                likes++; // Increment the likes count
                parts[4] = "Likes: " + likes;
                line = String.join(", ", parts);

                // Update the UI
                likesLabel.setText("Likes: " + likes);
                updated = true;
            }
            newContent.append(line).append("\n");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Write updated likes back to image_details.txt
    if (updated) {
        try (BufferedWriter writer = Files.newBufferedWriter(detailsPath)) {
            writer.write(newContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Record the like in notifications.txt
        String notification = String.format("%s; %s; %s; %s\n", imageOwner, currentUser, imageId, timestamp);
        try (BufferedWriter notificationWriter = Files.newBufferedWriter(Paths.get("data", "notifications.txt"), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            notificationWriter.write(notification);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


    
private String[][] createSampleData() {
    String currentUser = "";
    try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
        String line = reader.readLine();
        if (line != null) {
            currentUser = line.split(":")[0].trim();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    String followedUsers = "";
    try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "following.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(currentUser + ":")) {
                followedUsers = line.split(":")[1].trim();
                break;
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Temporary structure to hold the data
    String[][] tempData = new String[100][]; // Assuming a maximum of 100 posts for simplicity
    int count = 0;

    try (BufferedReader reader = Files.newBufferedReader(Paths.get("img", "image_details.txt"))) {
        String line;
        while ((line = reader.readLine()) != null && count < tempData.length) {
            String[] details = line.split(", ");
            String imagePoster = details[1].split(": ")[1];
            if (followedUsers.contains(imagePoster)) {
                String imagePath = "img/uploaded/" + details[0].split(": ")[1] + ".png"; // Assuming PNG format
                String description = details[2].split(": ")[1];
                String likes = "Likes: " + details[4].split(": ")[1];

                tempData[count++] = new String[]{imagePoster, description, likes, imagePath};
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Transfer the data to the final array
    String[][] sampleData = new String[count][];
    System.arraycopy(tempData, 0, sampleData, 0, count);

    return sampleData;
}

    private JButton createIconButton(String iconPath) {
        ImageIcon iconOriginal = new ImageIcon(iconPath);
        Image iconScaled = iconOriginal.getImage().getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(iconScaled));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        return button;
    }

    private void displayImage(String[] postData) {
        imageViewPanel.removeAll(); // Clear previous content

       
        String imageId = new File(postData[3]).getName().split("\\.")[0];
        JLabel likesLabel = new JLabel(postData[2]); // Update this line



        // Display the image
        JLabel fullSizeImageLabel = new JLabel();
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);
      

         try {
                BufferedImage originalImage = ImageIO.read(new File(postData[3]));
                BufferedImage croppedImage = originalImage.getSubimage(0, 0, Math.min(originalImage.getWidth(), WIDTH-20), Math.min(originalImage.getHeight(), HEIGHT-40));
                ImageIcon imageIcon = new ImageIcon(croppedImage);
                fullSizeImageLabel.setIcon(imageIcon);
            } catch (IOException ex) {
                // Handle exception: Image file not found or reading error
                fullSizeImageLabel.setText("Image not found");
            }

        //User Info 
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel,BoxLayout.Y_AXIS));
        JLabel userName = new JLabel(postData[0]);
        userName.setFont(new Font("Arial", Font.BOLD, 18));
        userPanel.add(userName);//User Name

           JButton likeButton = new JButton("❤");
            likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            likeButton.setBackground(LIKE_BUTTON_COLOR); // Set the background color for the like button
            likeButton.setOpaque(true);
            likeButton.setBorderPainted(false); // Remove border
            likeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                   handleLikeAction(imageId, likesLabel); // Update this line
                   refreshDisplayImage(postData, imageId); // Refresh the view
                }
            });
       
        // Information panel at the bottom
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel(postData[1])); // Description
        infoPanel.add(new JLabel(postData[2])); // Likes
        infoPanel.add(likeButton);

        imageViewPanel.add(fullSizeImageLabel, BorderLayout.CENTER);
        imageViewPanel.add(infoPanel, BorderLayout.SOUTH);
        imageViewPanel.add(userPanel,BorderLayout.NORTH);
            
        imageViewPanel.revalidate();
        imageViewPanel.repaint();


        cardLayout.show(cardPanel, "ImageView"); // Switch to the image view
    }

    private void refreshDisplayImage(String[] postData, String imageId) {
        // Read updated likes count from image_details.txt
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("img", "image_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("ImageID: " + imageId)) {
                    String likes = line.split(", ")[4].split(": ")[1];
                    postData[2] = "Likes: " + likes;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // Call displayImage with updated postData
        displayImage(postData);
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

    private void ImageUploadUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        ImageUploadUI upload = new ImageUploadUI();
        upload.setVisible(true);
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
