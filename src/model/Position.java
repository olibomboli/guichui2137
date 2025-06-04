package model;

// kontener na współrzędne
public class Position {
    public int row;
    public int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Position nextInDirection(Direction dir) { // sus poprawić
        switch (dir) {
            case UP:
                return new Position(row - 1, col);
            case DOWN:
                return new Position(row + 1, col);
            case LEFT:
                return new Position(row, col - 1);
            case RIGHT:
                return new Position(row, col + 1);
            default:
                throw new IllegalArgumentException();
        }
    }

    /* @Override
    public boolean equals(Object obj) { // sus
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position that = (Position) obj;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return 31 * row + col;
    } */

    public int getRow() { return row; }

    public int getCol() { return col; }
}
