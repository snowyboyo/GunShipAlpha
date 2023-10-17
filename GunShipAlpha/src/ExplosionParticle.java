import java.awt.*;


public class ExplosionParticle {
    private final Point location;
    private final Point velocity;

    public ExplosionParticle(Point location, Point velocity) {
        this.location = location;
        this.velocity = velocity;
    }

    public void update() {
        location.translate(velocity.x, velocity.y);
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(location.x, location.y, 20, 20);
    }
}