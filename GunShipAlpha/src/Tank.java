import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

class Tank {
    private Point location;
    private Point target;
    private int health = 2;
    private Timer firingTimer;

    public Tank(int x, int y, int targetX, int targetY) {
        this.location = new Point(x, y);
        this.target = new Point(targetX, targetY);
        scheduleFiring();
    }
    private void scheduleFiring() {
        if (firingTimer != null) {
            firingTimer.cancel();
        }

        firingTimer = new Timer();

        TimerTask firingTask = new TimerTask() {
            @Override
            public void run() {

                Projectile projectile = fireAtPlayer();
                Game.gs.addProjectile(projectile);
                scheduleFiring();
            }
        };
        Random random = new Random();
        long delay = 0 + random.nextInt(3000);
        firingTimer.schedule(firingTask, delay);
    }
    public void moveTowardTarget() {
        if (hasEnemyReachedTarget()) return;
        if (location.x < target.x) location.x++;
        if (location.x> target.x) location.x--;
        if (location.y < target.y) location.y++;
        if (location.y > target.y) location.y--;
    }

    private boolean hasEnemyReachedTarget() {
        if(location.x == target.x && location.y == target.y ) {
            health-=2;
            Game.gs.removeEnemy();
            return true;
        }
        return false;
    }

    void removeHealth() {
        health--;
        if (isDead()) {
            firingTimer.cancel();
        }
    }
    public boolean isCollidingWithProjectile(Projectile projectile) {
        Rectangle enemyBounds = BehemothSprite.getBounds(location.x, location.y);
        return projectile.isInsideEnemy(enemyBounds);
    }
    public void draw(Graphics g) {
        BehemothSprite.draw(g, location);
    }
    public boolean isDead() {
        return health <= 0;
    }
    public boolean isBehemothIntersectingLine(Line line) {
        Rectangle behemothBounds = BehemothSprite.getBounds(location.x, location.y);
        return line.intersects(behemothBounds);
    }
    public boolean isTankIntersectingLine(Line line) {
        Rectangle tankBounds = TankSprite.getBounds(location.x, location.y);
        return line.intersects(tankBounds);
    }
    public Projectile fireAtPlayer() {
        Point2D.Double directionVector = vectorBetween(location, target);
        Point2D firePosition = TankSprite.getFireOffset(location, target);
        return new Projectile((int) firePosition.getX(), (int) firePosition.getY(),
                directionVector);
    }

    public Point2D.Double vectorBetween(Point from, Point to) {
        double directionX = to.x - from.x;
        double directionY = to.y - from.y;
        double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
        return new Point2D.Double(directionX / magnitude, directionY / magnitude);
    }
    public Rectangle getBounds() {
        int width = 100;
        int height = 100;
        return new Rectangle(location.x, location.y, width, height);
    }

}
