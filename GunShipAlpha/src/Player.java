import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class Player {
    private int health = 10;
    private BufferedImage sprite;
    public Player(){
        loadSprite();
    }
    void loadSprite() {
        try {
            sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Images/GunShip.png")));
        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException("Failed to load sprite", e);
        }
    }

    public void reduceHealth(){
        health--;
    }
    public boolean isDead() {
        boolean dead = health <= 0;
        return dead;
    }
    public void draw(Graphics g, int canvasWidth, int canvasHeight) {
        int spriteX = (canvasWidth - sprite.getWidth()) / 2;
        int spriteY = (canvasHeight - sprite.getHeight()) / 2;
        g.drawImage(sprite, spriteX, spriteY, null);
    }
    public boolean isTouchedByEnemy(Enemy enemy) {
        Rectangle playerBounds = new Rectangle((800 - sprite.getWidth()) / 2,
                (800 - sprite.getHeight()) / 2,
                sprite.getWidth(),
                sprite.getHeight());
        Rectangle enemyBounds = enemy.getBounds();
        return playerBounds.intersects(enemyBounds);
    }
    public void checkAndHandleExplosion(Point explosionCenter, int radius) {
        if (isWithinRange(explosionCenter, radius)) {
            health -= 10;
        }
    }

    public boolean isWithinRange(Point explosionCenter, int radius) {
        Point playerLocation = new Point(350,350);
        double distance = playerLocation.distance(explosionCenter);
        return distance <= radius;
    }
}
