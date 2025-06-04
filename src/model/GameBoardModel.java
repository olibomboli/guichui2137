package model;

import javax.swing.table.AbstractTableModel;

// dostarcza dane do JTable
public class GameBoardModel extends AbstractTableModel {
    private GameState gameState;

    public GameBoardModel(GameState state) {
        this.gameState = state;
    }

    // informują JTable ile ma wierszy i kolumn
    @Override
    public int getRowCount() {
        return gameState.getBoardMap().getRows();
    }

    @Override
    public int getColumnCount() {
        return gameState.getBoardMap().getCols();
    }

    // zwraca do JTable obiekt, który ma być narysowany w danym miejscu planszy
    // tutaj jeśli jest w komórce pac - daje sprite, jeśli nie - daje tiletype
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Player player = gameState.getPlayer();
        if (player != null)  {
            Position position = player.getPosition();
            if (position.getRow() == rowIndex && position.getCol() == columnIndex) {
                return player.getSprite();
            }
        }
        return gameState.getBoardMap().getTileAt(rowIndex, columnIndex);
    }

    /* public GameState getState() {
        return gameState;
    } */

    public void refresh() {
        fireTableRowsUpdated(0, getRowCount() - 1);
    }
}