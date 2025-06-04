package controller;

import model.*;
import view.GameWindow;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController {
    private GameBoardModel model;
    private GameState gameState;
    private GameWindow view;

    public GameController(GameBoardModel model, GameWindow view, GameState gameState) {
        this.model = model;
        this.view = view;
        this.gameState = gameState;

        Player player = new Player(new Position(23, 13), 3);
        gameState.setPlayer(player);

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
}
