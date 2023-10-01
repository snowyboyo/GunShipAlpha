import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

class Enemy {
    private int x, y;
    private final int targetX, targetY;
    private int health = 2;
    private Timer firingTimer;
    public Enemy(int x, int y, int targetX, int targetY) {
        this.x = x;
        this.y = y;
        this.targetX = targetX;
        this.targetY = targetY;
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
        if (x < targetX) x++;
        if (x > targetX) x--;
        if (y < targetY) y++;
        if (y > targetY) y--;
    }

    private boolean hasEnemyReachedTarget() {
        if(x == targetX && y == targetY ) {
            health--;
            health--;
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
        Rectangle enemyBounds = new Rectangle(x, y, EnemySprite.width(), EnemySprite.height());
        return projectile.isInsideEnemy(enemyBounds);
    }
    public void draw(Graphics g) {
        g.drawImage(EnemySprite.sprite(), x, y, null);
    }
    public boolean isDead() {
        return health <= 0;
    }
    public boolean isIntersectingLine(LineLogic line) {
        Rectangle enemyBounds = createBounds();
        return line.intersects(enemyBounds);
    }

    private Rectangle createBounds() {
        return new Rectangle(x, y, EnemySprite.width(), EnemySprite.height());
    }
    public Projectile fireAtPlayer() {
        double targetX = 400;
        double targetY = 400;
        double directionX = targetX - this.x;
        double directionY = targetY - this.y;
        double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
        directionX /= magnitude;
        directionY /= magnitude;
        double offsetX = directionX * EnemySprite.width() / 2;
        double offsetY = directionY * EnemySprite.height() / 2;
        return new Projectile((int) (this.x + offsetX), (int) (this.y + offsetY), new Point2D.Double(directionX, directionY));
    }

}
