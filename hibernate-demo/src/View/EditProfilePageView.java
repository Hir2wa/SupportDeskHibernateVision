package View;

import Controller.UserController;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class EditProfilePageView {

    private JFrame editFrame;
    private UserController userController;
    private User currentUser;

    public EditProfilePageView(User user, UserController controller) {
        this.currentUser = user;
        this.userController = controller;
        String username = user.getUsername();
        String email = user.getEmail();
    
        BufferedImage fallbackImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = fallbackImg.createGraphics();
        g2d.setColor(new Color(0, 102, 204));
        g2d.fillRect(0, 0, 100, 100);
        g2d.dispose();
        ImageIcon defaultPic = new ImageIcon(fallbackImg);
        
        
        try {
            File imageFile = new File("Assets/LogoSupportDesk.png");
            if (imageFile.exists()) {
                defaultPic = new ImageIcon(imageFile.getAbsolutePath());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
        ImageIcon finalProfilePic = defaultPic;
        editFrame = new JFrame("Edit Profile");
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.setSize(450, 500);
        editFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        JPanel editPanel = new JPanel(new GridBagLayout());
        editPanel.setBackground(Color.WHITE);
        editPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Edit Your Information",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16),
                Color.GRAY
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel profileLabel = new JLabel();
        profileLabel.setIcon(resizeAndRoundIcon(finalProfilePic, 100, 100));
        profileLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        editPanel.add(profileLabel, gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        editPanel.add(new JLabel("Username:"), gbc);

        JTextField usernameField = new JTextField(username, 20);
        gbc.gridx = 1;
        editPanel.add(usernameField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        editPanel.add(new JLabel("Email:"), gbc);

        JTextField emailField = new JTextField(email, 20);
        gbc.gridx = 1;
        editPanel.add(emailField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        editPanel.add(new JLabel("Password (optional):"), gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        editPanel.add(passwordField, gbc);
        JButton saveButton = new JButton("💾 Save Changes");
        saveButton.setBackground(new Color(0, 102, 204));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        editPanel.add(saveButton, gbc);
        mainPanel.add(editPanel, BorderLayout.CENTER);
        editFrame.setContentPane(mainPanel);
        editFrame.setVisible(true);
        saveButton.addActionListener(e -> {
            System.out.println("Save button clicked");
            
            // First, check if controller is null
            if (userController == null) {
                System.out.println("ERROR: UserController is null!");
                JOptionPane.showMessageDialog(editFrame, 
                    "Internal error: Controller not found.", 
                    "System Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if user is null or has invalid ID
            if (currentUser == null) {
                System.out.println("ERROR: Current user is null!");
                JOptionPane.showMessageDialog(editFrame, 
                    "Internal error: User data not found.", 
                    "System Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            System.out.println("Current user ID: " + currentUser.getId());
            
            // Check if the user ID is valid
            if (currentUser.getId() <= 0) {
                System.out.println("ERROR: Invalid user ID: " + currentUser.getId());
                JOptionPane.showMessageDialog(editFrame, 
                    "Cannot update profile: Invalid user ID", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String newUsername = usernameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newPassword = new String(passwordField.getPassword()).trim();
            
            if (newUsername.isEmpty() || newEmail.isEmpty()) {
                JOptionPane.showMessageDialog(editFrame, 
                    "Username and email cannot be empty!", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            System.out.println("Attempting to update user with ID: " + currentUser.getId());
            System.out.println("New username: " + newUsername);
            System.out.println("New email: " + newEmail);
            System.out.println("Password change requested: " + (!newPassword.isEmpty()));
            
            boolean updated = userController.updateUser(
                currentUser.getId(), 
                newUsername, 
                newEmail, 
                newPassword);
            
            System.out.println("Update result: " + (updated ? "SUCCESS" : "FAILED"));
                
            if (updated) {
                currentUser.setUsername(newUsername);
                currentUser.setEmail(newEmail);
                if (!newPassword.isEmpty()) {
                    currentUser.setPassword(newPassword);
                }
                JOptionPane.showMessageDialog(editFrame, 
                    "Profile updated successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                editFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(editFrame, 
                    "Failed to update profile. Please try again.", 
                    "Update Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    private ImageIcon resizeAndRoundIcon(ImageIcon originalIcon, int width, int height) {
        Image img = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage roundedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = roundedImage.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillRoundRect(0, 0, width, height, width, height);
        g2d.setComposite(AlphaComposite.SrcIn);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return new ImageIcon(roundedImage);
    }
  
}