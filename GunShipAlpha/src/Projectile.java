import java.awt.*;
import java.awt.geom.Point2D;

class Projectile {
    private int x, y;
   private final double dx;
    private final double dy;
   private static final double SPEED = 5.0;

    public Projectile(int x, int y, Point2D direction) {
        this.x = x;
        this.y = y;
        double magnitude = Math.sqrt(direction.getX() * direction.getX() + direction.getY() * direction.getY());
        this.dx = SPEED * direction.getX() / magnitude;
        this.dy = SPEED * direction.getY() / magnitude;
    }

    public void update() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval((int)x-5, (int)y-5, 10, 10);
    }
    public boolean isOutOfBounds(int canvasWidth, int canvasHeight) {
        return x < 0 || x > canvasWidth || y < 0 || y > canvasHeight;
    }
    public boolean isInsideEnemy(Rectangle bounds) {
        return bounds.contains(x, y);
    }
    public boolean isCollidingWithPlayer() {
        Rectangle playerBounds = new Rectangle(350, 350, 100, 100);
        Rectangle projectileBounds = new Rectangle(x, y, 10,10 );
        return projectileBounds.intersects(playerBounds);
    }
}
