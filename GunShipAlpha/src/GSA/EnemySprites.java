package GSA;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class EnemySprites {

    private BufferedImage SPRITE;
    private static String defaultPath = "/Images/";

    public EnemySprites(String spritePath) {
        this.SPRITE = loadSprite(spritePath);
    }

    static BufferedImage loadSprite(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(EnemySprites.class.getResourceAsStream(defaultPath + path)));
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
        double offsetX = directionX * SPRITE.getWidth();
        double offsetY = directionY * SPRITE.getHeight();
        return new Point2D.Double(location.x + offsetX, location.y + offsetY);
    }
    public BufferedImage getSprite(){
        return SPRITE;
    }
}
 class MineSprite extends EnemySprites {

     public MineSprite() {
         super("Mine.png");
     }

}
class GinkerSprite extends EnemySprites {

    public GinkerSprite() {
        super("Ginker.png");
    }
}
class BehemothSprite extends EnemySprites {

    public BehemothSprite() {
        super("Behemoth.png");
    }
}
