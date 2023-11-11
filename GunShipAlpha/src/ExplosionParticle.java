import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ExplosionParticle {
    private Point location;
    private Point velocity;

    public ExplosionParticle(Point location, Point velocity) {
        this.location = location;
        this.velocity = velocity;
    }

    public void update() {
        location.translate(velocity.x, velocity.y);
    }

    public void draw(Graphics g) {;
        g.setColor(Color.RED);
        g.fillRect(location.x, location.y, 20, 20);
    }
}