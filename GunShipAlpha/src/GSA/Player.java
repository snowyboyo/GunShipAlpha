package GSA;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class Player {
    private int health = 10;
    private BufferedImage sprite;
    protected Point location;
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
    public void resetHealth(){health = 10;}
    public boolean isDead() {
        boolean dead = health <= 0;
        return dead;
    }
    public void draw(Graphics g) {
        g.drawImage(sprite, location.x, location.y, null);
    }
    public boolean isTouchedByEnemy(Tank enemy) {
        Rectangle playerBounds = new Rectangle(location.x, location.y,
                sprite.getWidth(),
                sprite.getHeight());
        Rectangle enemyBounds = enemy.getBounds();
        return playerBounds.intersects(enemyBounds);
    }
    protected void displayHealth(Graphics g){
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Health = "+health, 800, 24);
    }
}
