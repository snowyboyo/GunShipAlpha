package GSA;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class Player {
    private int health = 10;
    private BufferedImage sprite;
    protected Point location;
    private double angle;
    public Player(int x,int y){
        this.location = new Point(x,y);
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
    public void resetLocation(){
        location = new  Point(850,450);
    }

    public void resetHealth(){health = 10;}
    public boolean isDead() {
        boolean dead = health <= 0;
        return dead;
    }
    public void updateAngle(Point cursorPosition) {
        int centerX = location.x + sprite.getWidth() / 2;
        int centerY = location.y + sprite.getHeight() / 2;
        angle = Math.atan2(cursorPosition.y - centerY, cursorPosition.x - centerX) - Math.toRadians(-90);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        int spriteCenterX = location.x + sprite.getWidth() / 2;
        int spriteCenterY = location.y + sprite.getHeight() / 2;
        AffineTransform at = new AffineTransform();
        at.translate(spriteCenterX, spriteCenterY);
        at.rotate(angle);
        at.translate(-sprite.getWidth() / 2, -sprite.getHeight() / 2);
        g2d.drawImage(sprite, at, null);
        g2d.dispose();
    }
    protected void playerHit(Game game){
        game.playerHit();
    }
    public boolean isTouchedByEnemy(Enemy enemy) {
        Rectangle playerBounds = new Rectangle(location.x, location.y,
                sprite.getWidth(),
                sprite.getHeight());
        Rectangle enemyBounds = enemy.getBounds();
        if (playerBounds.intersects(enemyBounds)){

        }
        return playerBounds.intersects(enemyBounds);
    }
    protected void displayHealth(Graphics g){
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Health = "+health, 800, 24);
    }
}
