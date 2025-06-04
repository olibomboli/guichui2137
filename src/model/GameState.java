package model;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private BoardMap boardMap;
    private Player player;
    private int score;
    private final List<Ghost> ghosts = new ArrayList<>();
    private boolean gameOver = false;

    public GameState(BoardMap boardMap) {
        this.boardMap = boardMap;
        this.score = 0;
    }

    // wrzucam gracza na mapę po jego stworzeniu przez GameController
    public void setPlayer(Player player) {
        this.player = player;
    }

    //zwracm stan pacmana
    public Player getPlayer() {
        return player;
    }

    // uzywany przez GameWindow do wyświetlania wyniku
    public int getScore() {
        return score;
    }

    public BoardMap getBoardMap() {
        return boardMap;
    }

    public List<Ghost> getGhosts() {
        return ghosts;
    }

    public void addGhost(Ghost ghost) {
        ghosts.add(ghost);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private void checkCollisions() {
        if (player == null) return;
        for (Ghost ghost : ghosts) {
            if (ghost.getPosition().getRow() == player.getPosition().getRow() &&
                    ghost.getPosition().getCol() == player.getPosition().getCol()) {
                if (!player.isInvincible()) {
                    player.loseHeart();
                    player.makeInvincible(2000);
                    if (player.getHearts() <= 0) {
                        gameOver = true;
                    }
                }
            }
        }
    }

    public void moveGhosts() {
        for (Ghost ghost : ghosts) {
            ghost.move(ghost.getRandomDirection(), boardMap);
        }
        checkCollisions();
    }

    public boolean movePlayer(Direction direction) {
        if (player != null) {
            if (!player.canMove()) {
                return false;
            }
            boolean moved = player.move(direction, boardMap);
            if (moved) {
                Position newPos = player.getPosition();
                TileType tile = boardMap.getTileAt(newPos.getRow(), newPos.getCol());
                if (tile == TileType.DOT) {
                    boardMap.setTileAt(newPos.getRow(), newPos.getCol(), TileType.EMPTY);
                    score += 10;
                }
            }
            checkCollisions();
            return moved;
        }
        return false;
    }
}
