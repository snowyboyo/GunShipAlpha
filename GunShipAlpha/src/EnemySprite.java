import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EnemySprite {

    private static final String SPRITE_PATH = "src/Images/Behemoth.png";
    private static final BufferedImage SPRITE = loadSprite(SPRITE_PATH);

    private static BufferedImage loadSprite(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage sprite() {
        return SPRITE;
    }

    public static Rectangle getBounds(int x, int y) {
        return new Rectangle(x, y, SPRITE.getWidth(), SPRITE.getHeight());
    }

    public static boolean isPointInside(int x, int y, Point point) {
        Rectangle bounds = new Rectangle(x, y, SPRITE.getWidth(), SPRITE.getHeight());
        return bounds.contains(point);
    }
    public static Point2D getFireOffset(Point location, Point target) {
        double directionX = target.x - location.x;
        double directionY = target.y - location.y;
        double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
        directionX /= magnitude;
        directionY /= magnitude;
        double offsetX = directionX * SPRITE.getWidth() / 2;
        double offsetY = directionY * SPRITE.getHeight() / 2;
        return new Point2D.Double(location.x + offsetX, location.y + offsetY);
    }
    public static void draw(Graphics g, Point location) {
        g.drawImage(SPRITE, location.x, location.y, null);
    }
}
