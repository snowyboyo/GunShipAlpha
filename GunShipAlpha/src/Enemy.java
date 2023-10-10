import java.awt.*;
        import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
        import java.util.Timer;
        import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

class Enemy {
    Point location;
    private Point target;
    int health = 2;
    private Timer firingTimer;
    protected EnemySprites sprites;

    public Enemy(int x, int y, int targetX, int targetY) {
        this.location = new Point(x, y);
        this.target = new Point(targetX, targetY);
        this.sprites = new EnemySprites("GunShipAlpha/src/Images/Behemoth.png");
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
      Rectangle enemyBounds = sprites.getBounds(location.x, location.y);
        return projectile.isInsideEnemy(enemyBounds);
    }
    public void draw(Graphics g) {
        sprites.draw(g, location);
    }
    public boolean isDead() {
        return health <= 0;
    }
    public boolean isIntersectingLine(Line line) {
        Rectangle enemyBounds = sprites.getBounds(location.x, location.y);
        return line.intersects(enemyBounds);
    }
    public Projectile fireAtPlayer() {
        Point2D.Double directionVector = vectorBetween(location, target);
        Point2D firePosition = sprites.getFireOffset(location, target);
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
    private ArrayList<ExplosionParticle> explosionParticles = new ArrayList<>();
    private Random random = new Random();

    public Tank(int x, int y, int targetX, int targetY) {
        super(x, y, targetX, targetY);
        this.sprites = new TankSprite();
        this.health = 1;
    }
    void explode(ArrayList<Line> lines, CopyOnWriteArrayList<Projectile> projectiles, Player player, int canvasWidth, int canvasHeight) {
        System.out.println("BOOM!");
        createExplosionParticles();
        Rectangle explosionArea = createExplosionArea();
        Rectangle playerBounds = new Rectangle(350, 350, 100, 100);
        if (explosionArea.intersects(playerBounds)){
            player.takeExplosionDamage();
        }
        projectiles.removeIf(projectile -> projectile.isIntersected(explosionArea));
        lines.removeIf(line -> line.intersects(explosionArea));
        removeHealth();
    }

    private Rectangle createExplosionArea() {
        int explosionSize = 300;
        Rectangle explosionArea = new Rectangle(
                location.x - explosionSize / 2,
                location.y - explosionSize / 2,
                explosionSize,
                explosionSize);
        return explosionArea;
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
        //preventing tanks from firing projectiles by overriding it with empty method
    }
    private void createExplosionParticles() {
        for (int i = 0; i < 50; i++) {
            int velocityX = random.nextInt(10) - 5;
            int velocityY = random.nextInt(10) - 5;
            System.out.println("Creating particle " + i + " with velocity (" + velocityX + "," + velocityY + ")");
            explosionParticles.add(new ExplosionParticle(new Point(location.x, location.y), new Point(velocityX, velocityY)));
        }
        System.out.println("Explosion particles created: " + explosionParticles.size());
    }
    public void updateExplosionParticles() {
        for (int i = 0; i < explosionParticles.size(); i++) {
            explosionParticles.get(i).update();
        }
    }

    public void drawExplosionParticles(Graphics g) {
        System.out.println(explosionParticles.size());
        for (int i = 0; i < explosionParticles.size(); i++) {
            ExplosionParticle p = explosionParticles.get(i);
            if (p != null) {
                p.draw(g);
            }
        }
    }
}
