import java.awt.*;

public class LineLogic {
        private Point start, end;
         private long creationTime;

        public LineLogic(Point start, Point end) {
            this.start = start;
            this.end = end;
            this.creationTime = System.currentTimeMillis();
        }

    public boolean intersects(Rectangle enemyBounds) {
        return enemyBounds.intersectsLine(start.x, start.y, end.x, end.y);
    }
    void draw(Graphics g) {
        g.drawLine(start.x, start.y, end.x, end.y);
    }
    boolean isExpired() {
        return Game.ms.isExpired(creationTime);
    }
}
