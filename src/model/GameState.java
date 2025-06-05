package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class GameState {
    private BoardMap boardMap;
    private Player player;
    private int score;
    private final List<Ghost> ghosts = new ArrayList<>();
    private final List<PowerUp> powerUps = new ArrayList<>();
    private long doublePointsUntil = 0;
    private long ghostSlowUntil = 0;
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

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public void addPowerUp(PowerUp p) {
        powerUps.add(p);
    }

    public void removePowerUp(PowerUp p) {
        powerUps.remove(p);
    }

    public void addGhost(Ghost ghost) {
        ghosts.add(ghost);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getGhostMoveDelay() {
        if (System.currentTimeMillis() < ghostSlowUntil) {
            return 400;
        }
        return 200;
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
            ghost.move(ghost.nextDirection(boardMap), boardMap);
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
                    int points = isDoublePointsActive() ? 20 : 10;
                    score += points;
                    if (!boardMap.hasDotsRemaining()) {
                        gameOver = true;
                    }
                }

                Iterator<PowerUp> it = powerUps.iterator();
                while (it.hasNext()) {
                    PowerUp p = it.next();
                    Position pp = p.getPosition();
                    if (pp.getRow() == newPos.getRow() && pp.getCol() == newPos.getCol()) {
                        applyPowerUp(p);
                        it.remove();
                        break;
                    }
                }
            }
            checkCollisions();
            return moved;
        }
        return false;
    }

    private boolean isDoublePointsActive() {
        return System.currentTimeMillis() < doublePointsUntil;
    }

    private void applyPowerUp(PowerUp powerUp) {
        switch (powerUp.getType()) {
            case SPEED_BOOST -> player.speedUp(80, 5000);
            case GHOST_SLOW -> ghostSlowUntil = System.currentTimeMillis() + 5000;
            case DOUBLE_POINTS -> doublePointsUntil = System.currentTimeMillis() + 3000;
            case BONUS_POINTS -> score += 100;
            case INVINCIBILITY -> player.grantTemporaryInvincibility(3000);
        }
    }
}