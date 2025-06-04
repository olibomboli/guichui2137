package model;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Random;

public class Ghost extends Entity {
    private Direction direction;
    private Map<Direction, BufferedImage> sprites;
    private Random random;

    public Ghost(Position position, Map<Direction, BufferedImage> sprites) {
        super(position);
        this.sprites = sprites;
        this.direction = Direction.UP;
        this.random = new Random();
    }

    public BufferedImage getSprite() {
        return sprites.get(direction);
    }

    @Override
    public boolean move(Direction dir, BoardMap map) {
        this.direction = dir;
        Position nextP = position.nextInDirection(dir);
        TileType nextTile = map.getTileAt(nextP.getRow(), nextP.getCol());

        if (nextTile != TileType.WALL) {
            position = nextP;
            return true;
        }
        return false;
    }

    public Direction getRandomDirection() {
        Direction[] directions = Direction.values();
        return directions[random.nextInt(directions.length)];
    }
}
