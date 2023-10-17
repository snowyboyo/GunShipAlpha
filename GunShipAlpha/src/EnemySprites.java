import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class EnemySprites {

    private BufferedImage SPRITE;

    public EnemySprites(String spritePath) {
        this.SPRITE = loadSprite(spritePath);
    }

    static BufferedImage loadSprite(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(EnemySprites.class.getResourceAsStream(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Rectangle getBounds(int x, int y) {
        return new Rectangle(x, y, SPRITE.getWidth(), SPRITE.getHeight());
    }

    public Point2D getFireOffset(Point location, Point target) {
        double directionX = target.x - location.x;
        double directionY = target.y - location.y;
        double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
        directionX /= magnitude;
        directionY /= magnitude;
        double offsetX = directionX * SPRITE.getWidth() / 2;
        double offsetY = directionY * SPRITE.getHeight() / 2;
        return new Point2D.Double(location.x + offsetX, location.y + offsetY);
    }

    public void draw(Graphics g, Point location) {
        g.drawImage(SPRITE, location.x, location.y, null);
    }
}
 class TankSprite extends EnemySprites {

     public TankSprite() {
         super("/Images/Tank.png");
     }

}
