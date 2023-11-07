package GSA;

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
    public boolean isCollidingWithPlayer(Player player) {
        int playerSize = 50;
        Rectangle playerBounds = new Rectangle(player.location.x, player.location.y, playerSize, playerSize);
        Rectangle particleBounds = new Rectangle(location.x, location.y, 15,15 );
        return particleBounds.intersects(playerBounds);
    }
    public boolean isCollidingWithLine(Line line) {
        return line.isIntersectingWithPoint(this.location.x, this.location.y);
    }

}