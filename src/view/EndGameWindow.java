package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class EndGameWindow extends JFrame {
    public EndGameWindow(int score, Runnable onSubmit) {
        setTitle("Game Over");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel overLabel = new JLabel("Game Over! Score: " + score);
        overLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(overLabel);

        panel.add(Box.createVerticalStrut(10));

        JLabel prompt = new JLabel("Enter your name:");
        prompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(prompt);

        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        panel.add(nameField);

        JButton submit = new JButton("Submit");
        submit.setAlignmentX(Component.CENTER_ALIGNMENT);
        submit.addActionListener(e -> {
            if (onSubmit != null) {
                onSubmit.run();
            }
            dispose();
        });
        panel.add(Box.createVerticalStrut(10));
        panel.add(submit);

        add(panel);
        setVisible(true);
    }
}