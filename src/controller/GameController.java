package controller;

import model.*;
import view.EndGameWindow;
import view.GameWindow;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;

public class GameController {
    private GameBoardModel model;
    private GameState gameState;
    private GameWindow view;
    private Thread ghostThread;
    private volatile boolean running;

    public GameController(GameBoardModel model, GameWindow view, GameState gameState) {
        this.model = model;
        this.view = view;
        this.gameState = gameState;

        Player player = new Player(new Position(23, 13), 3);
        gameState.setPlayer(player);
        view.updateScore(gameState.getScore());
        view.updateHearts(player.getHearts());

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
                    gameState.getPlayer().setDirection(dir);
                    gameState.movePlayer(dir);
                    model.refresh();
                    view.updateScore(gameState.getScore());
                }
            }
        });
    }

    private void createGhosts() {
        gameState.addGhost(new Ghost(new Position(14, 13)));
        gameState.addGhost(new Ghost(new Position(14, 14)));
    }

    private void startGhostThread() {
        running = true;
        ghostThread = new Thread(() -> {
            while (running) {
                gameState.moveGhosts();
                model.refresh();
                view.updateScore(gameState.getScore());
                view.updateHearts(gameState.getPlayer().getHearts());

                if (gameState.isGameOver()) {
                    running = false;
                    SwingUtilities.invokeLater(this::endGame);
                    break;
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        ghostThread.start();
    }

    private void endGame() {
        view.setVisible(false);
        new EndGameWindow(gameState.getScore(), () -> {
            view.dispose();
        });
    }
}
