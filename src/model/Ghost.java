package model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Ghost extends Entity {
    private final BufferedImage sprite;
    private final Random random = new Random();

    public Ghost(Position position) {
            super(position);
            sprite = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = sprite.createGraphics();
            g2.setColor(Color.BLUE);
            g2.fillOval(0, 0, 30, 30);
            g2.dispose();
        }

        public BufferedImage getSprite() {
            return sprite;
        }

        public Direction getRandomDirection() {
            Direction[] directions = Direction.values();
            return directions[random.nextInt(directions.length)];
        }
    }