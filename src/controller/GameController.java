package controller;

import model.*;
import view.GameWindow;
import view.HighScoresWindow;
import view.EndGameWindow;
import java.util.Random;
import java.util.function.Consumer;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController {
    private GameBoardModel model;
    private GameState gameState;
    private GameWindow view;
    private Thread ghostThread;
    private volatile boolean running;
    private long startTime;
    private final Random random = new Random();
    private long lastDropTime = 0L;

    public GameController(GameBoardModel model, GameWindow view, GameState gameState) {
        this.model = model;
        this.view = view;
        this.gameState = gameState;
        this.startTime = 0L;

        Player player = new Player(new Position(23, 13), 3);
        gameState.setPlayer(player);
        view.updateScore(gameState.getScore());
        view.updateHearts(player.getHearts());
        view.updateTime(0);

        createGhosts();
        startGhostThread();
        keyListener();
    }

    private void keyListener() {
        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                Direction dir = switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> Direction.UP;
                    case KeyEvent.VK_DOWN -> Direction.DOWN;
                    case KeyEvent.VK_LEFT -> Direction.LEFT;
                    case KeyEvent.VK_RIGHT -> Direction.RIGHT;
                    default -> null;
                };

                if (dir != null) {
                    if (startTime == 0L) {
                        startTime = System.currentTimeMillis();
                    }
                    gameState.getPlayer().setDirection(dir);
                    gameState.movePlayer(dir);
                    model.refresh();
                    view.updateScore(gameState.getScore());
                }
            }
        });
    }

    private void createGhosts() {
        Ghost pinkGhost = new Ghost(new Position(13, 11), "ghostPink.png", 0);
        Ghost blueGhost = new Ghost(new Position(13, 15), "ghostBlue.png", 1);
        Ghost purpleGhost = new Ghost(new Position(15, 11), "ghostPurple.png", 2);
        Ghost mintGhost = new Ghost(new Position(15, 15), "ghostMint.png", 3);

        gameState.addGhost(pinkGhost);
        gameState.addGhost(blueGhost);
        gameState.addGhost(purpleGhost);
        gameState.addGhost(mintGhost);
    }

    private void startGhostThread() {
        running = true;
        ghostThread = new Thread(() -> {
            while (running) {
                gameState.moveGhosts();
                maybeSpawnPowerUp();
                SwingUtilities.invokeLater(() -> {
                    model.refresh();
                    view.updateScore(gameState.getScore());
                    view.updateHearts(gameState.getPlayer().getHearts());
                    if (startTime != 0L) {
                        view.updateTime(System.currentTimeMillis() - startTime);
                    }
                });

                if (gameState.isGameOver()) {
                    running = false;
                    SwingUtilities.invokeLater(this::endGame);
                    break;
                }

                try {
                    Thread.sleep(gameState.getGhostMoveDelay());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        ghostThread.start();
    }

    private void maybeSpawnPowerUp() {
        long now = System.currentTimeMillis();
        if (now - lastDropTime >= 5000) {
            lastDropTime = now;
            if (random.nextInt(100) < 25) {
                var ghosts = gameState.getGhosts();
                if (!ghosts.isEmpty()) {
                    Ghost g = ghosts.get(random.nextInt(ghosts.size()));
                    Position pos = new Position(g.getPosition().getRow(), g.getPosition().getCol());
                    PowerUpType[] types = PowerUpType.values();
                    PowerUpType type = types[random.nextInt(types.length)];
                    gameState.addPowerUp(new PowerUp(pos, type));
                }
            }
        }
    }

    private void endGame() {
        // Freeze the game view for a short moment so the prompt does not
        // immediately pop up in the player's face.
        Thread delayThread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
            SwingUtilities.invokeLater(() -> {
                view.setVisible(false);

                Consumer<String> submit = playerName -> {
                    String name = (playerName == null || playerName.isBlank()) ? "Player" : playerName;
                    HighScoresManager manager = new HighScoresManager();
                    manager.addScore(new ScoreEntry(name, gameState.getScore()));

                    view.dispose();
                };

                EndGameWindow window = new EndGameWindow(gameState.getScore(), submit);
                window.setVisible(true);
            });
        });
        delayThread.start();
    }
}