import java.awt.*;
import java.awt.geom.Point2D;

class Projectile {
    private Point location;
   private Point2D difference;
   private static final double SPEED = 5.0;
    private int lifespan;
    private int distanceTravelled = 0;

    public Projectile(int x, int y, Point2D direction) {
        this.lifespan = lifespan;
        this.location = new Point(x,y);
        double magnitude = Math.sqrt(direction.getX() * direction.getX() + direction.getY() * direction.getY());
        this.difference = new Point2D.Double(SPEED * direction.getX() / magnitude, SPEED * direction.getY() / magnitude);
    }

    public void update() {
        location.setLocation(location.x + difference.getX(), location.y + difference.getY());
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(location.x-5, location.y-5, 10, 10);
    }
    public boolean isOutOfBounds(int canvasWidth, int canvasHeight) {
        return location.x < 0 || location.x > canvasWidth ||location.y < 0 || location.y > canvasHeight;
    }
    public boolean isInsideEnemy(Rectangle bounds) {
        return bounds.contains(location.x, location.y);
    }
    public boolean isCollidingWithPlayer() {
        Rectangle playerBounds = new Rectangle(350, 350, 100, 100);
        Rectangle projectileBounds = new Rectangle(location.x, location.y, 10,10 );
        return projectileBounds.intersects(playerBounds);
    }
}
