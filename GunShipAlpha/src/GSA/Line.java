package GSA;

import java.awt.*;

public class Line {
        private Point start, end;
         private long creationTime;
    private final long LINE_LIFETIME = 5000;

        public Line(Point start, Point end) {
            this.start = start;
            this.end = end;
            this.creationTime = System.currentTimeMillis();
        }

    public boolean intersects(Rectangle bounds) {
        return bounds.intersectsLine(start.x, start.y, end.x, end.y);
    }
    void draw(Graphics g) {
        g.drawLine(start.x, start.y, end.x, end.y);
    }
    public boolean isExpired() {
        return System.currentTimeMillis() - creationTime > LINE_LIFETIME;
    }
    public boolean isWithinExplosionRange(Point explosionCenter, int radius) {
        double distance1 = this.start.distance(explosionCenter);
        double distance2 = this.end.distance(explosionCenter);
        return distance1 <= radius || distance2 <= radius;
    }
    public boolean isIntersectingWithPoint(int x, int y) {
        return x >= Math.min(start.x, end.x)
                && x <= Math.max(start.x, end.x)
                && y >= Math.min(start.y, end.y)
                && y <= Math.max(start.y, end.y);
    }

}
