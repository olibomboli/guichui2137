package view;

import javax.swing.*;
import java.awt.*;

public class HighScoresWindow extends JFrame {

    public HighScoresWindow() {
        setTitle("High Scores: Pac-Man");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // CHECK
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        ImageIcon iconPac = new ImageIcon(getClass().getResource("/iconPac.png"));
        setIconImage(iconPac.getImage());

        Color backgroundColor = new Color(0xFFF9E6);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(backgroundColor);
        titlePanel.setPreferredSize(new Dimension(800, 180));

        ImageIcon highScoresTitle = scaleImage("/highScoresTitle.png", 500, 80);
        JLabel highScoresLabel = new JLabel(highScoresTitle);
        highScoresLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titlePanel.add(highScoresLabel, BorderLayout.CENTER);
        titlePanel.setPreferredSize(new Dimension(800, 200));

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel scoresPanel = new JPanel();
        JLabel placeholder = new JLabel("Tu będą wyniki");
        scoresPanel.setBackground(backgroundColor);
        scoresPanel.add(placeholder);
        add(scoresPanel, BorderLayout.CENTER);

        mainPanel.add(scoresPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private ImageIcon scaleImage(String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
