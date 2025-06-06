package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import model.GameBoardModel;
import model.GameState;
import model.TileType;

public class GameWindow extends JFrame {
    private GameBoardModel boardModel;
    private GameState gameState;
    private JTable gameTable;
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private JPanel heartsPanel;
    private ImageIcon heartIcon;

    public GameWindow(GameBoardModel boardModel, GameState gameState) {
        this.boardModel = boardModel;
        this.gameState = gameState;
        init();
        gameTable.setFocusable(false); //potrzebne, żeby keyListener zadziałał
        setFocusable(true);
        requestFocusInWindow();
    }

    private void init() {
        setTitle("Play: Pac-Man");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 1100);
        setLocationRelativeTo(null);

        ImageIcon iconPac = new ImageIcon(getClass().getResource("/iconPac.png"));
        setIconImage(iconPac.getImage());

        Color backgroundColor = new Color(0xFFF9E6);
        getContentPane().setBackground(backgroundColor);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(backgroundColor);
        scoreLabel = new JLabel();
        scoreLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        scoreLabel.setForeground(new Color(0xF08080));
        topPanel.add(scoreLabel);

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        timeLabel.setForeground(new Color(0xF08080));
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(timeLabel);
        add(topPanel, BorderLayout.NORTH);

        gameTable = new JTable(boardModel);
        setupTable();
        configureTableSize();

        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(backgroundColor);
        gamePanel.add(gameTable, BorderLayout.CENTER);
        add(gamePanel, BorderLayout.CENTER);

        loadHeartIcon();
        heartsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        heartsPanel.setBackground(backgroundColor);
        add(heartsPanel, BorderLayout.SOUTH);
    }

    private void setupTable() {
        gameTable.setRowSelectionAllowed(false);
        gameTable.setColumnSelectionAllowed(false);
        gameTable.setTableHeader(null);
        gameTable.setShowGrid(false);
        gameTable.setIntercellSpacing(new Dimension(0, 0));
        gameTable.setDefaultRenderer(Object.class, new GameCellRenderer());
    }

    private void configureTableSize() {
        int tileSize = 30;
        gameTable.setRowHeight(tileSize);

        int cols = boardModel.getColumnCount();
        for (int col = 0; col < cols; col++) {
            gameTable.getColumnModel().getColumn(col).setPreferredWidth(tileSize);
        }
    }

    private class GameCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            label.setText("");

            if (value instanceof BufferedImage image) {
                label.setIcon(new ImageIcon(image));
                label.setBackground(new Color(0xFFF9E6));
            } else if (value instanceof TileType tile) {
                label.setIcon(null);

                switch (tile) {
                    case WALL -> label.setBackground(new Color(0xF08080));
                    case DOT -> {
                        return new DotPanel();
                    }
                    case EMPTY -> label.setBackground(new Color(0xFFF9E6));
                    default -> label.setBackground(new Color(0xFFF9E6));
                }
            } else {
                label.setIcon(null);
                label.setBackground(Color.PINK);
            }
            return label;
        }
    }

    private static class DotPanel extends JPanel {
        public DotPanel() {
            setBackground(new Color(0xFFF9E6));
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(0xF8AD9D));
            int size = Math.min(getWidth(), getHeight()) / 3;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;
            g.fillOval(x, y, size, size);
        }
    }

    public JTable getGameTable() {
        return gameTable;
    }

    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    public void updateTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long sec = seconds % 60;
        timeLabel.setText(String.format("Time: %02d:%02d", minutes, sec));
    }

    public void updateHearts(int hearts) {
        heartsPanel.removeAll();
        for (int i = 0; i < hearts; i++) {
            if (heartIcon != null) {
                heartsPanel.add(new JLabel(heartIcon));
            } else {
                JLabel heart = new JLabel("\u2764");
                heart.setFont(new Font("SansSerif", Font.BOLD, 24));
                heart.setForeground(Color.PINK);
                heartsPanel.add(heart);
            }
        }
        heartsPanel.revalidate();
        heartsPanel.repaint();
    }

    private void loadHeartIcon() {
        try {
            Image img = ImageIO.read(getClass().getResource("/lifeHeart.png"));
            heartIcon = new ImageIcon(img.getScaledInstance(24, 24, Image.SCALE_SMOOTH));
        } catch (IOException | IllegalArgumentException e) {
            heartIcon = null;
        }
    }
}