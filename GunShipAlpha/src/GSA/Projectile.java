package GSA;

import GSA.Line;

import java.awt.*;
import java.awt.geom.Point2D;

class Projectile {
    private Point location;
   private Point2D difference;
   private static final double SPEED = 2.0;
    private int lifespan;
    private int distanceTravelled = 0;
    protected boolean isPlayerProjectile;

    public Projectile(int x, int y, Point2D direction, boolean isPlayerProjectile) {
        this.lifespan = lifespan;
        this.location = new Point(x,y);
        double magnitude = Math.sqrt(direction.getX() * direction.getX() + direction.getY() * direction.getY());
        this.difference = new Point2D.Double(SPEED * direction.getX() / magnitude, SPEED * direction.getY() / magnitude);
        this.isPlayerProjectile = isPlayerProjectile;
    }

    public void update() {
        location.setLocation(location.x + difference.getX(), location.y + difference.getY());
    }

    public void draw(Graphics g) {
        if(isPlayerProjectile)g.setColor(Color.RED);
        else g.setColor(Color.BLACK);
        g.fillOval(location.x-5, location.y-5, 10, 10);
    }
    public boolean isOutOfBounds(int canvasWidth, int canvasHeight) {
        return location.x < 0 || location.x > canvasWidth ||location.y < 0 || location.y > canvasHeight;
    }
    public boolean isIntersected(Rectangle area) {
        int width = 10;
        int height = 10;
        Rectangle bounds = new Rectangle(location.x, location.y, width, height);

        return area.intersects(bounds);
    }
    public boolean isInsideEnemy(Rectangle bounds) {
        if(!isPlayerProjectile) return false;
        return bounds.contains(location.x, location.y);
    }
    public boolean isCollidingWithPlayer() {
        if (isPlayerProjectile) return false;
        Rectangle playerBounds = new Rectangle(850, 450, 100, 100);
        Rectangle projectileBounds = new Rectangle(location.x, location.y, 10,10 );
        return projectileBounds.intersects(playerBounds);
    }
    public boolean isCollidingWithLine(Line line) {
        if(isPlayerProjectile) return false;
        return line.isIntersectingWithPoint(this.location.x, this.location.y);
    }
}