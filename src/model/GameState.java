package model;

public class GameState {
    private BoardMap boardMap;
    private Player player;
    private int score;

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

    public boolean movePlayer(Direction direction) {
        if (player != null) {
            Position prevPos = player.getPosition();
            boolean moved = player.move(direction, boardMap);
            if (moved) {
                Position newPos = player.getPosition();
                TileType tile = boardMap.getTileAt(newPos.getRow(), newPos.getCol());
                if (tile == TileType.DOT) {
                    boardMap.setTileAt(newPos.getRow(), newPos.getCol(), TileType.EMPTY);
                    score += 10;
                }
            }
            return moved;
        }
        return false;
    }
}
