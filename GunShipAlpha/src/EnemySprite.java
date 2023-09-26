import javax.imageio.ImageIO;
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

    public static int width() {
        return SPRITE.getWidth();
    }

    public static int height() {
        return SPRITE.getHeight();
    }
}
