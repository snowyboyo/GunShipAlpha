import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {
    private int health = 10;
    private BufferedImage sprite;
    public Player(){
        loadSprite();
    }
    void loadSprite() {
        try {
            sprite = ImageIO.read(new File("GunShipAlpha/src/Images/GunShip.png"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load sprite", e);
        }
    }

    public void reduceHealth(){
        health--;
    }
    public boolean isDead(){
        boolean dead = health <= 0;
        return dead;
    }
    public void draw(Graphics g, int canvasWidth, int canvasHeight) {
        int spriteX = (canvasWidth - sprite.getWidth()) / 2;
        int spriteY = (canvasHeight - sprite.getHeight()) / 2;
        g.drawImage(sprite, spriteX, spriteY, null);
    }
    public boolean isTouchedByEnemy(Enemy enemy, int canvasWidth, int canvasHeight) {
        Rectangle playerBounds = new Rectangle((canvasWidth - sprite.getWidth()) / 2,
                (canvasHeight - sprite.getHeight()) / 2,
                sprite.getWidth(),
                sprite.getHeight());
        Rectangle enemyBounds = enemy.getBounds();
        return playerBounds.intersects(enemyBounds);
    }

    public void takeExplosionDamage() {
        health -= 5;
    }
}
