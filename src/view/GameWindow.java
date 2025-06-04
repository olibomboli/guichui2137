package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;

import model.GameBoardModel;
import model.GameState;
import model.TileType;

public class GameWindow extends JFrame {
    private GameBoardModel boardModel;
    private GameState gameState;
    private JTable gameTable;
    private JLabel scoreLabel;
    private JPanel heartsPanel;

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
        setSize(900, 1000);
        setLocationRelativeTo(null);
        // setResizable(true);

        ImageIcon iconPac = new ImageIcon(getClass().getResource("/iconPac.png"));
        setIconImage(iconPac.getImage());

        Color backgroundColor = new Color(0xFFF9E6);
        getContentPane().setBackground(backgroundColor);
        setLayout(new BorderLayout());

        //TODO: TIMER NA TOP PANELU
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(backgroundColor);
        scoreLabel = new JLabel(); // ??????????
        scoreLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        scoreLabel.setForeground(new Color(0xF08080));
        topPanel.add(scoreLabel);
        add(topPanel, BorderLayout.NORTH);

        gameTable = new JTable(boardModel);
        setupTable();
        configureTableSize();

        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(backgroundColor);
        gamePanel.add(gameTable, BorderLayout.CENTER);
        add(gamePanel, BorderLayout.CENTER);

        heartsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        heartsPanel.setBackground(backgroundColor);
        add(heartsPanel, BorderLayout.SOUTH);
    }

    private void setupTable() {
        gameTable.setRowSelectionAllowed(false);
        gameTable.setColumnSelectionAllowed(false);
        gameTable.setTableHeader(null);
        gameTable.setGridColor(new Color(0xF4978E));
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

    public void updateHearts(int hearts) {
        heartsPanel.removeAll();
        for (int i = 0; i < hearts; i++) {
            JLabel heart = new JLabel("\u2764");
            heart.setFont(new Font("SansSerif", Font.BOLD, 24));
            heart.setForeground(Color.RED);
            heartsPanel.add(heart);
        }
        heartsPanel.revalidate();
        heartsPanel.repaint();
    }
}