package client;

import server.User;
import service.UserService;
import service.PhotoService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PhotoClient {

    private static PhotoService photoService;
    private static UserService userService;
    private static User loggedInUser;
    private static JLabel photoLabel;
    private static JComboBox<String> photoComboBox;
    private static JLabel averageRatingLabel;
    private static JLabel viewCountLabel;
    private static JTextField ratingText;
    private static JTextField commentText;
    private static JButton uploadPhotoButton;
    private static JButton addCommentButton;
    private static JButton ratePhotoButton;
    private static JTextArea commentsArea;
    private static boolean isFirstLoad = true;
    private static final int PHOTO_WIDTH = 750;
    private static final int PHOTO_HEIGHT = 500;

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            photoService = (PhotoService) registry.lookup("PhotoService");
            userService = (UserService) registry.lookup("UserService");

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    showLoginGUI();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showLoginGUI() {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        frame.add(panel);

        JLabel userLabel = new JLabel("User");
        JTextField userText = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password");
        JPasswordField passwordText = new JPasswordField(20);
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        panel.add(userLabel);
        panel.add(userText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(loginButton);
        panel.add(registerButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());
                try {
                    loggedInUser = userService.loginUser(username, password);
                    frame.dispose();
                    createAndShowGUI();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Login failed: " + ex.getMessage());
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());
                String[] roles = {"admin", "user"};
                String role = (String) JOptionPane.showInputDialog(null, "Select role", "Role", JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);
                try {
                    userService.registerUser(username, password, role);
                    JOptionPane.showMessageDialog(null, "User registered successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Registration failed: " + ex.getMessage());
                }
            }
        });

        frame.setVisible(true);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Photo Ranking Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 700);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        frame.add(panel);

        // Layout settings
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Comment label
        JLabel commentLabel = new JLabel("Comment:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(commentLabel, gbc);

        // Comment text field
        commentText = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(commentText, gbc);

        // Add Comment button
        addCommentButton = new JButton("Add Comment");
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(addCommentButton, gbc);

        // Upload Photo button
        uploadPhotoButton = new JButton("Upload Photo");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(uploadPhotoButton, gbc);

        // Rate Photo button
        ratePhotoButton = new JButton("Rate Photo");
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(ratePhotoButton, gbc);

        // Rating label
        JLabel ratingLabel = new JLabel("Rating:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(ratingLabel, gbc);

        // Rating text field
        ratingText = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(ratingText, gbc);

        // Photo combo box
        photoComboBox = new JComboBox<>();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(photoComboBox, gbc);

        // Load Photo button
        JButton loadPhotoButton = new JButton("Load Photo");
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(loadPhotoButton, gbc);

        // Photo label
        photoLabel = new JLabel();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(photoLabel, gbc);

        // Average rating label
        averageRatingLabel = new JLabel("Average Rating: N/A");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.weighty = 0;
        panel.add(averageRatingLabel, gbc);

        // View count label
        viewCountLabel = new JLabel("View Count: N/A");
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(viewCountLabel, gbc);

        // Comments title label
        JLabel commentsTitleLabel = new JLabel("Comments:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(commentsTitleLabel, gbc);

        // Comments area
        commentsArea = new JTextArea();
        commentsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(commentsArea);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        panel.add(scrollPane, gbc);

        // Action listeners
        addCommentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String photoName = (String) photoComboBox.getSelectedItem();
                String comment = commentText.getText();
                try {
                    photoService.addComment(photoName, loggedInUser.getUsername(), comment);
                    JOptionPane.showMessageDialog(null, "Comment added successfully!");
                    updateComments(photoName);
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to add comment: " + ex.getMessage());
                }
            }
        });


        uploadPhotoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!loggedInUser.getRole().equals("admin")) {
                    JOptionPane.showMessageDialog(null, "Only admins can upload photos.");
                    return;
                }
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        byte[] data = Files.readAllBytes(selectedFile.toPath());
                        String photoName = selectedFile.getName();
                        photoService.uploadPhoto(photoName, data);
                        JOptionPane.showMessageDialog(null, "Photo uploaded successfully!");
                        updatePhotoComboBox();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Failed to upload photo: " + ex.getMessage());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Failed to upload photo: " + ex.getMessage());
                    }
                }
            }
        });

        loadPhotoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String photoName = (String) photoComboBox.getSelectedItem();
                try {
                    byte[] imageData = photoService.downloadPhoto(photoName);
                    ImageIcon imageIcon = new ImageIcon(scaleImage(imageData, PHOTO_WIDTH, PHOTO_HEIGHT));
                    photoLabel.setIcon(imageIcon);

                    double averageRating = photoService.getAverageRating(photoName);
                    int viewCount = photoService.getViewCount(photoName);

                    averageRatingLabel.setText("Average Rating: " + averageRating);
                    viewCountLabel.setText("View Count: " + viewCount);

                    updateComments(photoName);

                    if (!isFirstLoad) {
                        photoService.incrementViewCount(photoName);
                    }
                    isFirstLoad = false;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Failed to load photo: " + ex.getMessage());
                }
            }
        });

        ratePhotoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String photoName = (String) photoComboBox.getSelectedItem();
                int rating;
                try {
                    rating = Integer.parseInt(ratingText.getText());
                    if (rating < 1 || rating > 5) {
                        throw new NumberFormatException();
                    }
                    photoService.ratePhoto(photoName, rating);
                    JOptionPane.showMessageDialog(null, "Photo rated successfully!");

                    double averageRating = photoService.getAverageRating(photoName);
                    averageRatingLabel.setText("Average Rating: " + averageRating);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid rating. Please enter a number between 1 and 5.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Failed to rate photo: " + ex.getMessage());
                }
            }
        });

        updatePhotoComboBox();
        frame.setVisible(true);
    }

    private static void updatePhotoComboBox() {
        try {
            String[] photoNames = photoService.getPhotoNames();
            photoComboBox.removeAllItems();
            for (String photoName : photoNames) {
                photoComboBox.addItem(photoName);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Failed to fetch photo list: " + ex.getMessage());
        }
    }

    private static void updateComments(String photoName) {
        try {
            String[] comments = photoService.getComments(photoName);
            commentsArea.setText(String.join("\n", comments));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Failed to retrieve comments: " + ex.getMessage());
        }
    }

    private static Image scaleImage(byte[] imageData, int width, int height) {
        ImageIcon imageIcon = new ImageIcon(imageData);
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return scaledImage;
    }
}
