import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ExplosionParticle {
    private BufferedImage spriteImage;
    private Point location;
    private Point velocity;

    public ExplosionParticle(Point location, Point velocity) {
        this.location = location;
        this.velocity = velocity;
        try {
            spriteImage = ImageIO.read(new File("GunShipAlpha/src/Images/Drawing.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        location.translate(velocity.x, velocity.y);
    }

    public void draw(Graphics g) {
        System.out.println("Drawing Explosion");
            g.drawImage(spriteImage, location.x, location.y, null);
    }
}