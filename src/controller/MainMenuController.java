package controller;

import model.BoardMap;
import model.GameBoardModel;
import model.GameState;
import view.MainMenuWindow;
import view.GameWindow;
import view.HighScoresWindow;

public class MainMenuController {
    private MainMenuWindow window;

    public MainMenuController(MainMenuWindow window) {
        this.window = window;

        window.addNewGameListener(e -> startNewGame());
        window.addHighScoresListener(e -> showHighScores());
        window.addExitListener(e -> System.exit(0));
    }

    private void startNewGame() {
        window.setVisible(false);

        BoardMap map = new BoardMap();
        GameState state = new GameState(map);
        GameBoardModel model = new GameBoardModel(state);
        GameWindow gameWindow =  new GameWindow(model, state);
        new GameController(model, gameWindow, state);

        gameWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                   window.setVisible(true);
            }
        });
        gameWindow.setVisible(true);
    }

    private void showHighScores() {
        window.setVisible(false);

        HighScoresWindow scoresWindow = new HighScoresWindow();
        scoresWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                window.setVisible(true);
            }
        });
        scoresWindow.setVisible(true);
    }
}