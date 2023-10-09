import java.awt.*;
        import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
        import java.util.Timer;
        import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

class Enemy {
    private Point location;
    private Point target;
    int health = 2;
    private Timer firingTimer;
    protected EnemySprites sprites;

    public Enemy(int x, int y, int targetX, int targetY) {
        this.location = new Point(x, y);
        this.target = new Point(targetX, targetY);
        this.sprites = new EnemySprites("src/Images/Behemoth.png");
        scheduleFiring();
    }
     void scheduleFiring() {
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
        long delay = random.nextInt(8000);
        firingTimer.schedule(firingTask, delay);
    }
    public void moveTowardTarget() {
        System.out.println(location.x + location.y);
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
            if (firingTimer != null) {
                firingTimer.cancel();
            }
        }
    }
    public boolean isCollidingWithProjectile(Projectile projectile) {
      Rectangle enemyBounds = EnemySprites.getBounds(location.x, location.y);
        return projectile.isInsideEnemy(enemyBounds);
    }
    public void draw(Graphics g) {
        sprites.draw(g, location);
    }
    public boolean isDead() {
        return health <= 0;
    }
    public boolean isIntersectingLine(Line line) {
        Rectangle enemyBounds = EnemySprites.getBounds(location.x, location.y);
        return line.intersects(enemyBounds);
    }
    public Projectile fireAtPlayer() {
        Point2D.Double directionVector = vectorBetween(location, target);
        Point2D firePosition = EnemySprites.getFireOffset(location, target);
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
class Tank extends Enemy {

    public Tank(int x, int y, int targetX, int targetY) {
        super(x, y, targetX, targetY);
        this.sprites = new TankSprite();
        this.health = 1;
    }
    void explode() {
        System.out.println("BOOM!");
    }
    public double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public boolean isIntersectingAnyLine(ArrayList<Line> lines) {
        for (Line line : lines) {
            if (this.isIntersectingLine(line)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCollidingWithAnyProjectile(CopyOnWriteArrayList<Projectile> projectiles) {
        for (Projectile projectile : projectiles) {
            if (this.isCollidingWithProjectile(projectile)) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected void scheduleFiring() {
        //prevent tanks from firing projectiles by overriding it with empty method
    }
}
